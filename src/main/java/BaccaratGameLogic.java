//Class implements several methods for working with the game logic for Baccarat

import java.util.ArrayList;

public class BaccaratGameLogic {

    //Determines who won the game of Baccarat.
    //Takes in the player's and banker's hands as argument
    //Returns either "Player", "Banker", or "Draw", depending on the result of the game
    public String whoWon(ArrayList<Card> hand1, ArrayList<Card> hand2){
        int playHandTotal = handTotal(hand1);
        int bankerHandTotal = handTotal(hand2);

        if(playHandTotal > bankerHandTotal)
            return "Player";
        else if(bankerHandTotal > playHandTotal)
            return "Banker";
        else
            return "Draw";
    }

    //Calculates the total score for a hand
    //Takes in the hand as an argument, and returns the score
    public int handTotal(ArrayList<Card> hand){
        int total = 0;
        for (Card card : hand){
            //Any card with a value >= 10 counts as 0 in Baccarat
            if(card.value < 10)
                total += card.value;
        }
        //Only the last digit of the score matters in Baccarat
        return total % 10;
    }

    //Determines if the banker needs to draw a third card
    //Takes in the bankers hand, and either the card drawn by the play
    //or null if the player did not draw a card as arguments
    //Returns true if the banker should draw a third card and false otherwise
    public boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard){
        if (playerCard == null)
            return handTotal(hand) < 6;
        switch (playerCard.value){
            case 2:
            case 3:
                return handTotal(hand) < 5;
            case 4:
            case 5:
                return handTotal(hand) < 6;
            case 6:
            case 7:
                return handTotal(hand) < 7;
            case 8:
                return handTotal(hand) < 2;
            //Handles cards with a value of 0, 1, or 9
            default:
                return handTotal(hand) < 4;
        }
    }

    //Determines if the player should draw a third card
    //Takes in the player's hand as an argument
    //Returns true if the player should draw a third card and false otherwise
    public boolean evaluatePlayerDraw(ArrayList<Card> hand){
        return handTotal(hand) < 6;
    }
}
