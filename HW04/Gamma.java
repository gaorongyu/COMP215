/**
 *  the class to create RV of Gamma distribution, the difference between this and UnitGamma is the shape and theta will larger than one
 */

public class Gamma extends ARandomGenerationAlgorithm <Double> {
 private long seed;
 private double k;  // the shape
 private double m;  // the theta
 private double j;  // the decimal part of the shape 
 private SCUnitGamma UniG1;  // the unit gamma object to calculate the integer part of shape
 private SCUnitGamma UniG2;  // the unit gamma object to calculate the decimal part of shape
  
 /**
  *   the construct method to initialize three variables and two class
  */ 
 
 public Gamma(long mySeed, GammaParam myParam) {
  super(mySeed);
  // initialize seed
  seed = mySeed;
  // initialize three variables: shape, theta and decimal part of shape
  k = myParam.getShape();
  m = myParam.getScale();
  j = k - Math.floor(k);
  // a gama parameter to wrap the interge part of shape and theta is one
  GammaParam newParam1 = new GammaParam(1,1,myParam.getLeftmostStep (), myParam.getNumSteps ());
  // a gmam parameter to warp the decimal part of shapeand theta is one
  GammaParam newParam2 = new GammaParam(j,1,myParam.getLeftmostStep (), myParam.getNumSteps ());
  // two classes to produce two object to caculate integer part and decimal part of shpae 
  UniG1 = new SCUnitGamma(mySeed,newParam1);
  UniG2 = new SCUnitGamma(mySeed,newParam2);
  // TODO Auto-generated constructor stub
 }
 
 public Gamma (IPRNG useMe, GammaParam myParam)
 {
  super(useMe);
  // initialize three variables: shape, theta and decimal part of shape
  k = myParam.getShape();
  m = myParam.getScale();
  j = k - Math.floor(k);
  // a gama parameter to wrap the interge part of shape and theta is one
  GammaParam newParam1 = new GammaParam(1,1,myParam.getLeftmostStep (), myParam.getNumSteps ());
  // a gmam parameter to warp the decimal part of shapeand theta is one
  GammaParam newParam2 = new GammaParam(j,1,myParam.getLeftmostStep (), myParam.getNumSteps ());
  // two classes to produce two object to caculate integer part and decimal part of shpae 
  UniG1 = new SCUnitGamma(seed,newParam1);
  UniG2 = new SCUnitGamma(seed,newParam2);
  
 }

 /**
  * use the method of scgamma to restart two gamma object
  */ 
  public void startOver () {
  UniG1.startOver();
  UniG2.startOver();
 }

 @Override
 /**
  * to calculate gamma distribution RV
  */ 
 public Double getNext() {
  // create a double variable to cauculate RV
   double result = 0.0;
   // use a for loop to calculate many times to get the whole value of gamma RV when its shape is larger than one
   // the value is also need to multiply m, because its theta is also larger than one 
   for (int i=0; i<Math.floor(k); i++)
     result = result + UniG1.getNext()*m;
   result = result + UniG2.getNext()*m;
   // adds all the number to result and return it
   return result;
 }

}
