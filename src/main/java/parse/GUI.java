package parse;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GUI extends Application {
    @Override
    public void start(@NonNull Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Parse Pro");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
