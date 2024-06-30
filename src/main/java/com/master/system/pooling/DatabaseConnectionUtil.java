package com.master.system.pooling;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionUtil {

    public static Connection getDatabaseConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/connpool";
        String uname = "root";
        String pass = "password";

        return DriverManager.getConnection(url, uname, pass);
    }
}
