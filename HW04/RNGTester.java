import junit.framework.TestCase;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */
public class RNGTester extends TestCase {
  
  
  // checks to see if two values are close enough
  private void checkCloseEnough (double observed, double goal, double tolerance, String whatCheck, String dist) { 
    
    // first compute the allowed error 
    double allowedError = goal * tolerance; 
    if (allowedError < tolerance) 
      allowedError = tolerance; 
    
    // print the result
    System.out.println ("Got " + observed + ", expected " + goal + " when I was checking the " + whatCheck + " of the " + dist);
    
    // if it is too large, then fail 
    if (!(observed > goal - allowedError && observed < goal + allowedError)) 
      fail ("Got " + observed + ", expected " + goal + " when I was checking the " + whatCheck + " of the " + dist); 
  } 
 
  /**
   * This routine checks whether the observed mean for a one-dim RNG is close enough to the expected mean.
   * Note the weird "runTest" parameter.  If this is set to true, the test for correctness is actually run.
   * If it is set to false, the test is not run, and only the observed mean is returned to the caller.
   * This is done so that we can "turn off" the correctness check, when we are only using this routine 
   * to compute an observed mean in order to check if the seeding is working.  This way, we don't check
   * too many things in one single test.
   */
  private double checkMean (IRandomGenerationAlgorithm<Double> myRNG, double expectedMean, boolean runTest, String dist) {
  
    int numTrials = 100000;
    double total = 0.0;
    for (int i = 0; i < numTrials; i++) {
      total += myRNG.getNext ();  
    }
    
    if (runTest)
      checkCloseEnough (total / numTrials, expectedMean, 10e-3, "mean", dist);
    
    return total / numTrials;
  }
  
  /**
   * This checks whether the standard deviation for a one-dim RNG is close enough to the expected std dev.
   */
  private void checkStdDev (IRandomGenerationAlgorithm<Double> myRNG, double expectedVariance, String dist) {
  
    int numTrials = 100000;
    double total = 0.0;
    double totalSquares = 0.0;
    for (int i = 0; i < numTrials; i++) {
      double next = myRNG.getNext ();
      total += next;
      totalSquares += next * next;
    }
    
    double observedVar = -(total / numTrials) * (total / numTrials) + totalSquares / numTrials;
    
    checkCloseEnough (Math.sqrt(observedVar), Math.sqrt(expectedVariance), 10e-3, "standard deviation", dist);
  }
  
  /**
   * Tests whether the startOver routine works correctly.  To do this, it generates a sequence of random
   * numbers, and computes the mean of them.  Then it calls startOver, and generates the mean once again.
   * If the means are very, very close to one another, then the test case passes.
   */
  public void testUniformReset () {
    
    double low = 0.5;
    double high = 1.0;
    IRandomGenerationAlgorithm<Double> myRNG = new Uniform (745664, new UniformParam (low, high));
    double result1 = checkMean (myRNG, 0, false, "");
    myRNG.startOver ();
    double result2 = checkMean (myRNG, 0, false, "");
    assertTrue ("Failed check for uniform reset capability", Math.abs (result1 - result2) < 10e-10);
  }
  
  /** 
   * Tests whether seeding is correctly used.  This is run just like the startOver test above, except
   * that here we create two sequences using two different seeds, and then we verify that the observed
   * means were different from one another.
   */
  public void testUniformSeeding () {
    
    double low = 0.5;
    double high = 1.0;
    IRandomGenerationAlgorithm<Double> myRNG1 = new Uniform (745664, new UniformParam (low, high));
    IRandomGenerationAlgorithm<Double> myRNG2 = new Uniform (2334, new UniformParam (low, high));
    double result1 = checkMean (myRNG1, 0, false, "");
    double result2 = checkMean (myRNG2, 0, false, "");
    assertTrue ("Failed check for uniform seeding correctness", Math.abs (result1 - result2) > 10e-10);
  }
  
  /**
   * Generates a bunch of random variables, and then uses the know formulas for the mean and variance of those
   * variables to verify that the observed mean and variance are reasonable; if they are, this is a strong 
   * indication that the variables are being generated correctly
   */
  public void testUniform1 () {
    
    double low = 0.5;
    double high = 1.0;
    IRandomGenerationAlgorithm<Double> myRNG = new Uniform (745664, new UniformParam (low, high));
    checkMean (myRNG, low / 2.0 + high / 2.0, true, "Uniform (" + low + ", " + high + ")");
    checkStdDev (myRNG, (high - low) * (high - low) / 12.0, "Uniform (" + low + ", " + high + ")");
  }
  
  /**
   * Generates a bunch of random variables, and then uses the know formulas for the mean and variance of those
   * variables to verify that the observed mean and variance are reasonable; if they are, this is a strong 
   * indication that the variables are being generated correctly
   */
  public void testUniform2 () {

    double low = -123456.0;
    double high = 233243.0;
    IRandomGenerationAlgorithm<Double> myRNG = new Uniform (745664, new UniformParam (low, high));
    checkMean (myRNG, low / 2.0 + high / 2.0, true, "Uniform (" + low + ", " + high + ")");
    checkStdDev (myRNG, (high - low) * (high - low) / 12.0, "Uniform (" + low + ", " + high + ")");
  }
  
  /**
   * Tests whether the startOver routine works correctly.  To do this, it generates a sequence of random
   * numbers, and computes the mean of them.  Then it calls startOver, and generates the mean once again.
   * If the means are very, very close to one another, then the test case passes.
   */  
  public void testUnitGammaReset () {
    
    double shape = 0.5;
    double scale = 1.0;
    IRandomGenerationAlgorithm<Double> myRNG = new UnitGamma (745664, new GammaParam (shape, scale, 10e-40, 150));
    double result1 = checkMean (myRNG, 0, false, "");
    myRNG.startOver ();
    double result2 = checkMean (myRNG, 0, false, "");
    assertTrue ("Failed check for unit gamma reset capability", Math.abs (result1 - result2) < 10e-10);
  }
  /** 
   * Tests whether seeding is correctly used.  This is run just like the startOver test above, except
   * that here we create two sequences using two different seeds, and then we verify that the observed
   * means were different from one another.
   */  
  public void testUnitGammaSeeding () {
    
    double shape = 0.5;
    double scale = 1.0;
    IRandomGenerationAlgorithm<Double> myRNG1 = new UnitGamma (745664, new GammaParam (shape, scale, 10e-40, 150));
    IRandomGenerationAlgorithm<Double> myRNG2 = new UnitGamma (232, new GammaParam (shape, scale, 10e-40, 150));
    double result1 = checkMean (myRNG1, 0, false, "");
    double result2 = checkMean (myRNG2, 0, false, "");
    assertTrue ("Failed check for unit gamma seeding correctness", Math.abs (result1 - result2) > 10e-10);
  }
  
  /**
   * Generates a bunch of random variables, and then uses the know formulas for the mean and variance of those
   * variables to verify that the observed mean and variance are reasonable; if they are, this is a strong 
   * indication that the variables are being generated correctly
   */
  public void testUnitGamma1 () {
    
    double shape = 0.5;
    double scale = 1.0;
    IRandomGenerationAlgorithm<Double> myRNG = new UnitGamma (745664, new GammaParam (shape, scale, 10e-40, 150));
    checkMean (myRNG, shape * scale, true, "Gamma (" + shape + ", " + scale + ")");
    checkStdDev (myRNG, shape * scale * scale, "Gamma (" + shape + ", " + scale + ")");
  }

  /**
   * Generates a bunch of random variables, and then uses the know formulas for the mean and variance of those
   * variables to verify that the observed mean and variance are reasonable; if they are, this is a strong 
   * indication that the variables are being generated correctly
   */
  public void testUnitGamma2 () {
    
    double shape = 0.05;
    double scale = 1.0;
    IRandomGenerationAlgorithm<Double> myRNG = new UnitGamma (6755, new GammaParam (shape, scale, 10e-40, 150));
    checkMean (myRNG, shape * scale, true, "Gamma (" + shape + ", " + scale + ")");
    checkStdDev (myRNG, shape * scale * scale, "Gamma (" + shape + ", " + scale + ")");
  }
  
  /**
   * Tests whether the startOver routine works correctly.  To do this, it generates a sequence of random
   * numbers, and computes the mean of them.  Then it calls startOver, and generates the mean once again.
   * If the means are very, very close to one another, then the test case passes.
   */  
  public void testGammaReset () {
    
    double shape = 15.09;
    double scale = 3.53;
    IRandomGenerationAlgorithm<Double> myRNG = new Gamma (27, new GammaParam (shape, scale, 10e-40, 150));
    double result1 = checkMean (myRNG, 0, false, "");
    myRNG.startOver ();
    double result2 = checkMean (myRNG, 0, false, "");
    assertTrue ("Failed check for gamma reset capability", Math.abs (result1 - result2) < 10e-10);
  }
  
  /** 
   * Tests whether seeding is correctly used.  This is run just like the startOver test above, except
   * that here we create two sequences using two different seeds, and then we verify that the observed
   * means were different from one another.
   */  
  public void testGammaSeeding () {
    
    double shape = 15.09;
    double scale = 3.53;
    IRandomGenerationAlgorithm<Double> myRNG1 = new Gamma (27, new GammaParam (shape, scale, 10e-40, 150));
    IRandomGenerationAlgorithm<Double> myRNG2 = new Gamma (297, new GammaParam (shape, scale, 10e-40, 150));
    double result1 = checkMean (myRNG1, 0, false, "");
    double result2 = checkMean (myRNG2, 0, false, "");
    assertTrue ("Failed check for gamma seeding correctness", Math.abs (result1 - result2) > 10e-10);
  }

  /**
   * Generates a bunch of random variables, and then uses the know formulas for the mean and variance of those
   * variables to verify that the observed mean and variance are reasonable; if they are, this is a strong 
   * indication that the variables are being generated correctly
   */
  public void testGamma1 () {
    
    double shape = 5.88;
    double scale = 34.0;
    IRandomGenerationAlgorithm<Double> myRNG = new Gamma (27, new GammaParam (shape, scale, 10e-40, 150));
    checkMean (myRNG, shape * scale, true, "Gamma (" + shape + ", " + scale + ")");
    checkStdDev (myRNG, shape * scale * scale, "Gamma (" + shape + ", " + scale + ")");
  }
  
  /**
   * Generates a bunch of random variables, and then uses the know formulas for the mean and variance of those
   * variables to verify that the observed mean and variance are reasonable; if they are, this is a strong 
   * indication that the variables are being generated correctly
   */
  public void testGamma2 () {
    
    double shape = 15.09;
    double scale = 3.53;
    IRandomGenerationAlgorithm<Double> myRNG = new Gamma (27, new GammaParam (shape, scale, 10e-40, 150));
    checkMean (myRNG, shape * scale, true, "Gamma (" + shape + ", " + scale + ")");
    checkStdDev (myRNG, shape * scale * scale, "Gamma (" + shape + ", " + scale + ")");
  }
  
  /**
   * This checks the sub of the absolute differences between the entries of two vectors; if it is too large, then
   * a jUnit error is generated
   */
  public void checkTotalDiff (IDoubleVector actual, IDoubleVector expected, double error, String test, String dist) throws OutOfBoundsException {
    double totError = 0.0;
    for (int i = 0; i < actual.getLength (); i++) {
      totError += Math.abs (actual.getItem (i) - expected.getItem (i));
    }
    checkCloseEnough (totError, 0.0, error, test, dist);
  }
  
  /**
   * Computes the difference between the observed mean of a multi-dim random variable, and the expected mean.
   * The difference is returned as the sum of the parwise absolute values in the two vectors.  This is used
   * for the seeding and startOver tests for the two multi-dim random variables.
   */
  public double computeDiffFromMean (IRandomGenerationAlgorithm<IDoubleVector> myRNG, IDoubleVector expectedMean, int numTrials) {
    
    // set up the total so far
    try {
      IDoubleVector firstOne = myRNG.getNext ();
      DenseDoubleVector meanObs = new DenseDoubleVector (firstOne.getLength (), 0.0);
    
      // add in a bunch more
      for (int i = 0; i < numTrials; i++) {
        IDoubleVector next = myRNG.getNext ();
        next.addMyselfToHim (meanObs);
      }
    
      // compute the total difference from the mean
      double returnVal = 0;
      for (int i = 0; i < meanObs.getLength (); i++) {
        returnVal += Math.abs (meanObs.getItem (i) / numTrials - expectedMean.getItem (i));
      }
      
      // and return it
      return returnVal;
      
    } catch (OutOfBoundsException e) {
      fail ("I got an OutOfBoundsException when running getMean... bad vector length back?");  
      return 0.0;
    }
  }
                                     
  /**
   * Checks the observed mean and variance for a multi-dim random variable versus the theoretically expected values
   * for these quantities.  If the observed differs substantially from the expected, the test case is failed.  This
   * is used for checking the correctness of the multi-dim random variables.
   */
  public void checkMeanAndVar (IRandomGenerationAlgorithm<IDoubleVector> myRNG, 
      IDoubleVector expectedMean, IDoubleVector expectedStdDev, double errorMean, double errorStdDev, int numTrials, String dist) {
  
    // set up the total so far
    try {
      IDoubleVector firstOne = myRNG.getNext ();
      DenseDoubleVector meanObs = new DenseDoubleVector (firstOne.getLength (), 0.0);
      DenseDoubleVector stdDevObs = new DenseDoubleVector (firstOne.getLength (), 0.0);
    
      // add in a bunch more
      for (int i = 0; i < numTrials; i++) {
        IDoubleVector next = myRNG.getNext ();
        next.addMyselfToHim (meanObs);
        for (int j = 0; j < next.getLength (); j++) {
          stdDevObs.setItem (j, stdDevObs.getItem (j) + next.getItem (j) * next.getItem (j));  
        }
      }
    
      // divide by the number of trials to get the mean
      for (int i = 0; i < meanObs.getLength (); i++) {
        meanObs.setItem (i, meanObs.getItem (i) / numTrials);
        stdDevObs.setItem (i, Math.sqrt (stdDevObs.getItem (i) / numTrials - meanObs.getItem (i) * meanObs.getItem (i)));
      }
    
      // see if the mean and var are acceptable
      checkTotalDiff (meanObs, expectedMean, errorMean, "total distance from true mean", dist);
      checkTotalDiff (stdDevObs, expectedStdDev, errorStdDev, "total distance from true standard deviation", dist);
      
    } catch (OutOfBoundsException e) {
      fail ("I got an OutOfBoundsException when running getMean... bad vector length back?");  
    }
  }
  
  /**
   * Tests whether the startOver routine works correctly.  To do this, it generates a sequence of random
   * numbers, and computes the mean of them.  Then it calls startOver, and generates the mean once again.
   * If the means are very, very close to one another, then the test case passes.
   */  
  public void testMultinomialReset () {
    
    try {
      // first set up a vector of probabilities
      int len = 100;
      SparseDoubleVector probs = new SparseDoubleVector (len, 0.0);
      for (int i = 0; i < len; i += 2) {
        probs.setItem (i, i); 
      }
      probs.normalize ();
    
      // now, set up a distribution
      IRandomGenerationAlgorithm<IDoubleVector> myRNG = new Multinomial (27, new MultinomialParam (1024, probs));
    
      // and check the mean...
      DenseDoubleVector expectedMean = new DenseDoubleVector (len, 0.0);
      for (int i = 0; i < len; i++) {
        expectedMean.setItem (i, probs.getItem (i) * 1024);
      }
      
      double res1 = computeDiffFromMean (myRNG, expectedMean, 500);
      myRNG.startOver ();
      double res2 = computeDiffFromMean (myRNG, expectedMean, 500);
      
      assertTrue ("Failed check for multinomial reset", Math.abs (res1 - res2) < 10e-10);
      
    } catch (Exception e) {
      fail ("Got some sort of exception when I was testing the multinomial."); 
    }
   
  }

  /** 
   * Tests whether seeding is correctly used.  This is run just like the startOver test above, except
   * that here we create two sequences using two different seeds, and then we verify that the observed
   * means were different from one another.
   */  
  public void testMultinomialSeeding () {
    
    try {
      // first set up a vector of probabilities
      int len = 100;
      SparseDoubleVector probs = new SparseDoubleVector (len, 0.0);
      for (int i = 0; i < len; i += 2) {
        probs.setItem (i, i); 
      }
      probs.normalize ();
    
      // now, set up a distribution
      IRandomGenerationAlgorithm<IDoubleVector> myRNG1 = new Multinomial (27, new MultinomialParam (1024, probs));
      IRandomGenerationAlgorithm<IDoubleVector> myRNG2 = new Multinomial (2777, new MultinomialParam (1024, probs));
      
      // and check the mean...
      DenseDoubleVector expectedMean = new DenseDoubleVector (len, 0.0);
      for (int i = 0; i < len; i++) {
        expectedMean.setItem (i, probs.getItem (i) * 1024);
      }
      
      double res1 = computeDiffFromMean (myRNG1, expectedMean, 500);
      double res2 = computeDiffFromMean (myRNG2, expectedMean, 500);
      
      assertTrue ("Failed check for multinomial seeding", Math.abs (res1 - res2) > 10e-10);
      
    } catch (Exception e) {
      fail ("Got some sort of exception when I was testing the multinomial."); 
    }
   
  }
  
  
  /**
   * Generates a bunch of random variables, and then uses the know formulas for the mean and variance of those
   * variables to verify that the observed mean and variance are reasonable; if they are, this is a strong 
   * indication that the variables are being generated correctly
   */  
  public void testMultinomial1 () {
    
    try {
      // first set up a vector of probabilities
      int len = 100;
      SparseDoubleVector probs = new SparseDoubleVector (len, 0.0);
      for (int i = 0; i < len; i += 2) {
        probs.setItem (i, i); 
      }
      probs.normalize ();
    
      // now, set up a distribution
      IRandomGenerationAlgorithm<IDoubleVector> myRNG = new Multinomial (27, new MultinomialParam (1024, probs));
    
      // and check the mean... we repeatedly double the prob vector to multiply it by 1024
      DenseDoubleVector expectedMean = new DenseDoubleVector (len, 0.0);
      DenseDoubleVector expectedStdDev = new DenseDoubleVector (len, 0.0);
      for (int i = 0; i < len; i++) {
        expectedMean.setItem (i, probs.getItem (i) * 1024);
        expectedStdDev.setItem (i, Math.sqrt (probs.getItem (i) * 1024 * (1.0 - probs.getItem (i))));
      }
      
      checkMeanAndVar (myRNG, expectedMean, expectedStdDev, 5.0, 5.0, 5000, "multinomial number one");
      
    } catch (Exception e) {
      fail ("Got some sort of exception when I was testing the multinomial."); 
    }
   
  }

  /**
   * Generates a bunch of random variables, and then uses the know formulas for the mean and variance of those
   * variables to verify that the observed mean and variance are reasonable; if they are, this is a strong 
   * indication that the variables are being generated correctly
   */  
  public void testMultinomial2 () {
    
    try {
      // first set up a vector of probabilities
      int len = 1000;
      SparseDoubleVector probs = new SparseDoubleVector (len, 0.0);
      for (int i = 0; i < len - 1; i ++) {
        probs.setItem (i, 0.0001); 
      }
      probs.setItem (len - 1, 100);
      probs.normalize ();
    
      // now, set up a distribution
      IRandomGenerationAlgorithm<IDoubleVector> myRNG = new Multinomial (27, new MultinomialParam (10, probs));
    
      // and check the mean... we repeatedly double the prob vector to multiply it by 1024
      DenseDoubleVector expectedMean = new DenseDoubleVector (len, 0.0);
      DenseDoubleVector expectedStdDev = new DenseDoubleVector (len, 0.0);
      for (int i = 0; i < len; i++) {
        expectedMean.setItem (i, probs.getItem (i) * 10);
        expectedStdDev.setItem (i, Math.sqrt (probs.getItem (i) * 10 * (1.0 - probs.getItem (i))));
      }
      
      checkMeanAndVar (myRNG, expectedMean, expectedStdDev, 0.05, 5.0, 5000, "multinomial number two");
      
    } catch (Exception e) {
      fail ("Got some sort of exception when I was testing the multinomial."); 
    }
   
  }
  
  /**
   * Tests whether the startOver routine works correctly.  To do this, it generates a sequence of random
   * numbers, and computes the mean of them.  Then it calls startOver, and generates the mean once again.
   * If the means are very, very close to one another, then the test case passes.
   */  
  public void testDirichletReset () {
    
    try {
      
      // first set up a vector of shapes
      int len = 100;
      SparseDoubleVector shapes = new SparseDoubleVector (len, 0.0);
      for (int i = 0; i < len; i++) {
        shapes.setItem (i, 0.05); 
      }
    
      // now, set up a distribution
      IRandomGenerationAlgorithm<IDoubleVector> myRNG = new Dirichlet (27, new DirichletParam (shapes, 10e-40, 150));
    
      // compute the expected mean
      DenseDoubleVector expectedMean = new DenseDoubleVector (len, 0.0);
      double norm = shapes.l1Norm ();
      for (int i = 0; i < len; i++) {
        expectedMean.setItem (i, shapes.getItem (i) / norm);
      }
      
      double res1 = computeDiffFromMean (myRNG, expectedMean, 500);
      myRNG.startOver ();
      double res2 = computeDiffFromMean (myRNG, expectedMean, 500);
      
      assertTrue ("Failed check for Dirichlet reset", Math.abs (res1 - res2) < 10e-10);
      
    } catch (Exception e) {
      fail ("Got some sort of exception when I was testing the Dirichlet."); 
    }
   
  }

  /** 
   * Tests whether seeding is correctly used.  This is run just like the startOver test above, except
   * that here we create two sequences using two different seeds, and then we verify that the observed
   * means were different from one another.
   */  
  public void testDirichletSeeding () {
    
    try {
      
      // first set up a vector of shapes
      int len = 100;
      SparseDoubleVector shapes = new SparseDoubleVector (len, 0.0);
      for (int i = 0; i < len; i++) {
        shapes.setItem (i, 0.05); 
      }
    
      // now, set up a distribution
      IRandomGenerationAlgorithm<IDoubleVector> myRNG1 = new Dirichlet (27, new DirichletParam (shapes, 10e-40, 150));
      IRandomGenerationAlgorithm<IDoubleVector> myRNG2 = new Dirichlet (92, new DirichletParam (shapes, 10e-40, 150));
    
      // compute the expected mean
      DenseDoubleVector expectedMean = new DenseDoubleVector (len, 0.0);
      double norm = shapes.l1Norm ();
      for (int i = 0; i < len; i++) {
        expectedMean.setItem (i, shapes.getItem (i) / norm);
      }
      
      double res1 = computeDiffFromMean (myRNG1, expectedMean, 500);
      double res2 = computeDiffFromMean (myRNG2, expectedMean, 500);
      
      assertTrue ("Failed check for Dirichlet seeding", Math.abs (res1 - res2) > 10e-10);
      
    } catch (Exception e) {
      fail ("Got some sort of exception when I was testing the Dirichlet."); 
    }
   
  }

  /**
   * Generates a bunch of random variables, and then uses the know formulas for the mean and variance of those
   * variables to verify that the observed mean and variance are reasonable; if they are, this is a strong 
   * indication that the variables are being generated correctly
   */
  public void testDirichlet1 () {
    
    try {
      // first set up a vector of shapes
      int len = 100;
      SparseDoubleVector shapes = new SparseDoubleVector (len, 0.0);
      for (int i = 0; i < len; i++) {
        shapes.setItem (i, 0.05); 
      }
    
      // now, set up a distribution
      IRandomGenerationAlgorithm<IDoubleVector> myRNG = new Dirichlet (27, new DirichletParam (shapes, 10e-40, 150));
    
      // compute the expected mean and var
      DenseDoubleVector expectedMean = new DenseDoubleVector (len, 0.0);
      DenseDoubleVector expectedStdDev = new DenseDoubleVector (len, 0.0);
      double norm = shapes.l1Norm ();
      for (int i = 0; i < len; i++) {
        expectedMean.setItem (i, shapes.getItem (i) / norm);
        expectedStdDev.setItem (i, Math.sqrt (shapes.getItem (i) * (norm - shapes.getItem (i)) / 
                                   (norm * norm * (1.0 + norm))));
      }
      
      checkMeanAndVar (myRNG, expectedMean, expectedStdDev, 0.1, 0.3, 5000, "Dirichlet number one");
      
    } catch (Exception e) {
      fail ("Got some sort of exception when I was testing the Dirichlet."); 
    }
   
  }
  
  /**
   * Generates a bunch of random variables, and then uses the know formulas for the mean and variance of those
   * variables to verify that the observed mean and variance are reasonable; if they are, this is a strong 
   * indication that the variables are being generated correctly
   */
  public void testDirichlet2 () {
    
    try {
      // first set up a vector of shapes
      int len = 300;
      SparseDoubleVector shapes = new SparseDoubleVector (len, 0.0);
      for (int i = 0; i < len - 10; i++) {
        shapes.setItem (i, 0.05); 
      }
      for (int i = len - 9; i < len; i++) {
        shapes.setItem (i, 100.0); 
      }
    
      // now, set up a distribution
      IRandomGenerationAlgorithm<IDoubleVector> myRNG = new Dirichlet (27, new DirichletParam (shapes, 10e-40, 150));
    
      // compute the expected mean and var
      DenseDoubleVector expectedMean = new DenseDoubleVector (len, 0.0);
      DenseDoubleVector expectedStdDev = new DenseDoubleVector (len, 0.0);
      double norm = shapes.l1Norm ();
      for (int i = 0; i < len; i++) {
        expectedMean.setItem (i, shapes.getItem (i) / norm);
        expectedStdDev.setItem (i, Math.sqrt (shapes.getItem (i) * (norm - shapes.getItem (i)) / 
                                   (norm * norm * (1.0 + norm))));
      }
      
      checkMeanAndVar (myRNG, expectedMean, expectedStdDev, 0.01, 0.01, 5000, "Dirichlet number one");
      
    } catch (Exception e) {
      fail ("Got some sort of exception when I was testing the Dirichlet."); 
    }
   
  }
  
}
