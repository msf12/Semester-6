import java.sql.Time;


public class Task3 {

	public class Espionage {

		private String message; //the current message being passed

		public synchronized void dropSpyMsg(String msg) throws InterruptedException
		{
			while(message != null) //spies wait until the previous message has been picked up
				wait();
			message = msg; //place the new message
			notify(); //notify the waiting agent
		}

		public synchronized String agentPickMsg() throws InterruptedException
		{
			while(message == null) //agents wait until there is a message to pick up
				wait();
			String returnString = message; //save the message information
			message = null; //clear the message
			notifyAll(); //notify waiting spies that they can now leave messages
			return returnString; //return the message
		}


	}

	public static void main(String[] args) {
		Task3 t = new Task3();

		final Espionage e = t.new Espionage();

		Thread spy1 = new Thread(new Runnable(){
			public void run()
			{
				while(true)
					try {
						e.dropSpyMsg(Math.random()+" - Spy1");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		});

		Thread spy2 = new Thread(new Runnable(){
			public void run()
			{
				while(true)
					try {
						e.dropSpyMsg(Math.random()+" - Spy2");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		});

		Thread spy3 = new Thread(new Runnable(){
			public void run()
			{
				while(true)
					try {
						e.dropSpyMsg(Math.random()+" - Spy3");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		});

		Thread agent = new Thread(new Runnable(){
			public void run()
			{
				while(true)
					try {
						System.out.println(e.agentPickMsg());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		});

		spy1.start();
		spy2.start();
		spy3.start();
		agent.start();

	}

}
