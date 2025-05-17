package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    // Verifica que el método toString devuelva la representación correcta para cartas figura (Q)
    @Test
    void testToString() {
        // Crear una carta con número 12 (Q) y palo DIAMOND
        Card card = new Card(12, CardSuits.DIAMOND);
        // Verificar que el método toString devuelva "Q de DIAMOND"
        assertEquals("Q de DIAMOND", card.toString());
    }

    // Verifica que el método toString no devuelva una representación incorrecta (test negativo)
    @Test
    void testToStringFail() {
        // Crear una carta con número 10 y palo HEART
        Card card = new Card(10, CardSuits.HEART);
        // Verificar que el método toString NO devuelva "Q de HEART" (test negativo)
        assertNotEquals("Q de HEART", card.toString());
    }

    // Verifica que dos cartas iguales sean consideradas iguales con equals()
    @Test
    void testEquals() {
        // Crear dos cartas iguales (mismo número y palo)
        Card card1 = new Card(10, CardSuits.DIAMOND);
        Card card2 = new Card(10, CardSuits.DIAMOND);
        // Verificar que las dos cartas sean consideradas iguales (equals devuelve true)
        assertEquals(card1, card2);
    }

    // Verifica que dos cartas diferentes no sean consideradas iguales
    @Test
    void testEqualsFail() {
        // Crear dos cartas diferentes (números distintos)
        Card card1 = new Card(11, CardSuits.DIAMOND);
        Card card2 = new Card(10, CardSuits.DIAMOND);
        // Verificar que no sean iguales (equals devuelve false)
        assertNotEquals(card1, card2);
    }

    // Verifica que toString devuelva correctamente las letras para las cartas figura (A, J, Q, K)
    @Test
    void testToStringFiguras() {
        // Verificar que toString devuelva las letras correctas para figuras
        assertEquals("A de HEART", new Card(14, CardSuits.HEART).toString());
        assertEquals("J de CLUB", new Card(11, CardSuits.CLUB).toString());
        assertEquals("Q de PIKE", new Card(12, CardSuits.PIKE).toString());
        assertEquals("K de DIAMOND", new Card(13, CardSuits.DIAMOND).toString());
    }

    // Verifica que toString devuelva los números para cartas que no son figura
    @Test
    void testToStringNumeros() {
        // Verificar que toString devuelva los números para cartas no figura
        assertEquals("7 de HEART", new Card(7, CardSuits.HEART).toString());
        assertEquals("10 de CLUB", new Card(10, CardSuits.CLUB).toString());
    }

    // Verifica que equals no considere iguales objetos null ni objetos de otros tipos
    @Test
    void testEqualsNullYTipoDiferente() {
        // Crear una carta
        Card card = new Card(10, CardSuits.HEART);
        // Verificar que la carta no sea igual a null
        assertNotEquals(null, card);
        // Verificar que la carta no sea igual a un objeto de otro tipo
        assertNotEquals("no soy una carta", card);
    }

    // Verifica que dos cartas iguales tengan el mismo hashCode
    @Test
    void testHashCodeIgualdad() {
        // Crear dos cartas iguales
        Card card1 = new Card(9, CardSuits.PIKE);
        Card card2 = new Card(9, CardSuits.PIKE);
        // Verificar que tengan el mismo hashCode
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    // Verifica la consistencia entre equals y hashCode: si dos objetos son iguales, deben tener igual hashCode
    @Test
    void testConsistenciaEqualsHashCode() {
        // Crear dos cartas iguales
        Card card1 = new Card(5, CardSuits.HEART);
        Card card2 = new Card(5, CardSuits.HEART);
        // Si las cartas son iguales, sus hashCode también deben ser iguales
        if (card1.equals(card2)) {
            assertEquals(card1.hashCode(), card2.hashCode());
        }
    }

    // Verifica que un HashSet contenga correctamente cartas iguales, usando equals y hashCode
    @Test
    void testHashSetContiene() {
        // Crear un HashSet y agregar una carta
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(8, CardSuits.CLUB));
        // Verificar que el HashSet contiene una carta igual (testa equals y hashCode)
        assertTrue(cards.contains(new Card(8, CardSuits.CLUB)));
    }
}
