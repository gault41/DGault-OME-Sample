public class runtests
{
	// example method for running instances and creating bat file using Java
	
	public static void main(String [] args)
	{
		// generate 100 pigeonhole benchmark
		for(int i=0;i<100;i++)
		{
			pigeonhole.main(new String [] {"pigeonhole"+i, ""+i, " "+((i/10)*10+10)});
		}	

		// generate a single pictocross benchmark
		pictocross.main(new String [] {"pictotest", "PictocrossInput", "7", "7"});
		
		// create and open a new bat file
		createbat bat=new createbat();
		bat.openbat("examplebat");
		
		// add the single pictocross benchmark to the bat specifying that output should be saved
		bat.addtobat("pictotest",true,"exampleoutput");
		
		// add all 100 pigeonhole benchmarks to the bat, also saving their output
		for(int i=0;i<100;i++)
		{
			bat.addtobat("pigeonhole"+i,true,"exampleoutput");
		}
		
		// close the bat file specifying that results file should be parsed and graph generated 
		bat.closebat(true,"exampleoutput");
	}
}