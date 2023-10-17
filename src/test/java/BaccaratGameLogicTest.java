//Tester file for the BaccaratGameLogic class

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;


public class BaccaratGameLogicTest {
    BaccaratGameLogic gameLogic = new BaccaratGameLogic();

    @Test
    void handTotal2Cards(){
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card("Diamonds", 7));
        hand.add(new Card("Spades", 12));

        assertEquals(gameLogic.handTotal(hand), 7);
    }

    @Test
    void handTotal3Cards(){
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card("Heart", 9));
        hand.add(new Card("Clubs", 6));
        hand.add(new Card("Spades", 9));

        assertEquals(gameLogic.handTotal(hand), 4);
    }

    @Test
    void whoWonPlayerWin(){
        ArrayList<Card> playerHand = new ArrayList<>();
        ArrayList<Card> bankerHand = new ArrayList<>();

        playerHand.add(new Card("Clubs", 4));
        playerHand.add(new Card("Diamonds", 3));
        bankerHand.add(new Card("Hearts", 5));
        bankerHand.add(new Card("Clubs", 1));
        bankerHand.add(new Card("Spades", 12));

        assertEquals(gameLogic.whoWon(playerHand, bankerHand), "Player");
    }

    @Test
    void whoWonBankerWin(){
        ArrayList<Card> playerHand = new ArrayList<>();
        ArrayList<Card> bankerHand = new ArrayList<>();

        playerHand.add(new Card("Clubs", 5));
        playerHand.add(new Card("Diamonds", 7));
        playerHand.add(new Card("Hearts", 10));
        bankerHand.add(new Card("Hearts", 5));
        bankerHand.add(new Card("Spades", 9));

        assertEquals(gameLogic.whoWon(playerHand, bankerHand), "Banker");
    }

    @Test
    void whoWonDraw(){
        ArrayList<Card> playerHand = new ArrayList<>();
        ArrayList<Card> bankerHand = new ArrayList<>();

        playerHand.add(new Card("Clubs", 9));
        playerHand.add(new Card("Diamonds", 7));
        bankerHand.add(new Card("Hearts", 2));
        bankerHand.add(new Card("Spades", 4));

        assertEquals(gameLogic.whoWon(playerHand, bankerHand), "Draw");
    }

    @Test
    void evaluatePlayerDrawValidDraw(){
        ArrayList<Card> playerHand = new ArrayList<>();
        playerHand.add(new Card("Diamonds", 5));
        playerHand.add(new Card("Spades", 13));

        assertTrue(gameLogic.evaluatePlayerDraw(playerHand));
    }

    @Test
    void evaluatePlayerDrawNoDraw(){
        ArrayList<Card> playerHand = new ArrayList<>();
        playerHand.add(new Card("Hearts", 5));
        playerHand.add(new Card("Clubs", 1));

        assertFalse(gameLogic.evaluatePlayerDraw(playerHand));
    }

    @Test
    void evaluateBankerDrawValidDrawNoPlayerDraw(){
        ArrayList<Card> bankerHand = new ArrayList<>();
        bankerHand.add(new Card("Hearts", 9));
        bankerHand.add(new Card("Diamonds", 6));

        assertTrue(gameLogic.evaluateBankerDraw(bankerHand, null));
    }

    @Test
    void evaluateBankerDrawNoDrawNoPlayerDraw(){
        ArrayList<Card> bankerHand = new ArrayList<>();
        bankerHand.add(new Card("Clubs", 8));
        bankerHand.add(new Card("Spades", 8));

        assertFalse(gameLogic.evaluateBankerDraw(bankerHand, null));
    }

    @Test
    void evaluateBankerDrawValidDrawWithPlayerDraw(){
        ArrayList<Card> bankerHand = new ArrayList<>();
        bankerHand.add(new Card("Clubs", 10));
        bankerHand.add(new Card("Spades", 6));

        assertTrue(gameLogic.evaluateBankerDraw(bankerHand, new Card("Hearts", 6)));
    }

    @Test
    void evaluateBankerDrawNoDrawWithPlayerDraw(){
        ArrayList<Card> bankerHand = new ArrayList<>();
        bankerHand.add(new Card("Clubs", 2));
        bankerHand.add(new Card("Spades", 2));

        assertFalse(gameLogic.evaluateBankerDraw(bankerHand, new Card("Hearts", 11)));
    }
}