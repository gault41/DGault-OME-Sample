import java.util.Random;

public class linearformula
{
	public static void main(String [] args)
	{
		// error handling
		if(args.length<5)
		{
			System.out.println("Error: Incorrect number of parameters provided");
			System.exit(0);
		}
		if(args[1].toLowerCase().equals("random")==false && args[1].toLowerCase().equals("fixed")==false)
		{
			System.out.println("Error: Parameter 2 must be either 'random' or 'fixed'");
			System.exit(0);
		}
		int litperclause=0;
		int numvars=0;
		try{
			litperclause=Integer.parseInt(args[3]);
			numvars=Integer.parseInt(args[2]);
			int test=Integer.parseInt(args[4]);
		}catch(Exception e)
		{
			System.out.println("Error: Parameters 3, 4 and 5 must be Integers");
			System.exit(0);
		}
		
		Boolean random=args[1].toLowerCase().equals("random");
		benchmark linear=new benchmark();
		Random generator=new Random(new Long(Integer.parseInt(args[4])));
		
		// *********** Random ***************
		if(random)
		{
			int [] varused=new int [numvars+1];
			int numvarsused=0;
			// loop until the correct number of variables used
			while(numvarsused<numvars)
			{
				int [] vars=new int [litperclause];
				boolean [] bools=new boolean [litperclause];
				
				// allocate the correct number of literals per clause
				for(int x=0;x<litperclause;x++)
				{
					boolean litfound=false;
					int nextvar=0;
					
					// while not linear complient then loop
					while(litfound==false)
					{
						nextvar=generator.nextInt(numvars)+1;
						if(varused[nextvar]<2)
						{
							varused[nextvar]++;
							if(varused[nextvar]==1)numvarsused++;
							litfound=true;
						}
					}
					vars[x]=nextvar;
					bools[x]=(generator.nextInt(2)==1);
				}
				linear.addclause(vars,bools);
			}
		}
		// *********** Fixed ***************
		if(!random)
		{
			int [] varused=new int [numvars+1];
			int numvarsused=0;
			int nextvar=1;
			
			// loop until correct number of variables used
			while(numvarsused<numvars)
			{
				int [] vars=new int [litperclause];
				boolean [] bools=new boolean [litperclause];
				// ensure the correct number of literals per clause
				for(int x=0;x<litperclause;x++)
				{
					vars[x]=nextvar;
					bools[x]=(generator.nextInt(2)==1);
					if(x<litperclause-1)
					{
						nextvar++;
						numvarsused++;	
					}
					if(nextvar>numvars)nextvar=1;
				}
				linear.addclause(vars,bools);
			}
		}
		linear.createcnf(args[0]);
	}
}
	
