package schedulingapplication.View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import schedulingapplication.Model.NewScreen;

public class MainScreenController implements Initializable {
    
    @FXML
    private Label mainLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {        
    }    

    @FXML
    private void appointments(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("AppointmentScreen.fxml")); 
    }

    @FXML
    private void customers(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("CustomerScreen.fxml")); 
    }

    @FXML
    private void calendar(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("CalendarScreen.fxml")); 
    }

    @FXML
    private void reports(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("ReportsScreen.fxml")); 
    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }    
}
