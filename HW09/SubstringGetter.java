import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.concurrent.Semaphore;
import java.awt.event.*;
import java.io.*;
import java.util.*;

// this class displays the contents of a file in a window, and allows
// a user to use a mouse to select a subset of those contents
public class SubstringGetter implements Runnable {
  
  // these are the start and the end of the user-selected range
  private int start;
  private int end;
  
  // this is the string being displayed
  private String stringToCheck;
  
  // and the name of the file being displayed
  private String myFName;
   
  // the window to display
  private JFrame frame;
  
  // a semaphore that allows us to wait until the user is done
  private Semaphore iAmDone = new Semaphore (0);
  
  // give the selected substring back to the caller
  public String getSubstring () {
    return stringToCheck.substring (start, end);
  }
  // constructor... requires the name of the file to be displayed 
  public SubstringGetter (String myFileName) {

    // remeber the file name
    myFName = myFileName;
    try {
      
      // open up the file, and read all of the lines from the file, one-at-time
      // then use those lines to construct a string called "stringToCheck"
      FileReader myFile = new FileReader(myFileName);
      BufferedReader myReader = new BufferedReader(myFile);
      String myString;
      stringToCheck = "";
      while ((myString = myReader.readLine()) != null) {
        stringToCheck += (myString + "\n");
      }
      myReader.close ();
      
    // an exception here means there was a problem opening or reading the file
    } catch (Exception e) {
      throw new RuntimeException ("Problem opening/reading file"); 
    }
    
    // start up the window
    (new Thread(this)).start();
    
    // and then wait until the user has made a selection
    waitUntilDone ();
  }
  
  // this allows the object to wait until the user is done with the window
  private void waitUntilDone () {
    
    try {
      
      // just try to get the semaphore
      iAmDone.acquire (1); 
    } catch (Exception e) {
      throw new RuntimeException ("bad semaphore"); 
    }
  }
  
  // this is the routine that is called to bring up the window
  public void run () {
    
    // this is used to control the window
    Runnable runner = new Runnable() {
      
      public void run() {
        
        // declare the window
        frame = new JFrame("Choose the text that you want to use to search");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // create the area where the text is displayed
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap (true);
        textArea.insert (stringToCheck, 0);
        
        // this is the scroll area
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        
        // this is the panel where the buttons are displayed
        JPanel dotPanel = new JPanel(new BorderLayout());
        JButton myButtonSome = new JButton ("Select highlighted text ");
        JButton myButtonAll = new JButton ("Select all ");
        dotPanel.add(myButtonSome, BorderLayout.NORTH);
        dotPanel.add(myButtonAll, BorderLayout.SOUTH);
        frame.add(dotPanel, BorderLayout.SOUTH);
        
        // this listens to user selection of text
        CaretListener caratListener = new CaretListener () {
          public void caretUpdate(CaretEvent caretEvent) {
            end = caretEvent.getDot();
            start = caretEvent.getMark();
            if (start > end) {
              int temp = end;
              end = start;
              start = temp;
            }
          }
        };
        
        // listens to the "select highlighted" button
        ActionListener buttonListenerSome = new ActionListener () {
          public void actionPerformed (ActionEvent e) {
            iAmDone.release (1);
            frame.dispose ();
          }
        };
        
        // listens to the "select all" button
        ActionListener buttonListenerAll = new ActionListener () {
          public void actionPerformed (ActionEvent e) {
            end = stringToCheck.length ();
            start = 0;
            iAmDone.release (1);
            frame.dispose ();
          }
        };
          
        // add the listeners for user actions
        textArea.addCaretListener(caratListener);
        myButtonSome.addActionListener (buttonListenerSome);
        myButtonAll.addActionListener (buttonListenerAll);
        
        // set the window up
        frame.setSize(500, 500);
        frame.setVisible(true);
      }
    };
    
    // run the window!
    EventQueue.invokeLater(runner);
  }
  
}



