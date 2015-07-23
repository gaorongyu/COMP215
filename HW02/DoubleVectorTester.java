import junit.framework.TestCase;
import java.util.Random;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */
public class DoubleVectorTester extends TestCase {
  
  /**
   * In this part of the code we have a number of helper functions
   * that will be used by the actual test functions to test specific
   * functionality.
   */
  
  /**
   * This is used all over the place to check if an observed value is
   * close enough to an expected double value
   */
  private void checkCloseEnough (double observed, double goal, int pos) {
    if (!(observed >= goal - 1e-8 - Math.abs (goal * 1e-8) && observed <= goal + 1e-8 + Math.abs (goal * 1e-8)))
      if (pos != -1) 
        fail ("Got " + observed + " at pos " + pos + ", expected " + goal);
      else
        fail ("Got " + observed + ", expected " + goal);
  }
  
  /** 
   * This adds a bunch of data into testMe, then checks (and returns)
   * the l1norm
   */
  private double l1NormTester (IDoubleVector testMe, double init) {
  
    // This will by used to scatter data randomly
    
    // Note we don't want test cases to share the random number generator, since this
    // will create dependencies among them
    Random rng = new Random (12345);
    
    // pick a bunch of random locations and repeatedly add an integer in
    double total = 0.0;
    int pos = 0;
    while (pos < testMe.getLength ()) {
      
      // there's a 20% chance we keep going
      if (rng.nextDouble () > .2) {
        pos++;
        continue;
      }
      
      // otherwise, add a value in
      try {
        double current = testMe.getItem (pos);
        testMe.setItem (pos, current + pos);
        total += pos;
      } catch (OutOfBoundsException e) {
        fail ("Got an out of bounds exception on set/get pos " + pos + " not expecting one!\n");
      }
    }
    
    // now test the l1 norm
    double expected = testMe.getLength () * init + total;
    checkCloseEnough (testMe.l1Norm (), expected, -1);
    return expected;
  }
  
  /**
   * This does a large number of setItems to an array of double vectors,
   * and then makes sure that all of the set values are still there
   */
  private void doLotsOfSets (IDoubleVector [] allMyVectors, double init) {
    
    int numVecs = allMyVectors.length;
    
    // put data in exectly one array in each dimension
    for (int i = 0; i < allMyVectors[0].getLength (); i++) {
      
      int whichOne = i % numVecs;
      try {
        allMyVectors[whichOne].setItem (i, 1.345);
      } catch (OutOfBoundsException e) {
        fail ("Got an out of bounds exception on set/get pos " + whichOne + " not expecting one!\n");
      }
    }
    
    // now check all of that data
    // put data in exectly one array in each dimension
    for (int i = 0; i < allMyVectors[0].getLength (); i++) {
      
      int whichOne = i % numVecs;
      for (int j = 0; j < numVecs; j++) {
        try {
          if (whichOne == j) {
            checkCloseEnough (allMyVectors[j].getItem (i), 1.345, j);
          } else {
            checkCloseEnough (allMyVectors[j].getItem (i), init, j); 
          }        
        } catch (OutOfBoundsException e) {
          fail ("Got an out of bounds exception on set/get pos " + whichOne + " not expecting one!\n");
        }
      }
    }
  }
  
  /**
   * This sets only the first value in a double vector, and then checks
   * to see if only the first vlaue has an updated value.
   */
  private void trivialGetAndSet (IDoubleVector testMe, double init) {
    try {
      
      // set the first item
      testMe.setItem (0, 11.345);
      
      // now retrieve and test everything except for the first one
      for (int i = 1; i < testMe.getLength (); i++) {
        checkCloseEnough (testMe.getItem (i), init, i);
      }
      
      // and check the first one
      checkCloseEnough (testMe.getItem (0), 11.345, 0);
     
    } catch (OutOfBoundsException e) {
      fail ("Got an out of bounds exception on set/get... not expecting one!\n");
    }
  }
  
  /**
   * This uses the l1NormTester to set a bunch of stuff in the input
   * DoubleVector.  It then normalizes it, and checks to see that things
   * have been normalized correctly.
   */
  private void normalizeTester (IDoubleVector myVec, double init) {

    // get the L1 norm, and make sure it's correct
    double result = l1NormTester (myVec, init);
    
    try {
      // now get an array of ratios
      double [] ratios = new double[myVec.getLength ()];
      for (int i = 0; i < myVec.getLength (); i++) {
        ratios[i] = myVec.getItem (i) / result;
      }
    
      // and do the normalization
      myVec.normalize ();
    
      // now check it
      for (int i = 0; i < myVec.getLength (); i++) {
        checkCloseEnough (ratios[i], myVec.getItem (i), i);
      } 
      
      // and make sure the length is one
      checkCloseEnough (myVec.l1Norm (), 1.0, -1);
    
    } catch (OutOfBoundsException e) {
        fail ("Got an out of bounds exception on set/get... not expecting one!\n");
    } 
  }
  
  /**
   * Here we have the various test functions, organized in increasing order of difficulty.
   * The code for most of them is quite short, and self-explanatory.
   */
 
  public void testSingleDenseSetSize1 () {
    int vecLen = 1;
    IDoubleVector myVec = new SparseDoubleVector (vecLen, 45.67);
    assertEquals (myVec.getLength (), vecLen);
    trivialGetAndSet (myVec, 45.67);
    assertEquals (myVec.getLength (), vecLen);
  }
  
  public void testSingleSparseSetSize1 () {
    int vecLen = 1;
    IDoubleVector myVec = new SparseDoubleVector (vecLen, 345.67);
    assertEquals (myVec.getLength (), vecLen);
    trivialGetAndSet (myVec, 345.67);
    assertEquals (myVec.getLength (), vecLen);
  }
  
  public void testSingleDenseSetSize1000 () {
    int vecLen = 1000;
    IDoubleVector myVec = new DenseDoubleVector (vecLen, 245.67);
    assertEquals (myVec.getLength (), vecLen);
    trivialGetAndSet (myVec, 245.67);
    assertEquals (myVec.getLength (), vecLen);
  }
  
  public void testSingleSparseSetSize1000 () {
    int vecLen = 1000;
    IDoubleVector myVec = new SparseDoubleVector (vecLen, 145.67);
    assertEquals (myVec.getLength (), vecLen);
    trivialGetAndSet (myVec, 145.67);
    assertEquals (myVec.getLength (), vecLen);
  }  
  
  public void testLotsOfDenseSets () {
  
    int numVecs = 100;
    int vecLen = 10000;
    IDoubleVector [] allMyVectors = new DenseDoubleVector [numVecs];
    for (int i = 0; i < numVecs; i++) {
      allMyVectors[i] = new DenseDoubleVector (vecLen, 2.345);
    }
    
    assertEquals (allMyVectors[0].getLength (), vecLen);
    doLotsOfSets (allMyVectors, 2.345);
    assertEquals (allMyVectors[0].getLength (), vecLen);
  }
  
  public void testLotsOfSparseSets () {
  
    int numVecs = 100;
    int vecLen = 10000;
    IDoubleVector [] allMyVectors = new SparseDoubleVector [numVecs];
    for (int i = 0; i < numVecs; i++) {
      allMyVectors[i] = new SparseDoubleVector (vecLen, 2.345);
    }
 
    assertEquals (allMyVectors[0].getLength (), vecLen);
    doLotsOfSets (allMyVectors, 2.345);
    assertEquals (allMyVectors[0].getLength (), vecLen);
  }
  
  public void testLotsOfDenseSetsWithAddToAll () {
  
    int numVecs = 100;
    int vecLen = 10000;
    IDoubleVector [] allMyVectors = new DenseDoubleVector [numVecs];
    for (int i = 0; i < numVecs; i++) {
      allMyVectors[i] = new DenseDoubleVector (vecLen, 0.0);
      allMyVectors[i].addToAll (2.345);
    }
    
    assertEquals (allMyVectors[0].getLength (), vecLen);
    doLotsOfSets (allMyVectors, 2.345);
    assertEquals (allMyVectors[0].getLength (), vecLen);
  }
  
  public void testLotsOfSparseSetsWithAddToAll () {
  
    int numVecs = 100;
    int vecLen = 10000;
    IDoubleVector [] allMyVectors = new SparseDoubleVector [numVecs];
    for (int i = 0; i < numVecs; i++) {
      allMyVectors[i] = new SparseDoubleVector (vecLen, 0.0);
      allMyVectors[i].addToAll (-12.345);
    }
 
    assertEquals (allMyVectors[0].getLength (), vecLen);
    doLotsOfSets (allMyVectors, -12.345);
    assertEquals (allMyVectors[0].getLength (), vecLen);
  }
  
  public void testL1NormDenseShort () {
    int vecLen = 10;
    IDoubleVector myVec = new DenseDoubleVector (vecLen, 45.67);
    assertEquals (myVec.getLength (), vecLen);
    l1NormTester (myVec, 45.67); 
    assertEquals (myVec.getLength (), vecLen);
  }
  
  public void testL1NormDenseLong () {
    int vecLen = 100000;
    IDoubleVector myVec = new DenseDoubleVector (vecLen, 45.67);
    assertEquals (myVec.getLength (), vecLen);
    l1NormTester (myVec, 45.67); 
    assertEquals (myVec.getLength (), vecLen);
  }
  
  public void testL1NormSparseShort () {
    int vecLen = 10;
    IDoubleVector myVec = new SparseDoubleVector (vecLen, 45.67);
    assertEquals (myVec.getLength (), vecLen);
    l1NormTester (myVec, 45.67); 
    assertEquals (myVec.getLength (), vecLen);
  }
  
  public void testL1NormSparseLong () {
    int vecLen = 100000;
    IDoubleVector myVec = new SparseDoubleVector (vecLen, 45.67);
    assertEquals (myVec.getLength (), vecLen);
    l1NormTester (myVec, 45.67); 
    assertEquals (myVec.getLength (), vecLen);
  }
  
  public void testRounding () {
    
    // Note we don't want test cases to share the random number generator, since this
    // will create dependencies among them
    Random rng = new Random (12345);
    
    // create the vectors
    int vecLen = 100000;
    IDoubleVector myVec1 = new DenseDoubleVector (vecLen, 55.0);
    IDoubleVector myVec2 = new SparseDoubleVector (vecLen, 55.0);
    
    // put values with random additional precision in
    for (int i = 0; i < vecLen; i++) {
      double valToPut = i + (0.5 - rng.nextDouble ()) * .9;
      try {
        myVec1.setItem (i, valToPut);
        myVec2.setItem (i, valToPut);
      } catch (OutOfBoundsException e) {
        fail ("not expecting an out-of-bounds here");
      }
    }
    
    // and extract those values
    for (int i = 0; i < vecLen; i++) {
      try {
        checkCloseEnough (myVec1.getRoundedItem (i), i, i);
        checkCloseEnough (myVec2.getRoundedItem (i), i, i);
      } catch (OutOfBoundsException e) {
        fail ("not expecting an out-of-bounds here");
      }
    }
  }  
  
  public void testOutOfBounds () {
   
    int vecLen = 1000;
    SparseDoubleVector vec1 = new SparseDoubleVector (vecLen, 0.0);
    SparseDoubleVector vec2 = new SparseDoubleVector (vecLen + 1, 0.0);
    
    try {
      vec1.getItem (-1);
      fail ("Missed bad getItem #1");
    } catch (OutOfBoundsException e) {}
    
    try {
      vec1.getItem (vecLen);
      fail ("Missed bad getItem #2");
    } catch (OutOfBoundsException e) {}
    
    try {
      vec1.setItem (-1, 0.0);
      fail ("Missed bad setItem #3");
    } catch (OutOfBoundsException e) {}
    
    try {
      vec1.setItem (vecLen, 0.0);
      fail ("Missed bad setItem #4");
    } catch (OutOfBoundsException e) {}
    
    try {
      vec2.getItem (-1);
      fail ("Missed bad getItem #5");
    } catch (OutOfBoundsException e) {}
    
    try {
      vec2.getItem (vecLen + 1);
      fail ("Missed bad getItem #6");
    } catch (OutOfBoundsException e) {}
    
    try {
      vec2.setItem (-1, 0.0);
      fail ("Missed bad setItem #7");
    } catch (OutOfBoundsException e) {}
    
    try {
      vec2.setItem (vecLen + 1, 0.0);
      fail ("Missed bad setItem #8");
    } catch (OutOfBoundsException e) {}
    
    try {
      vec1.addMyselfToHim (vec2);
      fail ("Missed bad add #9");
    } catch (OutOfBoundsException e) {}
    
    try {
      vec2.addMyselfToHim (vec1);
      fail ("Missed bad add #10");
    } catch (OutOfBoundsException e) {}
  }
  
  /**
   * This test creates 100 sparse double vectors, and puts a bunch of data into them
   * pseudo-randomly.  Then it adds each of them, in turn, into a single dense double
   * vector, which is then tested to see if everything adds up.
   */
  public void testLotsOfAdds() {
 
    // This will by used to scatter data randomly into various sparse arrays
    // Note we don't want test cases to share the random number generator, since this
    // will create dependencies among them
    Random rng = new Random (12345);
 
    IDoubleVector [] allMyVectors = new IDoubleVector [100];
    for (int i = 0; i < 100; i++) {
      allMyVectors[i] = new SparseDoubleVector (10000, i * 2.345);
    }
    
    // put data in each dimension
    for (int i = 0; i < 10000; i++) {
      
      // randomly choose 20 dims to put data into
      for (int j = 0; j < 20; j++) {
        int whichOne = (int) Math.floor (100.0 * rng.nextDouble ());
        try {
          allMyVectors[whichOne].setItem (i, allMyVectors[whichOne].getItem (i) + i * 2.345);
        } catch (OutOfBoundsException e) {
          fail ("Got an out of bounds exception on set/get pos " + whichOne + " not expecting one!\n");
        }
      }
    }
    
    // now, add the data up
    DenseDoubleVector result = new DenseDoubleVector (10000, 0.0);
    for (int i = 0; i < 100; i++) {
      try {
        allMyVectors[i].addMyselfToHim (result);
      } catch (OutOfBoundsException e) {
        fail ("Got an out of bounds exception adding two vectors, not expecting one!");
      }
    }
    
    // and check the final result
    for (int i = 0; i < 10000; i++) {
      double expectedValue = (i * 20.0 + 100.0 * 99.0 / 2.0) * 2.345;
      try {
        checkCloseEnough (result.getItem (i), expectedValue, i);
      } catch (OutOfBoundsException e) {
        fail ("Got an out of bounds exception on getItem, not expecting one!");
      }
    }
  }
  
  public void testNormalizeDense () {
    int vecLen = 100000;
    IDoubleVector myVec = new DenseDoubleVector (vecLen, 55.67);
    assertEquals (myVec.getLength (), vecLen);
    normalizeTester (myVec, 55.67); 
    assertEquals (myVec.getLength (), vecLen);
  }
  
  public void testNormalizeSparse () {
    int vecLen = 100000;
    IDoubleVector myVec = new SparseDoubleVector (vecLen, 55.67);
    assertEquals (myVec.getLength (), vecLen);
    normalizeTester (myVec, 55.67); 
    assertEquals (myVec.getLength (), vecLen);
  }
}
