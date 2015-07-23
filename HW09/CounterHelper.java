

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CounterHelper {

	  private FileReader file = null;
	  private BufferedReader reader = null;
	  private WordCount wordCounter;
	  private WordCounter allCounter;
	  private WordCount backCounter;
	  
	  public CounterHelper(WordCount wordCounter, WordCounter allCounter, WordCount backCounter)
	  {
		  file = null;
		  reader = null;
		  this.wordCounter = wordCounter;
		  this.allCounter = allCounter;
		  this.backCounter = backCounter;
	  }
	  
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
	  
	  protected void tearDown () {
		    closeFiles();
		  }
	  
	  private void openFile(String fileName) {      
		    try {
		      file = new FileReader(fileName);
		      reader = new BufferedReader(file);
		    } catch (Exception e) {
		      System.err.format("Problem opening %s file\n", fileName);
		      System.err.println(e);
		      e.printStackTrace();
		    }
		  }
	  
	  public void read(String Filename)
	  {
		  openFile(Filename);
		  String s;
		  Pattern pattern = Pattern.compile("\\b[a-z]+\\b",Pattern.CASE_INSENSITIVE);
		  
		  try {
			while((s = reader.readLine()) != null)
			  {
					Matcher matcher = pattern.matcher(s);
					while (matcher.find())
					{
						s = matcher.group();
						s = s.toLowerCase();
						wordCounter.insert(s);
						allCounter.insert(s);
						backCounter.insert(s);
					}

			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			  
	  }
	
	}
