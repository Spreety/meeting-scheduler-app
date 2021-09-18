package DatabaseAccess;

import Model.Countries;
import Utilities.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** This class is where all of the SQL Queries related to Countries are kept */
public class DatabaseCountries {

    /**
     * This section pulls all countries from the database
     * @return this returns all countries from the database
     */
    public static ObservableList<Countries> getAllCountries() {
        ObservableList<Countries> countryList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM countries";

            //Creates a prepared statement
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement(sql);
            //Get results
            ResultSet rs = ps.executeQuery();
            //works through result set one row at a time
            while (rs.next()) {
                //get results data
                int countryID = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                String createDate = rs.getString("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");

                Countries a = new Countries(countryID, country, createDate, createdBy, lastUpdate, lastUpdatedBy); //creates countries based on result data
                countryList.add(a); //add new country object to list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return countryList; //return list
    }
}
