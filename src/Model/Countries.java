package Model;

import java.sql.Time;
import java.sql.Timestamp;

/** This class is where the constructors and getters are kept */
public class Countries {

    private int countryID;
    private String country;
    private String createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    //constructors
    public Countries(int countryID, String country, String createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.countryID = countryID;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    //getters
    public int getCountryID() {
        return countryID;
    }

    public String getCountry() {
        return country;
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

    //this overrides the toString in relation to the combo boxes
    @Override

    public String toString() {
        return getCountry(); //this returns only type
    }
}
