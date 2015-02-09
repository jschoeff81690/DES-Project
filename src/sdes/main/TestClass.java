package sdes.main;
import java.util.BitSet;

import sdes.functions.*;



public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int PC1[]        = {3,5,2,7,4,10,1,9,8,6};
		int IP[]         = {2, 6, 3, 1, 4, 8, 5, 7};
		int inverse_IP[] = {4, 1, 3, 5, 7, 2, 8, 6};
		int EP[]         = {4,1,2,3,2,3,4,1}; 
		int PC2[]        = {6,3,7,4,8,5,10,9};
		int numSBoxes 	 = 2;
		int sbox0[][]	 = {{1,0,3,2},{3,2,1,0},{0,2,1,3},{3,1,3,2}};//row1{c1,...,cN},...,rowN{c1,...,cN}
		int sbox1[][]	 = {{0,1,2,3},{2,0,1,3},{3,0,1,0},{2,1,0,3}};
		int sBoxes[][][] = {sbox0,sbox1};
		int rowChoice[]	 = {1,4};
		int colChoice[]	 = {2,3};
		SDES des         =  new SDES();	
		
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
		des.setVerbose(true);
		des.setKey("0110101100");
		des.setPlainText("10100101");
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