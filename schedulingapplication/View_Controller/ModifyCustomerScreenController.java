package schedulingapplication.View_Controller;

import java.io.IOException;
import java.net.URL;
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
import schedulingapplication.Model.NewScreen;
import schedulingapplication.Model.Query;
import schedulingapplication.Model.User;

public class ModifyCustomerScreenController implements Initializable {

    Customer cust;
    
    @FXML
    private Label mainLabel;
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField address2;
    @FXML
    private TextField city;
    @FXML
    private TextField postalCode;
    @FXML
    private TextField country;
    @FXML
    private TextField phone;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //get the Customer that needs to be modified
        cust = Customer.getCustomerToModify();
        
        //initialize fields with the Customer information
        name.setText(cust.getCustomerName());
        address.setText(cust.getAddress());
        address2.setText(cust.getAddress2());
        city.setText(cust.getCity());
        postalCode.setText(cust.getPostalCode());
        country.setText(cust.getCountry());
        phone.setText(cust.getPhone());
    }    

    @FXML
    private void submit(ActionEvent event) throws IOException {
        
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
            //update Customer information in the country database table
            Query.makeQuery("UPDATE country "
                    + "SET country = " + "'" + country.getText() + "'" + ", "
                    + "lastUpdate = " + "'" + LocalDateTime.now() + "', "
                    + "lastUpdateBy = " + "'" + User.getUser() + "' "
                    + "WHERE country = " + "'" + cust.getCountry() + "'");

            //update Customer information in the city database table
            Query.makeQuery("UPDATE city "
                    + "SET city = " + "'" + city.getText() + "'" + ", "
                    + "lastUpdate = " + "'" + LocalDateTime.now() + "', "
                    + "lastUpdateBy = " + "'" + User.getUser() + "' "
                    + "WHERE city = " + "'" + cust.getCity() + "'");

            //update Customer information in the address database table
            Query.makeQuery("UPDATE address "
                    + "SET address = " + "'" + address.getText() + "'" + ", "
                    + "address2 = " + "'" + address2.getText() + "'" + ", "
                    + "postalCode = " + "'" + postalCode.getText() + "'" + ", "
                    + "phone = " + "'" + phone.getText() + "'" + ", "
                    + "lastUpdate = " + "'" + LocalDateTime.now() + "', "
                    + "lastUpdateBy = " + "'" + User.getUser() + "' "
                    + "WHERE address = " + "'" + cust.getAddress() + "'");

            //update Customer information in the customer database table
            Query.makeQuery("UPDATE customer "
                    + "SET customerName = " + "'" + name.getText() + "'" + ", "
                    + "lastUpdate = " + "'" + LocalDateTime.now() + "', "
                    + "lastUpdateBy = " + "'" + User.getUser() + "' "
                    + "WHERE customerName = " + "'" + cust.getCustomerName() + "'");

            //update Customer information in the ObservableList
            cust.setCustomerName(name.getText());
            cust.setAddress(address.getText());
            cust.setAddress2(address2.getText());
            cust.setCity(city.getText());
            cust.setPostalCode(postalCode.getText());
            cust.setCountry(country.getText());
            cust.setPhone(phone.getText());        

            //return to the main screen after updating the Customer information
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
