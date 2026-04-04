package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;

public class AssetsController {
    @FXML
    private ComboBox<Property> propetyComboBox;
    @FXML
    private ListView<Property> propertyList;
    @FXML
    private ComboBox<User> userComboBox;
    @FXML
    private ListView<User> userList;
    @FXML
    private TextField shareField;

    private Property addedProperty;
    private User addedUser;

    @FXML
    private void addProperty(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("propertyPopup.fxml"));
        Parent root = loader.load();
        PropertyPopupController controller = loader.getController();

        Stage addPropertyStage = new Stage();
        addPropertyStage.setTitle("Add Property");
        addPropertyStage.setScene(new Scene(root));

        addPropertyStage.initModality(Modality.APPLICATION_MODAL);

        addPropertyStage.showAndWait();


        addedProperty=controller.getNewProperty();

        if(addedProperty!=null){
            propetyComboBox.getItems().add(addedProperty);

            propertyList.getItems().add(addedProperty);
        }

    }
    @FXML
    private void addUser(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userPopup.fxml"));
        Parent root = loader.load();

        UserPopupController controller = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Add Inheritor");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();

        addedUser=controller.getCurrentUser();

        if(addedUser!=null){
            userComboBox.getItems().add(addedUser);

            userList.getItems().add(addedUser);
        }
    }
    @FXML
    private void assignInheritor(){
        Property currentProperty = propetyComboBox.getValue();
        User currentInheritor = userComboBox.getValue();

        String share = shareField.getText();
        try {
            if (currentProperty != null && currentInheritor != null && !share.trim().isEmpty()) {
                double sharePercentage = Double.parseDouble(share);


                // Extract Ids using method amd give to database


                System.out.println("LINKED MEE PAA ZOO ZOO ZOO A ZOO ZOO");


            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Could not load data. Please enter all credentials!");
            }
        }
        catch (Exception e){
            System.out.println("LHELHD?HD WALEED");
        }
    }

}
