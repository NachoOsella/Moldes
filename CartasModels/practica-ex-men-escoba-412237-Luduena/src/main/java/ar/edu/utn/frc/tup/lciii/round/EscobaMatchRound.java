package ar.edu.utn.frc.tup.lciii.round;

import ar.edu.utn.frc.tup.lciii.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa cada una de las rondas que se juegan durante una parida.
 * En una partida se jugarán tantas rondas como sean necesarias hasta
 * que haya un ganador de la partida.
 *
 */
public class EscobaMatchRound {

    /**
     * Mazo de cartas de usado en la ronda.
     */
    private Deck deck;

    /**
     * Jugador de la ronda que representa al usuario.
     * RoundPlayer es una clase distinta de player, que no representa al usuario en sí,
     * sino al usuario en una ronda puntual
     */
    private RoundPlayer roundPlayerHuman;

    /**
     * Jugador de la ronda que representa a la app.
     * RoundPlayer es una clase distinta de player, que no representa al usuario en sí,
     * sino al usuario en una ronda puntual
     */
    private RoundPlayer roundPlayerApp;

    /**
     * Lista de cartas en la mesa durante la ronda.
     */
    private List<Card> tableCards;

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public RoundPlayer getRoundPlayerHuman() {
        return roundPlayerHuman;
    }

    public void setRoundPlayerHuman(RoundPlayer roundPlayerHuman) {
        this.roundPlayerHuman = roundPlayerHuman;
    }

    public RoundPlayer getRoundPlayerApp() {
        return roundPlayerApp;
    }

    public void setRoundPlayerApp(RoundPlayer roundPlayerApp) {
        this.roundPlayerApp = roundPlayerApp;
    }

    public List<Card> getTableCards() {
        return tableCards;
    }

    public void setTableCards(List<Card> tableCards) {
        this.tableCards = tableCards;
    }

    public EscobaMatchRound(User player, User app) {
        this.deck = new Deck();
        this.roundPlayerHuman = new HumanRoundPlayer(player);
        this.roundPlayerApp = new AppRoundPlayer(app);
        this.tableCards = new ArrayList<>();
    }

    /**
     * Este método inicia la ronda repartiendo 3 cartas a cada jugador y 4 cartas a la mesa.
     * Las cartas se reparten de manera intercalada entre el jugador y la app. Empezando a repartir
     * por el jugador que NO es dealer.
     *
     * @param isPlayerHumanDealer true si el jugador humano es el repartidor, false si la app es el repartidor
     *
     * @see this#dealCards(boolean)
     * @see Deck#takeCard()
     */
    public void startRound(Boolean isPlayerHumanDealer) {
        dealCards(isPlayerHumanDealer);
        for(int i = 0; i < 4; i++) {
            tableCards.add(deck.takeCard());
        }
        checkEscobaEnMesa(isPlayerHumanDealer);
    }

    /**
     * Este método verifica si la suma de los números de las cartas en la mesa es igual a 15.
     * Si la suma es 15, el jugador que repartió las cartas se lleva las cartas de la mesa y suma una escoba.
     *
     * @param isPlayerHumanDealer true si el jugador humano es el repartidor, false si la app es el repartidor
     */
    private void checkEscobaEnMesa(Boolean isPlayerHumanDealer) {
        Integer sum = 0;
        for(Card card : tableCards) {
            sum += card.getNumber();
        }
        if(sum == 15) {
            LetterByLetterPrinter.println(System.lineSeparator() + "Escoba en la mesa! The player who deals the cards takes them.");
            if(isPlayerHumanDealer) {
                roundPlayerHuman.setEscobasQuantity(roundPlayerHuman.getEscobasQuantity() + 1);
                roundPlayerHuman.getPersonalDeck().addAll(tableCards);
            } else {
                roundPlayerApp.setEscobasQuantity(roundPlayerApp.getEscobasQuantity() + 1);
                roundPlayerApp.getPersonalDeck().addAll(tableCards);
            }
            tableCards.clear();
        }
    }

    /**
     * Este método reparte 3 cartas a cada jugador.
     * Las cartas se reparten de manera intercalada entre el jugador y la app. Empezando a repartir
     * por el jugador que NO es dealer.
     *
     * @param isPlayerHumanDealer true si el jugador humano es el repartidor, false si la app es el repartidor
     *
     * @see RoundPlayer#getHandCards()
     * @see Deck#takeCard()
     */
    public void dealCards(boolean isPlayerHumanDealer) {
        LetterByLetterPrinter.println(System.lineSeparator() + "============================================");
        LetterByLetterPrinter.println("Dealing cards...");
        // TODO: Implementar lógica para repartir cartas intercaladamente una para cada jugador hasta repartir 3 a cada uno
        // Verifica si el jugador humano es el que reparte (dealer)
        if(isPlayerHumanDealer){
            // Si es así, reparten primero las cartas al jugador de la app y luego al jugador humano
            for (int i = 0; i < 3 ; i++){
                // Añade una carta del mazo a la mano del jugador de la app
                roundPlayerApp.getHandCards().add(deck.takeCard());
                // Añade una carta del mazo a la mano del jugador humano
                roundPlayerHuman.getHandCards().add(deck.takeCard());
            }
        // Si el jugador humano no es el dealer
        }else{
            // Reparte primero las cartas al jugador humano y luego al jugador de la app
            for (int i = 0; i < 3 ; i++){
                // Añade una carta del mazo a la mano del jugador humano
                roundPlayerHuman.getHandCards().add(deck.takeCard());
                // Añade una carta del mazo a la mano del jugador de la app
                roundPlayerApp.getHandCards().add(deck.takeCard());
            }
        }

        LetterByLetterPrinter.println("============================================" + System.lineSeparator());
    }

    /**
     *
     *
     * @param isPlayerDealer
     */
    public void playRound(boolean isPlayerDealer) {
        boolean playerTurn = !isPlayerDealer;
        do {
            do {
                if (playerTurn) {
                    roundPlayerHuman.playTurn(tableCards);
                } else {
                    roundPlayerApp.playTurn(tableCards);
                }
                playerTurn = !playerTurn;
            } while (!isHandFinish());
            if (!isRoundFinish()) {
                dealCards(isPlayerDealer);
            }
        } while (!isRoundFinish());
        calculateRoundScore();
    }

    /**
     * Método para calcular los puntos obtenidos por cada jugador en la ronda
     * Suma en los puntos de cada jugador sus escobas y reparte los puntos por
     * cantidad de cartas, siete de oro, cantidad de siete y cantidad de oros.
     *
     * @see EscobaMatchRound#solveSevenWinner()
     * @see EscobaMatchRound#solveOrosWinner()
     * @see EscobaMatchRound#solveSevenOroWinner()
     * @see EscobaMatchRound#solveQuantityCardsWinner()
     */
    private void calculateRoundScore() {
        LetterByLetterPrinter.println("Calculating round score...");
        roundPlayerHuman.setRoundPoints(roundPlayerHuman.getRoundPoints() + roundPlayerHuman.getEscobasQuantity());
        roundPlayerApp.setRoundPoints(roundPlayerApp.getRoundPoints() + roundPlayerApp.getEscobasQuantity());
        solveSevenWinner();
        solveSevenOroWinner();
        solveOrosWinner();
        solveQuantityCardsWinner();
        LetterByLetterPrinter.println("Round score:");
        LetterByLetterPrinter.println("Human player: " + roundPlayerHuman.getRoundPoints());
        LetterByLetterPrinter.println("App player: " + roundPlayerApp.getRoundPoints());
    }

    /**
     * Resuelve cúal de los 2 jugadores se lleva el punto por tener más siete.
     *
     * @see EscobaMatchRound#sevenQuantity(List)
     */
    private void solveSevenWinner() {
        // TODO: Implementar lógica para resolver quien gana el punto por cantidad de siete
        //  y si el jugador tiene los 4 siete, gana un punto extra
        // Inicializa el contador de cartas número 7 del jugador humano
        int countHumanSevens = 0;
        // Inicializa el contador de cartas número 7 del jugador de la app
        int countAppSevens = 0;

        // Recorre todas las cartas del mazo personal del jugador humano
        for(Card card :roundPlayerHuman.getPersonalDeck()){
            // Si la carta tiene número 7, incrementa el contador del jugador humano
            if(card.getNumber() == 7){
                countHumanSevens ++;
            }
        }

        // Recorre todas las cartas del mazo personal del jugador de la app
        for(Card card : roundPlayerApp.getPersonalDeck()){
            // Si la carta tiene número 7, incrementa el contador del jugador de la app
            if(card.getNumber() == 7){
                countAppSevens ++;
            }
        }

        // Si el jugador de la app tiene los cuatro sietes, le suma 1 punto en la ronda
        if(countAppSevens == 4){
            roundPlayerApp.setRoundPoints(roundPlayerApp.getRoundPoints() + 1);
        // Si el jugador humano tiene los cuatro sietes, le suma 1 punto en la ronda
        }else if(countHumanSevens == 4){
            roundPlayerHuman.setRoundPoints(roundPlayerHuman.getRoundPoints() + 1);
        }

        // Si el jugador de la app tiene más sietes que el jugador humano, le suma 1 punto en la ronda
        if(countAppSevens > countHumanSevens){
            roundPlayerApp.setRoundPoints(roundPlayerApp.getRoundPoints() + 1);
        // Si el jugador humano tiene más sietes que el jugador de la app, le suma 1 punto en la ronda
        }else if(countAppSevens < countHumanSevens){
            roundPlayerHuman.setRoundPoints(roundPlayerHuman.getRoundPoints() + 1);
        }

    }

    /**
     * Resuelve cúal de los 2 jugadores se lleva el punto por tener el siete de oro.
     *
     * @see EscobaMatchRound#sevenOroCheck(List)
     */
    private void solveSevenOroWinner() {
        if (sevenOroCheck(roundPlayerHuman.getPersonalDeck()) && !sevenOroCheck(roundPlayerApp.getPersonalDeck())) {
            LetterByLetterPrinter.println("El jugador gana 1 punto por tener el siete de oro.");
            roundPlayerHuman.setRoundPoints(roundPlayerHuman.getRoundPoints() + 1);
        } else if (!sevenOroCheck(roundPlayerHuman.getPersonalDeck()) && sevenOroCheck(roundPlayerApp.getPersonalDeck())) {
            LetterByLetterPrinter.println("La app gana 1 punto por tener el siete de oro.");
            roundPlayerApp.setRoundPoints(roundPlayerApp.getRoundPoints() + 1);
        }
    }

    /**
     * Resuelve cúal de los 2 jugadores se lleva el punto por tener más cartas de oro.
     *
     * @see EscobaMatchRound#orosQuantity(List)
     */
    private void solveOrosWinner() {
        if (orosQuantity(roundPlayerHuman.getPersonalDeck()) > orosQuantity(roundPlayerApp.getPersonalDeck())) {
            LetterByLetterPrinter.println("El jugador gana 1 punto por cantidad de oros.");
            roundPlayerHuman.setRoundPoints(roundPlayerHuman.getRoundPoints() + 1);
            if(orosQuantity(roundPlayerHuman.getPersonalDeck()) == 10) {
                LetterByLetterPrinter.println("El jugador gana 1 punto extra por tener todos oros.");
                roundPlayerHuman.setRoundPoints(roundPlayerHuman.getRoundPoints() + 1);
            }
        } else if (orosQuantity(roundPlayerHuman.getPersonalDeck()) < orosQuantity(roundPlayerApp.getPersonalDeck())) {
            LetterByLetterPrinter.println("La app gana 1 punto por cantidad de oros.");
            roundPlayerApp.setRoundPoints(roundPlayerApp.getRoundPoints() + 1);
            if(orosQuantity(roundPlayerApp.getPersonalDeck()) == 10) {
                LetterByLetterPrinter.println("La app gana 1 punto extra por tener todos oros.");
                roundPlayerApp.setRoundPoints(roundPlayerApp.getRoundPoints() + 1);
            }
        } else {
            LetterByLetterPrinter.println("No hay puntos por cantidad de oros. Ambos jugadores tienen la misma cantidad.");
        }
    }

    /**
     * Resuelve cúal de los 2 jugadores se lleva el punto por tener mayor cantidad de cartas.
     *
     * @see EscobaMatchRound#quantityCards(List)
     */
    private void solveQuantityCardsWinner() {
        if (quantityCards(roundPlayerHuman.getPersonalDeck()) > quantityCards(roundPlayerApp.getPersonalDeck())) {
            LetterByLetterPrinter.println("El jugador gana 1 punto por cantidad de cartas.");
            roundPlayerHuman.setRoundPoints(roundPlayerHuman.getRoundPoints() + 1);
        } else if (quantityCards(roundPlayerHuman.getPersonalDeck()) < quantityCards(roundPlayerApp.getPersonalDeck())) {
            LetterByLetterPrinter.println("La app gana 1 punto por cantidad de cartas.");
            roundPlayerApp.setRoundPoints(roundPlayerApp.getRoundPoints() + 1);
        } else {
            LetterByLetterPrinter.println("No hay puntos por cantidad de cartas. Ambos jugadores tienen la misma cantidad.");
        }
    }

    /**
     * Evalúa si la mano ha terminado.
     * Una mano es cada una de las veces que se vuelven a repartir cartas durante una ronda.
     *
     * @return true si ambos jugadores ya jugarón todas sus cartas en la mano, si no retorna false.
     *
     * @see RoundPlayer#getHandCards()
     */
    private boolean isHandFinish() {
        return roundPlayerHuman.getHandCards().isEmpty() && roundPlayerApp.getHandCards().isEmpty();
    }

    /**
     * Evalúa si la ronda ha terminado.
     *
     * @return true si ambos jugadores ya jugarón todas sus cartas en la mano
     * y no quedan cartas en el deck (mazo) para jugar otra mano, si no retorna false.
     *
     * @see RoundPlayer#getHandCards()
     * @see Deck#isEmpty()
     */
    private boolean isRoundFinish() {
        return roundPlayerHuman.getHandCards().isEmpty() && roundPlayerApp.getHandCards().isEmpty() && deck.isEmpty();
    }

    /**
     * Verifica en la lista de cartas, cuantas de ellas son del palo oro.
     *
     * @param cards lista de cartas a verificar
     *
     * @return la cantidad de cartas de oro que tiene la lista que recibe como parámetro
     */
    private Integer orosQuantity(List<Card> cards) {
        Integer quantity = 0;
        for (Card card : cards) {
            if (card.getCardSuit().equals(CardSuit.OROS)) {
                quantity++;
            }
        }
        return quantity;
    }

    /**
     * Verifica en la lista de cartas, cuantas de ellas son siete.
     *
     * @param cards lista de cartas a verificar
     *
     * @return la cantidad de siete que tiene la lista que recibe como parámetro
     */
    private Integer sevenQuantity(List<Card> cards) {
        Integer quantity = 0;
        for (Card card : cards) {
            if (card.getNumber().equals(7)) {
                quantity++;
            }
        }
        return quantity;
    }

    /**
     * Este método verifica si existe el siete de oro en la lista de cartas que recibe como parámetro.
     *
     * @param cards lista de cartas a verificar
     *
     * @return true si en la lista existe el siete de oro, false si no existe
     */
    private Boolean sevenOroCheck(List<Card> cards) {
        // TODO: Implementar lógica para verificar si existe el siete de oro en la lista de cartas
        // Crea una carta que representa el "Siete de Oro" con palo OROS, número 7 y valor 7
        Card sieteDeOro = new Card(CardSuit.OROS, 7, 7);

        // Retorna true si la lista 'cards' contiene la carta "Siete de Oro", de lo contrario false
        return cards.contains(sieteDeOro);

    }

    /**
     * Este cuenta la cantidad de cartas que hay en la lista que recibe como parámetro.
     *
     * @param cards lista de cartas a verificar
     *
     * @return la cantidad de cartas que tiene la lista que recibe como parámetro
     */
    private Integer quantityCards(List<Card> cards) {
        return cards.size();
    }

}
