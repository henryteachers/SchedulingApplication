package schedulingapplication.View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import schedulingapplication.Model.Appointment;
import schedulingapplication.Model.NewScreen;
import schedulingapplication.Model.Query;

public class AppointmentScreenController implements Initializable {

    @FXML
    private Label mainLabel;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> id;
    @FXML
    private TableColumn<Appointment, String> customer;
    @FXML
    private TableColumn<Appointment, String> consultant;
    @FXML
    private TableColumn<Appointment, String> title;
    @FXML
    private TableColumn<Appointment, String> description;
    @FXML
    private TableColumn<Appointment, String> location;
    @FXML
    private TableColumn<Appointment, String> contact;
    @FXML
    private TableColumn<Appointment, String> type;
    @FXML
    private TableColumn<Appointment, String> url;
    @FXML
    private TableColumn<Appointment, String> start;
    @FXML
    private TableColumn<Appointment, String> end;

    @Override
    public void initialize(URL parameter_url, ResourceBundle rb) {
        
        //Display appointments in a TableView
        appointmentTable.setItems(Appointment.getAppointmentList());
        
        id.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        consultant.setCellValueFactory(new PropertyValueFactory<>("consultantName"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        url.setCellValueFactory(new PropertyValueFactory<>("url"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));      
    }    

    @FXML
    private void add(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("AddAppointmentScreen.fxml")); 
    }

    @FXML
    private void modify(ActionEvent event) throws IOException {

        if (appointmentTable.getSelectionModel().getSelectedItem() == null)
        {   
            //Display alert if no selection has been made
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment was selected.");
            alert.setContentText("Select an appointment to modify.");
            alert.showAndWait();           
        }
        else
        {                
            //Identify the Appointment to be modified
            Appointment.setAppointmentToModify(appointmentTable.getSelectionModel().getSelectedItem());

            //Open the Modify Appointment Screen
            NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                    getClass().getResource("ModifyAppointmentScreen.fxml")); 
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        
        if (appointmentTable.getSelectionModel().getSelectedItem() == null)
        {   
            //Display alert if no selection has been made
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment was selected.");
            alert.setContentText("Select an appointment to delete.");
            alert.showAndWait();           
        }
        else
        {    
            //Confirm the deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Please confirm");
            alert.setContentText("Are you sure you want to delete?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){  
            
                //Identify the Appointment to be deleted
                String itemToDelete = appointmentTable.getSelectionModel().getSelectedItem().getAppointmentId();

                //Remove the Appointment from the ObservableList
                Appointment.getAppointmentList().remove(appointmentTable.getSelectionModel().getSelectedItem()); 
                appointmentTable.setItems(Appointment.getAppointmentList());

                //Remove the Appointment from the database
                Query.makeQuery("DELETE FROM appointment WHERE appointmentID = " + itemToDelete);
            }
        }
    }

    @FXML
    private void mainMenu(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("MainScreen.fxml")); 
    }
    
}
