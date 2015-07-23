public class DenseDoubleVector implements IDoubleVector {

 /*
  *  The densedoublevector has a simple dense double array, so the length of both are equal. 
  *  But to save time, we do not need to assign the initial value to each slot, just store the initial value into the back value.
  *  Every time when need to save a  number on a index, just save the number substract back value.
  *  And when we need to get a number on a index, just get the number plus the back value.
  *  All the other methods are based on the back value
  */ 
  
 private double[] dense_double_vector;
 private int len;
 private double back_value;

 /**
  * The constructor of DenseDoubleVector Two arguments, one is its length,
  * and the other is the initial value of the argument
  */
 public DenseDoubleVector(int len, double value) {
  dense_double_vector = new double[len];
  this.len = len;
  this.back_value = value;
 }

 /**
  * Adds another double vector to the contents of this one. Will throw OutOfBoundsException if the two vectors don't have exactly the same sizes.
  * Because the length of the dense double vector is equal to dense double array, we just use simple loop to finish this method.
  * 
  * @param addThisOne    the double vector to add in
  */
 public void addMyselfToHim(IDoubleVector addToHim)
   throws OutOfBoundsException {
  /**
   * if the length of both array  are not equal, the programme will throw the expception.
   */ 
  int n = addToHim.getLength();
  if(n != len) throw new OutOfBoundsException("x");
   /**
   * if the length are equal, just use loop to add both elements of each solt then put it back on the addTohim array.
   */ 
  for(int i = 0; i < n; i++) {
   addToHim.setItem(i, this.getItem(i) + addToHim.getItem(i));
  }
 }

 /**
  * Returns a particular item in the double vector. Will throw  OutOfBoundsException if the index passed in is beyond the end of the  vector.
  * 
  * @param whichOne     the index of the item to return
  * @return             the value at the specified index
  */
 public double getItem(int whichOne) throws OutOfBoundsException {
  if(whichOne < 0 || whichOne >= len)
   throw new OutOfBoundsException("x");
  /**
   *  when return a number, we need to get the number on the index and should plus extra back_value.
   */
  return this.dense_double_vector[whichOne]+back_value;
 }

 /**
  * Add a value to all elements of the vector.
  * 
  * @param addMe      the value to be added to all elements
  * 
  * Because of setting the value, we only need to plus addMe with back_value, then the time for this operation can be only O(1)
  */
 public void addToAll(double addMe) {
   back_value = back_value+addMe;
  }

 /**
   *  get the number and give a Math function to get the round number
   */
 public long getRoundedItem(int whichOne) throws OutOfBoundsException {
  if(whichOne < 0 || whichOne >= len)
   throw new OutOfBoundsException("x");
  double x = (this.dense_double_vector[whichOne])+back_value;
  long y = (long) Math.round(x);
  return y;
 }

 /**
  *  Use a simple loop to normalize the array -- use every element in the slot divides by the norm
  *  Besides this, we also need to devide the back_vaulue, otherwise we cannot get a correct normalize array.
  */
 public void normalize() {
   /*
    *  It is neccesary to return the norm, otherwise a worng result number will be get if only use the norm method, 
    *  because the result of the method will change during the process of normalize.
    */ 
   double norm = this.l1Norm();
   for (int i=0; i < len ; i++)
     this.dense_double_vector[i] = ((this.dense_double_vector[i])/norm);
   back_value = back_value / norm ;
 }

 /**
  * Sets a particular item in the vector. Will throw OutOfBoundsException if
  * we are trying to set an item that is past the end of the vector.
  * 
  * @param whichOne     the index of the item to set
  * @param setToMe      the value to set the item to
  * 
  * Because of the back value, each element which is going to be stored should substract the back value
  */
 public void setItem(int whichOne, double setToMe)
   throws OutOfBoundsException {
  if(whichOne < 0 || whichOne >= len)
   throw new OutOfBoundsException("x");
  this.dense_double_vector[whichOne] = setToMe-back_value;
 }

 /**
  * Returns the length of the vector.
  * 
  * @return the vector length
  */
 public int getLength() {
  return this.len;
 }

 /**
  * Returns the L1 norm of the vector (this is just the sum of the absolute value of all of the entries)
  * 
  * @return the L1 norm
  * 
  * the norm is the abs of all the element, so the first step is the back value multipy the length
  * then add each element which is not equal to zero
  */
 public double l1Norm() {
  double norm = back_value*len;
  for (int i = 0; i < len; i++) 
    norm = Math.abs(this.dense_double_vector[i]) + norm;
  return norm;
 }

 /**
  * Constructs and returns a new "pretty" String representation of the vector.
  * 
  * @return the string representation
  * 
  */
 public String toString() {

  String doubleString = "";
  for (int i = 0; i < len; i++) {
   doubleString = doubleString + " " + Double.toString(this.dense_double_vector[i]+back_value);
  }
  return doubleString;
 }
}