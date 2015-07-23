/**
 *  the matrix stores double number and the matrix is dependent on row
 *  in other words, the row is sparse array, the column is double vector.
 *  
 */ 
public class RowMajorDoubleMatrix extends ADoubleMatrix{
 
 /**
  *  declare an sparase array which stores the object of IDoubleVector 
  */   
 ISparseArray<IDoubleVector> row_major; 

 /**
  *  The construct methods extends the super abstract method, and initialize the sparse array "rows_major"
  *  Using a loop make each slot of rows_major storing an object of sparse double vector
  */ 
 public RowMajorDoubleMatrix(int rows, int colums, double value) {
  super(rows, colums, value);
  row_major = new LinearSparseArray<IDoubleVector>(rows);
  for (int i = 0; i<rows; i++)
   row_major.put(i, new SparseDoubleVector(colums,value));
 }
 
  /**
   *  because the matrix is rows major, so just directly return the row_major
   */
 @Override
 public IDoubleVector getRow(int i) throws OutOfBoundsException {
  // if the index out of bound then throw the exception 
  if ((i < 0) || (i >= rows)) throw new OutOfBoundsException("x");
  return row_major.get(i);
  
 }

 @Override
 public IDoubleVector getColumn(int j) throws OutOfBoundsException {
  // if the index out of bound then throw the exception 
  if ((j < 0) || (j >= colums)) throw new OutOfBoundsException("x");
  /**
   *  because the matrix is rows major, so I have to create a doublevector to store each double in the index J of each line
   */
  IDoubleVector temp = new SparseDoubleVector(rows,0); 
  for (int i=0; i<rows; i++)
  {
   temp.setItem(i, row_major.get(i).getItem(j));
  }
  return temp;
  
 }

 @Override
 public void setRow(int i, IDoubleVector setToMe) throws OutOfBoundsException {
 // if the index out of bound and , especially, when the length of the vecotr is not equal to colums, then throw the exception  
  if ((i < 0) || (i >= rows) || (setToMe.getLength() != colums)) throw new OutOfBoundsException("x");
 /**
  *  because the matrix is rows major, so I just directly set the rows major 
  */  
  row_major.put(i, setToMe);  
 }

 @Override
 public void setColumn(int j, IDoubleVector setToMe) throws OutOfBoundsException {
  // if the index out of bound and , especially, when the length of the vecotr is not equal to colums, then throw the exception  
  if ((j < 0) || (j >= colums) || (setToMe.getLength() != rows)) throw new OutOfBoundsException("x");
  /**
   *  because the matrix is rows major, so I have to get each doublevector to set the value in index i
   */
  for (int i=0; i<rows; i++)
  {
   row_major.get(i).setItem(j, setToMe.getItem(i));
  }  
 }

 @Override
 public double getEntry(int i, int j) throws OutOfBoundsException {
  // if the index out of bound and , especially, when the length of the vecotr is not equal to colums, then throw the exception 
  if (((i<0) || (i >= rows) || (j < 0) || (j >= colums))) throw new OutOfBoundsException("x");
 /**
  *  because the matrix is rows major, so I just directly set the colums major 
  */
  return row_major.get(i).getItem(j);
 }

 @Override
 public void setEntry(int i, int j, double setToMe)
   throws OutOfBoundsException {
  // if the index out of bound and , especially, when the length of the vecotr is not equal to colums, then throw the exception 
  if (((i<0) || (i >= rows) || (j < 0) || (j >= colums))) throw new OutOfBoundsException("x");
  /**
   *  just get colums and set the value on index i 
   */   
  row_major.get(i).setItem(j,setToMe);  
 }

}
