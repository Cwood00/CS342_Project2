//Name:Chris Wood, NetID:cwood35
//Name:Ana Theys,  NatID:athey3

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class BaccaratGame extends Application {
	ArrayList<Card> playerHand;
	ArrayList<Card> bankerHand;
	BaccaratDealer theDealer;
	BaccaratGameLogic gameLogic;
	double currentBet;
	double totalWinnings;
	String betPlacedOn;

	public static void main(String[] args) {
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		theDealer = new BaccaratDealer();
		gameLogic = new BaccaratGameLogic();

		primaryStage.setTitle("Baccarat");

		//-----Start scene-----//
		//TODO create start scene

		//-----Betting scene-----//
		//TODO create betting scene

		//-----Play scene-----//
		BorderPane playSceneRoot = new BorderPane();

		TextField playerCardHeader = new TextField("Player");
		TextField bankerCardHeader = new TextField("Banker");
		TextField playerCardFooter = new TextField("Score: ");
		TextField bankerCardFooter = new TextField("Score: ");
		playerCardHeader.setEditable(false);
		bankerCardHeader.setEditable(false);
		playerCardFooter.setEditable(false);
		bankerCardFooter.setEditable(false);

		HBox playerHandBox = new HBox();
		HBox bankerHandBox = new HBox();

		VBox playerCardBox = new VBox(playerCardHeader, playerHandBox, playerCardFooter);
		VBox bankerCardBox = new VBox(bankerCardHeader, bankerHandBox, bankerCardFooter);

		TextField playerWinningsTextField = new TextField("Player total winnings: $" + totalWinnings);
		TextField currentBetTextField = new TextField("Current Bet: $" + currentBet);
		Button dealAndPlayAgainButton = new Button("Deal");
		TextField resultsTextField = new TextField();
		playerWinningsTextField.setEditable(false);
		currentBetTextField.setEditable(false);
		resultsTextField.setEditable(false);

		VBox centralElements = new VBox(playerWinningsTextField, currentBetTextField, dealAndPlayAgainButton, resultsTextField);

		playSceneRoot.setLeft(playerCardBox);
		playSceneRoot.setRight(bankerCardBox);
		playSceneRoot.setCenter(centralElements);

		Scene playScene = new Scene(playSceneRoot, 1080,720);

		primaryStage.setScene(playScene);//TODO change to start scene when implemented
		primaryStage.show();
	}

	//Evaluates the amount won or lost
	//Returns the winnings. A loss is represented as winning a negative amount
	public double evaluateWinnings(){
		double winnings;
		String winner = gameLogic.whoWon(playerHand, bankerHand);

		if(betPlacedOn.equals(winner)){
			if(betPlacedOn.equals("Player"))
				winnings = currentBet;
			else if(betPlacedOn.equals("Banker"))
				winnings = currentBet * 0.95;
			else //Betting on tie
				winnings = currentBet * 8;
		}
		else
			winnings = -currentBet;

		return winnings;
	}
}
