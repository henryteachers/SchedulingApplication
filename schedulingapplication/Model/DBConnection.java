package schedulingapplication.Model;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    
    private static final String db = "U06bHC";
    private static final String url = "jdbc:mysql://52.206.157.109/" + db;
    private static final String user = "U06bHC";
    private static final String pass = "53688718862";
    private static final String driver = "com.mysql.jdbc.Driver";
    static Connection conn;
    
    public static void makeConnection() throws ClassNotFoundException, SQLException, Exception {
        Class.forName(driver);
        conn = DriverManager.getConnection(url,user,pass);   
    }
    
    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception {
        conn.close();
    }
    
    public static Connection returnConnection() {
        return conn;
    }
}