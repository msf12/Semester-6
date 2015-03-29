import java.util.concurrent.Semaphore;
import java.util.Random;


public class Task1 {


	/* a[0] for water, a[1] for ice, a[2] for cup */
	Semaphore[] a = new Semaphore[3];
	Semaphore server = new Semaphore(1);
	Random random = new Random();


	synchronized void Server() throws InterruptedException {
		int i,j,k;
		while(true) {
			i = random.nextInt(3); /* returns a random integer 0, 1 or 2 for i */
			j = random.nextInt(3); /* returns a random integer 0, 1 or 2 for j */
			if (i != j) { /* i and j must be different */
				server.acquire();
				k = 3 - (i+j); /*the drinker with the k-th ingredient
	                                is identified*/
				a[k].release();
			}
		}//end of while
	}//end of Server

	synchronized void Drinker(int r) throws InterruptedException {
		/* r indicates which ingredients this drinker has */
		while(true) {
			a[r].acquire();
//			Drink();
			server.release();
		}
	}//end of Drinker

	public static void main(String[] args) {
		

	}

}
