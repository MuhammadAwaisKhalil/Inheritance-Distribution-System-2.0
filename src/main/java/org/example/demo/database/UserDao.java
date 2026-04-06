package org.example.demo.database;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

    public static boolean writeUserToDatabase(String userName, String email, String pass, LocalDate dob1, Integer pId){
        String query = "Insert into accounts(user_name, password, date_of_birth, email,parent ) VALUES(?, ?, ?, ?, ?)";
        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query);){
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
                return true;

            }
        }
        catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database insertion failed for user: " + userName, ex);
            return false;
        }
    }


    public static int getIdByEmail(String userEmail){
        String query = "select id from accounts where email = ?";
        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)){
                pst.setString(1, userEmail);

                try(ResultSet rs = pst.executeQuery()){
                    if(rs.next()){
                        int userId = rs.getInt("id");
                        return userId;
                    }else {
                        LOGGER.log(Level.WARNING, "No user found for email: " + userEmail);
                        return 0;
                    }
                }
            }
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Database ID read failed for: " + userEmail, ex);
            return -1;
        }
    }

    public static boolean deleteUserById(int id){
        String query = "DELETE FROM accounts where id = (?)";

        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1,id);

                pst.executeUpdate();
                System.out.println("User "+id+" deleted successfully");
                return true;
            }
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Error deleting user: "+ id,ex);
            return false;
        }
    }

    public static boolean deleteUserByEmail(String userEmail){
        String query = "DELETE FROM accounts where email = (?)";

        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, userEmail);

                pst.executeUpdate();
                System.out.println("User "+userEmail+" deleted successfully");
                return true;
            }
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Error deleting user: "+ userEmail,ex);
            return false;
        }
    }




}