import java.util.concurrent.Semaphore;
import java.util.Random;


public class Task1 {


	/* a[0] for water, a[1] for ice, a[2] for cup */
	Semaphore[] a = new Semaphore[]{new Semaphore(0),new Semaphore(0),new Semaphore(0)};
	Semaphore server = new Semaphore(1);
	Random random = new Random();


	public class Server implements Runnable {
		
		public void run() {
			int i,j,k;
			while(true) {
				i = random.nextInt(3); /* returns a random integer 0, 1 or 2 for i */
				j = random.nextInt(3); /* returns a random integer 0, 1 or 2 for j */
				if (i != j) { /* i and j must be different */
					try {
						server.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					k = 3 - (i+j); /*the drinker with the k-th ingredient
		                                is identified*/
					
					System.out.println("Serving drinker " + k);
					
					a[k].release();
				}
			}//end of while
		}
	}//end of Server

	public class Drinker implements Runnable {
		
		int r;
		
		public Drinker(int r)
		{
			this.r = r;
		}
		
		public void run() {
			/* r indicates which ingredients this drinker has */
			while(true) {
				try {
					a[r].acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Drinker " + r + " drinking.");
				server.release();
			}
		}
	}//end of Drinker

	public static void main(String[] args) throws InterruptedException {
		Task1 t = new Task1();
		Thread s = new Thread(t.new Server());
		Thread d1 = new Thread(t.new Drinker(0));
		Thread d2 = new Thread(t.new Drinker(1));
		Thread d3 = new Thread(t.new Drinker(2));
		
		s.start();
		d1.start();
		d2.start();
		d3.start();
	}

}
