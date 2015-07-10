import java.io.*;

public class textreader
{
	static FileOutputStream out; 
    static PrintStream p;
	static FileInputStream in;
	static InputStreamReader instream;
	static String outputdirectory;
	static String title;
	static String filename;
	
	// constructor as called from other Java methods, initialises the output directory
	public textreader()
	{
		outputdirectory=returnOutputDir();
	}
		
	public static String returnOutputDir()
    {
    	String outputdir="";
    	try
    	{
    		// locate the options file and parse out the selected output directory
	    	File file=new File("options.txt");
			FileInputStream in = new FileInputStream(file);
			BufferedInputStream inbuffer=new BufferedInputStream(in);
			BufferedReader reader=new BufferedReader(new InputStreamReader(inbuffer));
			String currentln=reader.readLine();
			currentln=reader.readLine();
			String [] splits = currentln.split(":");
			outputdir=splits[1]+":"+splits[2];
			outputdir+="\\";
			outputdir=outputdir.trim();
		}
		catch(Exception e)
		{
			System.err.println ("Error reading options file "+e);	
		}
		return outputdir;
    }
    
    public static String returnInputDir()
    {
    	String inputdir="";
    	try
    	{
    		// locate the options file and parse out the selected input directory
	    	File file=new File("options.txt");
			FileInputStream in = new FileInputStream(file);
			BufferedInputStream inbuffer=new BufferedInputStream(in);
			BufferedReader reader=new BufferedReader(new InputStreamReader(inbuffer));
			String currentln=reader.readLine();
			String [] splits = currentln.split(":");
			inputdir=splits[1]+":"+splits[2];
			inputdir+="\\";
			inputdir=inputdir.trim();
		}
		catch(Exception e)
		{
			System.err.println ("Error reading options file "+e);	
		}
		return inputdir;
    }
    
    public static String [] returnSolvers()
    {
    	String [] solvers;
    	try
    	{
    		// locate the options file and parse out each selected solver, adding each to a String [] which is returned to calling method
	    	File file=new File("options.txt");
			FileInputStream in = new FileInputStream(file);
			BufferedInputStream inbuffer=new BufferedInputStream(in);
			BufferedReader reader=new BufferedReader(new InputStreamReader(inbuffer));
			String currentln=reader.readLine();
			while(currentln.indexOf("Solvers:")==-1)
			{
				currentln=reader.readLine();
			}
			String x = currentln.split(":")[1].trim();
			String [] splits=x.split("	");
			solvers=new String [splits.length];
			for(int i=0; i<splits.length;i++)
			{
				solvers[i]=splits[i];
			}
		}
		catch(Exception e)
		{
			System.err.println ("Error reading solvers from options file "+e);
			solvers=null;	
		}
		return solvers;
    }
    
    public static String returnSolverLoc(String thissolver)
    {
    	String solverloc="";
    	try
    	{
    		// searches options file for the entry thissolver directory, when found the directory is parsed out and returned to calling method
	    	File file=new File("options.txt");
			FileInputStream in = new FileInputStream(file);
			BufferedInputStream inbuffer=new BufferedInputStream(in);
			BufferedReader reader=new BufferedReader(new InputStreamReader(inbuffer));
			String currentln=reader.readLine();
			while(currentln.indexOf(thissolver)==-1)
			{
				currentln=reader.readLine();
			}
			String [] splits = currentln.split(":");
			solverloc+=splits[1]+":"+splits[2];
			solverloc=solverloc.trim();
		}
		catch(Exception e)
		{
			System.err.println ("Error reading solver loc "+thissolver+" from options file "+e);	
		}
		return solverloc;
    }
    
    public static String returnGnuPlotLoc()
    {
    	String gnudir="";
    	try
    	{
    		// locate the options file and parse out the selected GnuPlot directory
	    	File file=new File("options.txt");
			FileInputStream in = new FileInputStream(file);
			BufferedInputStream inbuffer=new BufferedInputStream(in);
			BufferedReader reader=new BufferedReader(new InputStreamReader(inbuffer));
			String currentln=reader.readLine();
			while(currentln.indexOf("GnuPlot")<0)currentln=reader.readLine();
			String [] splits = currentln.split(":");
			gnudir=splits[1]+":"+splits[2];
			gnudir+="\\";
			gnudir=gnudir.trim();
		}
		catch(Exception e)
		{
			System.err.println ("Error reading options file "+e);	
		}
		return gnudir;
    }
}
