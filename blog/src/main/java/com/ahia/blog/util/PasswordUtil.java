package com.ahia.blog.util;


import org.jasypt.util.password.StrongPasswordEncryptor;

public class PasswordUtil {
    private static final StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();


    public static String hashPassword(String password) {
        return encryptor.encryptPassword(password);
    }

    public static boolean verifyPassword(String rawPassword, String encryptedPassword) {
        return encryptor.checkPassword(rawPassword, encryptedPassword);
    }
}
