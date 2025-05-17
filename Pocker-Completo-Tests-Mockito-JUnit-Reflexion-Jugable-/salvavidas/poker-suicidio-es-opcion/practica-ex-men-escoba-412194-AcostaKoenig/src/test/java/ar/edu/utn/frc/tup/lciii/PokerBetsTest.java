package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para PokerBets
 */
public class PokerBetsTest {

    private PokerBets bets;      // instancia de PokerBets para cada test
    private Player player1;      // jugador 1 para usar en las pruebas
    private Player player2;      // jugador 2 para usar en las pruebas

    @BeforeEach
    void setUp() {
        bets = new PokerBets(50, 100);          // inicializa PokerBets con smallBlind=50 y bigBlind=100
        player1 = new Player("Player 1", true, 1000); // jugador 1 con 1000 fichas
        player2 = new Player("Player 2", false, 1000); // jugador 2 con 1000 fichas
    }

    // Verifica que el constructor inicialice los valores correctamente
    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals(0, bets.getCurrentBet()); // al iniciar currentBet es 0
        assertEquals(0, bets.getPot());        // pot comienza en 0
        assertEquals(50, bets.getSmallBlind()); // smallBlind inicial es 50
        assertEquals(100, bets.getBigBlind()); // bigBlind inicial es 100
    }

    // Verifica que placeBlindBets haga las apuestas smallBlind y bigBlind correctamente
    @Test
    void testPlaceBlindBets() {
        bets.placeBlindBets(player1, player2);

        assertEquals(50, player1.getBet());   // player1 apostó smallBlind
        assertEquals(100, player2.getBet());  // player2 apostó bigBlind
        assertEquals(150, bets.getPot());     // el pozo suma ambas apuestas
        assertEquals(100, bets.getCurrentBet()); // currentBet queda en el valor del bigBlind
    }

    // Verifica que un jugador que llama con suficientes fichas puede igualar la apuesta
    @Test
    void testCallWithEnoughChips() {
        bets.placeBlindBets(player1, player2); // currentBet = 100
        Player caller = new Player("Caller", true, 500); // jugador con 500 fichas
        int amount = bets.call(caller); // llama a la apuesta actual

        assertEquals(100, caller.getBet());   // su apuesta ahora es igual a currentBet
        assertEquals(100, amount);             // cantidad pagada es 100
        assertEquals(250, bets.getPot());     // pozo suma la nueva apuesta
    }

    // Verifica que un jugador con fichas insuficientes no pueda igualar la apuesta y la función devuelve -1
    @Test
    void testCallWithInsufficientChips() {
        bets.placeBlindBets(player1, player2);
        Player caller = new Player("Caller", true, 50); // solo 50 fichas, menos que currentBet=100
        int result = bets.call(caller);

        assertEquals(-1, result);       // llamada no válida, devuelve -1
        assertEquals(0, caller.getBet()); // apuesta del jugador no cambia
    }

    // Verifica que un jugador puede subir la apuesta si tiene fichas suficientes
    @Test
    void testRaiseSuccess() {
        boolean result = bets.raise(player1, 100); // apuesta total de 100
        assertTrue(result);                         // la subida fue exitosa
        assertEquals(100, bets.getCurrentBet());   // currentBet actualizado a 100
        assertEquals(100, player1.getBet());       // jugador apostó 100
    }

    // Verifica que la subida de apuesta falla si el jugador no tiene fichas suficientes
    @Test
    void testRaiseFailsWithInsufficientChips() {
        Player poorPlayer = new Player("Poor", true , 50); // solo 50 fichas
        boolean result = bets.raise(poorPlayer, 100);      // intenta apostar 100
        assertFalse(result);                                // falla la apuesta
        assertEquals(0, poorPlayer.getBet());               // no apuesta nada
    }

    // Verifica que un jugador que va all-in apuesta todas sus fichas, se actualiza pot y currentBet
    @Test
    void testAllInUpdatesPotAndCurrentBet() {
        Player allInPlayer = new Player("AllIn", true , 300);
        int amount = bets.allIn(allInPlayer);

        assertEquals(300, amount);                  // apostó 300 (todo su stack)
        assertEquals(300, allInPlayer.getBet());   // apuesta registrada en jugador
        assertEquals(300, bets.getPot());           // pozo actualizado
        assertEquals(300, bets.getCurrentBet());    // currentBet actualizado a 300
    }

    // Verifica que al hacer all-in con menos fichas que currentBet se crea un side pot
    @Test
    void testAllInCreatesSidePot() {
        bets.raise(player1, 300); // currentBet = 300
        Player shortStack = new Player("Short", false, 100);
        int amount = bets.allIn(shortStack);

        assertEquals(100, amount);                   // apostó todo lo que tenía (100)
        assertEquals(100, shortStack.getBet());      // apuesta del shortStack
        assertEquals(400, bets.getPot());            // pot total = 300 + 100
    }

    // Verifica que resetBets pone currentBet en 0 para nueva ronda
    @Test
    void testResetBets() {
        bets.raise(player1, 200);
        bets.resetBets();
        assertEquals(0, bets.getCurrentBet());
    }

    // Verifica que resetPot vacía el pozo
    @Test
    void testResetPot() {
        bets.raise(player1, 200);
        bets.resetPot();
        assertEquals(0, bets.getPot());
    }

    // Verifica que al premiar el pozo, el jugador ganador recibe las fichas y el pot queda vacío
    @Test
    void testAwardPotToWinner() {
        bets.raise(player1, 200); // pot = 200
        bets.awardPotToWinner(player1);

        assertEquals(1000, player1.getChips()); // fichas originales 1000 - 200 + 200 = 1000 (se resta y luego suma el pot)
        assertEquals(0, bets.getPot());          // pozo vacío
    }

    // Verifica que el jugador puede pasar (check) si currentBet es 0 (sin apostar)
    @Test
    void testCheckWhenBetIsZero() {
        Player checker = new Player("Checker", true, 500);
        bets.check(checker); // debería permitirse
        assertEquals(0, checker.getBet());
    }

    // Verifica que el jugador puede pasar (check) si ya igualó la apuesta actual
    @Test
    void testCheckWhenAlreadyMatchedCurrentBet() {
        bets.raise(player1, 100); // currentBet = 100
        player1.placeBet(100);
        bets.check(player1); // debería permitirse
    }

    // Verifica que el jugador no puede pasar (check) si no igualó la apuesta actual
    @Test
    void testCheckNotAllowedWhenBetNotMatched() {
        bets.raise(player1, 100); // currentBet = 100
        bets.check(player2); // no debería permitirse
    }
}
