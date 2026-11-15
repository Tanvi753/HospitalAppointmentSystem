package com.careconnect.db;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        try {
            Connection connection = DBConnection.getConnection();

            System.out.println("================================");
            System.out.println("DATABASE CONNECTED SUCCESSFULLY!");
            System.out.println("================================");

            connection.close();

        } catch (Exception e) {

            System.out.println("DATABASE CONNECTION FAILED!");
            e.printStackTrace();
        }
    }
}