package controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
	    Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
	    Scene scene = new Scene(root);
	    
	    try {
	        String cssUrl = getClass().getResource("/view/style.css").toExternalForm();
	        scene.getStylesheets().add(cssUrl);
	    } catch (Exception e) {
	        System.out.println("Warning: style.css not found, running without stylesheet.");
	    }
	    
	    primaryStage.setFullScreen(true); 
	    primaryStage.setFullScreenExitHint("Press ESC to exit full screen");
	    
	    primaryStage.setTitle("MELOWAVE");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

    public static void main(String[] args) {
        launch(args);
    }
}