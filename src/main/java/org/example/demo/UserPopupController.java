package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.example.demo.database.UserDao;

import java.time.LocalDate;



public class UserPopupController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private DatePicker dobPicker;

    User addUser;

    @FXML
    private void clickDone(ActionEvent event){
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        LocalDate dateOfBirth = dobPicker.getValue();

        try {
            if (!username.trim().isEmpty() && !email.trim().isEmpty() && !password.trim().isEmpty() && dateOfBirth != null) {

                UserDao.writeUserToDatabase(username,email,password,dateOfBirth,UserSession.getCurrentUserId());
                addUser = new User(username, email, password, dateOfBirth);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("All fields are mandatory to fill!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.out.println("Koni chi wa im stuck in the user");
        }
    }
    @FXML
    private void clickCancel(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    public User getCurrentUser(){
        return addUser;
    }
}
