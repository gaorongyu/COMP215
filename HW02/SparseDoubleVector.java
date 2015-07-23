import java.util.Iterator;

 /*
  *  The sparsedoublevector has a sparse dense double array, so the length of both are not equal. 
  *  The length given in the paramenter is the lenght of vector, but it is not the length of sparse array.
  *  Similar with dense double array, we do not need to assign the initial value to each slot, just store the initial value into the back value.
  *  Every time when need to save a  number on a index, just save the number substract back value.
  *  And when we need to get a number on a index, just get the number plus the back value.
  *  All the other methods are based on the back value
  */ 
  

public class SparseDoubleVector implements IDoubleVector

{
 private ISparseArray<Double> sparse_double_array;
 private double back_value;
 private int len; // the length of the vector rather than the sparse double array

 /*
  *  The constructor of the sparse double vector, the initial length is the lenght of the whole vector and the initial value will be stored into the back value.
  *  So the initialized sparse double array is an empty array, actually it is an empty container. 
  *  Only when set an item into the array, there will be an element existing in the array. 
  */ 
 
 public SparseDoubleVector(int len, double value) {
  sparse_double_array = new SparseArray<Double>();
  this.back_value = value;
  this.len = len;
 }

 @Override
 /*
  * The first step is to throw an exception, when the length of both arrays are not equal.
  * The second step is using the interator to search all the element exsiting in the array, and  only add those exsited element to addToHim array.
  * The third step is using the method named addToAll to let the back value of addToHim to add the back value of sparse array.
  * After above three steps, when getting the number of addToHim, we can get the sum of each number of both arrays 
  */ 
 public void addMyselfToHim(IDoubleVector addToHim)
   throws OutOfBoundsException {
  int n = addToHim.getLength();
  
  if(n != len) throw new OutOfBoundsException("x");
  
  Iterator<IIndexedData<Double>> iterator = sparse_double_array.iterator();
  while (iterator.hasNext()) {
   IIndexedData<Double> x = iterator.next();
   addToHim.setItem (x.getIndex(), x.getData() + addToHim.getItem(x.getIndex()));
  }
  addToHim.addToAll(back_value);
  
 }

 @Override
 /*
  * Similar with dense double vector, throws an exception depend on the situation and return the correct the number on the index 
  */ 
 public double getItem(int whichOne) throws OutOfBoundsException {
  if(whichOne < 0 || whichOne >= len)
   throw new OutOfBoundsException("x");
  if (this.sparse_double_array.get(whichOne) == null)
   return back_value;
  else
   return this.sparse_double_array.get(whichOne) + back_value;   
 }

 @Override
 /*
  * Using back value to add extra number, then the programme can realize the method for implemention must be O(1) 
  */ 
 public void addToAll(double addMe) {
  this.back_value = this.back_value + addMe;
 }

 @Override
 /*
  * The method is similar with that of the dense double array, get the number and plus back value to return the round number, 
  */ 
 public long getRoundedItem(int whichOne) throws OutOfBoundsException {
  if(whichOne < 0 || whichOne >= len)
   throw new OutOfBoundsException("x");

   return (long) (Math.round(this.sparse_double_array.get(whichOne)+back_value));

 }

 @Override
 /*
  * Similar with the dense double array, just use iterator to get and set the number.
  */ 
 public void normalize() {
   
  double norm = this.l1Norm();
  Iterator<IIndexedData<Double>> iterator = sparse_double_array
    .iterator();
  while (iterator.hasNext()) {
    IIndexedData<Double> x = iterator.next();
    this.sparse_double_array.put(x.getIndex(), x.getData()/norm);
  }
  back_value = back_value / norm;
 }

  


 @Override
 /*
  *  Because of the back value, each element which is going to be stored should substract the back value 
  */
   
 public void setItem(int whichOne, double setToMe)
   throws OutOfBoundsException {
  if(whichOne < 0 || whichOne >= len)
   throw new OutOfBoundsException("x");
  this.sparse_double_array.put(whichOne, (setToMe - back_value));

 }

 @Override
 /*
  *  return the length 
  */ 
 public int getLength() {
  return len;
 }

 @Override
 /*
  * Similar with the dense double vector , but using the iterator to add all the  unempty number and add the product of length and back value.
  */ 
 public double l1Norm() {
  Iterator<IIndexedData<Double>> iterator = sparse_double_array .iterator();
  double sum = len * back_value;
  while (iterator.hasNext()) {
   sum += iterator.next().getData();
  }
  return sum;
 }
 
  /**
  * Constructs and returns a new "pretty" String representation of the vector.
  * 
  * @return the string representation
  * 
  */
 public String toString() {
  String doubleString = "";
  Iterator<IIndexedData<Double>> iterator = sparse_double_array .iterator();
  int i = 0;
  while (iterator.hasNext()){ 
   doubleString = doubleString + " " + Double.toString(iterator.next().getData()+back_value);
   i++;
  }
  for (int x = 0; x < len - i; x ++)
      doubleString = doubleString + " " + Double.toString(back_value);    
  return doubleString;
 }
}
