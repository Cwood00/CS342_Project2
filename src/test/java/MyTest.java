import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

class MyTest {
    private BaccaratGame game;

    @BeforeEach
    void setUP(){
        game = new BaccaratGame();
        game.playerHand = new ArrayList<>();
        game.bankerHand = new ArrayList<>();
        game.gameLogic = new BaccaratGameLogic();
    }

	@Test
    void evaluateWinningsPlayerWin(){
        game.currentBet = 42.5;
        game.betPlacedOn = "Player";

        game.playerHand.add(new Card("Hearts", 4));
        game.playerHand.add(new Card("Spades", 3));
        game.bankerHand.add(new Card("Diamonds", 1));
        game.bankerHand.add(new Card("Clubs", 5));

        assertEquals(42.5, game.evaluateWinnings());
    }

    @Test
    void evaluateWinningsBankerWin(){
        game.currentBet = 10.0;
        game.betPlacedOn = "Banker";

        game.playerHand.add(new Card("Hearts", 3));
        game.playerHand.add(new Card("Spades", 3));
        game.playerHand.add(new Card("Hearts", 9));
        game.bankerHand.add(new Card("Diamonds", 1));
        game.bankerHand.add(new Card("Clubs", 5));

        assertEquals(9.5, game.evaluateWinnings());
    }

    @Test
    void evaluateWinningsTie(){
        game.currentBet = 8.125;
        game.betPlacedOn = "Draw";

        game.playerHand.add(new Card("Hearts", 4));
        game.playerHand.add(new Card("Spades", 3));
        game.bankerHand.add(new Card("Diamonds", 2));
        game.bankerHand.add(new Card("Clubs", 5));

        assertEquals(65.0, game.evaluateWinnings());
    }

    @Test
    void evaluateWinningsLostBet(){
        game.currentBet = 20.0;
        game.betPlacedOn = "Draw";

        game.playerHand.add(new Card("Hearts", 4));
        game.playerHand.add(new Card("Spades", 3));
        game.bankerHand.add(new Card("Diamonds", 1));
        game.bankerHand.add(new Card("Clubs", 5));

        assertEquals(-20.0, game.evaluateWinnings());
    }
}
