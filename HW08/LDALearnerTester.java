import junit.framework.TestCase;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */
public class LDALearnerTester extends TestCase {
  
  /**
   * A test method.
   * (Replace "X" with a name describing the test.  You may write as
   * many "testSomething" methods in this class as you wish, and each
   * one will be called when running JUnit over this class.)
   */
  
  public void loadUpLearner (ILDALearner myLearner, long dataGenSeed, int numWords, int numTopics, int numDocs, int docLen) {
    
    // this is used to generate the random data
    IPRNG myPRNG = new PRNG (dataGenSeed);
    
    // this is the true word_prob matrix, and the initial one
    IDoubleMatrix trueWordProbs = new RowMajorDoubleMatrix (numTopics, numWords, 0.0);
    IDoubleMatrix initialWordProbs = new RowMajorDoubleMatrix (numTopics, numWords, 0.0);
    
    try {
      // and load it up with data... there is no overalap among topics here
      for (int i = 0; i < numTopics; i++) {
        IDoubleVector myWordProbs = new SparseDoubleVector (numWords, 0.02);
        int mult = numWords / numTopics;
        for (int j = i * mult; j < (i + 1) * mult; j++) {
          myWordProbs.setItem (j, 1.0); 
        }
        myWordProbs.normalize ();
        trueWordProbs.setRow (i, myWordProbs);
        
        // choose an initial word_probs vector
        Dirichlet myDirichlet = new Dirichlet (myPRNG, new DirichletParam (new SparseDoubleVector (numWords, 1), 10e-99, 1000));
        IDoubleVector initialProbs = myDirichlet.getNext ();
        myWordProbs.addMyselfToHim (initialProbs);
        initialProbs.normalize ();
        initialWordProbs.setRow (i, initialProbs);
      }
      
      // load up the initial word_probs matrix
      myLearner.loadUpWordProbs (initialWordProbs);
      
      // now we create the docs
      for (int i = 0; i < numDocs; i++) {
        
        // get the words in the doc
        Multinomial myGenerator = new Multinomial (myPRNG, new MultinomialParam (docLen, trueWordProbs.getRow (i % numTopics)));
        IDoubleVector wordsInDoc = myGenerator.getNext ();
        
        // choose an initial topic_probs vector
        Dirichlet myDirichlet = new Dirichlet (myPRNG, new DirichletParam (new SparseDoubleVector (numTopics, 1), 10e-99, 1000));
        IDoubleVector initialTopicProbs = myDirichlet.getNext ();
        myLearner.loadUpAnotherDoc (i + "", wordsInDoc, initialTopicProbs);
      }
    } catch (Exception e) {
      e.printStackTrace ();
      fail ("Got an exception when trying to load data into the LDA object");
    }
  }
  
  public void checkSerializaton (int numWords, int numTopics, int numDocs, int docLen) {
    
    IPRNG hisPRNG = new PRNG (56);
    IDoubleVector hisAlpha = new SparseDoubleVector (numWords, 0.001);
    IDoubleVector hisBeta = new SparseDoubleVector (numTopics, 0.001);
    
    // create the learner
    ILDALearner hisLearnerOne = new LDALearner (hisAlpha, hisBeta, hisPRNG); //HAHAHA
    ILDALearner hisLearnerTwo = new LDALearner (hisAlpha, hisBeta, hisPRNG); //HAHAHA
    
    // run an interation of the learner
    loadUpLearner (hisLearnerOne, 12, numWords, numTopics, numDocs, docLen);
    hisLearnerOne.updateProduced ();
    hisLearnerOne.updateTopicProbs ();
    hisLearnerOne.updateWordProbs ();
    
    // write out the results
    hisLearnerOne.writeToFile ("file.xml");
    
    // read the results into another learner
    hisLearnerTwo.loadFromFile ("file.xml");
    
    // now verify that they match up
    // now, check to see if any of the results are an extreme and add his stuff to the average
    try {
   
      int counter = 0;
      
      // see if we got a new extreme result on a topic prob
      for (DocSignature i : hisLearnerOne) {
        
        for (int j = 0; j < numTopics; j++) {
          double two = hisLearnerTwo.getSignature (i.getFName ()).getTopicProbs ().getItem (j);
          double one = i.getTopicProbs ().getItem (j);
          
          if (Math.abs (one - two) > 0.000000001) {
            fail ("when re-reading from file, doc " + i.getFName () + " topic prob " + j + " did not match"); 
          }
        }
        
        for (int j = 0; j < numWords; j++) {
          long two = hisLearnerTwo.getSignature (i.getFName ()).getWordsInDoc ().getRoundedItem (j);
          long one = i.getWordsInDoc ().getRoundedItem (j); 
          
          if (one != two) {
            fail ("when re-reading from file, doc " + i.getFName () + " word count " + j + " did not match"); 
          }
        }

        counter++;
      }
      
      IDoubleMatrix one = hisLearnerOne.getWordProbs ();
      IDoubleMatrix two = hisLearnerTwo.getWordProbs ();
      
      // see if they match
      for (int i = 0; i < numTopics; i++) {
        for (int j = 0; j < numWords; j++) {
          if (Math.abs(one.getEntry (i, j) - two.getEntry (i, j)) > 0.00000001) {
            fail ("when re-reading from file, topic " + i + " word " + j + " prob not match");             
          }
        }
      }
  
    } catch (Exception e) {
      fail ("Got an exception.  Possibly I am getting bad vector lengths back from the learner?");
    }
    
    // finally, try to run another iteration
    hisLearnerTwo.updateProduced ();
    hisLearnerTwo.updateTopicProbs ();
    hisLearnerTwo.updateWordProbs ();
  }
  
  
  public void checkAllUpdates (int numWords, int numTopics, int numDocs, int docLen, int numItersToRun, int maxErrs) {
    
    // create the PRNGs
    IPRNG myPRNG = new PRNG (419);
    IPRNG hisPRNG = new PRNG (56);
    
    // create the alpha and beta vectors
    IDoubleVector myAlpha = new SparseDoubleVector (numWords, 0.001);
    IDoubleVector myBeta = new SparseDoubleVector (numTopics, 0.001);
    IDoubleVector hisAlpha = new SparseDoubleVector (numWords, 0.001);
    IDoubleVector hisBeta = new SparseDoubleVector (numTopics, 0.001);
    
    // create the learner
    ILDALearner myLearner = new SCLDALearner (myAlpha, myBeta, myPRNG);
    ILDALearner hisLearner = new LDALearner (hisAlpha, hisBeta, hisPRNG); //HAHAHA
    
    // set up the low bound, the high bound, and the average for the observed topic probs
    IDoubleMatrix topic_probsLOW = new RowMajorDoubleMatrix (numDocs, numTopics, 1.0);
    IDoubleMatrix topic_probsHIGH = new RowMajorDoubleMatrix (numDocs, numTopics, 0.0);
    IDoubleMatrix topic_probsAVG = new RowMajorDoubleMatrix (numDocs, numTopics, 0.0);
    IDoubleMatrix word_probsLOW = new RowMajorDoubleMatrix (numTopics, numWords, 1.0);
    IDoubleMatrix word_probsHIGH = new RowMajorDoubleMatrix (numTopics, numWords, 0.0);
    IDoubleMatrix word_probsAVG = new RowMajorDoubleMatrix (numTopics, numWords, 0.0);
    
    try {
      for (int i = 0; i < numDocs; i++) {
        topic_probsLOW.setRow (i, new DenseDoubleVector (numTopics, 1.0));
        topic_probsHIGH.setRow (i, new DenseDoubleVector (numTopics, 0.0));
        topic_probsAVG.setRow (i, new DenseDoubleVector (numTopics, 0.0));
      }
      
      for (int i = 0; i < numTopics; i++) {
        word_probsLOW.setRow (i, new DenseDoubleVector (numWords, 1.0));
        word_probsHIGH.setRow (i, new DenseDoubleVector (numWords, 0.0));
        word_probsAVG.setRow (i, new DenseDoubleVector (numWords, 0.0));
      }
      
    } catch (Exception e) {
      System.out.println ("Error in test case code?"); 
    }
    
    // now, use the Chris implementation to set the low and high bounds over 50 tries
    System.out.println ("Checking each learner 50 times");
    for (int iter = 0; iter < 50; iter++) {
      
      System.out.format ("Check " + iter + " ");
      
      // run some iterations of the learner
      loadUpLearner (myLearner, 12, numWords, numTopics, numDocs, docLen);
      loadUpLearner (hisLearner, 12, numWords, numTopics, numDocs, docLen);
      
      for (int i = 0; i < numItersToRun; i++) {
        myLearner.updateProduced ();
        myLearner.updateTopicProbs ();
        myLearner.updateWordProbs ();
        hisLearner.updateProduced ();
        hisLearner.updateTopicProbs ();
        hisLearner.updateWordProbs ();
      }
      
      // now, check to see if any of the results are an extreme and add his stuff to the average
      try {
        
        // see if we got a new extreme result on a topic prob
        for (DocSignature i : myLearner) {
          
          for (int j = 0; j < numTopics; j++) {
            if (i.getTopicProbs ().getItem (j) > topic_probsHIGH.getEntry (Integer.parseInt (i.getFName ()), j)) {
              topic_probsHIGH.setEntry (Integer.parseInt (i.getFName ()), j, i.getTopicProbs ().getItem (j));
            }
            if (i.getTopicProbs ().getItem (j) < topic_probsLOW.getEntry (Integer.parseInt (i.getFName ()), j)) {
              topic_probsLOW.setEntry (Integer.parseInt (i.getFName ()), j, i.getTopicProbs ().getItem (j));
            }
          }
        }
        
        IDoubleMatrix wordProbs = myLearner.getWordProbs ();
        
        // see if we got a new extreme result on a word prob
        for (int i = 0; i < numTopics; i++) {
          for (int j = 0; j < numWords; j++) {
            if (wordProbs.getEntry (i, j) > word_probsHIGH.getEntry (i, j)) {
              word_probsHIGH.setEntry (i, j, wordProbs.getEntry (i, j));
            }
            if (wordProbs.getEntry (i, j) < word_probsLOW.getEntry (i, j)) {
              word_probsLOW.setEntry (i, j, wordProbs.getEntry (i, j));
            }
          }
        }
        
        // add his result into the average over all topic probs
        for (DocSignature i : hisLearner) {
          i.getTopicProbs ().addMyselfToHim (topic_probsAVG.getRow (Integer.parseInt (i.getFName ())));
        }
        
        // add his result into the average over all word probs
        wordProbs = hisLearner.getWordProbs ();
        for (int i = 0; i < numTopics; i++) {
          wordProbs.getRow (i).addMyselfToHim (word_probsAVG.getRow (i)); 
        }
        
      } catch (Exception e) {
        e.printStackTrace ();
        fail ("Got an exception.  Possibly I am getting bad vector lengths back from the learner?");
      }
    }
    
    // now, verify that everything matches up
    int errs = 0;
    try {
      System.out.println ("Checking that observed average over all of the topic_probs vectors looks OK");
      for (int i = 0; i < numDocs; i++) {
        topic_probsAVG.getRow (i).normalize ();
        for (int j = 0; j < numTopics; j++) {
          if (topic_probsAVG.getEntry (i, j) < topic_probsLOW.getEntry (i, j) ||
              topic_probsAVG.getEntry (i, j) > topic_probsHIGH.getEntry (i, j)) {
            System.out.println ("When testing topic probs, for doc " + i + " topic " + j + " I got an unreasonable average probability (" 
                    + topic_probsLOW.getEntry (i, j) + " " + topic_probsAVG.getEntry (i, j) + " " +
                                topic_probsHIGH.getEntry (i, j) + ")");
            errs++;
          }
        }
      }
      
      for (int i = 0; i < numTopics; i++) {
        word_probsAVG.getRow (i).normalize ();
        for (int j = 0; j < numWords; j++) {
          if (i == 0) {
          }
          if (word_probsAVG.getEntry (i, j) < word_probsLOW.getEntry (i, j) ||
              word_probsAVG.getEntry (i, j) > word_probsHIGH.getEntry (i, j)) {
            System.out.println ("When testing word probs, for topic " + i + " word " + j + " I got an unreasonable average probability (" 
                    + word_probsLOW.getEntry (i, j) + " " + word_probsAVG.getEntry (i, j) + " " +
                                word_probsHIGH.getEntry (i, j) + ")");
            errs++;
          }
        }
      }
    } catch (Exception e) {
      fail ("Got an exception when I was trying to verify the correctness of the average");
    }
    if (errs > maxErrs) {
      fail ("Got " + errs + " bad vals in the test.  Max allowed was " + maxErrs); 
    }
  }
  
  public void checkProduceAndTopicProbUpdate (boolean doProducedOneAtATime,
                boolean useIterator, int numWords, int numTopics, int numDocs, int docLen, int maxErrs) {
  
    // create the PRNGs
    IPRNG myPRNG = new PRNG (276);
    IPRNG hisPRNG = new PRNG (890);
    
    // create the alpha and beta vectors
    IDoubleVector myAlpha = new SparseDoubleVector (numWords, 0.001);
    IDoubleVector myBeta = new SparseDoubleVector (numTopics, 0.001);
    IDoubleVector hisAlpha = new SparseDoubleVector (numWords, 0.001);
    IDoubleVector hisBeta = new SparseDoubleVector (numTopics, 0.001);
    
    // create the learner
    ILDALearner myLearner = new SCLDALearner (myAlpha, myBeta, myPRNG);
    ILDALearner hisLearner = new LDALearner (hisAlpha, hisBeta, hisPRNG); //HAHAHA
    
    // set up the low bound, the high bound, and the average for the observed topic probs
    IDoubleMatrix topic_probsLOW = new RowMajorDoubleMatrix (numDocs, numTopics, 1.0);
    IDoubleMatrix topic_probsHIGH = new RowMajorDoubleMatrix (numDocs, numTopics, 0.0);
    IDoubleMatrix topic_probsAVG = new RowMajorDoubleMatrix (numDocs, numTopics, 0.0);
    try {
      for (int i = 0; i < numDocs; i++) {
        topic_probsLOW.setRow (i, new DenseDoubleVector (numTopics, 1.0));
        topic_probsHIGH.setRow (i, new DenseDoubleVector (numTopics, 0.0));
        topic_probsAVG.setRow (i, new DenseDoubleVector (numTopics, 0.0));
      }
    } catch (Exception e) {
      System.out.println ("Error in test case code?"); 
    }
    
    // now, use the Chris implementation to set the low and high bounds over 50 tries
    System.out.println ("Checking each learner 50 times");
    for (int iter = 0; iter < 50; iter++) {
      
      System.out.format ("Check " + iter + " ");
      
      // run an interation of my learner
      loadUpLearner (myLearner, 12, numWords, numTopics, numDocs, docLen);
      myLearner.updateProduced ();
      myLearner.updateTopicProbs ();
      
      // run an iteration of his learner
      loadUpLearner (hisLearner, 12, numWords, numTopics, numDocs, docLen);
      if (doProducedOneAtATime) {
        for (int i = 0; i < numDocs; i++) {
          hisLearner.updateProduced (i + ""); 
        }
      } else {
        hisLearner.updateProduced ();
      }
      hisLearner.updateTopicProbs ();
      
      // now, check to see if any of the results are an extreme and add his stuff to the average
      try {
        
        // see if we got a new extreme result on a topic prob
        for (DocSignature i : myLearner) {
          
          for (int j = 0; j < numTopics; j++) {
            if (i.getTopicProbs ().getItem (j) > topic_probsHIGH.getEntry (Integer.parseInt (i.getFName ()), j)) {
              topic_probsHIGH.setEntry (Integer.parseInt (i.getFName ()), j, i.getTopicProbs ().getItem (j));
            }
            if (i.getTopicProbs ().getItem (j) < topic_probsLOW.getEntry (Integer.parseInt (i.getFName ()), j)) {
              topic_probsLOW.setEntry (Integer.parseInt (i.getFName ()), j, i.getTopicProbs ().getItem (j));
            }
          }
          
          if (!useIterator) {
             hisLearner.getSignature (i.getFName ()).getTopicProbs ().addMyselfToHim (topic_probsAVG.getRow (Integer.parseInt (i.getFName ())));
          }
        }
        
        if (useIterator) {
          for (DocSignature i : hisLearner) {
            i.getTopicProbs ().addMyselfToHim (topic_probsAVG.getRow (Integer.parseInt (i.getFName ())));
          }
        }
          
      } catch (Exception e) {
        fail ("Got an exception.  Possibly I am getting bad vector lengths back from the learner?");
      }
    }
    
    // now, verify that everything matches up
    int errs = 0;
    try {
      System.out.println ("Checking that observed average over all of the topic_probs vectors looks OK");
      for (int i = 0; i < numDocs; i++) {
        topic_probsAVG.getRow (i).normalize ();
        for (int j = 0; j < numTopics; j++) {
          if (topic_probsAVG.getEntry (i, j) < topic_probsLOW.getEntry (i, j) ||
              topic_probsAVG.getEntry (i, j) > topic_probsHIGH.getEntry (i, j)) {
            System.out.println ("When testing topic probs, for doc " + i + " topic " + j + " I got an unreasonable average probability (" 
                    + topic_probsLOW.getEntry (i, j) + " " + topic_probsAVG.getEntry (i, j) + " " +
                                topic_probsHIGH.getEntry (i, j) + ")");
            errs++;
          }
        }
      }
    } catch (Exception e) {
      fail ("Got an exception when I was trying to verify the correctness of the average");
    }
    if (errs > maxErrs) {
      fail ("Got " + errs + " bad vals in the test.  Max allowed was " + maxErrs); 
    }
  }
  
  public void testUpdateOfProducedAndTopicProbsUsingIteratorOne () {
    checkProduceAndTopicProbUpdate (false, true, 100, 20, 100, 200, 3); 
  }
  
  public void testUpdateOfProducedAndTopicProbsUsingIteratorTwo () {
    checkProduceAndTopicProbUpdate (false, true, 100, 2, 100, 200, 3); 
  }
  
  public void testUpdateOfProducedAndTopicProbsUsingIteratorThree () {
    checkProduceAndTopicProbUpdate (false, true, 10, 2, 1000, 200, 3); 
  }
  
  public void testUpdateOfProducedAndTopicProbsWOIteratorOne () {
    checkProduceAndTopicProbUpdate (false, false, 50, 10, 100, 200, 3); 
  }
  
  public void testUpdateOfProducedAndTopicProbsDoDocsSeparatelyOne () {
    checkProduceAndTopicProbUpdate (true, true, 100, 20, 100, 200, 3); 
  }
  
  public void testAllUpdatesOne () {
    checkAllUpdates (100, 20, 100, 200, 1, 3); 
  }
  
  public void testAllUpdatesTwo () {
    checkAllUpdates (100, 2, 100, 200, 1, 3); 
  }
  
  public void testAllUpdatesThree () {
    checkAllUpdates (10, 2, 1000, 200, 1, 3); 
  }
  
  public void testMultipleItersOne () {
    checkAllUpdates (100, 20, 100, 200, 2, 15); 
  }
  
  public void testMultipleItersTwo () {
    checkAllUpdates (100, 2, 100, 200, 5, 10); 
  }
    
  public void testSerializationOne () {
    checkSerializaton (100, 2, 100, 200); 
  }
  
  public void testSerializationTwo () {
    checkSerializaton (1000, 20, 1000, 200); 
  }
  
    
}
