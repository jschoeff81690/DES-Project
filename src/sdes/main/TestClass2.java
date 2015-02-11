package sdes.main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.BitSet;
import java.util.Arrays;




public class TestClass2 {

    /**
     * @param args
     */
    public static void main(String[] args) {

        //values from command line args
        boolean verbose = true;
        String paramsFile = "params.txt";

        //commandLine args here?

        SDES des = new SDES();

        readParams(paramsFile, des, verbose);

        des.setKey("1100011110");
        des.setPlainText("00101000");
        //plaintText: 00101000
        //encrypts to: 10001010
        des.setIsEncryption(true);
        des.encrypt();
    }


    public static void readParams(String fileName, SDES des, boolean verbose) {

        //String filename = args[0];
        String filename = "params.txt"; //default params.txt, comment this out and uncomment line above.  Just using this for easy testing
        des.setVerbose(verbose);
        String string;
        int[] parameters = new int[6];
        /*
         * These are values used for testing,
         * but will be overwritten once readable from params.txt
         */

        // int PC1[]        = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
        // int IP[]         = {2, 6, 3, 1, 4, 8, 5, 7};
        // int inverse_IP[] = {4, 1, 3, 5, 7, 2, 8, 6};
        // int EP[]         = {4, 1, 2, 3, 2, 3, 4, 1};
        // int PC2[]        = {6, 3, 7, 4, 8, 5, 10, 9};
        // int pBox[]       = {2, 4, 3, 1};
        // int numSBoxes    = 2;
        // int sbox0[][]    = {{1, 0, 3, 2}, {3, 2, 1, 0}, {0, 2, 1, 3}, {3, 1, 3, 2}}; //row1{c1,...,cN},...,rowN{c1,...,cN}
        // int sbox1[][]    = {{0, 1, 2, 3}, {2, 0, 1, 3}, {3, 0, 1, 0}, {2, 1, 0, 3}};
        // int sBoxes[][][] = {sbox0, sbox1};
        // int rowChoice[]  = {1, 4};
        // int colChoice[]  = {2, 3};



        try {
            File file = new File(filename);
            FileReader fr = new FileReader(file.getAbsolutePath());
            BufferedReader br = new BufferedReader(fr);

            int count=0;
            while(count < 5) {
                string = br.readLine();
                if(firstNum(string)) {
                    parameters[count] = getNum(string);
                    // System.out.println(parameters[count]);
                    count++;
                }
            }

            //set params
            int blockSize = parameters[0];
            int roundKeySize = parameters[3];
            des.setBlockSize(parameters[0]);
            des.setKeySize(parameters[1]);
            des.setEffectiveKeySize(parameters[2]);
            des.setRoundKeySize(parameters[3]);
            des.setNumberOfRounds(parameters[4]);

            
            while(count < 6) {
                string = br.readLine();
                if(firstNum(string)) {
                    int pc1[] = getNumArray(string);
                    // System.out.println(Arrays.toString(pc1));
                    des.setPc1(pc1);
                    count++;
                }
            }

            while(count < 7) {
                string = br.readLine();
                if(firstNum(string)) {
                    int pc2[] = getNumArray(string);
                    // System.out.println(Arrays.toString(pc2));
                    des.setPc2(pc2);
                    count++;
                }
            }

            while(count < 8) {
                string = br.readLine();
                if(firstNum(string)) {
                    int rs[] = getNumArray(string);
                    // System.out.println(Arrays.toString(rs));
                    des.setRotationSchedule(rs);
                    count++;
                }
            }

            while(count < 9) {
                string = br.readLine();
                if(firstNum(string)) {
                    int ip[] = getNumArray(string);
                    // System.out.println(Arrays.toString(ip));
                    // System.out.println(Arrays.toString(inverseArray(ip,blockSize)));
                    des.setInitialPerm(ip);
                    des.setInverseInitialPerm(inverseArray(ip,blockSize));
                    count++;
                }
            }

            while(count < 10) {
                string = br.readLine();
                if(firstNum(string)) {
                    int ep[] = getNumArray(string);
                    // System.out.println(Arrays.toString(ep));
                    des.setExpansionPerm(ep);
                    count++;
                }
            }
            int sBoxNum =0;
            while(count < 11) {
                string = br.readLine();
                if(firstNum(string)) {
                    sBoxNum= getNum(string);
                    // System.out.println(sBoxNum);
                    des.setNumSBoxes(sBoxNum);
                    count++;
                }
            }

            while(count < 12) {
                string = br.readLine();
                if(firstNum(string)) {
                    int rs[] = getNumArray(string);
                    // System.out.println(Arrays.toString(rs));
                    des.setRowChoice(rs);
                    count++;
                }
            }

            while(count < 13) {
                string = br.readLine();
                if(firstNum(string)) {
                    int cs[] = getNumArray(string);
                    // System.out.println(Arrays.toString(cs));
                    des.setColChoice(cs);
                    count++;
                }
            }

            while(count < 14) {
                string = br.readLine();
                if(firstNum(string)) {
                    int pbox[] = getNumArray(string);
                    // System.out.println(Arrays.toString(pbox));
                    des.setPBoxPerm(pbox);
                    count++;
                }
            }

            int box =0;
            int rows = (int)Math.pow(2, (roundKeySize/sBoxNum - blockSize/(2*sBoxNum)) );
            int cols = (int)Math.pow(2, (blockSize/(2*sBoxNum)) );
            while(box < sBoxNum) {
                string = br.readLine();
                if(firstNum(string)) {
                    int sBox[][] = new int[rows][cols];
                    for(int i =0; i < rows; i++) {
                        sBox[i] = getNumArray(string);
                        // System.out.println(Arrays.toString(sBox[i]));
                        string = br.readLine();
                    }
                    //System.out.println(Arrays.toString(sBox));
                    des.setSbox(sBox,box);
                    box++;
                }

            }

            br.close();
        } catch (FileNotFoundException  e) {
            System.out.println("There was an error reading file: " + fileName);
            System.out.println("Error: " + e.getLocalizedMessage());
        } catch (IOException  e) {
            System.out.println("There was an error reading params from: " + fileName);
            System.out.println("Error: " + e.getLocalizedMessage());
        }
    }

    public static int[] inverseArray(int[] a, int N) {
        int aInv[] = new int[N];
        for (int i = 0; i < N; i++)
            aInv[a[i]-1] = i+1;
        return aInv;
    }

    public static int getNum(String str) {
        String arr[] = str.split("/");
        return Integer.parseInt(arr[0].trim());
    }

    public static int[] getNumArray(String str) {
        String arr[] = str.split("/");
        arr = arr[0].trim().split(" ");
        int out[] = new int[arr.length];
        for(int i =0; i < arr.length; i++) {
            out[i] = Integer.parseInt(arr[i]);
        }
        return out;
    }

    /**
     * is the first character of a string a number
     * @param  str [description]
     * @return     [description]
     */
    public static boolean firstNum(String str) {
        if(("").equals(str)) return false;
        String s = str.trim();
        if(s.charAt(0) < '0' || s.charAt(0) > '9')
            return false;
        return true;
    }

    /*
    Converts Binary String to BitSet
    */
    public static BitSet toBitSet(String str) {
        BitSet b = new BitSet(str.length());
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '1')
                b.set(i);
            else
                b.set(i, false);
        }
        //System.out.println("String str: " +str +" to BitSet: "+convertToString(b,str.length()));
        return b;
    }
    public static String convertToString(BitSet b, int length) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < length; i++) {
            if (b.get(i))
                s.append(1);
            else
                s.append(0);
        }
        return s.toString();
    }
}
