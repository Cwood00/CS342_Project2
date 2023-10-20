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
            Card discard = testDealer.drawOne();
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

//    @Test
//    void testDrawOne2() {
//        BaccaratDealer testDealer = new BaccaratDealer();
//        testDealer.generateDeck();
//
//        for (int i = 0; i < 3; i++) {
//            Card discard = testDealer.drawOne();
//        }
//
//        Card test1 = testDealer.drawOne();
//
//        assertEquals(49, );
//        Card test2 = testDealer.drawOne();
//        Card test3 = testDealer.drawOne();
//        Card test4 = testDealer.drawOne();
//
//        assertEquals(48, testDealer.deckSize());
//
//        assertEquals("Hearts", test1.suite);
//        assertEquals(1, test1.value);
//        assertEquals("Diamonds", test2.suite);
//        assertEquals(1, test2.value);
//        assertEquals("Clubs", test3.suite);
//        assertEquals(1, test3.value);
//        assertEquals("Spades", test4.suite);
//        assertEquals(1, test4.value);
//    }

} // end class
