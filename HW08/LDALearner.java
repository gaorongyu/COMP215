import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class LDALearner implements ILDALearner {
  
  private IDoubleMatrix word_probs; // the probability of the word occurrence of each topic
  private HashMap<String,DocSignature> docList; // the hash map to store three elements of each document
  private IDoubleMatrix produce;  // the matrix to store the produce of one specific document
  private HashMap<String,IDoubleMatrix> produceMap; // the array list to store the produce of each document
 
 private IDoubleVector alpha; // the vector to record the alpha in the documentation
 private IDoubleVector beta; // the vector to record the beta in the documentation
 private IPRNG prng; // the random seed to generate all the distributions
 private SCXMLParser parser; // create a parser to parse the XML file
 
 
 /**
  * the construct method to initialize the class of LDALearner
  * @param alpha  the number of the length of word
  * @param beta   the number of the length of topic
  * @param prng   the random seed
  */
 public LDALearner(IDoubleVector alpha, IDoubleVector beta, IPRNG prng)
 {
  this.alpha = alpha; // initialize alpha
  this.beta = beta;  // initialize beta
  this.prng = prng;  // initialize prng
  

  this.word_probs = null; // initialize the matrix word_probs
  this.docList = null; // initialize the array list which stores the DocSignature
  this.produce = null; // initialize the matrix produce 
  this.produceMap = new HashMap<String, IDoubleMatrix>(); // initialize the produceMap to store produce according to the file of each doc
 }
 
 @Override
 public void loadFromFile(String loadFromMe) {

  /*
   *  once all the loadFromFile method, each private filed will be reset
   */
  this.produce = null;
  this.produceMap.clear();
  this.docList = new HashMap<String,DocSignature>();
  this.word_probs = new RowMajorDoubleMatrix(beta.getLength(),alpha.getLength(),0.0);
  this.parser = new SCXMLParser(loadFromMe);
  int i = 0;
  
  /*
   *  create a tempDoc and a tempWordProbs to store variant  
   */
  DocSignature tempDoc = null;  
  IDoubleVector tempWordProbs = null;


  /*
   *  if the next topic in xml is null, then add the word_probs of each topic into the word_probs 
   */
  while ( (tempWordProbs = parser.nextTopic()) != null)
  {
   try {
    word_probs.setRow(i,tempWordProbs);
   } catch (OutOfBoundsException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
   i++;
  }
  
  /*
   *  if the next doc in xml is null, then add the next doc into the doc list 
   */
  while ( (tempDoc = parser.nextDoc()) != null)
  {
   docList.put(tempDoc.getFName(),tempDoc);
  }

 }

 
 /**
  * the method to add a new doc into the document list
  * @param docName       new document name
  * @param wordsInDoc    the number of the occurrence of the words in this new document
  * @param topicProbs    the probability of topic of this new docuemnt
  */
 @Override
 public void loadUpAnotherDoc(String docName, IDoubleVector wordsInDoc,
   IDoubleVector topicProbs) {
   DocSignature tempDoc = new DocSignature(docName, topicProbs, wordsInDoc);
   if (this.docList == null)
    this.docList = new HashMap<String,DocSignature>();
   docList.put(docName,tempDoc);
 }

 /**
  * the method to assign an original word probability to a new matrix
  * @param loadMe   the new word probability
  */
 @Override
 public void loadUpWordProbs(IDoubleMatrix loadMe) {
  word_probs = loadMe;
 }
 
 /**
  * the method to update produce matrix of specific document
  * @param docName
  */

 @Override
 public void updateProduced(String docName) {
  // the loop to find the proper document
  
  if ((docList.containsKey(docName) == false)|| (word_probs == null)) throw new RuntimeException();
  
  // if the document does not have this file or the matrix of word probability does not exist, the code will throw an exception
  generateSingleProduce(docName);
 }

 /**
  * the method to update all the produce matrix of all the document
  */
@Override
 public void updateProduced() {
 // if the matirx of word probability does not exist, the code will throw an exception 
 if (word_probs == null) throw new RuntimeException();
 //  use a loop to generate all the produce of each document
 for (DocSignature i : this)
  generateSingleProduce(i.getFName());

 }

/**
 * the method to update the topic probability for one specific document
 * @param docName
 */
 @Override
 public void updateTopicProbs(String docName) {

  if ((docList.containsKey(docName) == false)|| (word_probs == null)) throw new RuntimeException();
  // call the method to update the single topic probability
  updateSingleTopicProbs(docName);
  
 }

@Override
 public void updateTopicProbs() {
 // if the matrix of word probability does not exist, the code will throw an exception  
 if (produceMap.size() == 0) throw new RuntimeException();
 //  use a loop to generate all the topic probability of each document
 for (DocSignature i : this)
  updateSingleTopicProbs(i.getFName());
 }

 @Override
 public void updateWordProbs() {
  // if the matrix of produce does not exist, the code will throw an exception 
  if (produceMap.size() == 0) throw new RuntimeException();
  // create a temporary matrix to update word probability
  IDoubleMatrix produce_temp = new RowMajorDoubleMatrix(beta.getLength(),alpha.getLength(),0.0);
  
  try
  {
 // use the loop to get the sum of each produce of each document list
  for (DocSignature x : this)
  {
   String word_file_name = x.getFName();
   if (produceMap.containsKey(word_file_name) == true)
   {
    IDoubleMatrix produce_matrix = produceMap.get(word_file_name);
    // use a simple nested loop to get sum of each produce of each document list
    for (int i = 0; i < beta.getLength(); i ++ )
     for (int j = 0; j < alpha.getLength(); j++ )
      produce_temp.setEntry(i, j, (produce_temp.getEntry(i, j)+produce_matrix.getEntry(i, j)));
   }
  }
  
  // use the loop to get the dirichet distribution of new produce 
  for (int k = 0; k < beta.getLength(); k++)
  {
   // this is algo which comes from the professor`s slides
   IDoubleVector word_temp = produce_temp.getRow(k);
   alpha.addMyselfToHim(word_temp); 
   DirichletParam para = new DirichletParam(word_temp, 0.00002, 1000);
   word_temp = (new Dirichlet(prng, para)).getNext();
   produce_temp.setRow(k, word_temp);   
  }
  word_probs = produce_temp;
  }
  catch (OutOfBoundsException e) {
   e.printStackTrace();
  }  
  
 }

 /**
  * return the document signature of the specific document
  * @param docName  the name of document
  * @return  the signature of this document
  */
 @Override
 public DocSignature getSignature(String docName) {
  if ((docList.containsKey(docName) == false)|| (word_probs == null)) throw new RuntimeException();
  // if the document does not have this file or the matrix of word probability does not exist, the code will throw an exception  
  return docList.get(docName);
 }

 /*
  * return the iterator of document list
  */
 @Override
 public Iterator<DocSignature> iterator() {
  return docList.values().iterator();
}

 /**
  * return the word probability
  * @return the double matrix class
  */
 @Override
 public IDoubleMatrix getWordProbs() {
  return word_probs;
 }

 /**
  * wtire all the updated data to a new file
  * @param writeToMe  the file name
  */
 @Override
 public void writeToFile(String writeToMe) {
  // create a new file
  File f = new File(writeToMe);
  if (!f.exists())
  {
  try {
   f.createNewFile();
  } catch (IOException e) {
   e.printStackTrace();
  }
  }
  // use the same style with professor to create a new file
  try {
   BufferedWriter output = new BufferedWriter(new FileWriter(f));

   output.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\n");
   output.write("<LDA_model>\n");
   
   // write word probability into the new file
   output.write("<word_probs>\n");
   output.write("<num_topics>" + beta.getLength() + "</num_topics>\n");
   for (int i = 0; i < beta.getLength(); i++) {
    output.write("<topic><topic_num>" + i + "</topic_num>\n");
    output.write("<dense_vector>\n");
    output.write("<length>" + alpha.getLength() + "</length>\n");
    for (int j = 0; j < alpha.getLength(); j++) {
     output.write("<entry><dim>" + j +"</dim><val>" + word_probs.getEntry(i, j) + "</val></entry>\n");
    }
    output.write("</dense_vector>\n");
    output.write("</topic>\n");
   }
   output.write("</word_probs>\n");
   
   // write all the data which are related to corpus into the corpus
   output.write("<corpus>\n");
   output.write("<num_docs>" + docList.size() + "</num_docs>\n");
   for (DocSignature i : this) {
    output.write("<doc_in_corpus>\n");
    output.write("<doc_name>" + i.getFName() + "</doc_name>");
    
    //write words probability into the new file
    output.write("<words_in_doc>");
    output.write("<sparse_int_vector>");
    
    output.write("<length>"+ i.getWordsInDoc().getLength() +"</length>");
    ArrayList<Integer> index = new ArrayList<Integer>();
    ArrayList<Integer> val = new ArrayList<Integer>();
    IDoubleVector wordsInDoc = i.getWordsInDoc();
    for (int j = 0; j < wordsInDoc.getLength(); j++) {
     if(!(wordsInDoc.getItem(j) == 0)) {
      index.add(j);
      val.add((int) wordsInDoc.getItem(j));
     }
    }
    output.write("<non_zero_cnt>" + index.size() + "</non_zero_cnt>");
    for (int k = 0; k < index.size(); k++) {
     output.write("<entry><dim>" + index.get(k) + "</dim><val>" + val.get(k) + "</val></entry>");
    }
    
    output.write("</sparse_int_vector>");
    output.write("</words_in_doc>");
    
    // write topic probability into the file
    output.write("<topic_probs>");
    output.write("<dense_vector>");
    output.write("<length>" + i.getTopicProbs().getLength() + "</length>");
    for (int h = 0; h < i.getTopicProbs().getLength(); h++) {
     output.write("<entry><dim>" + h + "</dim><val>" + i.getTopicProbs().getItem(h) + "</val></entry>");
    }
    output.write("</dense_vector>");
    output.write("</topic_probs>");
    
    output.write("</doc_in_corpus>\n");
   }
   output.write("</corpus>\n");
   output.write("</LDA_model>\n");
   // close the file
   output.close();
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (OutOfBoundsException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 }
 
 
 // the private method to generate a single produce
 private void generateSingleProduce(String docName) {
  
  try {
   DocSignature tempDoc = docList.get(docName);
   // create the matrix of produce
   produce = new RowMajorDoubleMatrix(word_probs.getNumRows(),word_probs.getNumColumns(),0.0);
   // use a nested loop to set each element into the produce matrix
   for (int m = 0; m < alpha.getLength(); m++)
   {
    IDoubleVector word_probs_column = new SparseDoubleVector(beta.getLength(),0);
    for (int n=0; n < beta.getLength(); n++)
    {
     double produce_temp = tempDoc.getTopicProbs().getItem(n)*word_probs.getEntry(n,m);
     word_probs_column.setItem(n, produce_temp);
    }
    // the algo which comes from the professor`s slides to create produce probability
    word_probs_column.normalize();    
    MultinomialParam para = new MultinomialParam((int)tempDoc.getWordsInDoc().getItem(m),word_probs_column);
    word_probs_column = new Multinomial(prng, para).getNext();
    produce.setColumn(m, word_probs_column);
   }

   produceMap.put(docName, produce);
  }
  catch (OutOfBoundsException e) {
   e.printStackTrace();
  }
 
}

// the private method to generate a single topic probs
 private void updateSingleTopicProbs(String docName) {
  try
  {
   DocSignature docTemp = docList.get(docName);
   // create the matrix of new topic vector
   IDoubleVector topic_vector = new DenseDoubleVector(beta.getLength(),0.0);
   IDoubleVector topic_word_prob = new DenseDoubleVector(beta.getLength(),0.0);
   // use a nested loop to set each element into the double vector
   for (int m = 0; m < beta.getLength(); m++)
   {
    for (int n = 0; n < alpha.getLength(); n++)
     topic_word_prob.setItem(m, topic_word_prob.getItem(m)+produceMap.get(docName).getEntry(m, n));
   }
  // the algo which comes from the professor`s slides to create topic probability
   beta.addMyselfToHim(topic_word_prob);
   DirichletParam para = new DirichletParam(topic_word_prob, 0.00002, 1000);
   topic_vector = (new Dirichlet(prng, para)).getNext();
   DocSignature newDoc = new DocSignature(docName, topic_vector, docTemp.getWordsInDoc());
   docList.put(docName, newDoc);
  }
  catch (OutOfBoundsException e) {
   e.printStackTrace();
  }

 } 

}
