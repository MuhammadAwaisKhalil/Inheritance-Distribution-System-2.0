package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {



    @FXML
    private void addAsset()throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("propertyPopup.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Asset");
        stage.setScene(scene);
        stage.showAndWait();

    }
    @FXML
    private void addInheritor()throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AssignInheritor.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Inheritor");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }


}
