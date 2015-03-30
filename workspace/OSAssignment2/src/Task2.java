import java.util.concurrent.Semaphore;


public class Task2 {

	Semaphore dentistReady = new Semaphore(0);
	Semaphore seatCountWriteAccess = new Semaphore(1);
	Semaphore patientReady = new Semaphore(0);
	int numFreeWRSeats;
	
	public Task2() {
		this(3);
	}
	
	public Task2(int N) {
		numFreeWRSeats = N;
	}
	
	public class Dentist implements Runnable {
		public void run() {
			while(true)// Run in an infinite loop.
			{
                /* Try to acquire a patient:
                if none is available, go to sleep*/
				try {
					patientReady.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		        /* Awake: try to get access to modify
		        # of available seats otherwise sleep*/
				try {
					seatCountWriteAccess.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/* One waiting room chair becomes free*/
				numFreeWRSeats += 1;
				
				// Doctor is ready to consult.
				dentistReady.release();
				/* Don’t need the lock on the
        		chairs anymore. */
				seatCountWriteAccess.release();
				
				System.out.println("Dentist seeing patient now");
			}
		}
	}
	
	public class Patient implements Runnable {
		
		int id;
		
		public Patient(int id)
		{
			this.id = id;
		}
		
		public void run() {

		    /* Run in an infinite loop to
		    simulate multiple patients */
			while(true)
			{
		        /* Try to get access to the
		        waiting room chairs. */
				try {
					seatCountWriteAccess.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/* If there are any free seats:*/
				if(numFreeWRSeats > 0)
				{
					System.out.println("Patient " + id + " waiting");
					
					/* sit down in a chair */
					numFreeWRSeats -= 1;
					
		            /* notify the dentist, who’s waiting
		            until there is a patient */
					patientReady.release();
					
		            /* don’t need to lock the
		            chairs anymore */
					seatCountWriteAccess.release();

		            /* wait until the dentist is ready */
					try {
						dentistReady.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else
				{
		            /* otherwise, there are no free seats; tough luck
		            /* but don’t forget to release the
		            lock on the seats! */
					System.out.println("Patient " + id + " leaving");
					
					seatCountWriteAccess.release();
				}
				
			}
		}
	}
	
	public static void main(String[] args) {
		Task2 t = new Task2(3);
		Thread d = new Thread(t.new Dentist());
		Thread p1 = new Thread(t.new Patient(1));
		Thread p2 = new Thread(t.new Patient(2));
		Thread p3 = new Thread(t.new Patient(3));
		Thread p4 = new Thread(t.new Patient(4));
		Thread p5 = new Thread(t.new Patient(5));
		
		d.start();
		p1.start();
		p2.start();
		p3.start();
		p4.start();
		p5.start();
	}
	
}
