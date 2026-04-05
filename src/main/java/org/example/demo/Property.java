package org.example.demo;

public class Property {
    private String property_name;
    private double valuation;
    Property(String property_name,double valuation){
        this.property_name=property_name;
        this.valuation=valuation;
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
