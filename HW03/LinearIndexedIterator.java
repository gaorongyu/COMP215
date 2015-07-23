import java.util.Iterator;
import java.util.Vector;


/**
 * the class implements Iterator<IIndexedData<T>>, to realize the iterator function
 */ 
public class LinearIndexedIterator<T> implements Iterator<IIndexedData<T>> {
 
 private int[] indices; 
 private Vector<T> data;
 private int len;
 private int i = 0;
 
 
 /**
  * the construct method will be pass into three parameters to initialize arguments
  * 
  * @ data    the vector to put elements
  * @ indices the array to put position
  * @ len     the length of the initial array
  */ 
 public LinearIndexedIterator(Vector<T> data , int[] indices, int len){
  this.data = data;
  this.indices = indices;
  this.len = len;  
 }
 
 
 /**
  *  the method to find if it has next element in indices, 
  * 
  *  @ return  boolean, if there is an element, returns TRUE. 
  */ 
 
 @Override
 public boolean hasNext() {
  /**
   *  if index in the array equals the length of array, that means there is the end of the array.
   */ 
   if (i == len)
    return false;
   /**
    * because I initialize the element in array equals -1,so I can judge if it has next element according to the next value equals -1 or not
    * if the element in indices equals -1, that means there is no element in array 
    */ 
  else if (indices[i] == -1)
    return false;
  /**
   *  if there is element left in array, the index will plus one to find next value 
   */ 
  else
  {
   i++;
   return true;
  }
 }

 /**
  *  the method use array[index] and the get method in vector to return the element in sparse array.
  * 
  *  @ return  IIndexedData, 
  */ 
 
 @Override
 public IIndexedData<T> next() {
  return new IndexDataClass<T>(indices[i-1],data.get(i-1));
 }

 
 /**
  *  no neccessary to override remove method in this assignment, or I can throw an excepton when anyone want to remove an element
  * 
  *  @ return  just return 
  */ 
 @Override
 public void remove() {
  // TODO Auto-generated method stub
  return ;

 }

}
