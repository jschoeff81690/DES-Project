package sdes.main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.BitSet;
import java.util.Arrays;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Scanner;




public class TestClass2 {

    /**
     * @param args
     */
    // public static void main(String[] args) {

    //     //values from command line args
    //     boolean verbose = true;
    //     String paramsFile = "params.txt";

    //     //commandLine args here?

    //     SDES des = new SDES();

    //     readParams(paramsFile, des, verbose);

        // des.setKey("1100011110");
        // des.setPlainText("10001010");
        // //plaintText: 00101000
        // //encrypts to: 10001010
        // des.setIsEncryption(false);
        // des.encrypt();
    // }

    public static void main(String[] args) {

        //we can change these to command line args by
        System.out.println("Arguments: " + Arrays.toString(args));
        SDES des = new SDES();

        
        //Defaults
        String paramsFile   = "params.txt";
        String inputFile    = "p.txt";
        String outputFile   = "c.txt";
        String keyFile      = "k.txt";
        boolean encrypt     = true;
        boolean verbose     = false;
        boolean hexInput    = false; //is the input in form of binary hex?
        boolean asciiInput = false;  //is the input in binary file or not?
        String key          = "";
        String plainText   = "";

        //commands
        String inputFileNameCommand  = "-i";
        String specialCommand        = "--";
        String outputFileNameCommand = "-o";
        String encryptCommand        = "-e";
        String decryptCommand        = "-d";
        String keyCommand            = "-k";
        String paramCommand          = "-p";
        String stepsCommand          = "-s";
        String hexCommand            = "-x";
        String asciiCommand          = "-a";
        String runCommand          = "-R";
        String menuCommand           = "-menu";

        boolean keepRunning = true;
        Scanner in = new Scanner (System.in);
        while (keepRunning && args[0].equalsIgnoreCase(menuCommand)) {

            System.out.println("Interactive Menu: type -R to run encrypt/decryption. Otherwise enter commands");
            System.out.println(">");
            String whole = in.nextLine(); // receives everything in the next line till enter

            String command = whole.substring(0, 2);
            String userInput = whole.substring(2).trim(); //userInput will be saved and used constantly key

            if ( command.equalsIgnoreCase(inputFileNameCommand) || command.equalsIgnoreCase(specialCommand) ) {
                inputFile = userInput;
            } else if ( command.equalsIgnoreCase(outputFileNameCommand) || command.equalsIgnoreCase(specialCommand) ) {
                outputFile = userInput;
            } else if ( command.equalsIgnoreCase(encryptCommand)) {
                encrypt = true;
            } else if (command.equalsIgnoreCase(decryptCommand)) {
                encrypt = false;
            } else if (command.equalsIgnoreCase(keyCommand)) {

                //use this as example
                ////////////-k49204c6f7665204a61766121
                //String hex = "78654654";
                StringBuilder output = new StringBuilder();
                for (int i = 0; i < userInput.length(); i += 2) {
                    String str = userInput.substring(i, i + 2);
                    output.append((char)Integer.parseInt(str, 16));
                } //output is the string of converted hex to words
                // System.out.println(output);
                key = output.toString();

            } else if ( command.equalsIgnoreCase(paramCommand)) {
                paramsFile = userInput;
                //readParams(userInput, des, verbose);
                //test
                //System.out.println(param);
            } else if ( command.equalsIgnoreCase(stepsCommand)) {
                verbose = true;
            } else if ( command.equalsIgnoreCase(hexCommand)) {
                hexInput =  true;
            }else if ( command.equalsIgnoreCase(runCommand)) {
                hexInput =  true;
            }
        }//eof while running
        //If the menu wasnt read, use the commandline args
        if(!args[0].equalsIgnoreCase(menuCommand)) {
            for(String arg : args) {
                String command = arg.substring(0, 2);
                String userInput = arg.substring(2).trim(); //userInput will be saved and used constantly key

                if ( command.equalsIgnoreCase(inputFileNameCommand) || command.equalsIgnoreCase(specialCommand) ) {
                    inputFile = userInput;
                } else if ( command.equalsIgnoreCase(outputFileNameCommand) || command.equalsIgnoreCase(specialCommand) ) {
                    outputFile = userInput;
                } else if ( command.equalsIgnoreCase(encryptCommand)) {
                    encrypt = true;
                } else if (command.equalsIgnoreCase(decryptCommand)) {
                    encrypt = false;
                } else if (command.equalsIgnoreCase(keyCommand)) {

                    //use this as example
                    ////////////-k49204c6f7665204a61766121
                    //String hex = "78654654";
                    StringBuilder output = new StringBuilder();
                    for (int i = 0; i < userInput.length(); i += 2) {
                        String str = userInput.substring(i, i + 2);
                        output.append((char)Integer.parseInt(str, 16));
                    } //output is the string of converted hex to words
                    // System.out.println(output);
                    key = output.toString();

                } else if ( command.equalsIgnoreCase(paramCommand)) {
                    paramsFile = userInput;
                } else if ( command.equalsIgnoreCase(stepsCommand)) {
                    verbose = true;
                } else if ( command.equalsIgnoreCase(hexCommand)) {
                    hexInput =  true;
                }else if ( command.equalsIgnoreCase(runCommand)) {
                    keepRunning =  false;
                }
            }
        }
        if(hexInput)
            plainText  = hexToBinary(getLineFromFile(inputFile));
        else
            plainText  = getLineFromFile(inputFile);

        System.out.println(plainText);
        if(key.equals(""))
           key  = getLineFromFile(keyFile);

        readParams(paramsFile, des, verbose);
        des.setDisplayHex(hexInput);
        des.setKey(key);
        des.setPlainText(plainText);
        //plaintText: 00101000
        //encrypts to: 10001010
        des.setIsEncryption(encrypt);
        String cipher = des.encrypt();

        if(hexInput)
            writeToFile(binaryToHex(cipher),outputFile);
        else
            writeToFile(cipher,outputFile);

    }
    public static void writeToFile(String line, String fileName) {
        try {
            File file = new File(fileName);
             
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(line);
            bw.close();
        }catch (IOException e) {
            System.out.println("Error writing to file: " + fileName);
            System.out.println("Error: " + e.getLocalizedMessage());

        }
        
    }
    public static String getLineFromFile(String fileName) {
        String line="";
        try {
            File file = new File(fileName);
            FileReader fr = new FileReader(file.getAbsolutePath());
            BufferedReader br = new BufferedReader(fr);

            line = getLine(br);
            br.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error finding file: " + fileName);
            System.out.println("Error: " + e.getLocalizedMessage());

        }
        catch (IOException e) {
            System.out.println("Error writing to file: " + fileName);
            System.out.println("Error: " + e.getLocalizedMessage());

        }
        return line;
    }

    //hex -> binary
    public static String hexToBinary(String hex) {
        return new BigInteger(hex, 16).toString(2);
    }

    //bin-> hex
    public static String binaryToHex(String bin) {
        String back2hex;
        return back2hex = Long.toHexString(Long.parseLong(bin, 2));
    }

    public static void readParams(String fileName, SDES des, boolean verbose) {

        //String filename = args[0];
        String filename = "params.txt"; //default params.txt, comment this out and uncomment line above.  Just using this for easy testing
        des.setVerbose(verbose);
        String string;
        int[] parameters = new int[6];

        try {
            File file = new File(filename);
            FileReader fr = new FileReader(file.getAbsolutePath());
            BufferedReader br = new BufferedReader(fr);
            String line;
            int count = 5;
            for (int x = 0; x < 5; x++) {
                parameters[x] = getNum(getLine(br));
            }

            //set params
            int blockSize = parameters[0];
            int roundKeySize = parameters[3];
            des.setBlockSize(parameters[0]);
            des.setKeySize(parameters[1]);
            des.setEffectiveKeySize(parameters[2]);
            des.setRoundKeySize(parameters[3]);
            des.setNumberOfRounds(parameters[4]);

            int pc1[] = getNumArray(getLine(br));
            // System.out.println(Arrays.toString(pc1));
            des.setPc1(pc1);
            int pc2[] = getNumArray(getLine(br));
            // System.out.println(Arrays.toString(pc2));
            des.setPc2(pc2);
            int rs[] = getNumArray(getLine(br));
            // System.out.println(Arrays.toString(rs));
            des.setRotationSchedule(rs);

            int ip[] = getNumArray(getLine(br));
            // System.out.println(Arrays.toString(ip));
            // System.out.println(Arrays.toString(inverseArray(ip,blockSize)));
            des.setInitialPerm(ip);
            des.setInverseInitialPerm(inverseArray(ip, blockSize));
            int ep[] = getNumArray(getLine(br));
            // System.out.println(Arrays.toString(ep));
            des.setExpansionPerm(ep);

            int sBoxNum = getNum(getLine(br));
            // System.out.println(sBoxNum);
            des.setNumSBoxes(sBoxNum);

            int rc[] = getNumArray(getLine(br));
            // System.out.println(Arrays.toString(rc));
            des.setRowChoice(rc);

            int cs[] = getNumArray(getLine(br));
            // System.out.println(Arrays.toString(cs));
            des.setColChoice(cs);

            int pbox[] = getNumArray(getLine(br));
            // System.out.println(Arrays.toString(pbox));
            des.setPBoxPerm(pbox);


            int box = 0;
            int rows = (int)Math.pow(2, (roundKeySize / sBoxNum - blockSize / (2 * sBoxNum)) );
            int cols = (int)Math.pow(2, (blockSize / (2 * sBoxNum)) );
            while (box < sBoxNum) {
                int sBox[][] = new int[rows][cols];
                for (int i = 0; i < rows; i++) {
                    sBox[i] = getNumArray(getLine(br));
                    // System.out.println(Arrays.toString(sBox[i]));
                }
                des.setSbox(sBox, box);
                box++;
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

    /**
     * Get a line from bufferReader that is not blank, or comments
     *
     * @param  br          BufferReader for file ot get line from
     * @return             String from br readLine, that is not a comment or blankline
     * @throws IOException [description]
     */
    public static String getLine(BufferedReader br) throws IOException {
        int found = 0;
        String line = "";
        while (found < 1) {
            line = br.readLine();
            if (firstNum(line)) {
                found++;
            }
        }
        return line;
    }

    /**
     * Return the inverse of a permutation
     * @param  a permutation to invert
     * @param  N size of the inverted permutation
     * @return   the inverse of a
     */
    public static int[] inverseArray(int[] a, int N) {
        int aInv[] = new int[N];
        for (int i = 0; i < N; i++)
            aInv[a[i] - 1] = i + 1;
        return aInv;
    }

    /**
     * gets an integer from a string line from params file. removes comments delimeter "/"
     * @param  str string to get num from
     * @return     integer from string
     */
    public static int getNum(String str) {
        String arr[] = str.split("/");
        return Integer.parseInt(arr[0].trim());
    }

    /**
     * Gets an array of integers from a string line of params file. removes "/"
     * @param  str string to get the intgers from
     * @return     int[] of numbers from the string
     */
    public static int[] getNumArray(String str) {
        String arr[] = str.split("/");
        arr = arr[0].trim().split(" ");
        int out[] = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
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
        if (("").equals(str)) return false;
        String s = str.trim();
        if (s.charAt(0) < '0' || s.charAt(0) > '9')
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
    /**
     * Converts BitSet to binary string
     * @param  b      bitset to convert to string
     * @param  length length of the bitSet
     * @return        String representation of the bitset
     */
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
