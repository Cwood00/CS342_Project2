public class Card {
    public String suite;
    public int value;

    // suite: Hearts, Diamonds, Clubs, Spades
    // value: numbers 1-13
    public Card(String theSuite, int theValue){
        this.suite = theSuite;
        this.value = theValue;
    }
}
