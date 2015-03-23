import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private int size;
	private Node first,last;
	
	private class Node
	{
		Item item;
		Node next;
	}
	
	public RandomizedQueue()                 // construct an empty randomized queue
	{
		size = 0;
	}
	
	public boolean isEmpty()                 // is the queue empty?
	{
		return size == 0;
	}
	
	public int size()                        // return the number of items on the queue
	{
		return size;
	}
	
	public void enqueue(Item item)           // add the item
	{
		if(item == null)
			throw new NullPointerException();
		Node oldlast = last;
		last = new Node();
		last.item = item;
		if(isEmpty())
			first = last;
		else
			oldlast.next = last;
		size++;
	}
	
	public Item dequeue()                    // delete and return a random item
	{
		if(isEmpty())
			throw new NoSuchElementException();
		int elemToDequeue = StdRandom.uniform(size);
		Node previous = null;
		Node current = first;
		for(int i = 0; i < elemToDequeue; i++)
		{
			previous = current;
			current = current.next;
		}
		if(first == current)
			first = current.next;
		else
			previous.next = current.next;
		if(isEmpty())
			last = null;
		size--;
		return current.item;
	}
	
	public Item sample()                     // return (but do not delete) a random item
	{
		if(isEmpty())
			throw new NoSuchElementException();
		int elemToSample = StdRandom.uniform(size);
		Node current = first;
		for(int i = 0; i < elemToSample; i++)
		{
			current = current.next;
		}
		return current.item;
	}
	
	public Iterator<Item> iterator()         // return an independent iterator over items in random order
	{
		return new RandomizedQueueIterator();
	}
	
	private class RandomizedQueueIterator implements Iterator<Item>
	{
		private Item[] items;
		private int index;
		
		public RandomizedQueueIterator()
		{
			index = 0;
			items = (Item[])(new Object[size]);
			Node current = first;
			for(int i = 0; i < size; i++)
			{
				items[i] = current.item;
				current = current.next;
			}
			for(int i = 0; i < size; i++)
			{
				int j = StdRandom.uniform(size);
				Item temp = items[i];
				items[i] = items[j];
				items[j] = temp;
			}
		}
		
		public boolean hasNext() {
			return index < size;
		}

		public Item next() {
			if(!hasNext())
				throw new NoSuchElementException();
			Item currentItem = items[index];
			index++;
			return currentItem;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public static void main(String[] args)   // unit testing'
	{
		RandomizedQueue<Integer> r = new RandomizedQueue<Integer>();
		for(int i = 0; i < 20; i++)
			r.enqueue(i);
		for(int i : r)
			StdOut.println(i);
	}
}