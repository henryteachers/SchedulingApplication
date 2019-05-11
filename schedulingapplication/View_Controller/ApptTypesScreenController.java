package schedulingapplication.View_Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import schedulingapplication.Model.Appointment;
import schedulingapplication.Model.MonthlyAppointmentTypes;
import schedulingapplication.Model.NewScreen;

public class ApptTypesScreenController implements Initializable {
    
    ObservableList<Appointment> allList = FXCollections.observableArrayList();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
    LocalDateTime apr = LocalDateTime.of(2019, Month.APRIL, 1, 0, 0, 0);
    LocalDateTime may = LocalDateTime.of(2019, Month.MAY, 1, 0, 0, 0);
    LocalDateTime jun = LocalDateTime.of(2019, Month.JUNE, 1, 0, 0, 0);
    LocalDateTime jul = LocalDateTime.of(2019, Month.JULY, 1, 0, 0, 0);
    LocalDateTime aug = LocalDateTime.of(2019, Month.AUGUST, 1, 0, 0, 0);
    LocalDateTime sep = LocalDateTime.of(2019, Month.SEPTEMBER, 1, 0, 0, 0);
    LocalDateTime oct = LocalDateTime.of(2019, Month.OCTOBER, 1, 0, 0, 0);

    @FXML
    private Label mainLabel;
    @FXML
    private TableView<MonthlyAppointmentTypes> apptTypesTable;
    @FXML
    private TableColumn<MonthlyAppointmentTypes, String> month;
    @FXML
    private TableColumn<MonthlyAppointmentTypes, Integer> phone;
    @FXML
    private TableColumn<MonthlyAppointmentTypes, Integer> virtualChat;
    @FXML
    private TableColumn<MonthlyAppointmentTypes, Integer> screenShare;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Start with empty Observable List
        MonthlyAppointmentTypes.getApptTypes().clear();
        
        //Add monthly totals
        MonthlyAppointmentTypes.getApptTypes().add(new MonthlyAppointmentTypes(
            "April",
            Appointment.getMonthlyTotal("Phone", apr, may),
            Appointment.getMonthlyTotal("Virtual Chat", apr, may),
            Appointment.getMonthlyTotal("Screen Share", apr, may)));
        MonthlyAppointmentTypes.getApptTypes().add(new MonthlyAppointmentTypes(
            "May",
            Appointment.getMonthlyTotal("Phone", may, jun),
            Appointment.getMonthlyTotal("Virtual Chat", may, jun),
            Appointment.getMonthlyTotal("Screen Share", may, jun)));
        MonthlyAppointmentTypes.getApptTypes().add(new MonthlyAppointmentTypes(
            "June",
            Appointment.getMonthlyTotal("Phone", jun, jul),
            Appointment.getMonthlyTotal("Virtual Chat", jun, jul),
            Appointment.getMonthlyTotal("Screen Share", jun, jul)));
        MonthlyAppointmentTypes.getApptTypes().add(new MonthlyAppointmentTypes(
            "July",
            Appointment.getMonthlyTotal("Phone", jul, aug),
            Appointment.getMonthlyTotal("Virtual Chat", jul, aug),
            Appointment.getMonthlyTotal("Screen Share", jul, aug)));
        MonthlyAppointmentTypes.getApptTypes().add(new MonthlyAppointmentTypes(
            "August",
            Appointment.getMonthlyTotal("Phone", aug, sep),
            Appointment.getMonthlyTotal("Virtual Chat", aug, sep),
            Appointment.getMonthlyTotal("Screen Share", aug, sep)));
        MonthlyAppointmentTypes.getApptTypes().add(new MonthlyAppointmentTypes(
            "September",
            Appointment.getMonthlyTotal("Phone", sep, oct),
            Appointment.getMonthlyTotal("Virtual Chat", sep, oct),
            Appointment.getMonthlyTotal("Screen Share", sep, oct)));
         
        //Disply monthly totals in TableView
        apptTypesTable.setItems(MonthlyAppointmentTypes.getApptTypes());
        
        month.setCellValueFactory(new PropertyValueFactory<>("month"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        virtualChat.setCellValueFactory(new PropertyValueFactory<>("virtualChat"));
        screenShare.setCellValueFactory(new PropertyValueFactory<>("screenShare"));        
    }    

    @FXML
    private void reportsMenu(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("ReportsScreen.fxml")); 
    }
    
}
