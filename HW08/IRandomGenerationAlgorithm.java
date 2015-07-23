/** 
 * Encapsulates the idea of a random object generation algorithm.  The random
 * variable that the algorithm simulates outputs an object of type OutputType.
 * Every concrete class that implements this interface should have a public 
 * constructor of the form:
 * 
 * ConcreteClass (Seed mySeed, ParamType myParams)
 * 
 */
interface IRandomGenerationAlgorithm <OutputType> {
  
  /**
   * Generate another random object
   */
  OutputType getNext ();
  
  /**
   * Resets the sequence of random objects that are created.  The net effect
   * is that if we create the IRandomGenerationAlgorithm object, 
   * then call getNext a bunch of times, and then call startOver (), then 
   * call getNext a bunch of times, we will get exactly the same sequence 
   * of random values the second time around.
   */
  void startOver ();
  
}
