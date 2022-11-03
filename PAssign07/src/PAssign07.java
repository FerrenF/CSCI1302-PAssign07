/**
 * File: PAssign05.java 
 * Class: CSCI 1302
 * Author: Hunter W. 
 * Created on: September 18, 2022
 * Description: 
 * 
 * Oh my god I regret this. I didn't have time to make a polished, finished project;
 * we got MEMES, and that is what matters.
 * 
 * ESC can be used to quit out of this relatively simply app: I didn't have time to make the side buttons work.
 * If I did make them work, I would probably use mouse detection events, cursor positions, and css to create pseudo buttons.
 * 
 * The only real function of this thing is to type fake phone numbers on the fake screen, 
 * and then flash and play piggy noises if you enter at least a single number. piggy sad face otherwise
 */
//https://github.com/FerrenF/CSCI1302-PAssign07.git
//https://github.com/GSU-CS-1302/csci1302-JavaFX

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.io.File;
import javax.swing.Timer;
import javafx.application.*;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import keypad.KeyPadPane;

//We could create custom events here using EventHandler class, but we can also use interfaces.

//Custom events that need to communicate between objects.
interface peppaCalled {
	//Fired when the enter button is hit on the keypad.
	public void peppaCalledEvent(int blinks);
}

interface peppaType {
	//Fired when something is typed on the keypad.
	public void peppaTyped(String key);
}


//Begin of main class
public class PAssign07 extends Application {

	private static final Dimension2D windowSize = new Dimension2D(292, 600);
	private Point2D initDrag = new Point2D(0, 0);

	
	//Class handler for our keypad.
	private class PeppaPane extends KeyPadPane {
		public peppaType typeHandler;

		PeppaPane() {
			super(true);
			this.setMaxSize(120, 145);
			this.listButtons.forEach((node) -> {
				node.setId("button-" + node.getText());
				PeppaPane.setHgrow(node, Priority.SOMETIMES);
				PeppaPane.setVgrow(node, Priority.ALWAYS);
			});
			this.setHgap(2);
			this.setVgap(2);
			this.setId("peppa-pane");
			registerEventHandlers();
		}

		@Override
		public void registerEventHandlers() {
			this.listButtons.forEach((node) -> {
				node.onMouseClickedProperty().set((e) -> this.typeHandler.peppaTyped(node.getText()));
				node.onMouseEnteredProperty().set((event) -> node.getStyleClass().add(0, "btn-over"));
				node.onMouseExitedProperty().set((event) -> node.getStyleClass().remove("btn-over"));
				node.onMousePressedProperty().set((event) -> node.getStyleClass().add(0, "btn-down"));
				node.onMouseReleasedProperty().set((event) -> node.getStyleClass().remove("btn-down"));
			});
		}
	}

	//Class handler for the bottom pane of our root, and the light.
	private class PeppaBottomPane extends VBox implements peppaCalled {
		private VBox peppaCallLight;
		Timer blinkTimer;
		int blinksLeft = 10;

		PeppaBottomPane() {
			blinkTimer = new Timer(500, (e) -> {
				peppaCallLight.setId("peppa-light-" + ((blinksLeft % 2 == 0) ? "on" : "off"));
				blinksLeft--;
				if (blinksLeft < 0) {
					blinkTimer.stop();
				}
			});

			peppaCallLight = new VBox();
			peppaCallLight.setId("peppa-light-off");
			peppaCallLight.setMinSize(120, 53);

			Rectangle peppaTopDivider = new Rectangle();
			peppaTopDivider.setHeight(8);

			this.getChildren().addAll(peppaTopDivider, peppaCallLight);
		}

		public void blinkStart(int blinks) {
			this.blinksLeft = blinks;
			blinkTimer.start();
		}
		
		//This method might be useless.
		public void blinkStop() {
			blinksLeft = 0;
			blinkTimer.stop();
		}

		public void peppaCalledEvent(int blinks) {
			
			blinkStart(blinks);
		}
	} //End class handler for bottom pane.

	//Class handler for the center pane of our root - this handles a lot of the functionality of the program.
	private class PeppaCenterPane extends VBox implements peppaType {
		private peppaCalled lightNode;
		Timer timer;
		private String currentNumber;

		public String getCurrentNumber() {
			return currentNumber;
		}

		public void setCurrentNumber(String currentNumber) {
			this.currentNumber = currentNumber;
		}

		public int getScreenPosition() {
			return screenPosition;
		}

		public void setScreenPosition(int screenPosition) {
			setPeppaScreen(screenPosition);
			this.screenPosition = screenPosition;
		}

		private boolean inCall = false;

		public boolean isInCall() {
			return inCall;
		}

		public void setInCall(boolean inCall) {
			this.inCall = inCall;
		}

		private int screenPosition = 3;
		private VBox peppaScreen;
		private PeppaPane peppaPane;
		private Button enterButton;
		private Text screenText;

		PeppaCenterPane() {
			super();

			this.setPrefHeight(420);

			this.setAlignment(Pos.TOP_CENTER);
			peppaScreen = new VBox();
			peppaScreen.setAlignment(Pos.CENTER);
			screenText = new Text();
			screenText.setLineSpacing(-10);
			screenText.setId("peppa-screen-text");
			peppaScreen.setPadding(new Insets(5, 10, 5, 00));
			setScreenPosition(1);

			setPeppaNumber("");

			peppaScreen.getChildren().add(screenText);

			Rectangle peppaTopDivider = new Rectangle();
			peppaTopDivider.setHeight(7);

			Rectangle peppaScreenDivider = new Rectangle();
			peppaScreenDivider.setHeight(45);
			peppaPane = new PeppaPane();

			Rectangle peppaEnterDivider = new Rectangle();
			peppaEnterDivider.setHeight(8);

			enterButton = new Button();
			enterButton.setId("peppa-button-enter");
			enterButton.onMouseClickedProperty().set((event) -> this.panelPressEnter());
			enterButton.onMouseEnteredProperty().set((event) -> enterButton.setId("peppa-button-enter-over"));
			enterButton.onMouseExitedProperty().set((event) -> enterButton.setId("peppa-button-enter"));

			peppaPane.typeHandler = this;
			this.getChildren().addAll(peppaTopDivider, peppaScreen, peppaScreenDivider, peppaPane, peppaEnterDivider,
					enterButton);
		}

		private void panelPressEnter() {
			if (this.isInCall()) {
				return;
			}
			beginCall();
		}

		//ahh, the beautiful source of peppa-pig.mp3
		public void beginCall() {
			boolean willCall=true;
			if(this.getCurrentNumber().length()>0)
			{
				setPeppaScreen(1);
			}
			else
			{
				willCall=false;
				setPeppaScreen(2);
			}
			timer = new Timer(1000+(5000*(willCall?1:0)),(e)->{
				setPeppaScreen(3);
				clearPeppaNumber();
				timer.stop();
				setInCall(false);
				});
			timer.start();
			if(willCall) {
				setInCall(true);
				try {

				Media sound = new Media(new File("src/peppa-pig.mp3").toURI().toString());
				MediaPlayer mediaPlayer = new MediaPlayer(sound);
				mediaPlayer.play();
				}
				catch(Exception e) {
					System.out.println("oopsie happened with the audio files");
				}
				
				this.lightNode.peppaCalledEvent(10);
			}
			else
			{
				this.lightNode.peppaCalledEvent(2);
			}
		}

		public void clearPeppaNumber() {
			setPeppaNumber("");
		}

		
		//format our string so it displays a number like an old flip phone
		public void setPeppaNumber(String number) {
			this.setCurrentNumber(number);
			StringBuilder displayNumber = new StringBuilder();
			String holder = this.getCurrentNumber();
			int lines = 0;
			while (holder.length() > 4 && lines < 2) {
				displayNumber.append(holder.substring(0, 3) + "\n");
				holder = holder.substring(3);
				lines++;
			}
			if (holder.length() > 4) {
				displayNumber.append(holder.substring(0, 4));
				displayNumber.append("...");
			} else {
				displayNumber.append(holder.substring(0));
			}
			screenText.setText(displayNumber.toString());
		}

		public void setPeppaScreen(int position) {
			if (position == 3) {
				screenText.setOpacity(1);
			} else {
				screenText.setOpacity(0);
			}
			peppaScreen.setId("peppa-screen-" + String.valueOf(position));
			peppaScreen.setMinSize(113, 112);
		}

		@Override
		public void peppaTyped(String key) {
			if (this.isInCall()) {
				return;
			} else {
				if (this.getScreenPosition() != 3) {
					setPeppaScreen(3);
				}
			}
			setPeppaNumber(this.getCurrentNumber() + key);
		}

	}
	//End class handler for center pane.


	public void start(Stage primaryStage) {

		VBox topPane = new VBox();
		PeppaBottomPane bottomPane = new PeppaBottomPane();
		VBox leftPane = new VBox();
		VBox rightPane = new VBox();
		PeppaCenterPane centerPane = new PeppaCenterPane();
		BorderPane root = new BorderPane(centerPane, topPane, rightPane, bottomPane, leftPane);
		root.setId("root-pane");
		centerPane.lightNode = bottomPane;
		leftPane.setPrefWidth(75);
		rightPane.setPrefWidth(75);

		topPane.setPrefHeight(150);
		bottomPane.setPrefHeight(80);

		root.onMouseDraggedProperty().set((event) -> {
			primaryStage.setX(event.getScreenX() - initDrag.getX());
			primaryStage.setY(event.getScreenY() - initDrag.getY());
		});
		root.onMousePressedProperty().set((event) -> {
			initDrag = new Point2D(event.getSceneX(), event.getSceneY());
		});

		Scene base = new Scene(root, windowSize.getHeight(), windowSize.getWidth());

		primaryStage.initStyle(StageStyle.TRANSPARENT);
		base.setFill(Color.TRANSPARENT);
		base.getStylesheets().add(String.valueOf(this.getClass().getResource("styles.css")));
		primaryStage.setScene(base);
		primaryStage.show();

		
		//Handler for our escape key - we don't have an exit button otherwise.
		base.onKeyPressedProperty().set((event) -> {
			switch (event.getCode()) {
			case ESCAPE: {
				primaryStage.close();
				break;
			}
			default:

			}
		});

	}

	//Going to just leave this here. 
	public static void main(String[] args) {		
		System.out.println("Press ESC to exit.");		
		launch(args);
	}
}
