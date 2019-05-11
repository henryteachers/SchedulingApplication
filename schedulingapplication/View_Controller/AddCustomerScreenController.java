package schedulingapplication.View_Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import schedulingapplication.Model.Customer;
import schedulingapplication.Model.User;
import schedulingapplication.Model.NewScreen;
import schedulingapplication.Model.Query;

public class AddCustomerScreenController implements Initializable {

    @FXML
    private Label mainLabel;
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField phone;
    @FXML
    private TextField country;
    @FXML
    private TextField postalCode;
    @FXML
    private TextField address2;
    @FXML
    private TextField city;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void submit(ActionEvent event) throws IOException, SQLException {       
        
        ResultSet rs;
        String tempString = null;
        String errorMsg = "";
        Boolean anyErrors = false;
        
        //Create a detailed error message if information is missing
        if (name.getText().equals("")) {
            errorMsg += "\nEnter a name.";
            anyErrors = true;
        }            
        if (address.getText().equals("")) {
            errorMsg += "\nEnter an address (Address 2 is optional).";
            anyErrors = true;
        }
        if (city.getText().equals("")) {
            errorMsg += "\nEnter a city.";
            anyErrors = true;
        }
        if (postalCode.getText().equals("")) {
            errorMsg += "\nEnter a postal code.";
            anyErrors = true;
        }
        try {
            if (!postalCode.getText().equals(""))
                Integer.parseInt(postalCode.getText());                        
        }
        catch (NumberFormatException ex) {
            errorMsg += "\nEnter a numeric postal code.";
            anyErrors = true;
        }
        if (country.getText().equals("")) {
            errorMsg += "\nEnter a country.";   
            anyErrors = true;
        }
        if (phone.getText().equals("")) {
            errorMsg += "\nEnter a phone number in the form ###-####.";
            anyErrors = true;
        }
        else if (phone.getText().length() != 8) {
                errorMsg += "\nEnter a phone number in the form ###-####.";
                anyErrors = true;
        }
        else try {
            Integer.parseInt(phone.getText().substring(0,2));
            Integer.parseInt(phone.getText().substring(4,8));
        }
        catch (NumberFormatException ex) {
            errorMsg += "\nEnter a phone number in the form ###-####.";
            anyErrors = true;
        }
        
        //Error message if required fields are not complete
        if (anyErrors) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("Not all of the required information has been entered.");
            alert.setContentText("Please complete the following:" + errorMsg);
            alert.showAndWait();
        }
        else {                
            //If country does not already exist, add it to database
            Query.makeQuery("SELECT country FROM country "
                    + "WHERE country.country = " + "'" + country.getText() + "'");
            rs = Query.getResult();
            if (rs.next() == false)
                Query.makeQuery("INSERT INTO country(country, createDate, createdBy,"
                    + "lastUpdate, lastUpdateBy) VALUES("
                    + "'" + country.getText() + "', "
                    + "'" + LocalDateTime.now() + "', "
                    + "'" + User.getUser() + "', "
                    + "'" + LocalDateTime.now() + "', "
                    + "'" + User.getUser() + "')");                

            //If city does not already exist, add it to database
            Query.makeQuery("SELECT city FROM city "
                    + "WHERE city.city = " + "'" + city.getText() + "'");
            rs = Query.getResult();
            if (rs.next() == false) {
                Query.makeQuery("SELECT countryId FROM country WHERE country.country = "
                    + "'" + country.getText() + "'");
                rs = Query.getResult();
                while (rs.next())
                    tempString = rs.getString("countryId");
                Query.makeQuery("INSERT INTO city(city, countryId, createDate, createdBy,"
                    + "lastUpdate, lastUpdateBy) VALUES("
                    + "'" + city.getText() + "', " 
                    + "'" + tempString + "', "
                    + "'" + LocalDateTime.now() + "', "
                    + "'" + User.getUser() + "', "
                    + "'" + LocalDateTime.now() + "', "
                    + "'" + User.getUser() + "')");
            }

            //Add info to address table in database
            Query.makeQuery("SELECT cityId FROM city WHERE city.city = "
                + "'" + city.getText() + "'");
            rs = Query.getResult();
            while (rs.next())
                tempString = rs.getString("cityId");
            Query.makeQuery("INSERT INTO address(address, address2, cityId, postalCode, "
                + "phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES("
                + "'" + address.getText() + "', "
                + "'" + address2.getText() + "', "
                + "'" + tempString + "', "
                + "'" + postalCode.getText() + "', "
                + "'" + phone.getText() + "', "
                + "'" + LocalDateTime.now() + "', "
                + "'" + User.getUser() + "', "
                + "'" + LocalDateTime.now() + "', "
                + "'" + User.getUser() + "')");

            //Add info to customer table in database
            Query.makeQuery("SELECT addressId FROM address WHERE address.address = "
                    + "'" + address.getText() + "'");
            rs = Query.getResult();
            while (rs.next())
                tempString = rs.getString("addressId");
            Query.makeQuery("INSERT INTO customer(customerName, addressId, active, "
                    + "createDate, createdBy, lastUpdate, lastUpdateBy) VALUES("
                    + "'" + name.getText() + "', "
                    + "'" + tempString + "'" + ", '1', "
                    + "'" + LocalDateTime.now() + "', "
                    + "'" + User.getUser() + "', "
                    + "'" + LocalDateTime.now() + "', "
                    + "'" + User.getUser() + "')");

            //Get customerID from the database and add customer to the ObservableList
            Query.makeQuery("SELECT customerId FROM customer "
                    + "WHERE customerName = " + "'" + name.getText() + "'");
            rs = Query.getResult();
            while(rs.next())
                tempString = rs.getString("customerId");
            Customer.getCustomerList().add(new Customer(tempString, name.getText(),
                    address.getText(), address2.getText(), city.getText(),
                    postalCode.getText(), country.getText(), phone.getText()));

            //Switch back to Customer Screen
            NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                    getClass().getResource("CustomerScreen.fxml")); 
        }
    }

    @FXML
    private void cancel(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("CustomerScreen.fxml"));         
    }
    
}
