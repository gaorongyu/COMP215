
// this silly little class is just a wrapper for a key, data pair
class DataWrapper <Key, Data> {
  
  Key key;
  Data data;
  
  public DataWrapper (Key keyIn, Data dataIn) {
    key = keyIn;
    data = dataIn;
  }
  
  public Key getKey () {
    return key;
  }
  
  public Data getData () {
    return data;
  }
}