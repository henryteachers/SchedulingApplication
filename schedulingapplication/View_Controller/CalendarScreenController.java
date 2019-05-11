package schedulingapplication.View_Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import schedulingapplication.Model.Appointment;
import schedulingapplication.Model.NewScreen;

public class CalendarScreenController implements Initializable {
    
    ObservableList<Appointment> allList = Appointment.getAppointmentList();
    ObservableList<Appointment> weekList = FXCollections.observableArrayList();
    ObservableList<Appointment> monthList = FXCollections.observableArrayList();    
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
    LocalDateTime ldt;

    @FXML
    private Label mainLabel;
    @FXML
    private TableView<Appointment> calendarTable;
    @FXML
    private TableColumn<Appointment, String> id;
    @FXML
    private TableColumn<Appointment, String> start;
    @FXML
    private TableColumn<Appointment, String> end;
    @FXML
    private TableColumn<Appointment, String> consultant;
    @FXML
    private TableColumn<Appointment, String> type;
    @FXML
    private TableColumn<Appointment, String> customer;
    @FXML
    private TableColumn<Appointment, String> title;
    @FXML
    private TableColumn<Appointment, String> description;
    @FXML
    private TableColumn<Appointment, String> location;
    @FXML
    private TableColumn<Appointment, String> contact;
    @FXML
    private TableColumn<Appointment, String> url;
    @FXML
    private ToggleGroup calendar;

    @Override
    public void initialize(URL url2, ResourceBundle rb) {
        //Lambda expression to check dates and determine those with 7 days and 30 days
        allList.forEach(n -> {
            ldt = LocalDateTime.parse(n.getStart(), dtf);
            if (ldt.isAfter(LocalDateTime.now())
                    && ldt.isBefore(LocalDateTime.now().plusDays(7)))
                {
                weekList.add(n);
                monthList.add(n);
                };
            if (ldt.isAfter(LocalDateTime.now().plusDays(7))
                    && ldt.isBefore(LocalDateTime.now().plusDays(30)))
                monthList.add(n);            
        });
        
        //Initally display Appointments the occur within the next seven days
        calendarTable.setItems(weekList);
        
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
    private void mainMenu(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("MainScreen.fxml")); 
    }

    @FXML
    private void week(ActionEvent event) {
        //Display appointments that occur within the next 7 days
        calendarTable.setItems(weekList);
    }

    @FXML
    private void month(ActionEvent event) {
        //Display appointments that occur within the next 30 days
        calendarTable.setItems(monthList);        
    }
    
}
