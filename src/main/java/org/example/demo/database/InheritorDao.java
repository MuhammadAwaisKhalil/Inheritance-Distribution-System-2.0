package org.example.demo.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
}
