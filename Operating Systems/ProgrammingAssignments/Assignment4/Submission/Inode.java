import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Inode {

	String location; //inode directory (based on the name of the given input file)
	String[] direct; //array of direct block data (simulates array of pointers to blocks)
	String[] indirect; //array of indirect block data (simulates a pointer to a list of block pointers)
	String[][] dindirect; //array of String arrays (simulates an array of pointers to arrays of block pointers)
	int datacount; //count of stored blocks

	public Inode(String inputfile)
	{
		this.location = inputfile + "_inode"; //inode directory = filename.ext_inode
		File locdir = new File(location); //create the directory if it doesn't exist
		if(!locdir.exists())
			locdir.mkdir();
		direct = new String[12]; //inode stores 12 direct block pointers
		datacount = 0; //initial datacount is 0
	}

	//add a new block to the inode and return if successful
	public boolean addData(String data)
	{
		if(datacount > 10112) //max possible blocks this inode can store is 12 direct + 100 indirect + 100*100 double indirect = 10112 
			return false;
		if(datacount < 12) //if direct
		{
			direct[datacount] = data;
		}
		else if(datacount < 112) //if indirect
		{
			if(indirect == null) //create the indirect array if necessary
				indirect = new String[100];
			indirect[datacount-12] = data; //index the indirect array by subtracting the direct array from the total block count
		}
		else //if double indirect
		{
			if(dindirect == null) //create the double indirect array if necessary
				dindirect = new String[100][100];

			 /*index the double indirect array by subtracting the indirect and direct array from the total block count
			 then use that value/100 for the address in the first indirect array and that value%100 for the address in the second*/
			dindirect[(datacount-112)/100][(datacount-112)%100] = data;
		}
		datacount++; //increment the count of blocks stored
		return true; //data addition was successful
	}

	//store the inode data on the hard disk
	public void store() throws IOException
	{
		File f, //f used for every block file
			sf = new File(location + "/super_block.txt"); //sf used for super_block.txt
		if(!sf.exists()) //create sf if it doesn't exist
			sf.createNewFile();
		FileWriter fw, //fw used to write block files
			sfw = new FileWriter(sf); //sfw used to write super_block.txt

		//index the direct array and save the contents to files
		for(int i = 0; i < direct.length; i++)
		{
			//create block file and write the block data to it
			f = new File(location + "/block" + i + ".txt");
			if(!f.exists())
				f.createNewFile();
			fw = new FileWriter(f);
			fw.write(direct[i]);

			//write the block file name to super_block.txt
			sfw.write(f.getName()+"\n");

			fw.close();
		}

		int offset = direct.length; //offset the new array being indexed by the length of the previous one
		
		for(int i = offset; i < indirect.length + offset;i++)
		{
			//create block file and write the block data to it
			f = new File(location + "/block" + i + ".txt");
			if(!f.exists())
				f.createNewFile();
			fw = new FileWriter(f);
			fw.write(indirect[i-offset]);

			//write the block file name to super_block.txt
			sfw.write(f.getName()+"\n");

			fw.close();
		}

		offset += indirect.length; //offset the new array being indexed by the length of the previous one
		
		for(int i = offset; i < dindirect.length + offset;i++)
		{
			//create block file and write the block data to it
			f = new File(location + "/block" + i + ".txt");
			if(!f.exists())
				f.createNewFile();
			fw = new FileWriter(f);
			fw.write(dindirect[(i-offset)/100][(i-offset)%100]);

			//write the block file name to super_block.txt
			sfw.write(f.getName()+"\n");

			fw.close();
		}
		sfw.close();
	}

	//uses the internal arrays of block data to return the value at blocknum or null if blocknum is outside the arrays
	public String read(int blocknum)
	{
		if(blocknum < 12 && blocknum >= 0)
			return direct[blocknum];
		else if(blocknum < 112)
			return indirect[blocknum-12];
		else if(blocknum < 10112)
			return dindirect[(blocknum-112)/100][(blocknum-112)%100];
		return null;
	}

	//write new data to a block and update the arrays - returns true if successful
	public boolean write(int blocknum,String towrite) throws IOException
	{
		File blocktowrite = null; //file of the block to be written

		//use blocknum to generate the correct filename
		if(blocknum < 10112 && blocknum >= 0)
		{
			blocktowrite = new File(location + "/block" + blocknum + ".txt");
		}

		//edit the appropriate array entry
		if(blocknum < 12 && blocknum >= 0)
		{
			direct[blocknum] = towrite;
		}
		else if(blocknum < 112)
		{
			indirect[blocknum-12] = towrite;
		}
		else if(blocknum < 10112)
		{
			dindirect[(blocknum-112)/100][(blocknum-112)%100] = towrite;
		}
		else
			return false;

		//edit the file and return true
		FileWriter fw = new FileWriter(blocktowrite);
		fw.write(towrite);
		fw.close();
		return true;
	}

}
