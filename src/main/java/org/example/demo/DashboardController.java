package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private VBox inheritorSnapshotContainer;

    @FXML
    public void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/demo/inheritorsSnapshot.fxml")
        );
        Parent snapshotView = loader.load();
        VBox.setVgrow(snapshotView, Priority.ALWAYS);
        inheritorSnapshotContainer.getChildren().add(snapshotView);
    }


    @FXML
    private void addAsset()throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/propertyPopup.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Asset");
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    private void addInheritor()throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/AssignInheritor.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Inheritor");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void gotoProfile(ActionEvent e)throws IOException{
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
       FXMLLoader loader = new FXMLLoader(
               getClass().getResource("/org/example/demo/UserProfile.fxml")
       );

        Parent root = loader.load();

        Scene scene = new Scene(root);
        //stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Profile");
        stage.setScene(scene);
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
    @FXML
    private void goToAnalytics(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/Dashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");
        stage.show();
    }


}
