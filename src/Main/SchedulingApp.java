package Main;

import Utilities.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**This class creates a scheduling app*/
public class SchedulingApp extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        //This starts the application in the login screen
        Parent root = FXMLLoader.load(getClass().getResource("/View/login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

/** This is the main method. It is the first method that gets run*/
    public static void main(String[] args)  throws SQLException {

        //These are the different locations
        Locale france = new Locale("fr, FR");
        Locale english = new Locale("en, US");
        Locale locale = Locale.getDefault();

       //This sets language based on users location
        ResourceBundle rb = ResourceBundle.getBundle("Utilities/Nat", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr"))
            System.out.println(rb.getString("username"));


        launch(args);
        //this closes the database connection when the app is closed
        DatabaseConnection.closeConnection();
    }
}
