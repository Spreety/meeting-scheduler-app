package DatabaseAccess;

import Controller.AddAppointmentController;
import Controller.LoginController;
import Controller.SchedulerController;
import Controller.UpdateAppointmentController;
import Model.Appointments;
import Model.Users;
import Utilities.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

/** This class is where all of the SQL Queries related to appointments are kept */
public class DatabaseAppointments {
    public static ObservableList<Appointments> allAppointments;
    //this declares variable
    private static int appointmentToDelete;
    private static String startDate;
    private static String startPlusMonth;
    public static String createdReport1;
    public static String createdReport2;
    public static String createdReport3;
    private static final DateTimeFormatter formatForDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter formatForTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final ZoneId localZID = ZoneId.systemDefault();
    private static final ZoneId utcZID = ZoneId.of("UTC");

    //This sets language based on users location
    static ResourceBundle rb = ResourceBundle.getBundle("Utilities/Nat", Locale.getDefault());

    public static ObservableList<Appointments> getAllMeetings() {
        return allAppointments;
    }
    /**
     * This section pulls all meetings from database and puts them in an observable list
     */
    public static ObservableList<Appointments> getAllAppointments() {
        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Appointment_ID, Title, Description, Location, Contact_Name, Type, Start, End, Customer_ID, appointments.Contact_ID, User_ID FROM appointments, contacts WHERE appointments.Contact_ID = contacts.Contact_ID ORDER BY Start";

            //Creates a prepared statement
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement(sql);
            //Get results
            ResultSet rs = ps.executeQuery();
            //works through result set one row at a time
            while (rs.next()) {
                //get results data
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String contactName = rs.getString("Contact_Name");
                String type = rs.getString("Type");
                int customerID = rs.getInt("Customer_ID");
                int contactID = rs.getInt("Contact_ID");
                int userID = rs.getInt("User_ID");

                //This gets the database start time
                String startFromDB = rs.getString("start").substring(0, 19);

                //This gets the database start time
                String endFromDB = rs.getString("end").substring(0, 19);

                //This converts database start and end UTC times to LocalDateTime
                LocalDateTime startToLocal = LocalDateTime.parse(startFromDB, formatForDateTime);
                LocalDateTime endToLocal = LocalDateTime.parse(endFromDB, formatForDateTime);

                //This converts times from UTC to local zoneId
                ZonedDateTime localZoneStart = startToLocal.atZone(utcZID).withZoneSameInstant(localZID);
                ZonedDateTime localZoneEnd = endToLocal.atZone(utcZID).withZoneSameInstant(localZID);

                //This converts ZonedDateTime to a string
                String localStart = localZoneStart.format(formatForDateTime);
                String localEnd = localZoneEnd.format(formatForDateTime);


                //this adds to the appointments observable list
                Appointments a = new Appointments(appointmentID, title, description, location, contactName, type, localStart, localEnd, customerID, contactID, userID); //creates appointment based on result data
                appointmentList.add(a); //add new appointment object to list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return appointmentList; //return list
    }

    /**
     * This section pulls the meeting totals by type and month and compiles them into a report
     */
    public static void getAppointmentsTotals() {

        try {
            String sql = "SELECT Type, MONTHNAME(Start) as 'Month', COUNT(*) as 'Total' FROM appointments GROUP BY Type, MONTHNAME(Start);";

            //Creates a prepared statement
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement(sql);
            //Get results
            ResultSet rs = ps.executeQuery();
            //This creates a string builder to format the string
            StringBuilder compileReport = new StringBuilder();

            //This formats the headings
            compileReport.append(String.format("%1$-25s %2$-30s %3$s \n", "Total", "Month", "Type"));
            //This formats the line dividing headings from data
            compileReport.append(String.join("", Collections.nCopies(55, "*")));
            compileReport.append("\n");

            //works through result set one row at a time
            while(rs.next()) {
                //This formats the data in the columns
                compileReport.append(String.format("%1$-30s %2$-30s %3$s \n", rs.getInt("Total"),rs.getString("Month"), rs.getString("Type")));
            }
            //this creates the output in string form to be put in the textbox
            createdReport1 = compileReport.toString();
        }

        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This section pulls the meetings by contacts and compiles them into a report
     */
    public static void getContactSchedules() {

        try {
            String sql = "SELECT Contact_ID, Appointment_ID, Title, Type, Description, Start, End, Customer_ID FROM appointments GROUP BY Contact_ID, Appointment_ID ORDER BY Contact_ID, Start;";

            //Creates a prepared statement
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement(sql);
            //Get results
            ResultSet rs = ps.executeQuery();
            //This creates a string builder to format the string
            StringBuilder compileReport2 = new StringBuilder();
            //This formats the headings
            compileReport2.append(String.format("%1$-30s %2$-30s %3$-35s %4$-66s %5$-70s %6$-50s %7$-50s %8$s \n", "Contact_ID", "Appointment_ID", "Customer_ID", "Start", "End", "Type", "Title", "Description"));
            //This formats the line dividing headings from data
            compileReport2.append(String.join("", Collections.nCopies(250, "*")));
            compileReport2.append("\n");

            //works through result set one row at a time
            while(rs.next()) {

                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String type = rs.getString("Type");
                int customerID = rs.getInt("Customer_ID");
                int contactID = rs.getInt("Contact_ID");


                //This gets the database start time
                String startFromDB = rs.getString("start").substring(0, 19);

                //This gets the database start time
                String endFromDB = rs.getString("end").substring(0, 19);

                //This converts database start and end UTC times to LocalDateTime
                LocalDateTime startToLocal = LocalDateTime.parse(startFromDB, formatForDateTime);
                LocalDateTime endToLocal = LocalDateTime.parse(endFromDB, formatForDateTime);

                //This converts times from UTC to local zoneId
                ZonedDateTime localZoneStart = startToLocal.atZone(utcZID).withZoneSameInstant(localZID);
                ZonedDateTime localZoneEnd = endToLocal.atZone(utcZID).withZoneSameInstant(localZID);

                //This converts ZonedDateTime to a string
                String localStart = localZoneStart.format(formatForDateTime);
                String localEnd = localZoneEnd.format(formatForDateTime);
                System.out.println(localStart);
                System.out.println(localEnd);

                //This formats the data in the columns
                compileReport2.append(String.format("%1$-45s %2$-45s %3$-45s %4$-45s %5$-45s %6$-45s %7$-45s %8$s \n", rs.getInt("Contact_ID"),rs.getInt("Appointment_ID"),rs.getInt("Customer_ID"), localStart, localEnd, rs.getString("Type"), rs.getString("Title"), rs.getString("Description")));
            }
            //this creates the output in string form to be put in the textbox
            createdReport2 = compileReport2.toString();
        }

        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This section pulls employees, sorts them by country and compiles them into a report
     */
    public static void getCustomersByCountry() {

        try {
            String sql = "SELECT COUNT(customers.Customer_ID) as 'Total Customers', first_level_divisions.Division, countries.Country  FROM customers INNER JOIN first_level_divisions ON first_level_divisions.Division_ID = customers.Division_ID INNER JOIN countries ON countries.Country_ID = first_level_divisions.Country_ID GROUP BY Country, Division;";

            //Creates a prepared statement
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement(sql);
            //Get results
            ResultSet rs = ps.executeQuery();
            //This creates a string builder to format the string
            StringBuilder compileReport3 = new StringBuilder();
            //This formats the headings
            compileReport3.append(String.format("%1$-45s %2$-60s %3$s \n", "Total Customers", "Division", "Country"));
            //This formats the line dividing headings from data
            compileReport3.append(String.join("", Collections.nCopies(90, "*")));
            compileReport3.append("\n");

            //works through result set one row at a time
            while(rs.next()) {
                //This formats the data in the columns
                compileReport3.append(String.format("%1$-56s %2$-60s %3$s \n", rs.getInt("Total Customers"),rs.getString("Division"), rs.getString("Country")));
            }
            //this creates the output in string form to be put in the textbox
            createdReport3 = compileReport3.toString();
        }

        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This section adds new meetings to the database
     * @param Title this is the title of the meeting
     * @param Description this is the description of the meeting
     * @param Location this is the location of the meeting
     * @param Start this is the start time of the meeting
     * @param End tnis is the end time of the meeting
     * @param Created_By this captures the user who made the update
     * @param Last_Updated_By this captures the user who last updated the meeting
     * @param Customer_ID this is the employee ID number
     * @param User_ID this is the user ID number
     * @param Type this is the type of meeting
     * @param Contact_ID This is the contact ID number
     * @param <Last_Updated_By> this is user who last updated the meeting
     */
    public static <Last_Updated_By> void addAppointment(String Title, String Description, String Location, Object Start, Object End, String Created_By, String Last_Updated_By, String Customer_ID, Users User_ID, String Type, int Contact_ID) {
        try {

            //SQL Statement to Insert into table
            String sqla = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Created_By, Last_Updated_By, Customer_ID, User_ID, appointments.Contact_ID) VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

            //prepared statement
            PreparedStatement psa = DatabaseConnection.startConnection().prepareStatement(sqla, Statement.RETURN_GENERATED_KEYS);
            //fills in question marks
            //psa.setInt(1, Appointment_ID);
            psa.setString(1, Title);
            psa.setString(2, Description);
            psa.setString(3, Location);
            psa.setString(4, String.valueOf(Type));
            psa.setString(5, String.valueOf(Start));
            psa.setString(6, String.valueOf(End));
            psa.setString(7, String.valueOf(Created_By));
            psa.setString(8, String.valueOf(Last_Updated_By));
            psa.setString(9, String.valueOf(Customer_ID));
            psa.setString(10, String.valueOf(User_ID));
            psa.setString(11, String.valueOf(Contact_ID));

            //execute query and get results
            psa.execute();

            ResultSet rs = psa.getGeneratedKeys();
            rs.next();
            int appointmentID = rs.getInt(1); //this pulls index 1 since appoinment ID is number 1 in table

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This section updates the appointments
     * @param Title this is the title of the meeting
     * @param Description this is the description of the meeting
     * @param Location this is the location of the meeting
     * @param Start this is the start time of the meeting
     * @param End this is the end time of the meeting
     * @param Last_Update this is the timestamp of the update to the meeting
     * @param Last_Updated_By this is the user who last updated the meeting
     * @param Customer_ID this is the employee ID number
     * @param User_ID this is the user ID number
     * @param Type this is the type of meeting
     * @param Contact_ID this is the contact ID number
     */
    public static void updateAppointment(String Title, String Description, String Location, Object Start, Object End, String Last_Update, String Last_Updated_By, Object Customer_ID, Object User_ID, String Type, int Contact_ID) {
        int appointmentToUpdate = UpdateAppointmentController.appointmentID;

        try {
            //SQL statement to modify customer
            String sqlu = "UPDATE appointments set Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

            //prepared statement
            PreparedStatement psu = DatabaseConnection.startConnection().prepareStatement(sqlu);
            //fills in question marks
            psu.setString(1, Title);
            psu.setString(2, Description);
            psu.setString(3, Location);
            psu.setString(4, Type);
            psu.setString(5, String.valueOf(Start));
            psu.setString(6, String.valueOf(End));
            psu.setString(7, String.valueOf(Last_Update));
            psu.setString(8, String.valueOf(Last_Updated_By));
            psu.setString(9, String.valueOf(Customer_ID));
            psu.setString(10, String.valueOf(User_ID));
            psu.setInt(11, Contact_ID);
            psu.setInt(12, appointmentToUpdate);
            //execute query
            psu.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This section deletes the meeting from the database
     */
    public static void deleteAppointment() {   //must delete from main table first to keep referential integrity
        //This pulls the selected appointment ID from scheduler
        appointmentToDelete = SchedulerController.selectedAppointmentID;
        System.out.println(appointmentToDelete);
        try {

            //SQL statement
            String sqld = "DELETE from appointments WHERE Appointment_ID = ?";

            //create a prepared statement
            PreparedStatement psd = DatabaseConnection.startConnection().prepareStatement(sqld);
            //fills in questions marks
            psd.setInt(1, appointmentToDelete);
            //execute query and get results
            psd.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This section checks for overlapping meetings. If any are found, it returns true which triggers an alert
     * @return this returns true if there is an overlapping meeting
     */
    public static boolean overlappingAppt() {
        System.out.println("Checking for overlapping meetings");

        //These pulls the employee ID and converts to a string
        String customerIDs = AddAppointmentController.Customer_ID;
        int customerID = Integer.parseInt(customerIDs);

        //These pull the start and end time as zonedDateTime
        ZonedDateTime start = AddAppointmentController.startZDT;
        ZonedDateTime end = AddAppointmentController.endZDT;

        //These convert the start and end time from local time to UTC
        LocalDateTime currentStartUTCTime = start.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime currentEndUTCTime = end.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();


        try {
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement("SELECT* FROM appointments WHERE Customer_ID = ? AND Start BETWEEN ? AND ? OR Customer_ID = ? AND End BETWEEN ? AND ?");
            ps.setInt(1, customerID);
            ps.setTimestamp(2, Timestamp.valueOf(currentStartUTCTime));
            ps.setTimestamp(3, Timestamp.valueOf(currentEndUTCTime));
            ps.setInt(4, customerID);
            ps.setTimestamp(5, Timestamp.valueOf(currentStartUTCTime));
            ps.setTimestamp(6, Timestamp.valueOf(currentEndUTCTime));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This section checks for overlapping meetings when meeting is being updated
     * @return this returns true if there is an overlapping meeting
     */

    public static boolean overlappingApptUpdate() {

        //These get the input data needed
        int customerID = Integer.parseInt(UpdateAppointmentController.customerID);
        int appointmentID = UpdateAppointmentController.appointmentID;

        //These pull the start and end time as zonedDateTime
        ZonedDateTime start = UpdateAppointmentController.startZDT;
        ZonedDateTime end = UpdateAppointmentController.endZDT;

        //These convert the start and end time from local time to UTC
        LocalDateTime currentStartUTCTime = start.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime currentEndUTCTime = end.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

        try {
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement("SELECT* FROM appointments WHERE Appointment_ID <> ? AND Customer_ID = ? AND Start BETWEEN ? AND ? OR Appointment_ID <> ? AND Customer_ID = ? AND End BETWEEN ? AND ?");
            ps.setInt(1, appointmentID);
            ps.setInt(2, customerID);
            ps.setTimestamp(3, Timestamp.valueOf(currentStartUTCTime));
            ps.setTimestamp(4, Timestamp.valueOf(currentEndUTCTime));
            ps.setInt(5, appointmentID);
            ps.setInt(6, customerID);
            ps.setTimestamp(7, Timestamp.valueOf(currentStartUTCTime));
            ps.setTimestamp(8, Timestamp.valueOf(currentEndUTCTime));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This section checks for upcoming meetings in the next 15 minutes
     * @throws SQLException
     */
    public static void upcomingAppointment() throws SQLException {

        //This gets the user ID
        int UserID = FindUserID();

        /**This section converts local time to UTC*/
        //This pulls the users current time
        LocalDateTime now = LocalDateTime.now();

        //This gets users current zoneId
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime currentZDT = now.atZone(zid);

        //This converts the current zonedDateTime to UTC
        LocalDateTime dateTimeUTC = currentZDT.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

        //This converts the current zonedDateTime  plus 15 minutes to UTC
        LocalDateTime dateTimeUTC15 = dateTimeUTC.plusMinutes(15);

        try {
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement("SELECT* FROM appointments WHERE User_ID = ? AND Start BETWEEN ? AND ?;");
            ps.setInt(1, UserID);
            ps.setTimestamp(2, Timestamp.valueOf(dateTimeUTC));
            ps.setTimestamp(3, Timestamp.valueOf(dateTimeUTC15));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                //get results data
                int appointmentID = rs.getInt("Appointment_ID");
                //String start = rs.getString("Start");

                //This gets the database start time
                String startFromDB = rs.getString("start").substring(0, 19);

                //This converts database start and end UTC times to LocalDateTime
                LocalDateTime startToLocal = LocalDateTime.parse(startFromDB, formatForDateTime);

                //This converts times from UTC to local zoneId
                ZonedDateTime localZoneStart = startToLocal.atZone(utcZID).withZoneSameInstant(localZID);

                //This converts ZonedDateTime to a string
                String localStart = localZoneStart.format(formatForDateTime);

                //This gets the substring to separate start date and time
                String startDate = localStart.substring(0,10);
                String startTime = localStart.substring(11, 19);

                //This creates an alert if there is an appointment within 15 minutes
                String upcomingApptAlert = (rb.getString("You") + " " + rb.getString("have") + " " + rb.getString("an") + " " + rb.getString("upcoming") + " " + rb.getString("meeting") + ": \n" + rb.getString("Meeting") + " " + rb.getString("ID") + ": " + appointmentID + "\n " + rb.getString("Date") + ": " + startDate + "\n " + rb.getString("Time") + ": " + startTime);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(rb.getString("Upcoming") + " " + rb.getString("Meeting") + " " + rb.getString("Reminder"));
                alert.setHeaderText(rb.getString("Upcoming") + " " + rb.getString("Meeting") + " " + rb.getString("within") + " " + rb.getString("15") + " " +  rb.getString("Minutes"));
                alert.setContentText(upcomingApptAlert);
                alert.showAndWait();

            }

            else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(rb.getString("No") + " " + rb.getString("Upcoming") + " " + rb.getString("Meetings"));
                    alert.setHeaderText(rb.getString("You") + " " + rb.getString("do") + " " + rb.getString("not") + " " + rb.getString("have") + " " + rb.getString("any") + " " + rb.getString("upcoming") + " " + rb.getString("meetings") + ".");
                    alert.showAndWait();
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * This section gets the userID from username
     * @return this returns the user ID
     * @throws SQLException
     */

    private static int FindUserID() throws SQLException {
        String userName = LoginController.usernameText;
        int userID = -1;

        try {
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement("SELECT User_ID FROM users WHERE User_Name = ?");
            ps.setString(1, userName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //get results data
                userID = rs.getInt("User_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userID;
    }
}
