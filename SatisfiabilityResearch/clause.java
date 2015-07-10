public class clause 
{
	int [] varnames;
	boolean [] varvalues;
		
    // constructor - initialises the variables and their respective values for each literal in teh clause
    public clause(int [] names, boolean [] values) 
    {
    	varnames=names;
    	varvalues=values;
    } 
    
    // returns the stored data as a string in a format ready to be immediately added tto a CNF file
    public String getclause()
    {
    	String output="";
    	for (int i=0; i<varnames.length; i++)
    	{
    		if (varvalues[i])
    			output+=varnames[i]+" ";
    		else
    			output+="-"+varnames[i]+" ";
    	}

    	output+="0";
    	return output;	
    }
    
    // returns the variables in teh clause to the calling method
    public int [] getvalues()
    {
    	return varnames;
    }
    
    // replaces the existing variables with the variables provided in the input parameter
    public void updatevalues(int [] updated)
    {
    	varnames=updated;
    }
}