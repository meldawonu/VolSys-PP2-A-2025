package utils;

import java.util.regex.Pattern;

/**
 * Utility class untuk validasi input
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    
    /**
     * Validasi apakah string tidak kosong
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Validasi format email
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validasi password (minimal 6 karakter)
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    /**
     * Validasi apakah string bisa diparse menjadi integer
     */
    public static boolean isValidInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Mendapatkan pesan error untuk field kosong
     */
    public static String getEmptyFieldMessage(String fieldName) {
        return fieldName + " tidak boleh kosong!";
    }
    
    /**
     * Mendapatkan pesan error untuk email tidak valid
     */
    public static String getInvalidEmailMessage() {
        return "Format email tidak valid!";
    }
    
    /**
     * Mendapatkan pesan error untuk password tidak valid
     */
    public static String getInvalidPasswordMessage() {
        return "Password minimal 6 karakter!";
    }
}
