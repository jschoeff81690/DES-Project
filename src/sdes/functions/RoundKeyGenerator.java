package sdes.functions;

public class RoundKeyGenerator {
	private int keyLength;
	private int PC1[];
	private int PC2[];
	private boolean verbose;

	public RoundKeyGenerator(int keyLength, int pc1[], int pc2[], boolean verbose) {
		this.PC1 = pc1;
		this.PC2 = pc2;
		this.keyLength = keyLength;
		this.verbose = verbose;
	}

	public String generateSubkey(String key,int round) {
		StringBuilder output = new StringBuilder("0000000000");
		this.debugln("Generating Subkey: \n\tKey: " + key+ "\n\tRound: " + round);
		//Perform PC-1
		output = this.permutate(new StringBuilder(key), PC1, 10);
		this.debugln("\tPC1: " + output );
		
		//rotateleft halves
		output= this.rotateLeftSubstring(output,0,4,round);
		this.debugln("\tRotateleft(0,4): " + output );
		
		output= this.rotateLeftSubstring(output,5,9,round);
		this.debugln("\tRotateleft(5,9): " + output );
		
		//perform PC-2
		output = this.permutate(output, PC2, 8);
		this.debugln("\tPC2: " + output );
		
		return output.toString();
	}
	/* 
	 * Rotate a substring by amount
	 * returns stringBuilder with substring(start to end) rotated left by amount
	 * e.g.
	 * 	rotateLeftSubstring("abcdefgh",0,4,1) => "bceafgh"
	 * 	
	 */
	public StringBuilder rotateLeftSubstring(StringBuilder in, int start, int end, int amount) {
		StringBuilder output = new StringBuilder(in);
		for(int i=start; i <= end-amount; i++){
			output.setCharAt(i, in.charAt(i+amount));
		}
		for(int i=0; i<amount; i++) {
			output.setCharAt(end-amount+1+i, in.charAt(start+i));
		}
		return output;
	}
	
	public StringBuilder permutate(StringBuilder in, int permutation[], int length){ 
		StringBuilder output = new StringBuilder();
		for(int i =0; i < length; i++) {
			output.append("0");
			output.setCharAt(i, in.charAt(permutation[i]-1));
		}
		return output;
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
