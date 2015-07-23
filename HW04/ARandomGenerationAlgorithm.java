import java.util.Random;


public abstract class ARandomGenerationAlgorithm<OutputType> implements
  IRandomGenerationAlgorithm<OutputType> {
 /**
  * create an variable ran of interface IPRNG, which will equal the object created by class PRNG or by interface IPRNG
  */ 
  public IPRNG  ran;
 /**
 * This constructor is called when we want an ARandomGenerationAlgorithm
 * who uses the default pseudo-random number generator; in our case, this
 * is the class PRNG, and it will be initialized with mySeed
 */
 protected ARandomGenerationAlgorithm (long mySeed){
  ran = new PRNG(mySeed);
 }
 /**
 * This one is called when we want an ARandomGenerationAlgorithm who uses
 * a specific pseudo-random number generator
 */
 protected ARandomGenerationAlgorithm (IPRNG useMe){
  ran = useMe;
 }
 /**
 * Generate another random object
 */
 abstract public OutputType getNext ();
 /**
 * Resets the sequence of random objects that are created. The net effect
 * is that if we create the IRandomGenerationAlgorithm object,
 * then call getNext a bunch of times, and then call startOver (), then
 * call getNext a bunch of times, we will get exactly the same sequence
 * of random values the second time around.
 */
 public void startOver () {
  ran.startOver();  
 }
 /**
 * Returns a reference to the PRNG that this guy is currently using
 */
 protected IPRNG getPRNG () {
  return ran;
  
 }
 /**
 * Generate a random number uniformly between low and high using the
 * appropriate PRNG (this object’s or its parent’s)
 */
 protected double genUniform(double low, double high) {
   /**
    *  just use a simple sentence to produce uniform RV between two numbers
    */ 
  return ran.next()*(high - low)+low;
 }
 
}
