import java.io.*;

public class createbat
{
static String outputdirectory;
static FileOutputStream out; 
static PrintStream p; 
static String [] solvers;
static textreader read;

// constructor, initialises a textreader and the solvers and output directory to be used are stored
// used when creating bat file from a Java program 
public createbat()
{
	read=new textreader();
	solvers=read.returnSolvers();
	outputdirectory=read.returnOutputDir();
}
	
// main method run from command line
public static void main(String [] args)
{
	// error handling
	if(args.length<3)
	{
		System.out.println("Error: Wrong number of input parameters provided");
		System.exit(0);
	}else if(args[2].toLowerCase().equals("true")==false && args[2].toLowerCase().equals("false")==false)
	{
		System.out.println("Error: Parameter 3 must be of the form 'true' or 'false'");
		System.exit(0);
	}
	
	// initialise necessary variables such as directories, solvers etc
	read=new textreader();
	solvers=read.returnSolvers();
	outputdirectory=read.returnOutputDir();
	String cnffile;
	String title=args[0];
	
	// if user requires output then store to a given output file
	Boolean txtout=false;
	String outfile="";
	if(args[2].toLowerCase().equals("true"))
	{
		
		outfile=args[3];
		txtout=true;
	}
	
	// create a new bat file in output directory
	openbat(title);
	
	// if 'all' parameter used as opposed to a single cnf, then all cnf files in output directory are added to bat
	if(args[1].toLowerCase().equals("all"))
	{
		File dir=new File(outputdirectory);
		String [] children=dir.list();
		for(int i=0;i<children.length;i++)
		{
			if(children[i].indexOf(".cnf")>=0)addtobat(children[i].substring(0,children[i].length()-4), txtout, outfile);;
		}
	}
	// otherwise add the given cnf instance
	else{
		cnffile=args[1];	
		addtobat(cnffile, txtout, outfile);
	}
	
	// close the bat file
	closebat(txtout, outfile);
}

	public static void addtobat(String cnffile, Boolean outputtofile, String outputfile)
	{
        try
        {
        	// for each file added, loop for the number of solvers required
	        String fileloc=outputdirectory+cnffile;
	        for (int x=0;x<solvers.length;x++)
	        {
	        	// seperate code for given solvers, more can easily be added
	        	// code added to bat to browse to solver directory and run solver against given benchmark
	        	// if output is requried then it is ammended to a specified output file
				if(solvers[x].toLowerCase().equals("minisat"))
	        	{
	        		String minisat_loc=read.returnSolverLoc("minisat");
	        		p.println ("cd \""+minisat_loc+"\"");
	        		if(outputtofile)p.println ("minisat_static \""+fileloc+".cnf\" >>\""+outputdirectory+""+outputfile+"_minisat.txt\" 2>&1");
	        		else p.println ("minisat_static \""+fileloc+".cnf\"");
	        	}
				else if(solvers[x].toLowerCase().equals("rsat"))
				{
					String rsat_loc=read.returnSolverLoc("rsat");
					p.println ("cd \""+rsat_loc+"\"");
					if(outputtofile)p.println ("rsat \""+fileloc+".cnf\" >>\""+outputdirectory+""+outputfile+"_rsat.txt\" 2>&1");
					else p.println ("rsat \""+fileloc+".cnf\"");
				}
				else if(solvers[x].toLowerCase().equals("tts"))
				{
					String tts_loc=read.returnSolverLoc("tts");
					p.println ("cd \""+tts_loc+"\"");
					if(outputtofile)p.println ("tts-4-2 \""+fileloc+".cnf\" >>\""+outputdirectory+""+outputfile+"_tts.txt\" 2>&1");
					else p.println ("tts-4-2 \""+fileloc+".cnf\" \""+fileloc+""+x+".txt\"");
				}
				else if(solvers[x].toLowerCase().equals("marchks")||solvers[x].toLowerCase().equals("march_ks"))
				{
					String marchks_loc=read.returnSolverLoc("Marchks");
					p.println ("cd \""+marchks_loc+"\"");
					if(outputtofile)p.println ("march_ks \""+fileloc+".cnf\" >>\""+outputdirectory+outputfile+"_marchks.txt\" 2>&1");
					else p.println ("march_ks \""+fileloc+".cnf\"");
				}
			}
	    }
	    catch (Exception e)
	    {
	    	System.err.println ("Error adding to file");
	    }
	}
	
	public static void openbat(String title)
	{
		// creates a new bat file in teh output directory and initialises a printstream
		try
        {
			out = new FileOutputStream(outputdirectory+title+".bat");
	        p = new PrintStream(out);
	    }
	    catch (Exception e)
	    {
	    	System.err.println ("Error creating batch file");
	    }
	}
	
	public static void closebat(Boolean outputtofile, String outputfile)
	{
		try
		{	
			if(outputtofile)
			{
				// if output require then commands added for each solver to parse the output files into meaningful data
				// GnuPlot Config file generated, and run against GnuPlot to produce graph
				File currentdir=new File(".");
				p.println("cd \""+currentdir.getCanonicalPath()+"\"");
				for (int x=0;x<solvers.length;x++)
		        {
					if(solvers[x].toLowerCase().equals("minisat"))
		        	{
		        		p.println("Java textparser "+outputfile+"_minisat.txt time "+outputfile+"_minisat_time.txt");
		        		p.println("Java textparser "+outputfile+"_minisat.txt sat "+outputfile+"_minisat_sat.txt");
		        	}
					else if(solvers[x].toLowerCase().equals("rsat"))
					{
						p.println("Java textparser "+outputfile+"_rsat.txt time "+outputfile+"_rsat_time.txt");
		        		p.println("Java textparser "+outputfile+"_rsat.txt sat "+outputfile+"_rsat_sat.txt");
					}
					else if(solvers[x].toLowerCase().equals("tts"))
					{
						p.println("Java textparser "+outputfile+"_tts.txt time "+outputfile+"_tts_time.txt");
		        		p.println("Java textparser "+outputfile+"_tts.txt sat "+outputfile+"_tts_sat.txt");
					}
					else if(solvers[x].toLowerCase().equals("marchks")||solvers[x].toLowerCase().equals("march_ks"))
					{
		     			p.println("Java textparser "+outputfile+"_marchks.txt time "+outputfile+"_marchks_time.txt");
		        		p.println("Java textparser "+outputfile+"_marchks.txt sat "+outputfile+"_marchks_sat.txt");
					}
				}
				createGnuConfig(outputfile);
				p.println("cd \""+read.returnGnuPlotLoc()+"\"");
				p.println("wgnuplot.exe \""+outputdirectory+outputfile+".cfg\"");
			}
			
			// close the bat file
			p.println ("pause");
	    	p.close();
	    }
	    catch (Exception e)
	    {
	    	System.err.println ("Error writing to file");
	    }
	}
	
	private static void createGnuConfig(String name)
	{
		try{
			// create GnuPlot Config 
			FileOutputStream gnuconfig = new FileOutputStream(outputdirectory+name+".cfg");
	        PrintStream gnu = new PrintStream(gnuconfig);
	        gnu.println("set term png"); 
			gnu.println("set autoscale");                     
			gnu.println("set xtic auto");                        
			gnu.println("set ytic auto");                         
			gnu.println("set title \"Satisfiability results - "+name+"\"");
			gnu.println("set xlabel \"Instance\"");
			gnu.println("set ylabel \"Time (secs)\"");
			gnu.println("set output '"+outputdirectory+name+".png'");
			
			// plot data from parsed output time results of each solver
			if (solvers.length>1)
			{
				gnu.println("plot '"+outputdirectory+name+"_"+solvers[0].toLowerCase()+"_time.txt' title '"+solvers[0].toLowerCase()+"' with line, \\");
				for(int y=1;y<solvers.length-1;y++)
				{
					gnu.println("'"+outputdirectory+name+"_"+solvers[y].toLowerCase()+"_time.txt' title '"+solvers[y].toLowerCase()+"' with line, \\");
				}
				gnu.println("'"+outputdirectory+name+"_"+solvers[solvers.length-1].toLowerCase()+"_time.txt' title '"+solvers[solvers.length-1].toLowerCase()+"' with line");
			}
			else gnu.println("plot '"+outputdirectory+name+"_"+solvers[0].toLowerCase()+"_time.txt' title '"+solvers[0].toLowerCase()+"' with line");
	        gnu.close();
		}catch(Exception e)
		{
			System.out.println("Error creating GnuPlot config file "+e);
		}
	}

}