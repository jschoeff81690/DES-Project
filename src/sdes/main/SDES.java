package sdes.main;

import java.util.BitSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import sdes.functions.BinaryRoundKeyGenerator;

public class SDES {

	private int blockSize;				//Block size
	private int keySize;				//Key size - number of bits in key
	private int effectiveKeySize;		//Effective key size - number of key bits after PC-1
	private int roundKeySize;			//Round key size
	private int numberOfRounds;			//Number of Rounds
	private int pc1[];					// Initial Permuted Choice for Round Key Generation - takes Q bits in and outputs QE bits
	private int pc2[];					// Permuted Choice to set bits for Round Key
	private int rotationSchedule[];		// Left Rotation ScheduleLeft Rotation Schedule -  List of N integers, indicates number of logical left shifts 
	private int initialPerm[];			//Initial Permutation
	private int inverseInitialPerm[];	//IP^-1 - Inverse IP is derived from IP
	private int expansionPerm[];		//Expansion permutation - lists where each of R bits comes from in input of length B/2
	private int numSBoxes;				//Number of S-Boxes (each takes in R/T bits, outputs B/2T bits)
	private int rowChoice[];			//Permuted choice to select the bits from the input to an S-box that are used to select the row
	private int colChoice[];			//Permuted choice to select the bits from the input to an S-box that are used to select the col
	private int[][][] sBoxes; 			//ith S-box substitution - array of 2^x rows and 2^y columns, where x = R/T - B/2T, and y = B/2T
	private int pBoxPerm[];				//P-box transposition permutation 
	private boolean verbose;
	private BitSet key;
	private BitSet plainText;
	public static File file;
	

    	public static void main(String[] args) throws IOException{
        
    		//String filename = args[0];
    		String filename = "params.txt"; //default params.txt, comment this out and uncomment line above.  Just using this for easy testing
    		String string;
    		String[] parameters = new String[6]; 
    		file = new File(filename);

        	FileReader fr = new FileReader(file.getAbsolutePath());
        	BufferedReader br = new BufferedReader(fr);

        	//read in first 5 params
        	for(int i=0; i<5; i++){ //assumes params.txt is in format of int (tab) comments, with the int being less than 3 digits long
        		string = br.readLine();
        		parameters[i] = string.substring(0, 2);
        		//System.out.println(parameters[i]);
        	}
        
/*		//set params
        	setBlockSize(Integer.parseInt(parameters[0]));
        	setKeySize(Integer.parseInt(parameters[1]));
        	setEffectiveKeySize(Integer.parseInt(parameters[2]));
        	setRoundKeySize(Integer.parseInt(parameters[3]));
        	setNumberofRounds(Integer.parseInt(parameters[4]));
        
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




	
	public void encrypt() {
		//maybe confirm that all variables are correct?
		//length of plaintext == blocklength
		//length of key == keysize
		debugln("plainText:" + convertToString(plainText, blockSize));
		BitSet cipher = permutate(plainText, initialPerm, blockSize);
		debugln("InitialPermutation: " + convertToString(cipher,blockSize));
			

		//loop for rounds of encrypt
		for (int round = 1; round <= numberOfRounds; round++) {
			debugln("\nBegin Round: "+round);
			BitSet leftHalf = subSet(cipher,0,blockSize/2-1);
			debugln("\tRound("+round+") L0: " + convertToString(leftHalf,blockSize/2));
			BitSet rightHalf = subSet(cipher,blockSize/2,blockSize-1);
			debugln("\tRound("+round+") R0: " + convertToString(rightHalf, blockSize/2));
			
			BitSet effectivePerm = permutate(rightHalf,expansionPerm, blockSize);
			debugln("\tRound("+round+") R0, EP: " + convertToString(effectivePerm, blockSize));
			
			//generate Round KEy
			key = this.generateSubkey(key,round);
			
			// XOR result of EP with subkey
			effectivePerm.xor(key);
			debugln("\tRound("+round+") EP xor subkey: " + convertToString(effectivePerm, blockSize));
			
			//left right half after subkey
			leftHalf = subSet(effectivePerm,0,blockSize/2-1);
			debugln("\tRound("+round+") L0: " + convertToString(leftHalf,blockSize/2));
			rightHalf = subSet(effectivePerm,blockSize/2,blockSize-1);
			debugln("\tRound("+round+") R0: " + convertToString(rightHalf, blockSize/2));
			
			//sboxes
			BitSet boxResult,slice;
			for(int box =0; box < numSBoxes; box++) {
				slice = subSet(effectivePerm,(blockSize/(2*numSBoxes))*box,box+blockSize/(2*numSBoxes));
				debugln("\tRound("+round+") sbox("+box+") slice: " + convertToString(slice, blockSize/2));
				boxResult = sBox(slice,box, blockSize/2);

			}
			
			debugln("End Round: "+round +"\n");
		}
	}

	
	public BitSet sBox(BitSet in, int boxNum,  int outputLength) {
		int row = getInt(in,rowChoice);
		int col = getInt(in,colChoice);
		debugln("Input to Sbox("+boxNum+"):"+convertToString(in, outputLength));
		debugln("\tRow: "+row);
		debugln("\tColumn: "+col);
		int num = sBoxes[boxNum][row][col];
		debugln("\tOutput: "+num);
		return toBitSet(Integer.toBinaryString(num));
	}

	public int getInt(BitSet in, int[] choice) {
		String binary = "";
		for(int i = 0; i < choice.length; i++) {
			binary += (in.get(choice[i]-1)) ? "1" : "0";
		}
		return Integer.parseInt(binary, 2);
	}
	
	public BitSet generateSubkey(BitSet key, int round) {
		BitSet output = new BitSet(keySize);
		debugln("\n\tGenerating Round Key("+round+"): \n\t\tKey: " + convertToString(key, keySize));
		
		//Perform PC-1
		output = this.permutate(key, pc1, effectiveKeySize);
		debugln("\t\tPC1: " + convertToString(output,effectiveKeySize));
		
		//rotateleft halves
		output= this.rotateLeftSubstring(output,0,4,rotationSchedule[round-1]);
		debugln("\t\tRotateleft(0,4): " + convertToString(output,effectiveKeySize));
		
		output= this.rotateLeftSubstring(output,5,9,rotationSchedule[round-1]);
		debugln("\t\tRotateleft(5,9): " + convertToString(output,effectiveKeySize));
		
		//perform PC-2
		output = this.permutate(output, pc2, roundKeySize);
		debugln("\t\tPC2: " + convertToString(output,8));
		
		debugln("\tEnd roundKey Generation("+round +")\n");
		return output;
	}

	/* 
	 * Rotate a substring by amount
	 * returns stringBuilder with substring(start to end) rotated left by amount
	 * start -- the begin index, inclusive.
	 * end -- the end index, inclusive.
	 * e.g.
	 * 	rotateLeftSubstring("abcdefgh",0,4,1) => "bceafgh"
	 * 	
	 */
	public BitSet rotateLeftSubstring(BitSet in, int start, int end, int amount) {
		BitSet output = in;
		for(int i=start; i <= end-amount; i++){
			output.set(i, in.get(i+amount));
		}
		for(int i=0; i<amount; i++) {
			output.set(end-amount+1+i, in.get(start+i));
		}
		return output;
	}
	
	public BitSet permutate(BitSet in, int permutation[], int length){ 
		BitSet output = new BitSet(length);
		for(int i =0; i < length; i++) {
			output.set(i, in.get(permutation[i]-1));
		}
		return output;
	}

	/*
	 * start -- the begin index, inclusive.
	 * end -- the end index, inclusive.
	 */
	public BitSet subSet(BitSet in, int start, int end) {
		BitSet output = new BitSet();
		int length = 0;
		for(int i=start; i <=end; i++) {
			output.set(length,in.get(i));
			length++;
		}
		return output;
	}

	/* 
	Converts Binary String to BitSet
	e.g., "10101" becomes a bit set of {true, false, true, false, true}
	*/
	public BitSet toBitSet(String str) {
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
	public String convertToString(BitSet b,int length) {
		StringBuffer s = new StringBuffer();
		for(int i=0; i < length; i++) {
			if(b.get(i))
				s.append(1);
			else 
				s.append(0);
		}
		return s.toString();
	}

	public <T> void debug(T output) { 
		if(verbose)
			System.out.print(output);
	}
	public <T> void debugln(T output) { 
		if(verbose)
			System.out.println(output);
	}

	/*
		GETters and SETters for SDES class

	 */
	public void setKey(String str) {
		this.key = toBitSet(str);
	}
	public BitSet getKey() {
		return this.key;
	}

	public void setPlainText(String str) {
		this.plainText = toBitSet(str);
	}
	public BitSet getPlainText() {
		return this.plainText;
	}

	public void setVerbose(boolean v) {
		this.verbose = v;
	}
	public boolean getVerbose() {
		return this.verbose;
	}
	public void setBlockSize(int num) {
		this.blockSize = num;
	}

	public int getBlockSize() {
		return this.blockSize;
	}

	public void setKeySize(int num) {
		this.keySize = num;
	}

	public int getKeySize() {
		return this.keySize;
	}

	public void setEffectiveKeySize(int num) {
		this.effectiveKeySize = num;
	}

	public int getEffectiveKeySize() {
		return this.effectiveKeySize;
	}

	public void setRoundKeySize(int num) {
		this.roundKeySize = num;
	}

	public int getRoundKeySize() {
		return this.roundKeySize;
	}

	public void setNumberOfRounds(int num) {
		this.numberOfRounds = num;
	}

	public int getNumberOfRounds() {
		return this.numberOfRounds;
	}

	public void setPc1(int[] num) {
		this.pc1 = num;
	}

	public int[] getPc1() {
		return this.pc1;
	}

	public void setPc2(int[] num) {
		this.pc2 = num;
	}

	public int[] getPc2() {
		return this.pc2;
	}

	public void setRotationSchedule(int[] num) {
		this.rotationSchedule = num;
	}

	public int[] getRotationSchedule() {
		return this.rotationSchedule;
	}

	public void setInitialPerm(int[] num) {
		this.initialPerm = num;
	}

	public int[] getInitialPerm() {
		return this.initialPerm;
	}

	public void setInverseInitialPerm(int[] num) {
		this.inverseInitialPerm = num;
	}

	public int[] getInverseInitialPerm() {
		return this.inverseInitialPerm;
	}

	public void setExpansionPerm(int[] num) {
		this.expansionPerm = num;
	}

	public int[] getExpansionPerm() {
		return this.expansionPerm;
	}

	public void setNumSBoxes(int num) {
		this.numSBoxes = num;
	}

	public int getNumSBoxes() {
		return this.numSBoxes;
	}

	public void setRowChoice(int[] num) {
		this.rowChoice = num;
	}

	public int[] getRowChoice() {
		return this.rowChoice;
	}

	public void setColChoice(int[] num) {
		this.colChoice = num;
	}

	public int[] getColChoice() {
		return this.colChoice;
	}

	public void setPBoxPerm(int[] num) {
		this.pBoxPerm = num;
	}

	public int[] getPBoxPerm() {
		return this.pBoxPerm;
	}

	public int[][][] getSBoxes() {
		return sBoxes;
	} 

	public void setSBoxes(int[][][] boxes) {
		this.sBoxes = boxes;
	}

}
