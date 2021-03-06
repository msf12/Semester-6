import java.util.Comparator;

public class BinarySearchDeluxe {
	
    // Return the index of the first key in a[] that equals the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator)
    {
		if(a == null || key == null || comparator == null)
			throw new NullPointerException();
    	
    	int index = search(a,key,comparator);
    	
    	for(;index>0&&comparator.compare(a[index],a[index-1])==0;index--);
    	
		return index;
    }

    // Return the index of the last key in a[] that equals the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator)
    {
		if(a == null || key == null || comparator == null)
			throw new NullPointerException();
		
    	int index = search(a,key,comparator);
    	
    	for(;index>=0&&index<a.length-1&&comparator.compare(a[index],a[index+1])==0;index++);
    	
		return index;
    }
    
    private static <Key> int search(Key[] a, Key key, Comparator<Key> comparator)
    {
    	int index = -1;
    	
    	for(int start=0, end=a.length-1, current=(start+end)/2; start<end&&index<0;current=(start+end)/2)
    	{
    		int comp = comparator.compare(key, a[current]);
    			if(comp < 0)
    				end=current;
    			else if(comp > 0)
    				start=current;
    			else
    				index=current;
    	}
    	
    	return index;
    }
    
//	public static void main(String[] args) {
//		Integer[] a = new Integer[]{1,1,2,2,3,3,4,4};
//		
//		System.out.println(BinarySearchDeluxe.lastIndexOf(a, 4, new Comparator<Integer>(){
//
//			@Override
//			public int compare(Integer o1, Integer o2) {
//				return o1.compareTo(o2);
//			}
//			
//		}));
//	}

}
