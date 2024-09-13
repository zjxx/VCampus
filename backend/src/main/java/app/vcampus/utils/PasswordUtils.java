package app.vcampus.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for password-related operations.
 */
public class PasswordUtils {

    /**
     * Hashes a password using the MD5 algorithm.
     *
     * @param password the password to hash
     * @return the hashed password in hexadecimal format
     * @throws RuntimeException if the MD5 algorithm is not available
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // Get MD5 message digest instance
            byte[] hash = md.digest(password.getBytes()); // Compute the hash
            StringBuilder hexString = new StringBuilder(); // Convert hash bytes to hex format
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString(); // Return the hashed password
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // Wrap and rethrow the exception as a runtime exception
        }
    }
}