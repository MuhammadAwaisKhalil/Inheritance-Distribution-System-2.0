package org.example.demo.Inheritors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.demo.User.UserSession;
import org.example.demo.database.InheritorDao;
import org.example.demo.database.PropertyDao;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InheritorsDetailController {

    @FXML private Label totalWorthLabel;
    @FXML private Label inheritorCountLabel;
    @FXML private Accordion inheritorsAccordion;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortCombo;

    private ObservableList<Inheritor> allInheritors = FXCollections.observableArrayList();
    private double totalWorth = 0;

    @FXML
    public void initialize() {
        sortCombo.setItems(FXCollections.observableArrayList(
                "Name (A→Z)", "Name (Z→A)", "Share (High→Low)", "Share (Low→High)"
        ));

        // Live search
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        sortCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        loadData();
    }

    private void loadData() {

        totalWorth = PropertyDao.getTotalworth(UserSession.getCurrentUserId());
        allInheritors.setAll(InheritorDao.getAllInheritors(UserSession.getCurrentUserId()));

        totalWorthLabel.setText(String.format("PKR %,.0f", totalWorth));
        applyFilters();
    }

    private void applyFilters() {
        String query = searchField.getText().toLowerCase().trim();

        List<Inheritor> filtered = allInheritors.stream()
                .filter(i -> query.isEmpty()
                        || i.getName().toLowerCase().contains(query))
                .collect(Collectors.toList());

        String sort = sortCombo.getValue();
        if (sort != null) {
            switch (sort) {
                case "Name (A→Z)"        -> filtered.sort(Comparator.comparing(Inheritor::getName));
                case "Name (Z→A)"        -> filtered.sort(Comparator.comparing(Inheritor::getName).reversed());
                case "Share (High→Low)"  -> filtered.sort(Comparator.comparingDouble(
                        i -> -i.calculateTotalSharePercent(totalWorth)));
                case "Share (Low→High)"  -> filtered.sort(Comparator.comparingDouble(
                        i ->  i.calculateTotalSharePercent(totalWorth)));
            }
        }

        buildAccordion(filtered);
        inheritorCountLabel.setText(filtered.size() + " Inheritor" + (filtered.size() != 1 ? "s" : ""));
    }

    private void buildAccordion(List<Inheritor> inheritors) {
        inheritorsAccordion.getPanes().clear();
        for (Inheritor inheritor : inheritors) {
            inheritorsAccordion.getPanes().add(buildInheritorPane(inheritor));
        }
    }

    private TitledPane buildInheritorPane(Inheritor inheritor) {
        double sharePercent = inheritor.calculateTotalSharePercent(totalWorth);
        double shareValue   = inheritor.calculateTotalValue();

        // Header
        HBox header = new HBox(10);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label nameLabel     = new Label(inheritor.getName());     nameLabel.getStyleClass().add("inheritor-name");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        Label shareLabel = new Label(String.format("%.1f%%  •  PKR %,.0f", sharePercent, shareValue));
        shareLabel.getStyleClass().add("inheritor-share");
        header.getChildren().addAll(nameLabel, spacer, shareLabel);

        // Body with property rows + action buttons
        VBox body = new VBox(6);
        body.getStyleClass().add("property-list");
        body.setPadding(new javafx.geometry.Insets(10, 16, 14, 16));

        for (InheritorProperty prop : inheritor.getProperties()) {
            body.getChildren().add(buildDetailPropertyRow(inheritor, prop, body));
        }

        // "Assign Property" button inside expanded pane
        Button assignBtn = new Button("+ Assign Property");
        assignBtn.getStyleClass().add("assign-btn");
        assignBtn.setOnAction(e -> openAssignPropertyDialog(inheritor, body));
        body.getChildren().add(assignBtn);

        TitledPane pane = new TitledPane();
        pane.setGraphic(header);
        pane.setText("");
        pane.setContent(body);
        pane.getStyleClass().add("inheritor-pane");
        return pane;
    }

    private HBox buildDetailPropertyRow(Inheritor inheritor, InheritorProperty prop, VBox parentBody) {
        HBox row = new HBox(10);
        row.getStyleClass().add("property-row");
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label propName    = new Label(prop.getPropertyName()); propName.getStyleClass().add("prop-name");
        Region spacer     = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        Label portionLbl  = new Label(String.format("%.0f%%", prop.getPortionGiven())); portionLbl.getStyleClass().add("prop-portion");
        Label valueLbl    = new Label(String.format("PKR %,.0f", prop.getAbsoluteValue())); valueLbl.getStyleClass().add("prop-value");

        Button editBtn    = new Button("Edit");   editBtn.getStyleClass().add("row-btn-edit");
        Button removeBtn  = new Button("Remove"); removeBtn.getStyleClass().add("row-btn-remove");

        editBtn.setOnAction(e -> openEditPortionDialog(inheritor, prop, portionLbl, valueLbl));
        removeBtn.setOnAction(e -> {
            inheritor.getProperties().remove(prop);
            parentBody.getChildren().remove(row);
            //InheritorDAO.removePropertyFromInheritor(inheritor.getId(), prop.getPropertyId());
            InheritorDao.removePropertyFromInheritor(inheritor.getId(), prop.getPropertyId());
        });

        row.getChildren().addAll(propName, spacer, portionLbl, valueLbl, editBtn, removeBtn);
        return row;
    }

    private void openEditPortionDialog(Inheritor inheritor, InheritorProperty prop,
                                       Label portionLbl, Label valueLbl) {
        Dialog<Double> dialog = new Dialog<>();
        dialog.setTitle("Edit Portion — " + prop.getPropertyName());
        dialog.setHeaderText("Adjust " + inheritor.getName() + "'s share of " + prop.getPropertyName());

        Slider slider = new Slider(0, 100, prop.getPortionGiven());
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(25);
        Label valueDisplay = new Label(String.format("%.0f%%", prop.getPortionGiven()));
        slider.valueProperty().addListener((obs, o, n) ->
                valueDisplay.setText(String.format("%.0f%%", n.doubleValue())));

        VBox content = new VBox(10, new Label("Portion:"), slider, valueDisplay);
        content.setPadding(new javafx.geometry.Insets(10));
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(btn -> btn == ButtonType.OK ? slider.getValue() : null);

        dialog.showAndWait().ifPresent(newPortion -> {
            prop.setPortionGiven(newPortion);
            portionLbl.setText(String.format("%.0f%%", newPortion));
            valueLbl.setText(String.format("PKR %,.0f", prop.getAbsoluteValue()));
            // STUB: InheritorDAO.updatePortion(inheritor.getId(), prop.getPropertyId(), newPortion);
            InheritorDao.reassignPropertyFromInheritor(inheritor.getId(), prop.getPropertyId(), newPortion);
        });
    }

    private void openAssignPropertyDialog(Inheritor inheritor, VBox body) {
        // --- STUB: Replace this list with your DAO call ---
        // List<InheritorProperty> availableProperties = PropertyDAO.getPropertiesNotAssignedTo(inheritor.getId());
        List<InheritorProperty> availableProperties = InheritorDao.getPropertiesNotAssignedTo(inheritor.getId(), UserSession.getCurrentUserId());
        // --------------------------------------------------

        if (availableProperties.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "No unassigned properties available.").showAndWait();
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(body.getScene().getWindow());
        dialog.setTitle("Assign Property — " + inheritor.getName());

        VBox root = new VBox(12);
        root.setPadding(new javafx.geometry.Insets(20));

        Label heading = new Label("Select a property and set portion:");
        heading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Property picker
        ComboBox<InheritorProperty> propertyCombo = new ComboBox<>();
        propertyCombo.setMaxWidth(Double.MAX_VALUE);
        propertyCombo.getItems().addAll(availableProperties);
        propertyCombo.setConverter(new javafx.util.StringConverter<>() {
            public String toString(InheritorProperty p) {
                return p == null ? "" : p.getPropertyName() + "  (PKR " + String.format("%,.0f", p.getWorth()) + ")";
            }
            public InheritorProperty fromString(String s) { return null; }
        });

        // Portion slider + live label
        Label portionLabel = new Label("Portion: 0%");
        Slider portionSlider = new Slider(0, 100, 0);
        portionSlider.setShowTickLabels(true);
        portionSlider.setMajorTickUnit(25);
        portionSlider.setSnapToTicks(false);
        portionSlider.valueProperty().addListener((obs, o, n) ->
                portionLabel.setText(String.format("Portion: %.0f%%", n.doubleValue()))
        );

        // Confirm button
        Button confirmBtn = new Button("Assign");
        confirmBtn.setDisable(true);
        confirmBtn.setStyle("-fx-background-color: #F0C040; -fx-font-weight: bold; -fx-cursor: hand;");
        propertyCombo.valueProperty().addListener((obs, o, n) -> confirmBtn.setDisable(n == null));

        confirmBtn.setOnAction(e -> {
            InheritorProperty selected = propertyCombo.getValue();
            selected.setPortionGiven(portionSlider.getValue());
            inheritor.getProperties().add(selected);

            // Add the new row to the expanded pane body (above the assign button)
            HBox newRow = buildDetailPropertyRow(inheritor, selected, body);
            body.getChildren().add(body.getChildren().size() - 1, newRow); // insert before assign btn

            // STUB: InheritorDAO.assignProperty(inheritor.getId(), selected.getPropertyId(), selected.getPortionGiven());
            InheritorDao.assignProperty(selected.getPropertyId(), inheritor.getId(), selected.getPortionGiven());

            dialog.close();
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(10, cancelBtn, confirmBtn);
        buttons.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        root.getChildren().addAll(heading, propertyCombo, portionSlider, portionLabel, buttons);

        dialog.setScene(new Scene(root, 380, 230));
        dialog.showAndWait();
    }

    @FXML
    private void openAddDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/demo/AssignInheritor.fxml")
            );
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(inheritorsAccordion.getScene().getWindow());
            stage.setTitle("Add New Inheritor");
            stage.setScene(new Scene(loader.load(), 400, 300));
            stage.showAndWait();
            loadData(); // Refresh after adding
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToAnalytics(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/Dashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");
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
}