package com.gdb.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.io.*;

public class SimpleFileEncryptor {
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1234567890123456"; // 16-byte key (Secure in production)
    private static final String FILE_NAME = "JDBC Config File.txt"; // Persistent file

    // 🔹 Encrypt text
    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 🔹 Decrypt text
    public static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // 🔹 Save text to file (Append if already exists)
    public static void saveToFile(String data) throws IOException {
        File file = new File(FILE_NAME);

        // Only create a new file if it doesn't exist
        if (!file.exists()) {
            file.createNewFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) { // `false` overwrites
            writer.write(data);
            writer.newLine();
        }
    }

    // 🔹 Read text from file
    public static String readFromFile() throws IOException {
        File file = new File(FILE_NAME);
        
        if (!file.exists()) {
            return "File does not exist! Please write data first.";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        }
    }
}
