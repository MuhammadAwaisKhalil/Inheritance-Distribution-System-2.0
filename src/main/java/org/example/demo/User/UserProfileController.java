package org.example.demo.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.example.demo.database.UserDao;

import java.io.IOException;

public class UserProfileController {

    @FXML
    private Circle imageCircle;

    @FXML
    private TextField userNameTextbox, emailTextbox;

    @FXML
    private Label userNameLabel, DateLabel, emailLabel, accountName;

    @FXML
    private DatePicker dateTextBox;

    @FXML
    public void initialize(){
        Image img = new Image(getClass().getResourceAsStream("/org/example/demo/images/blank-profile-400x400-ffffff-circle-user1-80-402956.png"));
        imageCircle.setFill(new ImagePattern(img));
        populateFields();
    }

    public void populateFields(){
        userNameLabel.setText("@" + UserSession.getCurrentUsername());
        accountName.setText(UserSession.getCurrentUsername());
        emailLabel.setText(UserSession.getCurrentUserEmail());
        if(UserSession.getCurrentDateOfBirth()!=null)
            DateLabel.setText(UserSession.getCurrentDateOfBirth().toString());
        else{
            DateLabel.setText("Date of birth not Assigned Yet!");
        }
    }

    @FXML
    private void handleSave(ActionEvent e){
        if(!emailTextbox.getText().isEmpty()) {
            if (!emailTextbox.getText().contains("@")) {
                showAlert("Please enter a vaild email address");
                return;
            }else if (!emailTextbox.getText().equals(UserSession.getCurrentUserEmail())) {
                if (UserDao.updateUserEmail(emailTextbox.getText(), UserSession.getCurrentUserEmail())) {
                    UserSession.setCurrentUserEmail(emailTextbox.getText());
                    showAlert("Email updated successfully");
                } else {
                    showAlert("Error updating email");
                }
            }
        }

        if (!userNameTextbox.getText().isEmpty() && !userNameTextbox.getText().equals(UserSession.getCurrentUsername())){
            if (UserDao.updateUserName(userNameTextbox.getText(), UserSession.getCurrentUsername())) {
                UserSession.setCurrentUsername(userNameTextbox.getText());
                showAlert("Username updated successfully");
            }else {
                showAlert("Error updating username");
            }
        }

        if (!userNameTextbox.getText().isEmpty() && !userNameTextbox.getText().equals(UserSession.getCurrentUsername())){
            if (UserDao.updateUserName(userNameTextbox.getText(), UserSession.getCurrentUsername())) {
                UserSession.setCurrentUsername(userNameTextbox.getText());
                showAlert("Username updated successfully");
            }else {
                showAlert("Error updating username");
            }
        }

        if (dateTextBox.getValue() != null && !dateTextBox.getValue().equals(UserSession.getCurrentDateOfBirth())) {
            if (UserDao.updateUserDob(UserSession.getCurrentUserId(), dateTextBox.getValue())){
                UserSession.setCurrentDateOfBirth(dateTextBox.getValue());
                showAlert("Date of Birth updated successfully");
            }else {
                showAlert("Error updating date of birth");
            }
        }


    }

    private void showAlert(String msg){
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.showAndWait();
    }
    @FXML
    private void goToAnalytics(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/Dashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");
        stage.show();
    }
    @FXML
    private void gotoInheritor(ActionEvent e)throws IOException{
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/demo/inheritors-detail.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root);
        //stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Inheritors");
        stage.setScene(scene);
        stage.show();

    }

}
