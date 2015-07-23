/**
 *   the class to create the RV of dirichlet type
 */
public class Dirichlet extends ARandomGenerationAlgorithm <IDoubleVector> {
   private long seed; // the seed to initialize RV type
   private int len; // the length of the shapes
   private IDoubleVector shapes; // the vector to store each shapes
   private SCGamma[] gamma; // an array to be used to store type of gamma
   private double[] shape; // the double array to store each value of shapes 
   private IDoubleVector store; // the vector to store the final vector
 
  /**
   *  the consturct to intialize each variable 
   */ 
  public Dirichlet (long mySeed, DirichletParam myParam){
  super(mySeed);
  seed = mySeed;
  shapes = myParam.getShapes();
  len = shapes.getLength();
  shape = new double[len];
  gamma = new SCGamma[len];
  /**
   *  put each value of shapes into array shape
   */ 
  store = new DenseDoubleVector (len, 0.0);
  for (int i=0; i<len; i++)
  {
    try {
  shape[i] = shapes.getItem(i);
 } catch (OutOfBoundsException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
 }
  }
  /**
   *  wrapper the realted variable into gamma parameter
   *  and create all the gamma sequence in the array to call the getNext method later 
   */ 
  for (int i=0; i<len; i++)
  {
      GammaParam gp = new GammaParam(shape[i],1.0,myParam.getLeftmostStep (), myParam.getNumSteps ());
      gamma[i] = new SCGamma(getPRNG(), gp);
  }
  }
 
 /**
  *  the other consturct to intialize each variable 
  */  
 public Dirichlet (IPRNG useMe, DirichletParam myParam){
  super(useMe);
  shapes = myParam.getShapes();
  len = shapes.getLength();
  shape = new double[len];
  gamma = new SCGamma[len];
  
 }
 
 /**
  * the method to restart all the gamma sequence in the array
  */ 
   public void startOver () {
     for (int i = 0; i < len ; i++)
       gamma[i].startOver();
 }

 /**
  * return the final value
  */ 
 @Override
 public IDoubleVector getNext() {
  /**
   * if call the gamma sequence to produce a random variable, the progrme will store the rv into the densedoublevector store immediately
   */ 
  for (int i = 0; i<len;i++)
 try {
  store.setItem(i, gamma[i].getNext());
 } catch (OutOfBoundsException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
 }
  /**
   *  after produce a vector including an array of rv, the program will normailze all the rv, and the result will be returned
   */ 
  store.normalize();
  
  return store;
  
 }

}
