package schedulingapplication.Model;

public class User {
    private static String user;
    
    public static void setUser(String user) {
        User.user = user;
    }
    
    public static String getUser() {
        return User.user;
    }
}
