package Model;

import DatabaseAccess.DatabaseAppointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static DatabaseAccess.DatabaseAppointments.getAllAppointments;

/** This class is where the constructors, getters, and validations are kept */
public class Appointments {

    private static ObservableList<Appointments> allAppointments = getAllAppointments();

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String start;
    private String end;
    private int customerID;
    private int contactID;
    private int userID;

    //constructors
    public Appointments(int appointmentID, String title, String description, String location, String contact, String type, String start, String end, int customerID, int contactID, int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.contactID = contactID;
        this.userID = userID;
    }

    //getters

    public int getAppointmentID() {
        return appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getContact() {
        return contact;
    }

    public String getType() {
        return type;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getContactID() {return contactID;}

    public int getUserID() { return userID;}

    public static ObservableList lookupAppointment(String searchTerm) {

        ObservableList<Appointments> foundAppointments = FXCollections.observableArrayList();

        if(searchTerm.length() == 0) {
            foundAppointments = allAppointments;

        }else{
            for (int i = 0; i < allAppointments.size(); i++) {

                if (allAppointments.get(i).getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundAppointments.add(allAppointments.get(i));
                }
                if (allAppointments.get(i).getDescription().toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundAppointments.add(allAppointments.get(i));
                }
                if (allAppointments.get(i).getLocation().toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundAppointments.add(allAppointments.get(i));
                }
                if (allAppointments.get(i).getContact().toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundAppointments.add(allAppointments.get(i));
                }
                if (allAppointments.get(i).getType().toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundAppointments.add(allAppointments.get(i));
                }
            }
        }
        return foundAppointments;
    }


    //Validation
    public static String appointmentValidation(int appointmentID, String title, String description, String location, String type, String start, String Created_By, String Last_Updated_By, String end, Users customerID, String userID, int contactID, String appointmentErrorMessage) {
        if (title == null || title.length() == 0) {
            appointmentErrorMessage = appointmentErrorMessage + "Please enter a title in the title field. ";
        }
        if (description == null || description.length() == 0) {
            appointmentErrorMessage = appointmentErrorMessage + "Please enter a description in the description box. ";
        }
        if (location == null || location.length() == 0) {
            appointmentErrorMessage = appointmentErrorMessage + "Please enter a location in the location field. ";
        }
        if (type == null || type.length() == 0) {
            appointmentErrorMessage = appointmentErrorMessage + "Please select a meeting type. ";
        }
        if (start == null || start.length() == 0) {
            appointmentErrorMessage = appointmentErrorMessage + "Please select a meeting start time. ";
        }
        if (end == null || end.length() == 0) {
            appointmentErrorMessage = appointmentErrorMessage + "Please select a meeting end time. ";
        }
        return appointmentErrorMessage;
    }

    //this overrides the toString in relation to the combo boxes
    @Override

    public String toString() {
        return getType(); //this returns only type
    }
}
