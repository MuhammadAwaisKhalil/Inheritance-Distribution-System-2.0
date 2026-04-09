package org.example.demo.User;

public class UserSession {
    private static String currentUsername;
    private static String currentUserEmail;
    private static int currentUserId;

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
}
