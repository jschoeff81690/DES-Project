package sdes.main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.BitSet;




public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//values from command line args
		boolean verbose = true;
		String paramsFile = "params.txt";
		
		//commandLine args here?
		
		
		SDES des         =  new SDES();	
		
		readParams(paramsFile, des, verbose);
		
		des.encrypt();
	}
	
	public static void readParams(String fileName, SDES des, boolean verbose) {

		//String filename = args[0];
		String filename = "params.txt"; //default params.txt, comment this out and uncomment line above.  Just using this for easy testing
		String string;
		String[] parameters = new String[6];
		int PC1[]        = {3,5,2,7,4,10,1,9,8,6};
		int IP[]         = {2, 6, 3, 1, 4, 8, 5, 7};
		int inverse_IP[] = {4, 1, 3, 5, 7, 2, 8, 6};
		int EP[]         = {4,1,2,3,2,3,4,1}; 
		int PC2[]        = {6,3,7,4,8,5,10,9};
		int pBox[]		 = {2, 4, 3, 1};
		int numSBoxes 	 = 2;
		int sbox0[][]	 = {{1,0,3,2},{3,2,1,0},{0,2,1,3},{3,1,3,2}};//row1{c1,...,cN},...,rowN{c1,...,cN}
		int sbox1[][]	 = {{0,1,2,3},{2,0,1,3},{3,0,1,0},{2,1,0,3}};
		int sBoxes[][][] = {sbox0,sbox1};
		int rowChoice[]	 = {1,4};
		int colChoice[]	 = {2,3};


		
		try {
			File file = new File(filename);

	    	FileReader fr = new FileReader(file.getAbsolutePath());
	    	BufferedReader br = new BufferedReader(fr);

	    	//read in first 5 params
	    	for(int i=0; i<5; i++){ //assumes params.txt is in format of int (tab) comments, with the int being less than 3 digits long
	    		string = br.readLine();
	    		parameters[i] = string.substring(0, 2);
	    		//System.out.println(parameters[i]);
	    	}
	    
	/*		//set params
	    	des.setBlockSize(Integer.parseInt(parameters[0]));
	    	des.setKeySize(Integer.parseInt(parameters[1]));
	    	des.setEffectiveKeySize(Integer.parseInt(parameters[2]));
	    	des.setRoundKeySize(Integer.parseInt(parameters[3]));
	    	des.setNumberofRounds(Integer.parseInt(parameters[4]));
	    
	*/
	    
	    	//read in PC-1
	    	string = br.readLine();
	    	String[] fullpc1 = string.split(" "); //only need first 10, rest is comments
	    	String[] perc1 = new String[10];
	    	int[] permc1 = new int[10];
	    	String[] fix = fullpc1[9].split("/");
	    	fullpc1[9] = fix[0];
	    
	    	for (int i=0; i<10; i++){
	    		perc1[i] = fullpc1[i];
	    		//permc1[i] = Integer.parseInt(perc1[i]);
	    	}
	    	//System.out.println(Arrays.toString(permc1));
	    
	    
	    	//read in PC-2
	    	string = br.readLine();
	    	String[] fullpc2 = string.split(" ");
	    	String[] perc2 = new String[8];
	    	int[] permc2 = new int[8];
	    	for (int i=0; i<8; i++){
	    		perc2[i] = fullpc2[i];

	    	}
	    	//System.out.println(Arrays.toString(perc2));
	    
	    	//set pc1 and pc2, NEED TO CONVER THE STRING ARRAYS TO HEX FIRST
	    	for (int i=0; i<9; i++){
	    	//setPc1((perc1));
	    
	    	}
	    	for (int i=0; i<8; i++){
	    
	    	}
	    
	    	br.close();
		}
		catch(Exception e) {
			System.out.println("There was an error reading params from: "+fileName);
			System.out.println("Error: "+e.getMessage());
		}
		des.setPBoxPerm(pBox);
		des.setRowChoice(rowChoice);
		des.setColChoice(colChoice);
		des.setSBoxes(sBoxes);
		des.setNumSBoxes(numSBoxes);
		des.setPc1(PC1);
		des.setPc2(PC2);
		des.setInitialPerm(IP);
		des.setExpansionPerm(EP);
		des.setInverseInitialPerm(inverse_IP);
		des.setKeySize(10);
		des.setEffectiveKeySize(10);
		des.setRoundKeySize(8);
		des.setNumberOfRounds(2);
		des.setBlockSize(8);
		des.setRotationSchedule(new int[]{1,2});
		des.setVerbose(verbose);
		des.setKey("0110101100");
		des.setPlainText("10100101");
		
	}
	/* 
	Converts Binary String to BitSet
	*/
	public static BitSet toBitSet(String str) {
		BitSet b = new BitSet(str.length());
		for(int i=0; i < str.length(); i++) {
			if(str.charAt(i) == '1')
				b.set(i);
			else
				b.set(i, false);
		}
		//System.out.println("String str: " +str +" to BitSet: "+convertToString(b,str.length()));
		return b;
	}
	public static String convertToString(BitSet b,int length) {
		StringBuffer s = new StringBuffer();
		for(int i=0; i < length; i++) {
			if(b.get(i))
				s.append(1);
			else 
				s.append(0);
		}
		return s.toString();
	}
}