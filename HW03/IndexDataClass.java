
/**
 * The class implemention the interface of IIndexedData<T>
 * The methods are very simple , the only function is to make concrect class of the interface.
 */ 


public class IndexDataClass<T> implements IIndexedData<T>{
 private int index;
 private T data;
 
 /**
 * The construct of the class to initialize index and data
 * 
 * @index  index  index
 * @data   vector data
 */ 
 
 public IndexDataClass(int index, T data) {
  this.index = index;
  this.data=  data;
 }
 
 /**
 * The method of index to get the index of container
 * 
 */ 
 
 public int getIndex() {
  return index;
 }
 
  /**
 * The method of index to get the index of data
 * 
 */ 

 public T getData() {
  return data;
 }

}
