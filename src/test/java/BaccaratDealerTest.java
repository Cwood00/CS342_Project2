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
        ArrayList<Card> expected = new ArrayList<>();

        expected.add(new Card("Hearts", 1));
        // not finished
    }
}
