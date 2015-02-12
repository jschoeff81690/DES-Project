package sdes.main;

import java.util.BitSet;

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
	private boolean verbose;			//P-box transposition permutation 
	private boolean isEncryption;
	private BitSet key;
	private BitSet plainText;
	private boolean displayHex = false;
	
	public String encrypt() {
		//maybe confirm that all variables are correct?
		//length of plaintext == blocklength
		//length of key == keysize
		debugln("plainText: " + convertToString(plainText, blockSize));
		debugln("key: " + convertToString(key, keySize));
		BitSet cipher = permutate(plainText, initialPerm, blockSize);

		debugln("InitialPermutation result: " + convertToString(cipher,blockSize));
			

		//loop for rounds of encrypt
		for (int round = 1; round <= numberOfRounds; round++) {
			debugln("\nBegin Round: "+round);

			//Start round, Split result of Initial Perm into Lo and Ro
			BitSet leftHalf = subSet(cipher,0,blockSize/2-1);
			debugln("\tRound("+round+") Lo: " + convertToString(leftHalf,blockSize/2));
			BitSet rightHalf = subSet(cipher,blockSize/2,blockSize-1);
			debugln("\tRound("+round+") Ro: " + convertToString(rightHalf, blockSize/2));
			
			//perform expansion Permutatun on the right half,
			BitSet effectivePerm = permutate(rightHalf,expansionPerm, blockSize);
			debugln("\tRound("+round+") R0, EP result: " + convertToString(effectivePerm, blockSize));
			
			//generate Round Key
			int keyRound = (isEncryption) ? round : numberOfRounds-round+1;
			BitSet subKey = this.generateSubkey(key,keyRound);
			
			// XOR result of EP with subkey
			effectivePerm.xor(subKey);
			debugln("\tRound("+round+") EP xor subkey result: " + convertToString(effectivePerm, blockSize));
			
//			//left and right half after EP XOR subkey
//			leftHalf = subSet(effectivePerm,0,blockSize/2-1);
//			debugln("\tRound("+round+") L0: " + convertToString(leftHalf,blockSize/2));
//			rightHalf = subSet(effectivePerm,blockSize/2,blockSize-1);
//			debugln("\tRound("+round+") R0: " + convertToString(rightHalf, blockSize/2));
//			
			//sboxes
			BitSet boxResult = new BitSet(0);
			int boxResultLength = 0;
			BitSet slice  = new BitSet(blockSize/(2*numSBoxes));
			
			for(int box =0; box < numSBoxes; box++) {
				debugln("\tRound("+round+") sbox("+box+"): ");
				//break the expansion perm into equal parts based on the num of sBoxes
				int sliceStart = (blockSize/numSBoxes)*box;
				int sliceEnd = sliceStart+(blockSize/numSBoxes);
				slice = subSet(effectivePerm,sliceStart, sliceEnd) ;
				
				//get value from sbox
				slice = sBox(slice,box, blockSize/2);
				debugln("\t\tOutput: " + convertToString(slice, blockSize/(2*numSBoxes)));
				
				//the result of each sBox is combined
				appendBitSet(boxResult, slice, boxResultLength);
				boxResultLength += blockSize/(2*numSBoxes);
				
				debugln("\n\tCombined SBox Result: " +convertToString(boxResult, boxResultLength));
				
			}
			debugln("\tAfter Sboxes: " + convertToString(boxResult, boxResultLength));
			
			
			BitSet pBoxResult = permutate(boxResult,pBoxPerm, blockSize/2);
			debugln("\tRound("+round+") P-Box Result: " + convertToString(pBoxResult, blockSize/2));
			
			pBoxResult.xor(leftHalf);
			debugln("\tRound("+round+") P-Box XORed with L0 Result: " + convertToString(pBoxResult, blockSize/2));
			appendBitSet(cipher,rightHalf,0);
			appendBitSet(cipher, pBoxResult,blockSize/2);
			debugln("\tRound("+round+") Cipher Result: " + convertToString(cipher, blockSize));
			debugln("End Round: "+round +"\n");
		}
		BitSet result = new BitSet();
		//swap after last round
		appendBitSet(result,subSet(cipher,blockSize/2,blockSize),0);
		appendBitSet(result,subSet(cipher,0,blockSize/2),blockSize/2);

		debugln("After Swap: " + convertToString(result, blockSize));
		//perform Inverse IP on result
		result = permutate(result,inverseInitialPerm,blockSize);

		debugln("Cipher Result: " + convertToString(result, blockSize));
			
		return convertToString(result, blockSize);
	}

	/**
	 * Given BitSet A and B the result will be A appended with B. 
	 *
	 * 
	 * @param in          [BitSet to modify]
	 * @param append      [bitSet for source to append]
	 * @param inputLength [startLength of input BitSet]
	 */
	public void appendBitSet(BitSet in, BitSet append, int inputLength) {
		for(int i = 0; i <= append.length(); i++) {
			in.set(inputLength++,append.get(i));
		}
	}
	
	public BitSet sBox(BitSet in, int boxNum,  int inputLength) {
		int row = getInt(in,rowChoice);
		int col = getInt(in,colChoice);
		debugln("\t\tInput:"+convertToString(in, inputLength));
		debugln("\t\tRow: "+row);
		debugln("\t\tColumn: "+col);
		int num = sBoxes[boxNum][row][col];
		return toBitSet(Integer.toBinaryString(num));
	}
	
	/*
	 * Returns the decimal value of a combination of bits from BitSet input. 
	 * Choice[0] represents the MSB of result, choice[lenght-1] is LSB of result. 
	 * 
	 * @param in : used to form a binary number
	 * @param choice used to select bits from BitSet
	 * Returns an integer of choices from bitSet.
	 */
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
		BitSet output = (BitSet)in.clone();
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
	 * returns a sub-set of the input set.
	 * @param start -- the begin index, inclusive.
	 * @param end -- the end index, inclusive.
	 * @returns the subset of in from start to end
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
		if(!this.displayHex)
			return s.toString();
		else
			return binaryToHex(s.toString());
	}
	//bin-> hex
    public static String binaryToHex(String bin) {
        String back2hex;
        return back2hex = Long.toHexString(Long.parseLong(bin, 2));
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
	public void setDisplayHex(boolean b) {
		this.displayHex = b;
	}
	public boolean getDislayHex() {
		return this.displayHex;
	}
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

	public void setIsEncryption(boolean e) {
		this.isEncryption = e;
	}
	public boolean getIsEncryption() {
		return this.isEncryption;
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
		this.sBoxes = new int[num][][];
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

	public void setSbox(int[][] sbox, int boxNum) {
		this.sBoxes[boxNum] = sbox;
	}

}
