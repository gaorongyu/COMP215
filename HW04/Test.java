public class Test{
  public static void main(String[] args){
    
    /*double shape = 0.5;
    double scale = 1.0;
    UnitGamma myRNG1 = new UnitGamma (745664, new GammaParam (shape, scale, 10e-40, 150));
    IRandomGenerationAlgorithm<Double> myRNG2 = new UnitGamma (232, new GammaParam (shape, scale, 10e-40, 150));*/
    //double result1 = checkMean (myRNG1, 0, false, "");
    //double result2 = checkMean (myRNG2, 0, false, "");
    
   //for (int i=0; i <150; i++)
   //{
   
   GammaParam x = new GammaParam(0.5,1.0,10e-40,150); 
    
   UnitGamma myRNG = new UnitGamma(745664,x);
    
   for (int i=0; i<150; i++)
     System.out.println();
}
}