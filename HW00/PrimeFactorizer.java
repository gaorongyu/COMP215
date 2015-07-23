import java.lang.Math;
import java.io.PrintStream;

class PrimeFactorizer {
  
  
  
  private int [] allPrimes;
  private int upperBound;
  private int numPrimes;
  private PrintStream resultStream;
  
  /*
   *  define a long parameter to avoid the maxnumber is too large
   */
  
  private long maxNumber; 
  
  

  public PrimeFactorizer (int maxNumberToFactorize, PrintStream outputStream) 
  {
    
      resultStream = outputStream;  
      maxNumber = ( long ) maxNumberToFactorize;
  }

 
  public void printPrimeFactorization (int numToFactorize) {
  
    
    /**
     *   Since numToFactorize has many cases, so I must write different codes
     *   to deal with different cases. When numToFactorize less than one, equal one, or
     *   larger than maxNumber, the entire class can directly output the conclusion and 
     *   do not need to continue run the class. So, I put the codes of these three situation
     *   on the beginning of the entire class.
     */
       if ( numToFactorize < 1 )
    {
      resultStream.print("Can't factorize a number less than one" ); 
      return;
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
        
    /*
     * upperBound is an very important argument, it equals the square root of the numToFactorize.
     * Because fractizing a prime is only to need caulate to its square root, so almost all the other argument are related to it.
     * I put it on the beginning of the arguments
     */
    upperBound = (int) Math.ceil (Math.sqrt (numToFactorize));
     
    /*
     * Becasue the prime array does not include the number one, so the numprimes = upperBound -1 
     */
    numPrimes = upperBound -1;
    
    /*
     * Becasue the prime array does not include the number one, and the sequence of array begins from zero, so the max sequence of the array = upperBound -1 
     */
    int maxArray = upperBound -2;
    

    int seqOfArray;
    int firstPrime = 2; 
    
    /*
     *  the array*allPrimes will accommodate all the primes which are less than the upperBound, the square root of the number to factorize
     *  and the below function is to generate a array which includes all the numbers that are less than upperBound
     */
    
    allPrimes = new int[numPrimes];
    for ( seqOfArray = 0 ; seqOfArray <= maxArray ; seqOfArray ++ )
    {
      allPrimes[seqOfArray] = firstPrime;
      firstPrime++;
    }
    
    /*
     * to finish the assignment, I need to do two things, the first thing is to calculate all the primes less than the number to factorize
     * the second thing is devide the primes to the number by sequence to fatorize the number
     * 
     * the function below is to generate all the primes and put them into the array*allPrimes
     * 
     * the first and the second inner loop is to set each number which is the power of the number two to zero 
     * then the third loop put all the numbers whicn are not zero in the array*allPrimes to the backupPrime, and modify the maxArray
     * when eliminate a zero in the array*allPrime, the maxArray will deduct one, it will increase the speed of calculation for the further steps
     * the forth step is reload the numbers in array*backupPrime to the array*allPrime, then at that time, all the numbers in the array*allPrime are less 
     * than upperBound and are not equal to zero, and do not include all the numbers which are the power of two, the maxArray are also dramatically shortened
     * the five step is increase the cursor and repeatedly implement the above four step to eliminate all the power of each cursor
     * so, finally, the number in the array*allPrime are primes which are less than upperBound
     */
    
      for (int cursor=2; cursor<=upperBound; cursor++)
      {
        int []backupPrimes = new int[numPrimes];  
        for(int firstFactor=2; cursor*firstFactor<=upperBound;firstFactor++)
       {
         seqOfArray = 0;
         for(;seqOfArray<=maxArray;seqOfArray++)
         {
          if (allPrimes[seqOfArray] == (cursor*firstFactor))
            allPrimes[seqOfArray] = 0;
         }
        }
        seqOfArray = 0;
        for(int tempSeqOfArray=0;tempSeqOfArray<=maxArray;tempSeqOfArray++)
        {
          if (allPrimes[tempSeqOfArray] != 0)
          {
            backupPrimes[seqOfArray]=allPrimes[tempSeqOfArray];
            seqOfArray++;
          }
        }
        
        maxArray = seqOfArray-1;
        
        seqOfArray = 0;
        for(int i=0; i<=maxArray; i++)          
        {
          allPrimes[seqOfArray] = backupPrimes[i];
          seqOfArray ++;
        }
      }
      
      /*
       *  under these texts are the second part of this programme, the purpose of the second part is to generate the correct output
       *  beside the three cases of the number to factorize at the beginning of the programme, there are still three cases left
       *  the first is the number can be devided completely by the primes in the array*allPrimes, is this case, repetealy devide all the primes 
       *  when the mod is not equal to zero, then change the next prime, until the number are factorized compeletely
       */
                 
            seqOfArray = 0;
            int backupNumToFactorize = numToFactorize;
            resultStream.print("Prime factorization of " + numToFactorize + " is: " );
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
                  if (numToFactorize != 1)       // I do not hope the first * will be displayed, so I judge the final numToFactorize
                    resultStream.print(" x ");
              }
            }
            }

               /*
                * the second case is the number itself is the prime, so all the primes in the array*allPrime cannot devide the number, so if mod is not equal to zero
                * and the sequence of array is equal to the max sequence of array, the number will be regared as a prime.
                * 
                * the third case is the number can be factorized primes, but one number is larger than upperBound, so the output should display the number multiple 
                * the larger number. Becasuse in this case, the small number can be calculated in the first case, and the large number can be calculated by the second case,
                * so I write the one code for both the second case and third case
                */
            
               if ((seqOfArray == maxArray)&&(numToFactorize!=1))
                   resultStream.print(numToFactorize); 

                                               
            }
                           
}  

