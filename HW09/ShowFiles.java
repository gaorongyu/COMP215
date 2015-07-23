
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.concurrent.Semaphore;
import java.awt.event.*;
import java.io.*;
import java.util.*;


// this class takes a list of file names as input and then displays them, one-at-a-time,
// in a series of windows
class ShowFiles implements Runnable {

  // this is the string to display
  private String stringToCheck;
  
  // the current file to display
  private String myFName;
  
  // the window we are displaying in
  private JFrame frame;
  
  // used to wait until the user makes a selection 
  private Semaphore iAmDone = new Semaphore (0);
  
  // the list of files to display
  private ArrayList <String> myFileList;
  
  // this constructor takes as an arg the list of file names that are gonna be displayed
  ShowFiles (ArrayList <String> myFiles) {

    // if it is an empty file list, do nothing
    if (myFiles.size () == 0)
      return;
    
    // remember the list of files
    myFileList = myFiles;
    
    // this is the current file to display
    myFName = myFiles.remove (0);
    
    try {
      
      // open up the file to display
      FileReader myFile = new FileReader(myFName);
      BufferedReader myReader = new BufferedReader(myFile);
      String myString;
      
      // and build up the string to display
      stringToCheck = "";
      while ((myString = myReader.readLine()) != null) {
        stringToCheck += (myString + "\n");
      }
      
      // close the reader when done
      myReader.close ();
    } catch (Exception e) {
      throw new RuntimeException ("Problem opening/reading file"); 
    }
    
    // start up the window and wait until it is done
    (new Thread(this)).start();
    waitUntilDone ();
    
    // and recursively display the rest of the files
    ShowFiles temp = new ShowFiles (myFiles);
    myFiles.add (0, myFName);
  }
  
  // block until the user clicks the done button
  private void waitUntilDone () {
    try {
      iAmDone.acquire (1); 
    } catch (Exception e) {
      throw new RuntimeException ("bad semaphore"); 
    }
  }
  
  // used to actually bring up the window
  public void run () {
    
    // this creates the window
    Runnable runner = new Runnable() {
      
      public void run() {
        
        // create the main frame
        frame = new JFrame("Search result: " + myFName);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // create the area to display the text
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap (true);
        textArea.insert (stringToCheck, 0);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        
        // create the button and the area where it is displated
        JPanel dotPanel = new JPanel(new BorderLayout());
        JButton myButton;
        if (myFileList.size () != 0)
          myButton = new JButton ("Onto next file");
        else
          myButton = new JButton ("Done");
        dotPanel.add(myButton, BorderLayout.CENTER);
        frame.add(dotPanel, BorderLayout.SOUTH);
        
        // this listens for a user to click the "done" button
        ActionListener buttonListener = new ActionListener () {
          public void actionPerformed (ActionEvent e) {
            iAmDone.release (1);
            frame.dispose ();
          }
        };
          
        // add the button and make the frame visible
        myButton.addActionListener (buttonListener);
        frame.setSize(500, 500);
        frame.setVisible(true);
      }
    };
    
    // run the window
    EventQueue.invokeLater(runner);
  }
  
}