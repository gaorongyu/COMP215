
class MultiDimPoint implements IPointInMetricSpace <MultiDimPoint> {
 
  // this is the point in a multidimensional space
  IDoubleVector me;
  
  // make this out of an IDoubleVector
  public MultiDimPoint (IDoubleVector useMe) {
    me = useMe;
  }
  
  // get the Euclidean distance to another point
  public double getDistance (MultiDimPoint toMe) {
    double distance = 0.0;
    try {
      for (int i = 0; i < me.getLength (); i++) {
        double diff = me.getItem (i) - toMe.me.getItem (i);
        diff *= diff;
        distance += diff;
      }
    } catch (OutOfBoundsException e) {
        throw new IndexOutOfBoundsException ("Can't compare two MultiDimPoint objects with different dimensionality!"); 
    }
    return Math.sqrt(distance);
  }
  
}