package org.example.demo;

import java.time.LocalDate;

public class User {

    private String username;
    private String email;
    private String password;
    private LocalDate date_of_birth;
    private Integer userID;


    User(String username,String email, String password, LocalDate date_of_birth){
        this.userID=userID;
        this.username=username;
        this.email=email;
        this.password=password;
        this.date_of_birth=date_of_birth;

    }

    public Integer getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public String getUsername() {
        return username;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
    @Override
    public String toString(){
        return "";
    }
}
