import java.util.Random;

public class maxinstance
{	
	public static void main(String [] args)
	{
		int numvars=0;
		
		if(args.length<2)
		{
			System.out.println("Error: Incorrect number of parameters");
			System.exit(0);
		}
		try{
			numvars=Integer.parseInt(args[1]);
		}catch(Exception e)
		{
			System.out.println("Error: Parameter 2 must be an Integer");
			System.exit(0);
		}
		benchmark bench=new benchmark();
			
		//step 1 add extra var that must be true
		bench.addclause(new int [] {numvars+1}, new boolean [] {true});
		
		//step 2 for all possible combinations except 1 make new var false
		//random choice of correct ans = 2^numvars
		Random generator=new Random();
		int numposs=(int)Math.pow(2,numvars);
		int rand = generator.nextInt(numposs);
		
		int [] vars =new int [numvars+1];
		for (int i=0;i<numvars+1;i++)vars[i]=i+1;
		
		// loop through all combinations, only ignoring one, which will be the solution
		for(int x=1;x<=numposs;x++)
		{
			boolean [] bools=new boolean [numvars+1];
			bools[numvars]=false;
			for(int y=1;y<=numvars;y++)
			{
				int sign=((int)((x-1)/(numposs/((int)Math.pow(2,y)))))%2;
				if(sign==0)bools[y-1]=true;
				else bools[y-1]=false;
			}
			if(x!=rand)bench.addclause(vars,bools);
		}
		
		bench.createcnf(args[0]);	
	}
}