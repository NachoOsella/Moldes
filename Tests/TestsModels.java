//✅ 2. Test de Distribución de Cartas
//Propósito: Verificar que los jugadores reciben la cantidad correcta de cartas.

@Test
void testDealCardsToPlayers() {
    when(deck.draw()).thenReturn(new Card("Spades", "A"));
    gameService.dealCards(players, 2);

    for (Player player : players) {
        assertEquals(2, player.getHand().size());
    }
    verify(deck, times(players.size() * 2)).draw();
}

//✅ 3. Test de Baraja Aleatoria (Mock + Estado Interno)
//Propósito: Verificar que se llama a shuffle() una vez en la creación del mazo.
@Test
void testDeckIsShuffledOnCreation() {
    Deck deck = spy(new Deck());
    verify(deck).shuffle(); // suponiendo que shuffle() es un método público o protegido
}

//✅ 4. Test de Reparto sin Repetición
//Propósito: Asegurarse de que las cartas repartidas no se repiten.
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

//✅ 5. Test de Evaluación de Manos (Mock de Evaluador)
//Propósito: Verificar que la mano ganadora se determina correctamente.
@Test
void testEvaluateHandsReturnsCorrectWinner() {
    when(handEvaluator.evaluate(any())).thenReturn(HandRank.ONE_PAIR, HandRank.FLUSH);

    Player winner = gameService.evaluateWinner(players);

    assertEquals("Bob", winner.getName()); // si Bob tiene FLUSH
}

//✅ 6. Test con @BeforeEach para Configuración Repetida
//Propósito: Centralizar lógica repetida.
@BeforeEach
void setUp() {
    players = List.of(new Player("Alice"), new Player("Bob"));
    deck = mock(Deck.class);
    gameService = new PokerGameService(deck);
}

//✅ 7. Test con @InjectMocks y @Mock (Mockito)
//Propósito: Inyectar dependencias y simular comportamiento.
@ExtendWith(MockitoExtension.class)
class PokerServiceTest {

    @Mock
    private Deck deck;

    @InjectMocks
    private PokerGameService gameService;

    @Test
    void testDealCallsDeckDraw() {
        when(deck.draw()).thenReturn(new Card("Clubs", "K"));
        gameService.dealCards(players, 2);
        verify(deck, times(4)).draw();
    }
}

//✅ 8. Test con Reflection (acceso a campos privados)
//Propósito: Verificar valores internos sin getter.

@Test
void testPrivateFieldUsingReflection() throws Exception {
    Player player = new Player("Test");

    Field nameField = Player.class.getDeclaredField("name");
    nameField.setAccessible(true);

    String name = (String) nameField.get(player);
    assertEquals("Test", name);
}

//✅ 9. Test de Excepción Esperada
//Propósito: Asegurar que no se puedan repartir más cartas de las que hay.
@Test
void testDealMoreCardsThanInDeckThrowsException() {
    when(deck.draw()).thenThrow(new IllegalStateException("No more cards"));

    assertThrows(IllegalStateException.class, () -> {
        gameService.dealCards(players, 30);
    });
}

//✅ 10. Test de Comportamiento de Apuesta
//Propósito: Verificar que el jugador puede apostar correctamente.

@Test
void testPlayerBetReducesChips() {
    Player player = new Player("Alice", 1000);
    player.bet(200);
    assertEquals(800, player.getChips());
}

//✅ 11. Test de Fold
//Propósito: Asegurar que un jugador puede retirarse.
@Test
void testPlayerCanFold() {
    Player player = new Player("Bob");
    player.fold();
    assertTrue(player.hasFolded());
}

//✅ 12. Test de Lógica de Rondas
//Propósito: Validar flujo de ronda (pre-flop, flop, turn, river).
@Test
void testGameProgressesThroughPhases() {
    gameService.startGame(players);
    assertEquals(GamePhase.PRE_FLOP, gameService.getCurrentPhase());

    gameService.advancePhase();
    assertEquals(GamePhase.FLOP, gameService.getCurrentPhase());
}

//✅ 13. Test de Comparación de Manos Empatadas
//Propósito: Verificar empate.
@Test
void testTiedHandsResultInDraw() {
    when(handEvaluator.evaluate(players.get(0))).thenReturn(HandRank.ONE_PAIR);
    when(handEvaluator.evaluate(players.get(1))).thenReturn(HandRank.ONE_PAIR);

    List<Player> winners = gameService.getWinners(players);

    assertEquals(2, winners.size());
}

// 🔧 Se ejecuta antes de cada test
@BeforeEach
void setUp() {
    player = new Player("Alice", 1000); // nombre y fichas iniciales
}

// ✅ Test: estado inicial del jugador
@Test
void testInitialChips() {
    assertEquals(1000, player.getChips());
    assertEquals("Alice", player.getName());
    assertTrue(player.getHand().isEmpty());
    assertFalse(player.hasFolded());
}

// ✅ Test: agregar una carta a la mano
@Test
void testAddCardToHand() {
    Card card = new Card("Hearts", "A");
    player.addCard(card);

    List<Card> hand = player.getHand();
    assertEquals(1, hand.size());
    assertEquals("A", hand.get(0).getValue());
    assertEquals("Hearts", hand.get(0).getSuit());
}

// ✅ Test: hacer una apuesta válida
@Test
void testBetReducesChips() {
    player.bet(300);
    assertEquals(700, player.getChips());
}

// ✅ Test: apuesta mayor a las fichas → excepción
@Test
void testBetMoreThanChipsThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> player.bet(1200));
}

// ✅ Test: fold cambia estado interno
@Test
void testPlayerCanFold() {
    player.fold();
    assertTrue(player.hasFolded());
}

// 🛠️ Test opcional: usar reflexión para inspeccionar el campo privado 'name'
@Test
void testPlayerNameWithReflection() throws Exception {
    var nameField = Player.class.getDeclaredField("name");
    nameField.setAccessible(true);
    String name = (String) nameField.get(player);

    assertEquals("Alice", name);
}

@BeforeEach
void setUp() {
    deck = new Deck(); // instancia real, sin mocks
}

// ✅ Test: mazo tiene 52 cartas únicas al iniciar
@Test
void testDeckHas52UniqueCardsOnCreation() {
    Set<Card> cards = new HashSet<>();

    for (int i = 0; i < 52; i++) {
        Card card = deck.draw();
        assertNotNull(card);
        assertTrue(cards.add(card), "Carta duplicada encontrada: " + card);
    }

    assertEquals(52, cards.size());
}

// ✅ Test: después de 52 draw() el mazo está vacío
@Test
void testDeckIsEmptyAfter52Draws() {
    for (int i = 0; i < 52; i++) {
        deck.draw();
    }

    assertThrows(IllegalStateException.class, deck::draw);
}

// ✅ Test: shuffle aleatoriza el orden (muy básico, no determinista)
@Test
void testShuffleChangesOrder() {
    Deck unshuffled = new Deck() {
        @Override
        public void shuffle() {
            // no baraja para este test específico
        }
    };

    Deck shuffled = new Deck(); // este sí baraja

    boolean sameOrder = true;
    for (int i = 0; i < 5; i++) {
        Card c1 = unshuffled.draw();
        Card c2 = shuffled.draw();
        if (!c1.equals(c2)) {
            sameOrder = false;
            break;
        }
    }

    assertFalse(sameOrder, "El mazo barajado tiene el mismo orden que el no barajado");
}



// ✅ Test: mano con un par
@Test
void testEvaluateOnePair() {
    List<Card> hand = List.of(
        new Card("Hearts", "5"),
        new Card("Spades", "5"),
        new Card("Diamonds", "2"),
        new Card("Clubs", "8"),
        new Card("Hearts", "J")
    );

    HandRank result = evaluator.evaluate(hand);
    assertEquals(HandRank.ONE_PAIR, result);
}

// ✅ Test: escalera
@Test
void testEvaluateStraight() {
    List<Card> hand = List.of(
        new Card("Hearts", "6"),
        new Card("Spades", "7"),
        new Card("Diamonds", "8"),
        new Card("Clubs", "9"),
        new Card("Hearts", "10")
    );

    HandRank result = evaluator.evaluate(hand);
    assertEquals(HandRank.STRAIGHT, result);
}

// ✅ Test: full house
@Test
void testEvaluateFullHouse() {
    List<Card> hand = List.of(
        new Card("Hearts", "K"),
        new Card("Spades", "K"),
        new Card("Clubs", "K"),
        new Card("Hearts", "9"),
        new Card("Diamonds", "9")
    );

    HandRank result = evaluator.evaluate(hand);
    assertEquals(HandRank.FULL_HOUSE, result);
}

// ✅ Test: comparar manos (desempate)
@Test
void testCompareHands() {
    List<Card> hand1 = List.of(
        new Card("Hearts", "10"),
        new Card("Hearts", "J"),
        new Card("Hearts", "Q"),
        new Card("Hearts", "K"),
        new Card("Hearts", "A")
    );

    List<Card> hand2 = List.of(
        new Card("Spades", "5"),
        new Card("Spades", "6"),
        new Card("Spades", "7"),
        new Card("Spades", "8"),
        new Card("Spades", "9")
    );

    Player p1 = new Player("Alice");
    Player p2 = new Player("Bob");

    p1.setHand(hand1);
    p2.setHand(hand2);

    Player winner = evaluator.compare(p1, p2);
    assertEquals("Alice", winner.getName());
}



