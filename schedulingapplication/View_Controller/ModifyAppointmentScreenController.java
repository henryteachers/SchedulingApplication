package schedulingapplication.View_Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.Duration;
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

public class ModifyAppointmentScreenController implements Initializable {
    
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
    Appointment appt;    
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
    private DatePicker calendar;
    @FXML
    private ChoiceBox<String> type;
    @FXML
    private ChoiceBox<String> hour;
    @FXML
    private ChoiceBox<String> duration;
    @FXML
    private ChoiceBox<String> customer;
    @FXML
    private ChoiceBox<String> consultant;
    @FXML
    private ChoiceBox<String> min;
    
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
        
        //get the Appointment that needs to be updated
        appt = Appointment.getAppointmentToModify();
        
        //Get customer and consultant names from database
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
        
        //Populate customer names, consultant names, and appointment types in the choice boxes
        customer.setValue(appt.getCustomerName());
        customer.setItems(customerList);
        
        consultant.setValue(appt.getConsultantName());
        consultant.setItems(consultantList);
        
        type.setValue(appt.getType());
        type.setItems(apptTypes);
        
        //Get start and end times and parse to LocalDateTime objects
        LocalDateTime apptStart = LocalDateTime.parse(appt.getStart(), dtf);
        LocalDateTime apptEnd = LocalDateTime.parse(appt.getEnd(), dtf);

        //Set the calendar to the correct date of the appointment
        calendar.setValue(apptStart.toLocalDate());
        
        //Disable days in the past as well as Saturdays and Sundays
        Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        calendar.setDayCellFactory(dayCellFactory);

        //Set the hours and minutes to the correct values for the given appointment
        hour.setValue(String.valueOf(apptStart.getHour()));
        if(apptStart.getHour() == 8)
            hour.setValue("08");
        else if(apptStart.getHour() == 9)
            hour.setValue("09");
        hour.setItems(apptHours);
        
        min.setValue(String.valueOf(apptStart.getMinute()));
        if(apptStart.getMinute() == 0)
            min.setValue("00");
        min.setItems(apptMins);
        
        //Set the duration of the appointment to the correct value
        Duration dur = Duration.between(apptStart, apptEnd);
        duration.setValue(String.valueOf(dur.toMinutes()));
        duration.setItems(apptDuration);
        
        //Set other fields to the correct values
        title.setText(appt.getTitle());
        description.setText(appt.getDescription());
        location.setText(appt.getLocation());
        contact.setText(appt.getContact());
        urlText.setText(appt.getUrl());
        
    }    

    @FXML
    private void submit(ActionEvent event) throws SQLException, IOException {
        
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
                else
                {
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

                    //Get the appointmentId primary key from the appointment table
                    Query.makeQuery("SELECT appointmentId FROM appointment "
                            + "WHERE appointment.customerId = " + "'" + custId + "'" 
                            + "AND appointment.userId = " + "'" + usrId + "'");            
                    rs = Query.getResult();
                    while(rs.next())
                        apptId = rs.getString("appointmentId");

                    //Update any changes in the appointment table
                    //Start and end dates are converted to UTC time to store in the database
                    Query.makeQuery("UPDATE appointment SET "
                            + "customerId = " + "'" + custId + "', "
                            + "userId = " + "'" + usrId + "', "
                            + "title = " + "'" + title.getText() + "', "
                            + "description = " + "'" + description.getText() + "', "
                            + "location = " + "'" + location.getText() + "', "
                            + "contact = " + "'" + contact.getText() + "', "
                            + "type = " + "'" + type.getValue() + "', "
                            + "url = " + "'" + urlText.getText() + "', "
                            + "start = " + "'" + DBTimeStamp.convertToUTC(apptDateTime) + "', "
                            + "end = " + "'" + DBTimeStamp.convertToUTC(apptDateTime.plusMinutes(Long.valueOf(duration.getValue()))) + "', "                
                            + "lastUpdate = " + "'" + LocalDateTime.now() + "', "
                            + "lastUpdateBy = " + "'" + User.getUser() + "' "
                            + "WHERE appointmentId = " + "'" + apptId + "'");            

                    //Update Appointment information in ObservableList
                    appt.setCustomerName(customer.getValue());
                    appt.setConsultantName(consultant.getValue());
                    appt.setTitle(title.getText());
                    appt.setDescription(description.getText());
                    appt.setLocation(location.getText());
                    appt.setContact(contact.getText());
                    appt.setType(type.getValue());
                    appt.setUrl(urlText.getText());
                    appt.setStart(apptDateTime.format(dtf));
                    appt.setEnd(apptDateTime.plusMinutes(Long.valueOf(duration.getValue())).format(dtf));        

                    //Return to Appointment Screen after updating the Appointment information
                    NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                            getClass().getResource("AppointmentScreen.fxml"));
                }
            }           
        }
    }

    @FXML
    private void cancel(ActionEvent event) throws IOException {            
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("AppointmentScreen.fxml"));
    }    
}