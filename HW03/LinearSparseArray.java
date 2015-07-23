import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

/**
 * the linear sparse array class, which is also implements isparsearray
 * the class has two fields, one is an array, the other is an vector
 * the index of the element in vector is the same with the index of the positoin in array, here the positoin should be pair with the array 
 */ 

public class LinearSparseArray<T> implements ISparseArray<T> {
 
 public int [] indices; // an array to store the position
 public Vector<T> data; // the vector which can store the element
 private int len;  // the length of the whole array lenth
 private int index; // the index which is paired with the position in array and with the element in vector
 private int max; // the max which will be equal to the max position in put method
 private int flag; // the index flag which can be used in get method 
 
 /**
  * the construct to initialize all the parameters to zero
  */ 
 public LinearSparseArray(int n){
  data = new Vector<T>(n);
  indices = new int[n];
  Arrays.fill(indices, -1);
  len = n;
  index = 0;
  max = 0;
  flag = 0;
 }

 
 /**
  * the put method which can quickly put all the element is vector and all the position in array, all the position will be sorted by my code.
  */ 
 @Override
 public void put(int position, T element) {
   /** 
    * the first thing is judge whether the array has been used.
    * if the array has almost used up, that means that the index puls one is equal to the length of array.
    * then the array indices will double its capacity.
    * The process of double is create a new array which has double capacity
    * and then copy all the element in indices to the new array, then redirect the indices to new array.
    */ 
    if ( index+1 == len )
  {
   int[] indices_back = new int[len*2];
   Arrays.fill(indices_back,-1);
   for (int x=0; x < index; x++)
    indices_back[x] = indices[x];
   indices = indices_back;
   len = len*2;
  }
    
   /** 
    * When index equals zero, there is a special situation, because nothing is the array and vector.
    * This is the beginning of the code, I prefer to seprate the first setp with other code.
    * After the first step, we can let the parameter "max" equals position
    * and the index will become one, because it will point to the second slot in the array "indices" and vector "data"
    */ 
   
   if (index == 0)
   {
     data.add(index,element);
     indices[index] = position;
     max = position;
     index++;
     return;
   }
   
   /** 
    * In the put method, there are many situations
    * The first situation is position which will be put equals to the max position which has been put in the array. 
    * In this situation, what we need to do is just set the element in the vector again.
    * Please note, the index should substract one, because index is point to the next slot of array and vector.
    */ 
   
   if (position == max)
  {   
    data.set(index-1,element);
    return;
  }
  
   /** 
    * The second situation is position which will be put is bigger than the max position which has been put in the array.
    * Then what we need to do is just put the position in the last slot in the array, which has been pointed by index.
    * At the same time, we will add the element into the last slot in the vector.
    * Also, we need to set the parameter "max" to the new max position, and let the index plus one to point the next slot.
    */ 
   
  else if (position > max)
  {
    data.add(index,element);
    indices[index] = position;
    index++;
    max = position;
    return;
  }
  
  /** 
    * The third situation is a little more complex, the postion is smaller than the max positioin exsited in the array.
    */ 
      
  else if (position < max)
  {
    /**
     * If index equal one, the method should be seperate from the loop.
     * Because, if index equals one, the loop cannot run, so nothing will happen in the loop
     * the postion in the index and the element in the array will not be repalced. 
     */ 
    
    if (index == 1)
   {
    /**
     *  The the first position in indices equals to the position which will be put.
     *  The first element in vector will be replaced.
     */ 
     if (indices[0] == position)
   {
    data.set(0,element);
    return;
   } 
    /**
     *  The first position is bigger than the position which will beput
     *  The second position will equal to the first position, and the first position will be repalced to the new position.
     *  Vector will add new element in the zero position.
     */ 
   else if (indices[0] > position)
   {
    indices[index] = indices[0];
    indices[0]=position;
    data.add(0,element);
    index++;
    return;
   }
    }
    
    for (int i=0; i < index-1; i++)
  {
    if (indices[i] == position)
   {
    data.set(i,element);
    return;
   }
   /* If the first position in indices is bigger than the position which will be put
    * That means we need to shift all the slots in array backward one step.
    * The process is make the every latter slot equals the former one.
    * We do not need worry about vector "data", because the add method in vector can automatically shift the slot in vector backward one step.
    */     
    else if (indices[0] > position)
    {
    int m; // the paramenter equals to the index, the last slot in array, will be used in loop
    for (m = index; m > i; m--)
    {
      indices[m] = indices[m-1];
    }
    indices[m]=position; 
    data.add(i,element); // the add method of vector
    index++;
    return;     
    }
   /* If some position in the middle of indices is bigger than the position which will be put
    * That means we need to shift the slots behind index in the slot in array backward one step.
    * The process is almost totally similar with the former process, the only differnce is m should bigger than i plus one
    * otherwise the method replace the first middle slot in mistake.
    */ 
    
   else if (indices[i]<position && indices[i+1]>position)
   {
    int m;
    for (m = index; m > i+1; m--)
    {
      indices[m] = indices[m-1];
    }
    indices[m]=position;
    data.add(i+1,element);
    index++;
    return;
   }
  }
  }


 }
 
 @Override
 /**
  *  Compare with the put method, the get method is much easier, because there are not so many situations here.
  */ 
 
 public T get(int position) {
  /**
   *  If someone wants to get a position of which the value is less than zero, the code will return null 
   */  
  if(position < 0) return null;
  
 /**
   *  If position bigger than the position of indices, we will only to search the indices bigger than flag index.
   *  use the math just to ensure the when the first get will begin from the first slot in array.
   */  
  if(position >= indices[Math.max(0, flag)]) {
   /**
    * the loop will start from the flag index and search all the postion bigger than indices[flag], until find the same postion
    * and set the flag to the new index, then in the next time, the next loop can begin at the new index.
    */ 
   for(int i = Math.max(0, flag); i < len; i++) {
    if(indices[i] == position) {
     flag = i;
     return data.get(i);
    }
    /**
     *  an very important judge, which can save much much time in some special situation.
     *  if indices[i] bigger than position, that means the postion will not appear in the whole array
     *  because all the position has been sorted from small to big, once the indices[i] is bigger than the position, 
     *  all the value in the indiecs will bigger than the position, so the code can return null immediately.
     * 
     *  another situation is judge whether the indices[i] equals -1 or not, because once the indices[i] equals -1,
     *  that means no new position in the array, and it is no neccessary to continue search backward.
     */     
    if(indices[i] > position || indices[i] == -1)
     return null;
   }
   
  /**
   * the other get situation is the position which will be get is less than the indices[i]
   * in this situation, the code will use for loop to search the correct position from zero to flag index
   * once find the position, the flag will change to the latest position and return the element on the index
   */ 
  } else {
   for(int i = flag; i >= 0; i--) {
    if(indices[i] == position) {
     flag = i;
     return data.get(i);
    }
    
    /**
     * this situation also need to be optimized to save time, if indices[i] is smaller than the positoin, it is also not neccessary to continue looping.
     * but compare with the former situation,we do not need to take into account the index bigger than the last index of the last position.
     */ 
    if(indices[i] < position) return null;
   }
  }
  
  return null;
 }
 
 /**
  * The Iterator method, the function of this method has been commented in the iterator class
  */ 
 @Override
 public Iterator<IIndexedData<T>> iterator() {
  return new LinearIndexedIterator<T>(data,indices,len); // just pass three parameters into the function.
 }


}
