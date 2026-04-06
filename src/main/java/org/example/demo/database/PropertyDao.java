package org.example.demo.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
