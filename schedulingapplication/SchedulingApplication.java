/*
 * Greg Henry
 * C195 Scheduling Application Project
 */
package schedulingapplication;

import schedulingapplication.Model.DBConnection;
import schedulingapplication.Model.Query;
import java.sql.ResultSet;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import schedulingapplication.Model.Appointment;
import schedulingapplication.Model.Customer;
import schedulingapplication.Model.DBTimeStamp;

public class SchedulingApplication extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View_Controller/LoginScreen.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("View_Controller/screens.css").toExternalForm());
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {         
        
        try {            
            //make connection to database
            DBConnection.makeConnection();        
            
            //get customer information from database tables
            Query.makeQuery("SELECT customerId, customerName, address, address2,"
                    + "city, postalCode, country, phone "
                    + "FROM customer, address, city, country "
                    + "WHERE customer.addressId = address.addressId "
                    + "AND address.cityId = city.cityId "
                    + "AND city.countryId = country.countryId "
                    + "ORDER BY customerID");
            ResultSet rs = Query.getResult();
            
            //create Customer objects with database info and add them to an ObservableList
            while(rs.next())
                Customer.getCustomerList().add(new Customer(
                        rs.getString("customerID"), rs.getString("customerName"),
                        rs.getString("address"), rs.getString("address2"), rs.getString("city"),
                        rs.getString("postalCode"), rs.getString("country"), rs.getString("phone"))); 
            
            //get appointment information from database tables
            Query.makeQuery("SELECT appointmentId, customerName, userName,"
                    + "title, description, location, contact, type, url, start, end "
                    + "FROM appointment, customer, user "
                    + "WHERE appointment.customerId = customer.customerId "
                    + "AND appointment.userId = user.userId");
            rs = Query.getResult();
            
            //create Appointment objects with database info and add them to an ObservableList
            while(rs.next()) 
                Appointment.getAppointmentList().add(new Appointment(
                    rs.getString("appointmentId"), rs.getString("customerName"),
                    rs.getString("userName"), rs.getString("title"), rs.getString("description"),
                    rs.getString("location"), rs.getString("contact"), rs.getString("type"),
                    rs.getString("url"), rs.getString("start"), rs.getString("end")));
            
            //create temporary ObservableList for the purpose of adjusting the times to display
            ObservableList<Appointment> allList = Appointment.getAppointmentList();

            //Lambda expression to convert times from the database, stored in UTC time, to Local time
            allList.forEach(n -> {
                n.setStart(String.valueOf(DBTimeStamp.convertToLocal(n.getStart())));
                n.setEnd(String.valueOf(DBTimeStamp.convertToLocal(n.getEnd())));
            });           
            
            //call the start method
            launch(args);
            
            //close the database after the application is terminated
            DBConnection.closeConnection();
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
        }
    }    
}