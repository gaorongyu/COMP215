/**
 *  the matrix stores double number and the matrix is dependent on column
 *  in other words, the column is sparse array, the row is double vector.
 *  
 */ 
public class ColumnMajorDoubleMatrix extends ADoubleMatrix{
  
  
 /**
  *  declare an sparase array which stores the object of IDoubleVector 
  */ 
 ISparseArray<IDoubleVector> colums_major;
 
 /**
  *  the construct methods extends the super abstract method, and initialize the sparse array "colums_major"
  *  use a loop make each slot of colums_major storeing an object of sparse double vector
  */ 
 public ColumnMajorDoubleMatrix(int rows, int colums, double value) {
  super(rows, colums, value);
  colums_major = new LinearSparseArray<IDoubleVector>(colums);
  for (int i = 0; i < colums; i++)
   colums_major.put(i, new SparseDoubleVector(rows,value));
 }

 /**
  * the method to return all the object in one specific row 
  */ 
 @Override
 public IDoubleVector getRow(int i) throws OutOfBoundsException {
  // if the index out of bound then throw the exception 
  if ((i < 0) || (i >= rows)) throw new OutOfBoundsException("x");
  /**
   *  because the matrix is colums major, so I have to create a doublevector to store each double in the index J of each line
   */
  IDoubleVector temp = new SparseDoubleVector(colums,0); 
  for (int j=0; j<colums; j++)
  {
   temp.setItem(j, colums_major.get(j).getItem(i));
  }
  return temp;
 }

 @Override
 public IDoubleVector getColumn(int j) throws OutOfBoundsException {
  // if the index out of bound then throw the exception 
  if ((j < 0) || (j >= colums)) throw new OutOfBoundsException("x");
  // beacuse the matrix is colums major, so just return the object of index j in the sparse array
  return colums_major.get(j);
 }

 @Override
 public void setRow(int i, IDoubleVector setToMe) throws OutOfBoundsException {  
  // if the index out of bound and , especially, when the length of the vecotr is not equal to colums, then throw the exception 
  if ((i < 0) || (i >= rows) || (setToMe.getLength() != colums)) throw new OutOfBoundsException("x");
  /**
   *  because the matrix is colums major, so I have to get each doublevector to set the value in index i
   */
  for (int j = 0; j < colums; j++)
  {
   colums_major.get(j).setItem(i, setToMe.getItem(j));
  }
  
 }

 @Override
 public void setColumn(int j, IDoubleVector setToMe)  throws OutOfBoundsException {
  // if the index out of bound and , especially, when the length of the vecotr is not equal to rows, then throw the exception 
 if ((j < 0) || (j >= colums) || (setToMe.getLength() != rows)) throw new OutOfBoundsException("x");
 /**
  *  because the matrix is colums major, so I just directly set the colums major 
  */
  colums_major.put(j, setToMe);
  
 }

 @Override
 public double getEntry(int i, int j) throws OutOfBoundsException {
  // if the index out of bound and , especially, when the length of the vecotr is not equal to colums, then throw the exception 
   if (((i<0) || (i >= rows) || (j < 0) || (j >= colums))) throw new OutOfBoundsException("x");
  /**
   *  just get colums and rows
   */ 
  return colums_major.get(j).getItem(i);
 }

 @Override
 public void setEntry(int i, int j, double setToMe)
   throws OutOfBoundsException {
  // if the index out of bound and , especially, when the length of the vecotr is not equal to colums, then throw the exception 
  if (((i<0) || (i >= rows) || (j < 0) || (j >= colums))) throw new OutOfBoundsException("x");
  /**
   *  just get colums and set the value on index i 
   */ 
  colums_major.get(j).setItem(i,setToMe);
 }

}
