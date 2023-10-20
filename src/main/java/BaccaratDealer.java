import java.util.ArrayList;
import java.util.Collections;

public class BaccaratDealer {
    ArrayList<Card> deck;

    // generates a new standard 52-card deck of Card objects
    public void generateDeck() {
        deck = new ArrayList<>();

        Card hearts;
        Card diamonds;
        Card clubs;
        Card spades;
        for (int i = 1; i <= 13; i++) {
            hearts = new Card("Hearts", i);
            diamonds = new Card("Diamonds", i);
            clubs = new Card("Clubs", i);
            spades = new Card("Spades", i);

            deck.add(hearts);
            deck.add(diamonds);
            deck.add(clubs);
            deck.add(spades);

        }

    } // end generateDeck()


    // deals first two cards of the deck and returns them in an ArrayList
    public ArrayList<Card> dealHand() {
        ArrayList<Card> result = new ArrayList<>();

        result.add(deck.get(0));
        deck.remove(0);

        result.add(deck.get(0));
        deck.remove(0);

        return result;
    } // end dealHand()


    // draws the top card off the deck and returns it
    public Card drawOne() {
        Card result = deck.get(0);
        deck.remove(0);

        return result;
    } // end drawOne()


    // generates a new deck of 52 cards and randomizes the order
    public void shuffleDeck() {
        generateDeck();
        Collections.shuffle(deck);
    } // end shuffleDeck()


    // returns the current size of the deck
    public int deckSize() {
        return deck.size();
    } // end deckSize()



} // end class
