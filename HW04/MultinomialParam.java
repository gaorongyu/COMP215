/**
 * This holds a parameterization for the multinomial distribution
 */
class MultinomialParam {
 
  int numTrials;
  IDoubleVector probs;
  
  public MultinomialParam (int numTrialsIn, IDoubleVector probsIn) {
    numTrials = numTrialsIn;
    probs = probsIn;
  }
  
  public int getNumTrials () {
    return numTrials;
  }
  
  public IDoubleVector getProbs () {
    return probs;
  }
}
