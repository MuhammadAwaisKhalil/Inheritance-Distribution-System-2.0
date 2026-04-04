package org.example.demo;


import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class onDatabase {
    private static final Logger LOGGER = Logger.getLogger(onDatabase.class.getName());

    public static void writeUserToDatabase(String userName, String email, String pass, LocalDate dob1, Integer pId){
        String url = "jdbc:postgresql://ep-red-brook-anoa6kbb.c-6.us-east-1.aws.neon.tech/neondb?sslmode=require";
        String user = "neondb_owner";
        String password = "npg_zPdho9GrU1HO";


        String query = "Insert into accounts(user_name, password, date_of_birth, email,parent ) VALUES(?, ?, ?, ?, ?)";

        try(Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = con.prepareStatement(query);){

            pst.setString(1,userName);
            pst.setString(2,pass);
            pst.setObject(3, dob1);
            pst.setString(4,email);

            if (pId == null ){
                pst.setNull(5, Types.INTEGER);
            }else{
                pst.setInt(5,pId);
            }

            pst.executeUpdate();

            System.out.println("Successfully created");

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database insertion failed for user: " + userName, ex);
        }
    }
}
