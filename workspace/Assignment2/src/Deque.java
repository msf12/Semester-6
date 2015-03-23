import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
	private int size;
	private Node first,last;
	
	private class Node
	{
		Item item;
		Node next;
		Node previous;
	}
	
	public Deque()                           // construct an empty deque
	{
		size = 0;
	}
	
	public boolean isEmpty()                 // is the deque empty?
	{
		return size == 0;
	}
	
	public int size()                        // return the number of items on the deque
	{
		return size;
	}
	
	public void addFirst(Item item)          // insert the item at the front
	{
		if(item == null)
			throw new NullPointerException();
		Node oldfirst = first;
		first = new Node();
		first.item = item;
		first.next = oldfirst;
		if(isEmpty())
			last = first;
		size++;
	}
	
	public void addLast(Item item)           // insert the item at the end
	{
		if(item == null)
			throw new NullPointerException();
		Node oldlast = last;
		last = new Node();
		last.item = item;
		last.previous = oldlast;
		if(isEmpty())
			first = last;
		else
			oldlast.next = last;
		size++;
	}
	
	public Item removeFirst()                // delete and return the item at the front
	{
		if(isEmpty())
			throw new NoSuchElementException();
		Item item = first.item;
		first = first.next;
		if(first != null)
			first.previous = null;
		else
			last = null;
		size--;
		return item;
	}
	
	public Item removeLast()                 // delete and return the item at the end
	{
		if(isEmpty())
			throw new NoSuchElementException();
		Item item = last.item;
		last = last.previous;
		if(last != null)
			last.next = null;
		else
			first = null;
		size--;
		return item;
	}
	
	public Iterator<Item> iterator()         // return an iterator over items in order from front to end
	{
		return new DequeIterator();
	}
	
	private class DequeIterator implements Iterator<Item>
	{
		private Node current = first;
		
		public boolean hasNext() {
			return current != null;
		}

		public Item next() {
			if(!hasNext())
				throw new NoSuchElementException();
			Item currentItem = current.item;
			current = current.next;
			return currentItem;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public static void main(String[] args)   // unit testing
	{
		Deque<Integer> d = new Deque<Integer>();
		for(int i = 0;i<20;i++)
			d.addLast(i);
		System.out.println(d.removeLast() + "\n" + d.removeFirst());
	}

}