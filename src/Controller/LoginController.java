package Controller;

import DatabaseAccess.DatabaseAppointments;
import DatabaseAccess.DatabaseUsers;
import Model.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/** This class created the login screen */
public class LoginController implements Initializable {
    public PasswordField passwordField;
    public TextField usernameField;
    public Label titleText;
    public Label zone;
    public Button signIn;


    private String errorMessage = new String();
    private String username = Users.getUserName();
    public static String usernameText;
    public static String passwordText;
    private final DateTimeFormatter formatForDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter formatForTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    //This sets language based on users location
    ResourceBundle rb = ResourceBundle.getBundle("Utilities/Nat", Locale.getDefault());

    /**
     * This section is triggered by the login button
     * It gets the text entered into the username and password fields
     * it then checks those against the database
     * It also triggers a check for meetings user have in the next 15 minutes
     * @param actionEvent the sign in button checks username and password against database and takes user to scheduler screen
     * @throws IOException
     * @throws SQLException
     */
    public void signInButton(ActionEvent actionEvent) throws IOException, SQLException {

        //this pulls the entered text from the username field
        usernameText = usernameField.getText();

        //this pulls the entered text from the password field
        passwordText = passwordField.getText();

        /**this runs the loginValidation function and returns as a boolean. If there is a match with the DB, then it returns true.*/
        boolean userValidated = DatabaseUsers.loginValidation();
        if(userValidated) {
            //This triggers the upcoming appointment SQL
            DatabaseAppointments.upcomingAppointment();

            //this triggers the function to record the login attempt
            loginSuccessful();

            //this takes you to the scheduler screen
            Parent signIn = FXMLLoader.load(getClass().getResource("/View/Scheduler.fxml"));
            Scene scene = new Scene(signIn);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            //this triggers the function to record the login attempt
            loginFailed();

            //this alerts of incorrect password of username
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("ERROR"));
            alert.setHeaderText(rb.getString("Login") + " " + rb.getString("Failed"));
            alert.setContentText(rb.getString("Incorrect") + " " + rb.getString("Username") + " " + rb.getString("or")  + " " + rb.getString("Password"));
            alert.showAndWait();
            System.out.println("Login Failed");
        }
    }
    //This changes all of the login text to language based on user language settings
    public void setText() throws IOException {
        String titleLabel =rb.getString("Welcome");
        String username = rb.getString("Username");
        String password = rb.getString("Password");
        String signInText = rb.getString("Login");
        titleText.setText(titleLabel);
        usernameField.setPromptText(username);
        passwordField.setPromptText(password);
        signIn.setText(signInText);
    }

    //This sets the zone label based off users timezone
    public void setZone() throws IOException {
         String userZone = String.valueOf(ZoneId.of(TimeZone.getDefault().getID()));
         zone.setText(userZone);
     }

    /**
     * This section is triggered if login successful
     * It logs the login attempt in the login log file
     * @throws IOException
     */
    public void loginSuccessful() throws IOException {
        //This gets the users current date and time
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        //file name
        String fileName = "login_activity.txt";

        //This creates a file writer object
        FileWriter loginFile = new FileWriter(fileName, true);

        //This created the string to be input into the file
        loginFile.append(formatForDateTime.format(now) + "   " + "Username: " + usernameText + "   " + "Login Successful" + "\n");

        //This creates and opens the file
        PrintWriter outputFile = new PrintWriter(loginFile);

        //This closes the file
        outputFile.close();
        System.out.println("Login Attempt Recorded");
    }

    /**
     * This section is triggered if login fails
     * It logs the login attempt in the login log file
     * @throws IOException
     */
    public void loginFailed() throws IOException {
        //This gets the users current date and time
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        //file name
        String fileName = "login_activity.txt";

        //This creates a file writer object
        FileWriter loginFile = new FileWriter(fileName, true);

        loginFile.append(formatForDateTime.format(now) + "   " + "Username: " + usernameText + "   " + "Login Failed" + "\n");

        //This creates and opens the file
        PrintWriter outputFile = new PrintWriter(loginFile);


        //This closes the file
        outputFile.close();
        System.out.println("Login Attempt Recorded");
    }

    /** This section is everything that needs to run upon initialization*/
    @Override
    public void initialize (URL url, ResourceBundle rb) {


        //this calls the method to change zone label and text language
        try {
            setZone();
            setText();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
