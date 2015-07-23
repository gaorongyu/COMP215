import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */
public class RecursionShellTest extends TestCase {
  
  
  private RecursionShell mytest; 
  private SCRecursionShell sctest; 
  
  public RecursionShellTest(){
    mytest = new RecursionShell(); // create the object of my RecursionShell class
    sctest = new SCRecursionShell(); // create the object of professor`s RecursionShell class
  }
  /**
   *  to generate an ArrayList<ArrayList<Integer>> to provide a test arraylist for the first method
   *  the outter arraylist has five same inner arraylist which has three elements: 0,1,2
   */  
  private ArrayList<ArrayList<Integer>> generateSame(){
    ArrayList<ArrayList<Integer>> outer = new ArrayList<ArrayList<Integer>>(); 
    ArrayList<Integer> inner = new ArrayList<Integer>();
    
    for (int i = 0; i < 5; i ++)
    {
      inner = new ArrayList<Integer>();
      for (int k = 0; k < 3; k++)
      {
        inner.add(k,k);
      }
      outer.add(i,inner);
    }
    
    return outer;
  }
  
 /**
   *  to generate an ArrayList<ArrayList<Integer>> to provide a test arraylist for the first method
   *  the outter arraylist has five different inner arraylist which has different elements
   *  [0],[0,1],[0,1,2],[0,1,2,3],[0,1,2,3,4]
   */  
  private ArrayList<ArrayList<Integer>> generateDiff(){
    ArrayList<ArrayList<Integer>> outer = new ArrayList<ArrayList<Integer>>(); 
    ArrayList<Integer> inner = new ArrayList<Integer>();
    int len = 1;
    
    for (int i = 0; i < 5; i ++)
    {
      inner = new ArrayList<Integer>();
      for (int k = 0; k < len; k++)
      {
        inner.add(k,k);
      }
      outer.add(i,inner);
      len++;
      if (len == 6)
        break;
    }
    
    return outer;
  }
  /**
   *  to generate a simple ArrayList<Integer>, the length is n, and the element is increment by one
   */
  private ArrayList<Integer> generateSimple(int n)
  {
     ArrayList<Integer> simple = new ArrayList<Integer>();
     
      for (int i = 0; i < n; i++)
      {
        simple.add(i,i);
      }
      
      return simple;  
  }
  
  
  
  
  /**
   * the test method to check insert the number into an arraylist including several same arraylist
   * and the insert position is less than the length of each arraylist
   */ 
  public void testInsertIntoAllSameInSeq() {
    ArrayList<ArrayList<Integer>> test = generateSame();
    sctest.insertIntoAll(5, test, 1);
    ArrayList<ArrayList<Integer>> sctest = test;
    
    test = generateSame();
    mytest.insertIntoAll(5, test, 1);
    ArrayList<ArrayList<Integer>> mytest = test;

    //Check if test passed
    assertEquals (sctest,mytest);
  }
  
  /**
   * the test method to check insert the number into an arraylist including several same arraylist
   * and the insert position is larger than the length of each arraylist
   */ 
  public void testInsertIntoAllSameOutSeq() {
    ArrayList<ArrayList<Integer>> test = generateSame();
    sctest.insertIntoAll(5, test, 8);
    ArrayList<ArrayList<Integer>> sctest = test;
    
    test = generateSame();
    mytest.insertIntoAll(5, test, 8);
    ArrayList<ArrayList<Integer>> mytest = test;

    //Check if test passed
    assertEquals (sctest,mytest);
  }
  
  /**
   * the test method to check insert the number into an arraylist including several different arraylists
   * those arraylist has different length and different elements
   * and the insert position locates in the middle of the length of all the arraylists
   */ 
  public void testInsertIntoAllDiff() {
    ArrayList<ArrayList<Integer>> test = generateDiff();
    
    sctest.insertIntoAll(5, test, 3);
    ArrayList<ArrayList<Integer>> sctest = test;
    
    
    test = generateDiff();
    mytest.insertIntoAll(5, test, 3);
    ArrayList<ArrayList<Integer>> mytest = test;

    //Check if test passed
    assertEquals (sctest,mytest);
  }
  
  /**
   * the test method to check insert the number into an arraylist including nothing
   */ 
  public void testInsertIntoAllEmpty() {
    ArrayList<ArrayList<Integer>> test = new ArrayList<ArrayList<Integer>>();
    
    sctest.insertIntoAll(5, test, 3);
    ArrayList<ArrayList<Integer>> sctest = test;
    
    test = new ArrayList<ArrayList<Integer>>();
    mytest.insertIntoAll(5, test, 3);
    ArrayList<ArrayList<Integer>> mytest = test;


    //Check if test passed
    assertEquals (sctest,mytest);
  }
  
  
    /**
     * the test method to test if two arrays are same, when the length of both is zero
     */ 
    public void testIsSameLengthIsZero() {
    ArrayList<Integer> checkMe = new ArrayList<Integer>();
    ArrayList<Integer> meTo = new ArrayList<Integer>();
    // Check if test passed      
    assertEquals (sctest.isSame(checkMe,meTo), mytest.isSame(checkMe,meTo));
    }
     
    /**
     * the test method to test if two arrays are same, when the length of both is not equal
     */ 
    public void testIsSameLengthIsNotEqual() {
    ArrayList<Integer> checkMe = generateSimple(3);
    ArrayList<Integer> meTo = generateSimple(5);
 
    // Check if test passed  
    assertEquals (sctest.isSame(checkMe,meTo), mytest.isSame(checkMe,meTo));
    }
  
    /**
     * the test method to test if two arrays are same, when both of them are equal
     */ 
    public void testIsSameEqual() {
    ArrayList<Integer> checkMe = generateSimple(5);
    ArrayList<Integer> meTo = generateSimple(5);
    
    // Check if test passed      
    assertEquals (sctest.isSame(checkMe,meTo), mytest.isSame(checkMe,meTo));
    } 
    
    public void testIsSameNotEqual() {
    ArrayList<Integer> checkMe = new ArrayList<Integer>();
    ArrayList<Integer> meTo = new ArrayList<Integer>();
    
    for (int i = 0; i < 5; i ++)
      checkMe.add(i,i);
    for (int i = 0, k = 4; i < 5; i++, k--)
    {
      meTo.add(i,k);
    }
    // Check if test passed
    assertEquals (sctest.isSame(checkMe,meTo), mytest.isSame(checkMe,meTo));
    }
  
  
    /**
     *  the test method to test the subset of both empty arraylist
     */ 
    public void testIsSubSetLatterLengthIsZero() {
    ArrayList<ArrayList<Integer>> checkMe = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> toSeeIfIAmInThere = new ArrayList<ArrayList<Integer>>();
    // Check if test passed
    assertEquals (mytest.isSubset(checkMe,toSeeIfIAmInThere), sctest.isSubset(checkMe,toSeeIfIAmInThere));
    }
    
    /**
     *  the test method to test the subset, the checkMe arraylist is empty, but the other arraylist is not empty
     */ 
    public void testIsSubSetFormerLengthIsZero() {
    ArrayList<ArrayList<Integer>> checkMe = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> toSeeIfIAmInThere = new ArrayList<ArrayList<Integer>>();
    
    ArrayList<Integer> temp = new ArrayList<Integer>();
    for (int i = 0; i < 4; i++)
      temp.add(i,i);
    toSeeIfIAmInThere.add(temp);
    // Check if test passed
    assertEquals (mytest.isSubset(checkMe,toSeeIfIAmInThere), sctest.isSubset(checkMe,toSeeIfIAmInThere));
    }
    
  /**
   * the test method to test the subset, the checkMe only has one arraylist, but the other arraylist has four same arraylist
   */ 
    public void testIsSubSetLatterElementRepeat() {
    ArrayList<ArrayList<Integer>> checkMe = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> toSeeIfIAmInThere = new ArrayList<ArrayList<Integer>>();
    
    ArrayList<Integer> temp = generateSimple(4);
    checkMe.add(temp);
    
    for (int i = 0; i < 4; i++)
    {
      temp = generateSimple(4);
      toSeeIfIAmInThere.add(i,temp);
    }         
    // Check if test passed
    assertEquals (mytest.isSubset(checkMe,toSeeIfIAmInThere), sctest.isSubset(checkMe,toSeeIfIAmInThere));
    }
 
  /**
   * the test method to test the subset, the checkMe and the other arraylist has different arraylist
   */ 
    public void testIsSubSetNotEqual() {
    ArrayList<ArrayList<Integer>> checkMe = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> toSeeIfIAmInThere = new ArrayList<ArrayList<Integer>>();
    
    ArrayList<Integer> temp = generateSimple(4);   
    
    checkMe.add(temp);
    toSeeIfIAmInThere.add(temp);
    
    temp = new ArrayList<Integer>();
    for (int i = 0, k = 10; i < 4; i++, k++)
      temp.add(i,k);
    toSeeIfIAmInThere.add(temp);
          
    // Check if test passed
    assertEquals (mytest.isSubset(checkMe,toSeeIfIAmInThere), sctest.isSubset(checkMe,toSeeIfIAmInThere));
    }
    
  /**
   * the test method to test producing all perms of a provided arraylist, the arraylist is empty in this test
   */ 
    public void testAllPermFromEmpty() {

    ArrayList<Integer> temp = new ArrayList<Integer>();
    
    ArrayList<ArrayList<Integer>> perm = mytest.allPerms(temp); 
    
    // Check if test passed
    assertEquals (sctest.isSubset(mytest.allPerms(temp),sctest.allPerms(temp)), sctest.isSubset(sctest.allPerms(temp),mytest.allPerms(temp)));
    }
    
  /**
    * the test method to test producing all perms of a provided arraylist, the arraylist has six elements in this test
    */ 
    public void testAllPermFromZero() {

    ArrayList<Integer> temp = generateSimple(0);
    
    // Check if test passed
    assertEquals (sctest.isSubset(mytest.allPerms(temp),sctest.allPerms(temp)), sctest.isSubset(sctest.allPerms(temp),mytest.allPerms(temp)));
    }  
    
  /**
    * the test method to test producing all perms of a provided arraylist, the arraylist has three elements in this test
    */ 
    public void testAllPermFromThree() {

    ArrayList<Integer> temp = generateSimple(3);
      
    // Check if test passed
    assertEquals (sctest.isSubset(mytest.allPerms(temp),sctest.allPerms(temp)), sctest.isSubset(sctest.allPerms(temp),mytest.allPerms(temp)));
    }
    
  /**
    * the test method to test producing all perms of a provided arraylist, the arraylist has six elements in this test
    */ 
    public void testAllPermFromFive() {

    ArrayList<Integer> temp = generateSimple(5);  
    // Check if test passed
    assertEquals (sctest.isSubset(mytest.allPerms(temp),sctest.allPerms(temp)), sctest.isSubset(sctest.allPerms(temp),mytest.allPerms(temp)));
    }
    
  /**
    * the test method to test the TSP problem, the sales man leave from a and visit no city, and it should be return zero
    */   
    public void testSolveTSPVistiNoCity() {
      String startCity = "a";
      ArrayList<String> toVisit = new ArrayList<String>();
      Map<String, Integer> distances = new HashMap<String, Integer>();
    // Check if test passed
      assertEquals (sctest.solveTSP(startCity, toVisit, distances), mytest.solveTSP(startCity, toVisit, distances));
    }
   
   /**
    * the test method to test the TSP problem, the sales man leave from a and visit no city, and visit the local city
    * because the city is not in the list, so the value of cost will be 99999999 
    */       
    public void testSolveTSPVisitLocalCity() {
      String startCity = "a";
      ArrayList<String> toVisit = new ArrayList<String>();
      toVisit.add("a");
      Map<String, Integer> distances = new HashMap<String, Integer>();
    // Check if test passed
      assertEquals (sctest.solveTSP(startCity, toVisit, distances), mytest.solveTSP(startCity, toVisit, distances));
    }    
    
   /**
    * the test method to test the TSP problem, the sales man leave from city"a", and visit another three cities
    * the method will choose the past of least cost
    */     
    public void testSolveTSPVisitThreeCities() {
      String startCity = "a";
      ArrayList<String> toVisit = new ArrayList<String>();
      // add three cities in the arraylist
      toVisit.add("b");
      toVisit.add("c");
      toVisit.add("d");
      Map<String, Integer> distances = new HashMap<String, Integer>();
      // add all the distances in the map      
      distances.put("a:b",12);
      distances.put("a:c",20);
      distances.put("b:c",30);
      distances.put("c:b",9);
      distances.put("d:c",29);
      distances.put("b:d",17);
    // Check if test passed      
      assertEquals (sctest.solveTSP(startCity, toVisit, distances), mytest.solveTSP(startCity, toVisit, distances));
    }
    
   /**
    * the test method to test the TSP problem, the sales man leave from city"a", and visit another six cities
    * the method will choose the past of least cost
    */    
     public void testSolveTSPVisitSixCities() {
      String startCity = "a";
      // add five cities in the arraylist
      ArrayList<String> toVisit = new ArrayList<String>();
      toVisit.add("b");
      toVisit.add("c");
      toVisit.add("d");
      toVisit.add("e");
      toVisit.add("f");
      toVisit.add("g");
      // add all the distances in the map
      Map<String, Integer> distances = new HashMap<String, Integer>();
      distances.put("a:b",12);
      distances.put("a:c",20);
      distances.put("b:c",30);
      distances.put("c:b",9);
      distances.put("d:c",29);
      distances.put("b:d",17);
      distances.put("e:f",29);
      distances.put("d:f",16);
      distances.put("e:d",19);
      distances.put("g:f",16);
      distances.put("d:g",17);
      distances.put("f:g",17);
      distances.put("c:e",30);
      distances.put("d:b",20);
      distances.put("a:g",14);
      distances.put("a:e",23);
    // Check if test passed      
      assertEquals (sctest.solveTSP(startCity, toVisit, distances), mytest.solveTSP(startCity, toVisit, distances));
    }    
}
