import java.util.Arrays;

public class Test
{
  public static void main(String[] arg)
  {
    
    LinearSparseArray<Double> myArray = new LinearSparseArray<Double>(2000);

    
    // add a bunch of stuff into it
    System.out.println ("Doing the inserts...");
    for (int i = 0; i < 1000000; i++) {
      if (i % 100000 == 0)
        System.out.format ("%d%% done ", i * 10 / 100000);
      
      // put one item at an even position
      myArray.put (i * 10, i * 1.0);
      
      // and put another one at an odd position
      myArray.put (i * 10 + 3, i * 1.1);
    }
    
    // now, iterate through the list
    System.out.println ("\nDoing the lookups...");
    double total = 0.0;
    
    // note that we are only going to look at the data in the even positions
    for (int i = 0; i < 2000000; i += 2) {
      if (i % 2000000 == 0)
        System.out.format ("%d%% done ", i * 10 / 2000000);
      Double temp = myArray.get (i);
      
      // if we got back a null, there was no data there
      if (temp != null)
        total += temp;
    }
    
    //System.out.println (myArray.indices[3]);
    //System.out.println (myArray.get(12));
    System.out.println (total);
    

  }
}
