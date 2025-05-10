package utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PasswordHashingUtils {


    // method to hash a password using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b)); // converts byte to hex
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }


    // method to verify a plain password against a stored hashed password
    public static boolean verifyPassword(String inputPassword, String storedHashedPassword) {
        return hashPassword(inputPassword).equals(storedHashedPassword);
    }
}

