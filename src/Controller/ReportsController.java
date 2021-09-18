package Controller;

import DatabaseAccess.DatabaseAppointments;
import DatabaseAccess.DatabaseUsers;
import Model.Reports;
import Utilities.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

/** This class created the reports screen */
public class ReportsController implements Initializable {
    public ComboBox reportTypeCombo;
    public TextArea reportField;
    public Button runReportButton;
    private int reportID = 1;
    ObservableList<Reports> reports = FXCollections.observableArrayList();

    /**
     * This section is triggered by the logout button
     * it brings up a confirmation box
     * It then takes the user to the login screen and closes the connection to the database
     * @param actionEvent the logout button takes user to the login screen and closes the database connection
     * @throws IOException
     */
    public void logoutButton(ActionEvent actionEvent) throws IOException {

        //This brings up a confirmation box asking if user wants to logout
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("CONFIRMATION NEEDED");
        alert.setHeaderText("Confirm logout");
        alert.setContentText("Are you sure you want to logout?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            //This closes the program and database connection
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
     * This is triggered by the view employees button
     * It takes the user to the employees screen
     * @param actionEvent the employee button takes the user to the employee screen
     * @throws IOException
     */
    public void CustomersButton(ActionEvent actionEvent) throws IOException {

        //This takes the user to the employee screen
        Parent customerView = FXMLLoader.load(getClass().getResource("/View/Customer.fxml"));
        Scene scene = new Scene(customerView);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This section is triggered by the view schedule button
     * It takes the user to the scheduler screen
     * @param actionEvent the scheduler button takes the user to the schedule screen
     * @throws IOException
     */
    public void SchedulerButton(ActionEvent actionEvent) throws IOException {

        //This takes the user to the schedule screen
        Parent scheduleView = FXMLLoader.load(getClass().getResource("/View/Scheduler.fxml"));
        Scene scene = new Scene(scheduleView);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void reportTypeComboBox(ActionEvent actionEvent) {

    }

    /**
     * This triggers the reports to run based on selected report
     * <p><b>
     *     This Lambda expression triggers the actions on the button click without
     *     needing an action event section for the button.
     *     It's quicker than setting up and action event that would also usually include a scene builder.
     * </b></p>
     */
    public void runReports() {


        //This compares the selected item in the combo box to a string
        if(String.valueOf(reportTypeCombo.getValue()) == "Employee Meeting Totals") {

            //This calls the database function to run report
            DatabaseAppointments.getAppointmentsTotals();

            //This stores the results from running the function as a String
            String createdReport1 = DatabaseAppointments.createdReport1;

            //This sets the textfield with the formatted String
            reportField.setText(createdReport1);
        }
        //This compares the selected item in the combo box to a string
        if(String.valueOf(reportTypeCombo.getValue()) == "Contact Schedules") {

            //This calls the database function to run report
            DatabaseAppointments.getContactSchedules();

            //This stores the results from running the function as a String
            String createdReport2 = DatabaseAppointments.createdReport2;

            //This sets the textfield with the formatted String
            reportField.setText(createdReport2);
        }
        //This compares the selected item in the combo box to a string
        if(String.valueOf(reportTypeCombo.getValue()) == "Employees by Country"){

            //This calls the database function to run report
            DatabaseAppointments.getCustomersByCountry();

            //This stores the results from running the function as a String
            String createdReport3 = DatabaseAppointments.createdReport3;

            //This sets the textfield with the formatted String
            reportField.setText(createdReport3);
        }
    }

    /** This section is everything that needs to run upon initialization*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //This adds the report names to the observable list
        reports.add(new Reports("Employee Meeting Totals"));
        reports.add(new Reports("Employees by Country"));
        reports.add(new Reports("Contact Schedules"));

        //This sets the report combo box with the report names
        reportTypeCombo.setItems(reports);

        /**
         * This triggers the action event that runs the selected reports
         * <p><b>
         *     This Lambda expression triggers the actions on the button click without
         *     needing an action event section for the button.
         *     It's quicker than setting up and action event that would also usually include a scene builder.
         * </b></p>
         */
        runReportButton.setOnAction(event -> runReports());

    }
}

