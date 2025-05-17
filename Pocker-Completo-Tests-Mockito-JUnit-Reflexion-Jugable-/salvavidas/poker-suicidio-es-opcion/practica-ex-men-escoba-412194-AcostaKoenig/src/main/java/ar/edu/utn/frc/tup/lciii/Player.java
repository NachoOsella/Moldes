package ar.edu.utn.frc.tup.lciii;

import java.util.ArrayList;
import java.util.List;

public class Player {
    // Nombre del jugador
    private final String name;
    // Indica si el jugador es un bot o humano
    private final boolean isBot;
    // Cartas en mano del jugador (máximo 2)
    private final List<Card> hand;
    // Cantidad de fichas disponibles
    private int chips;
    // Apuesta actual del jugador en la ronda
    private int currentBet;
    // Estado de si el jugador se ha retirado (fold)
    private boolean folded;
    // Indica si el jugador es el dealer en la ronda
    private boolean isDealer;

    // Constructor: crea un jugador con nombre, tipo (bot/humano) y fichas iniciales
    public Player(String name, boolean isBot, int initialChips) {
        this.name = name;
        this.isBot = isBot;
        this.hand = new ArrayList<>();
        this.chips = initialChips;
        this.currentBet = 0;
        this.folded = false;
        this.isDealer = false;
    }

    // Devuelve el nombre del jugador
    public String getName() {
        return name;
    }

    // Devuelve la lista de cartas en mano
    public List<Card> getHand() {
        return hand;
    }

    // Añade una carta a la mano (máximo 2 cartas)
    public void addCard(Card card) {
        if (hand.size() < 2) {
            hand.add(card);
        }
    }

    // Limpia la mano del jugador (para nueva ronda)
    public void clearHand() {
        hand.clear();
    }

    // Devuelve la cantidad de fichas disponibles
    public int getChips() {
        return chips;
    }

    // Realiza una apuesta, descontando fichas y sumando a la apuesta actual
    public void placeBet(int amount) {
        if (amount > chips) {
            amount = chips; // Si apuesta más de lo que tiene, se ajusta a su cantidad
        }
        chips -= amount;
        currentBet += amount;
    }

    // Devuelve la apuesta actual del jugador
    public int getBet() {
        return currentBet;
    }

    // Establece la apuesta actual (usado para reiniciar o ajustar)
    public void setBet(int amount) {
        currentBet = amount;
    }

    // Indica si el jugador se ha retirado (fold)
    public boolean hasFolded() {
        return folded;
    }

    // Marca al jugador como retirado
    public void fold() {
        folded = true;
    }

    // Reinicia estado y apuesta para una nueva ronda, y limpia la mano
    public void resetForNewRound() {
        currentBet = 0;
        folded = false;
        clearHand();
    }

    // Suma fichas ganadas al total del jugador
    public void winChips(int amount) {
        chips += amount;
    }

    // Indica si el jugador es bot o humano
    public boolean isBot() {
        return isBot;
    }

    // Establece la cantidad de fichas (ej. para asignar ganancias)
    public void setChips(int i) {
        chips = i;
    }

    // Define si el jugador es dealer
    public void setDealer(boolean isDealer) {
        this.isDealer = isDealer;
    }

    // Devuelve si el jugador es dealer
    public boolean isDealer() {
        return isDealer;
    }
}
