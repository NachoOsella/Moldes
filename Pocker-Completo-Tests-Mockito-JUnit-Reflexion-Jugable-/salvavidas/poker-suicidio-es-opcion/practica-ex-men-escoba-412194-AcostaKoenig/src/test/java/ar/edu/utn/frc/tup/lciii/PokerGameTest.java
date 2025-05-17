package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.BeforeEach;  // Importa anotación para método que se ejecuta antes de cada test
import org.junit.jupiter.api.Test;        // Importa anotación para método de prueba

import java.lang.reflect.Field;          // Importa clase para acceder a campos privados vía reflexión
import java.lang.reflect.Method;         // Importa clase para acceder a métodos privados vía reflexión
import java.util.ArrayList;
import java.util.List;                   // Importa lista genérica
import java.util.Scanner;                // Importa Scanner para simular entrada por consola

import static org.junit.jupiter.api.Assertions.*;  // Importa métodos estáticos para aserciones (assertEquals, assertTrue, etc)

public class PokerGameTest {  // Clase que contiene pruebas unitarias para la clase PokerGame
    private PokerGame pokerGame;  // Variable para instancia de la clase PokerGame que vamos a testear

    @BeforeEach
    public void setup() {  // Método que se ejecuta antes de cada test para preparar el ambiente
        pokerGame = new PokerGame();  // Crear nueva instancia del juego
        try {
            Method setupGame = PokerGame.class.getDeclaredMethod("setupGame"); // Obtener método privado setupGame
            setupGame.setAccessible(true);  // Permitir acceso a método privado
            setupGame.invoke(pokerGame);    // Invocar método para inicializar el juego
        } catch (Exception e) {
            fail("Error al preparar el juego: " + e.getMessage());  // Falla el test si hay error en la preparación
        }
    }

    @Test
    public void testDealerRotation() throws Exception {  // Test para validar que el dealer rota correctamente
        Field playersField = PokerGame.class.getDeclaredField("players"); // Obtener campo privado players (lista de jugadores)
        playersField.setAccessible(true);  // Permitir acceso
        List<Player> players = (List<Player>) playersField.get(pokerGame);  // Obtener la lista de jugadores

        Field dealerIndexField = PokerGame.class.getDeclaredField("dealerIndex"); // Obtener índice del dealer
        dealerIndexField.setAccessible(true);
        int initialDealerIndex = (int) dealerIndexField.get(pokerGame);  // Guardar índice inicial del dealer

        Method moveDealerMethod = PokerGame.class.getDeclaredMethod("moveDealer");  // Obtener método privado moveDealer()
        moveDealerMethod.setAccessible(true);
        moveDealerMethod.invoke(pokerGame);  // Ejecutar rotación del dealer

        int newDealerIndex = (int) dealerIndexField.get(pokerGame);  // Obtener nuevo índice del dealer
        assertEquals((initialDealerIndex + 1) % players.size(), newDealerIndex);  // Validar que el dealer avanzó uno (circularmente)

        long dealerCount = players.stream().filter(Player::isDealer).count();  // Contar cuántos jugadores están como dealer
        assertEquals(1, dealerCount, "Debe haber exactamente un dealer activo");  // Debe haber solo un dealer
    }

    @Test
    public void testDealInitialCardsGivesTwoCardsToEachPlayer() throws Exception {  // Test que verifica que se repartan 2 cartas a cada jugador
        Method dealInitialCards = PokerGame.class.getDeclaredMethod("dealInitialCards"); // Obtener método privado dealInitialCards()
        dealInitialCards.setAccessible(true);
        dealInitialCards.invoke(pokerGame);  // Invocar reparto inicial de cartas

        Field playersField = PokerGame.class.getDeclaredField("players"); // Obtener lista de jugadores
        playersField.setAccessible(true);
        List<Player> players = (List<Player>) playersField.get(pokerGame);

        for (Player player : players) {
            assertEquals(2, player.getHand().size(), "Cada jugador debe tener 2 cartas.");  // Validar que cada jugador tenga 2 cartas
        }
    }

    @Test
    public void testAskToPlayAgainReturnsFalseWhenUserInputs2() throws Exception {  // Test que simula entrada 2 para no seguir jugando
        Scanner mockScanner = new Scanner("2\n");  // Crear Scanner que simula ingreso del número 2 por consola
        Field scannerField = PokerGame.class.getDeclaredField("scanner");  // Obtener campo privado scanner
        scannerField.setAccessible(true);
        scannerField.set(pokerGame, mockScanner);  // Reemplazar scanner del juego por el mock

        Method askToPlayAgain = PokerGame.class.getDeclaredMethod("askToPlayAgain");  // Obtener método privado askToPlayAgain()
        askToPlayAgain.setAccessible(true);
        boolean result = (boolean) askToPlayAgain.invoke(pokerGame);  // Invocar método y guardar resultado

        assertFalse(result, "Cuando el usuario ingresa 2, debe retornar false (no seguir jugando).");  // Validar que retorna false
    }

    @Test
    public void testGetUserInputAcceptsValidInput() throws Exception {  // Test que valida entrada válida dentro de rango
        Scanner mockScanner = new Scanner("3\n");  // Simula que usuario ingresa el número 3
        Field scannerField = PokerGame.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(pokerGame, mockScanner);  // Reemplaza scanner original con el mock

        Method getUserInput = PokerGame.class.getDeclaredMethod("getUserInput", int.class, int.class);  // Método que recibe min y max
        getUserInput.setAccessible(true);
        int result = (int) getUserInput.invoke(pokerGame, 1, 3);  // Invoca getUserInput con rango 1-3

        assertEquals(3, result, "Debe aceptar la entrada válida 3.");  // Validar que acepta entrada 3
    }

    // Nuevo test: startNewRound resetea bets, folded y cartas
    @Test
    public void testStartNewRoundResetsPlayersAndBets() throws Exception {  // Test para verificar que nueva ronda limpia estados
        Method startNewRound = PokerGame.class.getDeclaredMethod("startNewRound");  // Obtener método privado startNewRound()
        startNewRound.setAccessible(true);

        Field playersField = PokerGame.class.getDeclaredField("players");  // Obtener lista de jugadores
        playersField.setAccessible(true);
        List<Player> players = (List<Player>) playersField.get(pokerGame);

        // Manipulo un jugador para tener estado no inicial
        Player p = players.get(0);
        p.placeBet(100);  // Simular apuesta
        p.fold();         // Simular que se tiró fold

        startNewRound.invoke(pokerGame);  // Invocar método para iniciar nueva ronda

        assertEquals(0, p.getBet(), "El jugador debe tener la apuesta en 0 luego de nueva ronda");  // Validar apuesta reset
        assertFalse(p.hasFolded(), "El jugador no debe estar folded luego de nueva ronda");          // Validar folded reset
        assertEquals(0, p.getHand().size(), "La mano del jugador debe estar vacía luego de nueva ronda");  // Validar mano vacía
    }

    // Nuevo test: awardPotToWinner asigna correctamente el pot y resetea
    @Test
    public void testAwardPotToWinner() throws Exception {  // Test para validar que se entrega pot al ganador y se limpia
        Field betsField = PokerGame.class.getDeclaredField("pokerBets");  // Obtener campo privado pokerBets
        betsField.setAccessible(true);
        PokerBets bets = (PokerBets) betsField.get(pokerGame);  // Obtener instancia de PokerBets

        Field playersField = PokerGame.class.getDeclaredField("players");  // Obtener lista de jugadores
        playersField.setAccessible(true);
        List<Player> players = (List<Player>) playersField.get(pokerGame);

        Player winner = players.get(0);  // Elegir ganador (primer jugador)

        // Configurar pot en PokerBets
        Field potField = PokerBets.class.getDeclaredField("pot");  // Obtener campo privado pot
        potField.setAccessible(true);
        potField.set(bets, 500);  // Setear pot con valor 500 para simular situación

        int chipsBefore = winner.getChips();  // Guardar fichas antes de ganar

        Method awardPotToWinner = PokerGame.class.getDeclaredMethod("awardPotToWinner", Player.class);  // Método para asignar pot a ganador
        awardPotToWinner.setAccessible(true);
        awardPotToWinner.invoke(pokerGame, winner);  // Invocar método con ganador

        assertEquals(chipsBefore + 500, winner.getChips(), "El ganador debe recibir el pot");  // Validar que fichas aumentaron correctamente
        assertEquals(0, bets.getPot(), "El pot debe resetearse después de dar las fichas");    // Validar que el pot se reseteó a 0
    }

    // Nuevo test: checkAllBetsEqual funciona correctamente (suponiendo método privado)
    @Test
    public void testCheckAllBetsEqual() throws Exception {  // Test para verificar que método chequea si todas las apuestas son iguales
        Field playersField = PokerGame.class.getDeclaredField("players");  // Obtener lista de jugadores
        playersField.setAccessible(true);
        List<Player> players = (List<Player>) playersField.get(pokerGame);

        // Todos igualan 100
        for (Player p : players) {
            p.setBet(100);  // Asignar 100 en apuesta a cada jugador
        }

        Method checkAllBetsEqual = PokerGame.class.getDeclaredMethod("checkAllBetsEqual");  // Método privado que chequea igualdad de apuestas
        checkAllBetsEqual.setAccessible(true);
        boolean result = (boolean) checkAllBetsEqual.invoke(pokerGame);  // Invocar método

        assertTrue(result, "Las apuestas deberían ser iguales");  // Validar que retorna true cuando todas apuestas iguales

        // Cambiar uno
        players.get(0).setBet(50);  // Modificar apuesta de un jugador
        result = (boolean) checkAllBetsEqual.invoke(pokerGame);

        assertFalse(result, "Las apuestas no deberían ser iguales cuando un jugador apuesta distinto");  // Validar que retorna false si alguna difiere
    }

    // Si tienes métodos processHumanTurn y processBotTurn (privados)
    // los podés testear invocándolos y verificando comportamiento simple (ej: no crash, llamadas, flags)

    @Test
    public void testProcessHumanTurnDoesNotFoldWhenInputIsCall() throws Exception {  // Test que simula que humano elige

        Field playersField = PokerGame.class.getDeclaredField("players"); // Obtener jugadores
        playersField.setAccessible(true);
        List<Player> players = (List<Player>) playersField.get(pokerGame);
        Player human = players.get(0); // Tomar primer jugador (humano)

        Scanner mockScanner = new Scanner("1\n"); // Simular que usuario ingresa opción 'Call' (1)
        Field scannerField = PokerGame.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(pokerGame, mockScanner);  // Reemplazar scanner en juego con mock

        Method processHumanTurn = PokerGame.class.getDeclaredMethod("processHumanTurn", Player.class);  // Método privado que procesa turno humano
        processHumanTurn.setAccessible(true);
        processHumanTurn.invoke(pokerGame, human);  // Invocar método con jugador humano

        assertFalse(human.hasFolded(), "El jugador humano no debería hacer fold con opción call");  // Validar que no se tiró fold
    }

    @Test
    public void testProcessBotTurnDoesNotFold() throws Exception {  // Test que simula turno bot y valida que no hace fold
        Field playersField = PokerGame.class.getDeclaredField("players");
        playersField.setAccessible(true);
        Player bot = null;
        for (Player p : (List<Player>) playersField.get(pokerGame)) {  // Buscar un jugador que sea bot
            if (p.isBot()) {
                bot = p;
                break;
            }
        }
        if (bot == null) {
            fail("No hay bots en la lista de jugadores para testear");  // Falla si no hay bots
        }

        Method processBotTurn = PokerGame.class.getDeclaredMethod("processBotTurn", Player.class);  // Obtener método para turno bot
        processBotTurn.setAccessible(true);
        processBotTurn.invoke(pokerGame, bot);  // Ejecutar turno bot

        assertFalse(bot.hasFolded(), "El bot no debería estar folded tras su turno");  // Validar que bot no se tiró fold
    }
}