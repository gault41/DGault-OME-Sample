import java.io.*;
import java.util.ArrayList;

public class futoshiki
{	
	static ArrayList <int []> fixedplaces=new ArrayList <int []> ();
	static ArrayList <String []> equalities=new ArrayList <String []> ();
	
	public static void main(String [] args)
	{
		benchmark futoshiki=new benchmark();
		int gridsize=0;
		
		// error handling
		if(args.length<3)
		{
			System.out.println("Error: Incorrect number of parameters provided");
			System.exit(0);
		}
		
		try{
			gridsize=Integer.parseInt(args[1]);
		}catch(Exception e)
		{
			System.out.println("Error: Parameter 2 must be of Integer format");
			System.exit(0);
		}
		
		// read in the user provided input to populate fixedplaces and equalities ArrayLists
		readInput(args[2]);
		
		// each square must be a number between 1 and gridsize, but cannot be two numbers
		for (int d=0;d<(gridsize*gridsize);d++)
		{
			int [] vars=new int [gridsize];
			boolean [] bools=new boolean [gridsize];
			
			for (int e=1;e<=(gridsize);e++)
			{
				vars[e-1]=(d*gridsize)+e;
				bools[e-1]=true;
			}
			futoshiki.addclause(vars,bools);
			
			for(int a=1;a<(gridsize);a++)
			{
				for(int b=a+1;b<=gridsize;b++)
				{
					futoshiki.addclause(new int [] {a+(gridsize*d),b+(gridsize*d)},new boolean [] {false,false});
				}
			}
		}
		
		for(int x=0;x<gridsize;x++)
		{

			for (int z=1;z<=gridsize;z++)
			{
				int [] vars=new int [gridsize];
				int [] vars2=new int[gridsize];
				boolean [] bools=new boolean [gridsize];
				boolean [] bools2=new boolean [gridsize];
				
				// each row or column must contain numbers in the range 1 upto gridsize
				for (int y=0;y<gridsize;y++)
				{
					vars[y]=(y*gridsize)+z+(gridsize*gridsize*(x));
					bools[y]=true;
					vars2[y]=z+(x*gridsize)+(gridsize*gridsize*y);
					bools2[y]=true;
				}
			
				futoshiki.addclause(vars,bools);
				futoshiki.addclause(vars2,bools2);
				
				// no number can appear more than once in a row or column
				for(int a=1;a<(gridsize);a++)
				{
					for(int b=a+1;b<=gridsize;b++)
					{
						futoshiki.addclause(new int [] {(x*gridsize*gridsize)+z+(gridsize*(a-1)),(x*gridsize*gridsize)+z+(gridsize*(b-1))},new boolean [] {false,false});
						futoshiki.addclause(new int [] {z+(x*gridsize)+(gridsize*gridsize*(a-1)),z+(x*gridsize)+(gridsize*gridsize*(b-1))},new boolean [] {false,false});
					}
				}
			}
		}
		
		// clauses for fixed place numbers
		for (int z=0;z<fixedplaces.size();z++)
		{
			futoshiki.addclause(new int [] {((fixedplaces.get(z)[0]-1)*(gridsize))+fixedplaces.get(z)[1]},new boolean [] {true});
		}
		
		// clauses for equalities
		for (int j=0;j<equalities.size();j++)
		{
			int greater;
			int lesser;
			
			if (equalities.get(j)[1].equals(">"))
			{
				greater=Integer.parseInt(equalities.get(j)[0]);
				lesser=Integer.parseInt(equalities.get(j)[2]);
			}
			else
			{
				greater=Integer.parseInt(equalities.get(j)[2]);
				lesser=Integer.parseInt(equalities.get(j)[0]);
			}
			// lesser square cannot be largest number and greater square cannot be smallest number
			futoshiki.addclause(new int [] {1+(gridsize*(greater-1))},new boolean [] {false});
			futoshiki.addclause(new int [] {(gridsize*lesser)},new boolean [] {false});
			
			// only combinations were the lesser square remains smaller than the greater square are allowed
			for(int a=1;a<(gridsize);a++)
			{
				int [] vars=new int [gridsize-(a-1)];
				boolean [] bools=new boolean [gridsize-(a-1)];
				vars[0]=(gridsize*greater)-(a-1);
				bools[0]=false;
				int numvars=1;
				for(int b=a+1;b<=gridsize;b++)
				{

					vars[numvars]=(gridsize*(lesser))-(b-1);
					bools[numvars]=true;
					numvars++;
				}
				futoshiki.addclause(vars,bools);
			}
		}
		
		futoshiki.createcnf(args[0]);
	}
	
	private static void readInput(String filename)
	{
		textreader read=new textreader();
		String dir=read.returnInputDir();
		try
    	{
    		// locate the input file and parse the user submitted input to populate fixedplaces and equalities  
	    	File file=new File(dir+filename+".txt");
			FileInputStream in = new FileInputStream(file);
			BufferedInputStream inbuffer=new BufferedInputStream(in);
			BufferedReader reader=new BufferedReader(new InputStreamReader(inbuffer));
			String currentln=reader.readLine();
			currentln=reader.readLine();
			currentln=reader.readLine();
			while(currentln.equals("")==false)
			{
				String [] splits = currentln.split("	");
				int [] thisrow=new int [splits.length];
				for(int j=0;j<thisrow.length;j++)
				{
					thisrow[j]=Integer.parseInt(splits[j].trim());
				}
				fixedplaces.add(thisrow);
				currentln=reader.readLine();
			}
			currentln=reader.readLine();
			currentln=reader.readLine();
			while(currentln!=null)
			{
				equalities.add(currentln.split(" "));
				currentln=reader.readLine();
			}
		}
		catch(Exception e)
		{
			System.err.println ("Error reading Futoshiki Input "+e);	
		}
	}
}
