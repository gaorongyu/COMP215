import java.util.Arrays;
/**
 *   the class to reject the RV of the type Multinomial
 */

public class Multinomial extends ARandomGenerationAlgorithm <IDoubleVector>{  
  private int times; // the times to try to catch the ball
  private IDoubleVector probs ; // the probs of the type of interface Idoublevector 
  private double [] random; // a double array to store uniform RV
  private double [] back_probs; // a double array to store back_probs, which means the proportions of the colors of ball will be catched
  private int len; // the length OF the passed probs 
  private IDoubleVector result; // the vector of the interface IDoubleVector to store the final result
  
  /**
   * this construct use seed to initialize all the variables.
   */
  public Multinomial (long mySeed, MultinomialParam myParam){
    super(mySeed);
    times = myParam.getNumTrials();
    probs = myParam.getProbs();
    len = probs.getLength();    
    random = new double [times];  
    back_probs = new double [len];
  }
  
  /**
   * this construct use passed PRNG useMe to initialize all the variables.
   */
  public Multinomial (IPRNG useMe, MultinomialParam myParam){
    super(useMe);
    times = myParam.getNumTrials();
    probs = myParam.getProbs();
    len = probs.getLength();
    random = new double [times];
    back_probs = new double[len];
  }
  
  @Override 
  /**
   * to get and return the final result
   */ 
  public IDoubleVector getNext(){
    double sum = 0.0;
      /**
       * use getUniform method inherited by the super class to generate uniform randoms and store in the double array ranodm
       */ 
       for (int i = 0; i < times; i++){
         double rv = genUniform(0, 1);
         random[i] = rv;
       }
      //according to the request in PDF to sort all the array
       Arrays.sort(random);
       try{
         /**
          *  this loop is similar to the unitgamma class, the unitgamma class to sum all the square, this is to sum all the proporbilities.
          */ 
        for (int i = 0; i < len; i++){
          sum = sum + probs.getItem(i);
          back_probs[i] = sum;
        }
      }
      catch(OutOfBoundsException e){
        e.printStackTrace(); 
      }
      /**
       *  initialize the result vector to store the result and return, the length of the vector is same with the probs, and the type is dense double vector 
       */ 
      result = new DenseDoubleVector(len, 0.0);
      try{
       /**
         * if the random value is less than the position which has the sum of proporbiliteis, the position will increase the number one on the special position
         */
        int a = 0;
        for (int i = 0; i < times; i++){
          // to judge the random and the probability
          while (random[i] >= back_probs[a])
            a++;
          result.setItem(a, result.getItem(a)+1);
            }
          }
      catch(OutOfBoundsException e){
        e.printStackTrace(); 
    }
      return result;
  }
}