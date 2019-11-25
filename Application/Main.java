package application;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	/**
	 * Number of minutes to start timer from
	 */
	private static int startMinutes = 0;

	/**
	 * Current number of seconds left on timer. Note that the timer counts only in
	 * seconds, so this value doesn't consider minutes.
	 */
	private int currentSeconds = startMinutes * 60;

	/**
	 * Label to display minutes remaining
	 */
	private Label minutesLabel = new Label();

	/**
	 * Label to display seconds remaining (taking minutes into consideration)
	 */
	private Label secondsLabel = new Label();

	/**
	 * Timeline object to keep track of time for timer
	 */
	private Timeline timeline;

	/**
	 * Indicates if the timer is currently stopped
	 */
	private boolean stop;

	/**
	 * Layout used for app
	 */
	GridPane grid;

	/**
	 * Scene used for primaryStage
	 */
	Scene timerScene;

	/**
	 * Print the new timer screen based on minutes input by user.
	 * 
	 * @param primaryStage
	 */
	private void printTimer(Stage primaryStage) {
		// Construct new layout
		grid.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

		/*
		 * // Construct MediaPlayer to play alert sound when timer has finished String
		 * alertMusicFile = new File("appMedia/airHorn.mp3").getAbsolutePath(); Media
		 * sound = new Media(new File(alertMusicFile).toURI().toString()); MediaPlayer
		 * player = new MediaPlayer(sound);
		 * 
		 */

		// Initialize currentSeconds to appropriate value
		currentSeconds = startMinutes * 60;

		// Set up minute and second labels
		minutesLabel.setText(Integer.toString(startMinutes));
		minutesLabel.setStyle("-fx-font-size: 5em;");
		secondsLabel.setText(String.format("%02d", startMinutes * 60 % 60));
		secondsLabel.setStyle("-fx-font-size: 5em;");

		// Set up minute and second captions
		Text minuteHeader = new Text("Minutes");
		minuteHeader.setFont(Font.font("Ariel", FontWeight.BOLD, 20));
		Text secondHeader = new Text("Seconds");
		secondHeader.setFont(Font.font("Ariel", FontWeight.BOLD, 20));

		// Separate Text for : in between minutes and seconds
		Text colon = new Text(":");
		colon.setFont(Font.font("Ariel", FontWeight.NORMAL, 30));

		// Construct startButton and associated actions
		Button startButton = new Button();
		startButton.setText("Start");
		startButton.setOnAction(new EventHandler<ActionEvent>() { // Button event handler

			@Override
			public void handle(ActionEvent event) {
				if (timeline != null) {
					timeline.stop();
				}

				// update timer labels
				secondsLabel.setText(String.format("%02d", currentSeconds % 60));
				minutesLabel.setText(Integer.toString(currentSeconds / 60));

				// Set the background to a green color to indicate that the timer is running
				grid.setBackground(
						new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));

				// The timer is not stopped
				stop = false;

				// Set up timeline for iterating through appropriate number of seconds
				timeline = new Timeline();
				timeline.setCycleCount(Timeline.INDEFINITE);
				timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
					// KeyFrame event handler
					public void handle(ActionEvent event) {
						currentSeconds--;
						// update timer labels
						minutesLabel.setText(Integer.toString(currentSeconds / 60));
						secondsLabel.setText(String.format("%02d", currentSeconds % 60));

						// If the timer has reached 0, play the alert sound, reset the sound, and stop
						// the timer
						if (currentSeconds <= 0) {
							if (new File("appMedia/airHorn.mp3").exists()) {
								// Construct MediaPlayer to play alert sound when timer has finished
								String alertMusicFile = new File("appMedia/airHorn.mp3").getAbsolutePath();
								Media sound = new Media(new File(alertMusicFile).toURI().toString());
								MediaPlayer player = new MediaPlayer(sound);
								// Play sound
								player.play();
								player.seek(Duration.ZERO);
							}
							grid.setBackground(new Background(
									new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY)));
							timeline.stop();
						}

						// If the timer is not supposed to be running, stop it
						if (stop) {
							timeline.stop();
						}
					}
				}));

				// Run the timer
				timeline.playFromStart();
			}
		});

		// Construct stopButton and associated actions
		Button stopButton = new Button();
		stopButton.setText("Stop");
		stopButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// Turn off the timer and set the background color to red to indicate that the
				// timer is stopped
				stop = true;
				grid.setBackground(
						new Background(new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY)));

			} // Button event handler
		});

		// Construct resetButton and associated actions
		Button resetButton = new Button();
		resetButton.setText("Reset");
		resetButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// Turn off the timer, reset the timer, and set the background color to grey to
				// indicate that the timer has been reset.
				stop = true;
				grid.setBackground(
						new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
				currentSeconds = startMinutes * 60;
				minutesLabel.setText(Integer.toString(startMinutes));
				secondsLabel.setText(String.format("%02d", currentSeconds % 60));

			} // Button event handler
		});

		// Box for 3 buttons above
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.BOTTOM_CENTER);

		// Add each button
		btnBox.getChildren().add(startButton);
		btnBox.getChildren().add(stopButton);
		btnBox.getChildren().add(resetButton);

		// Add the minute and second captions to the layout
		grid.add(minuteHeader, 0, 0);
		grid.add(secondHeader, 6, 0);

		// Add the minute, colon, and second labels/text to the layout
		grid.add(minutesLabel, 0, 1);
		grid.add(colon, 1, 1);
		grid.add(secondsLabel, 6, 1);

		// Add the button box to the layout
		grid.add(btnBox, 0, 2, 6, 1);

		// Set up the UI with the updated layout
		primaryStage.setScene(timerScene);
		primaryStage.show();

	}

	/**
	 * The user enters the number of the minutes they would like the timer to start
	 * from.
	 * 
	 * @param primaryStage
	 */
	private void chooseMinutes(Stage primaryStage) {
		// Set up the layout
		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		// Welcome text
		Text welcomeTitle = new Text("Welcome to the Timer App!");
		welcomeTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(welcomeTitle, 0, 0, 2, 1);

		// Prompt to enter # of minutes
		Label noMinutes = new Label("Enter number of minutes:");
		grid.add(noMinutes, 0, 1);

		// Field where user enters minutes
		TextField userMinInput = new TextField();
		grid.add(userMinInput, 1, 1);
		// Next button
		Button submitMin = new Button();
		submitMin.setText("Next");
		submitMin.setOnAction(new EventHandler<ActionEvent>() { // on click

			@Override
			public void handle(ActionEvent arg0) {
				// Set the startMintues variable above to the user input only if the input is an
				// integer.
				try {
					startMinutes = Integer.parseInt(userMinInput.getText());
					grid.getChildren().clear();
					printTimer(primaryStage);
				} catch (Exception e) {
					// Don't do anything if an integer is not input
				}
			}
		});

		// Container for "next" button
		HBox hbNextBtn = new HBox(10);
		hbNextBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbNextBtn.getChildren().add(submitMin);
		grid.add(hbNextBtn, 1, 2);

		// Set background color of layout to orange
		grid.setStyle("-fx-background-color: orange");

		// Construct the UI with the updated layout
		timerScene = new Scene(grid, 320, 200);
		primaryStage.setScene(timerScene);
		primaryStage.show();

	}

	@Override
	public void start(Stage primaryStage) {
		// Initialize the window and go to welcome screen
		primaryStage.setTitle("Timer");
		chooseMinutes(primaryStage);

	}

	/**
	 * Start application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
