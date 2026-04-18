package org.example.demo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static final String URL = "jdbc:postgresql://ep-red-brook-anoa6kbb.c-6.us-east-1.aws.neon.tech/neondb?sslmode=require"
            ;
    //jdbc:postgresql://ep-red-brook-anoa6kbb.c-6.us-east-1.aws.neon.tech/neondb?sslmode=require
    private static final String user = "neondb_owner";
    private static final String password = "npg_zPdho9GrU1HO";

    private static Connection connection;

    public static Connection getConnection() throws SQLException{
        if (connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(URL, user, password);
        }
        return connection;
    }
}