package DatabaseAccess;

import Controller.CustomerController;
import Controller.UpdateCustomerController;
import Model.*;
import Utilities.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.ZonedDateTime;

/** This class is where all of the SQL Queries related to employees are kept */
public class DatabaseCustomers {

    /**
     * This section gets all of the employees from the database
     * @return this returns all employees from the database
     */
    public static ObservableList<Customers> getAllCustomers() {
        ObservableList<Customers> customerList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, first_level_divisions.Division, customers.Postal_Code, countries.Country, customers.Phone FROM customers INNER JOIN first_level_divisions ON first_level_divisions.Division_ID = customers.Division_ID INNER JOIN countries ON countries.Country_ID = first_level_divisions.Country_ID ORDER BY Customer_Name;";

            //Creates a prepared statement
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement(sql);
            //Get results
            ResultSet rs = ps.executeQuery();
            //works through result set one row at a time
            while (rs.next()) {
                //get results data
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String division = rs.getString("Division");
                String postalCode = rs.getString("Postal_Code");
                String country = rs.getString("Country");
                String phone = rs.getString("Phone");


                //need to finish filling this section out
                Customers a = new Customers(customerID, customerName, address, division, postalCode, country, phone); //creates customers based on result data
                customerList.add(a); //add new employee object to list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //this returns the entire list of employees
        return customerList;
    }

    /**
     * This section adds new employees to the database
     * @param customer_ID this is the employee ID number
     * @param Customer_Name this is the employee name
     * @param Address this is the employee address
     * @param Postal_Code this is the employee postal code
     * @param Phone this is the employee phone number
     * @param Last_Updated_By this is the user who did the update
     * @param Created_By this is the user who created the employee
     * @param Division_ID this is the division ID number
     */
    public static void addCustomer(int customer_ID, String Customer_Name, String Address, String Postal_Code, String Phone, String Last_Updated_By, String Created_By, int Division_ID) {
        try {

            //SQL Statement to Insert into table
            String sqlac = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Last_Updated_By, Created_By, Division_ID) VALUES(NULL, ?, ?, ?, ?, ?, ?, ?) ";

            //prepared statement
            PreparedStatement psa = DatabaseConnection.startConnection().prepareStatement(sqlac, Statement.RETURN_GENERATED_KEYS);
            //fills in question marks

            psa.setString(1, Customer_Name);
            psa.setString(2, Address);
            psa.setString(3, Postal_Code);
            psa.setString(4, Phone);
            psa.setString(5,Last_Updated_By);
            psa.setString(6, Created_By);
            psa.setInt(7, Division_ID);

            //execute query and get results
            psa.execute();

            ResultSet rs = psa.getGeneratedKeys();
            rs.next();
            int Customer_ID = rs.getInt(1); //this pulls index 1 since customer ID is number 1 in table

        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This section updates employees in the database
     * @param Customer_Name this is the employee name
     * @param Address this is the employee address
     * @param Postal_Code this is the employee postal code
     * @param Phone this is the employee phone number
     * @param Last_Update this is the timestamp of update
     * @param Last_Updated_By this is the user who updated
     * @param Division_ID this is the division ID number
     */
    public static void updateCustomer(String Customer_Name, String Address, String Postal_Code, String Phone, String Last_Update, String Last_Updated_By, int Division_ID) {
        int customerToUpdate = UpdateCustomerController.customerID;
        System.out.println(customerToUpdate);
        try{
            //SQL statement to modify employee
            String sqlu = "UPDATE customers set Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

            //prepared statement
            PreparedStatement psu = DatabaseConnection.startConnection().prepareStatement(sqlu);
            //fills in question marks
            psu.setString(1, Customer_Name);
            psu.setString(2, Address);
            psu.setString(3, Postal_Code);
            psu.setString(4, Phone);
            psu.setString(5, Last_Update);
            psu.setString(6, Last_Updated_By);
            psu.setInt(7, Division_ID);
            psu.setInt(8, customerToUpdate);
            //execute query
            psu.execute();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    //method for deleting meeting

    /**
     * This section deletes selected employee from the database
     */
    public static void deleteCustomer() {

        //This pulls the selected employeeID from scheduler
        int customerToDelete = CustomerController.selectedCustomerID;

        try {

            //SQL statement to delete meetings first
            String sqle = "DELETE from appointments WHERE Customer_ID = ?";

            //create a prepared statement
            PreparedStatement psd = DatabaseConnection.startConnection().prepareStatement(sqle);
            //fills in questions marks
            psd.setInt(1, customerToDelete);
            //execute query and get results
            psd.execute();

            //SQL statement to delete employee
            String sqlf = "DELETE from customers WHERE Customer_ID = ?";

            //create a prepared statement
            PreparedStatement psd2 = DatabaseConnection.startConnection().prepareStatement(sqlf);
            //fills in questions marks
            psd2.setInt(1, customerToDelete);
            //execute query and get results
            psd2.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
