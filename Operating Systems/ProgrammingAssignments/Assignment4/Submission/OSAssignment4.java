import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class OSAssignment4 {

	public static void main(String[] args) throws IOException
	{
		File input = new File(args[0]);
		Inode node = new Inode(input.getName());
		readInputFile(input,node);
		
		if(args.length > 1)
		{
			File access_trace = new File(args[1]);
			Scanner s = new Scanner(access_trace);
			while(s.hasNext())
			{
				String[] line = s.nextLine().split(", ");
				if(line[0].equals("R"))
				{
					String toprint = node.read(Integer.parseInt(line[1]));
					//if the read wasn't successful print invalid block number and the number
					System.out.println((toprint == null ? "Invalid block number: " + line[1] : toprint));
				}
				else if(line[0].equals("W"))
				{
					node.write(Integer.parseInt(line[1]), line[2]);
				}
			}
		}
	}
	
	//read the file and put its contents into the inode, printing if there is an error
	private static void readInputFile(File input,Inode i) throws IOException
	{
		Scanner s = new Scanner(input);
		while(s.hasNext())
		{
			String line = s.nextLine();
			if(line.charAt(0) != '#')
			{
				if(!i.addData(line.substring(line.indexOf(",")+1)))
					System.out.println("File too large");
			}
		}
		i.store();
	}
	
}
