package schedulingapplication.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {
    
    private String customerID, customerName, address, address2,
            city, postalCode, country, phone;
    
    private static ObservableList<Customer> oblist = FXCollections.observableArrayList();
    
    private static Customer customerToModify;


    public Customer(String customerID, String customerName, String address, String address2,
            String city, String postalCode, String country, String phone) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
    }
    
    public static Customer getCustomerToModify() {
        return customerToModify;
    }
    
    public static void setCustomerToModify(Customer customer) {
        customerToModify = customer;
    }
    
    public static ObservableList<Customer> getCustomerList() {
        return oblist;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    
    
}
