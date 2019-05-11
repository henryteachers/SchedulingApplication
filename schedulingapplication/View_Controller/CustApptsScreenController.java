package schedulingapplication.View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import schedulingapplication.Model.Appointment;
import schedulingapplication.Model.CustomerTotalAppointments;
import schedulingapplication.Model.MonthlyAppointmentTypes;
import schedulingapplication.Model.NewScreen;

public class CustApptsScreenController implements Initializable {

    @FXML
    private Label mainLabel;
    @FXML
    private TableView<CustomerTotalAppointments> apptTypesTable;
    @FXML
    private TableColumn<CustomerTotalAppointments, String> customer;
    @FXML
    private TableColumn<CustomerTotalAppointments, String> appointments;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        CustomerTotalAppointments.getApptTotals().clear();
        int total = 0;
        boolean duplicate = false;
        
        for (Appointment appt: Appointment.getAppointmentList()) {
            total = Appointment.getCustomerApptTotal(appt.getCustomerName());
            for (CustomerTotalAppointments cta: CustomerTotalAppointments.getApptTotals())
                if(appt.getCustomerName().equals(cta.getName()))
                    duplicate = true;
            if (!duplicate)
                CustomerTotalAppointments.getApptTotals().add(
                    new CustomerTotalAppointments(appt.getCustomerName(), total));
        }
        
        apptTypesTable.setItems(CustomerTotalAppointments.getApptTotals());
        
        customer.setCellValueFactory(new PropertyValueFactory<>("name"));
        appointments.setCellValueFactory(new PropertyValueFactory<>("appts")); 
    }    

    @FXML
    private void reportsMenu(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("ReportsScreen.fxml")); 
    }
    
}
