
//https://github.com/GSU-CS-1302/csci1302-JavaFX

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.application.*;
import javafx.geometry.Insets;
import keypad.KeyPadPane;
import keypad.TestKeyPadPane;


public class PAssign07 extends Application {
	public void start(Stage primaryStage) {
		
		
		VBox topPane = new VBox();
		VBox bottomPane = new VBox();
		VBox leftPane = new VBox();
		VBox rightPane = new VBox();
		VBox centerPane = new VBox();
		BorderPane root = new BorderPane(centerPane,topPane,rightPane,bottomPane,leftPane);
		root.setId("root-pane");
	
		leftPane.setPrefWidth(75);
		rightPane.setPrefWidth(75);
		topPane.setPrefHeight(150);
		centerPane.setPrefHeight(420);
		bottomPane.setPrefHeight(80);
	
		centerPane.setPadding(new Insets(5,0,10,0));
		Rectangle peppaScreen = new Rectangle();
		peppaScreen.setWidth(115);
		peppaScreen.setHeight(115);
		Rectangle peppaScreenDivider = new Rectangle();
		peppaScreenDivider.setHeight(50);
		KeyPadPane testPane = new KeyPadPane();
		
	
		testPane.setId("keypad-pane");
		testPane.setPrefSize(134, 155);
		
		Scene base = new Scene(root,292,600);
		
		
		centerPane.getChildren().addAll(peppaScreen,peppaScreenDivider,testPane);
		
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		base.setFill(Color.TRANSPARENT);		
		base.getStylesheets().add(String.valueOf(this.getClass().getResource("styles.css")));
		primaryStage.setScene(base);
		primaryStage.show();
		
		base.onKeyPressedProperty().set((event)->{
			switch(event.getCode()) {
				case ESCAPE:
				{
					primaryStage.close();
					break;
				}
				default:
					
			}
		});
		
		
// your code here
	}

	public static void main(String[] args) {
		launch(args);
	}
}
