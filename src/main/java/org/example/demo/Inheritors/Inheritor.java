package org.example.demo.Inheritors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inheritor {
    private int id;
    private String name;
    private ObservableList<InheritorProperty> properties;

    public Inheritor(int id, String name) {
        this.id = id;
        this.name = name;
        this.properties = FXCollections.observableArrayList();
    }

    public double calculateTotalSharePercent(double totalWorth){
        if (totalWorth == 0) return 0;
        double weightedSum = properties.stream()
                .mapToDouble(p -> p.getWorth() * p.getPortionGiven() / 100.0).sum();
        return (weightedSum / totalWorth) * 100;
    }

    public double calculateTotalValue(){
        return properties.stream()
                .mapToDouble(p -> p.getWorth() * p.getPortionGiven() / 100.0).sum();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ObservableList<InheritorProperty> getProperties() {
        return properties;
    }

    public void setProperties(ObservableList<InheritorProperty> properties) {
        this.properties = properties;
    }
}
