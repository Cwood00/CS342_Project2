// Name:Chris Wood, NetID:cwood35
// Name:Ana Theys,  NatID:athey3

import javafx.application.Application;

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
	TextField playerWinningsTextField, currentBetTextField, resultsTextField;
	ImageView playerCard1, playerCard2, playerCard3, bankerCard1, bankerCard2, bankerCard3;
	Button dealAndPlayAgainButton;
	VBox centralElements;
	EventHandler<ActionEvent> replayEvent;
	static final int cardWidth = 125;
	static final int cardHeight = 182;
	// Map of images of all 52 playing card Key is suite, ArrayList index is card number
	Map<String, ArrayList<Image>> cardMap;
	// ----------------------------------------------------------------------

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//TODO remove these lines, only here for testing
		betPlacedOn = "Banker";
		currentBet = 100.0;

		theDealer = new BaccaratDealer();
		gameLogic = new BaccaratGameLogic();

		primaryStage.setTitle("Baccarat");

		populateCardMap();

		createMenuBar();

		Scene startScene = createStartScene();
		Scene bettingScene = createBettingScene();
		Scene playScene = createPlayScene();

		// Play scene EventHandlers
		EventHandler<ActionEvent> firstDrawEvent;
		this.replayEvent = new EventHandler<ActionEvent>() {
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
		EventHandler<ActionEvent> playerDrawEvent = new EventHandler<>() {
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
		firstDrawEvent = new EventHandler<>(){
			@Override
			public void handle(ActionEvent event) {
				//Deal cards
				theDealer.shuffleDeck();
				playerHand = theDealer.dealHand();
				bankerHand = theDealer.dealHand();
				//Display score for cards
				playerCardFooter.setText("Score: " + gameLogic.handTotal(playerHand));
				bankerCardFooter.setText("Score: " + gameLogic.handTotal(bankerHand));
				//Display cards
				playerCard1.setImage(cardMap.get(playerHand.get(0).suite).get(playerHand.get(0).value));
				playerCard2.setImage(cardMap.get(playerHand.get(1).suite).get(playerHand.get(1).value));
				bankerCard1.setImage(cardMap.get(bankerHand.get(0).suite).get(bankerHand.get(0).value));
				bankerCard2.setImage(cardMap.get(bankerHand.get(1).suite).get(bankerHand.get(1).value));

				Button thisButton = (Button)event.getSource();

				if(gameLogic.handTotal(playerHand) >= 8 || gameLogic.handTotal(bankerHand) >= 8){//Natural win
					endRound();
				}
				else if(gameLogic.evaluatePlayerDraw(playerHand)){
					thisButton.setOnAction(playerDrawEvent);
					thisButton.setText("Draw for Player");
				}
				else if(gameLogic.evaluateBankerDraw(bankerHand, null)){
					thisButton.setOnAction(bankerDrawEvent);
					thisButton.setText("Draw for Banker");
				}
				else{
					endRound();
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

		primaryStage.setScene(playScene);
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
		BorderPane playSceneRoot = new BorderPane();

		playerCardHeader = new Text("Player");
		bankerCardHeader = new Text("Banker");
		playerCardFooter = new Text("Score: 0");
		bankerCardFooter = new Text("Score: 0");

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
		dealAndPlayAgainButton = new Button("Draw");
		resultsTextField = new TextField();
		centralElements = new VBox(playerWinningsTextField, currentBetTextField, dealAndPlayAgainButton, resultsTextField);

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
		resultsTextField.setEditable(false);
		resultsTextField.setPrefColumnCount(3);

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

	public void endRound(){
		double roundWinnings = evaluateWinnings();
		String displayedResults = "Player Total: " + gameLogic.handTotal(playerHand) +
				                  " Banker Total: " + gameLogic.handTotal(bankerHand) + "\n" +
				                   gameLogic.whoWon(playerHand, bankerHand) + "\n";

		totalWinnings += evaluateWinnings();
		playerWinningsTextField.setText("Player total winnings: $" + totalWinnings);

		if(roundWinnings < 0){
			resultsTextField.setText(displayedResults + "Sorry, you bet " + this.betPlacedOn + "! You lost your bet!");
		}
		else{
			resultsTextField.setText(displayedResults + "Congrats you bet " + this.betPlacedOn + "! You won!");
		}

		dealAndPlayAgainButton.setOnAction(replayEvent);
		dealAndPlayAgainButton.setText("Replay");
	}

} // end class
