
/**
 * Interface to a pseudo random number generator that generates
 * numbers between 0.0 and 1.0 and can start over to regenerate
 * exactly the same sequence of values.
 */

interface IPRNG {
  /**
   * Return the next double value between 0.0 and 1.0
   */
  double next();
  
  /**
   * Reset the PRNG to the original seed
   */
  void startOver();
}