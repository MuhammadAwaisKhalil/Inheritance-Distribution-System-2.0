package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.demo.Property.Property;
import org.example.demo.Property.PropertyPopupController;
import org.example.demo.User.UserSession;
import org.example.demo.database.PropertyDao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;

public class DashboardController {

    private static final double MINIMUM_VISIBLE_SLICE_WEIGHT = 0.0001;

    @FXML private VBox inheritorSnapshotContainer;

    @FXML
    public void initialize() throws IOException {
        reloadSnapshotPanel();

        assetPieChart.setLegendSide(Side.BOTTOM);
        assetPieChart.setLabelsVisible(true);
        assetPieChart.setLabelLineLength(12);
        assetPieChart.setClockwise(true);
        assetPieChart.setStartAngle(90);
        assetPieChart.setAnimated(true);

        loadUserPropertyChartData();
    }

    private void loadUserPropertyChartData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Map<PieChart.Data, Double> actualValuesBySlice = new HashMap<>();
        int currentUserId = UserSession.getCurrentUserId();

        if (currentUserId <= 0) {
            assetPieChart.setData(pieChartData);
            assetPieChart.setTitle("Analysis (No user loaded)");
            return;
        }

        try {
            List<Property> userProperties = PropertyDao.allProperties(currentUserId);
            for (Property property : userProperties) {
                double actualValue = Math.max(property.getValuation(), 0.0);
                double displayWeight = actualValue > 0 ? actualValue : MINIMUM_VISIBLE_SLICE_WEIGHT;

                PieChart.Data data = new PieChart.Data(buildPropertyDisplayName(property), displayWeight);
                pieChartData.add(data);
                actualValuesBySlice.put(data, actualValue);
            }
        } catch (RuntimeException ex) {
            assetPieChart.setData(pieChartData);
            assetPieChart.setTitle("Analysis (Failed to load)");
            return;
        }

        assetPieChart.setData(pieChartData);
        if (pieChartData.isEmpty()) {
            assetPieChart.setTitle("Analysis (No positive-valued properties yet)");
        } else {
            assetPieChart.setTitle("Property Distribution");
        }

        applyPieSliceLabelsAndTooltips(pieChartData, actualValuesBySlice);
    }

    private void applyPieSliceLabelsAndTooltips(ObservableList<PieChart.Data> pieChartData, Map<PieChart.Data, Double> actualValuesBySlice) {
        double total = pieChartData.stream().mapToDouble(PieChart.Data::getPieValue).sum();

        for (PieChart.Data data : pieChartData) {
            String propertyName = data.getName();
            double percentage = total > 0 ? (data.getPieValue() / total) * 100 : 0.0;
            double actualValue = actualValuesBySlice.getOrDefault(data, data.getPieValue());
            data.setName(String.format(Locale.US, "%s (%.2f%%)", propertyName, percentage));
            String tooltipText = String.format(
                    Locale.US,
                    "%s\nActual value: PKR %,.0f\nVisual share: %.2f%%",
                    propertyName,
                    actualValue,
                    percentage
            );


            if (data.getNode() != null) {
                Tooltip.install(data.getNode(), new Tooltip(tooltipText));
            } else {
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        Tooltip.install(newNode, new Tooltip(tooltipText));
                    }
                });

            }

        }
    }

    private String buildPropertyDisplayName(Property property) {
        String propertyName = property.getProperty_name();
        if (propertyName == null) {
            return "Property #" + property.getPropertyId();
        }

        String trimmedName = propertyName.trim();
        if (trimmedName.isEmpty() || trimmedName.matches("(?i)^x\\d+$")) {
            return "Property #" + property.getPropertyId();
        }

        return trimmedName;
    }


    @FXML
    private void addAsset()throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/propertyPopup.fxml"));
        Parent root = loader.load();
        PropertyPopupController popupController = loader.getController();

        Scene scene = new Scene(root);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Asset");
        stage.setScene(scene);
        stage.showAndWait();

        if (popupController.getNewProperty() != null) {
            loadUserPropertyChartData();
            refreshSnapshotPanel();
        }

    }

    private void refreshSnapshotPanel() {
        try {
            reloadSnapshotPanel();
        } catch (IOException e) {
            throw new RuntimeException("Failed to refresh total estate worth panel", e);
        }
    }

    private void reloadSnapshotPanel() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/demo/inheritorsSnapshot.fxml")
        );
        Parent snapshotView = loader.load();
        VBox.setVgrow(snapshotView, Priority.ALWAYS);
        inheritorSnapshotContainer.getChildren().setAll(snapshotView);
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
    private void goToAssets(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/demo/property_and_inheritor.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle("Assets");
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
    @FXML private PieChart assetPieChart;




}
