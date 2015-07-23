
public class UnitGamma extends ARandomGenerationAlgorithm <Double> {
  
  private double shape; // the shape of gama parameter
  private double scale; // the scale of gama parameter
  private double value; // the x need to caculate the curve
  private int len;    // the total number of step 
  private double[] square; // the array to store the square
  private double[] sum; // the array to store the sum of the squares
  private double[] each_x; // the double array of each value x resulted by pow(2,i)
  private double[] each_y; // the doulbe array of the value y resulted by each value x
  
  public UnitGamma(long mySeed, GammaParam myParam) {
  super(mySeed);
  shape = myParam.getShape();
  scale = myParam.getScale();
  value = myParam.getLeftmostStep();
  len = myParam.getNumSteps();
  ///////////////////////////////////////////
  each_x = new double[len+1];
  // calculate each x and put into the array
  for (int i=0;i<len+1;i++)
    each_x[i] = Math.pow(2,i)*value;
  ///////////////////////////////////////////
  each_y = new double[len];
  // calculate each y and put into the array
  for (int i=0;i<len;i++)
    each_y[i] = function(each_x[i]);
  ///////////////////////////////////////////
  square = new double[len];
  // calculate each square and put into the array
  for (int i=0;i<len;i++)
    square[i] = (each_x[i+1]-each_x[i])*each_y[i];
  ///////////////////////////////////////////
  //calculate each sum of the former square
  sum(len);
  
  
  // TODO Auto-generated constructor stub
 }
 
 public UnitGamma (IPRNG useMe, GammaParam myParam){
  super(useMe);
  shape = myParam.getShape();
  scale = myParam.getScale();
  value = myParam.getLeftmostStep();
  len = myParam.getNumSteps();
  ///////////////////////////////////////////
  each_x = new double[len+1];
  // calculate each x and put into the array
  for (int i=0;i<len+1;i++)
    each_x[i] = Math.pow(2,i)*value;
  ///////////////////////////////////////////
  each_y = new double[len];
  // calculate each y and put into the array
  for (int i=0;i<len;i++)
    each_y[i] = function(each_x[i]);
  ///////////////////////////////////////////
  square = new double[len];
  // calculate each square and put into the array
  for (int i=0;i<len;i++)
    square[i] = (each_x[i+1]-each_x[i])*each_y[i];
  ///////////////////////////////////////////
  //calculate each sum of the former square
  sum(len);
 }
 
 /**
  * the function of the unitgamma expression
  */ 
 private double function(double x){
   return (Math.pow(x,(shape-1))*(Math.pow(Math.E,(-x/scale))));
 }
 /**
  * calculate the sum of all the square and put each square in the array
  */
 private void sum(int len){
 sum = new double[len];
 sum[0] = square[0];
  for (int i = 1; i < len; i++)
   sum[i] = square[i] + sum[i-1];
 }
 
 @Override
 /**
  * the next method to get UnitGamma RV
  */ 
 public Double getNext() {
   
   /**
    * make the loop always ture, so the process will restart totally if the curve is smaller than the value of the function of x
    */ 
   while(true)
   { 
     // to generate a uniform random value between zero and the sum of square
     double ran_num = genUniform(0,sum[len-1]);
     // we need a variable to calculate the sum of square when in the loop 
     double begin = 0.0;
     
     for (int i=0; i <len; i++)
     {
       /**
        * Notice: in this situation, we cannot directly use "sum[i]<ran_num&&sum[i+1]>=ran_rum", because this will omit two situation:
        * 0 < ran_num < sum[0] and sum[i_max] < ran_num, so in such occasion, we can only use zero and zero plus the least value
        */       
       if (begin<ran_num && ran_num<=begin+square[i])
       {
         /**
          * if find proper sequence, generate uniform of x and uniform of y
          * if the curve of y is larger than the RV of y, then return x, otherwise, break the loop and totally restart the while
          */ 
         double x = genUniform(each_x[i],each_x[i+1]);
         double y = genUniform(0,each_y[i]);
         if (y<=function(x))
           return x;
         else
           break;
       }
       // if it cannot find proper i, than increase the square and continue finding next proper i
       begin = begin+square[i];
     }
  }
 }
}