package org.example.demo.Property;

public class Property {
    private int propertyId;
    private String property_name;
    private double valuation;
    private int parentId;
    Property(String property_name,double valuation){
        this.property_name=property_name;
        this.valuation=valuation;
    }

    public Property(int propertyId, String property_name, double valuation){
        this.propertyId=propertyId;
        this.property_name=property_name;
        this.valuation=valuation;
    }

    Property(int propertyId, String property_name, double valuation, int parentId){
        this.propertyId=propertyId;
        this.property_name=property_name;
        this.valuation=valuation;
        this.parentId=parentId;
    }


    public int getParentId() {
        return parentId;
    }


    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public double getValuation() {
        return valuation;
    }
    public String getProperty_name(){
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public void setValuation(double valuation) {
        this.valuation = valuation;
    }
    public String toString(){
        return property_name+"    Rs"+valuation;
    }
}
