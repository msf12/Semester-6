import java.util.LinkedList;
import java.util.List;


public class KdTreeST<Value> {
	
	private class Node
	{
		Point2D p;
		Value value;
		Node left,right;
		RectHV rect;
		
		public Node(Point2D point, Value val,RectHV rectangle)
		{
			p = point;
			value = val;
			rect = rectangle;
		}
	}
	
	private Node root;
	private int size;
	
	public KdTreeST()                                // construct an empty symbol table of points 
	{
		root = null;
		size = 0;
	}
	
	public boolean isEmpty()                      // is the symbol table empty? 
	{
		return size == 0;
	}
	
	public int size()                         // number of points 
	{
		return size;
	}
	
	public void put(Point2D p, Value val)      // associate the value val with point p
	{
		root = put(root,p,val,true,new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
		size++;
	}
	
	private Node put(Node n,Point2D p,Value val,boolean orientation,RectHV rect) //orientation true = x oriented node false = y oriented node
	{
		if(n == null)
			return new Node(p,val,rect);

		Point2D np = n.p;

		if(orientation)
			if(np.x() > p.x())
				n.left = put(n.left,p,val,(!orientation),new RectHV(rect.xmin(),rect.ymin(),np.x(),rect.ymax()));
			else
				n.right = put(n.right,p,val,(!orientation),new RectHV(np.x(),rect.ymin(),rect.xmax(),rect.ymax()));
		else
			if(np.y() > p.y())
				n.left = put(n.left,p,val,(!orientation),new RectHV(rect.xmin(),rect.ymin(),rect.xmax(),np.y()));
			else
				n.right = put(n.right,p,val,(!orientation),new RectHV(rect.xmin(),np.y(),rect.xmax(),rect.ymax()));
		
		return n;
	}
	
	public Value get(Point2D p)                 // value associated with point p 
	{
		return get(root,p,true);
	}
	
	private Value get(Node n,Point2D p,boolean orientation)
	{
		if(n==null)
			return null;
		
		Point2D np = n.p;
		
		if(np.equals(p))
			return n.value;
		
		Node nextNode;
		
		if(orientation)
			nextNode = (np.x() > p.x()) ? n.left : n.right;
		else
			nextNode = (np.y() > p.y()) ? n.left : n.right;
		
		return get(nextNode,p,(!orientation));
	}
	
	public boolean contains(Point2D p)            // does the symbol table contain point p? 
	{
		return get(p) != null;
	}
	
	public Iterable<Point2D> points()                       // all points in the symbol table 
	{
		Queue<Node> q = new Queue<Node>();
		List<Point2D> points = new LinkedList<Point2D>();
		
		if(root==null)
			return points;
		
		points.add(root.p);
		q.enqueue(root);
		
		while(!q.isEmpty())
		{
			Node currentNode = q.dequeue();
			
			if(currentNode.left!=null)
			{
				points.add(currentNode.left.p);
				q.enqueue(currentNode.left);
			}
			if(currentNode.right!=null)
			{
				points.add(currentNode.right.p);
				q.enqueue(currentNode.right);
			}
				
		}
		
		return points;
	}
	
	public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle 
	{
		List<Point2D> points = new LinkedList<Point2D>();

		range(rect,root,points,true);
		
		return points;
	}
	
	private void range(RectHV rect, Node n, List<Point2D> points,boolean orientation)
	{
		Point2D np = n.p;
		
		if(rect.contains(np))
			points.add(np);
		
		Node left = n.left,right = n.right;
		
		if(left!=null && left.rect.intersects(rect))
			range(rect,n.left,points,(!orientation));
		if(right != null && right.rect.intersects(rect))
			range(rect,n.right,points,(!orientation));
	}
	
	public Point2D nearest(Point2D p)             // a nearest neighbor to point p; null if the symbol table is empty 
	{
		return nearest(p,null,Double.POSITIVE_INFINITY,root,true);
	}
	
	private Point2D nearest(Point2D p,Point2D closest,double distance,Node n,boolean orientation)
	{
		if(n==null || distance < n.rect.distanceSquaredTo(p))
			return closest;
		
		Point2D np = n.p;
		
		if(np.distanceSquaredTo(p) < distance)
		{
			closest = np;
			distance = np.distanceSquaredTo(p);
		}
		
		if(orientation)
			if(np.x() > p.x())
			{
				closest = nearest(p,closest,distance,n.left,(!orientation));
				closest = nearest(p,closest,distance,n.right,(!orientation));
			}
			else
			{
				closest = nearest(p,closest,distance,n.right,(!orientation));
				closest = nearest(p,closest,distance,n.left,(!orientation));
			}
		else
			if(np.y() > p.y())
			{
				closest = nearest(p,closest,distance,n.left,(!orientation));
				closest = nearest(p,closest,distance,n.right,(!orientation));
			}
			else
			{
				closest = nearest(p,closest,distance,n.right,(!orientation));
				closest = nearest(p,closest,distance,n.left,(!orientation));
			}
		
		return closest;
	}
	
	public static void main(String[] args)                  // unit testing of the methods (not graded) 
	{
		KdTreeST<Integer> t = new KdTreeST<Integer>();
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
