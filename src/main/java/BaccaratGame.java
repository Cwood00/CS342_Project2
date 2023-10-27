// Name:Chris Wood, NetID:cwood35
// Name:Ana Theys,  NatID:athey3

import javafx.animation.PauseTransition;
import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


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
	Text playerCardHeader, bankerCardHeader, playerCardFooter, bankerCardFooter;
	HBox playerHandBox, bankerHandBox;
	VBox playerCardBox, bankerCardBox;
	TextField playerWinningsTextField, currentBetTextField;
	ListView<String> resultsListView;
	ObservableList<String> resultsList;
	ImageView playerCard1, playerCard2, playerCard3, bankerCard1, bankerCard2, bankerCard3;
	Button dealAndPlayAgainButton;
	VBox centralElements;
	EventHandler<ActionEvent> replayEvent, bankerDrawEvent, playerDrawEvent, firstDrawEvent;
	PauseTransition firstDealPause, secondDealPause, thirdDealPause;
	static final double dealBetweenCardDelay = 0.33;
	static final int cardWidth = 120;
	static final int cardHeight = 175;
	// Map of images of all 52 playing cards. Suite is map key, card number is ArrayList index
	Map<String, ArrayList<Image>> cardMap;
	// ----------------------------------------------------------------------

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//TODO remove these lines, only here for testing
		betPlacedOn = "Draw";
		currentBet = 100.00;

		theDealer = new BaccaratDealer();
		gameLogic = new BaccaratGameLogic();

		primaryStage.setTitle("Baccarat");

		populateCardMap();

		createMenuBar();

		Scene startScene = createStartScene();
		Scene bettingScene = createBettingScene();
		Scene playScene = createPlayScene();

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
		playButton.setOnAction(e -> {
			primaryStage.setScene(playScene);
		});

		// Play scene event handlers

		firstDealPause.setOnFinished(e ->{
			playerCard2.setImage(cardMap.get(playerHand.get(1).suite).get(playerHand.get(1).value));
			//Display score for player
			playerCardFooter.setText("Score: " + gameLogic.handTotal(playerHand));
			secondDealPause.play();
		});

		secondDealPause.setOnFinished(e ->{
			bankerCard1.setImage(cardMap.get(bankerHand.get(0).suite).get(bankerHand.get(0).value));
			thirdDealPause.play();
		});

		thirdDealPause.setOnFinished(e -> {
			bankerCard2.setImage(cardMap.get(bankerHand.get(1).suite).get(bankerHand.get(1).value));
			//Display score for banker
			bankerCardFooter.setText("Score: " + gameLogic.handTotal(bankerHand));

			dealAndPlayAgainButton.setDisable(false);
			if(gameLogic.handTotal(playerHand) >= 8 || gameLogic.handTotal(bankerHand) >= 8){//Natural win
				endRound();
			}
			else if(gameLogic.evaluatePlayerDraw(playerHand)){
				dealAndPlayAgainButton.setOnAction(playerDrawEvent);
				dealAndPlayAgainButton.setText("Draw for Player");
			}
			else if(gameLogic.evaluateBankerDraw(bankerHand, null)){
				dealAndPlayAgainButton.setOnAction(bankerDrawEvent);
				dealAndPlayAgainButton.setText("Draw for Banker");
			}
			else{
				endRound();
			}
		});

		this.replayEvent = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(bettingScene);

				playerHand = null;
				bankerHand = null;
				playerCard1.setImage(null);
				playerCard2.setImage(null);
				playerCard3.setImage(null);
				bankerCard1.setImage(null);
				bankerCard2.setImage(null);
				bankerCard3.setImage(null);

				playerCardFooter.setText("Score: 0");
				bankerCardFooter.setText("Score: 0");

				resultsList.clear();
				resultsListView.setItems(resultsList);

				Button thisButton = ((Button)event.getSource());
				thisButton.setText("Deal");
				thisButton.setOnAction(firstDrawEvent);
			}
		};
		this.bankerDrawEvent = new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				//Draw card
				Card bankerDraw = theDealer.drawOne();
				bankerHand.add(bankerDraw);
				//Update displayed score
				bankerCardFooter.setText("Score: " + gameLogic.handTotal(bankerHand));
				//Display card
				bankerCard3.setImage(cardMap.get(bankerDraw.suite).get(bankerDraw.value));

				endRound();
			}
		};
		this.playerDrawEvent = new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				//Draw card
				Card playerDraw = theDealer.drawOne();
				playerHand.add(playerDraw);
				//Update displayed score
				playerCardFooter.setText("Score: " + gameLogic.handTotal(playerHand));
				//Display card
				playerCard3.setImage(cardMap.get(playerDraw.suite).get(playerDraw.value));

				Button thisButton = (Button)event.getSource();

				if(gameLogic.evaluateBankerDraw(bankerHand, playerDraw)) {
					thisButton.setOnAction(bankerDrawEvent);
					thisButton.setText("Draw for Banker");
				}
				else{
					endRound();
				}
			}
		};
		this.firstDrawEvent = new EventHandler<>(){
			@Override
			public void handle(ActionEvent event) {
				//Deal cards
				theDealer.shuffleDeck();
				playerHand = theDealer.dealHand();
				bankerHand = theDealer.dealHand();

				//Display cards
				playerCard1.setImage(cardMap.get(playerHand.get(0).suite).get(playerHand.get(0).value));
				firstDealPause.play();

				((Button)event.getSource()).setDisable(true);
			}
		};

		dealAndPlayAgainButton.setOnAction(firstDrawEvent);

		// Lights, camera, ACTION

		primaryStage.setScene(startScene);
		primaryStage.show();

	} // end start()

	public void populateCardMap(){
		cardMap = new TreeMap<>();
		String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};

		for(String suit: suits){
			cardMap.put(suit, new ArrayList<>(14));
			cardMap.get(suit).add(null);//Added so that card value matches index
			for(int i = 1; i <= 13; i++){
				cardMap.get(suit).add(new Image(i + "_of_" + suit.toLowerCase() + ".png"));
			}
		}
	}

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
		firstDealPause = new PauseTransition(Duration.seconds(dealBetweenCardDelay));
		secondDealPause = new PauseTransition(Duration.seconds(dealBetweenCardDelay));
		thirdDealPause = new PauseTransition(Duration.seconds(dealBetweenCardDelay));

		BorderPane playSceneRoot = new BorderPane();

		playerCardHeader = new Text("Player");
		bankerCardHeader = new Text("Banker");
		playerCardFooter = new Text("Score: 0");
		bankerCardFooter = new Text("Score: 0");

		playerCardHeader.setStyle("-fx-font-size: 32");
		bankerCardHeader.setStyle("-fx-font-size: 32");
		playerCardFooter.setStyle("-fx-font-size: 32");
		bankerCardFooter.setStyle("-fx-font-size: 32");

		playerCard1 = new ImageView();
		playerCard2 = new ImageView();
		playerCard3 = new ImageView();
		bankerCard1 = new ImageView();
		bankerCard2 = new ImageView();
		bankerCard3 = new ImageView();

		playerHandBox = new HBox(playerCard1, playerCard2, playerCard3);
		bankerHandBox = new HBox(bankerCard1, bankerCard2, bankerCard3);
		playerCardBox = new VBox(playerCardHeader, playerHandBox, playerCardFooter);
		bankerCardBox = new VBox(bankerCardHeader, bankerHandBox, bankerCardFooter);
		playerWinningsTextField = new TextField("Player total winnings: $" + totalWinnings);
		currentBetTextField = new TextField("Current Bet: $" + currentBet);
		dealAndPlayAgainButton = new Button("Deal");
		resultsListView = new ListView<>();
		resultsList = FXCollections.observableArrayList();
		centralElements = new VBox(playerWinningsTextField, currentBetTextField, dealAndPlayAgainButton, resultsListView);

		playerWinningsTextField.setMaxSize(512, 32);
		currentBetTextField.setPadding(new Insets(20, 0,50,0));
		currentBetTextField.setMaxSize(512, 32);
		dealAndPlayAgainButton.setPrefSize(512, 48);
		dealAndPlayAgainButton.setStyle("-fx-font-size: 24");

		playerCard1.setFitWidth(cardWidth);
		playerCard1.setFitHeight(cardHeight);
		playerCard2.setFitWidth(cardWidth);
		playerCard2.setFitHeight(cardHeight);
		playerCard3.setFitWidth(cardWidth);
		playerCard3.setFitHeight(cardHeight);
		bankerCard1.setFitWidth(cardWidth);
		bankerCard1.setFitHeight(cardHeight);
		bankerCard2.setFitWidth(cardWidth);
		bankerCard2.setFitHeight(cardHeight);
		bankerCard3.setFitWidth(cardWidth);
		bankerCard3.setFitHeight(cardHeight);

		playerWinningsTextField.setEditable(false);
		currentBetTextField.setEditable(false);
		playerWinningsTextField.setStyle("-fx-font-size: 20");
		currentBetTextField.setStyle("-fx-font-size: 20");
		resultsListView.setStyle("-fx-font-size: 16");
		resultsListView.setPrefHeight(125);

		BorderPane.setAlignment(playerCardBox, Pos.BOTTOM_CENTER);
		BorderPane.setAlignment(bankerCardBox, Pos.BOTTOM_CENTER);
		BorderPane.setAlignment(centralElements, Pos.CENTER);
		BorderPane.setMargin(playerCardBox, new Insets(5));
		BorderPane.setMargin(bankerCardBox, new Insets(5));
		BorderPane.setMargin(centralElements, new Insets(5));

		playSceneRoot.setTop(mainMenuBar);
		playSceneRoot.setLeft(playerCardBox);
		playSceneRoot.setRight(bankerCardBox);
		playSceneRoot.setCenter(centralElements);

		playSceneRoot.setStyle("-fx-background-color: DarkGreen");

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

	//Called whenever a round ends. Evaluates the winnings, displays the result,
	//and sets up button for replaying the game
	public void endRound(){
		double roundWinnings = evaluateWinnings();
		totalWinnings += evaluateWinnings();
		playerWinningsTextField.setText("Player total winnings: $" + totalWinnings);

		resultsList.add("Player Total: " + gameLogic.handTotal(playerHand) +
				" Banker Total: " + gameLogic.handTotal(bankerHand));
		String winner = gameLogic.whoWon(playerHand, bankerHand);
		if(winner.equals("Draw")) {
            resultsList.add(winner);
        } else {
            resultsList.add(winner + " wins");
        }
		if(roundWinnings < 0){
			resultsList.add("Sorry, you bet " + this.betPlacedOn + "! You lost your bet!");
		}
		else{
			resultsList.add("Congrats you bet " + this.betPlacedOn + "! You won!");
		}
		resultsListView.setItems(resultsList);

		dealAndPlayAgainButton.setOnAction(replayEvent);
		dealAndPlayAgainButton.setText("Replay");
	}

} // end class
