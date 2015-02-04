package sdes.functions;

import java.util.BitSet;

public class BinaryRoundKeyGenerator {
	private BitSet key;
	private int keyLength;
	private int PC1[];
	private int PC2[];
	private boolean verbose;

	public BinaryRoundKeyGenerator(int keyLength,int effectiveKeySize, int roundKeySize, int pc1[], int pc2[], boolean verbose) {
		this.PC1 = pc1;
		this.PC2 = pc2;
		this.keyLength = keyLength;
		this.verbose = verbose;
	}

	public String generateSubkey(BitSet key,int round) {
		BitSet output = new BitSet(keyLength);
		this.key = key;
		
		debugln("Generating Subkey: \n\tKey: " + convertToString(key, keyLength)+ "\n\tRound: " + round);
		
		//Perform PC-1
		output = this.permutate(key, PC1, 10);
		debugln("\tPC1: " + convertToString(output,keyLength));
		
		//rotateleft halves
		output= this.rotateLeftSubstring(output,0,4,round);
		debugln("\tRotateleft(0,4): " + convertToString(output,keyLength));
		
		output= this.rotateLeftSubstring(output,5,9,round);
		debugln("\tRotateleft(5,9): " + convertToString(output,keyLength));
		
		//perform PC-2
		output = this.permutate(output, PC2, 8);
		debugln("\tPC2: " + convertToString(output,8));
		
		return output.toString();
	}
	/* 
	 * Rotate a substring by amount
	 * returns stringBuilder with substring(start to end) rotated left by amount
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
	
	public <T> void debug(T output) { 
		if(verbose)
			System.out.print(output);
	}
	public <T> void debugln(T output) { 
		if(verbose)
			System.out.println(output);
	}
}
