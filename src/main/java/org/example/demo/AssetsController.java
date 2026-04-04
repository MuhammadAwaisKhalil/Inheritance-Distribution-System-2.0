package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
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
    private void addUser(ActionEvent event){

    }
}
