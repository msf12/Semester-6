import java.util.Arrays;


public class Autocomplete {
	private final Term[] termList;
	
	// Initialize the data structure from the given array of terms.
	public Autocomplete(Term[] terms)
	{
		if(terms == null)
			throw new NullPointerException();
		Arrays.sort(terms);
		termList = terms;
	}

	// Return all terms that start with the given prefix, in descending order of weight.
	public Term[] allMatches(String prefix)
	{
		if(prefix == null)
			throw new NullPointerException();
		
		int firstTerm = BinarySearchDeluxe.firstIndexOf(termList, new Term(prefix,0), Term.byPrefixOrder(prefix.length())),
			lastTerm = BinarySearchDeluxe.lastIndexOf(termList, new Term(prefix,0), Term.byPrefixOrder(prefix.length()));
		Term[] matches = new Term[lastTerm-firstTerm+1];
		for(int i = firstTerm;i<=lastTerm;i++)
		{
			matches[i-firstTerm] = termList[i];
		}
		
		Arrays.sort(matches,Term.byReverseWeightOrder());
		
		return matches;
	}

	// Return the number of terms that start with the given prefix.
	public int numberOfMatches(String prefix)
	{
		if(prefix == null)
			throw new NullPointerException();
		
		int firstTerm = BinarySearchDeluxe.firstIndexOf(termList, new Term(prefix,0), Term.byPrefixOrder(prefix.length())),
			lastTerm = BinarySearchDeluxe.lastIndexOf(termList, new Term(prefix,0), Term.byPrefixOrder(prefix.length()));
		return lastTerm-firstTerm+1;
	}
	
//	public static void main(String[] args) throws FileNotFoundException {
//		Scanner scan = new Scanner(new File("/home/mitchel/Documents/Div 2/Semester 6/workspace/Assignment3/src/test.txt"));
//		Term[] ts = new Term[166];
//		for(int i = 0;scan.hasNext();i++)
//		{
//			String line = scan.nextLine();
//			int tab = line.indexOf('\t');
//			ts[i] = new Term(line.substring(tab+1),Integer.parseInt(line.substring(0, tab)));
//		}
//		scan.close();
//		
//		Autocomplete auto = new Autocomplete(ts);
//		
//		auto.allMatches("2");
//	}

}
