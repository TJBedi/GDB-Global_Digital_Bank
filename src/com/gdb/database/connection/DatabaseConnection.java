package com.gdb.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.gdb.util.SimpleFileEncryptor;

public class DatabaseConnection {
    // Encrypted credentials (Use SimpleFileEncryptor.encrypt() to generate these)
    private static final String ENCRYPTED_URL = "pfxSphs3qHtr+PA34DmPwWBjdn+wqFbYge4XZNDGlgg=";
    private static final String ENCRYPTED_USER = "3jgmUpRuHqoLmF418AV7Sw==";
    private static final String ENCRYPTED_PASSWORD = "j0iq5AaYrbHN4CLggQMr+A==";

    public static Connection getConnection() throws SQLException {
        try {
            // Decrypt credentials before use
            String url = SimpleFileEncryptor.decrypt(ENCRYPTED_URL);
            String user = SimpleFileEncryptor.decrypt(ENCRYPTED_USER);
            String password = SimpleFileEncryptor.decrypt(ENCRYPTED_PASSWORD);
            Connection con =  DriverManager.getConnection(url, user, password);
            return con;
        } catch (Exception e) {
            throw new SQLException("Error decrypting database credentials", e);
        }
    }
}
