package sdes.main;
import java.util.BitSet;

import sdes.functions.*;



public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int PC1[] = {3,5,2,7,4,10,1,9,8,6};
		int EP[]  = {4,1,2,3,2,3,4,1}; 

		int PC2[] = {6,3,7,4,8,5,10,9};
		SDES des =  new SDES();	
		des.setPc1(PC1);
		des.setPc2(PC2);
		des.setKeySize(10);
		des.setEffectiveKeySize(10);
		des.setRoundKeySize(8);
		des.setVerbose(true);
		des.encrypt();
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
		System.out.println("String str: " +str +" to BitSet: "+convertToString(b,str.length()));
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
	public static String generate_subkey(String key,int round) {
		int PC1[] = {3,5,2,7,4,10,1,9,8,6};
		int PC2[] = {6,3,7,4,8,5,10,9};
		StringBuilder output = new StringBuilder("0000000000");
		
		//Perform PC-1
		output = permutate(new StringBuilder(key), PC1, 10);
		//System.out.println("PC1: " + output );
		
		//rotateleft halves
		output= rotateLeftSubstring(output,0,4,round);
		//System.out.println("rotateleft(0,4): " + output );
		output= rotateLeftSubstring(output,5,9,round);
		//System.out.println("rotateleft(5,9): " + output );
		
		//perform PC-2
		output = permutate(output, PC2, 8);
		//System.out.println("PC2: " + output );
		
		return output.toString();
	}
	/* 
	 * Rotate a substring by amount
	 * returns stringBuilder with substring(start to end) rotated left by amount
	 * e.g.
	 * 	rotateLeftSubstring("abcdefgh",0,4,1) => "bceafgh"
	 * 	
	 */
	public static StringBuilder rotateLeftSubstring(StringBuilder in, int start, int end, int amount) {
		StringBuilder output = new StringBuilder(in);
		for(int i=start; i <= end-amount; i++){
			output.setCharAt(i, in.charAt(i+amount));
		}
		for(int i=0; i<amount; i++) {
			output.setCharAt(end-amount+1+i, in.charAt(start+i));
		}
		return output;
	}
	
	public static StringBuilder permutate(StringBuilder in, int permutation[], int length){ 
		StringBuilder output = new StringBuilder();
		for(int i =0; i < length; i++) {
			output.append("0");
			output.setCharAt(i, in.charAt(permutation[i]-1));
		}
		return output;
	}
}
