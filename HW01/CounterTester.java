import junit.framework.TestCase;
import java.io.*;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */
public class CounterTester extends TestCase {

  /**
   * File object to read words from, with buffering.
   */
  private FileReader     file = null;
  private BufferedReader reader = null;

  /**
   * Close file readers.
   */
  private void closeFiles() {
    try {
      if (file != null) {
        file.close();
        file = null;
      }
      if (reader != null) {
        reader.close();
        reader = null;
      }
    } catch (Exception e) {
      System.err.println("Problem closing file");
      System.err.println(e);
      e.printStackTrace();
    }
  }

  /**
   * Close files no matter what happens in the test.
   */
  protected void tearDown () {
    closeFiles();
  }

  /**
   * Open file and set up readers.
   *
   * @param fileName  name of file to open
   */
  private void openFile(String fileName) {      
    try {
      file = new FileReader(fileName);
      reader = new BufferedReader(file);
    } catch (Exception e) {
      System.err.format("Problem opening %s file\n", fileName);
      System.err.println(e);
      e.printStackTrace();
      fail();
    }
  }

  /**
   * Read the next numWords from the file.
   *
   * @param counter   word counter to update
   * @param numWords  number of words to read.
   */
  private void readWords(WordCounter counter, int numWords) {
    try {
      for (int i=0; i<numWords; i++) {
        String word = reader.readLine();
        if (word == null) {
          return;
        }
        counter.insert(word);
      } 
    } catch (Exception e) {
      System.err.println("Problem reading file");
      System.err.println(e);
      e.printStackTrace();
      fail();
    }
  }

  /**
   * Check that a sequence of words starts at the "start"th most
   * frequent word.
   *
   * @param counter   word counter to lookup
   * @param start     frequency index to start checking at
   * @param expected  array of expected words that start at that frequency
   */
  private void checkExpected(WordCounter counter, int start, String [] expected) {
    for (int i = 0; i<expected.length; i++) {
      String actual = counter.getKthMostFrequent(start);
      System.out.format("k: %d, expected: %s, actual: %s\n",
                        start, expected[i], actual);
      assertEquals(expected[i], actual);
      start++;
    }
  }

  /**
   * A test method.
   * (Replace "X" with a name describing the test.  You may write as
   * many "testSomething" methods in this class as you wish, and each
   * one will be called when running JUnit over this class.)
   */
  public void testSimple() {
    System.out.println("\nChecking insert");
    WordCounter counter = new WordCounter();
    counter.insert("pizzaz");
    counter.insert("pizza");
    counter.insert("pizza");
    String [] expected = {"pizza"};
    checkExpected(counter, 0, expected);
  }

  public void testTie() {
    System.out.println("\nChecking tie for 2nd place");
    WordCounter counter = new WordCounter();
    counter.insert("panache");
    counter.insert("pizzaz");
    counter.insert("pizza");
    counter.insert("zebra");
    counter.insert("pizza");
    counter.insert("lion");
    counter.insert("pizzaz");
    counter.insert("panache");
    counter.insert("panache");
    counter.insert("camel");
    // order is important here
    String [] expected = {"pizza", "pizzaz"};
    checkExpected(counter, 1, expected);
  }

 
  public void testPastTheEnd() {
    System.out.println("\nChecking past the end");
    WordCounter counter = new WordCounter();
    counter.insert("hi");
    counter.insert("hello");
    counter.insert("greetings");
    counter.insert("salutations");
    counter.insert("hi");
    counter.insert("welcome");
    counter.insert("goodbye");
    counter.insert("later");
    counter.insert("hello");
    counter.insert("when");
    counter.insert("hi");
    assertNull(counter.getKthMostFrequent(8));
  }

  public void test100Top5() {
    System.out.println("\nChecking top 5 of 100 words");
    WordCounter counter = new WordCounter();
    openFile("allWordsBig");
    readWords(counter, 100);
    String [] expected = {"edu", "comp", "cs", "windows", "cmu"};
    checkExpected(counter, 0, expected);
  }

  public void test300Top5() {
    System.out.println("\nChecking top 5 of first 100 words");
    WordCounter counter = new WordCounter();
    openFile("allWordsBig");
    readWords(counter, 100);
    String [] expected1 = {"edu", "comp", "cs", "windows", "cmu"};
    checkExpected(counter, 0, expected1);

    System.out.println("Adding 100 more words and rechecking top 5");
    readWords(counter, 100);
    String [] expected2 = {"edu", "cmu", "comp", "cs", "state"};
    checkExpected(counter, 0, expected2);

    System.out.println("Adding 100 more words and rechecking top 5");
    readWords(counter, 100);
    String [] expected3 = {"edu", "cmu", "comp", "ohio", "state"};
    checkExpected(counter, 0, expected3);
  }

  public void test300Words14Thru19() {
    System.out.println("\nChecking rank 14 through 19 of 300 words");
    WordCounter counter = new WordCounter();
    openFile("allWordsBig");
    readWords(counter, 300);
    String [] expected = {"cantaloupe", "from", "ksu", "okstate", "on", "srv"};
    checkExpected(counter, 14, expected);
  }

  public void test300CorrectNumber() {
    System.out.println("\nChecking correct number of unique words in 300 words");
    WordCounter counter = new WordCounter();
    openFile("allWordsBig");
    readWords(counter, 300);
    assertNotNull(counter.getKthMostFrequent(122));
    assertNotNull(counter.getKthMostFrequent(123));
    assertNotNull(counter.getKthMostFrequent(124));
    assertNull(counter.getKthMostFrequent(125));
  }

  public void test300and100() {
    System.out.println("\nChecking top 5 of 100 and 300 words with two counters");
    WordCounter counter1 = new WordCounter();
    openFile("allWordsBig");
    readWords(counter1, 300);
    closeFiles();

    WordCounter counter2 = new WordCounter();
    openFile("allWordsBig");
    readWords(counter2, 100);

    String [] expected1 = {"edu", "cmu", "comp", "ohio", "state"};
    checkExpected(counter1, 0, expected1);

    String [] expected2 = {"edu", "comp", "cs", "windows", "cmu"};
    checkExpected(counter2, 0, expected2);
  }

  public void testAllTop15() {
    System.out.println("\nChecking top 15 of all words");
    WordCounter counter = new WordCounter();
    openFile("allWordsBig");
    readWords(counter, 6000000);
    String [] expected = {"the", "edu", "to", "of", "and", 
                          "in", "is", "ax", "that", "it", 
                          "cmu", "for", "com", "you", "cs"};
    checkExpected(counter, 0, expected);
  }

  public void testAllTop10000() {
    System.out.println("\nChecking time to get top 10000 of all words");
    WordCounter counter = new WordCounter();
    openFile("allWordsBig");
    readWords(counter, 6000000);

    for (int i=0; i<10000; i++) {
      counter.getKthMostFrequent(i);
    }
  }
}
