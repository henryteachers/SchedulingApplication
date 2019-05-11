package schedulingapplication.View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import schedulingapplication.Model.NewScreen;

public class ReportsScreenController implements Initializable {

    @FXML
    private Label mainLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void appointmentTypes(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("ApptTypesScreen.fxml")); 
    }

    @FXML
    private void consultantSchedules(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("ConsultantSchedulesScreen.fxml")); 
    }

    @FXML
    private void custMultipleAppt(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("CustApptsScreen.fxml")); 
    }

    @FXML
    private void mainMenu(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("MainScreen.fxml")); 
    }    
}
