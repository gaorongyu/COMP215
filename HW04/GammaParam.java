/*
 * This holds a parameterization for the gamma distribution
 */
class GammaParam {
 
  private double shape, scale, leftmostStep;
  private int numSteps;
  
  public GammaParam (double shapeIn, double scaleIn, double leftmostStepIn, int numStepsIn) {
    shape = shapeIn;
    scale = scaleIn;
    leftmostStep = leftmostStepIn;
    numSteps = numStepsIn;  
  }
  
  public double getShape () {
    return shape;
  }
  
  public double getScale () {
    return scale;
  }
  
  public double getLeftmostStep () {
    return leftmostStep;
  }
  
  public int getNumSteps () {
    return numSteps;
  }
}
