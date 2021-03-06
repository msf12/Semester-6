import java.util.LinkedList;
import java.util.List;



public class PointST<Value> {
	
	private RedBlackBST<Point2D, Value> st;
	
	public PointST()                                // construct an empty symbol table of points 
	{
		st = new RedBlackBST<Point2D,Value>();
	}
	
	public boolean isEmpty()                      // is the symbol table empty? 
	{
		return st.size() == 0;
	}
	
	public int size()                         // number of points 
	{
		return st.size();
	}
	
	public void put(Point2D p, Value val)      // associate the value val with point p
	{
		st.put(p, val);
	}
	
	public Value get(Point2D p)                 // value associated with point p 
	{
		return st.get(p);
	}
	
	public boolean contains(Point2D p)            // does the symbol table contain point p? 
	{
		return st.contains(p);
	}
	
	public Iterable<Point2D> points()                       // all points in the symbol table 
	{
		return st.keys();
	}
	
	public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle 
	{
		Iterable<Point2D> ps = points();
		List<Point2D> insidePoints = new LinkedList<Point2D>();
		
		for(Point2D p : ps)
		{
			if(rect.contains(p))
				insidePoints.add(p);
		}
		
		return insidePoints;
	}
	
	public Point2D nearest(Point2D p)             // a nearest neighbor to point p; null if the symbol table is empty 
	{
		if(st.size()==0)
			return null;
		
		Point2D c = st.ceiling(p);
		Point2D f = st.floor(p);
		
		if(c == null)
			return f;
		if(f == null)
			return c;
		return (p.distanceSquaredTo(f) > p.distanceSquaredTo(c)) ? c : f;
		
	}
	
	public static void main(String[] args) {
		PointST<Integer> t = new PointST<Integer>();
		t.put(new Point2D(5,5), 0);
		t.put(new Point2D(4,5), 1);
		t.put(new Point2D(6,5), 2);
		t.put(new Point2D(4,6), 3);
		t.put(new Point2D(4,4), 4);
		t.put(new Point2D(6,6), 5);
		
		for(Point2D p : t.points())
			System.out.println("X: " + p.x() + " | Y: " + p.y());
		
		System.out.println("\nGet (6,6): " + t.get(new Point2D(6,6)));
		
		System.out.println("\nRange (3,3,5,7): " + t.range(new RectHV(3,3,5,7)));
		
		System.out.println("\nNearest (5.1,5.1): " + t.nearest(new Point2D(5.1,5.1)));
		System.out.println("Nearest (1,1): " + t.nearest(new Point2D(1,1)));

	}

}
