
// this class is used as a little container to pass info around regarding a document.  It
// contains the name of the document, the topic_probs vector for the document, and the
// words_in_doc vector for the document.  This is used by the ILDALearner interface.
public class DocSignature {
  
  private String fName;
  private IDoubleVector topicProbs;
  private IDoubleVector wordsInDoc;
  
  public DocSignature (String fNameIn, IDoubleVector topicProbsIn, IDoubleVector wordsInDocIn) {
    fName = fNameIn;
    topicProbs = topicProbsIn;
    wordsInDoc = wordsInDocIn;
  }
  
  // return the file name for the document
  public String getFName () {
    return fName;
  }
  
  // return the vector of topic probs for the document
  public IDoubleVector getTopicProbs () {
    return topicProbs; 
  }
  
  // return the vector of words for the document
  public IDoubleVector getWordsInDoc () {
    return wordsInDoc; 
  }
}
