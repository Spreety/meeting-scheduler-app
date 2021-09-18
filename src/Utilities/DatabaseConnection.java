package Utilities;

import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**This class creates the connection to the database*/
public class DatabaseConnection {
    //JDBC Connection Parts
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String serverName = "//wgudb.ucertify.com/WJ062WQ?autoReconnect=true&useSSL=false";

    //concatenates all parts to form the URL
    private static final String jdbcURL = protocol + vendor + serverName;

    //Driver and Connection interface reference
    private static final String MySQLjdbcDriver = "com.mysql.jdbc.Driver";
    private static Connection connect = null;

    //username
    private static final String username = "U062WQ";

    //password
    private static final String password = "53688675754";

    //This starts the database connection
    public static Connection startConnection() {
        try {
            Class.forName(MySQLjdbcDriver);
            connect = (Connection) DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return connect;
    }

    //this closes the database connection
    public static void closeConnection() {
        try {
            connect.close();
            System.out.println("Connection Closed");
        }  catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}