package org.example.demo.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyDao {
    private static final Logger LOGGER = Logger.getLogger(PropertyDao.class.getName());

    public static void addProperty(String name, Integer propertyValue, Integer parentId){
        String query = "Insert into properties (property_name, value, parent_owner) values (?,?,?)";

        try{
            Connection con = DbConnection.getConnection();
            try (PreparedStatement pst = con.prepareStatement(query)){
                pst.setString(1,name);
                pst.setInt(2, propertyValue);
                pst.setInt(3, parentId);

                pst.executeUpdate();
                System.out.println("Property Successfully created: "+name);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding property: " + name, ex);
        }
    }
}
