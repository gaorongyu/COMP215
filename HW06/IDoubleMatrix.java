
/**
 * This interface corresponds to a sparse implementation of a matrix
 * of double-precision values.
 */
interface IDoubleMatrix {
 
  /** 
   * This returns the i^th row in the matrix.  Note that the row that
   * is returned may contain one or more references to data that are
   * actually contained in the matrix, so if the caller modifies this
   * row, it could end up modifying the row in the underlying matrix in
   * an unpredicatble way.  If i exceeds the number of rows in the matrix
   * or it is less than zero, an OutOfBoundsException is thrown.
   */
  IDoubleVector getRow (int i) throws OutOfBoundsException;
  
  /** 
   * This returns the j^th column in the matrix.  All of the comments
   * above regarding getRow apply.  If j exceeds the number of columns in the
   * matrix or it is less than zero, an OutOfBoundsException is thrown.
   */
  IDoubleVector getColumn (int j) throws OutOfBoundsException;
  
  /**
   * This sets the i^th row of the matrix.  After the row is inserted into
   * the matrix, the matrix "owns" the row and it is free to do whatever it
   * wants to it, including modifying the row.  If i exceeds the number of rows
   * in the matrix or it is less than zero, an OutOfBoundsException is thrown.
   */
  void setRow (int i, IDoubleVector setToMe) throws OutOfBoundsException;
  
  /**
   * This sets the j^th column of the matrix.  All of the comments above for
   * the "setRow" method apply to "setColumn".  If j exceeds the number of columns
   * in the matrix or it is less than zero, an OutOfBoundsException is thrown.
   */
  void setColumn (int j, IDoubleVector setToMe) throws OutOfBoundsException;
  
  /**
   * Returns the entry in the i^th row and j^th column in the matrix.
   * If i or j are less than zero, or if j exceeds the number of columns
   * or i exceeds the number of rows, then an OutOfBoundsException is thrown.
   */
  double getEntry (int i, int j) throws OutOfBoundsException;
  
  /**
   * Sets the entry in the i^th row and j^th column in the matrix.
   * If i or j are less than zero, or if j exceeds the number of columns
   * or i exceeds the number of rows, then an OutOfBoundsException is thrown.
   */
  void setEntry (int i, int j, double setToMe) throws OutOfBoundsException;
  
  /**
   * Returns the number of rows in the matrix.
   */
  int getNumRows ();
  
  /**
   * Returns the number of columns in the matrix.
   */
  int getNumColumns ();
  
}
