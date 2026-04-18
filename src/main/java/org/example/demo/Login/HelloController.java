package org.example.demo.Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.demo.Inheritors.Inheritor;
import org.example.demo.Inheritors.InheritorProperty;
import org.example.demo.Property.Property;
import org.example.demo.User.User;
import org.example.demo.User.UserSession;
import org.example.demo.database.InheritorDao;
import org.example.demo.database.PropertyDao;
import org.example.demo.database.UserDao;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/Dashboard.fxml"));

        try {
            this.username = usernameField1.getText();
            this.password = passwordField1.getText();
            try{
                if(username!=null && password!=null){

                    int userID = UserDao.getIdByEmail(this.username);

                    if(userID>0){
                        User u = UserDao.getCurrentUserInfo(userID);
                        if(u!=null){
                            UserSession.setCurrentUsername(u.getUsername());
                            UserSession.setCurrentUserEmail(u.getEmail());
                            if(u.getDate_of_birth()!=null) {
                                UserSession.setCurrentDateOfBirth(u.getDate_of_birth());
                            }
                            else{
                                UserSession.setCurrentDateOfBirth(LocalDate.now());
                            }
                            System.out.println("User Session Initialized");
                        }
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
                System.out.println("Login me errror hai waleeeed"+e.getMessage());
                System.out.println("THE REAL CAUSE IS: " + e.getCause());
                e.printStackTrace();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/property_and_inheritor.fxml"));

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
                    UserSession.setCurrentUsername(username);
                    UserSession.setCurrentUserEmail(email);
                    UserSession.setCurrentDateOfBirth(dateOfBirth);

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

    private void notifyAllUsers(int parentId){
        List<Inheritor> allInheritors = InheritorDao.getAllInheritors(parentId);

        for (Inheritor inheritor: allInheritors){
            try{
                sendEmail(inheritor);
            }catch (MessagingException e){
                Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    private void sendEmail(Inheritor inheritor) throws MessagingException{
        String email = UserDao.getUserEmailFromId(inheritor.getId());

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        String myemail = "sawasta556@gmail.com";
        String mypassword = "zqli ejyg totp txwc";

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myemail, mypassword);
            }
        });

        String message1 = buildEmailContent(inheritor);
        Message message = prepareMessage(session, myemail, email, message1);

        if (message != null) {
            Transport.send(message);
            new Alert(Alert.AlertType.INFORMATION, "Send Email Success").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Please try later").show();
        }
    }

    private Message prepareMessage(Session session, String from, String to, String message) {
        try{
            Message message1 = new MimeMessage(session);
            message1.setFrom(new InternetAddress(from));
            message1.setRecipients(Message.RecipientType.TO, new InternetAddress[]{
                    new InternetAddress(to)
            });

            message1.setSubject("Important! Property Distributed");
            message1.setText(message);
            return message1;
        } catch (Exception e) {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, e);
        }

        return null;
    }

    private String buildEmailContent(Inheritor inheritor) {
        StringBuilder sb = new StringBuilder();

        sb.append("Dear ").append(inheritor.getName()).append(",\n\n");
        sb.append("We are pleased to inform you that your inheritor has been added to our system.\n\n");
        sb.append("Inherited Property: \n");
        sb.append("----------------------------------------\n");

        double totalValue = inheritor.calculateTotalValue();
        for(InheritorProperty p: inheritor.getProperties()){
            sb.append("Property: ").append(p.getPropertyName()).append("\n");
            sb.append("Valuation: ").append(p.getWorth()).append("\n");;
            sb.append("Share: ").append(p.getPortionGiven()).append("%\n\n");
        }

        sb.append("----------------------------------------\n");

        sb.append("Total Value: ").append(totalValue).append("\n");
        sb.append("Please log in to the app for more details.\n\n");
        sb.append("Regards,\nYour App Team");

        return sb.toString();
    }

    private void addPropertyToInheritor(int parentId){
        List<Inheritor> allInheritors = InheritorDao.getAllInheritors(parentId);
        for (Inheritor inheritor: allInheritors){
            for(InheritorProperty p: inheritor.getProperties()){
                if (PropertyDao.addProperty(p.getPropertyName(), (int)p.getAbsoluteValue(), inheritor.getId())){
                    System.out.println("Property added to inheritor");
                }else {
                    System.out.println("Property not added to inheritor");
                }
            }
        }

    }

}
