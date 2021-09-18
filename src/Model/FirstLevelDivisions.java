package Model;

import javafx.collections.ObservableList;

import java.sql.Timestamp;

/** This class is where the constructors and getters are kept */
public class FirstLevelDivisions {

    private static int divisionID;
    private static String division;
    private String createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int countryID;

    //constructors
    public FirstLevelDivisions(int divisionID, String division, String createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryID = countryID;
    }

    public FirstLevelDivisions(String division) {
    }

    //getters
    public static int getDivisionID() {
        return divisionID;
    }

    public String getDivision() {
        return division;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public int getCountryID() {
        return countryID;
    }

//this overrides the toString in relation to the combo boxes
@Override

public String toString() {
    return String.valueOf(getDivision()); //this returns only divisions
}
}
