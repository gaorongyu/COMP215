/*
 * This holds a parameterization for the Dirichlet distribution
 */
class DirichletParam {
 
  private IDoubleVector shapes;
  private double leftmostStep;
  private int numSteps;
  
  public DirichletParam (IDoubleVector shapesIn, double leftmostStepIn, int numStepsIn) {
    shapes = shapesIn;
    leftmostStep = leftmostStepIn;
    numSteps = numStepsIn;
  }
  
  public IDoubleVector getShapes () {
    return shapes;
  }
  
  public double getLeftmostStep () {
    return leftmostStep;
  }
  
  public int getNumSteps () {
    return numSteps;
  }
}
