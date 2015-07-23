
import java.util.ArrayList;

// this is a map from a set of keys of type PointInMetricSpace to a
// set of data of type DataType
interface IMTree <Key extends IPointInMetricSpace <Key>, Data> {
 
  // insert a new key/data pair into the map
  void insert (Key keyToAdd, Data dataToAdd);
  
  // find all of the key/data pairs in the map that fall within a
  // particular distance of query point if no results are found, then
  // an empty array is returned (NOT a null!)
  ArrayList <DataWrapper <Key, Data>> find (Key query, double distance);
  
  // find the k closest key/data pairs in the map to a particular
  // query point 
  //
  // if the number of points in the map is less than k, then the
  // returned list will have less than k pairs in it
  //
  // if the number of points is zero, then an empty array is returned
  // (NOT a null!)
  ArrayList <DataWrapper <Key, Data>> findKClosest (Key query, int k);
  
  // returns the number of nodes that exist on a path from root to leaf in the tree...
  // whtever the details of the implementation are, a tree with a single leaf node should return 1, and a tree
  // with more than one lead node should return at least two (since it must have at least one internal node).
  public int depth ();
  
}