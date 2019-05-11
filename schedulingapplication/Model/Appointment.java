package schedulingapplication.Model;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Appointment {
    
    private String appointmentId, customerName, consultantName, title, description,
            location, contact, type, url, start, end;
    
    private static Boolean overlap = false;
    private static String overlapName = "";
    
    private static ObservableList<Appointment> oblist = FXCollections.observableArrayList();    
    
    private static Appointment appointmentToModify;

    public Appointment(String appointmentId, String customerName, String consultantName, 
            String title, String description, String location, String contact, 
            String type, String url, String start, String end) {
        this.appointmentId = appointmentId;
        this.customerName = customerName;
        this.consultantName = consultantName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public static ObservableList<Appointment> getAppointmentList() {
        return oblist;
    }

    public void setAppointmentList(ObservableList<Appointment> oblist) {
        Appointment.oblist = oblist;
    }

    public static Appointment getAppointmentToModify() {
        return appointmentToModify;
    }

    public static void setAppointmentToModify(Appointment appointmentToModify) {
        Appointment.appointmentToModify = appointmentToModify;
    }
    
    public static void setOverlap(Boolean overlap) {
        Appointment.overlap = overlap;
    }
    
    public static boolean getOverlap() {
        return Appointment.overlap;
    }
    
    public static void setOverlapName(String name) {
        Appointment.overlapName = name;
    }
    
    public static String getOverlapName() {
        return Appointment.overlapName;
    }
    
    public static int getMonthlyTotal(String type, LocalDateTime monthBegin, LocalDateTime nextMonthBegin) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
        int count = 0;
        for (Appointment appt: Appointment.getAppointmentList()) {
            if (appt.getType().equals(type) && 
                    LocalDateTime.parse(appt.getStart(), dtf).isAfter(monthBegin) &&
                    LocalDateTime.parse(appt.getStart(), dtf).isBefore(nextMonthBegin))
                count++;
        }
        return count;
    }
    
    public static int getCustomerApptTotal(String name) {
        int count = 0;
        for (Appointment appt: Appointment.getAppointmentList()) {
            if (appt.getCustomerName().equals(name))
                count++;
        }
        return count;
    }
}
