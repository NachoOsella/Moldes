//✅ 2. Test de Distribución de Cartas
@Test
void testDealCardsToPlayers() {
    // Simulamos que el deck siempre devuelve el mismo As de Espadas
    when(deck.draw()).thenReturn(new Card("Spades", "A"));

    // Repartimos 2 cartas a cada jugador
    gameService.dealCards(players, 2);

    // Verificamos que cada jugador tenga 2 cartas
    for (Player player : players) {
        assertEquals(2, player.getHand().size());
    }

    // Verificamos que se haya llamado al método draw() el número correcto de veces
    verify(deck, times(players.size() * 2)).draw();
}

//✅ 3. Test de Baraja Aleatoria
@Test
void testDeckIsShuffledOnCreation() {
    // Espiamos un objeto real de tipo Deck
    Deck deck = spy(new Deck());

    // Verificamos que se llamó a shuffle() al crear el mazo
    verify(deck).shuffle(); // shuffle() debe ser público o protegido
}

//✅ 4. Test de Reparto sin Repetición
@Test
void testDealtCardsAreUnique() {
    Set<Card> dealtCards = new HashSet<>();

    // Mock para devolver cartas únicas usando UUID como valor
    when(deck.draw()).thenAnswer(invocation -> {
        Card card = new Card("Hearts", UUID.randomUUID().toString());
        dealtCards.add(card);
        return card;
    });

    // Repartimos 2 cartas a cada jugador
    gameService.dealCards(players, 2);

    // Comprobamos que todas las cartas sean únicas
    assertEquals(players.size() * 2, dealtCards.size());
}

//✅ 5. Evaluación de Manos
@Test
void testEvaluateHandsReturnsCorrectWinner() {
    // Simulamos que el primer jugador tiene un par, el segundo un flush
    when(handEvaluator.evaluate(any()))
        .thenReturn(HandRank.ONE_PAIR, HandRank.FLUSH);

    // Evaluamos el ganador
    Player winner = gameService.evaluateWinner(players);

    // Verificamos que Bob (con flush) sea el ganador
    assertEquals("Bob", winner.getName());
}

//✅ 6. Setup Repetido
@BeforeEach
void setUp() {
    // Creamos lista de jugadores
    players = List.of(new Player("Alice"), new Player("Bob"));

    // Mockeamos el deck
    deck = mock(Deck.class);

    // Inyectamos dependencias en el servicio
    gameService = new PokerGameService(deck);
}

//✅ 7. Uso de @InjectMocks y @Mock
@ExtendWith(MockitoExtension.class)
class PokerServiceTest {

    @Mock
    private Deck deck;

    @InjectMocks
    private PokerGameService gameService;

    @Test
    void testDealCallsDeckDraw() {
        // Simulamos que el deck siempre devuelve una carta válida
        when(deck.draw()).thenReturn(new Card("Clubs", "K"));

        // Repartimos cartas
        gameService.dealCards(players, 2);

        // Verificamos que se llamaron 4 veces a draw() (2 cartas x 2 jugadores)
        verify(deck, times(4)).draw();
    }
}

//✅ 8. Test con Reflection
@Test
void testPrivateFieldUsingReflection() throws Exception {
    Player player = new Player("Test");

    // Accedemos al campo privado 'name'
    Field nameField = Player.class.getDeclaredField("name");
    nameField.setAccessible(true);

    // Obtenemos el valor y lo comparamos
    String name = (String) nameField.get(player);
    assertEquals("Test", name);
}

//✅ 9. Excepción Esperada
@Test
void testDealMoreCardsThanInDeckThrowsException() {
    // Simulamos que el deck lanza una excepción al intentar extraer una carta extra
    when(deck.draw()).thenThrow(new IllegalStateException("No more cards"));

    // Verificamos que se lanza la excepción al intentar repartir demasiadas cartas
    assertThrows(IllegalStateException.class, () -> {
        gameService.dealCards(players, 30);
    });
}

//✅ 10. Comportamiento de Apuesta
@Test
void testPlayerBetReducesChips() {
    Player player = new Player("Alice", 1000);

    // Realiza una apuesta de 200
    player.bet(200);

    // Verificamos que se descontaron correctamente
    assertEquals(800, player.getChips());
}

//✅ 11. Fold
@Test
void testPlayerCanFold() {
    Player player = new Player("Bob");

    // Se retira de la ronda
    player.fold();

    // Confirmamos el estado
    assertTrue(player.hasFolded());
}

//✅ 12. Fases del Juego
@Test
void testGameProgressesThroughPhases() {
    gameService.startGame(players);

    // Comprobamos que comienza en pre-flop
    assertEquals(GamePhase.PRE_FLOP, gameService.getCurrentPhase());

    // Avanzamos una fase
    gameService.advancePhase();

    // Debe estar en flop
    assertEquals(GamePhase.FLOP, gameService.getCurrentPhase());
}

//✅ 13. Empate en Manos
@Test
void testTiedHandsResultInDraw() {
    // Simulamos que ambos jugadores tienen el mismo valor de mano
    when(handEvaluator.evaluate(players.get(0))).thenReturn(HandRank.ONE_PAIR);
    when(handEvaluator.evaluate(players.get(1))).thenReturn(HandRank.ONE_PAIR);

    // Obtenemos la lista de ganadores
    List<Player> winners = gameService.getWinners(players);

    // Verificamos que hay empate
    assertEquals(2, winners.size());
}

//✅ 1. PokerGameServiceTest.java
@ExtendWith(MockitoExtension.class)
class PokerGameServiceTest {

    @Mock
    private Deck deck;

    @Mock
    private HandEvaluator handEvaluator;

    @InjectMocks
    private PokerGameService gameService;

    private List<Player> players;

    @BeforeEach
    void setUp() {
        players = List.of(new Player("Alice"), new Player("Bob"));
    }

    @Test
    void testDealCardsToPlayers() {
        when(deck.draw()).thenReturn(new Card("Spades", "A"));

        gameService.dealCards(players, 2);

        for (Player player : players) {
            assertEquals(2, player.getHand().size());
        }

        verify(deck, times(players.size() * 2)).draw();
    }

    @Test
    void testDeckIsShuffledOnCreation() {
        Deck deckSpy = spy(new Deck());

        verify(deckSpy).shuffle();
    }

    @Test
    void testDealtCardsAreUnique() {
        Set<Card> dealtCards = new HashSet<>();

        when(deck.draw()).thenAnswer(invocation -> {
            Card card = new Card("Hearts", UUID.randomUUID().toString());
            dealtCards.add(card);
            return card;
        });

        gameService.dealCards(players, 2);

        assertEquals(players.size() * 2, dealtCards.size());
    }

    @Test
    void testEvaluateHandsReturnsCorrectWinner() {
        when(handEvaluator.evaluate(any()))
            .thenReturn(HandRank.ONE_PAIR, HandRank.FLUSH);

        Player winner = gameService.evaluateWinner(players);

        assertEquals("Bob", winner.getName());
    }

    @Test
    void testDealMoreCardsThanInDeckThrowsException() {
        when(deck.draw()).thenThrow(new IllegalStateException("No more cards"));

        assertThrows(IllegalStateException.class, () -> {
            gameService.dealCards(players, 30);
        });
    }

    @Test
    void testGameProgressesThroughPhases() {
        gameService.startGame(players);
        assertEquals(GamePhase.PRE_FLOP, gameService.getCurrentPhase());

        gameService.advancePhase();
        assertEquals(GamePhase.FLOP, gameService.getCurrentPhase());
    }

    @Test
    void testTiedHandsResultInDraw() {
        when(handEvaluator.evaluate(players.get(0))).thenReturn(HandRank.ONE_PAIR);
        when(handEvaluator.evaluate(players.get(1))).thenReturn(HandRank.ONE_PAIR);

        List<Player> winners = gameService.getWinners(players);

        assertEquals(2, winners.size());
    }

    @Test
    void testPrivateFieldUsingReflection() throws Exception {
        Player player = new Player("Test");

        Field nameField = Player.class.getDeclaredField("name");
        nameField.setAccessible(true);
        String name = (String) nameField.get(player);

        assertEquals("Test", name);
    }
}

//✅ 2. PotManagerTest.java
@ExtendWith(MockitoExtension.class)
class PotManagerTest {

    private PotManager potManager;

    @BeforeEach
    void setUp() {
        potManager = new PotManager();
    }

    @Test
    void testAddToPotIncreasesTotal() {
        potManager.addToPot(100);
        potManager.addToPot(50);

        assertEquals(150, potManager.getTotalPot());
    }

    @Test
    void testResetPotToZero() {
        potManager.addToPot(300);
        potManager.resetPot();

        assertEquals(0, potManager.getTotalPot());
    }

    @Test
    void testDistributePotToWinner() {
        Player winner = new Player("Alice", 1000);
        potManager.addToPot(500);

        potManager.distributePot(List.of(winner));

        assertEquals(1500, winner.getChips());
        assertEquals(0, potManager.getTotalPot());
    }

    @Test
    void testDistributePotAmongMultipleWinners() {
        Player alice = new Player("Alice", 500);
        Player bob = new Player("Bob", 1000);
        potManager.addToPot(600);

        potManager.distributePot(List.of(alice, bob));

        assertEquals(500 + 300, alice.getChips());
        assertEquals(1000 + 300, bob.getChips());
    }
}

//✅ 3. RoundManagerTest.java
@ExtendWith(MockitoExtension.class)
class RoundManagerTest {

    private RoundManager roundManager;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        players.add(new Player("Alice"));
        players.add(new Player("Bob"));
        players.add(new Player("Charlie"));

        roundManager = new RoundManager(players);
    }

    @Test
    void testGetNextPlayerCyclesCorrectly() {
        Player first = roundManager.getNextPlayer();
        Player second = roundManager.getNextPlayer();
        Player third = roundManager.getNextPlayer();
        Player againFirst = roundManager.getNextPlayer();

        assertEquals("Alice", first.getName());
        assertEquals("Bob", second.getName());
        assertEquals("Charlie", third.getName());
        assertEquals("Alice", againFirst.getName());
    }

    @Test
    void testGetNextPlayerSkipsFolded() {
        players.get(1).fold(); // Bob se retira

        Player p1 = roundManager.getNextPlayer(); // Alice
        Player p2 = roundManager.getNextPlayer(); // Charlie

        assertEquals("Alice", p1.getName());
        assertEquals("Charlie", p2.getName());
    }

    @Test
    void testResetRoundResetsPlayerActions() {
        Player player = players.get(0);
        player.fold();

        roundManager.resetRound();

        assertFalse(player.hasFolded());
    }
}

//✅ 4. ActionValidatorTest.java
class ActionValidatorTest {

    private ActionValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ActionValidator();
    }

    @Test
    void testValidBet() {
        Player player = new Player("Alice", 1000);
        boolean isValid = validator.canBet(player, 200);

        assertTrue(isValid);
    }

    @Test
    void testBetMoreThanChipsIsInvalid() {
        Player player = new Player("Bob", 100);
        boolean isValid = validator.canBet(player, 200);

        assertFalse(isValid);
    }

    @Test
    void testCannotActIfFolded() {
        Player player = new Player("Charlie");
        player.fold();

        assertFalse(validator.canAct(player));
    }

    @Test
    void testCanActIfNotFolded() {
        Player player = new Player("Alice");

        assertTrue(validator.canAct(player));
    }
}

//✅ 5. HandEvaluatorTest.java
class HandEvaluatorTest {

    private HandEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new HandEvaluator();
    }

    @Test
    void testPairIsIdentifiedCorrectly() {
        List<Card> hand = List.of(
            new Card("Hearts", "7"),
            new Card("Spades", "7"),
            new Card("Diamonds", "2"),
            new Card("Clubs", "9"),
            new Card("Hearts", "K")
        );

        HandRank rank = evaluator.evaluate(hand);

        assertEquals(HandRank.ONE_PAIR, rank);
    }

    @Test
    void testFlushIsIdentifiedCorrectly() {
        List<Card> hand = List.of(
            new Card("Hearts", "2"),
            new Card("Hearts", "4"),
            new Card("Hearts", "6"),
            new Card("Hearts", "9"),
            new Card("Hearts", "Q")
        );

        HandRank rank = evaluator.evaluate(hand);

        assertEquals(HandRank.FLUSH, rank);
    }

    @Test
    void testHighCardIsDefault() {
        List<Card> hand = List.of(
            new Card("Spades", "2"),
            new Card("Hearts", "4"),
            new Card("Clubs", "6"),
            new Card("Diamonds", "9"),
            new Card("Hearts", "Q")
        );

        HandRank rank = evaluator.evaluate(hand);

        assertEquals(HandRank.HIGH_CARD, rank);
    }
}
