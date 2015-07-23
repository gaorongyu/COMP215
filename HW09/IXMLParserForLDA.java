

// To use this parser, just repeatedly call "nextTopic" to get the word_probs vector
// for each of the topics, until you get a null.  Then you are done obtaining all of
// the words_probs vectors. Next, you repeatedly call "nextDoc" until you get a null.
// Then you are done obtaining all of the documents in the file.
//
interface IXMLParserForLDA {
  
  // this returns an IDoubleVector corresponding to the next topic described in the XML file.  If
  // no topics are left, this returns null. 
  IDoubleVector nextTopic ();
  
  // this returns a DocSignature object corresponding to the next document described in the XML file.
  // If no topics are left, this returns null
  DocSignature nextDoc ();
  
}