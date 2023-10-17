//Tester class for the card Class

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    @Test
    void CardConstructor(){
        Card firstCard = new Card("Hearts", 12);
        Card secondCard = new Card("Spades", 1);

        assertEquals(firstCard.suite, "Hearts");
        assertEquals(firstCard.value, 12);
        assertEquals(secondCard.getSuite(), "Spades");
        assertEquals(secondCard.getValue(), 1);
    }
}
