package org.example.demo.database;

import org.example.demo.Property.Property;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyDao {
    private static final Logger LOGGER = Logger.getLogger(PropertyDao.class.getName());

    public static boolean addProperty(String name, Integer propertyValue, Integer parentId){
        String query = "Insert into properties (property_name, value, parent_owner) values (?,?,?)";

        try{
            Connection con = DbConnection.getConnection();
            try (PreparedStatement pst = con.prepareStatement(query)){
                pst.setString(1,name);
                pst.setInt(2, propertyValue);
                pst.setInt(3, parentId);

                pst.executeUpdate();
                System.out.println("Property Successfully created: "+name);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding property: " + name, ex);
            return false;
        }
    }

    public static boolean deletePropertyById(int id){
        String query = "DELETE FROM properties where id = (?)";

        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1,id);

                pst.executeUpdate();
                System.out.println("Property "+id+" deleted successfully");
                return true;
            }
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Error deleting property: "+ id,ex);
            return false;
        }
    }

    public static int getPropertyId(String propName, int parentId){
        String query = "select id from properties where property_name = (?) and parent_owner = (?)";

        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, propName);
                pst.setInt(2, parentId);

                try (ResultSet rs = pst.executeQuery()){
                    if (rs.next()){
                        int propId = rs.getInt("id");
                        return propId;
                    }else {
                        LOGGER.log(Level.SEVERE, "No property found for owner: " + parentId + " and name: " + propName);
                        return 0;
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding property for owner: " + parentId + " and name: " + propName, ex);
            return -1;
        }
    }

    public static int getTotalworth(int parentId){
        String query = "select Sum(value) as total from properties where parent_owner = ?";
        try {
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)){
                pst.setInt(1, parentId);
                try(ResultSet rs = pst.executeQuery()){
                    if(rs.next()){
                        int totalWorth = rs.getInt("total");
                        return totalWorth;
                    }else {
                        return 0;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding property for owner: " + parentId, e);
            return -1;
        }
    }


    public static List<Property> allProperties(int parentId){
        List<Property> properties = new ArrayList<>();
        String query = "select id, property_name, value from properties where parent_owner = ?";
        try {
            Connection con = DbConnection.getConnection();
            try (PreparedStatement pst = con.prepareStatement(query)){
                pst.setInt(1, parentId);
                try(ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String property_name = rs.getString("property_name");
                        double value = rs.getDouble("value");

                        Property property = new Property(id, property_name, value);
                        properties.add(property);
                    }
                }
                System.out.println("All properties found for user " + parentId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding properties for user " + parentId ,e);
            throw new RuntimeException(e);
        }

        return properties;
    }


}
