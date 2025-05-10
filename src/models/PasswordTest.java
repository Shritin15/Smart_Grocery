package models;

import utils.PasswordHashingUtils;

public class PasswordTest {
    public static void main(String[] args) {
        String plainPassword = "admin123";
        String hashedPassword = PasswordHashingUtils.hashPassword(plainPassword);
        System.out.println("Hashed password for '" + plainPassword + "' is:");
        System.out.println(hashedPassword);
    }
}
