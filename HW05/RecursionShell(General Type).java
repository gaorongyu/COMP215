import java.util.ArrayList;
import java.util.Map;


public class RecursionShell<T> implements IRecursionShell<T>{
 
 private int perm_int_seq; // the variable will be used in the methed all perms
 private ArrayList<ArrayList<T>> perms; // the perms will be used in the method all perms and the method TSP
 
 /**
  * construct method to initialize two variables
  */
 public RecursionShell(){
   perm_int_seq = 0;  
   perms = new ArrayList<ArrayList<T>>();
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
 public boolean isSame(ArrayList<T> check, ArrayList<T> temp) {
  // if the length of two arrays is not equal, they cannot be equal
  if (temp.size() != check.size())
   return false;
  // if the length of two arrays is zero, they must be equal
  if (temp.size() == 0)
   return true;
  //  in the normal condition, jsut remove the first element of two arrays at the first step
  T meTo_temp = temp.remove(0), checkMe_temp = check.remove(0);
  //  compare both elements and recurse the method to return the value of true of false
  boolean recVal = (meTo_temp==checkMe_temp) && (isSame(check,temp));
  //  add the removed element back to two arrays
  check.add(0,checkMe_temp);
  temp.add(0,meTo_temp);
  // return the final value  
  return recVal;
 }
 
 /**
  *  the method can check whether the latter array is the subset of array of the former array
  */ 
 @Override
 public boolean isSubset(ArrayList<ArrayList<T>> checkMe,
   ArrayList<ArrayList<T>> toSeeIfIAmInThere) {
  // if the length of the latter array is zero, it must be the subset of the former array
  if (toSeeIfIAmInThere.size() == 0)
   return true;
  // if the length of the former array is zero, the latter cannot be the subset of the former array
  if (checkMe.size() == 0)
   return false;
  // in normal condition, just remove the first element of the latter array
  ArrayList<T> temp = toSeeIfIAmInThere.remove(0);
  // use the help function match to check whether the former contain the first element of the latter array
  // and recurse the isSubset value and return the value of true or false
  boolean recVal = match(checkMe,temp) && isSubset(checkMe, toSeeIfIAmInThere);
  // add the removed element back to the array
  toSeeIfIAmInThere.add(temp);
  // retrun the final value
  return recVal;
 }

 
 /**
  *  the method can produce all the perms array of a provided array.
  */ 
 @Override
 public ArrayList<ArrayList<T>> allPerms(ArrayList<T> ofMe) {
  // TODO Auto-generated method stub 
  if (ofMe.size()==0)
  {
    perms.add(ofMe);
    return perms;
  }
  if(perm_int_seq==ofMe.size())
  {
   return perms;
  }
  
  for (int i = 0; i<ofMe.size(); i++)
  {
   T temp = ofMe.get(i);
   ofMe.set(i,ofMe.get(perm_int_seq));
   ofMe.set(perm_int_seq,temp);
   perm_int_seq = perm_int_seq+1;
   
   if (!(match(perms,ofMe)))
   {
    ArrayList<T> ofMe_back = new ArrayList<T>();
    ofMe_back.addAll(ofMe);
    perms.add(ofMe_back);
   }
   allPerms(ofMe);
   
   perm_int_seq = perm_int_seq-1;
   temp = ofMe.get(i);
   ofMe.set(i,ofMe.get(perm_int_seq));
   ofMe.set(perm_int_seq,temp);
  }
  return perms;
 }

 @Override
 public int solveTSP(String startCity, ArrayList<String> toVisit,
   Map<String, Integer> distances) {  
  // TODO Auto-generated method stub
  int min_distance = 999999999;
  if (toVisit.size()==0 || toVisit.get(0) == startCity)
   return 0;

  ArrayList<ArrayList<T>> toVisit_perm = allPerms((ArrayList<T>) toVisit);
  for (int i=0; i < toVisit_perm.size(); i++)
  {
   ArrayList<String> temp = (ArrayList<String>) toVisit_perm.get(i); 
   int compare_distance = solveTspHelper(temp, distances, 0);
   if (distances.containsKey(startCity + ":" + temp.get(0)))
    compare_distance = compare_distance + distances.get(startCity+":"+temp.get(0));
   else 
    compare_distance = compare_distance + 999999999;
   
   if (min_distance > compare_distance)
    min_distance = compare_distance;
  }
  return min_distance;
 }
 /**
  *  the function of the match method is same with the function of contain
  *  which can tell us whether the checkMe contian the temp
  */ 
 private boolean match(ArrayList<ArrayList<T>> checkMe, ArrayList<T> temp)
 {
   
    if (checkMe.size() == 0)
     return false;
    ArrayList<T> check = checkMe.remove(0);
    boolean value = isSame(check,temp)||match(checkMe,temp);
    checkMe.add(0,check);
    return value;
 }

private int solveTspHelper(ArrayList<String> toVisit, Map<String, Integer> distances, int n)
{
   if (toVisit.size()==1)
   return n;
   String startCity = toVisit.remove(0);
   int n_temp = solveTspHelper(toVisit,distances,n);

  if (distances.containsKey(startCity + ":" + toVisit.get(0)))
   n = n_temp + distances.get(startCity+":"+toVisit.get(0));
  else 
   n = n_temp + 999999999;
   toVisit.add(0, startCity);
   return n;  
}
}
