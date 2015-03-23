import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Scheduler {

	public static void main(String[] args) throws FileNotFoundException {
		
		//Ensures valid input
		if(args.length < 1)
			throw new IllegalArgumentException();
		
		Scanner scan = new Scanner(new File(args[0]));
		
		//Due to side effects (and the laziness of the programmer) each simulation uses a different map
		//maps used to easily compensate for PIDs starting at 0 or 1 (and also cause I like them)
		Map<Integer,Integer> jobsFCFS = new HashMap<Integer,Integer>();
		Map<Integer,Integer> jobsRR = new HashMap<Integer,Integer>();
		Map<Integer,Integer> jobsSJF = new HashMap<Integer,Integer>();
		
		//Reads the jobs and parses them into the maps
		while(scan.hasNextLine())
		{
			String[] nextJob = scan.nextLine().split(",");
			if(nextJob[0].charAt(0) == '#')
				continue;
			
			int pid = Integer.parseInt(nextJob[0]);
			int jobTime = Integer.parseInt(nextJob[1]);
//			System.out.println(pid + "," + jobTime);
			
			jobsFCFS.put(pid, jobTime);
		}
		
		jobsRR.putAll(jobsFCFS);
		jobsSJF.putAll(jobsFCFS);
		
		System.out.println("\nFCFS");
		FCFS(jobsFCFS);
		
		System.out.println("\nRR");
		RR(jobsRR); //default time quantum is 1
		
//		for(int i = 1;i<11;i++)
//		{
//			System.out.println("\nRR" + i);
//			Map<Integer, Integer> temp = new HashMap<Integer,Integer>();
//			temp.putAll(jobsFCFS);
//			RR(temp,i);
//		}
				
		System.out.println("\nSJF");
		SJF(jobsSJF);
		
		scan.close();
	}
	
	public static void FCFS(Map<Integer,Integer> jobs)
	{
		int totalWaitTime = 0;
		int totalCompletionTime = 0;
		int timeElapsed = 0;
		
		for(int i = 0; i <= jobs.size(); i++)
		{
			if(!jobs.containsKey(i)) //compensates for PID=0 or PID=jobs.size()
				continue;
			
			int timeToComplete = jobs.get(i); //time to complete the current job
			
			totalWaitTime += timeElapsed; //this job's wait time is the elapsed time so far
			
			timeElapsed += timeToComplete; //add the job's CPU burst time to timeElapsed
			
			totalCompletionTime += timeElapsed; //add timeElapsed to total time to complete all processes
		}
		
		SchedStats(jobs.size(),timeElapsed,totalCompletionTime,totalWaitTime);
	}
	
	public static void RR(Map<Integer,Integer> jobs)
	{
		RR(jobs, 1); //default quantum is 1
	}
	
	public static void RR(Map<Integer,Integer> jobs, int quantum)
	{
		int totalWaitTime = 0;
		int totalCompletionTime = 0;
		int timeElapsed = 0;
		int totalJobs = jobs.size();
		
		final int TIME_QUANTUM = quantum;
		
		while(jobs.size() > 0)
		{
			for(int i = 0; i <= totalJobs; i++)
			{
				
				if(!jobs.containsKey(i)) //compensates for PID=0 or PID=jobs.size()
					continue;

				int timeToComplete = jobs.get(i);
				
				if(timeToComplete <= TIME_QUANTUM) //if current job will be completed this time quantum
				{
					totalWaitTime += timeElapsed; //the time before a job is completed including running time (which is subtracted when it runs before being completed)
					jobs.remove(i); //remove completed job
					timeElapsed += timeToComplete; //add the job's CPU burst time to timeElapsed
					totalCompletionTime += timeElapsed; //add timeElapsed to total time to complete all processes
				}
				else
				{
					totalWaitTime-=TIME_QUANTUM; //every time a job is run the time that job spends running is not counted
					jobs.put(i, timeToComplete-TIME_QUANTUM);
					timeElapsed += TIME_QUANTUM;
				}
			}
		}
		
		SchedStats(totalJobs,timeElapsed,totalCompletionTime,totalWaitTime);
	}
	
	public static void SJF(Map<Integer,Integer> j)
	{
		List<Integer> times = new ArrayList<Integer>(j.values()); //get the list of CPU burst times
		
		Map<Integer,Integer> jobs = new HashMap<Integer,Integer>();
		
		Collections.sort(times); //sort the completion times
		
		/*
		 * reorder the job completion times so the shortest is first
		 * this is equivalent to sorting the actual jobs but without reordering the PIDs
		 */
		for(int i = 0; i < times.size(); i++)
			jobs.put(i, times.get(i));
		
		//SJF is a shortest first sorted FCFS
		FCFS(jobs);
	}
	
	public static void SchedStats(int nJobs, int tElapsed, int tCompletionTime, int tWaitTime)
	{
		//convert all values to doubles for precision
		double numJobs = nJobs;
		double timeElapsed = tElapsed;
		double totalWaitTime = tWaitTime;
		double totalCompletionTime = tCompletionTime;
		
		//compute and print statistics
		System.out.println("Average turnaround: " + totalCompletionTime/numJobs + " seconds/job" + 
				"\nAverage wait time: " + totalWaitTime/numJobs + " seconds" +
				"\nThroughput: " + timeElapsed/60.0 + " jobs/minute");
	}

}
