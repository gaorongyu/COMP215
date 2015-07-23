/**
 * This holds a parameterization for the uniform distribution
 */
class UniformParam {
 
  double low, high;
  
  public UniformParam (double lowIn, double highIn) {
    low = lowIn;
    high = highIn;
  }
  
  public double getLow () {
    return low;
  }
  
  public double getHigh () {
    return high;
  }
}
