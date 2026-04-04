package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PropertyPopupController {
    @FXML
    private TextField propertyNameField;
    @FXML
    private TextField valuationField;

    private Property newProperty;

    @FXML
    private void doneClick(ActionEvent event){
        String propertyName = propertyNameField.getText();
        String valuation = valuationField.getText();
        try {
            if (!propertyName.trim().isEmpty() || !valuation.trim().isEmpty()) {


                double value = Double.parseDouble(valuation);
                newProperty = new Property(propertyName, value);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Property name and valuation are mandatory fields");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.out.println("Property popup has error");
        }

    }
    @FXML
    private void CancelClick(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public Property getNewProperty() {
        return newProperty;
    }
}
