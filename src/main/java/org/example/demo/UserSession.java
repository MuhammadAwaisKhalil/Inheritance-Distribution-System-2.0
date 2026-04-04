package org.example.demo;

public class UserSession {
    private static String currentUsername;
    private static String currentUserEmail;

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


}
