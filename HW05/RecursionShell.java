import java.util.ArrayList;
import java.util.Map;


public class RecursionShell implements IRecursionShell{
 
 private int perm_int_seq; // the variable which will be used in the method of allperms
 private int perm_int_str; // the variable will be used in the method of cityperm which will be used in TSP
 private ArrayList<ArrayList<Integer>> perms; // the perms will be used in the method of allperms
 private ArrayList<ArrayList<String>> cities; // the cities will be used in the method of cityperm which will be used in TSP

 /**
  * construct method
  * set all the varaible as zero and set all the array as empty array
  */ 
 public RecursionShell(){
   perm_int_seq = 0;
   perm_int_str = 0;
   perms = new ArrayList<ArrayList<Integer>>();
   cities = new ArrayList<ArrayList<String>>();
 }
 
 
 @Override
 /**
  *  the method can insert an integer into the position if the position is less than length of the array
  *  if the position larger than array, it will put the integer on the last of the array
  */ 
 public void insertIntoAll(Integer insertMe,
   ArrayList<ArrayList<Integer>> toEveryoneInMe, int atThisPosition) {
  if (toEveryoneInMe.size() == 0)
   return;
  // return the first element in the toEveryoneInMe
  ArrayList<Integer> temp = toEveryoneInMe.remove(0);
  // the recursion of the method
  insertIntoAll(insertMe,toEveryoneInMe,atThisPosition);
  // begin to add element at desired position      
  if (atThisPosition >= temp.size())
   temp.add(insertMe);
  else
   temp.add(atThisPosition,insertMe);
  // add the removed element back to array
  toEveryoneInMe.add(0,temp);
  return;
 }
 
 /**
  *  the method can check whether two different arrays are same
  */ 
 @Override
 public boolean isSame(ArrayList<Integer> checkMe, ArrayList<Integer> meTo) {
  // if the length of two arrays is not equal, they cannot be equal
  if (meTo.size() != checkMe.size())
   return false;
  // if the length of two arrays is zero, they must be equal
  if (meTo.size() == 0)
   return true;
  //  in the normal condition, jsut remove the first element of two arrays at the first step
  Integer meTo_temp = meTo.remove(0), checkMe_temp = checkMe.remove(0);
  //  compare both elements and recurse the method to return the value of true of false
  boolean recVal = (meTo_temp==checkMe_temp) && (isSame(checkMe,meTo));
  //  add the removed element back to two arrays
  checkMe.add(0,checkMe_temp);
  meTo.add(0,meTo_temp);
  // return the final value  
  return recVal;
 }
 
 /**
  *  the method can check whether the latter array is the subset of array of the former array
  */ 
 @Override
 public boolean isSubset(ArrayList<ArrayList<Integer>> checkMe,
   ArrayList<ArrayList<Integer>> toSeeIfIAmInThere) {
  // if the length of the latter array is zero, it must be the subset of the former array
  if (toSeeIfIAmInThere.size() == 0)
   return true;
  // if the length of the former array is zero, the latter cannot be the subset of the former array
  if (checkMe.size() == 0)
   return false;
  // in normal condition, just remove the first element of the latter array
  ArrayList<Integer> temp = toSeeIfIAmInThere.remove(0);
  // use the help function match to check whether the former contain the first element of the latter array
  // and recurse the isSubset value and return the value of true or false
  boolean recVal = match(checkMe,temp) && isSubset(checkMe, toSeeIfIAmInThere);
  // add the removed element back to the array
  toSeeIfIAmInThere.add(temp);
  // retrun the final value
  return recVal;
 }

 
 /**
  *  the method can produce all the perms array of a provided array, the array must be the type of ArrayList<Integer>
  */ 
 @Override
 public ArrayList<ArrayList<Integer>> allPerms(ArrayList<Integer> ofMe) {
  // if the arraylist to be permed is an empty arraylist, then just add an empty arraylist to the arraylist to be returned
  if (ofMe.size()==0)
  {
    perms.add(ofMe);
    return perms;
  }
  // if the value of the sequence equals to the size of array, that means the swap of the number is end in one loop
  // and it will return the pernms and finish the method
  if(perm_int_seq==ofMe.size())
  {
   return perms;
  }
  // create a loop, in each loop, the array will swap two numbers and return the arraylist  
  for (int i = 0; i<ofMe.size(); i++)
  {
   /**
    * to swap two numbers between i and sequence, the sequence will increment gredually
    */ 
   Integer temp = ofMe.get(i);
   ofMe.set(i,ofMe.get(perm_int_seq));
   ofMe.set(perm_int_seq,temp);
   perm_int_seq = perm_int_seq+1;
   
   // if the arraylist does not contains ofMe, then add it
   if (!(match(perms,ofMe)))
   {
    /**
     * this is very important ,with below two sentence, the perms just add the pointers rather than each different arraylist
     */ 
    ArrayList<Integer> ofMe_back = new ArrayList<Integer>();
    ofMe_back.addAll(ofMe);
    perms.add(ofMe_back);
   }
   
   // the recursion of the function
   allPerms(ofMe);
   /**
    * to reswap the numbers between i and sequence, the sequence will decrease gradually.
    * After the reswqp ,the array will restore to the original status to make the next loop can work properly
    */ 
   perm_int_seq = perm_int_seq-1;
   temp = ofMe.get(i);
   ofMe.set(i,ofMe.get(perm_int_seq));
   ofMe.set(perm_int_seq,temp);
  }
  // return the final result
  return perms;
 }

 /**
  *  the solveTSP method to find the shortest path to through each cities in arraylist toVisit
  *  in the method, the algo is calculating all the path of each perm of toVisit,
  *  it is not the easiest method, but it can deal with the problem if the length of toVisit is not very large
  */ 
 @Override
 public int solveTSP(String startCity, ArrayList<String> toVisit,
   Map<String, Integer> distances) {
  // initialize the mini distance as 99999999 
  int min_distance = 999999999;
  // two special condition to return the zero cost
  if (toVisit.size()==0)
   return 0;
  // create an arraylist<arraylist<string>> to store the perms of all the toVisit cities
  ArrayList<ArrayList<String>> toVisit_perm = cityPerms(toVisit);
  // create an loop to get the cost of the path of each perm of the toVisit and compeare them and return the minimum value
  for (int i=0; i < toVisit_perm.size(); i++)
  {
   // get each perm according to the the increment of i in the loop 
   ArrayList<String> temp = (ArrayList<String>) toVisit_perm.get(i);
   // use the helper method to get the distance of one perm
   int compare_distance = solveTspHelper(temp, distances, 0);
   // if the map of distances contains the path of start city and the first city in the current perm list, then plus the distance and return it 
   if (distances.containsKey(startCity + ":" + temp.get(0)))
    compare_distance = compare_distance + distances.get(startCity+":"+temp.get(0));
   // if the map does not containt, then plus 999999999 to the distance
   else 
    compare_distance = compare_distance + 999999999;
   // compare the distance with the former minium distance, if the new distance is smaller, than replace it.
   if (min_distance > compare_distance)
    min_distance = compare_distance;
  }
  // return the minium distance
  return min_distance;
 }
 /**
  *  the function of the match method is same with the function of contain
  *  which can tell us whether the checkMe contians the temp
  */ 
 private boolean match(ArrayList<ArrayList<Integer>> checkMe, ArrayList<Integer> temp)
 {
    // the return condition
    if (checkMe.size() == 0)
     return false;
    // remove the first element to compare them
    ArrayList<Integer> check = checkMe.remove(0);
    // just check the integer and temp is same or not and recurse the match method
    boolean value = isSame(check,temp)||match(checkMe,temp);
    // add back the removed element
    checkMe.add(0,check);
    // return the value
    return value;
 } 

 /**
  * the helper method to deal with the TSP problem, because I do not want the whole TSP method will be recursed
  */ 
private int solveTspHelper(ArrayList<String> toVisit, Map<String, Integer> distances, int n)
{
   // if the size of arraylist toVisit is one, it means there is only one city need to be visited, so just return the cost n, in this condition, the n is zero
   if (toVisit.size()==1)
   return n;
   //  always remove the first element of list toVisit and make it to be the startCity for the next recursion
   String startCity = toVisit.remove(0);
   //  set the temporary value of n to store the recursion value
   int n_temp = solveTspHelper(toVisit,distances,n);
   //  the process of the caculation of the the distance of each cost of two cities of each recursion
  if (distances.containsKey(startCity + ":" + toVisit.get(0)))
   //  if map has two cities, then calcuate the cost and add to the last cose
   n = n_temp + distances.get(startCity+":"+toVisit.get(0));
  else 
   // if map does not both cities, then make the cost equals 99999999. 
   // We cannot plus 999999999 to n, because it will beyong the range and set the n as negative value, the the code will break  
   n = 999999999;
   //  add the startCity back to the the perm list
   toVisit.add(0, startCity);
   // return the cost
   return n;  
}

/**
 *  the method can produce all the perms array of a provided array, the array must be the type of ArrayList<String>
 */ 
public ArrayList<ArrayList<String>> cityPerms(ArrayList<String> ofMe) {
  // if the arraylist to be permed is an empty arraylist, then just add an empty arraylist to the arraylist to be returned
 if (ofMe.size()==0)
 {
   cities.add(ofMe);
   return cities;
 }
  // if the value of the sequence equals to the size of array, that means the swap of the number is end in one loop
  // and it will return the pernms and finish the method
 if(perm_int_str==ofMe.size())
 {
  return cities;
 }
 // create a loop, in each loop, the array will swap two numbers and return the arraylist, this is similar with the method of cityPerms
 for (int i = 0; i<ofMe.size(); i++)
 {
  String temp = ofMe.get(i);
  ofMe.set(i,ofMe.get(perm_int_str));
  ofMe.set(perm_int_str,temp);
  perm_int_str = perm_int_str+1;
  
   ArrayList<String> ofMe_back = new ArrayList<String>();
   ofMe_back.addAll(ofMe);
   cities.add(ofMe_back);
  cityPerms(ofMe);
  
  perm_int_str = perm_int_str-1;
  temp = ofMe.get(i);
  ofMe.set(i,ofMe.get(perm_int_str));
  ofMe.set(perm_int_str,temp);
 }
 // return the final result
 return cities;
}




}
