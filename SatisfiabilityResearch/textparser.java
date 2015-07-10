import java.io.*;

public class textparser
{ 	
	static FileOutputStream out; 
    static PrintStream p;
	static FileInputStream in;
	static InputStreamReader instream;
	static String outputdirectory;
	static String title;
	static String filename;
	
	public static void main(String [] args)
	{
		try
		{
			// set input directory, file and initialise reader
			textreader read=new textreader();
			outputdirectory=read.returnOutputDir();
			File file=new File(outputdirectory+args[0]);
			in = new FileInputStream(file);
			BufferedInputStream inbuffer=new BufferedInputStream(in);
			BufferedReader reader=new BufferedReader(new InputStreamReader(inbuffer));
			String currentln=reader.readLine();
			
			// set output directory and create output file
			out = new FileOutputStream(outputdirectory+args[2]);
	        p = new PrintStream(out);
			
			// parse through the entire input file
			while(currentln!=null)
			{
				if(args[1].toLowerCase().equals("sat"))
				{
					// If generating sat results, look for lines with the results and then parse out only the parts of interest
					if(currentln.toLowerCase().indexOf("satisfiable")>=0)
					{
						String [] splits=currentln.split(" ");
						p.println(splits[splits.length-1].toLowerCase());
					}
				}
				else if (args[1].toLowerCase().equals("time"))
				{
					// if generating time results, find the times and isolate only the numbers before adding each to output file
					if(currentln.toLowerCase().indexOf("time")>=0 && currentln.toLowerCase().indexOf("parsing time")<0)
					{
						String [] splits=currentln.split(" ");
						if(splits.length>3)p.println(splits[splits.length-2]);
						else p.println(splits[splits.length-1].split("=")[1]);
					}		
				}
				else if (args[1].toLowerCase().equals("todos"))
				{
					// if converting to dos, simply reprint the file to generate the correct carriage returns
					p.println(currentln);
				}
				currentln=reader.readLine();
			}
			p.close();
		}
		catch (Exception e)
	    {
	    	System.err.println ("Error parsing results"+e);
	    }
	}
}