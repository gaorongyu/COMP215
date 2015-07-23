package run;å

import java.util.ArrayList;

public class Perm {
	
	private ArrayList<ArrayList<Integer>> returnList = new ArrayList<ArrayList<Integer>>();
	private int k = 0;
	
	public ArrayList<ArrayList<Integer>> allPerms(ArrayList<Integer> ofMe)
	{
		
		if (k==ofMe.size())
		{
			ArrayList<Integer> backup = new ArrayList<Integer>();
			backup.addAll(ofMe);
			returnList.add(backup);
			//System.out.println(ofMe);
		}
		
		for (int i = k; i < ofMe.size(); i++ )
		{
			Integer temp = ofMe.get(i);
			ofMe.set(i,ofMe.get(k));
			ofMe.set(k, temp);
			k++;
			
			allPerms(ofMe);
			
			k--;
			ofMe.set(k,ofMe.get(i));        
			ofMe.set(i,temp);
			
		}
				
		return returnList;
	}

	public static void main(String[] args)
	{
		ArrayList<Integer> ofMe = new ArrayList<Integer>();
		
		ofMe.add(1);
		ofMe.add(2);
		ofMe.add(3);
		ofMe.add(4);
		
		
		System.out.println(new Perm().allPerms(ofMe));
	}
}
