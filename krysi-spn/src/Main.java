import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * spn data
 * r = 4
 * n = 4
 * m = 4
 * s = 32
 */
public class Main {

    static String X_TEST = "0001001010001111";
    static String Y_TEST = "1010111010110100";
    static String K_TEST = "00010001001010001000110000000000";

    static String chiffre = "000001001101001000001011101110000000001010001" +
            "111100011100111111101100000010100010100001110" +
            "10000000010011011001110010101110110000";
    static String k = "00111010100101001101011000111111";

    public static void main(String[] args) {

        // decrypt chiffre
        CTR ctr = new CTR(k);
        String decryption = ctr.decrypt(chiffre);
        System.out.println(decryption);

    }
}