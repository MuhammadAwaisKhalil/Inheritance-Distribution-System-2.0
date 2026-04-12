package org.example.demo.database;

import javafx.collections.FXCollections;
import org.example.demo.Inheritors.Inheritor;
import org.example.demo.Inheritors.InheritorProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InheritorDao {
    private static final Logger LOGGER = Logger.getLogger(InheritorDao.class.getName());

    public static boolean assignProperty(int propertyId, int inheritorId, double portionGiven){
        String query = "Insert into inherits (property_id, inheritor_id, portion_given) values (?,?,?)";

        try {
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, propertyId);
                pst.setInt(2,inheritorId);
                pst.setDouble(3, portionGiven);

                pst.executeUpdate();
                System.out.println("User " + inheritorId + " assigned to property " + propertyId);
                return true;
            }
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Cannot assign property: " + propertyId + " to user " + inheritorId, ex);
            return false;
        }
    }

    public static boolean deleteAssignPropertyById(int property_id, int inheritor_id){
        String query = "DELETE FROM inherits where property_id = (?) and inheritor_id = (?)";

        try{
            Connection con = DbConnection.getConnection();
            try(PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1,property_id);
                pst.setInt(2,inheritor_id);

                pst.executeUpdate();
                System.out.println("Property "+property_id+" deleted successfully for user " + inheritor_id);
                return true;
            }
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Error deleting property: "+ property_id + " for user " + inheritor_id,ex);
            return false;
        }
    }


    public static List<Inheritor> getAllInheritors(int parentId){
        List<Inheritor> inheritors = FXCollections.observableArrayList();
        String query1 = "select id, user_name from accounts where parent = (?)";
        String query2 = "select property_id, portion_given, property_name, value from accounts join inherits on (inherits.inheritor_id = accounts.id) join properties on (properties.id = inherits.property_id) where accounts.id = ? ";
        try {
            Connection con = DbConnection.getConnection();
            try (PreparedStatement pst = con.prepareStatement(query1)){
                pst.setInt(1, parentId);
               try(ResultSet rs = pst.executeQuery()) {
                   while (rs.next()) {
                       int id = rs.getInt("id");
                       String name = rs.getString("user_name");
                       Inheritor inheritor = new Inheritor(id, name);
                       try (PreparedStatement pst2 = con.prepareStatement(query2)){
                           pst2.setInt(1, id);
                        try(ResultSet rs2 = pst2.executeQuery()){
                            while (rs2.next()){
                                int prop_id = rs2.getInt("property_id");
                                double portion = rs2.getDouble("portion_given");
                                String property_name = rs2.getString("property_name");
                                long value = rs2.getLong("value");
                                inheritor.getProperties().add(new InheritorProperty(prop_id, property_name, value, portion));
                            }
                        }
                       inheritors.add(inheritor);
                   }
                   }
               }
            }


        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding inheritor for: "+ parentId,e);
            return null;
        }
        return inheritors;
    }

    public static void removePropertyFromInheritor(int inheritor_id, int property_id){
        String query = "delete from inherits where property_id = ? and inheritor_id = ?";
        try {
            Connection con = DbConnection.getConnection();
            try (PreparedStatement pst = con.prepareStatement(query)){
                pst.setInt(1, property_id);
                pst.setInt(2, inheritor_id);
                pst.executeUpdate();
                System.out.println("Property "+property_id+" deleted successfully for user " + inheritor_id);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting inherit property  for: "+ inheritor_id + " property " + property_id ,e);
            throw new RuntimeException(e);
        }
    }

    public static void reassignPropertyFromInheritor(int inheritor_id, int property_id, double portionGiven){
        String query = "update inherits set portion_given = ? where property_id = ? and inheritor_id = ?";
        try {
            Connection con = DbConnection.getConnection();
            try (PreparedStatement pst = con.prepareStatement(query)){
                pst.setDouble(1, portionGiven);
                pst.setInt(2, property_id);
                pst.setInt(3, inheritor_id);
                pst.executeUpdate();
                System.out.println("Property "+property_id+" updated successfully for user " + inheritor_id);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating inherit property  for: "+ inheritor_id + " property " + property_id ,e);
            throw new RuntimeException(e);
        }
    }

    public static List<InheritorProperty> getPropertiesNotAssignedTo(int inheritor_id, int parentId){
        String query = "select id, property_name, value from properties where id not in (select property_id from inherits where inheritor_id = ?) and parent_owner = ?";
        List<InheritorProperty> leftoverproperties = new ArrayList<>();

        try {
            Connection con = DbConnection.getConnection();
            try (PreparedStatement pst = con.prepareStatement(query)){
                pst.setInt(1, inheritor_id);
                pst.setInt(2, parentId);
                try(ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("property_name");
                        double value = rs.getDouble("value");
                        InheritorProperty inheritorProperty = new InheritorProperty(id, name, value, 0);
                        leftoverproperties.add(inheritorProperty);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding inheritor for: "+ parentId,e);
            return null;
        }

        return leftoverproperties;
    }

}
