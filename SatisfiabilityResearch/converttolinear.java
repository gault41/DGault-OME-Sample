import java.io.*;

public class converttolinear
{	
	static benchmark linear;

	public static void main(String [] args)
	{
		textreader read=new textreader();
		String inputdir=read.returnInputDir();
		linear=new benchmark();
		
    	// regenerate existing benchmark from file
    	try
    	{
	    	File file=new File(inputdir+args[0]+".cnf");
			FileInputStream in = new FileInputStream(file);
			BufferedInputStream inbuffer=new BufferedInputStream(in);
			BufferedReader reader=new BufferedReader(new InputStreamReader(inbuffer));
			String currentln=reader.readLine();
	    	while((currentln!=null))
			{
				if(currentln.indexOf("c")==-1 && currentln.indexOf("p")==-1)
				{
					String [] splits = currentln.split(" ");
					int [] vars=new int[splits.length-1];
					boolean [] bools=new boolean[splits.length-1];
					for(int i=0;i<splits.length-1;i++)
					{
						if(Integer.parseInt(splits[i])<0)
						{
							bools[i]=false;
							vars[i]=Integer.parseInt(splits[i])*-1;
						}
						else if(Integer.parseInt(splits[i])>0)
						{
							bools[i]=true;
							vars[i]=Integer.parseInt(splits[i]);
						}
					}
					linear.addclause(vars,bools);
				}
				currentln=reader.readLine();
			}
		}
		catch(Exception e)
		{
			System.out.println("Error reading from existing file: "+e);
		}
		
		// make the newly created benchmark linear and generate a CNF in the output directory
		linear.makelinear();
		linear.createcnf(args[0]+"_linear");
	}
}