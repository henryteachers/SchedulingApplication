package schedulingapplication.Model;

import java.sql.ResultSet;
import java.sql.Statement;
import static schedulingapplication.Model.DBConnection.conn;


public class Query {
    
    private static String query;
    private static Statement stmt;
    private static ResultSet result;
    
    public static void makeQuery(String q) {
        query = q;
        
        try {
            //create Statement object
            stmt = conn.createStatement();
            
            //determine query execution
            if(query.toLowerCase().startsWith("select")) 
                result = stmt.executeQuery(query);
            if(query.toLowerCase().startsWith("delete") || 
                    query.toLowerCase().startsWith("update") || 
                    query.toLowerCase().startsWith("insert")) 
                stmt.executeUpdate(query);            
        }
        catch(Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    public static ResultSet getResult() {
        return result;
    }
}
