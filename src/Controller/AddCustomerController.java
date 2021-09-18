package Controller;

import DatabaseAccess.DatabaseCustomers;
import Model.Countries;
import Model.Customers;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static DatabaseAccess.DatabaseCountries.getAllCountries;
import static DatabaseAccess.DatabaseFirstLevelDivisions.*;

/** This class creates the add employee screen */
public class AddCustomerController implements Initializable {
    public Label addCustomerTitle;
    public TextField customerIDField;
    public TextField customerNameField;
    public TextField addressField;
    public Label customerIDLabel;
    public Label customerNameLabel;
    public Label addressLabel;
    public Label stateLabel;
    public TextField zipField;
    public TextField telephoneField;
    public Label zipLabel;
    public Label telephoneLabel;
    public Label countryLabel;
    public ComboBox<String> stateComboBox;
    public ComboBox<Countries> countryComboBox;

    private int customerID;
    private String  customerExceptionMessage = new String();
    public static String countryComboSelection;
    public static int divisionID;
    public static String stateComboSelection;


    public void stateCombo(ActionEvent actionEvent) {
    }

    /**
     * This section is triggered when a country is selected in the country combo box.
     * It takes the division ID and finds the corresponding Divisions.
     * The state combo box gets populated with this data
     * @param actionEvent the country combo box allows the user to select country
     */
    public void countryCombo(ActionEvent actionEvent) {

        //This pulls the selected value from country combo box
        countryComboSelection = String.valueOf(countryComboBox.getValue());

        //this calls the function to run the query
        getFilteredDivisions();

        //this sets corresponding States in the combo box
        stateComboBox.setItems(getFilteredDivisions());

    }

    /**
     * This section gets the string values from the selected tableview row
     * @param actionEvent  The save button triggers checks for overlapping meetings etc.
     */
    public void saveButton(ActionEvent actionEvent) {

        //this gets the value of state combo box selection
        stateComboSelection = String.valueOf(stateComboBox.getValue());

        //this gets the first object in the getFilteredDivsionIDs observable list
        int stateDivisionID = getFilteredDivisionIDs().get(0);

        //this gets the text that was entered into the employee ID field.
        int Customer_ID = Integer.parseInt(customerIDField.getText());

        //this gets the text entered in the employee name field
        String Customer_Name = customerNameField.getText();

        //this gets the text entered into the address field.
        String Address = addressField.getText();

        //this gets the text entered into the zip code field.
        String Postal_Code = zipField.getText();

        //this gets the text entered into the phone number field.
        String Phone = telephoneField.getText();

        //this gets the user for created_by field
        String userName = LoginController.usernameText;

        //this gets the username so it can be saved as Created_By
        String Last_Updated_By = userName;
        String Created_By = userName;

        //this gets the division ID form the selected state.
        int Division_ID = stateDivisionID;

        //this gets the selected country from the combo box
        String country = String.valueOf(countryComboBox.getValue());

        /**This section checks for incomplete fields.
         * If validation checks are passed, the employee is added to the database
         */
        try {
            customerExceptionMessage = Customers.customerValidation(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Last_Updated_By, Created_By, Division_ID, customerExceptionMessage);
            //this checks for incomplete fields
            if (customerExceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Unable to add employee.");
                alert.setContentText(customerExceptionMessage);
                alert.showAndWait();
                customerExceptionMessage = "";
                System.out.println("Error: Unable to save employee");
            } else {
                //This triggers the add customers SQL Query
                DatabaseCustomers.addCustomer(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Last_Updated_By, Created_By, Division_ID);
                System.out.println("Employee Added");

                //This takes the user back to the employees field
                Parent saveCustomer = FXMLLoader.load(getClass().getResource("/View/Customer.fxml")); //takes you back to main screen
                Scene scene = new Scene(saveCustomer);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (IOException e) {
        }
    }
    /**
     * This is the cancel button which cancels add employee
     * @param actionEvent this cancel button takes the user back to the employee screen
     * @throws IOException
     */
    public void cancelButton(ActionEvent actionEvent) throws IOException {

        //This brings up a confirmation box
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("CONFIRMATION NEEDED");
        alert.setHeaderText("Confirm cancel");
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {

            //This takes the user to the employee screen once they hit ok
            Parent cancelAdd = FXMLLoader.load(getClass().getResource("/View/Customer.fxml")); //takes you back to the login screen
            Scene scene = new Scene(cancelAdd);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            //This closes the confirmation box and stays on the add employee screen
            alert.close();
        }
    }

    /** This section is everything that needs to run upon initialization*/
    @Override
    public void initialize (URL url, ResourceBundle rb) {

        //this auto sets the employee ID
        customerIDField.setText(String.valueOf(customerID));

        //this sets countries in the combo box
        countryComboBox.setItems(getAllCountries());


    }
}
