package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck sut;  // System Under Test: instancia de Deck para las pruebas
    private List<Card> deck; // Lista para manipular el mazo en las pruebas

    // Se ejecuta antes de cada test para inicializar las variables de prueba
    @BeforeEach
    void setup() {
        sut = new Deck();   // Crear un nuevo mazo antes de cada test
        deck = new ArrayList<>();  // Crear una lista vacía

        sut.setCards(deck); // Setear el mazo interno con la lista vacía para pruebas específicas
    }

    // Verifica que al inicializar el mazo se creen las 52 cartas (4 palos * 13 valores)
    @Test
    void testInitializeDeck() {
        Deck deck = new Deck();
        assertEquals(52, deck.availableCards());
    }

    // Verifica que getCards devuelve una copia y no la referencia interna para proteger el mazo
    @Test
    void testGetCardsDefensiveCopy() {
        Deck deck = new Deck();
        List<Card> cards = deck.getCards();
        cards.clear(); // Borra la copia
        // El mazo interno no debe haberse afectado
        assertEquals(52, deck.availableCards());
    }

    // Verifica que setCards también haga una copia para no modificar la lista original externa
    @Test
    void testSetCardsDefensiveCopy() {
        Deck deck = new Deck();
        List<Card> newCards = new ArrayList<>();
        newCards.add(new Card(10, CardSuits.HEART));
        deck.setCards(newCards);
        newCards.clear(); // Borra la lista externa
        // El mazo interno no debe haberse vaciado
        assertEquals(1, deck.availableCards());
    }

    // Verifica que mezclar las cartas realmente cambia el orden (con la pequeña chance de fallar por azar)
    @Test
    void testShuffleChangesOrder() {
        Deck deck = new Deck();
        List<Card> originalOrder = deck.getCards(); // Guardar el orden original
        deck.shuffleCards();                         // Mezclar cartas
        List<Card> shuffledOrder = deck.getCards(); // Obtener nuevo orden
        assertNotEquals(originalOrder, shuffledOrder);
    }

    // Verifica que takeCard reduce la cantidad de cartas y devuelve una carta no nula
    @Test
    void testTakeCard() {
        Deck deck = new Deck();
        int before = deck.availableCards();
        Card card = deck.takeCard();
        assertNotNull(card);
        assertEquals(before - 1, deck.availableCards());
    }

    // Verifica que si el mazo está vacío, takeCard devuelve null
    @Test
    void testTakeCardFromEmptyDeck() {
        Deck deck = new Deck();
        // Vaciar el mazo completamente
        while (!deck.isEmpty()) {
            deck.takeCard();
        }
        assertTrue(deck.isEmpty());
        assertNull(deck.takeCard());
    }

    // Verifica que isEmpty devuelva falso cuando hay cartas y verdadero cuando no queda ninguna
    @Test
    void testIsEmpty() {
        Deck deck = new Deck();
        assertFalse(deck.isEmpty()); // Al principio no está vacío
        // Vaciar el mazo
        for (int i = 0; i < 52; i++) {
            deck.takeCard();
        }
        assertTrue(deck.isEmpty());  // Ahora sí está vacío
    }

    // Verifica que returnCards agrega cartas al mazo aumentando su tamaño
    @Test
    void testReturnCards() {
        Deck deck = new Deck();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(10, CardSuits.HEART));
        cards.add(new Card(11, CardSuits.CLUB));

        int before = deck.availableCards();
        deck.returnCards(cards); // Devuelve cartas al mazo
        assertEquals(before + 2, deck.availableCards());
    }
}
