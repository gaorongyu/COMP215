import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * The tree sparse array class. 
 * It implements the interface ISparseArray, so it need to implenment three method in ISparseArray.
 * Because this class is directly use the treemap in java, so it is not neccessary to write much code to realize the function of three methods.
 * Just using the treemap`s method, and only another thing to is to create a class treeindexed to realize the iterator function.
 *
 */ 

public class TreeSparseArray<T> implements ISparseArray<T>
{
  /**
   *  declare a TreeMap whose class is TreeMap<Integer,T>
   *  The class contains two generalized parameter, one is integer, the other is generalized parameter T.
   */ 
 private TreeMap<Integer,T> treemap; 
 
 
 
  /**
   *  The constructor to create a new treemap for the treemap
   */ 
 public TreeSparseArray()
 {
  treemap = new TreeMap<Integer,T>();
 }
 
  /**
   *  The put method, just use the put method in TreeMap to put element in the index of position. 
   */ 
 public void put(int position, T element) { 
  treemap.put(position,element);  
 }
 
  /**
   *  The get method, just use the get method in TreeMap to get element in the index of position. 
   */ 
 public T get(int position) {
  return treemap.get(position); 
 }

  /**
   *  The iterator method will return an parameter whose class is Iterator<IIndexedData<T>>. 
   */ 
 public Iterator<IIndexedData<T>> iterator() {
  return new TreeIndexedIterator<T>(treemap);
 } 
}

 /**
  *  The TreeIndexIterator method just use the iterator in the treemap  
  */ 
class TreeIndexedIterator<T> implements Iterator<IIndexedData<T>> {
 Iterator<Entry<Integer, T>> it;
 
 /**
  * The constuct of TreeIndexedIterator to create a iterator by TreeMap
  */ 
 public TreeIndexedIterator(TreeMap<Integer, T> treemap) {
  it = treemap.entrySet().iterator();
 }
 
 /**
  * The next method to find if it has next value in treemap, also use the internal method in TreeMap
  */ 

 public boolean hasNext() {
  return it.hasNext();
 }

 /**
  * use TreeMap internal method to find next element and return
  */ 
 public IIndexedData<T> next() {
  Entry<Integer, T> entry = it.next();
  return new IndexDataClass<T>(entry.getKey(), entry.getValue());
 }

 /**
  * use TreeMap internal method to remove an element
  */ 
 public void remove() {
  it.remove();
 }
}


