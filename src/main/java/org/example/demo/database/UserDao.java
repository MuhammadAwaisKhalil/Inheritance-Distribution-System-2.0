package org.example.demo.database;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

    public static void writeUserToDatabase(String userName, String email, String pass, LocalDate dob1, Integer pId){
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

            }
        }
        catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database insertion failed for user: " + userName, ex);
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
            return 0;
        }
    }
}