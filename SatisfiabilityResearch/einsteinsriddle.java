public class einsteinsriddle
{
	public static void main(String [] args)
	{
		benchmark riddle=new benchmark();
		
		//set general rule clauses
		for(int j=0;j<5;j++)
		{
			for(int i=0;i<5;i++)
			{
				int x=(j*25)+(i*5);
				riddle.addclause(new int [] {x+1,x+2,x+3,x+4,x+5},new boolean [] {true,true,true,true,true});
				riddle.addclause(new int [] {x+1,x+2},new boolean []{false,false});
				riddle.addclause(new int [] {x+1,x+3},new boolean []{false,false});
				riddle.addclause(new int [] {x+1,x+4},new boolean []{false,false});
				riddle.addclause(new int [] {x+1,x+5},new boolean []{false,false});
				riddle.addclause(new int [] {x+2,x+3},new boolean []{false,false});
				riddle.addclause(new int [] {x+2,x+4},new boolean []{false,false});
				riddle.addclause(new int [] {x+2,x+5},new boolean []{false,false});
				riddle.addclause(new int [] {x+3,x+4},new boolean []{false,false});
				riddle.addclause(new int [] {x+3,x+5},new boolean []{false,false});
				riddle.addclause(new int [] {x+4,x+5},new boolean []{false,false});
				
				int y=(j*25)+i;
				riddle.addclause(new int [] {y+1,y+6,y+11,y+16,y+21},new boolean []{true,true,true,true,true});
				riddle.addclause(new int [] {y+1,y+6},new boolean []{false,false});
				riddle.addclause(new int [] {y+1,y+11},new boolean []{false,false});
				riddle.addclause(new int [] {y+1,y+16},new boolean []{false,false});
				riddle.addclause(new int [] {y+1,y+21},new boolean []{false,false});
				riddle.addclause(new int [] {y+6,y+11},new boolean []{false,false});
				riddle.addclause(new int [] {y+6,y+16},new boolean []{false,false});
				riddle.addclause(new int [] {y+6,y+21},new boolean []{false,false});
				riddle.addclause(new int [] {y+11,y+16},new boolean []{false,false});
				riddle.addclause(new int [] {y+11,y+21},new boolean []{false,false});
				riddle.addclause(new int [] {y+16,y+21},new boolean []{false,false});
			}
		}
		
		// order:
		// house		1				2				3				4				5
		// colour		red				yellow			blue			ivory			green
		// nationality	ukranian		english			norweigan		spaniard		japanese
		// drink		tea				water			milk			coffee			orange
		// smoke		chesterfields	kools			old gold		parliment		lucky strike
		// pet			zebra			fox				horse			snail			dog
			
		//set special rule clauses
		// 1. englishman lives in red house
		riddle.addclause(new int [] {1,27},new boolean []{false,true});
		riddle.addclause(new int [] {6,32},new boolean []{false,true});
		riddle.addclause(new int [] {11,37},new boolean []{false,true});
		riddle.addclause(new int [] {16,42},new boolean []{false,true});
		riddle.addclause(new int [] {21,47},new boolean []{false,true});
//		riddle.addclause(new int [] {127},new boolean []{true});
//		
//		// 2. spaniard owns a dog
		riddle.addclause(new int [] {29,105},new boolean []{false,true});
		riddle.addclause(new int [] {34,110},new boolean []{false,true});
		riddle.addclause(new int [] {39,115},new boolean []{false,true});
		riddle.addclause(new int [] {44,120},new boolean []{false,true});
		riddle.addclause(new int [] {49,125},new boolean []{false,true});
//		riddle.addclause(new int [] {295},new boolean []{true});
//		
//		// 3. coffee is drunk in the green house
		riddle.addclause(new int [] {5,54},new boolean []{false,true});
		riddle.addclause(new int [] {10,59},new boolean []{false,true});
		riddle.addclause(new int [] {15,64},new boolean []{false,true});
		riddle.addclause(new int [] {20,69},new boolean []{false,true});
		riddle.addclause(new int [] {25,74},new boolean []{false,true});
//		riddle.addclause(new int [] {174},new boolean []{true});
//		
//		// 4. ukranian drinks tea
		riddle.addclause(new int [] {26,51},new boolean []{false,true});
		riddle.addclause(new int [] {31,56},new boolean []{false,true});
		riddle.addclause(new int [] {36,61},new boolean []{false,true});
		riddle.addclause(new int [] {41,66},new boolean []{false,true});
		riddle.addclause(new int [] {46,71},new boolean []{false,true});
//		riddle.addclause(new int [] {226},new boolean []{true});
//		
//		// 5. green house is immediately to the right of the ivory house
		riddle.addclause(new int [] {4,9,14,19},new boolean []{true,true,true,true});
		riddle.addclause(new int [] {4,10},new boolean []{false,true});
		riddle.addclause(new int [] {9,15},new boolean []{false,true});
		riddle.addclause(new int [] {14,20},new boolean []{false,true});
		riddle.addclause(new int [] {19,25},new boolean []{false,true});
//		
//		// 6. old gold smoker owns snails
		riddle.addclause(new int [] {78,104},new boolean []{false,true});
		riddle.addclause(new int [] {83,109},new boolean []{false,true});
		riddle.addclause(new int [] {88,114},new boolean []{false,true});
		riddle.addclause(new int [] {93,119},new boolean []{false,true});
		riddle.addclause(new int [] {98,124},new boolean []{false,true});
//		riddle.addclause(new int [] {364},new boolean []{true});
//		
//		// 7. kools are smoked in yellow house
		riddle.addclause(new int [] {77,2},new boolean []{false,true});
		riddle.addclause(new int [] {82,7},new boolean []{false,true});
		riddle.addclause(new int [] {87,12},new boolean []{false,true});
		riddle.addclause(new int [] {92,17},new boolean []{false,true});
		riddle.addclause(new int [] {97,22},new boolean []{false,true});
//		riddle.addclause(new int [] {182},new boolean []{true});
//		
//		// 8. milk is drunk in 3rd house
		riddle.addclause(new int [] {63},new boolean []{true});
//		
//		// 9. norweigan person lives in 1st house
		riddle.addclause(new int [] {28},new boolean []{true});
//		
//		// 10. man who smokes chesterfields lives next to fox
		riddle.addclause(new int [] {76,107},new boolean []{false,true}); 
		riddle.addclause(new int [] {81,102,112},new boolean []{false,true,true}); 
		riddle.addclause(new int [] {86,107,117},new boolean []{false,true,true}); 
		riddle.addclause(new int [] {91,112,122},new boolean []{false,true,true}); 
		riddle.addclause(new int [] {96,117},new boolean []{false,true}); 
//		
//		// 11. kools smoked in house next to horse
		riddle.addclause(new int [] {77,108},new boolean []{false,true}); 
		riddle.addclause(new int [] {82,103,113},new boolean []{false,true,true}); 
		riddle.addclause(new int [] {87,108,118},new boolean []{false,true,true}); 
		riddle.addclause(new int [] {92,113,123},new boolean []{false,true,true}); 
		riddle.addclause(new int [] {97,118},new boolean []{false,true}); 		
//		
//		// 12. lucky striker smoker drinks orange juice
		riddle.addclause(new int [] {80,55},new boolean []{false,true});
		riddle.addclause(new int [] {85,60},new boolean []{false,true});
		riddle.addclause(new int [] {90,65},new boolean []{false,true});
		riddle.addclause(new int [] {95,70},new boolean []{false,true});
		riddle.addclause(new int [] {100,75},new boolean []{false,true});
//		riddle.addclause(new int [] {325},new boolean []{true});
//		
//		// 13. japanese person smokes parliment
		riddle.addclause(new int [] {30,79},new boolean []{false,true});
		riddle.addclause(new int [] {35,84},new boolean []{false,true});
		riddle.addclause(new int [] {40,89},new boolean []{false,true});
		riddle.addclause(new int [] {45,94},new boolean []{false,true});
		riddle.addclause(new int [] {50,99},new boolean []{false,true});
//		riddle.addclause(new int [] {274},new boolean []{true});
//		
//		// 14. norweigian person lives next to blue house
		riddle.addclause(new int [] {28,8},new boolean []{false,true}); 
		riddle.addclause(new int [] {33,3,13},new boolean []{false,true,true});
		riddle.addclause(new int [] {38,8,18},new boolean []{false,true,true}); 
		riddle.addclause(new int [] {43,13,23},new boolean []{false,true,true}); 
		riddle.addclause(new int [] {48,18},new boolean []{false,true}); 
//		
		riddle.createcnf(args[0]);
	}
}