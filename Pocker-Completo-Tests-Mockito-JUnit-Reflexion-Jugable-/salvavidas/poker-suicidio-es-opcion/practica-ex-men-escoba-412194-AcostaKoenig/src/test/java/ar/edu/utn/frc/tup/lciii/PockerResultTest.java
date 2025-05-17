package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PokerResultTest {

    // Verifica que el constructor inicializa correctamente los campos hand y highCard
    @Test
    void testConstructorAndFields() {
        PokerResult result = new PokerResult(Hands.FLUSH, 10);
        assertEquals(Hands.FLUSH, result.hand);
        assertEquals(10, result.highCard);
    }

    // Verifica la comparación entre dos manos de poker con distintos tipos (hand)
    @Test
    void testCompareTo_DifferentHands() {
        PokerResult higherHand = new PokerResult(Hands.STRAIGHT_FLUSH, 5);
        PokerResult lowerHand = new PokerResult(Hands.FLUSH, 14);

        // higherHand es mejor mano que lowerHand, entonces:
        // higherHand.compareTo(lowerHand) debe ser > 0 (mayor)
        assertFalse(higherHand.compareTo(lowerHand) < 0); // No es menor
        assertFalse(lowerHand.compareTo(higherHand) > 0); // No es mayor
    }

    // Verifica la comparación entre dos manos iguales pero con diferente carta alta
    @Test
    void testCompareTo_SameHandDifferentHighCard() {
        PokerResult highCardResult = new PokerResult(Hands.PAIR, 12);
        PokerResult lowCardResult = new PokerResult(Hands.PAIR, 9);

        // La mano con carta alta 12 es mejor que la con carta alta 9
        assertTrue(highCardResult.compareTo(lowCardResult) > 0); // mayor que
        assertTrue(lowCardResult.compareTo(highCardResult) < 0); // menor que
    }

    // Verifica que dos manos iguales con misma carta alta se consideren iguales en la comparación
    @Test
    void testCompareTo_SameHandSameHighCard() {
        PokerResult result1 = new PokerResult(Hands.THREE_OF_A_KIND, 7);
        PokerResult result2 = new PokerResult(Hands.THREE_OF_A_KIND, 7);

        assertEquals(0, result1.compareTo(result2)); // comparación igual a 0
    }
}
