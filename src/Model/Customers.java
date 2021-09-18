package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static DatabaseAccess.DatabaseAppointments.getAllAppointments;
import static DatabaseAccess.DatabaseCustomers.getAllCustomers;

/** This class is where the constructors, getters, and validations are kept */
public class Customers {

    private static ObservableList<Customers> allCustomers = getAllCustomers();

    private int customerID;
    private String customerName;
    private String address;
    private String division;
    private String postalCode;
    private String country;
    private String phone;
    private static int divisionID;

    //constructors
    public Customers(int customerID, String customerName, String address, String division, String postalCode, String country, String phone) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.division = division;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
        this.divisionID = divisionID;
    }

    //getters
    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public String getDivision() {
        return division;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public static int getDivisionID() {
        return divisionID;
    }

    public static ObservableList lookupCustomer(String searchTerm) {

        ObservableList<Customers> foundCustomers = FXCollections.observableArrayList();

        if(searchTerm.length() == 0) {
            foundCustomers = allCustomers;

        }else{
            for (int i = 0; i < allCustomers.size(); i++) {

                if (allCustomers.get(i).getCustomerName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundCustomers.add(allCustomers.get(i));
                }
                if (allCustomers.get(i).getAddress().toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundCustomers.add(allCustomers.get(i));
                }
                if (allCustomers.get(i).getDivision().toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundCustomers.add(allCustomers.get(i));
                }
                if (allCustomers.get(i).getPostalCode().toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundCustomers.add(allCustomers.get(i));
                }
                if (allCustomers.get(i).getCountry().toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundCustomers.add(allCustomers.get(i));
                }
            }
        }
        return foundCustomers;
    }

    //Validation
    public static String customerValidation(int customerID, String customerName, String address, String postalCode, String phone, String Last_Updated_By, String Created_By, int divisionID, String customerErrorMessage) {
        if (customerName == null || customerName.length() == 0) {
            customerErrorMessage = customerErrorMessage + "Please enter a name. ";
        }
        if (address == null || address.length() == 0) {
            customerErrorMessage = customerErrorMessage + "Please enter an address. ";
        }
        if (postalCode == null || postalCode.length() == 0) {
            customerErrorMessage = customerErrorMessage + "Please enter a postal code. ";
        }
        if (phone == null || phone.length() == 0) {
            customerErrorMessage = customerErrorMessage + "Please enter a phone number. ";
        }
        return customerErrorMessage;
    }

    //this overrides the toString in relation to the combo boxes
    @Override

    public String toString() {
        return String.valueOf(customerID); //this returns only customer ID
    }
}
