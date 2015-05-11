import java.util.LinkedList;
import java.util.List;


public class SAP {

	private final Digraph graph;
	
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G)
	{
		graph = G;
	}

	// is the digraph a directed acyclic graph?
	public boolean isDAG()
	{
		DirectedCycle dc = new DirectedCycle(graph);
		return !dc.hasCycle();
	}

	// is the digraph a rooted DAG?
	public boolean isRootedDAG()
	{
		if(!isDAG())
			return false;
	
		int rootcount = 0;
		for(int i = 0; i < graph.V()&&rootcount < 2; i++)
		{
			if(graph.outdegree(i) == 0)
				rootcount++;
		}
		
		if(rootcount < 2)
			return true;
		return false;
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w)
	{
		//create symbol table/2D list
		//index by distance from v to get ancestors at that index
		return -1;
	}
	
	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
//	public int ancestor(int v, int w)

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
//	public int length(Iterable<Integer> v, Iterable<Integer> w)

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
//	public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
