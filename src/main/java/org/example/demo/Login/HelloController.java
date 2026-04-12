package org.example.demo.Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.demo.Property.Property;
import org.example.demo.User.UserSession;
import org.example.demo.database.UserDao;

import java.io.IOException;
import java.time.LocalDate;

public class HelloController {
    //-------------------FXML VARS-------------------
    @FXML
    private TextField usernameField2;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField2;
    @FXML
    private DatePicker dateOfBirthField;
    @FXML
    private TextField usernameField1;
    @FXML
    private PasswordField passwordField1;
    @FXML
    private ComboBox<Property> propertyCombo;
    @FXML
    private AnchorPane mainlayout;
    //---------------------DATA VARS---------------------
    private String username;
    private String email;
    private String password;
    private LocalDate dateOfBirth;

    @FXML
    private void registerUser(){
        try {
            this.username = usernameField2.getText();
            UserSession.setCurrentUsername(username);
            this.email = emailField.getText();
            this.password = passwordField2.getText();
            this.dateOfBirth = dateOfBirthField.getValue();

            System.out.println("Username: "+username);
            System.out.println("Password:"+password);
            System.out.println("Email:"+email);
            System.out.println("Date: "+dateOfBirth.toString());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    @FXML
    private void userLogin() throws IOException {

        Stage s = (Stage)usernameField1.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));

        try {
            this.username = usernameField1.getText();
            this.password = passwordField1.getText();
            try{
                if(username!=null && password!=null){

                    int userID = UserDao.getIdByEmail(this.username);
                    if(userID>0){
                        System.out.println("Logged IN");
                        UserSession.setCurrentUserId(userID);
                        System.out.println("Username: "+username);
                        System.out.println("Password:"+password);
                        Parent root = loader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Dashboard");
                        stage.setScene(new Scene(root));
                        s.close();
                        stage.show();

                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("No User");
                        alert.setContentText("No User Found. Please check your credentials.");
                        username=null;
                        password=null;
                        alert.showAndWait();
                    }
                }
            } catch (Exception e) {
                System.out.println("Login me errror hai waleeeed");
            }
        } catch (Exception e) {
            System.out.println("Sometinh wong");
        }

    }
    @FXML
    private void switchToRegister() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("/org/example/demo/loginPage.fxml"));
        Scene newLoginScene = new Scene(loader.load(),350,451);
        Stage stage = (Stage)usernameField1.getScene().getWindow();
        System.out.println("Scene switched");
        stage.setScene(newLoginScene);


    }

    @FXML
    private void addAssetandInheritorData() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("property_and_inheritor.fxml"));

        try{
            this.username=usernameField2.getText();
            this.email=emailField.getText();
            this.password=passwordField2.getText();
            this.dateOfBirth = dateOfBirthField.getValue();

            try{
                if(username!=null && email!=null&&password!=null&&dateOfBirth!=null){
                    UserDao.writeUserToDatabase(username,email,password,dateOfBirth,null);
                    System.out.println("User added to db");
                    Scene scene = new Scene(loader.load(),679,625);
                    Stage assetStage = new Stage();
                    assetStage.setTitle("Add Assets");
                    assetStage.setScene(scene);
                    assetStage.show();
                    int id=UserDao.getIdByEmail(email);
                    UserSession.setCurrentUserId(id);

                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Registration Error");
                    alert.setContentText("Please enter all credentials");
                    username=null;
                    password=null;
                    email=null;
                    dateOfBirth=null;
                    alert.showAndWait();

                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("The registere screen aint workin dawg");
        }
    }


}
