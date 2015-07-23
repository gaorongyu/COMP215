import java.util.ArrayList;
import java.util.List;

/**
 *  the mtree class which can realize the funciton of MTree
 */
public class MTree<Key extends IPointInMetricSpace <Key>, Data> implements IMTree<Key, Data> {
 
 private int internalNodeSize; // the size of each internal node
 private int leafNodeSize; // the size of each leaf node
 private ANode myMTree; // the root node of mTree
 private ANode curNode; // the node used to point the specific node in each step, but the node is just for internal node
 private ANode curLeafNode; // the node used to point the specific node in each step, but the node is just for leaf node
 private DataWrapper<Key,Data> dw; // a data wrapper to wrap the key and data  
 
 /**
  * the construct method to initialize the size of internal node and the size of leaf node
  * also initialzie the myTree node, datawrapper dw and current leaf node
  */ 
 
 public MTree(int internalNodeSize, int leafNodeSize)
 {
  this.internalNodeSize = internalNodeSize; 
  this.leafNodeSize = leafNodeSize;
  this.myMTree = null;
  this.dw = null;
  this.curLeafNode = null;
 }
 
 /**
  * the inner class of leaf node
  * @author Yanda
  */
 private class LeafNode extends ANode{

  private List<DataWrapper<Key,Data>> dataList;

  private LeafNode(Boolean isleaf, int curNum, int maxNum) {
   super(isleaf, curNum, maxNum);
   this.dataList = new ArrayList<DataWrapper<Key,Data>>();
  }
 } 
 /**
  * the inner class of internal node 
  * @author Yanda
  */
 private class InternalNode extends ANode{
  private List<ANode> nodeList;
  private List<Key> keyList;
  private List<Double> distanceList;

  // the construct method of internal node 
  private InternalNode(Boolean isleaf, int curNum, int maxNum) {
   super(isleaf, curNum,maxNum);
   this.nodeList = new ArrayList<ANode>();
   this.keyList = new ArrayList<Key>();
   this.distanceList = new ArrayList<Double>();
  }
  
 }
 /**
  * the method to construct the fist root node -- it is actually a leaf node 
  * @param keyToAdd
  * @param dataToAdd
  */
 @SuppressWarnings("unchecked")
 private void constructRoot(Key keyToAdd, Data dataToAdd) 
 {
  myMTree = new LeafNode(true, 1, leafNodeSize);
  dw = new DataWrapper<Key, Data>(keyToAdd,dataToAdd);
  ((LeafNode)myMTree).dataList.add(dw);
 }

 /**
  * the insert method to insert the key and data into the MTree
  */
 @SuppressWarnings("unchecked")
 @Override
 public void insert(Key keyToAdd, Data dataToAdd) {
  /**
   * generate the first root node, and this root node is a leaf node
   */
  if (myMTree == null)
  {
   constructRoot(keyToAdd,dataToAdd);
   return;
  }
  curNode = myMTree;
  curLeafNode = myMTree;
  /**
   * generate the second node, and finally the code create two leaf nodes and a root node
   */
  if (myMTree.getIsLeaf() == true)
  {
   // initialize the myTree node, and insert the first leaf node and its element into the node 
   myMTree = new InternalNode(false,1, internalNodeSize);
   ((InternalNode)myMTree).nodeList.add(curNode);
   ((InternalNode)myMTree).keyList.add(((LeafNode)curNode).dataList.get(0).getKey());
   Double tempDistance = ((LeafNode)curNode).dataList.get(0).getKey().getDistance(keyToAdd);
   ((InternalNode)myMTree).distanceList.add(tempDistance);
   
   // also create a new leaf node and add a new data wrapper into this leaf node
   dw = new DataWrapper<Key,Data>(keyToAdd,dataToAdd);
   ((LeafNode)curNode).dataList.add(dw);
   ((LeafNode)curNode).setCurNum(((LeafNode)curNode).getCurNum()+1);
   return;
  }
  /**
   * normal insert process which begin at inserting the third node
   */
  double leastDistance = 999999;
  int seq = 0;
  // when the node is the leaf node, the method will still run until the node points to the leaf node
  while (curLeafNode.getIsLeaf() != true)
  {
   // the method to split the internal node
   curNode = splitInternalNode(curNode);
   seq = 0;
   /*
    * the loop to find that node of which the distace value plus the distance value of the key to be inserted is less than both distance
    */  
   for (int i = 0; i < curNode.getCurNum(); i++)
   {
    // temp is the distance between a node with the keyToAdd 
    double temp = ((InternalNode)curNode).keyList.get(i).getDistance(keyToAdd);
    // if temp value is less than least distance, the least distance and sequence will be kept in record
    if (temp < leastDistance)
    {
     leastDistance = temp;
     seq = i;
    }
    // if the value of distance of the leaf node is less than least distance, the least distance will be replaced
    if (((InternalNode)curNode).distanceList.get(i) < leastDistance)
     ((InternalNode)curNode).distanceList.set(i,leastDistance);
   }
   //in the next level of the node is leaf node, the current leaf node will point the next node 
   if (((InternalNode)curNode).nodeList.get(seq).getIsLeaf() == true)
    curLeafNode =  ((InternalNode)curNode).nodeList.get(seq);
   // otherwise the current node will point the next node
   else
    curNode = ((InternalNode)curNode).nodeList.get(seq);    
  }
  
  // if the leaf node is full, then the method will be executed to split the leaf node and add to the up node
  if (((LeafNode)curLeafNode).getCurNum() == ((LeafNode)curLeafNode).getMaxNum())
  {
   // first to add the keyToAdd and dataToAdd in the current leaf node
   dw = new DataWrapper<Key,Data>(keyToAdd,dataToAdd);
   ((LeafNode)curLeafNode).dataList.add(dw);
   ((LeafNode)curLeafNode).setCurNum(((LeafNode)curLeafNode).getCurNum()+1);
   
   // create a temp key to get the farest datawrapper in the leaf node that will be splited
   Key tempKey = null;
   tempKey = findFarData(((LeafNode)curLeafNode)).getKey();
   LeafNode tempLeafNode = generateLeafNode(tempKey,((LeafNode)curLeafNode));
   
   // make curNode to set the key and data and to add the key and data on the list
   ((InternalNode)curNode).keyList.set(seq, findFarData(((LeafNode)curLeafNode)).getKey());
   ((InternalNode)curNode).distanceList.set(seq, findFarData(((LeafNode)curLeafNode)).getData());
   ((InternalNode)curNode).keyList.add(findFarData(tempLeafNode).getKey());
   ((InternalNode)curNode).distanceList.add(findFarData(tempLeafNode).getData());
   ((InternalNode)curNode).nodeList.add(tempLeafNode);
   ((InternalNode)curNode).setCurNum(((InternalNode)curNode).getCurNum()+1);  
   return;
  }
  // the common situation to directly add the data andwrapper into the list and change the current size value
  else
  {
   dw = new DataWrapper<Key,Data>(keyToAdd,dataToAdd);
   ((LeafNode)curLeafNode).dataList.add(dw);
   ((LeafNode)curLeafNode).setCurNum(((LeafNode)curLeafNode).getCurNum()+1);
   return;
  }
 }
 
 /**
  *  the method to generate the leaf node to be used in split method
  */  
 private LeafNode generateLeafNode(Key tempKey, LeafNode node) {
  
  // create a tempLeafNode 
  LeafNode tempLeafNode = new LeafNode(true, 0, leafNodeSize);
  
  // create the temp distance and minium distance and remove index
  double tempDistance = 0;
  double minDistance = 9999999;
  int removeSeq = 0;
  
  // create a temp leaf node to accommodate half max number of the node 
  for (int i=0; i<(Math.floor(node.getMaxNum()/2)); i++)
  {
   minDistance = 9999999;
   // the loop to get the minium distace and remove index
   for (int j=0; j<node.getCurNum(); j++)
   {
    tempDistance = tempKey.getDistance(node.dataList.get(j).getKey());
    if (tempDistance < minDistance)
    {
     minDistance = tempDistance;
     removeSeq = j;
    }
   }
   // remvoe the least distnce node and aa it to temp leaf node
   // also change both current num
   tempLeafNode.dataList.add(node.dataList.remove(removeSeq));
   tempLeafNode.setCurNum(tempLeafNode.getCurNum()+1);
   node.setCurNum(node.getCurNum()-1);
  }
   
  // TODO Auto-generated method stub
  return tempLeafNode;
 }

 // the method to find the farrest data
 private DataWrapper<Key, Double> findFarData(LeafNode node) {
  double maxDistance = 0;
  double distance = 0;
  int seq = 0;

  /**
   * the loop to find the max distance and the index of the data
   */
  for (int i=0; i<node.getCurNum(); i++)
  {
   distance = 0; 
   for (int j=0; j<node.getCurNum(); j++)
   {
    distance = distance + node.dataList.get(i).getKey().getDistance(node.dataList.get(j).getKey());
   }
   if (distance > maxDistance)
   {
    maxDistance = distance;
    seq = i;
   }
  }
  // wrapper the data and its distance and return the wrapper
  DataWrapper<Key,Double> dwdw = new DataWrapper<Key, Double>(node.dataList.get(seq).getKey(),maxDistance);
  return dwdw;
 }

 @SuppressWarnings("unchecked")
 /**
  *  the method to split internal node
  */ 
 private ANode splitInternalNode(ANode node) {
  if (node.getCurNum() == node.getMaxNum())
  {
   ANode tempNode = null;
   Key tempKey = null;
   InternalNode leftInternalNode = null;
   InternalNode rightInternalNode = null;
   tempNode = findFarNode(node).getKey();
   tempKey = findFarNode(node).getData().getKey();
   leftInternalNode = gernerateInternalNode(tempNode,node, tempKey).getKey();
   rightInternalNode = gernerateInternalNode(tempNode,node, tempKey).getData();
   // clear the nodelist and re add both internal node 
   ((InternalNode)node).nodeList.clear();
   ((InternalNode)node).nodeList.add(leftInternalNode);
   ((InternalNode)node).nodeList.add(rightInternalNode);
   // clear the keylist and re add both internal node 
   ((InternalNode)node).keyList.clear();
   ((InternalNode)node).keyList.add(findFarNode(leftInternalNode).getData().getKey());
   ((InternalNode)node).keyList.add(findFarNode(rightInternalNode).getData().getKey());
   // clear the datalist and re add both internal node 
   ((InternalNode)node).distanceList.clear();
   ((InternalNode)node).distanceList.add(findFarNode(leftInternalNode).getData().getData());
   ((InternalNode)node).distanceList.add(findFarNode(rightInternalNode).getData().getData()); 
   
  }
  return node;  
 }

 @SuppressWarnings("unchecked")
 /**
  *  the method to generate an internal node 
  */ 
 private DataWrapper<InternalNode, InternalNode> gernerateInternalNode(ANode tempNode, ANode node, Key tempKey) {
   // wrap the left internal node and right internal node
  DataWrapper<InternalNode, InternalNode> ii = null;
  InternalNode left = new InternalNode(false,internalNodeSize,internalNodeSize);
  
  // add the three element list into the left internal node
  left.nodeList.addAll(((InternalNode)node).nodeList);
  left.keyList.addAll(((InternalNode)node).keyList);
  left.distanceList.addAll(((InternalNode)node).distanceList);
  
  InternalNode right = new InternalNode(false,0,internalNodeSize);
  double mindistance = 999999;
  double tempdistance = 0;
  int removeseq = 0;
  
  // find the minium distance and the remove sequence
  for (int i = 0; i < Math.floor(internalNodeSize/2); i++ )
  {
   mindistance = 999999;
   for (int j=0; j<node.getCurNum(); j++)
   {
    tempdistance = tempKey.getDistance(((InternalNode)node).keyList.get(j));
    if (tempdistance < mindistance)
    {
     mindistance = tempdistance;
     removeseq = j;
    }
   } 
   
   //  add the three element list into the left internal node
   right.nodeList.add(left.nodeList.remove(removeseq));
   right.keyList.add(left.keyList.remove(removeseq));
   right.distanceList.add(left.distanceList.remove(removeseq));   
   left.setCurNum(left.getCurNum()-1);
   right.setCurNum(right.getCurNum()+1);
  }
  // wrap both internal node into ii and return it
  ii = new DataWrapper<InternalNode, InternalNode>(left,right);
  return ii;
 }

 @SuppressWarnings("unchecked")
 /**
  *  the method to find the farest node in the internal node, and the method will return the data wrapper 
  */ 
 private DataWrapper<ANode, DataWrapper<Key,Double>> findFarNode(ANode node) {
  // TODO Auto-generated method stub
  double maxdistance = 0;  // the invariant to record the max distance
  double distance = 0; // the invariant to record the sum of the distace in each loop
  int seq = 0; // the invariant to record the index of the max distance
  DataWrapper<Key,Double> dwkd = null;  // the data wrapper to wrap the key vaule and distance value
  DataWrapper<ANode,DataWrapper<Key,Double>> dwnd= null; // the data wrapper to wrap the abstract node and above datawrapper
  // the loop to get the max distance and the sequence
   for (int i=0; i<node.getCurNum(); i++)
   {
    distance = 0; 
    for (int j=0; j<node.getCurNum(); j++)
    {
     distance = distance + ((InternalNode)node).keyList.get(i).getDistance(((InternalNode)node).keyList.get(j));
    }
    if (distance > maxdistance)
    {
     maxdistance = distance;
     seq = i;
    }
   }
  // wrap the elements and return them
  dwkd = new DataWrapper<Key,Double>(((InternalNode)node).keyList.get(seq),maxdistance);
  dwnd = new DataWrapper<ANode,DataWrapper<Key,Double>>(((InternalNode)node).nodeList.get(seq),dwkd);
  return dwnd;
 }

 /**
  * the retrun method to return the data wrapper in the requested range 
  */
 @Override
 public ArrayList<DataWrapper<Key, Data>> find(Key query, double distance) {
  
  curNode = myMTree;
  
  ArrayList<DataWrapper<Key,Data>> findList = new ArrayList<DataWrapper<Key, Data>>();
  
     findList = helpfind(findList, query, distance, curNode);
  
  return findList;
 }

 @Override
 public ArrayList<DataWrapper<Key, Data>> findKClosest(Key query, int k) {
  // TODO Auto-generated method stub
  return new ArrayList<DataWrapper<Key, Data>>();
 }

 @SuppressWarnings("unchecked")
 @Override
 /*
  *  the fourth method which can gain the depth of the tree
  */ 
 public int depth() {
   // if the tree is null, then directly return the null value
  if (myMTree == null)
   return 0;
  //  if the tree is not null, the use a loop to reach the leaf node to find the depth 
  else
  {
   curNode = myMTree;
   int depth = 1;
   while (curNode.getIsLeaf() != true)
   {
    curNode = ((InternalNode)curNode).nodeList.get(0);
    depth ++;
   }
   return depth;
  }
 }
 
 /**
  *  the help method to find all the nodes which qualified the search request 
  */ 
 @SuppressWarnings("unchecked")
 private ArrayList<DataWrapper<Key, Data>> helpfind (ArrayList<DataWrapper<Key, Data>> findList, Key query, double distance, ANode node)
 {
  ANode findNode = null;
  /**
   *  the loop method to find all the node under the internal node
   */ 
  for (int j = 0; j < node.getCurNum(); j++)
  {
   findNode = ((InternalNode)node).nodeList.get(j);
   if (findNode.getIsLeaf() == true)
   {
    for (int i = 0; i < ((LeafNode)findNode).getCurNum() ; i++)
    {
     if (query.getDistance(((LeafNode)findNode).dataList.get(i).key) <= distance)
      findList.add(((LeafNode)findNode).dataList.get(i));
    }
   }
  }
  // return the arraylist
  return findList;

 }
}
