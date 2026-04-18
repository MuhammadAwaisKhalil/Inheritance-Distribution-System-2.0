package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.demo.Property.Property;
import org.example.demo.User.UserSession;
import org.example.demo.database.InheritorDao;
import org.example.demo.database.PropertyDao;
import org.example.demo.database.UserDao;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddInheritorPopUp implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Property> propertyComboBox;
    @FXML
    private TextField shareField;

    private String username;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String share;
    Property Assignproperty;


    @FXML
    private void addInheritor(ActionEvent e){
        this.username=usernameField.getText();
        this.email=emailField.getText();
        this.password=passwordField.getText();
        this.dateOfBirth=datePicker.getValue();
        this.share=shareField.getText();
        this.Assignproperty = propertyComboBox.getValue();
        if (username==null||email==null||password==null||dateOfBirth==null&&(share==null || Assignproperty==null)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Details");
            alert.setHeaderText("");
            alert.setContentText("Error! Invalid credentials added!");
            alert.showAndWait();
        }
        else if(!username.trim().isEmpty()&&!email.trim().isEmpty()&&!password.trim().isEmpty()){
            try {
                if(UserDao.writeUserToDatabase(username, email, password, dateOfBirth, UserSession.getCurrentUserId())){
                    if(share==null && Assignproperty==null){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Inheritor added");
                        alert.setContentText("Inheritor added without inheritance assignment!");
                        alert.showAndWait();
                        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        stage.close();

                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Entry");
                        alert.setContentText("Invalid inheritance assignment!");
                        alert.showAndWait();
                    }
                    double shareVal = Double.parseDouble(share);
                    try {
                        Assignproperty = propertyComboBox.getValue();


                        InheritorDao.assignProperty(PropertyDao.getPropertyId(Assignproperty.getProperty_name(), UserSession.getCurrentUserId()), UserDao.getIdByEmail(email), shareVal);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Inheritance Assigned");
                        alert.setContentText("Inheritance successfully assigned");
                        alert.showAndWait();
                        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        stage.close();

                    }
                    catch(Exception ex){
                        System.out.println(ex.getMessage()+" IT DO NOT WAANT MERA JAIDAT TWO SOCKS 1 Binyan");
                    }

                }
            } catch (Exception ex) {
                System.out.println("Dis wong no  assign wirasat");
            }
        }

    }
    @FXML
    private void cancelButton(ActionEvent e){
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Property> properties = PropertyDao.allProperties(UserSession.getCurrentUserId());
        propertyComboBox.getItems().addAll(properties);


    }
}
