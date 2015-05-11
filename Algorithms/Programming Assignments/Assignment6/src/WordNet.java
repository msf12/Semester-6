import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class WordNet {

	private class SynSet
	{
		public final int ID;
		public List<String> nouns;
		
		public SynSet(int i)
		{
			this.ID = i;
			this.nouns = new LinkedList<String>();
		}
		
		public void addNouns(String[] m)
		{
			for(String s : m)
			{
				nouns.add(s);
			}
		}
		
	}
	
	List<SynSet> ssets;
	Digraph hnyms;
	List<String> nouns;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) throws FileNotFoundException
	{
		int V = 0;
		ssets = new ArrayList<SynSet>();
		Scanner synscan = new Scanner(new File(synsets));
		while(synscan.hasNext())
		{
			String[] inputs = synscan.nextLine().split(",");
			SynSet newset = new SynSet(Integer.parseInt(inputs[0]));
			newset.addNouns(inputs[1].split(" "));
			ssets.add(newset);
			V++;
		}
		synscan.close();
		hnyms = new Digraph(V);
		Scanner hypscan = new Scanner(new File(hypernyms));
		while(hypscan.hasNext())
		{
			String[] inputs = synscan.nextLine().split(",");
			int hypo = Integer.parseInt(inputs[0]);
			for(int i = 1; i < inputs.length; i++)
			{
				hnyms.addEdge(hypo, Integer.parseInt(inputs[i]));
			}
		}
		hypscan.close();

		nouns = new LinkedList<String>();
		for(SynSet syn : ssets)
		{
			for(String s : syn.nouns)
			{
				if(!nouns.contains(s))
					nouns.add(s);
			}
		}
	}

	// returns all WordNet nouns
	public Iterable<String> nouns()
	{
		return nouns;
	}
	
	// is the word a WordNet noun?
	public boolean isNoun(String word)
	{
		return nouns.contains(word);
	}
	
	// distance between nounA and nounB
	public int distance(String nounA, String nounB)
	{
		
	}
	
	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	public String sap(String nounA, String nounB)
	{
		
	}
	
	public static void main(String[] args) {

	}

}
