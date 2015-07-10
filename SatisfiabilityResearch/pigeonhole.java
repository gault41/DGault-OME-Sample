public class pigeonhole
{	
	public static void main(String [] args)
	{
		benchmark pid=new benchmark();
		
		// error handling
		if(args.length<3)
		{
			System.out.println("Error: Incorrect number of Parameters");
			System.exit(0);
		}
		String output=args[0];
		int pigeons=0;
		int holes=0;
		try{
			pigeons=Integer.parseInt(args[1]);
			holes=Integer.parseInt(args[2]);
		}catch(Exception e)
		{
			System.out.println("Error: Parameters 2 and 3 must be Integers");
			System.exit(0);
		}
		// Each hole must not contain more than one pidgeon
		for (int x=0;x<holes;x++)
		{
			for (int z=0;z<pigeons-1;z++)
			{
				for(int w=z;w<pigeons-1;w++)
				{
					pid.addclause(new int [] {(x*pigeons+z+1),(x*pigeons+w+2)}, new boolean [] {false, false});
				}
			}	
		}
		
		// Each pidgeon has to be in one hole 
		for (int y=0;y<pigeons;y++)
		{
			int [] tempvars=new int [holes];
			boolean [] tempbools=new boolean [holes];
			for (int i=0;i<holes;i++)
			{
				tempvars[i]=(pigeons*i)+y+1;
				tempbools[i]=true;
			}
			pid.addclause(tempvars,tempbools);
		}
		
		// Each pidgeon can only be in one hole
		for (int x=0;x<pigeons;x++)
		{
			for (int z=0;z<holes-1;z++)
			{
				for(int w=z;w<holes-1;w++)
				{
					pid.addclause(new int [] {(z)*pigeons+x+1,((w+1)*pigeons+x+1)}, new boolean [] {false, false});
				}
			}	
		}	
		pid.createcnf(output);	
	}
}