import java.util.Random;

public class sgen
{
static int g, n, groups;
static boolean sat, trythis, useRandom;
static int [] permute;
static int [] unpermute;
static Long seed;
static Random generator;
static benchmark sg;	
static boolean sign;

public static void main(String [] args)
{
	sg= new benchmark();
	int argc=args.length;
	
	if (argc != 4)
	{
		System.out.println("Error: Incorrect number of parameters");
		System.exit(0);
	}
	if(args[3].toLowerCase().equals("sat")==false && args[3].toLowerCase().equals("unsat")==false)
	{
		System.out.println("Error: Parameter 4 must be either 'sat' or 'unsat'");
		System.exit(0);
	}
	try{
		n = Integer.parseInt(args[1]);
		seed=new Long(Integer.parseInt(args[2]));
	}catch(Exception e)
	{
		System.out.println("Error: Parameters 2 and 3 must be Integers");
		System.exit(0);
	}
	
	generator=new Random(seed);
	sat = args[argc-1].equals("sat") == true;
	useRandom = true;
	n = 4*((n-1)/4);
	if (!sat)n = n+1;
	groups = n/4;
	
	if (sat)
	{
		setPlainPermute();
		sign=false;
		for (g=0;g<groups;g++)otherGroup (g);
		sign=true;
		if (useRandom)setAnnealingPermute(1);
		for (g=0;g<groups;g++)otherGroup (g);
	}
	else
    {
    	setPlainPermute();
		sign=false;
		for (g=0;g<groups-1;g++)otherGroup(g);
		lastGroup();
		sign=true;
		if (useRandom)setAnnealingPermute(1);
		for (g=0;g<groups-1;g++)otherGroup (g);
		lastGroup();
	}
	sg.createcnf(args[0]);
}

static void setPlainPermute ()
{
	permute=new int [n+1];
	for (int i=1;i<=n;i++)permute[i] = i;
}

static boolean beatProbability (double power)
{
	boolean result;
	double p;
	
	if (power > 0.0)
		result = true;
	else
	{
        p = Math.exp(power);
        if (p >= 1)
        	result = true;
    	else
    		result = generator.nextInt(Integer.MAX_VALUE) < p*Integer.MAX_VALUE;
    }
	return result;
}

static int groupFor (int p1)
{
	if (!sat && p1 == n)
		return (p1-2) / 4;
	else
        return (p1-1)/4;
}

static int neighbours (int p1, int pp1)
{
	int result = 0;
	int i,j, k, g1, gp1, p2, g2, p3, pp3,g3;
  	g1 = groupFor (p1);
 	gp1 = groupFor (pp1);
	
	for (i=1;i<=4;i++)
	{
 		p2 = unpermute[4*gp1+i];
 		g2 = groupFor (p2);
 		if (pp1 != 4*gp1+i &&  (g2 == g1))
		{
			result+=16;
  		}

		for (j=1; j<=4; j++)
  		{
    		p3 = 4*g1+j;
			if (p1 != p3)
 			{
 				pp3 = permute[p3];
				g3 = groupFor(pp3);
   				for (k=1;k<=4;k++)
					if (4*g3+k != pp3 &&groupFor(unpermute[4*g3+k]) == g2)result += 1;
         	}
  		}
	}

	if (!sat && g1 == (n-1)/4 && p1 != n)
		if (groupFor (unpermute[n]) == g1)result+=16;
	return result;
}

static double swapGain (int p1, int p2)
{
	return (double) (neighbours (p1, permute[p1]) + neighbours (p2,permute[p2])) -(neighbours (p1, permute[p2]) + neighbours (p2, permute[p1]));
}

static void setAnnealingPermute (int sign)
{
	int i,j, p1, p2, t;
	double temperature;
	double gain;
	unpermute=new int [n+1];
	for (i=1;i<=n;i++)
	{
		permute[i] = i;
		unpermute[i] = i;
	}

	temperature = 20;

	while (temperature > 0.2)
	{
		for (i=1;i<=2*n;i++)
		{
			p1 = (int)(1 + generator.nextInt(Integer.MAX_VALUE) % n);
			p2 = (int)(1 + generator.nextInt(Integer.MAX_VALUE) % n);
			gain = swapGain (p1, p2);
			if (beatProbability (gain/temperature))
			{
				t = permute[p1];
				permute[p1] = permute[p2];
				permute[p2] = t;
				unpermute[permute[p1]] = p1;
				unpermute[permute[p2]] = p2;
			}
		}
		temperature *= 0.995;
    }
	for (i=1;i<=n;i++)
	{
		permute[i] *= sign;
	}
}

static void lastGroup()
{
	int i,j,k;
    int start = n-5;

    for (i=1;i<=3;i++)
    	for (j=i+1;j<=4;j++)
        	for (k=j+1;k<=5;k++)
            	sg.addclause(new int [] {permute[i+start], permute[j+start], permute[k+start]},new boolean [] {sign,sign,sign});
}

static void otherGroup (int g)
{
	int i,j;
	for (i=0;i<4;i++)
    {
    	int [] vars=new int [3];
        boolean [] bools=new boolean [3];
        int count=0;
    	for (j=0;j<4;j++)
        {
        	if (i != j)
        	{
        		vars[count]=permute[g*4+j+1];
        		bools[count]=sign;
        		count++;
        	}
        }
        sg.addclause(vars,bools);
    }
} 

}