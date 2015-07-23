
/**
 * This is thrown when someone tries to access an element in a DoubleVector that is beyond the end of 
 * the vector.
 */
class OutOfBoundsException extends Exception {
  static final long serialVersionUID = 2304934980L;

  public OutOfBoundsException(String message) {
    super(message);
  }
}
