import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */

public class MTreeTester extends TestCase {
  
  // compare two lists of strings to see if the contents are the same
  private boolean compareStringSets (ArrayList <String> setOne, ArrayList <String> setTwo) {
        
    // sort the sets and make sure they are the same
    boolean returnVal = true;
    if (setOne.size () == setTwo.size ()) {
      Collections.sort (setOne);
      Collections.sort (setTwo);
      for (int i = 0; i < setOne.size (); i++) {
        if (!setOne.get (i).equals (setTwo.get (i))) {
          returnVal = false;
          break; 
        }
      }
    } else {
      returnVal = false;
    }
    
    // print out the result
    System.out.println ("**** Expected:");
    for (int i = 0; i < setOne.size (); i++) {
      System.out.format (setOne.get (i) + " ");
    }
    System.out.println ("\n**** Got:");
    for (int i = 0; i < setTwo.size (); i++) {
      System.out.format (setTwo.get (i) + " ");
    }
    System.out.println ("");
    
    return returnVal;
  }
  
  
  /**
   * THESE TWO HELPER METHODS TEST SIMPLE ONE DIMENSIONAL DATA
   */
  
  // puts the specified number of random points into a tree of the given node size
  private void testOneDimRangeQuery (boolean orderThemOrNot, int minDepth,
                                               int internalNodeSize, int leafNodeSize, int numPoints, double expectedQuerySize) {
    
    // create two trees
    Random rn = new Random (133);
    IMTree <OneDimPoint, String> myTree = new SCMTree <OneDimPoint, String> (internalNodeSize, leafNodeSize);
    IMTree <OneDimPoint, String> hisTree = new MTree <OneDimPoint, String> (internalNodeSize, leafNodeSize);
    
    // add a bunch of points
    if (orderThemOrNot) {
      for (int i = 0; i < numPoints; i++) {
        double nextOne = i / (numPoints * 1.0);
        myTree.insert (new OneDimPoint (nextOne * numPoints), (new Double (nextOne * numPoints)).toString ());
        hisTree.insert (new OneDimPoint (nextOne * numPoints), (new Double (nextOne * numPoints)).toString ());
      }
    } else {
      for (int i = 0; i < numPoints; i++) {
        double nextOne = rn.nextDouble ();
        myTree.insert (new OneDimPoint (nextOne * numPoints), (new Double (nextOne * numPoints)).toString ());
        hisTree.insert (new OneDimPoint (nextOne * numPoints), (new Double (nextOne * numPoints)).toString ());
      }      
    }
    
    assertTrue ("Why was the tree not of depth at least " + minDepth, hisTree.depth () >= minDepth);
    
    // run a range query
    OneDimPoint query = new OneDimPoint (numPoints * 0.5);
    ArrayList <DataWrapper <OneDimPoint, String>> result1 = myTree.find (query, expectedQuerySize / 2);
    ArrayList <DataWrapper <OneDimPoint, String>> result2 = hisTree.find (query, expectedQuerySize / 2);
    
    // check the results
    ArrayList <String> setOne = new ArrayList <String> (), setTwo = new ArrayList <String> ();
    for (int i = 0; i < result1.size (); i++) {
      setOne.add (result1.get (i).getData ());  
    }
    for (int i = 0; i < result2.size (); i++) {
      setTwo.add (result2.get (i).getData ());  
    }
    assert (compareStringSets (setOne, setTwo));
    
    // run a second range query
    query = new OneDimPoint (numPoints);
    result1 = myTree.find (query, expectedQuerySize);
    result2 = hisTree.find (query, expectedQuerySize);
    
    // check the results
    setOne = new ArrayList <String> ();
    setTwo = new ArrayList <String> ();
    for (int i = 0; i < result1.size (); i++) {
      setOne.add (result1.get (i).getData ());  
    }
    for (int i = 0; i < result2.size (); i++) {
      setTwo.add (result2.get (i).getData ());  
    }
    assert (compareStringSets (setOne, setTwo));
  }
  
  // like the last one, but runs a top k
  private void testOneDimTopKQuery (boolean orderThemOrNot, int minDepth,
                                              int internalNodeSize, int leafNodeSize, int numPoints, int querySize) {
    
    // create two trees
    Random rn = new Random (167);
    IMTree <OneDimPoint, String> myTree = new SCMTree <OneDimPoint, String> (internalNodeSize, leafNodeSize);
    IMTree <OneDimPoint, String> hisTree = new MTree <OneDimPoint, String> (internalNodeSize, leafNodeSize);
    
    // add a bunch of points
    if (orderThemOrNot) {
      for (int i = 0; i < numPoints; i++) {
        double nextOne = i / (numPoints * 1.0);
        myTree.insert (new OneDimPoint (nextOne * numPoints), (new Double (nextOne * numPoints)).toString ());
        hisTree.insert (new OneDimPoint (nextOne * numPoints), (new Double (nextOne * numPoints)).toString ());
      }
    } else {
      for (int i = 0; i < numPoints; i++) {
        double nextOne = rn.nextDouble ();
        myTree.insert (new OneDimPoint (nextOne * numPoints), (new Double (nextOne * numPoints)).toString ());
        hisTree.insert (new OneDimPoint (nextOne * numPoints), (new Double (nextOne * numPoints)).toString ());
      }      
    }
    
    assertTrue ("Why was the tree not of depth at least " + minDepth, hisTree.depth () >= minDepth);
        
    // run a range query
    OneDimPoint query = new OneDimPoint (numPoints * 0.5);
    ArrayList <DataWrapper <OneDimPoint, String>> result1 = myTree.findKClosest (query, querySize);
    ArrayList <DataWrapper <OneDimPoint, String>> result2 = hisTree.findKClosest (query, querySize);
    
    // check the results
    ArrayList <String> setOne = new ArrayList <String> (), setTwo = new ArrayList <String> ();
    for (int i = 0; i < result1.size (); i++) {
      setOne.add (result1.get (i).getData ());  
    }
    for (int i = 0; i < result2.size (); i++) {
      setTwo.add (result2.get (i).getData ());  
    }
    assert (compareStringSets (setOne, setTwo));
    
    // run a second range query
    query = new OneDimPoint (0);
    result1 = myTree.findKClosest (query, querySize);
    result2 = myTree.findKClosest (query, querySize);
    
    // check the results
    setOne = new ArrayList <String> ();
    setTwo = new ArrayList <String> ();
    for (int i = 0; i < result1.size (); i++) {
      setOne.add (result1.get (i).getData ());  
    }
    for (int i = 0; i < result2.size (); i++) {
      setTwo.add (result2.get (i).getData ());  
    }
    assert (compareStringSets (setOne, setTwo));
  }
  
  /**
   * THESE TWO HELPER METHODS TEST MULTI-DIMENSIONAL DATA
   */
    
  // puts the specified number of random points into a tree of the given node size
  private void testMultiDimRangeQuery (boolean orderThemOrNot, int minDepth, int numDims,
                                               int internalNodeSize, int leafNodeSize, int numPoints, double expectedQuerySize) {
    
    // create two trees
    Random rn = new Random (133);
    IMTree <MultiDimPoint, String> myTree = new SCMTree <MultiDimPoint, String> (internalNodeSize, leafNodeSize);
    IMTree <MultiDimPoint, String> hisTree = new MTree <MultiDimPoint, String> (internalNodeSize, leafNodeSize);
    
    // these are the queries
    double dist1 = 0;
    double dist2 = 0;
    MultiDimPoint query1;
    MultiDimPoint query2;
    
    // add a bunch of points
    if (orderThemOrNot) {
      
      for (int i = 0; i < numPoints; i++) {
        SparseDoubleVector temp1 = new SparseDoubleVector (numDims, i);
        SparseDoubleVector temp2 = new SparseDoubleVector (numDims, i);
        myTree.insert (new MultiDimPoint (temp1), temp1.toString ());
        hisTree.insert (new MultiDimPoint (temp2), temp2.toString ());
      }
      
      // in this case, the queries are simple
      query1 = new MultiDimPoint (new SparseDoubleVector (numDims, numPoints / 2));
      query2 = new MultiDimPoint (new SparseDoubleVector (numDims, 0.0));
      dist1 = Math.sqrt (numDims) * expectedQuerySize / 2.0;
      dist2 = Math.sqrt (numDims) * expectedQuerySize;
      
    } else {
      
      // create a bunch of random data
      for (int i = 0; i < numPoints; i++) {
        DenseDoubleVector temp1 = new DenseDoubleVector (numDims, 0.0);
        DenseDoubleVector temp2 = new DenseDoubleVector (numDims, 0.0);
        for (int j = 0; j < numDims; j++) {
          double curVal = rn.nextDouble ();
          try {
            temp1.setItem (j, curVal);
            temp2.setItem (j, curVal);
          } catch (OutOfBoundsException e) {
            System.out.println ("Error in test case?  Why am I out of bounds?"); 
          }
        }
        myTree.insert (new MultiDimPoint (temp1), temp1.toString ());
        hisTree.insert (new MultiDimPoint (temp2), temp2.toString ());
      }
      
      // in this case, get the spheres first
      query1 = new MultiDimPoint (new SparseDoubleVector (numDims, 0.5));
      query2 = new MultiDimPoint (new SparseDoubleVector (numDims, 0.0));
      
      // do a top k query
      ArrayList <DataWrapper <MultiDimPoint, String>> result1 = myTree.findKClosest (query1, (int) expectedQuerySize);
      ArrayList <DataWrapper <MultiDimPoint, String>> result2 = myTree.findKClosest (query2, (int) expectedQuerySize);
      
      // find the distance to the furthest point in both cases
      for (int i = 0; i < (int) expectedQuerySize; i++) {
        double newDist = result1.get (i).getKey ().getDistance (query1);
        if (newDist > dist1)
          dist1 = newDist;
        newDist = result2.get (i).getKey ().getDistance (query2);
        if (newDist > dist2)
          dist2 = newDist;
      }
    }
    
    assertTrue ("Why was the tree not of depth at least " + minDepth, hisTree.depth () >= minDepth);
    
    // run a range query
    ArrayList <DataWrapper <MultiDimPoint, String>> result1 = myTree.find (query1, dist1);
    ArrayList <DataWrapper <MultiDimPoint, String>> result2 = hisTree.find (query1, dist1);
    
    // check the results
    ArrayList <String> setOne = new ArrayList <String> (), setTwo = new ArrayList <String> ();
    for (int i = 0; i < result1.size (); i++) {
      setOne.add (result1.get (i).getData ());  
    }
    for (int i = 0; i < result2.size (); i++) {
      setTwo.add (result2.get (i).getData ());  
    }
    assert (compareStringSets (setOne, setTwo));
    
    // run a second range query
    result1 = myTree.find (query2, dist2);
    result2 = hisTree.find (query2, dist2);
    
    // check the results
    setOne = new ArrayList <String> ();
    setTwo = new ArrayList <String> ();
    for (int i = 0; i < result1.size (); i++) {
      setOne.add (result1.get (i).getData ());  
    }
    for (int i = 0; i < result2.size (); i++) {
      setTwo.add (result2.get (i).getData ());  
    }
    assert (compareStringSets (setOne, setTwo));
  }
  
  // puts the specified number of random points into a tree of the given node size
  private void testMultiDimTopKQuery (boolean orderThemOrNot, int minDepth, int numDims,
                                               int internalNodeSize, int leafNodeSize, int numPoints, int querySize) {
    
    // create two trees
    Random rn = new Random (133);
    IMTree <MultiDimPoint, String> myTree = new SCMTree <MultiDimPoint, String> (internalNodeSize, leafNodeSize);
    IMTree <MultiDimPoint, String> hisTree = new MTree <MultiDimPoint, String> (internalNodeSize, leafNodeSize);
    
    // these are the queries
    MultiDimPoint query1;
    MultiDimPoint query2;
    
    // add a bunch of points
    if (orderThemOrNot) {
      
      for (int i = 0; i < numPoints; i++) {
        SparseDoubleVector temp1 = new SparseDoubleVector (numDims, i);
        SparseDoubleVector temp2 = new SparseDoubleVector (numDims, i);
        myTree.insert (new MultiDimPoint (temp1), temp1.toString ());
        hisTree.insert (new MultiDimPoint (temp2), temp2.toString ());
      }
      
      // in this case, the queries are simple
      query1 = new MultiDimPoint (new SparseDoubleVector (numDims, numPoints / 2));
      query2 = new MultiDimPoint (new SparseDoubleVector (numDims, 0.0));
      
    } else {
      
      // create a bunch of random data
      for (int i = 0; i < numPoints; i++) {
        DenseDoubleVector temp1 = new DenseDoubleVector (numDims, 0.0);
        DenseDoubleVector temp2 = new DenseDoubleVector (numDims, 0.0);
        for (int j = 0; j < numDims; j++) {
          double curVal = rn.nextDouble ();
          try {
            temp1.setItem (j, curVal);
            temp2.setItem (j, curVal);
          } catch (OutOfBoundsException e) {
            System.out.println ("Error in test case?  Why am I out of bounds?"); 
          }
        }
        myTree.insert (new MultiDimPoint (temp1), temp1.toString ());
        hisTree.insert (new MultiDimPoint (temp2), temp2.toString ());
      }
      
      // in this case, get the spheres first
      query1 = new MultiDimPoint (new SparseDoubleVector (numDims, 0.5));
      query2 = new MultiDimPoint (new SparseDoubleVector (numDims, 0.0));
    }
    
    assertTrue ("Why was the tree not of depth at least " + minDepth, hisTree.depth () >= minDepth);
    
    // run a top k query
    ArrayList <DataWrapper <MultiDimPoint, String>> result1 = myTree.findKClosest (query1, querySize);
    ArrayList <DataWrapper <MultiDimPoint, String>> result2 = hisTree.findKClosest (query1, querySize);
    
    // check the results
    ArrayList <String> setOne = new ArrayList <String> (), setTwo = new ArrayList <String> ();
    for (int i = 0; i < result1.size (); i++) {
      setOne.add (result1.get (i).getData ());  
    }
    for (int i = 0; i < result2.size (); i++) {
      setTwo.add (result2.get (i).getData ());  
    }
    assert (compareStringSets (setOne, setTwo));
    
    // run a second range query
    result1 = myTree.findKClosest (query2, querySize);
    result2 = hisTree.findKClosest (query2, querySize);
    
    // check the results
    setOne = new ArrayList <String> ();
    setTwo = new ArrayList <String> ();
    for (int i = 0; i < result1.size (); i++) {
      setOne.add (result1.get (i).getData ());  
    }
    for (int i = 0; i < result2.size (); i++) {
      setTwo.add (result2.get (i).getData ());  
    }
    assert (compareStringSets (setOne, setTwo));
  }
  
  
  /**
   * "TIER 1" TESTS
   *  COLLECTIVELY, THESE ARE WORTH 40% OF THE OVERALL "TEST CASE POINTS"
   *  NONE OF THESE REQUIRE ANY SPLITS
   */
  public void testEasy1() {
    testOneDimRangeQuery (true, 1, 10, 10, 5, 2.1);
  }
  
  public void testEasy2() {
    testOneDimRangeQuery (false, 1, 10, 10, 5, 2.1);
  }
  
  public void testEasy3() {
    testOneDimRangeQuery (true, 1, 10000, 10000, 5000, 10);
  }
  
  public void testEasy4() {
    testOneDimRangeQuery (false, 1, 10000, 10000, 5000, 10);
  }
  
  public void testEasy5() {
    testMultiDimRangeQuery (true, 1, 5, 10, 10, 5, 2.1);
  }
  
  public void testEasy6() {
    testMultiDimRangeQuery (false, 1, 10, 10, 10, 5, 2.1);
  }
  
  public void testEasy7() {
    testMultiDimRangeQuery (true, 1, 5, 10000, 10000, 5000, 10);
  }
  
  public void testEasy8() {
    testMultiDimRangeQuery (false, 1, 15, 10000, 10000, 1000, 4);
  }
  
  /**
   * "TIER 2" TESTS
   *  COLLECTIVELY, THESE ARE WORTH 20% OF THE OVERALL "TEST CASE POINTS"
   *  NONE OF THESE REQUIRE A SPLIT OF AN INTERNAL NODE
   */
  
  public void testMod1() {
    testOneDimRangeQuery (true, 2, 10, 10, 50, 5.1);
  }
  
  public void testMod2() {
    testOneDimRangeQuery (false, 2, 10, 10, 50, 5.1);
  }
  
  public void testMod3() {
    testOneDimRangeQuery (true, 2, 20, 100, 1000, 10);
  }
  
  public void testMod4() {
    testOneDimRangeQuery (false, 2, 20, 100, 1000, 10);
  }
  
  public void testMod5() {
    testMultiDimRangeQuery (true, 2, 5, 1000, 200, 10000, 2.1);
  }
  
  public void testMod6() {
    testMultiDimRangeQuery (false, 2, 10, 1000, 200, 10000, 2.1);
  }
  
  public void testMod7() {
    testMultiDimRangeQuery (true, 2, 5, 10000, 100, 1000, 10);
  }
  
  public void testMod8() {
    testMultiDimRangeQuery (false, 2, 15, 10000, 100, 1000, 4);
  }
  
  /**
   * "TIER 3" TESTS
   *  COLLECTIVELY, THESE ARE WORTH 20% OF THE OVERALL "TEST CASE POINTS"
   *  THESE REQUIRE A SPLIT OF AN INTERNAL NODE
   */
  
  public void testHard1() {
    testOneDimRangeQuery (true, 3, 10, 10, 500, 5.1);
  }
  
  public void testHard2() {
    testOneDimRangeQuery (false, 3, 10, 10, 500, 5.1);
  }
  
  public void testHard3() {
    testOneDimRangeQuery (true, 3, 4, 4, 10000, 10);
  }
  
  public void testHard4() {
    testOneDimRangeQuery (false, 3, 4, 4, 10000, 10);
  }
  
  public void testHard5() {
    testMultiDimRangeQuery (true, 3, 5, 5, 5, 100000, 2.1);
  }
  
  public void testHard6() {
    testMultiDimRangeQuery (false, 3, 5, 5, 5, 100000, 2.1);
  }
  
  public void testHard7() {
    testMultiDimRangeQuery (true, 3, 15, 4, 4, 10000, 10);
  }
  
  public void testHard8() {
    testMultiDimRangeQuery (false, 3, 15, 4, 4, 10000, 4);
  }
  
  /**
   * "TIER 4" TESTS
   *  COLLECTIVELY, THESE ARE WORTH 20% OF THE OVERALL "TEST CASE POINTS"
   *  THESE REQUIRE A SPLIT OF AN INTERNAL NODE AND THEY TEST THE TOPK FUNCTIONALITY
   */
  
  public void testTopK1() {
    testOneDimTopKQuery (true, 3, 10, 10, 500, 5);
  }
  
  public void testTopK2() {
    testOneDimTopKQuery (false, 3, 10, 10, 500, 5);
  }
  
  public void testTopK3() {
    testOneDimTopKQuery (true, 3, 4, 4, 10000, 10);
  }
  
  public void testTopK4() {
    testOneDimTopKQuery (false, 3, 4, 4, 10000, 10);
  }
  
  public void testTopK5() {
    testMultiDimTopKQuery (true, 3, 5, 5, 5, 100000, 2);
  }
  
  public void testTopK6() {
    testMultiDimTopKQuery (false, 3, 5, 5, 5, 100000, 2);
  }
  
  public void testTopK7() {
    testMultiDimTopKQuery (true, 3, 15, 4, 4, 10000, 10);
  }
  
  public void testTopK8() {
    testMultiDimTopKQuery (false, 3, 15, 4, 4, 10000, 4);
  }
}
