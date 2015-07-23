
import java.util.ArrayList;
import java.util.Map;

/**
 * This interface contains four simple methods that operate on lists of Integer objects
 */
interface IRecursionShell {
  
  /**
   * This inserts the Integer insertMe into every ArrayList found in toEveroneInMe.  insertMe is put
   * at position atThisPosition in every list.  If atThisPosition exceeds the length of a particular list,
   * then it is put at the end of the list.
   */
  void insertIntoAll(Integer insertMe, ArrayList<ArrayList<Integer>> toEveryoneInMe, int atThisPosition);
  
  /** 
   * Checks to see if the contents of checkMe and meTo are the same, in the sense that all of the ints
   * contained in both match up on a pairwise basis---the first int in checkMe matches the first int in
   * meTo, the second int in checkMe matches the second int in meTo, and so on.
   */
  boolean isSame(ArrayList<Integer> checkMe, ArrayList<Integer> meTo);
  
  /** 
   * Checks to see if everything in toSeeIfIAmInThere is in checkMe as well.  A list is in both if both
   * versions of the list contain exactly the same set of ints, and the ints in both match up on a pairwise
   * basis (as described in isSame)
   */
  boolean isSubset(ArrayList<ArrayList<Integer>> checkMe, ArrayList<ArrayList<Integer>> toSeeIfIAmInThere);
  
  /**
   * Returns a list that contains all permutations of ofMe
   */
  ArrayList<ArrayList<Integer>> allPerms(ArrayList<Integer> ofMe);
  
  /**
   * Solves the TSP, finding the shortest distance that visits all cities listed in toVisit...
   * it is assumed that the distance (if it exists) between two cities is given as a ("city1:city2", dist)
   * pair in the map "distances"
   */
  int solveTSP (String startCity, ArrayList<String> toVisit, Map<String,Integer> distances);
}
