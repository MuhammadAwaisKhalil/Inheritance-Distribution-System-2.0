package org.example.demo.Inheritors;

public class InheritorProperty {
    private int propertyId;
    private String propertyName;
    private double worth;
    private double portionGiven;

    public InheritorProperty(int propertyId, String propertyName, double worth, double portionGiven) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.worth = worth;
        this.portionGiven = portionGiven;
    }

    public int getPropertyId() {
        return propertyId;
    }
    public String getPropertyName() {
        return propertyName;
    }
    public double getWorth() {
        return worth;
    }
    public double getPortionGiven() {
        return portionGiven;
    }
    public void setPortionGiven(double worth) {
        this.portionGiven = worth;
    }

    public double getAbsoluteValue(){
        return worth * portionGiven /100.0;
    }
}
