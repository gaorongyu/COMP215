
// this interface corresponds to a point in some metric space
interface IPointInMetricSpace <PointInMetricSpace> {
  
  // get the distance to another point
  // 
  // for this to work in an M-Tree, distances should be "metric" and
  // obey the triangle inequality
  double getDistance (PointInMetricSpace toMe);
}