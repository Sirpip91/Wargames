/*
 * Bradley Allen 
 * COSC 1337
 * November 21 2021
 * Group Project WarGames
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
 
 *
This class is the unbeatable scene / game mode
 */

//package
package application;

import java.util.List;
import java.util.ArrayList;
//imports
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

//Player screen EXTENDS VBox so it has methods from vbox
public class UnbeatableScreen extends VBox{

	
	//variables to store UI information win lose ect...
	private int wins = 0;
	private int losses = 0;
	private int ties = 0;
	private int turn;
	private boolean gameOver =false;
	private boolean aiTrashTalk = false;
	
	//User interface
	private Button[][] buttons;
	private Label headerLabel;
	private Label winLabel;
	private Label lossesLabel;
	private Label tieLabel;
	private Label trashTalkLabel;
	private Button playAgain;
	
	
	
	//constructor for the player screen aka the screen the game is played on
	public UnbeatableScreen() {
		
		//creates a array of buttons which will be the tic tac toe board
		buttons = new Button[3][3];
		
		//gridPane holds the array of buttons or the board 
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		//creates the board of buttons and adds it to gridpane
		for(int i = 0; i< 3; i++) {
			for(int j = 0; j<3; j++) {
				
			
				buttons[i][j] = new Button();
				buttons[i][j].setText("");
				buttons[i][j].setFont(new Font(36));
				buttons[i][j].setPrefWidth(100);
				buttons[i][j].setPrefHeight(100);
				buttons[0][0].setText("O");
				gridPane.add(buttons[i][j], i, j);
				//main button activity
				buttons[i][j].setOnAction(event ->{
					Button selectedBtn = (Button)event.getSource();
					System.out.println("AI Clicked");
					if(!gameOver && !aiTrashTalk) {
					//if buttin is already selected do nothing
					if(selectedBtn.getText().length()>0) {
						// no need for time waiting
						Aiturn();
						trashTalkLabel.setText(trashTalk());
						return;
					}
					
					//if button is not selected ( free up ) 
					String place;
					if(turn % 2 == 0) {
						//This means X turn
						place = "X";
					}else {
						//this is O turn
						place = "O";
					}
					
					turn++;
				
				
					
					selectedBtn.setText(place);
					//you can only win tic tac toe when the turn is > 5
					if(turn>1 ) {
						
						
						//check for win
						if(checkifWin(place)) {
							//stop the game
							
							
							headerLabel.setText(String.format("%s Won!!!", place));
							TranslateTransition translate = new TranslateTransition();
							translate.setNode(headerLabel);
							translate.setDuration(Duration.millis(1000));
							translate.setCycleCount(4);
							translate.setByX(250);
							
							translate.setAutoReverse(true);
							translate.jumpTo(Duration.ZERO);
							translate.play();
							
							gameOver=true;
							getChildren().addAll(playAgain);
							return;
						}
					}
					//you can only win tic tac toe when the turn is > 5
					if(turn==7 ) {
						
						
						//check for win
						if(checkifWin(place)) {
							//stop the game
							TranslateTransition translate = new TranslateTransition();
							
							headerLabel.setText(String.format("%s Won!!!", place));
							translate.setNode(headerLabel);
							translate.setDuration(Duration.millis(1000));
							translate.setCycleCount(4);
							translate.setByX(250);
							
							translate.setAutoReverse(true);
							translate.jumpTo(Duration.ZERO);
							translate.play();
							
							
							getChildren().addAll(playAgain);
							gameOver=true;
							
							return;
						}
					}
					//draw if the turn is equal to 9 or all spaces filled
					if(turn == 8 ) {
						gameOver = true;
						getChildren().addAll(playAgain);
						setTies();
						tieLabel.setText("Ties: " + getTies());
						headerLabel.setText("DRaw GaME OveR...");
						TranslateTransition translate = new TranslateTransition();
						
	
						translate.setNode(headerLabel);
						translate.setDuration(Duration.millis(1000));
						translate.setCycleCount(4);
						translate.setByX(250);
						
						translate.setAutoReverse(true);
						translate.jumpTo(Duration.ZERO);
						translate.play();

						return;
					}
					
					// this is for the AI
					if(turn % 2 != 0) {
						
						aiTrashTalk = true;
						//implemetn trash talk
						{
							
							Runnable runnable = new Runnable() {
								@Override
								public void run() {			//this is a mess but adds deley for AI choosing
									Platform.runLater(()->{
										trashTalkLabel.setText(trashTalk());
										Aiturn();
									});
								}
							};
							ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
							service.schedule(runnable, 1000, TimeUnit.MILLISECONDS);
						}
					}
					
					//shows who turn it is above the board
					headerLabel.setText(String.format("%s's turn", turn % 2 == 0 ? "X" : "O"));
					
					
					}//end of game over
					
				
				});
					
			}
		}
		
		//creates the label
		headerLabel = new Label("X's Turn");
		headerLabel.setFont(new Font(50));
		
		//creates play button
		playAgain = new Button("Play Again?");
		playAgain.setFont(new Font(50));
		//when play again is pressed remove it and restart game
		playAgain.setOnAction(event ->{
			resetGame();
			getChildren().remove(playAgain);
		});
		
		
		//set the labels up
		winLabel = new Label ("Wins: " + getWins());
		winLabel.setFont(new Font(30));
		//losselabel counter
		lossesLabel = new Label ("Losses: " + getLosses());
		lossesLabel.setFont(new Font(30));
		//tie counter
		tieLabel = new Label ("Ties: " + getTies());
		tieLabel.setFont(new Font(30));
		//Trashtalk view
		trashTalkLabel = new Label ("AI says: I AM UNBEATABLE"  );
		trashTalkLabel.setFont(new Font(30));
		
		//vbox that contians trashtalk
		VBox bmTalk = new VBox(trashTalkLabel);
		
		//vbox for the labels
		VBox trackerBox = new VBox(winLabel, lossesLabel, tieLabel);
		trackerBox.setAlignment(Pos.CENTER_LEFT);
		trackerBox.setSpacing(10);
		
		//hbox for the container
		HBox container = new HBox(trackerBox);
		container.setSpacing(20);
		container.setAlignment(Pos.CENTER);
		container.setPrefWidth(50);
		
		
		//sets the whole PlayerScreen extends vbox class to cetner and adds the board to the vbox
		setAlignment(Pos.CENTER);
		getChildren().addAll(bmTalk,headerLabel,gridPane,container);
		
		
		
		
		
	}//end of constructor
	
	
	
	//-------------if you can get the button action event to be linked to this method insead of implemented above it would be cleaner
	/*
	public void buttonAction(ActionEvent event) {
		
		Button selectedBtn = (Button)event.getSource();
		
		//if buttin is selected do nothing
		if(selectedBtn.getText().length()>0) {
			
		}
		
		String place;
		if(turn % 2 == 0) {
			//This means X turn
			place = "X";
		}else {
			//this is O turn
			place = "O";
		}
		
		selectedBtn.setText(place);
		turn++;
		
	}
	
	*/
	
	// Make the AI unbeatble.  		//CANNOT FIGURE OUT MINIMAX ALGORITHM
	private void Aiturn() {

		aiTrashTalk = false;
		switch(turn) {
		case 1:
			if(buttons[0][2].getText() != "X" && buttons[0][1].getText() != "X") {
				buttons[0][2].fire();			// bottom left
			}else
			if(buttons[2][2].getText() != "X")
				buttons[2][2].fire();			// bottom rogjt
			else
			if(buttons[2][0].getText() != "X")
				buttons[2][0].fire();			//bottom right
			break;
		case 3:
			if(buttons[1][1].getText() == "" ){
				buttons[1][1].fire();
				}
			else if(buttons[1][1].getText() == "X" ){
				buttons[2][1].fire();
				}
			else if(buttons[2][0].getText() == "" ){			//hardcoded mess
				buttons[2][0].fire();
				}
			else if(buttons[1][2].getText() == "" ){
				buttons[1][2].fire();
				}
			
			break;
		case 5:
			if(buttons[1][1].getText() == "O" && buttons[2][1].getText() == "")
				buttons[2][1].fire();
			else if(buttons[1][2].getText() == "" ){
				buttons[1][2].fire();
				}
			else if(buttons[1][2].getText() == "X" ){
				buttons[1][0].fire();
				}
			
			break;
		case 7:
			if (buttons[2][2].getText() == "")
			{
				buttons[2][2].fire();
			}else
				if (buttons[1][2].getText() == "")
				{
					buttons[1][2].fire();
				}
				else if(buttons[1][2].getText() == "" ){		//HARDCODED MESS
					buttons[1][2].fire();					
					}
				else if(buttons[2][1].getText() == "" ){
					buttons[2][1].fire();
					}
				else if(buttons[2][0].getText() == "" ){
					buttons[2][0].fire();
					}
				else if(buttons[1][0].getText() == "" ){
					buttons[1][0].fire();
					}
		
		
		}//end of switch
		
		
		

	}
	
	
	
	//checks if the the game is win or not
	private boolean checkifWin(String player) {
	
		
		//Verticle
		if		(player.equals(buttons[0][0].getText()) &&
				player.equals(buttons[0][1].getText()) &&		//veritcle left win
				player.equals(buttons[0][2].getText())){
				//Player wins
				if(player.equals("X")) {
				setWins();
				winLabel.setText("Wins: " + getWins());
				}else
				//Player Losses
				if(player.equals("O")) {
				setLosses();
				lossesLabel.setText("Losses: " + getLosses());
				}
				
		return true;
	}else
		if		(player.equals(buttons[1][0].getText()) &&
				player.equals(buttons[1][1].getText()) &&		//verticle middle win
				player.equals(buttons[1][2].getText())){
			//Player wins
			if(player.equals("X")) {
			setWins();
			winLabel.setText("Wins: " + getWins());
			}else
			//Player Losses
			if(player.equals("O")) {
			setLosses();
			lossesLabel.setText("Losses: " + getLosses());
			}
		return true;
	}else
		if		(player.equals(buttons[2][0].getText()) &&
				player.equals(buttons[2][1].getText()) &&		//verticle right win
				player.equals(buttons[2][2].getText())){
			//Player wins
			if(player.equals("X")) {
			setWins();
			winLabel.setText("Wins: " + getWins());
			}else
			//Player Losses
			if(player.equals("O")) {
			setLosses();
			lossesLabel.setText("Losses: " + getLosses());
			}
		return true;
	}else
		
		
		//Horizontle win
		//Verticle
				if		(player.equals(buttons[0][0].getText()) &&
						player.equals(buttons[1][0].getText()) &&		//Horizontal left win
						player.equals(buttons[2][0].getText())){
					//Player wins
					if(player.equals("X")) {
					setWins();
					winLabel.setText("Wins: " + getWins());
					}else
					//Player Losses
					if(player.equals("O")) {
					setLosses();
					lossesLabel.setText("Losses: " + getLosses());
					}
				return true;
			}else
				if		(player.equals(buttons[0][1].getText()) &&
						player.equals(buttons[1][1].getText()) &&		//Horizontal middle win
						player.equals(buttons[2][1].getText())){
					//Player wins
					if(player.equals("X")) {
					setWins();
					winLabel.setText("Wins: " + getWins());
					}else
					//Player Losses
					if(player.equals("O")) {
					setLosses();
					lossesLabel.setText("Losses: " + getLosses());
					}
				return true;
			}else
				if		(player.equals(buttons[0][2].getText()) &&
						player.equals(buttons[1][2].getText()) &&		//Horizontal right win
						player.equals(buttons[2][2].getText())){
					//Player wins
					if(player.equals("X")) {
					setWins();
					winLabel.setText("Wins: " + getWins());
					}else
					//Player Losses
					if(player.equals("O")) {
					setLosses();
					lossesLabel.setText("Losses: " + getLosses());
					}
				return true;
			}else
		
		//Diaginal win
		
		if			(player.equals(buttons[0][0].getText()) &&
					player.equals(buttons[1][1].getText()) &&		//top left to bottom right
					player.equals(buttons[2][2].getText())){
			//Player wins
			if(player.equals("X")) {
			setWins();
			winLabel.setText("Wins: " + getWins());
			}else
			//Player Losses
			if(player.equals("O")) {
			setLosses();
			lossesLabel.setText("Losses: " + getLosses());
			}
			return true;
		}else
		if			(player.equals(buttons[0][2].getText()) &&
					player.equals(buttons[1][1].getText()) &&		//top right to bottom left
					player.equals(buttons[2][0].getText())){
			//Player wins
			if(player.equals("X")) {
			setWins();
			winLabel.setText("Wins: " + getWins());
			}else
			//Player Losses
			if(player.equals("O")) {
			setLosses();
			lossesLabel.setText("Losses: " + getLosses());
			}
			
			return true;
		}
				// if there is not wins return false aka keep game going
		return false;
	}
	
	
	
	//reset game method
	private void resetGame() {
		//clear game buttons
		for(int i = 0; i< 3; i++) {
			for(int j = 0; j<3; j++) {
				buttons[i][j].setText("");	
			}
		}
		//for unbeatable set top left to O
		buttons[0][0].setText("O");
		//reset turn counter
		turn = 0;
		//updated headerlabel
		headerLabel.setText("X turn!");
		//gamOver = false
		gameOver=false;
		
	}
	
	//returns trashtalk for the headerLabel
	public String trashTalk() {
		//need 9 differet saying for 9 moves max
		//create random numbers if number is >0 && <5 say ... if 
		String quote = "Lets Play!";
		Random rn = new Random();
		int number;
		
		// for the 9 turns play a different random quote /trashtalk
		for(int counter = 1; counter<=9; counter++) {
			number = rn.nextInt(10);
			switch(number) {
			case 1:
				quote = "AI: Ha Im smarter than you.";
				break;
			case 2:
				quote = "AI: Are you Stupid?";
				break;
			case 3:
				quote = "AI: Do you even know how to play?";
				break;
			case 4:
				quote = "AI: That was not really smart.";
				break;
			case 5:
				quote = "AI: 'burp'... this is easy";
				break;
			case 6:
				quote = "AI: You stink at tic tac toe";
				break;
			case 7:
				quote = "AI: My cat is better than you!";
				break;
			case 8:
				quote = "AI: Loser....Loser...Loser...";
				break;
			case 9:
				quote = "AI: My grandma is better than you!";
				break;	
			}
			
		}
		
		//return the trashtalk
		return quote;
		
	}
	
	
	// returns the wins
	public int getWins() {
		return wins;
	}
	//returns the losses
	public int getLosses() {
		return losses;
	}
	//returns the ties
	public int getTies() {
		return ties;
	}
	public void setTies() {
		this.ties +=1;
	}
	// adds 1 to the win
	public void setWins() {
		this.wins +=1;
	}
	//adds 1 to the loss
	public void setLosses() {
		this.losses += 1;
	}
	
	
}
