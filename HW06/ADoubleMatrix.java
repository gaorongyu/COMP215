/**
 *  the abstract class of DoubleMatrix which has two concrete methods and six abstract methods
 *  the two concrete matrix methods will extends from this abstract class
 */ 
public abstract class ADoubleMatrix implements IDoubleMatrix {

 int rows; // the number of the rows in each matrix
 int colums; // the number of colums in each matrix
 double value; // the initial value in each matrix
 
 
 /**
  *  the construct method which will initialize the number of the rows, the colums and the value.
  */ 
 public ADoubleMatrix(int rows, int colums, double value)
 {
  this.rows = rows;
  this.colums = colums;
  this.value = value;
 }
 
 /**
  *  the overided method which will return the number of the rows, this method will be extended 
  */ 
 @Override
 public int getNumRows() {
  // TODO Auto-generated method stub
  return rows;
 }

 /**
  *  the overided method which will return the number of the colums, this method will be extended 
  */ 
 @Override
 public int getNumColumns() {
  // TODO Auto-generated method stub
  return colums;
 }

 /**
  *  six abstract method, because the rowmajormatrix and colummajormatrix will have different content
  */ 
 @Override
 abstract public IDoubleVector getRow(int i) throws OutOfBoundsException;

 @Override
 abstract public IDoubleVector getColumn(int j) throws OutOfBoundsException;

 @Override
 abstract public void setRow(int i, IDoubleVector setToMe) throws OutOfBoundsException;

 @Override
 abstract public void setColumn(int j, IDoubleVector setToMe) throws OutOfBoundsException;

 @Override
 abstract public double getEntry(int i, int j) throws OutOfBoundsException;
 
 @Override
 abstract public void setEntry(int i, int j, double setToMe) throws OutOfBoundsException;

}
