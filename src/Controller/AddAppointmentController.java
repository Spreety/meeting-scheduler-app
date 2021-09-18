package Controller;

import DatabaseAccess.DatabaseAppointments;
import Model.Appointments;
import Model.Contacts;
import Model.Customers;
import Model.Users;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import static DatabaseAccess.DatabaseContacts.getAllContacts;
import static DatabaseAccess.DatabaseCustomers.getAllCustomers;
import static DatabaseAccess.DatabaseUsers.getAllUsers;

/** This class creates the add appointment screen */
public class AddAppointmentController implements Initializable {
    public Label addAppointmentTitle;
    public TextField appointmentIDField;
    public Label appointmentIDLabel;
    public Label customerIDLabel;
    public Label contactLabel;
    public TextField titleField;
    public TextArea descriptionBox;
    public Label typeLabel;
    public Label locationLabel;
    public TextField locationField;
    public Label userIDLabel;
    public Label dateLabel;
    public Label startTimeLabel;
    public Label endTimeLabel;
    public ComboBox<Contacts> contactCombBox;
    public ComboBox startCombo;
    public ComboBox endCombo;
    public ComboBox <Customers> customerIDCombo;
    public ComboBox <Users> userIDcombo;
    public ComboBox <String> typeCombo;
    public DatePicker datePickerField;

    private int appointmentID;
    public static String Customer_ID;
    public String contactComboSelection;
    public static LocalDate date;
    public static String startTime;
    public static String endTime;
    private String appointmentExceptionMessage = new String();
    private final DateTimeFormatter formatForDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter formatForTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    static ZoneId localZoneId;
    static ZoneId etZoneId;
    public static ZonedDateTime startZDT;
    public static ZonedDateTime endZDT;
    private ZonedDateTime currentStartETime;
    private ZonedDateTime currentEndETime;
    private LocalTime businessHourStart;
    private LocalTime businessHourEnd;
    private LocalTime startETimeOnly;
    private LocalTime endETimeOnly;

    //This sets language based on users location
    ResourceBundle rb = ResourceBundle.getBundle("Utilities/Nat", Locale.getDefault());

    /**
     * This section gets the string values from the selected tableview row
     * Time conversion to different formats and zones
     * @param actionEvent  The save button triggers checks for overlapping appointments etc.
     */
    public void saveButton(ActionEvent actionEvent) {
        /**this section gets the string value from the date picker and the start and end combo boxes and combines them*/

        //This pulls the value from the date picker
        date = datePickerField.getValue();

        //this pulls the value from the start time combo box
        startTime = String.valueOf(startCombo.getValue());
        endTime = String.valueOf(endCombo.getValue());

        //this pulls the start and end times and converts it to LocalTime
        LocalTime startTime1 = (LocalTime) startCombo.getValue();
        LocalTime endTime1 = (LocalTime) endCombo.getValue();

        /**this section pulls the selected start and end times and converts them to UTC*/

        //this sets the users local time zone
        localZoneId = ZoneId.systemDefault();

        //These pull the selected start and end times and converts them to a ZonedDateTime
        startZDT = ZonedDateTime.of(date, startTime1, localZoneId);
        endZDT = ZonedDateTime.of(date, endTime1, localZoneId);

        //These convert the start and end ZonedDateTimes to UTC
        LocalDateTime currentStartUTCTime = startZDT.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime currentEndUTCTime = endZDT.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        String convertedStart = String.valueOf(currentStartUTCTime);
        String convertedEnd = String.valueOf(currentEndUTCTime);

       /**this section pulls the local time and converts it to eastern time*/

        //this sets EST
        etZoneId = ZoneId.of("America/New_York");

        //this sets the users local time zone
        localZoneId = ZoneId.systemDefault();

        //These convert the start and end ZonedDateTimes to EST
        currentStartETime = startZDT.withZoneSameInstant(etZoneId);
        currentEndETime = endZDT.withZoneSameInstant(etZoneId);

        //These take the converted start and end times and pull the time only
        startETimeOnly = currentStartETime.toLocalTime();
        endETimeOnly = currentEndETime.toLocalTime();

        //These set the business hour start and end times
        businessHourStart = LocalTime.of(8, 00);
        businessHourEnd = LocalTime.of(22,00);

        /**This pulls the text or values from the different fields*/

        //This pulls the meeting ID from the meeting ID field
        int Appointment_ID = Integer.parseInt(appointmentIDField.getText());

        //This pulls the title from the title field
        String Title = titleField.getText();

        //This pulls the description from the description field
        String Description = descriptionBox.getText();

        //This pulls the location from the location field
        String Location = locationField.getText();

        //this gets the string value of the selected item in contact combo box
        contactComboSelection = String.valueOf(contactCombBox.getValue());

        //this only takes the first character of the string to input in the database
        int Contact_ID = Integer.parseInt(String.valueOf(contactComboSelection.charAt(0)));

        //These pull the combined date times so it matches what SQL needs
        String Start = convertedStart;
        String End = convertedEnd;

        //This pulls the employee ID from the combo box
        Customer_ID = String.valueOf(customerIDCombo.getValue());

        //This pulls the user ID from the combo box
        Users User_ID = userIDcombo.getValue();

        //This pulls the type from the combo box
        String Type = typeCombo.getValue();

        //this gets the user for created_by field
        String userName = LoginController.usernameText;

        //this gets the username so it can be saved as Created_By
        String Created_By = userName;

        //This gets the username to be updated as last updated by
        String Last_Updated_By = userName;

        //this calls the function to check for overlapping meetings
        Boolean apptOverlap;
        apptOverlap = DatabaseAppointments.overlappingAppt();

        /**This section runs check for overlapping meetings, hours outside business hours and start before end
         * If no issues are found, it allows the new meeting to be added to the database
         */
        try {
            appointmentExceptionMessage = Appointments.appointmentValidation(Appointment_ID, Title, Description, Location, Start, End, Created_By, Last_Updated_By, Customer_ID, User_ID, Type, Contact_ID, appointmentExceptionMessage);
            //this checks for incomplete fields
            if (appointmentExceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Unable to add meeting.");
                alert.setContentText(appointmentExceptionMessage);
                alert.showAndWait();
                System.out.println("Error: Unable to add Meeting");
                appointmentExceptionMessage = "";

            }
            //this checks to make sure that the end time is AFTER the start time
            else if (endTime1.equals(startTime1) || endTime1.isBefore(startTime1)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Error Found With Start And/Or End Time");
                alert.setContentText("The Meeting end time must be after the start time.");
                alert.showAndWait();
                System.out.println("Error: End time must be after start time");
            }
            //this checks to make sure the start time is within business hours
            else if (startETimeOnly.isBefore(businessHourStart)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Selected Start Time is Outside of Business Hours");
                alert.setContentText("Please select a new start time.");
                alert.showAndWait();
                System.out.println("Error: Selected time outside of business hours");
            }
            //this checks to make sure the end time is within business hours
            else if (endETimeOnly.isAfter(businessHourEnd) || endETimeOnly.equals(businessHourEnd)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Selected End Time is Outside of Business Hours");
                alert.setContentText("Please select a new end time.");
                alert.showAndWait();
                System.out.println("Error: Selected time outside of business hours");
            }
            //This checks for overlapping meetings
            else if(apptOverlap == true) {
                //this alerts of and overlapping meeting
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Conflicting Meeting");
                alert.setContentText("This employee already has an meeting that conflicts with these times.");
                alert.showAndWait();
                System.out.println("Error: Overlapping Meeting Error");
            }
        else {
                //This triggers the addAppointments SQL Query
                DatabaseAppointments.addAppointment(Title, Description, Location, Start, End, Created_By, Last_Updated_By, Customer_ID, User_ID, Type, Contact_ID);

                //This takes you to the schedule screen
                Parent savePart = FXMLLoader.load(getClass().getResource("/View/Scheduler.fxml")); //takes you back to main screen
                Scene scene = new Scene(savePart);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (IOException e) {
        }
}

    /**
     * This is the cancel button and returns user to the scheduler screen
     * @param actionEvent the cancel button takes the user back to the scheduler screen
     * @throws IOException
     */
    public void cancelButton(ActionEvent actionEvent) throws IOException {
        //This asks if the user is sure they want to cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("CONFIRMATION NEEDED");
        alert.setHeaderText(rb.getString("Confirm") + " " + rb.getString("cancel"));
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait();

        //If user says ok, it takes them back to the schedule screen
        if (alert.getResult() == ButtonType.OK) {
            System.out.println("Action Cancelled");
            Parent cancelAdd = FXMLLoader.load(getClass().getResource("/View/Scheduler.fxml")); //takes you back to the login screen
            Scene scene = new Scene(cancelAdd);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            //this closes the confirmation box and the user stays on the add meeting screen
            alert.close();
        }
    }

    public void contactCombo(ActionEvent actionEvent) {
    }

    public void datePicker(ActionEvent actionEvent) {
    }

    public void customerIDCombo(ActionEvent actionEvent) {
    }

    public void userIDCombo(ActionEvent actionEvent) {
    }

    public void typeComboBox(ActionEvent actionEvent) {
    }

    public void startCombo(ActionEvent actionEvent) {
    }

    public void endCombo(ActionEvent actionEvent) {
    }


    @Override
    public void initialize (URL url, ResourceBundle rb) {
        /** This section is everything that needs to run upon initialization*/

        //this auto sets the meeting ID
        appointmentIDField.setText(String.valueOf(appointmentID));

        //this sets all contacts in the contact combo box
        contactCombBox.setItems(getAllContacts());

        //this sets all employee ID's in the combo box
        customerIDCombo.setItems(getAllCustomers());

        //this sets all user ID's in the combo box
        userIDcombo.setItems(getAllUsers());

        //this sets the items in tne Type combo box
        ObservableList<String> types = FXCollections.observableArrayList();
        types.addAll("Planning Session", "De-Briefing", "Training", "Team Meeting", "Other");
        typeCombo.setItems(types);


        //this sets the start times in the combo box
        LocalTime start1 = LocalTime.of(8,0);
        LocalTime end1 = LocalTime.of(21,45);

        //This sets the start time values for every 15 minutes
        while(start1.isBefore(end1.plusSeconds(1))){
            startCombo.getItems().add(start1);
            start1 = start1.plusMinutes(15);
        }

        //this sets the end times in the combo box.
        LocalTime start2 = LocalTime.of(8,15);
        LocalTime end2 = LocalTime.of(22,0);

        //This sets the end time values for every 15 minutes
        while(start2.isBefore(end2.plusSeconds(1))){
            endCombo.getItems().add(start2);
            start2 = start2.plusMinutes(15);
        }
    }
}
