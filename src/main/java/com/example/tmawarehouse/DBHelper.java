package com.example.tmawarehouse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    Connection conn;
    String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    String USER = "c##java";
    String PASS = "admin";
    public DBHelper() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        if (conn != null) {
            System.out.println("Connected to the Oracle DB!");
        } else {
            System.out.println("Failed to make connection! DBHelper");
        }
    }
}
