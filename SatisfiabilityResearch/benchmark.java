import java.io.*;
import java.util.ArrayList;

public class benchmark 
{
	int numvars;
    int numclauses;
	ArrayList <clause> clauses;
	
    // constructor - initialise the number of clauses and variables to 0 and declare an empty ArrayList of clauses
    public benchmark() 
    {
    	numclauses=0;
		numvars=0;
		clauses=new ArrayList <clause> ();
    }
    
    public void createcnf(String name)
    {
		// scale the variables in the clause to remove unused variables
		scalevariables();
    	
    	// create a new CNF file
    	FileOutputStream cnfbenchmark;
    	textreader getdirs=new textreader();
    	String outputdir=getdirs.returnOutputDir();
		
        PrintStream print; 
        try
        {
        	cnfbenchmark = new FileOutputStream(outputdir+name+".cnf");
            print = new PrintStream(cnfbenchmark);
            print.println("c CNF created by benchmark framework");
			print.println("p cnf "+numvars+" "+numclauses);
            for (int i=0; i<numclauses; i++)
            {
            	print.println (clauses.get(i).getclause());
            }	
        	print.close();
        }
        catch (Exception e)
		{
			System.err.println ("Error writing to file "+e);
		}
    } 
    
    // creates a new clause from the provided input parameters and adds the new clause to the benchmarks clause ArrayList 
    public void addclause(int [] names, boolean [] values)
    {
    	for (int i=0;i<names.length;i++)
    	{
    		if (names[i]>numvars)numvars=names[i];
    	}
    	clauses.add(new clause(names, values));
    	numclauses++;
    }
    
    //remove any unused variables and scale other variables appropriately to compensate
    private void scalevariables()
    {
    	int [] vals=new int [numvars+1];
    	for (int x=0;x<numvars;x++)
    	{
    		vals[x]=0;
    	}
    	for (int y=0;y<numclauses;y++)
    	{
    		int [] tempvals=new int [clauses.get(y).getvalues().length];
    		tempvals=clauses.get(y).getvalues();
    		for (int z=0;z<tempvals.length;z++)
    		{
    			vals[tempvals[z]]=tempvals[z];
    		}
    	}
    	int nextvar=1;
    	for (int x=0;x<numvars;x++)
    	{
    		if(vals[x]>0)
    		{
    			vals[x]=nextvar;
    			nextvar++;
    		}
    	}
    	numvars=nextvar;
    	for (int y=0;y<numclauses;y++)
    	{
			int [] tempvals=clauses.get(y).getvalues();
			for (int z=0;z<tempvals.length;z++)
			{
				tempvals[z]=vals[tempvals[z]];
			}
			clauses.get(y).updatevalues(tempvals);
    	}
    }
      
    // convert the existing benchmark to make it complient with the rules of linear formula
    public void makelinear()
    {
    	ArrayList [] allvars=new ArrayList [numvars+1];	
 	
    	// for each clause check that they follow the linear rules
    	for(int x=0;x<numclauses;x++)
    	{
    		int [] vars=clauses.get(x).getvalues();
    		int [] newvars=vars;
    		int origvars=numvars;
    		for(int y=0;y<vars.length-1;y++)
    		{
    			for(int z=y+1;z<vars.length;z++)
    			{
    				// compare all pairs of variables in each clause
    				
    				if (vars[z]<=origvars && vars[y]<=origvars)
    				{
    					if(allvars[vars[y]]==null)allvars[vars[y]]= new ArrayList();
						
						// if 2 variables exist together in a previous clause
						if(allvars[vars[y]].contains(vars[z]))
						{
							 	//add new variable
							 	numvars++;
							 	newvars[z]=numvars;
						}
						else
						{
							// make note for each variable that they have occured together
 							allvars[vars[y]].add(vars[z]);
 							if(allvars[vars[z]]==null)allvars[vars[z]]= new ArrayList();
 							allvars[vars[z]].add(vars[y]);
						}
    				}
    			}
    			// update the clause with new modifications
    			clauses.get(x).updatevalues(newvars);
    		}
    	}
    }
}