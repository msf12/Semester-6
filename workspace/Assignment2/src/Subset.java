import java.util.Scanner;

public class Subset {
	
	public static void main(String[] args)
	{
		int N = Integer.parseInt(args[0]);
		StdOut.println("arg = " + N);
		RandomizedQueue<String> rq = new RandomizedQueue<String>();
		Scanner s = new Scanner(System.in);
		while(s.hasNext())
		{
			rq.enqueue(s.next());
		}
		for(int i = 0; i < N; i++)
			StdOut.println(rq.dequeue());
		s.close();
	}
	
}