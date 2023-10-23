// Name:Chris Wood, NetID:cwood35
// Name:Ana Theys,  NatID:athey3

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;


public class BaccaratGame extends Application {
	ArrayList<Card> playerHand;
	ArrayList<Card> bankerHand;
	BaccaratDealer theDealer;
	BaccaratGameLogic gameLogic;
	double currentBet;
	double totalWinnings;
	String betPlacedOn;

	// Start scene objects
	Text titleText;
	Button startGame;
	VBox layout;

	// Menu bar objects
	MenuBar mainMenuBar;
	Menu menuMenu;
	MenuItem itemExit;
	MenuItem itemFreshStart;

	// Betting scene objects
	BorderPane bettingBorderPane;
	VBox leftBox;
	VBox centerBox;
	HBox inputsBox;
	HBox buttonsBox;
	TextField betAmount;
	Button resetBets;
	Text selectBetText;
	Button selectPlayer;
	Button selectTie;
	Button selectBanker;
	Text currentBetText;
	TextField currentBetDisplay;
	Text totalWinningsText;
	TextField totalWinningsDisplay;
	TextField emptyText;
	Button playButton;

	// Play scene objects
	Text playerCardHeader;
	Text bankerCardHeader;
	Text playerCardFooter;
	Text bankerCardFooter;
	HBox playerHandBox;
	HBox bankerHandBox;
	VBox playerCardBox;
	VBox bankerCardBox;
	TextField playerWinningsTextField;
	TextField currentBetTextField;
	Button dealAndPlayAgainButton;
	TextField resultsTextField;
	VBox centralElements;
	// ----------------------------------------------------------------------

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		theDealer = new BaccaratDealer();
		gameLogic = new BaccaratGameLogic();

		primaryStage.setTitle("Baccarat");

		createMenuBar();

		Scene startScene = createStartScene();

		Scene bettingScene = createBettingScene();

		Scene playScene = createPlayScene();

		// Play scene EventHandlers
		EventHandler<ActionEvent> firstDrawEvent;
		EventHandler<ActionEvent> replayEvent = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(bettingScene);
				playerHand = null;
				bankerHand = null;
				Button thisButton = (Button)event.getSource();
				//TODO figure out how to reset event handler to draw event
				//thisButton.setOnAction(firstDrawEvent);
				thisButton.setText("Draw");
			}
		};
		EventHandler<ActionEvent> bankerDrawEvent = new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				bankerHand.add(theDealer.drawOne());
				Button thisButton = (Button)event.getSource();
				//TODO handle winnings
				thisButton.setOnAction(replayEvent);
				thisButton.setText("Replay");
			}
		};
		EventHandler<ActionEvent> playerDrawEvent = new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				Card playerDraw = theDealer.drawOne();
				playerHand.add(playerDraw);
				Button thisButton = (Button)event.getSource();
				if(gameLogic.evaluateBankerDraw(bankerHand, playerDraw)) {
					thisButton.setOnAction(bankerDrawEvent);
					thisButton.setText("Draw for Banker");
				}
				else{
					//TODO handle winnings
					thisButton.setOnAction(replayEvent);
					thisButton.setText("Replay");
				}
			}
		};
		firstDrawEvent = new EventHandler<>(){
			@Override
			public void handle(ActionEvent event) {
				theDealer.shuffleDeck();
				playerHand = theDealer.dealHand();
				bankerHand = theDealer.dealHand();
				Button thisButton = (Button)event.getSource();
				if(gameLogic.evaluatePlayerDraw(playerHand)){
					thisButton.setOnAction(playerDrawEvent);
					thisButton.setText("Draw for Player");
					//TODO check for natural win
				}
				else if(gameLogic.evaluateBankerDraw(bankerHand, null)){
					thisButton.setOnAction(bankerDrawEvent);
					thisButton.setText("Draw for Banker");
				}
				else{
					//TODO handle winnings
					thisButton.setOnAction(replayEvent);
					thisButton.setText("Replay");
				}
			}
		};

		dealAndPlayAgainButton.setOnAction(firstDrawEvent);

		// Start scene event handler
		startGame.setOnAction(e->primaryStage.setScene(bettingScene));

		// Menu bar event handlers
		itemExit.setOnAction(e->System.exit(0));
		itemFreshStart.setOnAction(e->{
			primaryStage.setScene(startScene);
			// TODO - fresh start handler
		});

		// Betting scene event handlers
		// TODO - write

		// Play scene event handlers
		// TODO - move here

		// Lights, camera, ACTION

		primaryStage.setScene(startScene);
		primaryStage.show();

	} // end start()


	//-----Start scene-----//
	public Scene createStartScene() {
		titleText = new Text("Baccarat");
		titleText.setStyle("-fx-font-size: 100");
		// titleText.setStyle("-fx-");

		startGame = new Button("Start");
		startGame.setStyle("-fx-font-size: 50");

		layout = new VBox(50, titleText, startGame);
		layout.setAlignment(Pos.CENTER);

		return new Scene(layout, 1080, 720);
	} // end createStartScene()


	//-----Menu Bar-----//
	public void createMenuBar() {
		mainMenuBar = new MenuBar();

		menuMenu = new Menu("Menu");
		menuMenu.setStyle("-fx-font-size: 18");
		itemExit = new MenuItem("Exit");
		itemExit.setStyle("-fx-font-size: 18");
		itemFreshStart = new MenuItem("Fresh Start");
		itemFreshStart.setStyle("-fx-font-size: 18");

		menuMenu.getItems().addAll(itemFreshStart, itemExit);

		mainMenuBar.getMenus().add(menuMenu);

	}


	//-----Betting scene-----//
	public Scene createBettingScene() {
		// left side
		selectBetText = new Text("Add to bet:");
		selectBetText.setStyle("-fx-font-size: 28");
		currentBetText = new Text("Current bet:");
		currentBetText.setStyle("-fx-font-size: 28");
		totalWinningsText = new Text("Total winnings:");
		totalWinningsText.setStyle("-fx-font-size: 28");
		leftBox = new VBox(28, selectBetText, currentBetText, totalWinningsText);
		leftBox.setAlignment(Pos.CENTER);

		// center
		betAmount = new TextField();
		betAmount.setPromptText("Enter amount to bet");
		betAmount.setStyle("-fx-font-size: 24");

		resetBets = new Button("Reset bets");
		resetBets.setStyle("-fx-font-size: 24");
		inputsBox = new HBox(10, betAmount, resetBets);

		selectPlayer = new Button("Player");
		selectPlayer.setStyle("-fx-font-size: 24");
		selectTie = new Button("Tie");
		selectTie.setStyle("-fx-font-size: 24");
		selectBanker = new Button("Banker");
		selectBanker.setStyle("-fx-font-size: 24");
		buttonsBox = new HBox(10, selectPlayer, selectTie, selectBanker);

		currentBetDisplay = new TextField("None selected");
		currentBetDisplay.setStyle("-fx-font-size: 24");
		currentBetDisplay.setEditable(false);
		totalWinningsDisplay = new TextField("5");
		totalWinningsDisplay.setStyle("-fx-font-size: 24");
		totalWinningsDisplay.setEditable(false);
		emptyText = new TextField("hi");
		emptyText.setStyle("-fx-font-size: 24");
		emptyText.setEditable(false);
		emptyText.setVisible(false);

		centerBox = new VBox(20, inputsBox, buttonsBox, currentBetDisplay, totalWinningsDisplay, emptyText);
		centerBox.setAlignment(Pos.CENTER);


		// right side
		playButton = new Button("Play");
		playButton.setStyle("-fx-font-size: 24");
		// playButton.setPrefWidth(totalWinningsText.getLayoutBounds().getWidth());

		Insets inset = new Insets(5);
		bettingBorderPane = new BorderPane();
		bettingBorderPane.setTop(mainMenuBar);
		bettingBorderPane.setLeft(leftBox);
		bettingBorderPane.setCenter(centerBox);
		bettingBorderPane.setRight(playButton);
		BorderPane.setAlignment(playButton, Pos.CENTER);
		BorderPane.setMargin(leftBox, inset);
		BorderPane.setMargin(centerBox, inset);
		BorderPane.setMargin(playButton, inset);

		Scene currentScene = new Scene(bettingBorderPane, 1080, 720);
		// currentScene.getStylesheets().add("./src/main/resources/styles.css");

		return currentScene;
	} // end createBettingScene()


	//-----Play scene-----//
	public Scene createPlayScene(){
		BorderPane playSceneRoot = new BorderPane();

		playerCardHeader = new Text("Player");
		bankerCardHeader = new Text("Banker");
		playerCardFooter = new Text("Score: ");
		bankerCardFooter = new Text("Score: ");
		playerHandBox = new HBox();
		bankerHandBox = new HBox();
		playerCardBox = new VBox(playerCardHeader, playerHandBox, playerCardFooter);
		bankerCardBox = new VBox(bankerCardHeader, bankerHandBox, bankerCardFooter);
		playerWinningsTextField = new TextField("Player total winnings: $" + totalWinnings);
		currentBetTextField = new TextField("Current Bet: $" + currentBet);
		dealAndPlayAgainButton = new Button("Draw");
		resultsTextField = new TextField();
		centralElements = new VBox(playerWinningsTextField, currentBetTextField, dealAndPlayAgainButton, resultsTextField);

		playerWinningsTextField.setEditable(false);
		currentBetTextField.setEditable(false);
		resultsTextField.setEditable(false);

		playSceneRoot.setTop(mainMenuBar);
		playSceneRoot.setLeft(playerCardBox);
		playSceneRoot.setRight(bankerCardBox);
		playSceneRoot.setCenter(centralElements);

		return new Scene(playSceneRoot, 1080, 720);
	}// end createPlayScene

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
	} // end evaluateWinnings()


} // end class
