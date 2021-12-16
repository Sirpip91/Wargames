/*
 * Bradley Allen 
 * 
 * This project is inspired by the 1983 classic WarGames. 
 * In order to stop WWIII, you need to convince government computers that sometimes there is no winner.
 * This is the Beginner Mode Class.
 * 
 * Features 4:
1.	Animations for win, lose, and draw
3.	A beginner difficulty AI that plays any random square not yet taken
6.	Make a game tracker that counts player wins and loses
7.	Add dialogue to the AI that has him “trash talk” after each play


this is the main / controller class
 */


//package
package application;
//imports
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;



// main class
public class Main extends Application {
	
	//setting up window elements and scenes 
	private Stage window;
	private Scene beginnerScreen;
	private Scene menuScreen;
	private Scene unbeatableScreen;
	
	
	// Game screen elements
	private BeginnerScreen beginner = new BeginnerScreen();
	private UnbeatableScreen unbeatable = new UnbeatableScreen();
	private Label headerLabel;
	
	@Override
    //start method that is needed with JavaFX
	   public void start(Stage stage) throws Exception {
			try {
				
				// Set window to the main stage
				window = stage;
				
				// Create the titleScreen scene
				BorderPane title = new BorderPane();
				title.setCenter(getTitleScreenButtons());
				menuScreen = new Scene(title,600,400);
				menuScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				
				
				// Create the gameScreen scene
				beginnerScreen = new Scene(beginner, 800, 600);
				beginnerScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				
				//Create The unbeatable
				unbeatableScreen = new Scene(unbeatable, 800, 600);
				unbeatableScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				
				
				
				// Set the JavaFX window title, icon, and scene. Then show the window
				window.setTitle("WarGames");
				window.setResizable(false);
				//window.setIcon(new Image());
				window.setScene(menuScreen);
				window.show();
				window.setOnCloseRequest(e -> Platform.exit());

			} catch (Exception e)	{
				e.printStackTrace();
			}
			
	
		 		
		}//end of start 
	
	// Create the buttons for the title screen
		private VBox getTitleScreenButtons() {
			// Create the HBox container for the buttons
			VBox box = new VBox(15);
			box.setAlignment(Pos.CENTER);
			
			//create large title
			
			headerLabel = new Label("WARGAMES CAN YOU WIN?");
			headerLabel.setFont(new Font(40));
			
			
			
			
			// Create the play game button
			Button beginnerBtn = new Button("Beginner");
			beginnerBtn.setId("menuBtn");
			beginnerBtn.setOnAction(e -> {	
				
				
				// Change the scene to the game
				window.setScene(beginnerScreen);
				window.show();
			});
			
			// Create the Unbeatable menu button
			Button unbeatableBtn = new Button("Unbeatable");
			unbeatableBtn.setId("menuBtn");
			unbeatableBtn.setOnAction(e -> window.setScene(unbeatableScreen));
			
			
			// Add the buttons to the HBox and return it
			box.getChildren().addAll(headerLabel,beginnerBtn, unbeatableBtn);
			return box;
		}
	
	
	
	
	//main that starts the program   
	public static void main(String[] args) {
		launch(args);
	}
}
