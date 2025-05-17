package ar.edu.utn.frc.tup.lciii;

import java.util.ArrayList;
import java.util.List;

public class Table {

    // Lista que representa las cartas comunitarias (cartas sobre la mesa)
    private final List<Card> tableCards = new ArrayList<>();

    // Baraja de cartas que usará la mesa para repartir
    private final Deck deck = new Deck();

    // Constructor: se ejecuta al crear una nueva mesa, baraja el mazo
    public Table() {
        deck.shuffleCards(); // Se mezcla el mazo apenas se crea la mesa
    }

    // Reinicia el mazo: lo vuelve a crear, mezcla las cartas y limpia la mesa
    public void resetDeck() {
        deck.initializeDeck(); // Crea un nuevo mazo con todas las cartas
        deck.shuffleCards();   // Mezcla el mazo
        tableCards.clear();    // Limpia las cartas comunitarias de la mesa
    }

    // Reparte 2 cartas a cada jugador para iniciar una mano
    public void dealHands(List<Player> players) {
        for (Player player : players) {
            player.resetForNewRound(); // Limpia las cartas y estado anterior del jugador
            player.addCard(deck.takeCard()); // Le da la primera carta del mazo
            player.addCard(deck.takeCard()); // Le da la segunda carta del mazo
        }
    }

    // Reparte el flop: quema una carta y luego reparte 3 cartas comunitarias
    public void dealFlop() {
        burnCard(); // Se "quema" una carta del mazo (se descarta sin mostrar)
        for (int i = 0; i < 3; i++) {
            tableCards.add(deck.takeCard()); // Se reparten 3 cartas a la mesa
        }
    }

    // Reparte el turn: quema una carta y agrega una más a la mesa
    public void dealTurn() {
        burnCard(); // Se quema una carta
        tableCards.add(deck.takeCard()); // Se agrega una carta más a la mesa
    }

    // Reparte el river: igual que el turn
    public void dealRiver() {
        burnCard(); // Se quema una carta
        tableCards.add(deck.takeCard()); // Se reparte la última carta comunitaria
    }

    // Método privado que quema una carta (solo si el mazo no está vacío)
    private void burnCard() {
        if (!deck.isEmpty()) {
            deck.takeCard(); // Se descarta la carta superior del mazo
        }
    }

    // Devuelve una copia de las cartas comunitarias (para evitar que se modifiquen desde afuera)
    public List<Card> getTableCards() {
        return new ArrayList<>(tableCards); // Retorna copia defensiva
    }

    // Alias del método anterior, mismo comportamiento pero más semántico
    public List<Card> getCommunityCards() {
        return new ArrayList<>(tableCards); // Retorna copia defensiva
    }
}
