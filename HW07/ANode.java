/**
 * the class of abstrace node, which will be extended by internal node and leaf node
 * both of the nodes are the internal classes in MTree
 */ 
abstract class ANode {
 
 private Boolean isleaf; // the flag to identify whether the node is leafnode or not
 private int curNum; // the value to express the current size of the node
 private int maxNum; // the value to express the max size of the node
 
 // the consturct method of ANode which can initialize isleaf, curNum and maxNum
 protected ANode(Boolean isleaf,int curNum, int maxNum)
 {
  this.isleaf = isleaf;
  this.curNum = curNum;
  this.maxNum = maxNum;
 }
 
 // a protect method which can get the value of isleaf
 protected Boolean getIsLeaf()
 {
  return isleaf;
 }
 
  // a protect method which can set the value of curNum
 protected void setCurNum(int num)
 {
  curNum = num;
 }
 
   // a protect method which can get the value of curNum
 protected int getCurNum()
 {
  return curNum;
 }
 
   // a protect method which can get the value of maxNum
 protected int getMaxNum()
 {
  return maxNum;
 }
 
}
