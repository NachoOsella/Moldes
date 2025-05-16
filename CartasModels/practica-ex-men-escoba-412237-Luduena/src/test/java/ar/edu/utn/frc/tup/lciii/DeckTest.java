package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

class DeckTest {

    private static Deck sut;
    private static List<Card> cards;

    @BeforeEach
    void setUp() {
        sut = new Deck();
        cards = new ArrayList<>();
        for(CardSuit suit : CardSuit.values()) {
            for (int i = 1; i <= 10; i++) {
                if(i < 8){
                    cards.add(new Card(suit, i, i));
                }else {
                    cards.add(new Card(suit, i+2, i));
                }
            }
        }
    }

    @Test
    void createDeckTest() {
        // TODO: Crear un test que valide que el mazo se crea con 40 cartas,
        //  que no se incluyen los 8 y 9.
        //  Validar que todas las cartas de un mazo de 40 cartas estén presentes.
        
        // Verifica si el método createDeck existe en la clase Deck usando reflexión
        if(ReflectionSupport.findMethod(Deck.class, "createDeck").isPresent()){
            // Invoca el método createDeck en la instancia 'sut' (el mazo que se está probando)
            ReflectionSupport.invokeMethod(ReflectionSupport.findMethod(Deck.class, "createDeck").get(), sut);
        }else{
            // Falla la prueba si el método createDeck no existe
            fail("No such method createDeck");
        }

        // Obtiene la lista de cartas actual del mazo
        List<Card> cardsSut = sut.getCards();

        // Recorre cada carta del mazo y verifica que no sean el número 8 ni 9
        for(Card card : cardsSut) {
            assertNotEquals(8, card.getNumber());
            assertNotEquals(9, card.getNumber());
        }

        // Verifica que el mazo tenga exactamente 40 cartas
        assertEquals(40, cardsSut.size());

        // Verifica que el mazo contenga todas las cartas esperadas (lista 'cards' debería tener la baraja completa)
        assertTrue(cardsSut.containsAll(cards));

    }

    @Test
    void takeCardTest() {
        // TODO: Crear un test que valide que al tomar una carta del mazo,
        //  la cantidad de cartas en el mazo disminuye en 1 y que la carta tomada
        //  es la que se esperaba; es decir la que está al tope de la pila.
        
        // Obtiene el tamaño del mazo antes de tomar la carta y resta 1 (lo que se espera luego de tomar una)
        int pedro = sut.getCards().size() - 1;

        // Obtiene la carta que está en la cima del mazo (sin removerla)
        Card topCard = sut.getCards().peek();

        // Toma (y remueve) la carta del tope del mazo
        Card cardTaken = sut.takeCard();

        // Verifica que el tamaño del mazo haya disminuido en 1
        assertEquals(pedro, sut.getCards().size());

        // Verifica que la carta tomada sea la misma que estaba en la cima antes
        assertEquals(topCard, cardTaken);
    }

    @Test
    void isEmptyTest() {
        // TODO: Crear un test que valide que el mazo está vacío cuando no tiene cartas
        //  y que no está vacío cuando tiene al menos una carta.
        
        // Limpia el mazo eliminando todas las cartas
        sut.getCards().clear();

        // Verifica que el mazo esté vacío después de limpiarlo
        assertTrue(sut.getCards().isEmpty());

        // Agrega una carta (7 de Oros) al mazo
        sut.getCards().add(new Card(CardSuit.OROS, 7, 7));

        // Verifica que el mazo ya no esté vacío después de agregar una carta
        assertFalse(sut.getCards().isEmpty());
    }

    @Test
    void shuffleDeckTest() {
        // TODO: Crear un test que valide que al mezclar el mazo, las cartas no están en el mismo orden
        //  que al crear el mazo.
        
        // Crea una copia inmutable del mazo original antes de mezclarlo
        List<Card> originalOrder = List.copyOf(sut.getCards());

        // Verifica si el método shuffleDeck existe en la clase Deck usando reflexión
        if (ReflectionSupport.findMethod(Deck.class, "shuffleDeck").isPresent()) {
            // Invoca el método shuffleDeck en la instancia 'sut' (Deck que se está probando)
            ReflectionSupport.invokeMethod(ReflectionSupport.findMethod(Deck.class, "shuffleDeck").get(), sut);
        } else {
            // Falla la prueba si el método shuffleDeck no está presente
            fail("No such method shuffleDeck");
        }

        // Verifica que después de mezclar, el orden de las cartas haya cambiado
        assertNotEquals(originalOrder, sut.getCards());

        // Verifica que, aunque el orden haya cambiado, todas las cartas originales aún están presentes
        assertTrue(sut.getCards().containsAll(originalOrder));
    }
}