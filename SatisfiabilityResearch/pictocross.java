import java.io.*;

public class pictocross
{	
	static int numrows=0;
	static int numcols=0;
	static int[][] rows; 
	static int[][] cols;
	static benchmark picto=new benchmark();
	
	public static void main(String [] args)
	{
		// error handling
		if(args.length<4)
		{
			System.out.println("Error: Incorrect number of parameters");
			System.exit(0);
		}
		String output=args[0];
		String inputfile=args[1];
		try{
			numrows=Integer.parseInt(args[2]);
			numcols=Integer.parseInt(args[3]);
		}catch(Exception e)
		{
			System.out.println("Parameters 3 and 4 must be Integers");
			System.exit(0);
		}
		
		
		// declare new int [][] for each row and column, each is populated from 
		// the data provided in the inputfile as part fo readInput
		// each array represents the numbers of each unbroken sequences for each row & column
		rows=new int [numrows][];
		cols=new int [numcols][];
		readInput(inputfile);
	
		// num of variables is represented as each square potentially being either a start, end or 
		// middle of a sequence for every possible sequence in that row & column. Results in large numbers of variables
		int numvars=1;
		int colvars=0;
	
		int maxsize=0;
		for(int i=0;i<rows.length;i++)
		{
			if(rows[i].length>maxsize)maxsize=rows[i].length*2;
		}
		numvars+=maxsize;
		maxsize=0;
		for(int i=0;i<cols.length;i++)
		{
			if(cols[i].length>maxsize)maxsize=cols[i].length*2;
		}
		numvars+=maxsize;
		colvars=maxsize;		
		
		// must be correct number of sequences per row and column, only 1 square can be a start for a seq
		for(int i=0;i<rows.length;i++)
		{
			for(int j=0;j<rows[i].length;j++)
			{
				// if there is a sequence in this row
				if(rows[i][j]>0)
				{
					int [] startseq=new int [cols.length];
					int [] endseq=new int [cols.length];
					boolean [] boolvals=new boolean [cols.length];
					for(int k=0;k<cols.length;k++)
					{
						startseq[k]=(i*cols.length*numvars)+(k*numvars)+(2+(j*2))+(colvars);
						endseq[k]=(i*cols.length*numvars)+(k*numvars)+(3+(j*2))+(colvars);
						boolvals[k]=true;
					}
					picto.addclause(endseq,boolvals);
					picto.addclause(startseq,boolvals);
					
					//each square can only be start and end of a single sequence
					for(int z=0;z<startseq.length-1;z++)
					{
						for(int w=z+1;w<startseq.length;w++)
						{
							picto.addclause(new int [] {startseq[z],startseq[w]},new boolean [] {false,false});
							picto.addclause(new int [] {endseq[z],endseq[w]},new boolean [] {false,false});
						}
					}
				}
				// if there is no sequence then all squares are part of no sequence
				else
				{
					for(int k=0;k<cols.length;k++)
					{
						picto.addclause(new int [] {(i*cols.length*numvars)+(k*numvars)+(2+(j*2))+(colvars)},new boolean [] {false});
						picto.addclause(new int [] {(i*cols.length*numvars)+(k*numvars)+(3+(j*2))+(colvars)},new boolean [] {false});
					}
				}
			}
		}
		// repeat above algorithm, but inverted for each row
		for(int i=0;i<cols.length;i++)
		{
			for(int j=0;j<cols[i].length;j++)
			{
				if(cols[i][j]>0)
				{
					int [] startseq=new int [rows.length];
					int [] endseq=new int [rows.length];
					boolean [] boolvals=new boolean [rows.length];
					for(int k=0;k<rows.length;k++)
					{
						startseq[k]=(k*cols.length*numvars)+(i*numvars)+((j*2)+2);
						endseq[k]=(k*cols.length*numvars)+(i*numvars)+((j*2)+3);
						boolvals[k]=true;
					}
					picto.addclause(endseq,boolvals);
					picto.addclause(startseq,boolvals);
					for(int z=0;z<startseq.length-1;z++)
					{
						for(int w=z+1;w<startseq.length;w++)
						{
							picto.addclause(new int [] {startseq[z],startseq[w]},new boolean [] {false,false});
							picto.addclause(new int [] {endseq[z],endseq[w]},new boolean [] {false,false});
						}
					}
				}
				else
				{
					for(int k=0;k<cols.length;k++)
					{
						picto.addclause(new int [] {(k*cols.length*numvars)+(i*numvars)+((j*2)+2)},new boolean [] {false});
						picto.addclause(new int [] {(k*cols.length*numvars)+(i*numvars)+((j*2)+3)+(colvars)},new boolean [] {false});
					}
				}
			}
		}
		
		// sequences must be in order (for >1 seqs, length==1 seqs taken care of by initial rules)
		for(int i=0;i<cols.length;i++)
		{
			//start of first seq cant be in last square unless length ==1
			if(cols[i].length>1)
			{
				picto.addclause(new int [] {((rows.length-1)*cols.length*numvars)+(numvars*i)+2},new boolean [] {false});
			}
			for(int j=0;j<cols[i].length-1;j++)
			{
				for(int k=0;k<rows.length-1;k++)
				{
					//start of next clause cant be on same square as end of this clause
					picto.addclause(new int [] {(k*cols.length*numvars)+(i*numvars)+((j+1)*2)+1,(k*cols.length*numvars)+(i*numvars)+((j+1)*2)+2},new boolean [] {false,false});
					//start of next clause cant be on next square after end of this clause
					picto.addclause(new int [] {(k*cols.length*numvars)+(i*numvars)+((j+1)*2)+1,(((k+1)*cols.length*numvars)+(i*numvars)+((j+1)*2)+2)},new boolean [] {false,false});
					//all squares before must not be start of next clause
					for(int m=0;m<k;m++)
					{
						picto.addclause(new int [] {(k*cols.length*numvars)+(i*numvars)+((j+1)*2)+1,(m*cols.length*numvars)+(i*numvars)+((j+1)*2)+2},new boolean [] {false,false});
					}
				}
			}
		}
		
		for(int i=0;i<rows.length;i++)
		{
			//start of first seq cant be in last square unless length==1
			if (rows[i].length>1)
			{
				picto.addclause(new int [] {((i+1)*cols.length*numvars)-numvars+2+colvars},new boolean [] {false});
			}
			for(int j=0;j<rows[i].length-1;j++)
			{
				for(int k=0;k<cols.length-1;k++)
				{
					picto.addclause(new int [] {(i*cols.length*numvars)+(k*numvars)+((j+1)*2)+1+(colvars),(i*cols.length*numvars)+(k*numvars)+((j+2)*2)+(colvars)},new boolean [] {false,false});
					picto.addclause(new int [] {(i*cols.length*numvars)+(k*numvars)+((j+1)*2)+1+(colvars),(i*cols.length*numvars)+((k+1)*numvars)+((j+2)*2)+(colvars)},new boolean [] {false,false});
					for(int m=0;m<k;m++)
					{
						picto.addclause(new int [] {(i*cols.length*numvars)+(k*numvars)+((j+1)*2)+1+(colvars),(i*cols.length*numvars)+(m*numvars)+((j+2)*2)+(colvars)},new boolean [] {false,false});
					}
				}
			}
		}
		
		// if its the start of a sequence, finish sequence correctly
		for (int x=0;x<cols.length;x++)
		{
			for (int y=0;y<cols[x].length;y++)
			{
				if(cols[x][y]==0)
				{
					//if length==0 then all squares in col are false
					for(int z=0;z<rows.length;z++)
					{
						picto.addclause(new int [] {(z*cols.length*numvars)+(x*numvars)+1},new boolean [] {false});
					}
				}
				else if(cols[x][y]==1)
				{
					for(int z=0;z<rows.length;z++)
					{
						//start square is selected
						picto.addclause(new int [] {(z*cols.length*numvars)+(x*numvars)+2+(y*2),(z*cols.length*numvars)+(x*numvars)+1},new boolean [] {false,true});
						//start square is also end square
						picto.addclause(new int [] {(z*cols.length*numvars)+(x*numvars)+2+(y*2),(z*cols.length*numvars)+(x*numvars)+3+(y*2)},new boolean [] {false,true});
						//next one blank if possible
						if(z+1<rows.length)
						{
							picto.addclause(new int [] {(z*cols.length*numvars)+(x*numvars)+2+(y*2),((z+1)*cols.length*numvars)+(x*numvars)+1},new boolean [] {false,false});
						}
					}
				}
				else
				{
					//cant start too close to end
					for(int w=rows.length+1-cols[x][y];w<rows.length;w++)
					{
						picto.addclause(new int [] {(w*cols.length*numvars)+(x*numvars)+2+(y*2)},new boolean [] {false});
					}
					for(int z=0;z<(rows.length+1-cols[x][y]);z++)
					{
						//if col is start then its followed by certain number and a space if there is room
						for(int w=0;w<cols[x][y];w++)
						{
							picto.addclause(new int [] {(z*cols.length*numvars)+(x*numvars)+2+(y*2),((z+w)*cols.length*numvars)+(x*numvars)+1},new boolean [] {false,true});
						}
						//add clause for end of seq
						picto.addclause(new int [] {(z*cols.length*numvars)+(x*numvars)+2+(y*2),((z+(cols[x][y]-1))*cols.length*numvars)+(x*numvars)+3+(y*2)},new boolean [] {false,true});
						//if there is room at end of sequence then add a gap
						if(z+1+cols[x][y]<rows.length)
						{
							picto.addclause(new int [] {(z*cols.length*numvars)+(x*numvars)+2+(y*2),((z+1+cols[x][y])*cols.length*numvars)+(x*numvars)+1},new boolean [] {false,false});
						}
					}
				}
			}
		}
		//do for rows
		for (int x=0;x<rows.length;x++)
		{
			for (int y=0;y<rows[x].length;y++)
			{
				if(rows[x][y]==0)
				{
					for(int z=0;z<cols.length;z++)
					{
						picto.addclause(new int [] {(x*cols.length*numvars)+(z*numvars)+1},new boolean [] {false});
					}
				}
				else if(rows[x][y]==1)
				{
					for(int z=0;z<cols.length;z++)
					{
						picto.addclause(new int [] {(x*cols.length*numvars)+(z*numvars)+2+(y*2)+(colvars),(x*cols.length*numvars)+(z*numvars)+1},new boolean [] {false,true});
						picto.addclause(new int [] {(x*cols.length*numvars)+(z*numvars)+2+(y*2)+(colvars),(x*cols.length*numvars)+(z*numvars)+3+(y*2)+(colvars)},new boolean [] {false,true});
						//next one blank if possible
						if(z+1<cols.length)
						{
							picto.addclause(new int [] {(x*cols.length*numvars)+(z*numvars)+2+(y*2)+(colvars),(x*cols.length*numvars)+((z+1)*numvars)+1},new boolean [] {false,false});
						}
					}
				}
				else
				{
					//cant start too close to end
					for(int w=cols.length+1-rows[x][y];w<cols.length;w++)
					{
						picto.addclause(new int [] {(x*cols.length*numvars)+(w*numvars)+2+(y*2)+(colvars)},new boolean [] {false});
						
					}
					for(int z=0;z<(cols.length+1-rows[x][y]);z++)
					{
						//if col is start then its followed by certain number and a space if there is room
						for(int w=0;w<rows[x][y];w++)
						{
							picto.addclause(new int [] {(x*cols.length*numvars)+(z*numvars)+2+(y*2)+(colvars),(x*cols.length*numvars)+(z*numvars)+(w*numvars)+1},new boolean [] {false,true});
						}
						//add clause for end of sequence
						picto.addclause(new int [] {(x*cols.length*numvars)+(z*numvars)+2+(y*2)+(colvars),(x*cols.length*numvars)+(z*numvars)+((rows[x][y]-1)*numvars)+3+(y*2)+(colvars)},new boolean [] {false,true});
						//if there is room at end of sequence then add a gap
						if(z+1+rows[x][y]<cols.length)
						{
							picto.addclause(new int [] {(x*cols.length*numvars)+(z*numvars)+2+(y*2)+(colvars),(x*cols.length*numvars)+(z*numvars)+(rows[x][y]*numvars)+1},new boolean [] {false,false});
						}
					}
				}
			}
		}
		picto.createcnf(output);
	}
		
	private static void readInput(String filename)
	{
		textreader read=new textreader();
		String dir=read.returnInputDir();
		try
    	{
	    	// locate input file and parse out the values for each row and column, using it to populate the rows and cols arrays
	    	File file=new File(dir+filename+".txt");
			FileInputStream in = new FileInputStream(file);
			BufferedInputStream inbuffer=new BufferedInputStream(in);
			BufferedReader reader=new BufferedReader(new InputStreamReader(inbuffer));
			String currentln=reader.readLine();
			currentln=reader.readLine();
			for(int i=0;i<numrows;i++)
			{
				String [] splits = currentln.split(" ");
				int [] thisrow=new int [splits.length];
				for(int j=0;j<thisrow.length;j++)
				{
					thisrow[j]=Integer.parseInt(splits[j]);
				}
				rows[i]=thisrow;
				currentln=reader.readLine();
			}
			currentln=reader.readLine();
			currentln=reader.readLine();
			for(int i=0;i<numcols;i++)
			{
				String [] splits = currentln.split(" ");
				int [] thiscol=new int [splits.length];
				for(int j=0;j<thiscol.length;j++)
				{
					thiscol[j]=Integer.parseInt(splits[j]);
				}
				cols[i]=thiscol;
				currentln=reader.readLine();
			}
		}
		catch(Exception e)
		{
			System.err.println ("Error reading Picross Input "+e);	
		}
	}
}