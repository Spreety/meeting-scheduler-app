package Model;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.Timestamp;

/** This class is where the constructors and getters are kept */
public class Users {

    public int userID;
    public static String userNameDB;
    public static String passwordDB;



    //constructors
    public Users(int userID, String userNameDB, String passwordDB) {
        this.userID = userID;
        this.userNameDB = userNameDB;
        this.passwordDB = passwordDB;

    }

    //getters

    public int getUserID() {
        return userID;
    }

    public static String getUserName() {
        return userNameDB;
    }

    public static String getPassword() {
        return passwordDB;
    }



    //this overrides the toString in relation to the combo boxes
    @Override

    public String toString() {
        return String.valueOf(userID); //this returns only user ID
    }
}
