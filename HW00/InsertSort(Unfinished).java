import java.lang.Math;
import java.io.PrintStream;

public class test
{
  public static void main (String []args)
  {
    int [] allPrimes;
    int upperBound;
    int numPrimes;
    int maxNumber;
    int numToFactorize;
    
        numToFactorize = 30;
            upperBound = (int) Math.ceil (Math.sqrt (numToFactorize));
        
    //if (numToFactorize >= 10000)
      //numToFactorize = upperBound;


    maxNumber = numToFactorize;
    numPrimes = maxNumber -1;
    int maxArray = maxNumber -2;
    int seqOfArray;
    int firstPrime = 2;
    

    
    allPrimes = new int[numPrimes];

    for (seqOfArray = 0;seqOfArray<=maxArray;seqOfArray++)
    {
      allPrimes[seqOfArray] = firstPrime;
      firstPrime++;
     // System.out.print(allPrimes[seqOfArray]+"  ");
    }
    


        int replaceOfArray=0;    
   for (int cursor=2; cursor<=upperBound; cursor++)
   {

                System.out.print("\n");
        int tempMaxArray = maxArray;
        for(int firstFactor=2; cursor*firstFactor<=maxNumber;firstFactor++)
        {

        seqOfArray = 0;
        

        for(;seqOfArray<=maxArray;seqOfArray++)
        {
          if (allPrimes[seqOfArray]==(cursor*firstFactor))  
          {
            if ((replaceOfArray==0))
              replaceOfArray = seqOfArray;
            if (seqOfArray!=maxArray)
            {
              allPrimes[replaceOfArray] = allPrimes[seqOfArray+1];
              replaceOfArray++;
            }
            if (seqOfArray == maxArray)
            tempMaxArray--;
            System.out.print(allPrimes[seqOfArray]+" ");
            //System.out.print(" " + allPrimes[replaceOfArray]+ " " );
            //System.out.print(" " + allPrimes[maxArray]);
            //System.out.print(tempMaxArray);
          }
        }

        }
        maxArray=tempMaxArray;
   }
             System.out.print("\n");    
          
    for (seqOfArray = 0;seqOfArray<maxArray;seqOfArray++)
    {
      System.out.print(allPrimes[seqOfArray]+"  ");
    } 
    
                if (numToFactorize != allPrimes[maxArray])
    {
            System.out.print ("\nPrime factorization of " + numToFactorize + " is: ");
              seqOfArray = 0;
            for (; seqOfArray < maxArray ; seqOfArray ++ )
            {
              int mod = 0;
            while (mod == 0)
            {
              mod = numToFactorize % allPrimes[seqOfArray];              
              if (mod != 0)
                break;
              numToFactorize = numToFactorize / allPrimes[seqOfArray];
              int numPrint = 0;
              numPrint ++;
              for (int n=0 ; n < numPrint ; n++)
              {
                System.out.print(allPrimes[seqOfArray]);
                      if (numToFactorize != 1)
                  System.out.print(" * ");
              }
               
    
            }
            }
            }
          System.out.print("\n");  

    
}
}