/**
 * This class is used to count occurences of words (strings) in a
 * corpus.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;



  public class WordCount {
 
 /**
  *  Declare a hashmap to store date. Each entry has two elements, one is string, the other is integer.
  *  In this hashmap, the key is string, the value is integer. The methods about how to establish this hashmap is below.   
  */
 private HashMap<String, Integer> table;
 /**
  *  Declare an ArrayList, put an ojbect WordWrapper into each array in the arraylist.
  *  Each WordWrapper contains two fields, one is string, another is integer, actually each WordWrapper is the entry in the Hashmap I want to put in Arraylist 
  */
 private ArrayList<WordWrapper> list;
  /**
   * In the consturctor, each wordcounter will initialize a hashmap table when it is called.
   */ 
 
 public HashMap<String, Integer> getTable()
 {
	 return table;
 }
 
 
 public WordCount()
 {
  table = new HashMap<String,Integer>();
 }
  /**
   * The function of the method "insert" is to establish the hashmap
   * Everytime when getting a string, hashmap will examine it. 
   * If the string is not included in the map, it will be added into the map, the value of this string will be set to one
   * If the string has existed in the map, the valuse will be get and adds one, and the string and the value will be put into the Hashmap again.
   * This means the string appeares twice in those words which have been inserted.
   */ 
 public void insert(String addMe)
 {
   if(table.containsKey(addMe))
   {
      table.put(addMe, table.get(addMe)+1);   
   }
   else
   {
     table.put(addMe, 1);
   }
 }
 
 
 public String getKthMostFrequent (int k) {
   
  /**
   * if the sequence of the word to test is larger than the size of the table, the programme will return a null value
   */ 
   
   if ( k >= table.size() )
     return null;
  
  /**
   * otherwise the programme will establish a list to store the entry in the hashmap to sort. 
   * The length of the hashmap is dependent on the the size of the table
   */ 
   list = new ArrayList<WordWrapper>(table.size());
  
  /**
   * The expression below can be devided two parts
   * The right part is the first step which is to set the hashmap, otherwise it cannot be used for iterator
   * The left part is the second step which is to create an iterator of the map to search each entry of the hashmap.
   */         
   Iterator<Map.Entry<String, Integer>> mark = table.entrySet().iterator();
  
  /**
   * The method"hasNext()" can search the entry of the hashmap, if the entry has element, the loop will continue running
   * In the loop, pairs will be created and it will be equal to the element which is detected by the iterator
   * Then, arraylist will continue adding the key and value of each entry into its list.
   */
   while (mark.hasNext()) {
     Map.Entry<String, Integer> pairs = mark.next();
     list.add(new WordWrapper(pairs.getKey(),pairs.getValue()));
   }
  
  /**
   * This is a metond belongs to java library, the function of the method is to arrange the element in my arraylist according to the rules of 
   * the function"comparable" which I will override below
   */   
   Collections.sort(list);
   
   /**
    * The K means the Kth most frequent, and it equals to the sequence of the arraylist, because the list has been arranged by the frequency.
    * Finally, the whole methond will return the string according to the parameter K, 
    */
   return list.get(k).getString();
}

/**
 * Class WordWrapper comes frome the interface named comparable
 * In the WordWrapper, we need two fields and two methods. 
 * The first field is string, the value of the string is dependent on the word which is imported from the txt
 * The count is the number about the apperance of the word
 */  
  
class WordWrapper implements Comparable<WordWrapper>
{
 
 private String word;
 private int count;

 public WordWrapper(String word, int count)
 {
  this.word=word;
  this.count=count;
 }
 
/**
 * The count and string are the private field, but we need to call them in the method wordcount.
 * So the below two methods can realize the function.
 */   
 
 public int getCount()
 {
   return count;
 }
 
 public String getString()
 {
   return word;
 }

/*
 * override the method compare, the method has two functions to compare the object in the arraylist
 * the first is compare the number of the appearance of both words and return the substract of the number of the appearance
 * the value of return can decide the sequence of both words
 * the second function is to call the word.compreto method to compare the order of the initials in those words which have the same number of appearnce
 */
 
 @Override
public int compareTo(WordWrapper anotherWord) 
{
   if(count == anotherWord.count)
     return word.compareTo(anotherWord.word);
   return anotherWord.count - count;
}
 
}
}

 