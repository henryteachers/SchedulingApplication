package schedulingapplication.View_Controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import schedulingapplication.Model.Appointment;
import schedulingapplication.Model.NewScreen;
import schedulingapplication.Model.User;
import schedulingapplication.Model.Query;

public class LoginScreenController implements Initializable {
    
    ObservableList<Appointment> allList = Appointment.getAppointmentList();    
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
    LocalDateTime ldt;
    Locale currentLocale = Locale.getDefault();
    
    @FXML
    private PasswordField password;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField username;
    @FXML
    private Label mainLabel;
    @FXML
    private Label instructionLabel;    
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButtonName;
    @FXML
    private Button quitButtonName;
    
    @FXML
    void login(ActionEvent event) throws IOException, SQLException {       
        
        //Check username and password against user information in the database
        //Translate into spanish if the language setting is "es"
        Query.makeQuery("SELECT userId FROM user "
                + "WHERE userName = " + "'" + username.getText() + "'");
        ResultSet rs = Query.getResult();
        String id;
        if(rs.next()) {
            id = rs.getString("userId");
            Query.makeQuery("SELECT password FROM user "
                    + "WHERE userId = " + "'" + id + "'");
            rs = Query.getResult();
            String pass;
            if(rs.next()) {
                pass = rs.getString("password");
                
                //Error message presented if password is not correct
                if(!password.getText().equals(pass)) {
                    if(currentLocale.getLanguage().equals("es")) {
                        errorLabel.setText("El contraseña no esta corecto.  "
                                + "Por favor, inténtalo de nuevo");                    
                    }
                    else {
                        errorLabel.setText("The password is not correct.  Please try again.");
                    }            
                    username.setText("");
                    password.setText("");
                }
                else {                    
                    User.setUser(username.getText());
                    
                    Logger userLog = Logger.getLogger("log.txt");        
         
                    //Write the user activity to a file names log.txt 
                    FileHandler myFileHandler = new FileHandler("log.txt", true);
                    SimpleFormatter mySimpleFormatter = new SimpleFormatter();
                    myFileHandler.setFormatter(mySimpleFormatter);
                    userLog.addHandler(myFileHandler);           

                    userLog.log(Level.INFO, "Consultant " + User.getUser()
                        + " logged in at " + LocalDateTime.now());                                        
                    
                    //Lambda expression to alert for an appointment starting within the next 15 minutes
                    allList.forEach(n -> {
                        ldt = LocalDateTime.parse(n.getStart(), dtf);
                        if (n.getConsultantName().equals(User.getUser()) 
                                && ldt.isAfter(LocalDateTime.now())
                                && ldt.isBefore(LocalDateTime.now().plusMinutes(15)))
                            { 
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Alert!");
                            alert.setHeaderText("Appointment within the next 15 minutes!");
                            alert.setContentText("Consultant " + User.getUser() +
                                    ", you have an appointment with " 
                                    + n.getCustomerName() + " at " + ldt.toLocalTime() + ".");
                            alert.showAndWait();
                            };                     
                    });                    
                    
                    //Open Main Screen if username and password are correct
                    NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                        getClass().getResource("MainScreen.fxml"));                     
                } 
            }            
        }
        else {
            //Error message presented if username does not exist
            if(currentLocale.getLanguage().equals("es")) {
                errorLabel.setText("El nombre de usuario no existe.  "
                        + "Por favor, inténtalo de nuevo");                    
            }
            else {
                errorLabel.setText("The username does not exist.  Please try again.");
            }            
            username.setText("");
            password.setText("");
        }        
    }

    @FXML
    void quit(ActionEvent event) {
        Platform.exit();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        
        //present information in spanish if language setting is "es"
        if(currentLocale.getLanguage().equals("es")) {
            mainLabel.setText("Solicitud de Programación");
            instructionLabel.setText("Ingrese su nombre de usuario y contraseña");
            usernameLabel.setText("Nombre de Usuario");
            passwordLabel.setText("Contraseña");
            loginButtonName.setText("Iniciar Sesión");
            quitButtonName.setText("Dejar");
        }
            
    }  

}
