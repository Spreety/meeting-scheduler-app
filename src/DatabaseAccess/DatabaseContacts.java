package DatabaseAccess;

import Model.Contacts;
import Utilities.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class is where all of the SQL Queries related to contacts are kept */
public class DatabaseContacts {

    /**
     * This section pulls all contacts from the database
     * @return Ths returns all contacts in the database
     */
    public static ObservableList<Contacts> getAllContacts() {
        ObservableList<Contacts> contactList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM contacts";

            //Creates a prepared statement
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement(sql);
            //Get results
            ResultSet rs = ps.executeQuery();
            //works through result set one row at a time
            while (rs.next()) {
                //get results data
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");


                //need to finish filling this section out
                Contacts a = new Contacts(contactID, contactName, email); //creates contacts based on result data
                contactList.add(a); //add new contact object to list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return contactList; //return list
    }
}

