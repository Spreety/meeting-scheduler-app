package DatabaseAccess;

import Controller.AddCustomerController;
import Controller.UpdateCustomerController;
import Model.FirstLevelDivisions;
import Utilities.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** This class is where all of the SQL Queries related to first level divisions are kept */
public class DatabaseFirstLevelDivisions {


    public static ObservableList<FirstLevelDivisions> stateList;
    private static String selectedCountryCombo;
    private static String pickedCountryCombo;
    private static String selectedStateCombo;
    private static String selectedStateComboU;

    /**
     * This section pulls all first level divisions from the database
     * @return this returns all first level divisions
     */
    public static ObservableList<FirstLevelDivisions> getAllFirstLevelDivisions() {
        ObservableList<FirstLevelDivisions> firstLevelDivisionsList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM first_level_divisions";

            //Creates a prepared statement
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement(sql);
            //Get results
            ResultSet rs = ps.executeQuery();
            //works through result set one row at a time
            while (rs.next()) {
                //get results data
                int divisionID = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                String createDate = rs.getString("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryID = rs.getInt("Country_ID");

                FirstLevelDivisions a = new FirstLevelDivisions(divisionID, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryID); //creates first level divisions based on result data
                firstLevelDivisionsList.add(a); //add new first level division object to list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return firstLevelDivisionsList; //return list
    }

    /**
     * This section pulls States from database based off Country ID for the Add employee screen
     * @return this returns filtered divisions
     */
    public static ObservableList<String> getFilteredDivisions() {
        ObservableList<String> stateList = FXCollections.observableArrayList();

        //this pulls the selected country from the combo box
        selectedCountryCombo = AddCustomerController.countryComboSelection;
        try {
            //SQL Query
            String sqlS = "SELECT Division FROM first_level_divisions JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID WHERE countries.Country = ?";

            //Creates a prepared statement
        PreparedStatement pss = DatabaseConnection.startConnection().prepareStatement(sqlS);

        //fills in questions mark
        pss.setString(1, selectedCountryCombo);
        pss.execute();
        //Get results
        ResultSet rs = pss.executeQuery();

        //works through result set one row at a time
        while (rs.next()) {
            String division = rs.getString("Division");

                String a = new String(division); //creates division based on result data
                stateList.add(a); //add new division object to list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return stateList; //return list
            }

    /**
     * Pulls Division ID's from database based off Country ID for the Add employee screen
     * @return this returns filtered division ID's
     */
    public static ObservableList<Integer> getFilteredDivisionIDs() {
        ObservableList<Integer> divisionIDList = FXCollections.observableArrayList();

        //this pulls the selected country from the combo box
        selectedStateCombo = AddCustomerController.stateComboSelection;

        try {
            //SQL Query
            String sqlI = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";

            //Creates a prepared statement
            PreparedStatement psp = DatabaseConnection.startConnection().prepareStatement(sqlI);

            //fills in questions mark
            psp.setString(1, selectedStateCombo);
            psp.execute();
            //Get results
            ResultSet rs = psp.executeQuery();

            //works through result set one row at a time
            while (rs.next()) {
                String divisionID = rs.getString("Division_ID");

                Integer a = new Integer(divisionID); //creates division based on result data
                divisionIDList.add(a); //add new division object to list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return divisionIDList; //return list
    }

    /**
     * This pulls filtered divisions based off country ID for the update employee screen
     * @return this returns filtered divisions for the update employee screen
     */
    public static ObservableList<String> getUpdateFilteredDivisions() {
        ObservableList<String> updateStateList = FXCollections.observableArrayList();

        //this pulls the selected country from the combo box
        pickedCountryCombo = UpdateCustomerController.countryComboPick;
        try {
            //SQL Query
            String sqlU = "SELECT Division FROM first_level_divisions JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID WHERE countries.Country = ?";

            //Creates a prepared statement
            PreparedStatement psu = DatabaseConnection.startConnection().prepareStatement(sqlU);

            //fills in questions mark
            psu.setString(1, pickedCountryCombo);
            psu.execute();
            //Get results
            ResultSet rs = psu.executeQuery();

            //works through result set one row at a time
            while (rs.next()) {
                String division = rs.getString("Division");

                String b = new String(division); //creates division based on result data
                updateStateList.add(b); //add new division object to list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return updateStateList; //return list
    }

    /**
     * This pulls Division ID's from database based off Country ID for the update employee screen
     * @return this returns filtered division ID's for the update employee screen
     */
    public static ObservableList<Integer> getUpdatedFilteredDivisionIDs() {
        ObservableList<Integer> divisionIDListU = FXCollections.observableArrayList();

        //this pulls the selected country from the combo box
        selectedStateComboU = UpdateCustomerController.stateComboSelection;

        try {
            //SQL Query
            String sqlUa = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";

            //Creates a prepared statement
            PreparedStatement psua = DatabaseConnection.startConnection().prepareStatement(sqlUa);

            //fills in questions mark
            psua.setString(1, selectedStateComboU);
            psua.execute();
            //Get results
            ResultSet rs = psua.executeQuery();

            //works through result set one row at a time
            while (rs.next()) {
                String divisionIDU = rs.getString("Division_ID");

                Integer a = new Integer(divisionIDU); //creates division based on result data
                divisionIDListU.add(a); //add new division object to list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return divisionIDListU; //return list
    }
}

