import java.lang.Math;
import java.io.PrintStream;

class PrimeFactorizer {
  
  private int [] allPrimes;
  private int upperBound;
  private int numPrimes;
  private PrintStream resultStream;
  
  private long maxNumber;

  public PrimeFactorizer (int maxNumberToFactorize, PrintStream outputStream) {
    
      resultStream = outputStream;
      maxNumber = (long)maxNumberToFactorize;
  }

 
  public void printPrimeFactorization (int numToFactorize) {
  
           
     if (numToFactorize < 1)
    {
      resultStream.print("Can't factorize a number less than one" ); 
      return;
    }
    
    int numPrimes = numToFactorize -1;
    int maxArray = numToFactorize - 2;
    int seqOfArray;
    int firstPrime = 2;
    
    upperBound = (int) Math.ceil (Math.sqrt (numPrimes));
    
    allPrimes = new int[numPrimes];

    for (seqOfArray = 0; seqOfArray <= maxArray; seqOfArray++)
    {
      allPrimes[seqOfArray] = firstPrime;
      firstPrime++;
     // resultStream.print(allPrimes[seqOfArray]+"  ");
    }
    
        if (numToFactorize == 1)
    {
          resultStream.print("Prime factorization of 1 is: 1");
          return;
    }

        
        if (numToFactorize > maxNumber)
    {
      resultStream.print(numToFactorize +" is too large to factorize" ); 
      return;
    }          

   for (int cursor=2; cursor<=upperBound; cursor++)
   {
        for(int firstFactor=2; cursor*firstFactor<=numToFactorize;firstFactor++)
        {
        seqOfArray = 0;
        for(;seqOfArray<=maxArray;seqOfArray++)
        {
          if (allPrimes[seqOfArray] == (cursor*firstFactor))
            allPrimes[seqOfArray] = 0;
          
          for(int tempSeqOfArray=0;tempSeqOfArray<maxArray;tempSeqOfArray++)
         {
            if (allPrimes[tempSeqOfArray] == 0)
             allPrimes[tempSeqOfArray]=allPrimes[tempSeqOfArray+1];
            if (allPrimes[tempSeqOfArray+1]==allPrimes[tempSeqOfArray])
            {
              allPrimes[tempSeqOfArray+1]=0;
              if (maxArray==tempSeqOfArray+1)
                maxArray--;
            }
          }
         // resultStream.print(allPrimes[seqOfArray]+"\t");
          }
        }
  }
  
            if (numToFactorize == allPrimes[maxArray])
    {
      resultStream.print("Prime factorization of "+ numToFactorize +" is: "+ numToFactorize); 
      return;
    }
            
                 if (numToFactorize != allPrimes[maxArray])
    {
            resultStream.print ("Prime factorization of " + numToFactorize + " is: ");
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
                resultStream.print(allPrimes[seqOfArray]);
                      if (numToFactorize != 1)
                  resultStream.print(" x ");
              }
            }
            }
                 }
                             
//    {
//     resultStream.print(allPrimes[seqOfArray]+"  ");
//    } 
  
  }
}  

