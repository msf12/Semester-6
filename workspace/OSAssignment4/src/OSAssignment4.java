import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class OSAssignment4 {

	public static void main(String[] args) throws IOException
	{
		String filename = args[0].substring(args[0].lastIndexOf("/")+1);
		File input = new File(args[0]);
		String outdirname = filename + "_inode";
		File outdir = new File(outdirname);
		if(!outdir.exists())
		{
			outdir.mkdir();
		}
		createInode(input,outdirname);
	}
	
	private static void createInode(File input, String outdir) throws IOException
	{
		List<String> blocklist = readInputFile(input);
		
		for(int i = 0; i < blocklist.size(); i++)
		{
			File f = new File(outdir + "/block" + i + ".txt");
			if(!f.exists())
				f.createNewFile();
			FileWriter fw = new FileWriter(f);
			fw.write(blocklist.get(i));
			fw.close();
		}
		inodewriter.close();
		
	}
	
	private static List<String> readInputFile(File input) throws FileNotFoundException
	{
		List<String> blocklist = new LinkedList<String>();
		Scanner s = new Scanner(input);
		while(s.hasNext())
		{
			String line = s.nextLine();
			if(line.charAt(0) != '#')
				blocklist.add(line.substring(line.indexOf(",")+1));
		}
		return blocklist;
	}
	
}
