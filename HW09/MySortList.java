package run;

public class MySortList {
	
	static ANode head;
	
	abstract class ANode
	{
		protected ANode next;
		protected Integer num;
		
		private ANode()
		{
			next = null;
			num = null;
		}
		
		protected abstract ANode insert(Integer num);
	}
	
	public MySortList()
	{
		this.head = new EndNode();

	}
	
	private class NormalNode extends ANode
	{

		@Override
		protected ANode insert(Integer num) {
			if (this.num <= num)
			{
				this.next = this.next.insert(num);
				return this;
			}
			else
			{
				ANode insert = new NormalNode();
				insert.next = this;
				insert.num = num;
				return insert;
			}
			// TODO Auto-generated method stub

		}
		
		
	}
	
	private class EndNode extends ANode
	{
		@Override
		protected ANode insert(Integer num) {
			ANode insert = new NormalNode();
			insert.next = this;
			insert.num = num;
			return insert;

		}
		
	}
	
	public static void insert(Integer num)
	{
		head = head.insert(num);
	}
	
	public static void main(String args[]) {
		
		MySortList list = new MySortList();
		
		insert(4);
		insert(6);
		insert(5);
		
		System.out.println(head.next);
		System.out.println(head.num);
		System.out.println(head.next.num);
		System.out.println(head.next.next.num);
		

	}
}
