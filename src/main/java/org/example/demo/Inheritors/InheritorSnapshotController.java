package org.example.demo.Inheritors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.demo.User.UserSession;
import org.example.demo.database.InheritorDao;
import org.example.demo.database.PropertyDao;

import java.io.IOException;
import java.lang.classfile.Label;
import java.util.List;

public class InheritorSnapshotController {
    @FXML
    private javafx.scene.control.Label totalWorthLabel;
    @FXML
    private Accordion inheritorsAccordion;

    private double totalWorth = 0;

    @FXML
    public void initialize(){
        loadData();
    }

    private void loadData(){
    totalWorth = PropertyDao.getTotalworth(UserSession.getCurrentUserId());
        List<Inheritor> inheritors = InheritorDao.getAllInheritors(UserSession.getCurrentUserId());

        totalWorthLabel.setText(String.format("PKR %,.0f", totalWorth));
        buildAccordion(inheritors);
    }

    private void buildAccordion(List<Inheritor> inheritors){
        inheritorsAccordion.getPanes().clear();

        for (Inheritor inheritor: inheritors){
            TitledPane pane = buildInheritorPane(inheritor);
            inheritorsAccordion.getPanes().add(pane);
        }
    }

    private TitledPane buildInheritorPane(Inheritor inheritor){
        double sharePercent = inheritor.calculateTotalSharePercent(totalWorth);
        double shareValue = inheritor.calculateTotalValue();

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        javafx.scene.control.Label nameLabel = new javafx.scene.control.Label(inheritor.getName());
        nameLabel.getStyleClass().add("inheritor-name");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        javafx.scene.control.Label shareLabel = new javafx.scene.control.Label(String.format("%.1f%%  •  PKR %,.0f", sharePercent, shareValue));
        shareLabel.getStyleClass().add("inheritor-share");

        header.getChildren().addAll(nameLabel, spacer, shareLabel);

        VBox body = new VBox(6);
        body.getStyleClass().add("property-list");
        body.setPadding(new javafx.geometry.Insets(10,16,10,16));

        for (InheritorProperty prop: inheritor.getProperties()){
            HBox row = buildPropertyRow(prop);
            body.getChildren().add(row);
        }

        TitledPane pane = new TitledPane();
        pane.setGraphic(header);
        pane.setText("");
        pane.setContent(body);
        pane.getStyleClass().add("inheritor-pane");

        return pane;

    }

    private HBox buildPropertyRow(InheritorProperty prop){
        HBox row = new HBox(10);
        row.getStyleClass().add("property-row");
        row.setAlignment(Pos.CENTER_LEFT);

        javafx.scene.control.Label propName = new javafx.scene.control.Label(prop.getPropertyName());
        propName.getStyleClass().add("prop-name");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        javafx.scene.control.Label portionLabel = new javafx.scene.control.Label(String.format("%.0f%%", prop.getPortionGiven()));
        portionLabel.getStyleClass().add("prop-portion");

        javafx.scene.control.Label valueLabel = new javafx.scene.control.Label(String.format("PKR %,.0f", prop.getAbsoluteValue()));
        valueLabel.getStyleClass().add("prop-value");

        row.getChildren().addAll(propName, spacer, portionLabel, valueLabel);
        return row;

    }


    @FXML
    private void openDetailedView(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/demo/inheritors-detail.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.NONE);
            stage.setTitle("Inheritors-Detailed View");
            stage.setScene(new Scene(loader.load(), 860, 620));
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private List<Inheritor> getSampleData() {
        Inheritor a = new Inheritor(1, "Ahmed Khan");
        a.getProperties().add(new InheritorProperty(1, "House Gulberg", 2000000, 50));
        a.getProperties().add(new InheritorProperty(2, "Shop DHA", 3000000, 0));

        Inheritor b = new Inheritor(2, "Fatima Khan");
        b.getProperties().add(new InheritorProperty(1, "House Gulberg", 2000000, 50));
        b.getProperties().add(new InheritorProperty(2, "Shop DHA", 3000000, 100));

        return List.of(a, b);
    }


}
