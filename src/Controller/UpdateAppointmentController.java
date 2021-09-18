package Controller;

import DatabaseAccess.DatabaseAppointments;
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
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static Controller.SchedulerController.chosenContactName;
import static DatabaseAccess.DatabaseContacts.getAllContacts;
import static DatabaseAccess.DatabaseCustomers.getAllCustomers;
import static DatabaseAccess.DatabaseUsers.getAllUsers;

/** This class created the screen to update appointments */
public class UpdateAppointmentController implements Initializable {
    public Label updateAppointmentTitle;
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
    public ComboBox customerIDCombo;
    public ComboBox userIDCombo;
    public ComboBox <String> typeCombo;
    public ComboBox contactCombo;
    public ComboBox startCombo;
    public ComboBox endCombo;
    public DatePicker datePickerField;

    public static String customerID;
    public String contactComboSelection;
    public String typeComboSelection;
    public String startComboSelection;
    public String endComboSelection;
    public Users userIDComboSelection;
    public static int appointmentID;
    private LocalDate Start;
    private LocalDate End;
    public static LocalDate date;
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

    private String appointmentExceptionMessage = new String();
    //This sets language based on users location
    ResourceBundle rb = ResourceBundle.getBundle("Utilities/Nat", Locale.getDefault());

    /**
     * This section gets the string values from the selected tableview row
     * Time conversion to different formats and zones
     * @param actionEvent The save button triggers checks for overlapping meetings etc.
     */
    public void saveButton(ActionEvent actionEvent) throws IOException {
        /**this section gets the string value from the date picker and the start and end combo boxes and combines them*/

        //This pulls the value from the date picker
        date = datePickerField.getValue();

        //this pulls the value from the start time combo box
        String startTime = String.valueOf(startCombo.getValue());

        //this pulls the value from the end time combo box
        String endTime = String.valueOf(endCombo.getValue());

        //this pulls the start and end times and converts it to LocalTime
        LocalTime startConvert = LocalTime.parse(startTime);
        LocalTime startTime1 = (LocalTime) startConvert;

        LocalTime endConvert = LocalTime.parse(endTime);
        LocalTime endTime1 = (LocalTime) endConvert;


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

        /** This section gets values from fields and comb boxes*/
        //This gets the meeting ID and parses it to an Int
        appointmentID = Integer.parseInt(appointmentIDField.getText());

        //this gets the value of employeeID combo box selection
        Object Customer_ID = customerIDCombo.getValue();

        //This parses the employeeID to String
        customerID = String.valueOf(customerIDCombo.getValue());

        //This gets the date from the date picker
        LocalDate Date = datePickerField.getValue();

        //This gets the title from the title field
        String Title = titleField.getText();

        //This gets the description from the description field
        String Description = descriptionBox.getText();

        //this gets the string value of the selected item in contact combo box
        contactComboSelection = String.valueOf(contactCombo.getValue());

        //this only takes the first character of the string to input in the database
        int Contact_ID = Integer.parseInt(String.valueOf(contactComboSelection.charAt(0)));

        //This gets the type from the type combo as a string
        typeComboSelection = String.valueOf(typeCombo.getValue());
        String Type = typeComboSelection;

        //This gets the location from the location field
        String Location = locationField.getText();

        //This gets this start from the combo box
        startComboSelection = String.valueOf(startCombo.getValue());

        //this pulls the combined date time so it matches what SQL needs
        String Start = convertedStart;

        //this pulls the combined date time so it matches what SQL needs
        String End = convertedEnd;

        //This sets the correct date time format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //this pulls the date time for GMT timezone
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        //this fills in Last_Update with the current GMT time
        String Last_Update = dateFormat.format(new Date());

        //this gets the user for Last_Updated_by field
        String Last_Updated_By = LoginController.usernameText;

        //This gets the end time from the combo box
        endComboSelection = String.valueOf(endCombo.getValue());

        //This gets the user ID from the combo box
        Object User_ID = userIDCombo.getValue();

       //This sets a boolean that triggers the overlapping meeting alert SQL Query
        Boolean apptOverlap;
        apptOverlap = DatabaseAppointments.overlappingApptUpdate();

        /**This section runs check for overlapping meetings, hours outside business hours and start before end
         * If no issues are found, it allows the new meeting to be added to the database
         */
        try {
            //this checks to make sure that the end time is AFTER the start time
            if (endTime1.equals(startTime1) || endTime1.isBefore(startTime1)) {
                //This brings up a warning box
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Error Found With Start And/Or End Time");
                alert.setContentText("The Meeting end time must be after the start time.");
                alert.showAndWait();
                System.out.println("Error: End time must be after start time");

            }
            //this checks to make sure the start time is within business hours
            else if (startETimeOnly.isBefore(businessHourStart)) {
                //This brings up a warning box
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Selected Start Time is Outside of Business Hours");
                alert.setContentText("Please select a new start time.");
                alert.showAndWait();
                System.out.println("Error: Selected time outside of business hours");

            }
            //this checks to make sure the end time is within business hours
            else if (endETimeOnly.isAfter(businessHourEnd)) {
                //This brings up a warning box
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Selected End Time is Outside of Business Hours");
                alert.setContentText("Please select a new end time.");
                alert.showAndWait();
                System.out.println("Error: Selected time outside of business hours");
            }
            //This checks for overlapping meetings
            else if(apptOverlap == true) {
                //this alerts of an overlapping meeting
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Conflicting Meeting");
                alert.setContentText("This customer already has a meeting that conflicts with these times.");
                alert.showAndWait();
                System.out.println("Error: Overlapping Meeting Error");
            }

            else {
                //This triggers the update meeting SQL query
                DatabaseAppointments.updateAppointment(Title, Description, Location, Start, End, Last_Update, Last_Updated_By, Customer_ID, User_ID, Type, Contact_ID);
                System.out.println("Meeting Updated");
                //This takes the user to the schedule screen
                Parent update = FXMLLoader.load(getClass().getResource("/View/Scheduler.fxml")); //takes you back to the appointment screen
                Scene scene = new Scene(update);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (IOException e) {
        }
    }

    /**
     * This cancels the update meeting and returns user to the scheduler screen.
     * @param actionEvent the cancel button returns user to the scheduler screen.
     * @throws IOException
     */
    public void CancelButton(ActionEvent actionEvent) throws IOException {

        //This brings up a confirmation box confirming cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("CONFIRMATION NEEDED");
        alert.setHeaderText("Confirm cancel");
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {

            //This takes the user to the schedule screen
            Parent cancelUpdate = FXMLLoader.load(getClass().getResource("/View/Scheduler.fxml")); //takes you back to the login screen
            Scene scene = new Scene(cancelUpdate);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {

            //This closes the confirmation box and leaves the user on the update meeting screen
            alert.close();
        }
    }

    public void datePicker(ActionEvent actionEvent) {
    }

    public void customerIDCombo(ActionEvent actionEvent) {
    }

    public void userIDCombo(ActionEvent actionEvent) {
    }

    public void typeCombo(ActionEvent actionEvent) {
    }

    public void contactCombo(ActionEvent actionEvent) {
    }

    public void startCombo(ActionEvent actionEvent) {
    }

    public void endCombo(ActionEvent actionEvent) {
    }

    /** This section is everything that needs to run upon initialization*/
    @Override
    public void initialize (URL url, ResourceBundle rb) {

        //this sets all employees in the employee combo box
        contactCombo.setItems(getAllContacts());

        //contactCombo.setPromptText(String.valueOf(SchedulerController.chosenContact));
        contactCombo.setValue(SchedulerController.chosenContactID + "-" + chosenContactName);

        //this sets all employee ID's in the combo box
        customerIDCombo.setValue(SchedulerController.chosenCustomerID);
        customerIDCombo.setItems(getAllCustomers());

        //this auto fills the meetingID field with selected meeting
        appointmentIDField.setText(String.valueOf(SchedulerController.chosenAppointmentID));

        //this auto fills the title field with selected meeting
        titleField.setText(SchedulerController.chosenTitle);

        //this auto fills the description box with selected meeting
        descriptionBox.setText(SchedulerController.chosenDescription);

        //this auto fills the location field
        locationField.setText(SchedulerController.chosenLocation);

        //this sets all user ID's in the combo box
        userIDCombo.setItems(getAllUsers());
        userIDCombo.setValue(SchedulerController.chosenUserID);

        //this sets the items in tne Type combo box
        ObservableList<String> types = FXCollections.observableArrayList();
        types.addAll("Planning Session", "De-Briefing", "Training", "Team Meeting", "Other");
        typeCombo.setItems(types);

        //this sets the selected item based on selected meeting
        typeCombo.setValue(SchedulerController.chosenType);

        //this fills in the datepicker field
        datePickerField.setValue(LocalDate.parse(SchedulerController.dateOnly));

        //this sets the value for the start time
        startCombo.setValue(SchedulerController.startTimeOnly);

        //this sets the value for the end time
        endCombo.setValue(SchedulerController.endTimeOnly);

        //this sets the local times, every 15 minutes in the start combo box.
        LocalTime start1 = LocalTime.of(8,0);
        LocalTime end1 = LocalTime.of(21,45);

        while(start1.isBefore(end1.plusSeconds(1))){
            startCombo.getItems().add(start1);
            start1 = start1.plusMinutes(15);
        }

        //this sets the local times, every 15 minutes in the end combo box.
        LocalTime start2 = LocalTime.of(8,15);
        LocalTime end2 = LocalTime.of(22,0);

        while(start2.isBefore(end2.plusSeconds(1))){
            endCombo.getItems().add(start2);
            start2 = start2.plusMinutes(15);
        }
    }
}
