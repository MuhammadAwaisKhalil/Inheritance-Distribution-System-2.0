package org.example.demo.database;

import org.example.demo.User.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
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

    public static boolean updateUserEmail(String newEmail, String oldEmail){
        String query = "UPDATE accounts SET email = ? where email = ?";
        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)){
                pst.setString(1, newEmail);
                pst.setString(2, oldEmail);

                pst.executeUpdate();
                System.out.println("User email updated successfully");
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user email: "+ oldEmail,e);
            return false;
        }
    }

    public static boolean updateUserName(String newuserName, String oldUserName){
        String query = "UPDATE accounts SET user_name = ? where user_name = ?";
        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)){
                pst.setString(1, newuserName);
                pst.setString(2, oldUserName);

                pst.executeUpdate();
                System.out.println("User name updated successfully");
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user name: "+ oldUserName,e);
            return false;
        }
    }

    public static boolean updateUserDob(int id, LocalDate newDob){
        String query = "UPDATE accounts SET date_of_birth = ? where id = ?";
        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)){
                pst.setObject(1, newDob);
                pst.setInt(2, id);

                pst.executeUpdate();
                System.out.println("User dob updated successfully");
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user dob: "+ id + " " + newDob,e);
            return false;
        }
    } //user_name, password, date_of_birth, email,parent
    public static User getCurrentUserInfo(int id){
        String query = "SELECT user_name, date_of_birth, email, password FROM accounts WHERE id = ?";
        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)){
                pst.setInt(1,id);
                try(ResultSet rs = pst.executeQuery()){
                    String userName = rs.getString("user_name");
                    LocalDate dob =  rs.getDate("date_of_birth").toLocalDate();
                    String email = rs.getString("email");
                    String password = rs.getString("password");

                    return new User(userName,email,password,dob);

                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user info: "+ id + " " + e);
        }
        return null;
    }

    public static List<Integer> allInheritors(int parentId){
        List<Integer> allInheritors = new ArrayList<>();
        String query = "Select id from accounts where parent = ?";

        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)){
                pst.setInt(1,parentId);
                try(ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        allInheritors.add(id);
                    }

                    System.out.println("All inheritors found for user " + parentId);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all inheritors: "+ parentId + " " + e);
        }
        return allInheritors;
    }

    public static String getUserEmailFromId(int id){
        String query = "Select email from accounts where id = ?";
        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)){
                pst.setInt(1,id);
                try(ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String email = rs.getString("email");
                        return email;
                    }
                }
            }
        } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error getting user email: "+ id + " " + e);
        }
        return null;
    }
    public static int getCurrentParentId(int id){
        String query = "SELECT parent FROM accounts WHERE id = ?";
        System.out.println("In parent before try");
        try{
            System.out.println("In parent before connectiion");

            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)){
                System.out.println("In parent before setint");

                pst.setInt(1,id);
                System.out.println("In parent before result");

                try(ResultSet rs = pst.executeQuery()){
                    if(rs.next()) {
                        int parentID = rs.getInt("parent");
                        return parentID;
                    }   

                }
            }

        } catch (SQLException e) {
            System.out.println("Error loadinf db in parent " + e);
        }
        return 0;
    }
    public static void setCurrentLoginTime(int userid){
        LocalDateTime currentDate = LocalDateTime.now();
        Timestamp stamp = Timestamp.valueOf(currentDate);


        String query = "UPDATE accounts SET last_login = ? WHERE id = ? ";
        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)){
                pst.setTimestamp(1,stamp);
                pst.setInt(2,userid);
                int rowsEffected = pst.executeUpdate();

                if(rowsEffected>0){
                    System.out.println("Value updated");
                }
                else{
                    System.out.println("Wongness");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static LocalDate getPastLoginTime(int userid){
        String query = "SELECT last_login FROM accounts WHERE id = ?";
        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)){
                pst.setInt(1,userid);
                try(ResultSet rs = pst.executeQuery()){
                    LocalDate last_login = null;
                    if(rs.next()){
                    last_login = rs.getDate("last_login").toLocalDate();
                    }
                    if(last_login==null){
                        return null;
                    }
                    else{
                        return last_login;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Wongness in the mooness");
        }
        return null;
    }
    public static boolean updateDeathofParent(int parentID){
        String query = "UPDATE accounts SET is_deceased = ? WHERE parent = ?";
        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)){
                pst.setBoolean(1,true);
                pst.setInt(2,parentID);

                int r = pst.executeUpdate();
                if(r>0){
                    return true;
                }
                else{
                    return false;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }







}