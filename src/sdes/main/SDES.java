package sdes.main;

import java.util.BitSet;

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
	
	public void encrypt() {
		//maybe confirm that all variables are correct?
		
		//loop for rounds?
		//for (int i =1; i <= rounds; i++) {
		//	//do round stuff
		//}
		
		BinaryRoundKeyGenerator binKeyGen = new BinaryRoundKeyGenerator(keySize, effectiveKeySize, roundKeySize,  pc1, pc2, verbose);
		binKeyGen.generateSubkey(toBitSet("0110101100"),1);
	}
	/* 
	Converts Binary String to BitSet
	*/
	public BitSet toBitSet(String str) {
		BitSet b = new BitSet(str.length());
		for(int i=0; i < str.length(); i++) {
			if(str.charAt(i) == '1')
				b.set(i);
			else
				b.set(i, false);
		}
		System.out.println("String str: " +str +" to BitSet: "+convertToString(b,str.length()));
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



	/*
		GETters and SETters for SDES class

	 */

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

	public <T> void debug(T output) { 
		if(verbose)
			System.out.print(output);
	}
	public <T> void debugln(T output) { 
		if(verbose)
			System.out.println(output);
	}
}
