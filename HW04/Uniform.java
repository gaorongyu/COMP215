/**
 *  the most basic class which can prodece uniform RV between two numbers.
 */ 

public class Uniform extends ARandomGenerationAlgorithm <Double> {

 private double low; // the low number of uniform distribution
 private double high; // the high number of uniform distribution
 
 /**
  *  the construct method which will use mySeed and passed parameter to initialize a random number sequence
  */
 public Uniform (long mySeed, UniformParam myParam){
  super(mySeed);
  low = myParam.getLow();
  high = myParam.getHigh(); 
 }
 
 /**
  *  the construct method which will use useMe and passed parameter to initialize a random number sequence
  */
 public Uniform (IPRNG useMe, UniformParam myParam){
  super(useMe);
  low = myParam.getLow();
  high = myParam.getHigh(); 
 }

 @Override
 /**
  * this method just get the low number and high number to produce uniform distribution between two number from superclass 
  */
 public Double getNext() {  
  // TODO Auto-generated method stub
  return genUniform(low,high);  
 }
}
