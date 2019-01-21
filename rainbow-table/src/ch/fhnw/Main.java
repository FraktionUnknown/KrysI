package ch.fhnw;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {
    static int PASSWORD_COUNT = 2000;
    static String HASH = "1d56a37fb6b08aa709fe90e12ca59e12";
    static String password;
    static String[][] passwords = new String[2][PASSWORD_COUNT];
    static int counter = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 2000; i++) {
            String password = "0000000";
            for (int j = 0; j < i; j++) {
                password = generatePassword(i);
            }
            passwords[0][i] = password;

            passwords[1][i] = md5(passwords[0][i]);
            passwords[1][i] = reduction(passwords[1][i], 0);
            for (int j = 1; j < 2000; j++) {
                passwords[1][i] = md5(passwords[1][i]);
                passwords[1][i] = reduction(passwords[1][i], j);

            }
        }

        String foundPassword = tryToFindPw(HASH);


        System.out.print("The Password is: " + foundPassword);

    }


    /**
     * the algorithm from Slide 3.27
     */
    private static String reduction(String hash, int step) {
        StringBuilder clear_text = new StringBuilder("0000000");
        BigInteger H = new BigInteger(hash, 16);
        H = H.add(BigInteger.valueOf(step)); //hasvalue

        int L = 7; //password length
        char[] Z = new char[36]; //characters from password
        BigInteger Z_length = BigInteger.valueOf(Z.length);

        //characters from 0 to 9
        for (int i = 0; i < 10; i++) {
            Z[i] = (char) (i + 48);
        }

        //characters from a to z
        for (int i = 10; i < 36; i++) {
            Z[i] = (char) (i + 87);
        }

        for (int i = L - 1; i >= 0; i--) {
            char value = Z[H.mod(Z_length).intValue()];
            clear_text.setCharAt(i, value); //clear value construction
            H = H.divide(Z_length); 
        }
        return clear_text.toString();
    }


    public static String md5(String input) {
        try {
            //Java API to generate MD5 hashes
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            String hash = new BigInteger(1, messageDigest).toString(16);
            //needed to ask for lengh so the string will be 32 bit long.
            while (hash.length() < 32) {
                hash = "0" + hash;
            }
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private static String generatePassword(int num) {
        //Stackoverflow FTW

        String charSet = "0123456789abcdefghijklmnopqrstuvwxyz";
        String text = "";
        //case 0: would ceil to -0
        if(num == 0){
            text = "0";
        }
        //case 1: would ceil to 0 had to check this case
        if(num == 1){
            text = "1";
            //everythin > 1 works.
        }else {
            int j = (int) Math.ceil(Math.log(num) / Math.log(charSet.length()));
            for (int i = 0; i < j; i++) {
                text += charSet.charAt(num % charSet.length());
                num /= charSet.length();
            }
        }
        while (text.length() < 7) {
            text = "0" + text;
        }
        return text;
    }

    //fixed this
    //http://royvanrijn.com/blog/2011/01/rainbow-tables/ Find Hash
    public static String tryToFindPw(String searching) {
        for (int i = 2000; i > -1; i--) {
            String momentum = searching;
            int step = i;

            while (step < 1999) {
                momentum = reduction(momentum, step);
                momentum = md5(momentum);
                step++;
            }
            //initial last step reduction 1999 of hash. Last step after the while loop when hash isnt the last possition.
            momentum = reduction(momentum, 1999);

//iterates over the last values of the Table
            if (Arrays.asList(passwords[1]).contains(momentum)) {
                //returns index of table where it has found the value
                int index = Arrays.asList(passwords[1]).indexOf(momentum);
                //temporary variable which stores the value of the initial password that generates the chain.
                String temp = passwords[0][index];
                //loops the hash reduction algo unitl the step i -1 which the value was found!
                for (int p = 0; p < i; p++) {
                    temp = md5(temp);
                    temp = reduction(temp, p);

                }

                return temp;
            }

        }

        return "not found";

    }
}