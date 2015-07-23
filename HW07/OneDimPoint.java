
class OneDimPoint implements IPointInMetricSpace <OneDimPoint> {
 
  // this is the actual double
  Double value;
  
  // construct this out of a double value
  public OneDimPoint (double makeFromMe) {
    value = new Double (makeFromMe);
  }
  
  // get the distance to another point
  public double getDistance (OneDimPoint toMe) {
    if (value > toMe.value)
      return value - toMe.value;
    else
      return toMe.value - value;
  }
  
}