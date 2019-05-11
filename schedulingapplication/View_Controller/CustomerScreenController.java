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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import schedulingapplication.Model.Customer;
import schedulingapplication.Model.NewScreen;
import schedulingapplication.Model.Query;
import schedulingapplication.Model.User;

public class CustomerScreenController implements Initializable {

    @FXML
    private Label mainLabel;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> id;
    @FXML
    private TableColumn<Customer, String> name;
    @FXML
    private TableColumn<Customer, String> address;    
    @FXML
    private TableColumn<Customer, String> address2;
    @FXML
    private TableColumn<Customer, String> city;    
    @FXML
    private TableColumn<Customer, String> postalCode;
    @FXML
    private TableColumn<Customer, String> country;    
    @FXML
    private TableColumn<Customer, String> phone;


    @FXML
    void add(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("AddCustomerScreen.fxml")); 
    }

    @FXML
    void delete(ActionEvent event) {
        
        if (customerTable.getSelectionModel().getSelectedItem() == null)
        {   
            //Display alert if no selection has been made
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("No customer was selected.");
            alert.setContentText("Select a customer to delete.");
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

                //Get the Customer to delete from the TableView
                String itemToDelete = customerTable.getSelectionModel().getSelectedItem().getCustomerID();

                //Remove the Customer from the ObservableList
                Customer.getCustomerList().remove(customerTable.getSelectionModel().getSelectedItem()); 
                customerTable.setItems(Customer.getCustomerList());

                //Remove the Customer from the database
                Query.makeQuery("DELETE FROM customer WHERE customerID = " + itemToDelete);
            }
        }
    }

    @FXML
    void mainMenu(ActionEvent event) throws IOException {
        NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                getClass().getResource("MainScreen.fxml")); 
    }

    @FXML
    void modify(ActionEvent event) throws IOException {
        
        if (customerTable.getSelectionModel().getSelectedItem() == null)
        {   
            //Display alert if no selection has been made
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("No customer was selected.");
            alert.setContentText("Select a customer to modify.");
            alert.showAndWait();           
        }
        else
        {        
            //Identify the Customer that will be modified
            Customer.setCustomerToModify(customerTable.getSelectionModel().getSelectedItem());

            //Open the Modify Customer Screen
            NewScreen.myScreen((Stage) mainLabel.getScene().getWindow(), 
                    getClass().getResource("ModifyCustomerScreen.fxml")); 
        }
    }
         
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Present Customer information in TableView
        customerTable.setItems(Customer.getCustomerList());
        
        id.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        address2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));        
    }    
    
}
