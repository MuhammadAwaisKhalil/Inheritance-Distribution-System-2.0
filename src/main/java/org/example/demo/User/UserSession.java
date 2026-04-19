package org.example.demo.User;

import org.example.demo.database.UserDao;

import java.time.LocalDate;

public class UserSession {
    private static String currentUsername;
    private static String currentUserEmail;
    private static int currentUserId;
    private static LocalDate currentDateOfBirth;


    public static void setCurrentUsername(String username){
        currentUsername=username;
    }
    public static void setCurrentUserEmail(String email){
        currentUserEmail=email;
    }
    public static String getCurrentUsername(){
        return currentUsername;
    }
    public static String getCurrentUserEmail(){
        return currentUserEmail;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }
    public static void setCurrentUserId(int id){
        currentUserId=id;
    }

    public static void setCurrentDateOfBirth(LocalDate currentDateOfBirth) {
        UserSession.currentDateOfBirth = currentDateOfBirth;
    }

    public static LocalDate getCurrentDateOfBirth() {
        return currentDateOfBirth;
    }





}
