package ar.edu.utn.frc.tup.lciii;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    // Lista que contiene las cartas del mazo
    private List<Card> cards;

    // Constructor: crea un mazo vacío y lo inicializa con todas las cartas
    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
    }

    // Inicializa el mazo con 52 cartas, valores del 2 al 14 (As = 14), para cada palo
    public void initializeDeck() {
        cards.clear(); // Vacía el mazo actual
        for (CardSuits suit : CardSuits.values()) { // Itera por cada palo
            for (int i = 2; i <= 14; i++) { // Itera por valores 2 a 14
                cards.add(new Card(i, suit)); // Añade cada carta al mazo
            }
        }
    }

    // Devuelve una copia de las cartas para evitar que se modifique la lista interna
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    // Reemplaza las cartas del mazo por una copia de la lista dada
    public void setCards(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    // Mezcla aleatoriamente las cartas del mazo
    public void shuffleCards() {
        Collections.shuffle(cards);
    }

    // Devuelve la cantidad de cartas disponibles en el mazo
    public int availableCards() {
        return cards.size();
    }

    // Toma la primera carta del mazo y la elimina; devuelve null si el mazo está vacío
    public Card takeCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    // Verifica si el mazo está vacío
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    // Devuelve cartas al mazo (por ejemplo, para devolver cartas usadas)
    public void returnCards(List<Card> returnedCards) {
        cards.addAll(returnedCards);
    }
}
