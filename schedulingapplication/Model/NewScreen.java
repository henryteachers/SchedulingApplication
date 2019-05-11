package schedulingapplication.Model;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class NewScreen {
    private static Stage stage;
    private static Parent root;        
    private static Scene scene;
    private static Rectangle2D primScreenBounds;
    
    
    public static void myScreen (Stage stage, URL name) throws IOException {
        NewScreen.stage = stage;
        NewScreen.root = FXMLLoader.load(name);
        NewScreen.scene = new Scene(root);
        NewScreen.stage.setScene(scene);
        NewScreen.primScreenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        NewScreen.stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        NewScreen.stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        NewScreen.stage.show();       
    }
}
