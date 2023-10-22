import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

public class BaccaratDealerTest {
    // two tests per method


    @Test
    void generateDeckTest() {
        BaccaratDealer testDealer = new BaccaratDealer();
        testDealer.generateDeck();

        assertEquals(52, testDealer.deckSize());

        Card temp;

        for (int i = 0; i < 52; i++) {
            temp = testDealer.drawOne();
            if (i % 4 == 0) {
                assertEquals("Hearts", temp.suite);
            } else if (i % 4 == 1) {
                assertEquals("Diamonds", temp.suite);
            } else if (i % 4 == 2) {
                assertEquals("Clubs", temp.suite);
            } else {
                assertEquals("Spades", temp.suite);
            }

            assertEquals((i/4)+1, temp.value);

            assertEquals(51-i, testDealer.deckSize());
        }

    }

    @Test
    void dealHandTest1() {
        BaccaratDealer testDealer = new BaccaratDealer();
        testDealer.generateDeck();

        ArrayList<Card> expected = new ArrayList<>();
        expected.add(new Card("Hearts", 1));
        expected.add(new Card("Diamonds", 1));
        ArrayList<Card> result = testDealer.dealHand();

        assertEquals(expected.size(), result.size());
        assertEquals(expected.get(0).value, result.get(0).value);
        assertEquals(expected.get(0).suite, result.get(0).suite);
        assertEquals(expected.get(1).value, result.get(1).value);
        assertEquals(expected.get(1).suite, result.get(1).suite);

    }

    @Test
    void dealHandTest2() {
        BaccaratDealer testDealer = new BaccaratDealer();
        testDealer.generateDeck();

        for (int i = 0; i < 7; i++) {
            testDealer.drawOne();
        }

        ArrayList<Card> expected = new ArrayList<>();
        expected.add(new Card("Spades", 2));
        expected.add(new Card("Hearts", 3));
        ArrayList<Card> result = testDealer.dealHand();

        assertEquals(expected.size(), result.size());
        assertEquals(expected.get(0).value, result.get(0).value);
        assertEquals(expected.get(0).suite, result.get(0).suite);
        assertEquals(expected.get(1).value, result.get(1).value);
        assertEquals(expected.get(1).suite, result.get(1).suite);

    }

    @Test
    void testDrawOne1() {
        BaccaratDealer testDealer = new BaccaratDealer();
        testDealer.generateDeck();

        Card test1 = testDealer.drawOne();
        Card test2 = testDealer.drawOne();
        Card test3 = testDealer.drawOne();
        Card test4 = testDealer.drawOne();

        assertEquals(48, testDealer.deckSize());

        assertEquals("Hearts", test1.suite);
        assertEquals(1, test1.value);
        assertEquals("Diamonds", test2.suite);
        assertEquals(1, test2.value);
        assertEquals("Clubs", test3.suite);
        assertEquals(1, test3.value);
        assertEquals("Spades", test4.suite);
        assertEquals(1, test4.value);
    }

    @Test
    void testDrawOne2() {
        BaccaratDealer testDealer = new BaccaratDealer();
        testDealer.generateDeck();

        for (int i = 0; i < 3; i++) {
            testDealer.drawOne();
        }
        Card test1 = testDealer.drawOne();

        assertEquals(48, testDealer.deckSize());
        assertEquals("Spades", test1.suite);
        assertEquals(1, test1.value);


        for (int i = 0; i < 5; i++) {
            testDealer.drawOne();
        }
        Card test2 = testDealer.drawOne();

        assertEquals(42, testDealer.deckSize());
        assertEquals("Diamonds", test2.suite);
        assertEquals(3, test2.value);


        for (int i = 0; i < 10; i++) {
            testDealer.drawOne();
        }
        Card test3 = testDealer.drawOne();

        assertEquals(31, testDealer.deckSize());
        assertEquals("Hearts", test3.suite);
        assertEquals(6, test3.value);


        for (int i = 0; i < 9; i++) {
            testDealer.drawOne();
        }
        Card test4 = testDealer.drawOne();

        assertEquals(21, testDealer.deckSize());
        assertEquals("Clubs", test4.suite);
        assertEquals(8, test4.value);

    }

    @Test
    void testShuffleDeck1() {
        BaccaratDealer defaultDealer = new BaccaratDealer();
        defaultDealer.generateDeck();

        BaccaratDealer shuffledDealer1 = new BaccaratDealer();
        shuffledDealer1.shuffleDeck();

        BaccaratDealer shuffledDealer2 = new BaccaratDealer();
        shuffledDealer2.shuffleDeck();

        assertEquals(52, shuffledDealer1.deckSize());
        assertNotEquals(defaultDealer.deck, shuffledDealer1.deck);

        assertEquals(52, shuffledDealer2.deckSize());
        assertNotEquals(defaultDealer.deck, shuffledDealer2.deck);

        assertNotEquals(shuffledDealer1.deck, shuffledDealer2.deck);
    }

    @Test
    void testShuffleDeck2() {
        BaccaratDealer defaultDealer = new BaccaratDealer();
        defaultDealer.generateDeck();

        BaccaratDealer testDealer = new BaccaratDealer();
        testDealer.generateDeck();

        for (int i = 0; i < 12; i++) {
            testDealer.drawOne();
        }

        assertEquals(40, testDealer.deckSize());

        testDealer.shuffleDeck();

        assertEquals(52, testDealer.deckSize());
        assertNotEquals(defaultDealer.deck, testDealer.deck);

    }

    @Test
    void testDeckSize1() {
        BaccaratDealer testDealer = new BaccaratDealer();

        assertEquals(0,testDealer.deckSize());

        testDealer.generateDeck();

        assertEquals(52, testDealer.deckSize());

    }

    @Test
    void testDeckSize2() {
        BaccaratDealer testDealer = new BaccaratDealer();
        testDealer.generateDeck();

        assertEquals(52, testDealer.deckSize());

        for (int i = 0; i < 51; i +=3) {
            testDealer.drawOne();
            assertEquals(52-(i+1), testDealer.deckSize());

            testDealer.dealHand();
            assertEquals(52-(i+3), testDealer.deckSize());
        }

        assertEquals(1, testDealer.deckSize());

        testDealer.drawOne();

        assertEquals(0, testDealer.deckSize());

    }


} // end class
