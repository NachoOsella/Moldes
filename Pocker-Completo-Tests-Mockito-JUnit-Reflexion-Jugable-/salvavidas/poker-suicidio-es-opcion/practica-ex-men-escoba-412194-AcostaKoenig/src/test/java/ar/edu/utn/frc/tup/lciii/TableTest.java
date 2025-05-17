package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    private Table table; // Variable para la instancia de la clase Table que vamos a testear.
    private List<Player> players; // Lista de jugadores para simular una partida.

    @BeforeEach
    void setUp() {
        // Este método se ejecuta antes de cada test para inicializar un estado limpio.
        table = new Table(); // Crea una nueva mesa para cada test.
        players = new ArrayList<>(); // Inicializa la lista de jugadores vacía.
        players.add(new Player("Alice", false, 1000)); // Agrega un jugador con nombre Alice, no bot, con 1000 fichas.
        players.add(new Player("Bob", false, 1000));   // Agrega otro jugador con nombre Bob, no bot, con 1000 fichas.
    }

    @Test
    void testDealHands() {
        // Prueba que al repartir cartas a los jugadores, cada uno reciba exactamente 2 cartas.
        table.dealHands(players); // Llama al método que reparte las cartas a la lista de jugadores.

        for (Player player : players) {
            List<Card> hand = player.getHand(); // Obtiene la mano de cada jugador.
            assertEquals(2, hand.size(), player.getName() + " debería tener 2 cartas."); 
            // Verifica que la mano tenga 2 cartas. Si falla, muestra el nombre del jugador.
        }
    }

    @Test
    void testDealFlop() {
        // Prueba que al repartir el flop se agreguen 3 cartas comunitarias.
        table.dealFlop(); // Reparte el flop en la mesa.
        List<Card> communityCards = table.getCommunityCards(); // Obtiene las cartas comunitarias.
        assertEquals(3, communityCards.size(), "El flop debería contener 3 cartas."); 
        // Verifica que sean 3 cartas, que es el número esperado para el flop.
    }

    @Test
    void testDealTurn() {
        // Prueba que después de repartir el flop y el turn haya 4 cartas comunitarias.
        table.dealFlop(); // Primero reparte el flop.
        table.dealTurn(); // Luego reparte el turn.
        List<Card> communityCards = table.getCommunityCards(); // Obtiene cartas comunitarias.
        assertEquals(4, communityCards.size(), "Después del turn debería haber 4 cartas comunitarias."); 
        // Verifica que haya 4 cartas (3 flop + 1 turn).
    }

    @Test
    void testDealRiver() {
        // Prueba que después de repartir flop, turn y river, haya 5 cartas comunitarias.
        table.dealFlop(); // Reparte flop (3 cartas).
        table.dealTurn(); // Reparte turn (1 carta).
        table.dealRiver(); // Reparte river (1 carta).
        List<Card> communityCards = table.getCommunityCards(); // Obtiene cartas comunitarias.
        assertEquals(5, communityCards.size(), "Después del river debería haber 5 cartas comunitarias."); 
        // Verifica que sean 5 cartas (3 flop + 1 turn + 1 river).
    }

    @Test
    void testResetDeck() {
        // Prueba que al resetear la mesa se limpien las cartas comunitarias.
        table.dealFlop(); // Primero reparte el flop para agregar cartas comunitarias.
        assertEquals(3, table.getCommunityCards().size()); // Asegura que hay 3 cartas antes del reset.

        table.resetDeck(); // Resetea la mesa y el mazo.
        assertEquals(0, table.getCommunityCards().size(), "Después de resetear, no debería haber cartas comunitarias."); 
        // Verifica que las cartas comunitarias se hayan limpiado después del reset.
    }

    @Test
    void testDeckDoesNotThrowWhenEmpty() {
        // Prueba que repartir cartas repetidamente no lance excepción aunque el mazo se vacíe.
        for (int i = 0; i < 20; i++) {
            table.dealTurn(); // Reparte el turn muchas veces para agotar el mazo.
        }
        assertDoesNotThrow(() -> table.dealRiver(), "No debería lanzar excepción al repartir con mazo vacío."); 
        // Asegura que el método dealRiver no lanza error cuando el mazo está vacío.
    }
}
