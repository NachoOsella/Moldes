package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PokerHandTest {

    @Mock
    private Card card; 
    // Mock de la clase Card para usar en los tests si fuera necesario, pero en este caso no se usa directamente

    @InjectMocks
    private PokerHand pokerHand; 
    // Instancia real de PokerHand donde se inyectarán los mocks (si hubiera)

    @BeforeEach
    void setup() {
        pokerHand = new PokerHand(); 
        // Antes de cada test, inicializo una nueva instancia limpia de PokerHand
    }

    @Test
    void hasStraight_Success() throws Exception {
        // Test que verifica si el método privado hasStraight detecta correctamente una escalera (straight)

        List<Card> testHand = Arrays.asList(
            new Card(5, CardSuits.DIAMOND),
            new Card(6, CardSuits.CLUB),
            new Card(7, CardSuits.HEART),
            new Card(8, CardSuits.PIKE),
            new Card(9, CardSuits.DIAMOND),
            new Card(2, CardSuits.CLUB),
            new Card(11, CardSuits.HEART)
        ); 
        // Mano de prueba que contiene una secuencia consecutiva (5,6,7,8,9)

        Method hasStraight = PokerHand.class.getDeclaredMethod("hasStraight", List.class);
        // Obtengo el método privado hasStraight que recibe una lista de cartas

        hasStraight.setAccessible(true); 
        // Hago accesible el método para poder invocarlo desde el test

        Hands result = (Hands) hasStraight.invoke(pokerHand, testHand);
        // Ejecuto el método hasStraight con la mano de prueba y guardo el resultado

        assertEquals(Hands.STRAIGHT, result);
        // Compruebo que el resultado sea Hands.STRAIGHT (escalera detectada correctamente)
    }

    @Test
    void hasStraight_Fail() throws Exception {
        // Test para verificar que hasStraight NO detecta una escalera cuando no hay

        List<Card> testHand = Arrays.asList(
            new Card(3, CardSuits.DIAMOND),
            new Card(5, CardSuits.CLUB),
            new Card(7, CardSuits.HEART),
            new Card(8, CardSuits.PIKE),
            new Card(10, CardSuits.DIAMOND),
            new Card(2, CardSuits.CLUB),
            new Card(11, CardSuits.HEART)
        );
        // Mano sin secuencia consecutiva

        Method hasStraight = PokerHand.class.getDeclaredMethod("hasStraight", List.class);
        hasStraight.setAccessible(true);

        Hands result = (Hands) hasStraight.invoke(pokerHand, testHand);

        assertNull(result);
        // Espero que no detecte ninguna escalera (resultado null)
    }

    @Test
    void testHasFlush_Success() throws Exception {
        // Test para verificar que hasFlush detecta un color (flush) correctamente

        List<Card> testHand = Arrays.asList(
            new Card(5, CardSuits.DIAMOND),
            new Card(8, CardSuits.DIAMOND),
            new Card(11, CardSuits.DIAMOND),
            new Card(7, CardSuits.DIAMOND),
            new Card(10, CardSuits.DIAMOND),
            new Card(5, CardSuits.CLUB),
            new Card(6, CardSuits.PIKE)
        );
        // Mano con 5 cartas del mismo palo (diamante)

        Method hasFlush = PokerHand.class.getDeclaredMethod("hasFlush", List.class);
        hasFlush.setAccessible(true);

        Hands result = (Hands) hasFlush.invoke(pokerHand, testHand);

        assertEquals(Hands.FLUSH, result, "Expected a flush to be detected.");
        // Verifico que se detecte el flush correctamente
    }

    @Test
    void testHasFlush_Fail() throws Exception {
        // Test que verifica que no detecta flush cuando las cartas no tienen el mismo palo

        List<Card> testHand = Arrays.asList(
            new Card(5, CardSuits.DIAMOND),
            new Card(8, CardSuits.DIAMOND),
            new Card(11, CardSuits.DIAMOND),
            new Card(7, CardSuits.PIKE),
            new Card(10, CardSuits.DIAMOND),
            new Card(5, CardSuits.CLUB),
            new Card(6, CardSuits.PIKE)
        );
        // Mano que no tiene 5 cartas del mismo palo

        Method hasFlush = PokerHand.class.getDeclaredMethod("hasFlush", List.class);
        hasFlush.setAccessible(true);

        Hands result = (Hands) hasFlush.invoke(pokerHand, testHand);

        assertNull(result, "Expected NO flush to be detected.");
        // Espero que no detecte flush (resultado null)
    }

    @Test
    void testHighestCard() throws Exception {
        // Test para obtener la carta más alta dentro de la mano

        List<Card> testHand = Arrays.asList(
            new Card(5, CardSuits.DIAMOND),
            new Card(8, CardSuits.DIAMOND),
            new Card(11, CardSuits.DIAMOND),
            new Card(7, CardSuits.PIKE),
            new Card(10, CardSuits.DIAMOND),
            new Card(5, CardSuits.CLUB),
            new Card(6, CardSuits.PIKE)
        );
        // Mano de prueba con diferentes valores

        Method highestCard = PokerHand.class.getDeclaredMethod("getHighestCard", List.class);
        highestCard.setAccessible(true);

        Card result = (Card) highestCard.invoke(pokerHand, testHand);
        // Invoco el método para obtener la carta más alta

        assertEquals(11,result.getNumber(), "Expected to get the highest card.");
        // Verifico que la carta más alta sea la de número 11
    }

    @Test
    void testHasPair() throws Exception{
        // Test para detectar si hay un par en la mano

        List<Card> testHand = Arrays.asList(
            new Card(5, CardSuits.DIAMOND),
            new Card(8, CardSuits.DIAMOND),
            new Card(11, CardSuits.DIAMOND),
            new Card(7, CardSuits.PIKE),
            new Card(10, CardSuits.DIAMOND),
            new Card(5, CardSuits.CLUB),
            new Card(6, CardSuits.PIKE)
        );
        // Mano con un par de cincos

        Method hasPair = PokerHand.class.getDeclaredMethod("returnRepeatHands", List.class);
        hasPair.setAccessible(true);

        Hands result = (Hands) hasPair.invoke(pokerHand, testHand);

        assertEquals(Hands.PAIR, result);
        // Verifico que se detecte el par
    }

    @Test
    void testHasTwoPair() throws Exception{
        // Test para detectar si hay dos pares en la mano

        List<Card> testHand = Arrays.asList(
            new Card(5, CardSuits.DIAMOND),
            new Card(8, CardSuits.DIAMOND),
            new Card(11, CardSuits.DIAMOND),
            new Card(7, CardSuits.PIKE),
            new Card(10, CardSuits.DIAMOND),
            new Card(5, CardSuits.CLUB),
            new Card(8, CardSuits.PIKE)
        );
        // Mano con dos pares: cincos y ochos

        Method hasTwoPair = PokerHand.class.getDeclaredMethod("returnRepeatHands", List.class);
        hasTwoPair.setAccessible(true);

        Hands result = (Hands) hasTwoPair.invoke(pokerHand, testHand);

        assertEquals(Hands.TWO_PAIR, result);
        // Verifico que se detecten los dos pares
    }

    @Test
    void testHasThree() throws Exception {
        // Test para detectar un trío (three of a kind)

        List<Card> testHand = Arrays.asList(
            new Card(5, CardSuits.DIAMOND),
            new Card(8, CardSuits.DIAMOND),
            new Card(11, CardSuits.DIAMOND),
            new Card(7, CardSuits.PIKE),
            new Card(10, CardSuits.DIAMOND),
            new Card(5, CardSuits.CLUB),
            new Card(5, CardSuits.PIKE)
        );
        // Mano con tres cincos

        Method hasThree = PokerHand.class.getDeclaredMethod("returnRepeatHands", List.class);
        hasThree.setAccessible(true);

        Hands result = (Hands) hasThree.invoke(pokerHand, testHand);

        assertEquals(Hands.THREE_OF_A_KIND, result);
        // Verifico que se detecte el trío
    }

    @Test
    void testHasPoker() throws Exception{
        // Test para detectar un póker (four of a kind)

        List<Card> testHand = Arrays.asList(
            new Card(5, CardSuits.DIAMOND),
            new Card(8, CardSuits.DIAMOND),
            new Card(11, CardSuits.DIAMOND),
            new Card(7, CardSuits.PIKE),
            new Card(5, CardSuits.HEART),
            new Card(5, CardSuits.CLUB),
            new Card(5, CardSuits.PIKE)
        );
        // Mano con cuatro cincos (póker)

        Method hasPair = PokerHand.class.getDeclaredMethod("returnRepeatHands", List.class);
        hasPair.setAccessible(true);

        Hands result = (Hands) hasPair.invoke(pokerHand, testHand);

        assertEquals(Hands.POKER, result);
        // Verifico que se detecte el póker
    }

    @Test
    void testHasStraightFlush_Success() throws Exception{
        // Test para detectar un escalera de color (straight flush)

        List<Card> testHand = Arrays.asList(
            new Card(5, CardSuits.DIAMOND),
            new Card(6, CardSuits.DIAMOND),
            new Card(7, CardSuits.DIAMOND),
            new Card(9, CardSuits.DIAMOND),
            new Card(8, CardSuits.DIAMOND),
            new Card(5, CardSuits.CLUB),
            new Card(6, CardSuits.PIKE)
        );
        // Mano con una secuencia consecutiva del mismo palo (diamante)

        Method hasStraightFlush = PokerHand.class.getDeclaredMethod("hasStraightFlush", List.class);
        hasStraightFlush.setAccessible(true);

        Hands result = (Hands) hasStraightFlush.invoke(pokerHand, testHand);

        assertEquals(Hands.STRAIGHT_FLUSH, result);
        // Verifico que se detecte el straight flush
    }

    @Test
    void testHasStraightFlush_Fail() throws Exception {
        // Test que verifica que no detecta straight flush si la secuencia no es del mismo palo

        List<Card> testHand = Arrays.asList(
            new Card(5, CardSuits.DIAMOND),
            new Card(6, CardSuits.DIAMOND),
            new Card(7, CardSuits.DIAMOND),
            new Card(9, CardSuits.PIKE), // palo diferente
            new Card(8, CardSuits.DIAMOND),
            new Card(5, CardSuits.CLUB),
            new Card(6, CardSuits.PIKE)
        );
        // Mano con secuencia que falla por un palo distinto en medio

        Method hasStraightFlush = PokerHand.class.getDeclaredMethod("hasStraightFlush", List.class);
        hasStraightFlush.setAccessible(true);

        Hands result = (Hands) hasStraightFlush.invoke(pokerHand, testHand);

        assertNull(result);
        // Espero que no detecte el straight flush (resultado null)
    }
}
