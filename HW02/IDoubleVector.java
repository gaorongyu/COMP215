
/**
 * This is the interface for a vector of double-precision floating point values.
 */
interface IDoubleVector {
  
  /** 
   * Adds another double vector to the contents of this one.  
   * Will throw OutOfBoundsException if the two vectors
   * don't have exactly the same sizes.
   * 
   * @param addThisOne       the double vector to add in
   */
  public void addMyselfToHim (IDoubleVector addToHim) throws OutOfBoundsException;
  
  /**
   * Returns a particular item in the double vector.  Will throw OutOfBoundsException if the index passed
   * in is beyond the end of the vector.
   * 
   * @param whichOne     the index of the item to return
   * @return             the value at the specified index
   */
  public double getItem (int whichOne) throws OutOfBoundsException;

  /**  
   * Add a value to all elements of the vector.
   *
   * @param addMe  the value to be added to all elements
   */
  public void addToAll (double addMe);
  
  /** 
   * Returns a particular item in the double vector, rounded to the nearest integer.  Will throw 
   * OutOfBoundsException if the index passed in is beyond the end of the vector.
   * 
   * @param whichOne    the index of the item to return
   * @return            the value at the specified index
   */
  public long getRoundedItem (int whichOne) throws OutOfBoundsException;
  
  /**
   * This makes it so that the l1Norm of the vector is one, keeping the ratios among all of the 
   * items constant.
   */
  public void normalize ();
  
  /**
   * Sets a particular item in the vector.  
   * Will throw OutOfBoundsException if we are trying to set an item
   * that is past the end of the vector.
   * 
   * @param whichOne     the index of the item to set
   * @param setToMe      the value to set the item to
   */
  public void setItem (int whichOne, double setToMe) throws OutOfBoundsException;

  /**
   * Returns the length of the vector.
   * 
   * @return     the vector length
   */
  public int getLength ();
  
  /**
   * Returns the L1 norm of the vector (this is just the sum of the absolute value of all of the entries)
   * 
   * @return      the L1 norm
   */
  public double l1Norm ();
  
  /**
   * Constructs and returns a new "pretty" String representation of the vector.
   * 
   * @return        the string representation
   */
  public String toString ();
}



