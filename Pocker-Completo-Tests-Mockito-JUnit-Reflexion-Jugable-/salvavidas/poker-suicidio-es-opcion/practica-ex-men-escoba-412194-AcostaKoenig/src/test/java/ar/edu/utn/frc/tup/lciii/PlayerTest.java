package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player; // Objeto Player que será probado

    // Se ejecuta antes de cada test para crear un nuevo jugador
    @BeforeEach
    void setUp() {
        player = new Player("Juan", false, 1000);
    }

    // Verifica que el constructor inicialice correctamente los atributos del jugador
    @Test
    void testConstructor() {
        assertEquals("Juan", player.getName());
        assertFalse(player.isBot());
        assertEquals(1000, player.getChips());
        assertTrue(player.getHand().isEmpty());
        assertEquals(0, player.getBet());
        assertFalse(player.hasFolded());
        assertFalse(player.isDealer());
    }

    // Prueba que solo se pueden agregar hasta 2 cartas en la mano y que la tercera no se añade
    @Test
    void testAddCard() {
        Card card1 = new Card(10, CardSuits.HEART);
        Card card2 = new Card(11, CardSuits.CLUB);
        Card card3 = new Card(12, CardSuits.DIAMOND); // No debería agregarse porque ya hay 2 cartas

        player.addCard(card1);
        player.addCard(card2);
        player.addCard(card3);

        List<Card> hand = player.getHand();
        assertEquals(2, hand.size());
        assertTrue(hand.contains(card1));
        assertTrue(hand.contains(card2));
        assertFalse(hand.contains(card3));
    }

    // Verifica que clearHand vacíe la mano del jugador
    @Test
    void testClearHand() {
        player.addCard(new Card(10, CardSuits.CLUB));
        player.clearHand();
        assertTrue(player.getHand().isEmpty());
    }

    // Verifica que placeBet descuente fichas correctamente y acumule la apuesta actual
    @Test
    void testPlaceBet() {
        player.placeBet(200);
        assertEquals(800, player.getChips());
        assertEquals(200, player.getBet());
    }

    // Verifica que placeBet limite la apuesta a las fichas disponibles cuando se intenta apostar más
    @Test
    void testPlaceBetMoreThanChips() {
        player.placeBet(1200); // Intentar apostar más fichas de las que tiene
        assertEquals(0, player.getChips());   // Se queda sin fichas
        assertEquals(1000, player.getBet());  // La apuesta máxima es la cantidad total de fichas iniciales
    }

    // Verifica que setBet cambie correctamente el valor de la apuesta actual
    @Test
    void testSetBet() {
        player.setBet(500);
        assertEquals(500, player.getBet());
    }

    // Verifica que fold cambie el estado de folded a true
    @Test
    void testFold() {
        player.fold();
        assertTrue(player.hasFolded());
    }

    // Verifica que resetForNewRound reinicie la apuesta, folded y limpie la mano
    @Test
    void testResetForNewRound() {
        player.addCard(new Card(2, CardSuits.DIAMOND));
        player.placeBet(300);
        player.fold();

        player.resetForNewRound();

        assertEquals(0, player.getBet());
        assertFalse(player.hasFolded());
        assertTrue(player.getHand().isEmpty());
    }

    // Verifica que winChips aumente correctamente las fichas del jugador
    @Test
    void testWinChips() {
        player.winChips(250);
        assertEquals(1250, player.getChips());
    }

    // Verifica que setChips modifique correctamente las fichas del jugador
    @Test
    void testSetChips() {
        player.setChips(500);
        assertEquals(500, player.getChips());
    }

    // Verifica que el estado de dealer se pueda modificar correctamente
    @Test
    void testDealerFlag() {
        assertFalse(player.isDealer());
        player.setDealer(true);
        assertTrue(player.isDealer());
    }
}
