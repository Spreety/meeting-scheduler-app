package Controller;

import DatabaseAccess.DatabaseCustomers;
import Model.Appointments;
import Model.Customers;
import Utilities.DatabaseConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static DatabaseAccess.DatabaseCustomers.getAllCustomers;

/** This class created the employee view screen */
public class CustomerController implements Initializable {

    public TableView customerInfoTable;
    public TableColumn customerIDColumn;
    public TableColumn customerNameColumn;
    public TableColumn addressColumn;
    public TableColumn divisionColumn;
    public TableColumn zipColumn;
    public TableColumn countryColumn;
    public TableColumn phoneNumberColumn;

    private static Customers updateCustomer;
    private static int updateCustomerNumber;
    public static int selectedCustomer;
    public static int selectedCustomerID;
    public static String selectedCustomerName;
    public static int chosenCustomerID;
    public static String chosenCustomerName;
    public static String chosenCustomerAddress;
    public static String chosenCustomerCountry;
    public static String chosenCustomerState;
    public static String chosenCustomerZip;
    public static String chosenCustomerPhone;
    public TextField employeeSearchBox;


    public static int customerNumberToUpdate() {
        return updateCustomerNumber;
    }

    /**
     * This section is triggered when add employee button is selected
     * it takes the user to the add employee screen
     * @param actionEvent The add employee button takes you to the add employee screen
     * @throws IOException
     */
    public void addCustomerButton(ActionEvent actionEvent) throws IOException { //need to test this
        Parent addCustomer = FXMLLoader.load(getClass().getResource("/View/AddCustomer.fxml"));
        Scene scene = new Scene(addCustomer);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This section is triggered when the update employee button is clicked
     * It grabs the selected employee row from the table view and stores it
     * it then takes the user to the Update employee screen
     * @param actionEvent the update employee button gets selected data and takes you to the update employee screen
     * @throws IOException
     */
    public void updateCustomerButton(ActionEvent actionEvent) throws IOException {

        //THis gets the selected row from the table
        updateCustomer = (Customers) customerInfoTable.getSelectionModel().getSelectedItem();

        //This gets the employee ID from the selected row
        chosenCustomerID = updateCustomer.getCustomerID();

        //This gets the employee name from the selected row
        chosenCustomerName = updateCustomer.getCustomerName();

        //This gets the employee address from the selected row
        chosenCustomerAddress = updateCustomer.getAddress();

        //This gets the employee country from the selected row
        chosenCustomerCountry = updateCustomer.getCountry();

        //This gets the employee state from the selected row
        chosenCustomerState = updateCustomer.getDivision();

        //This gets the zip from the selected row
        chosenCustomerZip = updateCustomer.getPostalCode();

        //This gets the phone from the selected row
        chosenCustomerPhone = updateCustomer.getPhone();

        //This takes the user to the update employee screen
        Parent EditCustomer = FXMLLoader.load(getClass().getResource("/View/UpdateCustomer.fxml"));
        Scene scene = new Scene(EditCustomer);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This section is triggered by the delete employee button
     * it gathers the needed info from the selected tableview row
     * it then runs the SQL Query in the database to delete the employee
     * @param actionEvent the delete button deletes the selected employee
     * @throws IOException
     */
    public void deleteCustomerButton(ActionEvent actionEvent) throws IOException {

        //This pulls the selected row from the table
        Customers selectedCustomer = (Customers) customerInfoTable.getSelectionModel().getSelectedItem();

        //This gets the employee ID from the selected row
        selectedCustomerID = selectedCustomer.getCustomerID();

        //This gets the employee Name from the selected row
        selectedCustomerName = selectedCustomer.getCustomerName();

        //This brings up a confirmation box to confirm user wants to delete the employee
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("CONFIRMATION NEEDED");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure you want to delete employee " + selectedCustomerName + "?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            //This deletes the employee if ok is selected from confirmation box
            DatabaseCustomers.deleteCustomer();
            System.out.println("Employee Deleted");

            //This takes the user to the employee screen
            Parent deleteCustomer = FXMLLoader.load(getClass().getResource("/View/Customer.fxml")); //takes you back to the login screen
            Scene scene = new Scene(deleteCustomer);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            //This closes the confirmation box and leaves the user on the employee page
            alert.close();
        }
    }

    /**
     * This section is triggered by the logout button
     * it brings up a confirmation box
     * It then takes the user to the login screen and closes the connection to the database
     * @param actionEvent the logout button takes use to the login screen and closes database connection
     * @throws IOException
     */
    public void logoutButton(ActionEvent actionEvent)  throws IOException {

        //This brings up a confirmation box asking if user wants to log out
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("CONFIRMATION NEEDED");
        alert.setHeaderText("Confirm logout");
        alert.setContentText("Are you sure you want to logout?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {

            //this triggers the database connection to close
            DatabaseConnection.closeConnection();
            System.out.println("Logged Out");

            //This takes the user back to the login screen
            Parent logout = FXMLLoader.load(getClass().getResource("/View/login.fxml")); //takes you back to the login screen
            Scene scene = new Scene(logout);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            //This closes the confirmation box
            alert.close();
        }
    }

    /**
     * This section is triggered by the view schedule button
     * It takes the user to the scheduler screen
     * @param actionEvent the view schedule button takes the user to the scheduler screen
     * @throws IOException
     */
    public void viewScheduleButton(ActionEvent actionEvent) throws IOException {

        //This takes the user the the schedule screen
        Parent scheduleView = FXMLLoader.load(getClass().getResource("/View/Scheduler.fxml"));
        Scene scene = new Scene(scheduleView);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This section is triggered by the Reports button
     * It takes the user to the reports screen
     * @param actionEvent the reports button takes the user to the reports screen
     * @throws IOException
     */
    public void reportsButton(ActionEvent actionEvent) throws IOException {

        //This takes the user to the reports screen
        Parent reportView = FXMLLoader.load(getClass().getResource("/View/Reports.fxml"));
        Scene scene = new Scene(reportView);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This event takes the String from the search box and compares it to the obseravable list
     * It will then change the table to only show the matching items.
     * @param actionEvent the employee search button filters down the list to match the search term
     */
    public void employeeSearchButton(ActionEvent actionEvent) {
        String searchForEmployee = employeeSearchBox.getText();
        ObservableList foundEmployees = Customers.lookupCustomer(searchForEmployee);

        if (foundEmployees.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("There was a problem with your search.");
            alert.setContentText("No employees were found.");
            alert.showAndWait();
        } else {
            customerInfoTable.setItems(foundEmployees);
        }
    }

    /** This section is everything that needs to run upon initialization*/
    @Override
    public void initialize (URL url, ResourceBundle rb) {

        //This sets the items in the tableview
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        //this sets the customer items to the table columns
        setCustomers();

    }
    //this creates the function of set employees
    public void setCustomers() {
        customerInfoTable.setItems(getAllCustomers());
    }

}
