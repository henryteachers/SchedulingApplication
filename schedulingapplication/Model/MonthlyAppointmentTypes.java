package schedulingapplication.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MonthlyAppointmentTypes {
    
    private String month;    
    private int phone, virtualChat, screenShare;
    private static ObservableList<MonthlyAppointmentTypes> apptTypes = FXCollections.observableArrayList();

    public MonthlyAppointmentTypes(String month, int phone, int virtualChat, int screenShare) {
        this.month = month;
        this.phone = phone;
        this.virtualChat = virtualChat;
        this.screenShare = screenShare;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getVirtualChat() {
        return virtualChat;
    }

    public void setVirtualChat(int virtualChat) {
        this.virtualChat = virtualChat;
    }

    public int getScreenShare() {
        return screenShare;
    }

    public void setScreenShare(int screenShare) {
        this.screenShare = screenShare;
    }

    public static ObservableList<MonthlyAppointmentTypes> getApptTypes() {
        return apptTypes;
    }

    public static void setApptTypes(ObservableList<MonthlyAppointmentTypes> apptTypes) {
        MonthlyAppointmentTypes.apptTypes = apptTypes;
    }       
}
