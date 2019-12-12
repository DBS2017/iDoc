package servicios;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @version 0.2
 * @author Martín Samán
 */
public class EncriptarS {

    /**
     * Encripta un texto a MD5
     *
     * @param input texto a encriptar
     * @return texto encriptado
     */
    public static String encriptarPssw(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Encripta un número de expediente, esto lo consulta el usuario por app
     * móvil
     *
     * @param input número de expediente en String
     * @return expediente encriptado
     */
    public static String encriptarExpediente(String input) {
        int output = 0;
        String abecedario = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ1234567890";
        try {
            char caracteres[] = input.toUpperCase().toCharArray();
            int total = 0;
            for (char caracter : caracteres) {
                total += Integer.parseInt(String.valueOf(caracter));
            }
            for (char caracter : caracteres) {
                output += Integer.parseInt(String.valueOf(caracter)) * total;
            }
            output *= Math.random();
            output %= 37;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return String.valueOf(abecedario.charAt(output));
    }
}
