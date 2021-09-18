package Controller;

import DatabaseAccess.DatabaseCustomers;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static Controller.CustomerController.*;
import static DatabaseAccess.DatabaseCountries.getAllCountries;
import static DatabaseAccess.DatabaseFirstLevelDivisions.*;

/** This class creates the screen to update the employee */
public class UpdateCustomerController implements Initializable {
    public Label updateCustomerTitle;
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
    public ComboBox countryComboBox;
    public ComboBox<String> stateComboBox;

    int customerNumber = customerNumberToUpdate();
    public static String countryComboPick;
    public static int customerID;
    public static String stateComboSelection;

    public UpdateCustomerController() {

    }
    /** This section is triggered when a country is selected in the country combo box
     * It takes the division ID and finds the corresponding Divisions
     * The state combo box gets populated with this data
     */
    public void populateStateCombo() {

        //This pulls the selected value from country combo box
        countryComboPick = String.valueOf(countryComboBox.getValue());

        //this calls the function to run the query
        getFilteredDivisions();

        //this sets corresponding States in the combo box
        stateComboBox.setItems(getUpdateFilteredDivisions());

    }

    public void stateCombo(ActionEvent actionEvent) {
    }

    /** This populates the state combo box*/
    public void countryCombo(ActionEvent actionEvent) {

        //this sets corresponding States in the combo box
        populateStateCombo();
    }

    /**
     * This section gets the string values from the selected tableview row
     * @param actionEvent The save button triggers checks for overlapping meetings etc.
     */
    public void SaveButton(ActionEvent actionEvent) throws IOException {

        //this gets the value of state combo box selection
        stateComboSelection = String.valueOf(stateComboBox.getValue());

        //this gets the first object in the getFilteredDivisionIDs observable list
        int stateDivisionID = getUpdatedFilteredDivisionIDs().get(0);

        //This gets the employeeID
        customerID = Integer.parseInt(customerIDField.getText());

        //This gets the employee name
        String Customer_Name = customerNameField.getText();

        //This gets the employees address
        String Address = addressField.getText();

        //This gets the Zip
        String Postal_Code = zipField.getText();

        //This gets the phone number
        String Phone = telephoneField.getText();

        //this gets the user for created_by field
        String userName = LoginController.usernameText;

        //This sets the correct date time format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //this pulls the date time for GMT timezone
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        //this fills in Last_Update with the current GMT time
        String Last_Update = dateFormat.format(new Date());

        //this gets the username so it can be saved as Updated_By
        String Last_Updated_By = userName;

        //This gets the division ID
        int Division_ID = stateDivisionID;

        //This brings up a confirmation box to confirm save
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("CONFIRMATION NEEDED");
        alert.setHeaderText("Confirm Update");
        alert.setContentText("Do you want to save these updates?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {

            //This triggers the update customer SQL Query
            DatabaseCustomers.updateCustomer(Customer_Name, Address, Postal_Code, Phone, Last_Update, Last_Updated_By, Division_ID);
            System.out.println("Employee Updated");

            //This takes the user to the employee screen
            Parent update = FXMLLoader.load(getClass().getResource("/View/Customer.fxml")); //takes you back to the customer screen
            Scene scene = new Scene(update);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            //This closes the confirmation box and leaves user on update employee screen
            alert.close();
        }
    }

    /**
     * This cancels the update employee and returns user to the employees screen
     * @param actionEvent the cancel button takes the user back to the employee screen
     * @throws IOException
     */
    public void cancelButton(ActionEvent actionEvent)  throws IOException {

        //This brings up a confirmation box to confirm cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("CONFIRMATION NEEDED");
        alert.setHeaderText("Confirm cancel");
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {

            //This takes user to the employee screen
            Parent logout = FXMLLoader.load(getClass().getResource("/View/Customer.fxml")); //takes you back to the customer screen
            Scene scene = new Scene(logout);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            //This closes the confirmation box and leaves user on update employee screen
            alert.close();
        }
    }

    /** This section is everything that needs to run upon initialization*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //this auto fills the customer ID field with selected employee
        customerIDField.setText(String.valueOf(CustomerController.chosenCustomerID));

        //this auto fills the customer name field with selected employee
        customerNameField.setText(CustomerController.chosenCustomerName);

        //this auto fills the address field with selected employee
        addressField.setText(chosenCustomerAddress);

        //this sets countries in the combo box
        countryComboBox.setItems(getAllCountries());

        //this sets the prompt text to the selected employee
        countryComboBox.setValue(CustomerController.chosenCustomerCountry);

        //this populates the state combo box based on the country selected.
        populateStateCombo();
        stateComboBox.setItems(getUpdateFilteredDivisions());

        //this sets the prompt text to the selected employee State
        stateComboBox.setValue(chosenCustomerState);

        //this auto fills the customer name field with selected employee
        zipField.setText(chosenCustomerZip);

        //this auto fills the customer name field with selected employee
        telephoneField.setText(chosenCustomerPhone);
    }

}
