package schedulingapplication.View_Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import schedulingapplication.Model.Appointment;
import schedulingapplication.Model.DBTimeStamp;
import schedulingapplication.Model.NewScreen;
import schedulingapplication.Model.Query;
import schedulingapplication.Model.User;

public class AddAppointmentScreenController implements Initializable {

    ObservableList<String> customerList = FXCollections.observableArrayList("Choose Customer");
    ObservableList<String> consultantList = FXCollections.observableArrayList("Choose Consultant");
    ObservableList<String> apptTypes = 
            FXCollections.observableArrayList("Choose Type", "Phone", "Virtual Chat", "Screen Share");
    ObservableList<String> apptHours = 
            FXCollections.observableArrayList("Hour", "08", "09", "10", "11", "12", "13",
                    "14", "15", "16");
    ObservableList<String> apptMins = 
            FXCollections.observableArrayList("Min", "00", "15", "30", "45");
    ObservableList<String> apptDuration = 
            FXCollections.observableArrayList("Duration", "15", "30", "45", "60");
    ObservableList<Appointment> allList = FXCollections.observableArrayList();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
    
    @FXML
    private Label mainLabel;
    @FXML
    private TextField title;
    @FXML
    private TextField description;
    @FXML
    private TextField location;
    @FXML
    private TextField contact;
    @FXML
    private TextField urlText;
    @FXML
    private ChoiceBox<String> type;
    @FXML
    private DatePicker calendar;
    @FXML
    private ChoiceBox<String> hour;
    @FXML
    private ChoiceBox<String> min;
    @FXML
    private ChoiceBox<String> duration;
    @FXML
    private ChoiceBox<String> customer;
    @FXML
    private ChoiceBox<String> consultant;
    
    private Callback<DatePicker, DateCell> getDayCellFactory() {
 
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
 
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
 
                        // Disable Saturday, Sunday and days before the current day
                        if (item.getDayOfWeek() == DayOfWeek.SATURDAY ||
                                item.getDayOfWeek() == DayOfWeek.SUNDAY ||
                                item.isBefore((ChronoLocalDate)LocalDateTime.now().toLocalDate())) {
                            setDisable(true);                            
                        }                                               
                    }
                };
            }
        };
        return dayCellFactory;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Populate customer and consultant choiceboxes with current database info
        try {
            Query.makeQuery("SELECT customerName FROM customer");
            ResultSet rs = Query.getResult();
            while(rs.next()) 
                customerList.add(rs.getString("customerName"));
            
            Query.makeQuery("SELECT userName FROM user");
            rs = Query.getResult();
            while(rs.next())
                consultantList.add(rs.getString("userName"));
            
        } catch (SQLException ex) {
            Logger.getLogger(AddAppointmentScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Populate choiceboxes and set initial values
        customer.setValue("Choose Customer");
        customer.setItems(customerList);
        
        consultant.setValue("Choose Consultant");
        consultant.setItems(consultantList);
        
        type.setValue("Choose Type");
        type.setItems(apptTypes);

        hour.setValue("Hour");
        hour.setItems(apptHours);
        
        min.setValue("Min");
        min.setItems(apptMins);

        duration.setValue("Duration");
        duration.setItems(apptDuration);
        
        //Populate calendar with initial value of today's date
        calendar.setValue(LocalDateTime.now().toLocalDate());                  
        
        //Disable days in the past as well as Saturdays and Sundays
        Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        calendar.setDayCellFactory(dayCellFactory);
        
        //Indicate that these fields are optional
        title.setPromptText("Optional");
        description.setPromptText("Optional");
        location.setPromptText("Optional");
        contact.setPromptText("Optional");
        urlText.setPromptText("Optional");    
        
    }    

    @FXML
    private void submit(ActionEvent event) throws IOException, SQLException {  
        
        ResultSet rs;
        String custId = null;
        String usrId = null;
        String apptId = null;
        String errorMsg = "";        
        
        //Create a detailed error message if information is missing
        if (customer.getValue().equals("Choose Customer"))
            errorMsg += "\nSelect a customer.";
        if (consultant.getValue().equals("Choose Consultant"))
            errorMsg += "\nSelect a consultant.";
        if (type.getValue().equals("Choose Type"))
            errorMsg += "\nSelect an appointment type.";
        if (hour.getValue().equals("Hour"))
            errorMsg += "\nSelect an hour.";        
        if (min.getValue().equals("Min"))
            errorMsg += "\nSelect minutes.";
        if (duration.getValue().equals("Duration"))
            errorMsg += "\nSelect a duration.";
        
        //Error message if required fields are not complete
        if (customer.getValue().equals("Choose Customer") ||
                consultant.getValue().equals("Choose Consultant") ||
                type.getValue().equals("Choose Type") ||
                hour.getValue().equals("Hour") ||
                min.getValue().equals("Min") ||
                duration.getValue().equals("Duration")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("Not all of the required information has been selected.");
            alert.setContentText("Please complete the following:" + errorMsg);
            alert.showAndWait();
        }
        else {
            //Set the full date and time of the appointment
            LocalDateTime apptDateTime = LocalDateTime.of(
                    calendar.getValue().getYear(),
                    calendar.getValue().getMonthValue(),
                    calendar.getValue().getDayOfMonth(),
                    Integer.parseInt(hour.getValue()),
                    Integer.parseInt(min.getValue()));
            
            LocalDateTime endAppt = apptDateTime.plusMinutes(Long.parseLong(duration.getValue()));
            
            //Make sure date is at least four hours in the future
            if (apptDateTime.isBefore(LocalDateTime.now().plusHours(4)))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");
                alert.setHeaderText("Appointments must be made at least four hours in advance.");
                alert.setContentText("Please adjust the date and/or time of your appointment.");
                alert.showAndWait();
            }
            else {
                
                //Determine if there is overlap in appointments for either the consultants or the customers
                allList = Appointment.getAppointmentList();
                
//Lambda expression to test for overlap in appointments
                allList.forEach(n -> {
                    //Test to see if consultant has overlap appointments and set name
                    if (n.getConsultantName().equals(consultant.getValue())) {
                        if (apptDateTime.isAfter(LocalDateTime.parse(n.getStart(), dtf)) &&
                                apptDateTime.isBefore(LocalDateTime.parse(n.getEnd(), dtf))) {
                            Appointment.setOverlap(true);
                            Appointment.setOverlapName(consultant.getValue());
                        }
                        else if (endAppt.isAfter(LocalDateTime.parse(n.getStart(), dtf)) &&
                                endAppt.isBefore(LocalDateTime.parse(n.getEnd(), dtf))) {
                            Appointment.setOverlap(true);
                            Appointment.setOverlapName(consultant.getValue());
                        }
                        else if (apptDateTime.isBefore(LocalDateTime.parse(n.getStart(), dtf)) &&
                                endAppt.isAfter(LocalDateTime.parse(n.getEnd(), dtf))) {
                            Appointment.setOverlap(true);
                            Appointment.setOverlapName(consultant.getValue());
                        }
                        else if (apptDateTime.isBefore(LocalDateTime.parse(n.getStart(), dtf)) &&
                                endAppt.isAfter(LocalDateTime.parse(n.getEnd(), dtf))) {
                            Appointment.setOverlap(true);
                            Appointment.setOverlapName(consultant.getValue());
                        }
                        else if(apptDateTime.isEqual(LocalDateTime.parse(n.getStart(), dtf))) {
                            Appointment.setOverlap(true);
                            Appointment.setOverlapName(consultant.getValue());
                        }
                        else if(endAppt.isEqual(LocalDateTime.parse(n.getEnd(), dtf))) {
                            Appointment.setOverlap(true);
                            Appointment.setOverlapName(consultant.getValue());
                        }
                    }
                    //Test to see if customer has overlap appointments and set name
                    else if (n.getCustomerName().equals(customer.getValue())) {
                        if (apptDateTime.isAfter(LocalDateTime.parse(n.getStart(), dtf)) &&
                                apptDateTime.isBefore(LocalDateTime.parse(n.getEnd(), dtf))) {
                            Appointment.setOverlap(true);
                            Appointment.setOverlapName(customer.getValue());
                        }
                        else if (endAppt.isAfter(LocalDateTime.parse(n.getStart(), dtf)) &&
                                endAppt.isBefore(LocalDateTime.parse(n.getEnd(), dtf))) {
                            Appointment.setOverlap(true);
                            Appointment.setOverlapName(customer.getValue());
                        }
                        else if (apptDateTime.isBefore(LocalDateTime.parse(n.getStart(), dtf)) &&
                                endAppt.isAfter(LocalDateTime.parse(n.getEnd(), dtf))) {
                            Appointment.setOverlap(true);
                            Appointment.setOverlapName(customer.getValue());
                        }
                        else if (apptDateTime.isBefore(LocalDateTime.parse(n.getStart(), dtf)) &&
                                endAppt.isAfter(LocalDateTime.parse(n.getEnd(), dtf))) {
                            Appointment.setOverlap(true);
                            Appointment.setOverlapName(customer.getValue());
                        }
                        else if(apptDateTime.isEqual(LocalDateTime.parse(n.getStart(), dtf))) {
                            Appointment.setOverlap(true);
                            Appointment.setOverlapName(customer.getValue());
                        }
                        else if(endAppt.isEqual(LocalDateTime.parse(n.getEnd(), dtf))) {
                            Appointment.setOverlap(true);
                            Appointment.setOverlapName(customer.getValue());
                        }
                    }       
                });
                
                //Alert message if there is overlap; reset overlap to false
                if (Appointment.getOverlap()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error!");
                    alert.setHeaderText("There is an overlap of appointments for "
                        + Appointment.getOverlapName() + ".");
                    alert.setContentText("Please adjust the date and/or time of the appointment.");
                    alert.showAndWait();
                    Appointment.setOverlap(false);

                }
                else {
                    //Get the customerId foreign key to store in the appointment table
                    Query.makeQuery("SELECT customerId FROM customer "
                            + "WHERE customer.customerName = " + "'" + customer.getValue() + "'");
                    rs = Query.getResult();
                    while(rs.next())
                        custId = rs.getString("customerId");

                    //Get the userId foreign key to store in the appointment table
                    Query.makeQuery("SELECT userId FROM user "
                            + "WHERE user.userName = " + "'" + consultant.getValue() + "'");
                    rs = Query.getResult();
                    while(rs.next())
                        usrId = rs.getString("userId");

                    //Store appointment info in the appointment table of the database
                    Query.makeQuery("INSERT INTO appointment(customerId, userId, title, "
                            + "description, location, contact, type, url, start, end, "
                            + "createDate, createdBy, lastUpdate, lastUpdateBy) VALUES("
                            + "'" + custId + "', "
                            + "'" + usrId + "', "
                            + "'" + title.getText() + "', "
                            + "'" + description.getText() + "', "
                            + "'" + location.getText() + "', "
                            + "'" + contact.getText() + "', "
                            + "'" + type.getValue() + "', "
                            + "'" + urlText.getText() + "', "
                            + "'" + DBTimeStamp.convertToUTC(apptDateTime) + "', "
                            + "'" + DBTimeStamp.convertToUTC(apptDateTime.plusMinutes(Long.valueOf(duration.getValue()))) + "', "
                            + "'" + LocalDateTime.now() + "', "
                            + "'" + User.getUser() + "', "
                            + "'" + LocalDateTime.now() + "', "
                            + "'" + User.getUser() + "')");

                    //Get the appointmentId primary key from the appointment table
                    Query.makeQuery("SELECT appointmentId FROM appointment "
                            + "WHERE appointment.customerId = " + "'" + custId + "'" 
                            + "AND appointment.userId = " + "'" + usrId + "'");
                    rs = Query.getResult();
                    while(rs.next())
                        apptId = rs.getString("appointmentId");

                    //Add appointment info to ObservableList
                    Appointment.getAppointmentList().add(new Appointment(
                        apptId, customer.getValue(), consultant.getValue(), title.getText(),
                        description.getText(), location.getText(), contact.getText(),
                        type.getValue(), urlText.getText(), apptDateTime.format(dtf),
                        apptDateTime.plusMinutes(Long.valueOf(duration.getValue())).format(dtf)));

                    //Return to the Appointment Screen after adding a new appointment
                    NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                        getClass().getResource("AppointmentScreen.fxml")); 
                }
            }           
        }        
    }

    @FXML
    private void cancel(ActionEvent event) throws IOException {
        //Return to the Appointment Screen         
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("AppointmentScreen.fxml")); 
    }
}
