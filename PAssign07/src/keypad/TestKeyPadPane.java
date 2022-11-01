package keypad;
/**
 * File: csci1302/keypad/TestKeyPad.java
 * Package: keypad
 * @author Christopher Williams
 * Created on: Mar 02, 2020
 * Last Modified; Mar 31, 2021
 * Description:  Test the custom KeyPadPane class
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestKeyPadPane extends Application {

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		BorderPane mainPane = new BorderPane();
		
		// create a KeyPadPane
		// KeyPadPane keyPane = new KeyPadPane(); // use for default layout
		// KeyPadPane keyPane = new KeyPadPane(true); // use this for phone layout
		KeyPadPanePlus keyPane = new KeyPadPanePlus(); // using custom version
		
		// add KeyPane to BorderPane
		mainPane.setCenter(keyPane);
		
		// create your scene (400 x 400 to clearly show KeyPadPane)
		Scene scene = new Scene(mainPane, 400, 400);
		
		primaryStage.setTitle("Test KeyPadPane"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	public static void main(String[] args) { 
		launch(args);
	}
}


