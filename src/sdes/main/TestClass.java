package sdes.main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Scanner;




public class TestClass {

    /**
     * @param args
     */
    /*  public static void main(String[] args) {

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
//      } */
//
//    public static void main(String[] args) {
//
//        //we can change these to command line args by
//        System.out.println("Arguments: " +Arrays.toString(args));
//
//        SDES des = new SDES();
//
//        readParams(paramsFile, des, verbose);
//        //Defaults
//        String paramsFile = "params.txt";
//        boolean encrypt = true;
//        boolean verbose = false;
//        boolean hexInput = false;
//        boolean binaryInput = false;
//
//        String inputFileNameCommand  = "-i";
//        String specialCommand        = "--";
//        String outputFileNameCommand = "-o";
//        String encryptCommand        = "-e";
//        String decryptCommand        = "-d";
//        String keyCommand            = "-k";
//        String paramCommand          = "-p";
//        String stepsCommand         = "-s";
//        String hexCommand            = "-x";
//
//        boolean keepRunning = true;
//        while (keepRunning) {
//
//            Scanner in = new Scanner (System.in);
//            String whole = in.nextLine(); // receives everything in the next line till enter
//
//            String command = whole.substring(0, 2);
//            String userInput = whole.substring(2).trim(); //userInput will be saved and used constantly key
//
//            if ( command.equalsIgnoreCase(inputFileName) || command.equalsIgnoreCase(special) ) {
//                //checks to see if input is = to -i reguardless of case
//
//                //////  call public static void readParams(userInput)
//
//                //  System.out.println(command);
//            } else if ( command.equalsIgnoreCase(outputFileName) || command.equalsIgnoreCase(special) ) {
//
//                //should set verbose so nothing is printed out??
//                // if something is printed to the console it needs to be printed to the outputfile through this
//                //if statement?
//                //???will this take everything written to the console and write it to a txt file?
//                //---see output.java & args.java
//
//                //System.out.println(outputFileName);
//
//            } else if ( command.equalsIgnoreCase(encrypt)) {
//                encrypt = true;
//                //call encrypt process stop before decrypting
//                //System.out.println(encrypt);
//            } else if (command.equalsIgnoreCase(decrypt)) {
//                encrypt = true;
//
//                //begin decrypt process
//
//            } else if (command.equalsIgnoreCase(key)) {
//
//                //use this as example
//                ////////////-k49204c6f7665204a61766121
//                //String hex = "78654654";
//                StringBuilder output = new StringBuilder();
//                for (int i = 0; i < userInput.length(); i += 2) {
//                    String str = userInput.substring(i, i + 2);
//                    output.append((char)Integer.parseInt(str, 16));
//                } //output is the string of converted hex to words
//                // System.out.println(output);
//                key = output;
//
//            } else if ( command.equalsIgnoreCase(param)) {
//
//                //readParams(userInput, des, verbose);
//                //test
//                //System.out.println(param);
//            } else if ( command.equalsIgnoreCase(steps)) {
//                //System.out.println(steps);
//                //this will goto intermediate steps (round keys, left & right side input to each round
//                //and print them
//                //--should be just a goto function
//                verbose = true;
//
//            } else if ( command.equalsIgnoreCase(hex)) {
//                //System.out.println(hex);
//                hex=  true;
//                //take user input send from hex -> binary
//                String binaryUserInput = hexToBinary(userInput);
//
//                //////////send the binary through encrpyt etc
//
//                ////receive new binary
//                //String binaryback2Hex = binaryToHex(new binary from sdes)
//            }
//        }//eof while running
//
//    }
//
//
//    //hex -> binary
//    public static String hexToBinary(String hex) {
//        return new BigInteger(hex, 16).toString(2);
//    }
//
//    //bin-> hex
//    public static String binaryToHex(String bin) {
//        String back2hex;
//        return back2hex = Long.toHexString(Long.parseLong(bin, 2));
//    }
//
//    public static void readParams(String fileName, SDES des, boolean verbose) {
//
//        //String filename = args[0];
//        String filename = "params.txt"; //default params.txt, comment this out and uncomment line above.  Just using this for easy testing
//        String string;
//        String[] parameters = new String[6];
//        /*
//         * These are values used for testing,
//         * but will be overwritten once readable from params.txt
//         */
//
//        int PC1[]        = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
//        int IP[]         = {2, 6, 3, 1, 4, 8, 5, 7};
//        int inverse_IP[] = {4, 1, 3, 5, 7, 2, 8, 6};
//        int EP[]         = {4, 1, 2, 3, 2, 3, 4, 1};
//        int PC2[]        = {6, 3, 7, 4, 8, 5, 10, 9};
//        int pBox[]       = {2, 4, 3, 1};
//        int numSBoxes    = 2;
//        int sbox0[][]    = {{1, 0, 3, 2}, {3, 2, 1, 0}, {0, 2, 1, 3}, {3, 1, 3, 2}}; //row1{c1,...,cN},...,rowN{c1,...,cN}
//        int sbox1[][]    = {{0, 1, 2, 3}, {2, 0, 1, 3}, {3, 0, 1, 0}, {2, 1, 0, 3}};
//        int sBoxes[][][] = {sbox0, sbox1};
//        int rowChoice[]  = {1, 4};
//        int colChoice[]  = {2, 3};
//
//
//
//        try {
//            File file = new File(filename);
//            FileReader fr = new FileReader(file.getAbsolutePath());
//            BufferedReader br = new BufferedReader(fr);
//
//            //read in first 5 params
//            for (int i = 0; i < 5; i++) { //assumes params.txt is in format of int (tab) comments, with the int being less than 3 digits long
//                string = br.readLine();
//                parameters[i] = string.substring(0, 2);
//                //System.out.println(parameters[i]);
//            }
//
//            //set params
//            des.setBlockSize(Integer.parseInt(parameters[0]));
//            des.setKeySize(Integer.parseInt(parameters[1]));
//            des.setEffectiveKeySize(Integer.parseInt(parameters[2]));
//            des.setRoundKeySize(Integer.parseInt(parameters[3]));
//            des.setNumberOfRounds(Integer.parseInt(parameters[4]));
//
//
//
//            //read in PC-1
//            string = br.readLine();
//            String[] fullpc1 = string.split(" "); //only need first 10, rest is comments
//            String[] perc1 = new String[10];
//            int[] permc1 = new int[10];
//            String[] fix = fullpc1[9].split("/");
//            fullpc1[9] = fix[0];
//
//            for (int i = 0; i < 10; i++) {
//                perc1[i] = fullpc1[i];
//                permc1[i] = Integer.parseInt(perc1[i].trim(), 16);
//            }
//            //System.out.println(Arrays.toString(permc1));
//
//
//            //read in PC-2
//            string = br.readLine();
//            String[] fullpc2 = string.split(" ");
//            String[] perc2 = new String[8];
//            int[] permc2 = new int[8];
//            for (int i = 0; i < 8; i++) {
//                perc2[i] = fullpc2[i];
//                permc2[i] = Integer.parseInt(perc2[i].trim(), 16);
//            }
//            //System.out.println(Arrays.toString(permc2));
//
//            //set pc1 and pc2
//            des.setPc1((permc1));
//            des.setPc2((permc2));
//
//
//
//            //read and set left rotation schedule
//            string = br.readLine();
//            String[] rsc = string.split(" "); // rs[1] = 2 //comments
//            fix = rsc[1].split("/"); //removing the comments so rs[1] = 2
//            rsc[1] = fix[0];
//
//            int[] rs = new int[2];
//            rs[0] = Integer.parseInt(rsc[0].trim(), 16);
//            rs[1] = Integer.parseInt(rsc[1].trim(), 16);
//            des.setRotationSchedule(rs);
//
//
//            //int [] IP = new int[8];
//            // read and set IP
//            string = br.readLine();
//            if (string.equals("")) { //check for blank line
//                string = br.readLine();
//            }
//            String[] initP = string.split(" ");
//            fix = initP[7].split("/");
//            initP[7] = fix[0];
//            for (int i = 0; i < 8; i++) {
//                IP[i] = Integer.parseInt(initP[i]);
//            }
//            //System.out.println(Arrays.toString(IP));
//            des.setInitialPerm(IP); // IP is int array
//
//
//            // read and set EP
//            // int EP[] = new int[8];
//            string = br.readLine();
//            String[] exP = string.split(" ");
//            fix = exP[7].split("/");
//            exP[7] = fix[0];
//            for (int i = 0; i < 8; i++) {
//                EP[i] = Integer.parseInt(exP[i]);
//            }
//            //System.out.println(Arrays.toString(EP));
//            des.setExpansionPerm(EP);
//
//
//            // read and set P-box transposition
//            // int pBox[] = null;
//            string = br.readLine();
//            String[] pBoxPerm = string.split(" ");
//            fix = pBoxPerm[3].split("/");
//            pBoxPerm[3] = fix[0];
//            for (int i = 0; i < 4; i++) {
//                pBox[i] = Integer.parseInt(pBoxPerm[i]);
//            }
//            des.setPBoxPerm(pBox);
//
//            // read and set # of sbox
//            // int numSBoxes;
//
//            string = br.readLine();
//            if (string.equals("")) {
//                string = br.readLine();
//            }
//            String[] sboxnum = string.split("/");
//            numSBoxes = Integer.parseInt(sboxnum[0]);
//            des.setNumSBoxes(numSBoxes);
//
//
//            // read and set row choice
//            string = br.readLine();
//            String[] rc = string.split(" ");
//            fix = rc[1].split("/");
//            rc[1] = fix[0];
//            for (int i = 0; i < 2; i++) {
//                rowChoice[i] = Integer.parseInt(rc[i]);
//            }
//            des.setRowChoice(rowChoice);
//
//
//            // read and set col choice
//            string = br.readLine();
//            String[] cc = string.split(" ");
//            fix = cc[1].split("/");
//            cc[1] = fix[0];
//            for (int i = 0; i < 2; i++) {
//                colChoice[i] = Integer.parseInt(cc[i]);
//            }
//            des.setColChoice(colChoice);
//
//
//
//
//            // read and set sBoxes 0 and 1
//
//            string = br.readLine();
//            if (string.equals("")) {
//                string = br.readLine();
//            }
//            String [] r11 = string.split(" ");
//            fix = r11[3].split("/");
//            r11[3] = fix[0];
//            for (int i = 0; i < 4; i++) {
//                sbox0[1][i] = Integer.parseInt(r11[i]);
//            }
//
//            string = br.readLine();
//            String [] r12 = string.split(" ");
//            fix = r12[3].split("/");
//            r12[3] = fix[0];
//            for (int i = 0; i < 4; i++) {
//                sbox0[2][i] = Integer.parseInt(r12[i]);
//            }
//
//            string = br.readLine();
//            String [] r13 = string.split(" ");
//            fix = r13[3].split("/");
//            r13[3] = fix[0];
//            for (int i = 0; i < 4; i++) {
//                sbox0[3][i] = Integer.parseInt(r13[i]);
//            }
//
//            string = br.readLine();
//            String [] r14 = string.split(" ");
//            fix = r14[3].split("/");
//            r14[3] = fix[0];
//            for (int i = 0; i < 4; i++) {
//                sbox0[4][i] = Integer.parseInt(r14[i]);
//            }
//            des.setSbox(sbox0, 0); //sbox 0
//
//
//            br.readLine(); //get rid of the comment line
//
//            //read and set sbox1
//            string = br.readLine();
//            String [] r21 = string.split(" ");
//            fix = r21[3].split("/");
//            r21[3] = fix[0];
//            for (int i = 0; i < 4; i++) {
//                sbox1[1][i] = Integer.parseInt(r21[i]);
//            }
//
//            string = br.readLine();
//            String [] r22 = string.split(" ");
//            fix = r22[3].split("/");
//            r22[3] = fix[0];
//            for (int i = 0; i < 4; i++) {
//                sbox1[2][i] = Integer.parseInt(r22[i]);
//            }
//
//            string = br.readLine();
//            String [] r23 = string.split(" ");
//            fix = r23[3].split("/");
//            r23[3] = fix[0];
//            for (int i = 0; i < 4; i++) {
//                sbox1[3][i] = Integer.parseInt(r23[i]);
//            }
//
//            string = br.readLine();
//            String [] r24 = string.split(" ");
//            fix = r24[3].split("/");
//            r24[3] = fix[0];
//            for (int i = 0; i < 4; i++) {
//                sbox1[4][i] = Integer.parseInt(r24[i]);
//            }
//            des.setSbox(sbox1, 1); //sbox 1
//
//
//
//
//            //des.setSBoxes(sBoxes);
//
//            br.close();
//        } catch (Exception e) {
//            System.out.println("There was an error reading params from: " + fileName);
//            System.out.println("Error: " + e.getMessage());
//        }
//
//        des.setPBoxPerm(pBox);
//        des.setRowChoice(rowChoice);
//        des.setColChoice(colChoice);
//        des.setSBoxes(sBoxes);
//        des.setNumSBoxes(numSBoxes);
//        des.setPc1(PC1);
//        des.setPc2(PC2);
//        des.setInitialPerm(IP);
//        des.setExpansionPerm(EP);
//        des.setInverseInitialPerm(inverse_IP);
//        des.setKeySize(10);
//        des.setEffectiveKeySize(10);
//        des.setRoundKeySize(8);
//        des.setNumberOfRounds(2);
//        des.setBlockSize(8);
//        des.setRotationSchedule(new int[] {1, 3});
//        des.setVerbose(verbose);
//
//    }
//    /*
//    Converts Binary String to BitSet
//    */
//    public static BitSet toBitSet(String str) {
//        BitSet b = new BitSet(str.length());
//        for (int i = 0; i < str.length(); i++) {
//            if (str.charAt(i) == '1')
//                b.set(i);
//            else
//                b.set(i, false);
//        }
//        //System.out.println("String str: " +str +" to BitSet: "+convertToString(b,str.length()));
//        return b;
//    }
//    public static String convertToString(BitSet b, int length) {
//        StringBuffer s = new StringBuffer();
//        for (int i = 0; i < length; i++) {
//            if (b.get(i))
//                s.append(1);
//            else
//                s.append(0);
//        }
//        return s.toString();
//    }
}
