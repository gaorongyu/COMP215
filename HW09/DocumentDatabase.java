import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class DocumentDatabase {
	
	private String homeDirectory;
	private HashMap<String,WordCount> docCounter;
	private WordCounter allCounter;
	private WordCount backCounter;
	private Scanner scan;
	private ArrayList<String> wordList;
	private ILDALearner learner;
	private IPRNG myPRNG;
	private double normSum;
	private SparseDoubleVector numList;

	
	private int wordNum;
	private int topicNum;
	private double alpha;
	private double beta;
	
	//private ISparseDoubleVector //wordProb;
	
	public DocumentDatabase()
	{
		docCounter = new HashMap<String,WordCount>();
		allCounter = new WordCounter();
		backCounter = new WordCount();
		scan = new Scanner(System.in);
		new HashMap<String,IDoubleVector>();
		wordList = new ArrayList<String>();
		normSum = 0;
	}
	
	public void run() {
		
		try {
			firstQuestion();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void firstQuestion() throws IOException
	{
		System.out.println("Do you want to:");
		System.out.println("        (1) Index a new corpus");
		System.out.print("        (2) Use/manage an already-indexed one:");	
		
		int num = inputInt();
		switch(num)
		{
		case 1: indexNewCorpus();break;
		case 2: break;
		default:firstQuestion();
		}
	}

	private void indexNewCorpus() throws IOException {
		System.out.print("Enter the directory where the corpus is located:  ");
		
		JFileChooser fc = new JFileChooser ();
		fc.setDialogTitle ("Where is the directory where the corpus is located? ");
		fc.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
		fc.showOpenDialog(null);
		File result = fc.getSelectedFile ();
		homeDirectory = result.getCanonicalPath ();	
		File[] listFile=result.listFiles();
		
		System.out.println(homeDirectory);
		System.out.println("Counting the number of occurs of each word in the corpus...");	
				
		for (int i = 0; i < listFile.length; i++)
		{
			WordCount wordCount = new WordCount();
			CounterHelper counterHelper = new CounterHelper(wordCount,allCounter,backCounter);
			counterHelper.read(listFile[i].getPath());
			docCounter.put(listFile[i].getName(), wordCount);	
		}
		
		System.out.println("Found " + allCounter.getNumWords() + " unique words in the corpus.");
		
		inputParameters();
	}


	private void inputParameters() {
		
		System.out.print("How many of those words do you want to use to index the docs?");
		wordNum = inputInt();
		numList = new SparseDoubleVector(wordNum,0.0);
		for(int i = 0; i < wordNum; i++)
		{
			String s = allCounter.getKthMostFrequent(i);
			wordList.add(i, s);	
			try {
				numList.setItem(i, backCounter.getTable().get(s));
			} catch (OutOfBoundsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Done creating the dictionary.");
		 
		System.out.print("How many topics do you want to use to index the docs?");
		topicNum = inputInt();
		System.out.print("What value should I use in the alpha vector?"); 
		alpha = inputDouble();
		System.out.print("What value should I use in the beta vector?"); 		 
		beta = inputDouble();
		System.out.println("Loading all of the docs into the LDA learner..."); 
		loadDocs();
		IDoubleMatrix a = learner.getWordProbs();
		System.out.println("Done loading all of the docs."); 
		System.out.println("And now I've initialized the word_probs matrix."); 
		System.out.println(alpha);

		chooseUpdateOrQuery();
	}

	private void loadDocs() 
	{
		// initialize the learner and import the initial word probability
		 myPRNG = new PRNG (66);
		 IDoubleVector alphaVector = new SparseDoubleVector (wordNum, alpha);
		 IDoubleVector betaVector = new SparseDoubleVector (topicNum, beta);
		 learner = new SCLDALearner (alphaVector, betaVector, myPRNG);

		 IDoubleMatrix initialWordProbs = new RowMajorDoubleMatrix (wordNum, topicNum,0.0);
		 try {
			 // and load it up with data... there is no overalap among topics here
			 for (int i = 0; i < topicNum; i++) {
				 IDoubleVector myWordProbs = new SparseDoubleVector (wordNum, 0.02);
				 int mult = wordNum / topicNum;
				 for (int j = i * mult; j < (i + 1) * mult; j++) 
				 {
					 myWordProbs.setItem (j, 1.0); 
				 }
				 myWordProbs.normalize ();

				 Dirichlet myDirichlet = new Dirichlet (myPRNG, new DirichletParam (new SparseDoubleVector (wordNum, 1), 10e-99, 1000));
				 IDoubleVector initialProbs = myDirichlet.getNext ();
				 myWordProbs.addMyselfToHim (initialProbs);
				 initialProbs.normalize ();
				 initialWordProbs.setRow (i, initialProbs);
			 }
			      // load up the initial word_probs matrix
			     learner.loadUpWordProbs (initialWordProbs);
	  } catch (Exception e) {
	      e.printStackTrace ();
	    }
		// choose an initial topic_probs vector
		Dirichlet myDirichlet = new Dirichlet (myPRNG, new DirichletParam (new SparseDoubleVector (topicNum, 1), 10e-99, 1000));
		IDoubleVector initialTopicProbs = myDirichlet.getNext ();
		java.util.Iterator<String> it = docCounter.keySet().iterator();
		while(it.hasNext())
		{
			String str = it.next();
			IDoubleVector singleWordInDoc = new SparseDoubleVector(wordNum,0.0);
			for (int i = 0; i < wordList.size(); i ++)
			{
				HashMap<String,Integer> temp = docCounter.get(str).getTable();
				String word = wordList.get(i);
				try {
					if (temp.containsKey(word))
						singleWordInDoc.setItem(i, temp.get(word));
					else
						singleWordInDoc.setItem(i, 0);	
				} catch (OutOfBoundsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			normSum = normSum + singleWordInDoc.l1Norm();
			learner.loadUpAnotherDoc(str, singleWordInDoc, initialTopicProbs);
		}
		// TODO Auto-generated method stub
		
	}

	private void chooseUpdateOrQuery() {
		System.out.println("Do you want to:"); 
		System.out.println("        (1) Update/view/save the learned model"); 
		System.out.println("        (2) Use the learned model to search the corpus");
		System.out.print("        (3) Exit:");
		int num = inputInt();
		switch(num){
		case 1 : fourChoices();break; 
		case 2 : pirntWords();break;
		case 3 : return;
		}
		
		// TODO Auto-generated method stub
		
	}

	private void fourChoices() {
		System.out.println("Do you want to:"); 
		System.out.println("        (1) Run some iterations of the learner"); 
		System.out.println("        (2) Print out the current topics");
		System.out.println("        (3) Write the current model to a file");
		System.out.println("        (4) Stop updating/viewing/saving the model: ");
		int num = inputInt();
		switch(num){
		case 1 : iteration(); break; 
		case 2 : pirntWords(); break;
		case 3 : writeFiles(); break;
		case 4 : chooseUpdateOrQuery(); break;
		default : System.out.println("Please type a 1, 2, 3, or 4");
		}
		// TODO Auto-generated method stub
		
	}


	private void iteration() {
		System.out.println("      How many iters do you want to run? "); 
		
		int num = inputInt();
		for (int i = 0; i < num; i++)
		{
			System.out.println("Running iter " + i + " ... "); 
			System.out.print("        Updating produced..."); 
			learner.updateProduced();
			System.out.print("        Updating topic_probs..."); 
			learner.updateTopicProbs();
			System.out.println("        Updating word_probs..."); 
			learner.updateWordProbs();
		}
		fourChoices();		
	}
	
	private void pirntWords() {
		System.out.print("How many of the top words do you want for each topic?"); 
		int num = inputInt();
		IDoubleVector pc_vector = new SparseDoubleVector(wordNum,0.0);


		for (int i = 0; i < topicNum; i ++)
		{
			double kl = 0.0;
			try {
				//			Get the wordProbs matrix from the LDALearner
				IDoubleMatrix pt_word_probs = learner.getWordProbs();
				IDoubleVector pt_vector = pt_word_probs.getRow(i);
				WrapperWord list[] = new WrapperWord[wordNum];
				for (int j = 0; j < wordNum; j++)
				{
					//			calculate a vector holding pc for each word
					pc_vector.setItem(j, numList.getItem(j)/normSum);
					double pt = pt_vector.getItem(j);
					double pc = pc_vector.getItem(j);
					if (pc == 0.0)
						kl = 0.0;
					else 
						kl =  (pt * Math.log(pt / pc) + ((1 - pt) * Math.log((1-pt)/(1-pc))));
					list[j] = (new WrapperWord(wordList.get(j),kl));
				}

				Arrays.sort(list);

				System.out.print("topic " + i + " :");
				for (int k = 0; k < num; k++ )
					System.out.print("  "+ list[k].word);
				System.out.println("");
			} catch (OutOfBoundsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fourChoices();
	}

	
	private void writeFiles() {
		
		System.out.println(" What directory should I write to? ");
		
		System.out.println("What is the name of the XML file where the topic model should be written?");
		
		System.out.println("And where should the binary file with the other meta-data be written?");
		
		fourChoices();
		
	}

	private int inputInt()
	{

		String s = scan.next();
		int num = Integer.parseInt(s);
		return num;	
	}
	
	private double inputDouble()
	{

		String s = scan.next();
		double num = Double.parseDouble(s);
		return num;	
	}
	
	private class WrapperWord implements Comparable<WrapperWord> {
		private String word;
		private double key;

		WrapperWord(String word, double key)
		{
			this.word = word;
			this.key = key;
		}
		
		public int compareTo(WrapperWord arg0) {
			return (int)(arg0.key - this.key);
		}
	}
	
}




