package schedulingapplication.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerTotalAppointments {
    
    private String name;
    private int appts;
    
    private static ObservableList<CustomerTotalAppointments> apptTotals = FXCollections.observableArrayList();

    public CustomerTotalAppointments(String name, int appts) {
        this.name = name;
        this.appts = appts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAppts() {
        return appts;
    }

    public void setAppts(int appts) {
        this.appts = appts;
    }

    public static ObservableList<CustomerTotalAppointments> getApptTotals() {
        return apptTotals;
    }

    public static void setApptTotals(ObservableList<CustomerTotalAppointments> apptTotals) {
        CustomerTotalAppointments.apptTotals = apptTotals;
    }
    
    
}
