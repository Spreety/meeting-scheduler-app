package Controller;

import DatabaseAccess.DatabaseAppointments;
import Model.Appointments;
import Utilities.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import static DatabaseAccess.DatabaseAppointments.*;

/** This class created the schedule screen */
public class SchedulerController implements Initializable {
    public TableView schedulerTable;
    public TableColumn appointmentIDColumn;
    public TableColumn titleColumn;
    public TableColumn descriptionColumn;
    public TableColumn locationColumn;
    public TableColumn contactColumn;
    public TableColumn typeColumn;
    public TableColumn startDateColumn;
    public TableColumn endDateColumn;
    public TableColumn customerIDColumn;
    public ToggleButton weeklyButtonToggle;
    public ToggleButton monthlyButtonToggle;
    public ToggleButton allButtonToggle;
    public TextField meetingSearchBox;

    ResourceBundle rb = ResourceBundle.getBundle("Utilities/Nat", Locale.getDefault());

    public static Appointments updateAppointment;
    public static int selectedAppointment;
    public static int selectedAppointmentID;
    public static String selectedAppointmentType;
    public static int chosenAppointmentID;
    public static int chosenCustomerID;
    public static String chosenTitle;
    public static String chosenDescription;
    public static Object chosenContactID;
    public static String chosenType;
    public static String chosenLocation;
    public static String chosenDate;
    public static String chosenStartDate;
    public static String chosenEndDate;
    public static int chosenUserID;
    public static String chosenContactName;
    public static String chosenContact;
    public static String dateOnly;
    public static String startTimeOnly;
    public static String endTimeOnly;
    private final DateTimeFormatter formatForDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter formatForTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    /**
     * This section is triggered by the Schedule meeting button
     * it takes the user to the add meeting screen
     * @param actionEvent the schedule meeting button takes the user to the add meeting screen
     * @throws IOException
     */
    public void scheduleButton(ActionEvent actionEvent) throws IOException {
        //This takes the user to the  add meeting screen
        Parent addAppointment = FXMLLoader.load(getClass().getResource("/View/AddAppointment.fxml"));
        Scene scene = new Scene(addAppointment);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This section is triggered when the update meeting button is clicked
     * It grabs the selected meeting row from the table view and stores it
     * it then takes the user to the Update meeting screen
     * @param actionEvent the update meeting button takes the user to the update meeting screen
     * @throws IOException
     */
    public void UpdateButton(ActionEvent actionEvent) throws IOException {

        //This pulls the items from the selected row
        updateAppointment = (Appointments) schedulerTable.getSelectionModel().getSelectedItem();

        //this checks to see if an meeting has been selected
        if (updateAppointment != null) {

            //This gets the selected meeting ID
            chosenAppointmentID = updateAppointment.getAppointmentID();

            //This gets the selected employee ID
            chosenCustomerID = updateAppointment.getCustomerID();

            //This gets the title text
            chosenTitle = updateAppointment.getTitle();

            //This gets the description text
            chosenDescription = updateAppointment.getDescription();

            //This gets the contact ID
            chosenContactID = updateAppointment.getContactID();

            //This gets the contact name
            chosenContactName = updateAppointment.getContact();

            //This gets the contact ID
            chosenContact = chosenContactID + "-" + chosenContactName;

            //This gets the selected type
            chosenType = updateAppointment.getType();

            //This gets the location
            chosenLocation = updateAppointment.getLocation();

            //This gets the date
            chosenDate = updateAppointment.getStart();

            //this displays only the date portion
            dateOnly = chosenDate.substring(0, 10);

            //This gets the selected start
            chosenStartDate = updateAppointment.getStart();

            //This gets only the time portion of the start datetime
            startTimeOnly = chosenStartDate.substring(11, 16);

            //This gets the selected end datetime
            chosenEndDate = updateAppointment.getEnd();

            //This gets the time only portion of the end datetime
            endTimeOnly = chosenEndDate.substring(11, 16);

            //This gets the selected user ID
            chosenUserID = updateAppointment.getUserID();

            //This takes the user to the update meeting screen
            Parent updateAppointment = FXMLLoader.load(getClass().getResource("/View/UpdateAppointment.fxml"));
            Scene scene = new Scene(updateAppointment);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            //This beings up an error box if no meeting selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No Meeting Selected");
            alert.setContentText("Please select a meeting to update.");
            alert.showAndWait();
            System.out.println("Error: No Meeting Selected");
        }
    }

    /**
     * This section is triggered by the logout button
     * it brings up a confirmation box
     * It then takes the user to the login screen and closes the connection to the database
     * @param actionEvent the logout button takes the user to the login screen and closes the database connection
     * @throws IOException
     */
    public void logoutButton(ActionEvent actionEvent) throws IOException {

        //This brings up a confirmation box to confirm logout
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("CONFIRMATION NEEDED");
        alert.setHeaderText("Confirm logout");
        alert.setContentText("Are you sure you want to logout?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {

            //This triggers the database to close if use hits OK
            DatabaseConnection.closeConnection();
            System.out.println("Logged Out");

            //This takes the user to the login screen
            Parent logout = FXMLLoader.load(getClass().getResource("/View/login.fxml")); //takes you back to the login screen
            Scene scene = new Scene(logout);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {

            //This closes the confirmation box and leaves user on the schedule screen
            alert.close();
        }
    }

    /**
     * This section is triggered by the cancel meeting button
     * It grabs the selected row from the table view and stores the information
     * It then takes the user to the add meeting screen after confirmation box is displayed
     * @param actionEvent the cancel meeting button deletes the selected meeting from the database
     * @throws IOException
     */
    public void cancelButton(ActionEvent actionEvent) throws IOException {

        //This pulls the selected row from the table
        Appointments selectedAppointment = (Appointments) schedulerTable.getSelectionModel().getSelectedItem();

        //This specifies we want the appointmentID from the selected row
        selectedAppointmentID = selectedAppointment.getAppointmentID();

        //This gets the selected type
        selectedAppointmentType = selectedAppointment.getType();

        //This brings up a confirmation box asking user if they want to cancel meeting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("CONFIRMATION NEEDED");
        alert.setHeaderText("Confirm Cancellation");
        alert.setContentText("Are you sure you want to cancel meeting ID " + selectedAppointmentID + ": " + selectedAppointmentType + "?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            //This deletes the meeting if ok is selected from confirmation box
            DatabaseAppointments.deleteAppointment();
            System.out.println("Meeting Deleted");

            //This takes the user to the schedule screen
            Parent cancelAppointment = FXMLLoader.load(getClass().getResource("/View/Scheduler.fxml")); //takes you back to the login screen
            Scene scene = new Scene(cancelAppointment);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            //This closes the confirmation box and leaves user on schedule screen
            alert.close();
        }
    }

    /**
     * This section is triggered by the weekly tab
     * It displays all of the meetings in the next 7 days
     * <p><b>
     * The Lambda expression is used to easily filter the meetings by week.
     * This is more efficient than having a separate SQL Query to pull that data.
     * This allows me to pull from the getAllAppointments observable list that I am
     * already using to populate the table view.
     * </b></p>
     * @param actionEvent the weekly button filters the meetings by week in the tableview
     */
    public void weeklyButton(ActionEvent actionEvent) {

        //This gets the current dateTime
        LocalDate now = LocalDate.now();

        //This gets current dateTime plus a week
        LocalDate addWeek = now.plusWeeks(1);

        //This is the dateTime formatter
        DateTimeFormatter formatForDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //This lambda expression is used to filter all meetings by week
        FilteredList<Appointments> filteredWeekly = new FilteredList<>(getAllAppointments());
        filteredWeekly.setPredicate(row -> {

            LocalDate rowDate = LocalDate.parse(row.getStart(), formatForDateTime);

            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(addWeek);
        });

        schedulerTable.setItems(filteredWeekly);
    }

    /**
     * This section is triggered by the monthly tab
     * It displays all meetings in the next month
     * <p><b>
     * The Lambda expression is used to easily filter the meetings by month.
     * This is more efficient than having a separate SQL Query to pull that data.
     * This allows me to pull from the getAllAppointments observable list that I am
     * already using to populate the table view.
     * </b></p>
     * @param actionEvent the monthly button filters meetings by month in the tableview
     */
    public void monthlyButton(ActionEvent actionEvent) {

        //This gets the current dateTime
        LocalDate now = LocalDate.now();

        //This gets current dateTime plus a week
        LocalDate addMonth = now.plusMonths(1);

        //This is the dateTime formatter
        DateTimeFormatter formatForDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //This lambda expression is used to filter all meetings by week
        FilteredList<Appointments> filteredMonthly = new FilteredList<>(getAllAppointments());
        filteredMonthly.setPredicate(row -> {

            LocalDate rowDate = LocalDate.parse(row.getStart(), formatForDateTime);

            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(addMonth);
        });

        schedulerTable.setItems(filteredMonthly);
    }

    /**
     * This section is triggered by the All tab
     * It displays all meetings
     * @param actionEvent the all button displays all meetings in the tableview
     */
    public void allButton(ActionEvent actionEvent) {

        //this sets tableview with all of the meetings
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentID")); //not pulling correct ID
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("End"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));

        //this tells it to set the filtered meetings in the table
        schedulerTable.setItems(getAllAppointments());
    }

    /**
     * This section is triggered by the Reports button
     * It takes the user to the reports screen
     * @param actionEvent the reports button takes the user to the reports screen
     * @throws IOException
     */
    public void reportsButton(ActionEvent actionEvent) throws IOException {

        //This takes the user to the reports screen
        Parent reportsView = FXMLLoader.load(getClass().getResource("/View/Reports.fxml"));
        Scene scene = new Scene(reportsView);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This section is triggered by the view employees button
     * It takes the user to the employees screen
     * @param actionEvent the view employee button takes the user to the employee screen
     * @throws IOException
     */
    public void viewCustomersButton(ActionEvent actionEvent) throws IOException {

        //This takes the user to the employee screen
        Parent customerView = FXMLLoader.load(getClass().getResource("/View/Customer.fxml"));
        Scene scene = new Scene(customerView);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This function sets the items in the scheduler table
     */
    public void setAppointments() {

        //This sets meetings in the table view
        schedulerTable.setItems(getAllAppointments());
    }

    /**
     * This event takes the String from the search box and compares it to the obseravable list
     * It will then change the table to only show the matching items.
     * @param actionEvent the meeting search button filters down the list to match the search term
     */
    public void meetingSearchButton(ActionEvent actionEvent) {
        String searchForMeeting = meetingSearchBox.getText();
        ObservableList foundAppointments = Appointments.lookupAppointment(searchForMeeting);

        if (foundAppointments.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("There was a problem with your search.");
            alert.setContentText("No meetings were found.");
            alert.showAndWait();
        } else {
            schedulerTable.setItems(foundAppointments);
        }
    }

    /** This section is everything that needs to run upon initialization*/
    @Override
    public void initialize (URL url, ResourceBundle rb) {

        //this creates a toggle group for the toggle buttons
        ToggleGroup toggleButtons = new ToggleGroup();

        //this adds the weekly, monthly, and all buttons to the toggle group so only one is selected at a time
        allButtonToggle.setToggleGroup(toggleButtons);

        //this sets the all button to be selected by default
        allButtonToggle.setSelected(true);
        monthlyButtonToggle.setToggleGroup(toggleButtons);
        weeklyButtonToggle.setToggleGroup(toggleButtons);

        //This sets the items in the tableview
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("End"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));

        //this tells it to set the meeting items in the table
        setAppointments();

        //This is the users current date, plus a month and plus a week
        LocalDate now = LocalDate.now();
        LocalDate addMonth = now.plusMonths(1);
        LocalDate addWeek = now.plusWeeks(1);

    }
}
