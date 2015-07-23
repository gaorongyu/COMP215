
import java.util.*;

interface ILDALearner extends Iterable <DocSignature> {
  
  /**
    * THESE METHODS ARE USED TO LOAD DATA INTO THE LDA ALGORITHM
    */
  
  // Using the specified file, this loads the words_in_doc and topic_probs vectors for 
  // every document, as well as the word_probs vector for every topic.  All of the data
  // in the LDA object before loadFromFile is called should be deleted.  Note that after
  // calling "loadFromFile", the produced matrix for each document does not exist
  void loadFromFile (String loadFromMe);
  
  // this loads a document into the machine learning algorithm... "topicProbs" is the initial
  // version of the topic_probs vector for that document, "wordsInDoc" is the words_in_doc 
  // vector for that document.  Often, topicProbs will be nothing more than a sample from
  // a Dirichlet distribution where the parameter is a vector of ones; this provides a random 
  // initialization
  void loadUpAnotherDoc (String docName, IDoubleVector wordsInDoc, IDoubleVector topicProbs);
  
  // this loads all of the current topics into the machine learning algorithm... the t^th row
  // of the matrix "loadMe" is the word_probs vector associated with the t^th topic.  Often,
  // wordProbs will initilly be nothing more than a random guess at the true wordProbs matrix,
  // with each row sampled from a Dirichlet () distribution where the parameter is a vector of
  // ones; this provides a random initialization
  void loadUpWordProbs (IDoubleMatrix loadMe);
  
  /**
    * THESE METHODS ARE USED TO UPDATE THE DATA AS REQUIRED BY THE LDA ALGORITHM... SEE
    * SLIDES 46 THROUGH 53 OF THE LECTURE 19 SLIDES
    */
  
  // update/create the produced matrix for a particular document... if the doc does not exist
  // or the word_probs matrix does not exist (it has not been loaded via "loadUpWordProbs" or
  // via "readFromFile") than an exception is thrown
  void updateProduced (String docName);
  
  // update/create the produced matrix for ALL of the documents... just as above, if the 
  // word_probs matrix does not exist, then an exception is thrown 
  void updateProduced ();
  
  // this updates the topic_probs vector for a particular document... if the doc does not exist,
  // or the produced matrix for that doc does not exist (because "updateProduced" has not been
  // called on this document since it was added or loaded) then an exception is thrown
  void updateTopicProbs (String docName);
  
  // this updates the topic_probs vector for ALL of the documents... just as above, if the 
  // produced matrix does not exist for ANY of the documents in the corpus, then an exception 
  // is thrown
  void updateTopicProbs ();
  
  // this updates the word_probs vector for all of the topics... if the produced matrix does 
  // not exist for ANY of the documents in the corpus, then this will fail
  void updateWordProbs ();
  
  /**
   * THESE METHODS ARE USED TO EXTRACT DATA FROM THE LDA ALGORITHM
   */
                     
  // gets the current topic_probs vector and the sords_in_doc vector for a particular document... 
  // if the document does not exist, then an exception is thrown
  DocSignature getSignature (String docName);
  
  // gets an iterator that returns the DocSignature for all of the documents
  Iterator <DocSignature> iterator ();
 
  // gets all of the word_probs vectors from the ML algorithm... the t^th row in the matrix 
  // corresponds to the word_probs vector for the t^th topic
  IDoubleMatrix getWordProbs ();
  
  // this writes the data maintained by the ML algorithm to a file (specifically, it writes all
  // of the topic_probs vectors, all of the words_in_doc vetors, and all of the word_probs vectors).
  // If one calls "writeToFile", then creates a new LDA object and calls "readFromFile" using that
  // same file, the new LDA object will have exactly the same contents as the old one, except that
  // all of the produced matrices will be lost, because they are not written
  void writeToFile (String writeToMe);
  
}