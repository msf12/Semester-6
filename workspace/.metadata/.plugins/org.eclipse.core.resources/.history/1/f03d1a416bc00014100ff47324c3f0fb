import java.util.Comparator;

public class Term implements Comparable<Term> {

	public final String query;
	public final double weight;

	// Initialize a term with the given query string and weight.
	public Term(String query, double weight)
	{
		if(query == null)
			throw new NullPointerException();
		
		if(weight < 0)
			throw new IllegalArgumentException();
		
		this.query = query;
		this.weight = weight;
	}

	// Compare the terms in descending order by weight.
	public static Comparator<Term> byReverseWeightOrder()
	{
		return new Comparator<Term>() {

			public int compare(Term t1, Term t2) {
				return (t1.weight == t2.weight) ?
						0 :
							((t1.weight < t2.weight) ?
									1 : -1);
			}

		};
	}

	// Compare the terms in lexicographic order but using only the first r characters of each query.
	public static Comparator<Term> byPrefixOrder(int r)
	{
		if(r < 0)
			throw new IllegalArgumentException();
		
		final int numChars = r;
		return new Comparator<Term>() {

			public int compare(Term t1, Term t2) {
				return (t1.query.substring(0, numChars).compareTo(t2.query.substring(0, numChars)));
			}

		};
	}

	// Compare the terms in lexicographic order by query.
	public int compareTo(Term that)
	{
		return this.query.compareTo(that.query);
	}

	// Return a string representation of the term in the following format:
	// the weight, followed by a tab, followed by the query.
	public String toString()
	{
		return this.weight + "\t" + this.query;
	}
	
//	public static void main(String[] args)
//	{
//		Term[] ts = new Term[20];
//		for(int i = 0; i < 20; i++)
//		{
//			ts[i] = new Term("hello"+i,i+(20*Math.random()));
//		}
//		
//		for(int i = 0; i < 20; i++)
//		{
//			System.out.println(ts[i]);
//		}
//		
//		System.out.println();
//		
//		Arrays.sort(ts);
//		
//		for(int i = 0; i < 20; i++)
//		{
//			System.out.println(ts[i]);
//		}
//		
//		System.out.println();
//		
//		Arrays.sort(ts, Term.byReverseWeightOrder());
//		
//		for(int i = 0; i < 20; i++)
//		{
//			System.out.println(ts[i]);
//		}
//		
//		System.out.println();
//		
//		Arrays.sort(ts, Term.byPrefixOrder(2));
//		
//		for(int i = 0; i < 20; i++)
//		{
//			System.out.println(ts[i]);
//		}
//	}
}