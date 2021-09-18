package DatabaseAccess;

import Controller.LoginController;
import Model.Users;
import Utilities.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class is where all of the SQL Queries related to users are kept */
public class DatabaseUsers {

    /**
     * This pulls all users from the database
     * @return this returns all users in the database
     */
    public static ObservableList<Users> getAllUsers() {
        ObservableList<Users> userList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM users";

            //Creates a prepared statement
            PreparedStatement ps = DatabaseConnection.startConnection().prepareStatement(sql);
            //Get results
            ResultSet rs = ps.executeQuery();
            //works through result set one row at a time
            while (rs.next()) {
                //get results data
                int userID = rs.getInt("User_ID");
                String usernameDB = rs.getString("User_Name");
                String passwordDB = rs.getString("Password");  //this should be Text type but getText not an option


                Users a = new Users(userID, usernameDB, passwordDB); //creates users based on result data
                userList.add(a); //add new user object to list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return userList; //return list
    }

    /**
     * This pulls the username and password entered by the user and verifies that it matches what is in the database.
     * @return this returns true if the username and password match what's in the database
     */
    public static boolean loginValidation() {

        //These get the username and password entered in the fields
        String username = LoginController.usernameText;
        String password = LoginController.passwordText;

        try{
            PreparedStatement psv = DatabaseConnection.startConnection().prepareStatement("SELECT * FROM users WHERE User_Name=? AND Password=?");
            psv.setString(1, username);
            psv.setString(2, password);
            ResultSet rs = psv.executeQuery();

            while(rs.next()) {

                if(rs.getString("User_Name").equals(username) && rs.getString("Password").equals(password))

                    return true;

            }

            return false;

        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
