package ar.edu.utn.frc.tup.lciii;

import java.util.ArrayList;
import java.util.List;

public class PokerBets {
    private int currentBet;      // Apuesta actual máxima en la ronda
    private int pot;             // Pozo acumulado total de la mano
    private int smallBlind;      // Valor del small blind (apuesta chica)
    private int bigBlind;        // Valor del big blind (apuesta grande)
    private List<Integer> sidePots;  // Lista para manejar posibles side pots (pozos laterales)

    /**
     * Constructor de la clase PokerBets
     * Inicializa valores y lista de side pots vacía
     * @param smallBlind valor del small blind
     * @param bigBlind valor del big blind
     */
    public PokerBets(int smallBlind, int bigBlind) {
        this.currentBet = 0;           // Al iniciar no hay apuestas
        this.pot = 0;                  // Pozo vacío
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.sidePots = new ArrayList<>(); // Sin side pots inicialmente
    }

    /**
     * Metodo para colocar ciegas al inicio de la mano
     * @param smallBlindPlayer jugador que pone small blind (apuesta chica)
     * @param bigBlindPlayer jugador que pone big blind (apuesta grande)
     */
    public void placeBlindBets(Player smallBlindPlayer, Player bigBlindPlayer) {
        // Colocar small blind, o máximo que pueda pagar el jugador si tiene pocas fichas
        int actualSmallBlind = Math.min(smallBlind, smallBlindPlayer.getChips());
        smallBlindPlayer.placeBet(actualSmallBlind);
        pot += actualSmallBlind;

        currentBet = actualSmallBlind;  // La apuesta actual queda en small blind

        // Colocar big blind, o máximo que pueda pagar el jugador si tiene pocas fichas
        int actualBigBlind = Math.min(bigBlind, bigBlindPlayer.getChips());
        bigBlindPlayer.placeBet(actualBigBlind);
        pot += actualBigBlind;

        currentBet = actualBigBlind;  // Actualiza la apuesta actual al big blind

        System.out.println(smallBlindPlayer.getName() + " pone small blind: " + actualSmallBlind);
        System.out.println(bigBlindPlayer.getName() + " pone big blind: " + actualBigBlind);
    }

    /**
     * Metodo para hacer call (igualar la apuesta actual)
     * @param player jugador que iguala la apuesta
     * @return cantidad apostada o -1 si no tiene fichas suficientes
     */
    public int call(Player player) {
        int amountToCall = currentBet - player.getBet(); // Lo que falta para igualar

        if (amountToCall > player.getChips()) {
            return -1; // No puede igualar la apuesta, no tiene fichas suficientes
        }

        player.placeBet(amountToCall);  // El jugador apuesta lo que falta para igualar
        pot += amountToCall;            // Se suma al pozo

        return amountToCall;            // Retorna cuánto apostó
    }

    /**
     * Metodo para hacer raise (subir la apuesta)
     * @param player jugador que sube la apuesta
     * @param amount cantidad a subir sobre la apuesta actual
     * @return true si pudo subir, false si no tiene fichas suficientes
     */
    public boolean raise(Player player, int amount) {
        int totalBet = currentBet + amount;          // Nueva apuesta máxima
        int amountToAdd = totalBet - player.getBet(); // Cuánto debe apostar el jugador para llegar a esa apuesta

        if (amountToAdd > player.getChips()) {
            return false;  // No puede hacer raise, no tiene fichas
        }

        player.placeBet(amountToAdd);  // Realiza la apuesta
        pot += amountToAdd;            // Se suma al pozo

        currentBet = totalBet;         // Actualiza la apuesta actual

        return true;                  // Raise exitoso
    }

    /**
     * Metodo para ir all-in (apostar todas las fichas)
     * @param player jugador que va all-in
     * @return cantidad apostada (todas sus fichas)
     */
    public int allIn(Player player) {
        int allInAmount = player.getChips(); // Toda la cantidad que tiene el jugador

        player.placeBet(allInAmount);  // Apuesta todo
        pot += allInAmount;            // Suma al pozo

        // Si la apuesta all-in supera la apuesta actual, se actualiza
        if (player.getBet() > currentBet) {
            currentBet = player.getBet();
        }

        // Si la apuesta all-in es menor que la apuesta actual, se crea un side pot
        if (player.getBet() < currentBet) {
            createSidePot(player);
        }

        return allInAmount;
    }

    /**
     * Metodo para crear side pot cuando un jugador va all-in y no puede cubrir la apuesta actual
     * @param allInPlayer jugador que fue all-in
     */
    private void createSidePot(Player allInPlayer) {
        int allInAmount = allInPlayer.getBet();

        // Aquí se agrega el monto para un side pot, simplificado
        // En una implementación completa se requiere más lógica para múltiples side pots
        sidePots.add(allInAmount);

        System.out.println("Se ha creado un side pot de " + allInAmount + " fichas");
    }

    /**
     * Metodo para reiniciar las apuestas al inicio de una nueva ronda dentro de la misma mano
     */
    public void resetBets() {
        currentBet = 0;  // Apuesta actual se resetea
    }

    /**
     * Metodo para reiniciar el pozo al inicio de una nueva mano
     */
    public void resetPot() {
        pot = 0;           // Pozo se vacía
        sidePots.clear();  // Se eliminan los side pots acumulados
    }

    /**
     * Metodo para otorgar el pozo completo al jugador ganador
     * @param winner jugador que gana el pozo
     */
    public void awardPotToWinner(Player winner) {
        winner.setChips(winner.getChips() + pot);  // Suma el pozo a las fichas del ganador
        System.out.println(winner.getName() + " gana " + pot + " fichas!");
        resetPot();  // Resetea pozo para la siguiente mano
    }

    /**
     * Metodo para que un jugador haga check (pasar)
     * @param player jugador que hace check
     */
    public void check(Player player) {
        // Solo puede hacer check si su apuesta es igual a la apuesta actual o si la apuesta actual es 0
        if (player.getBet() == currentBet || currentBet == 0) {
            System.out.println(player.getName() + " hace check.");
        } else {
            System.out.println(player.getName() + " no puede hacer check.");
        }
    }

    // Getters y setters

    public int getCurrentBet() {
        return currentBet;
    }

    public int getPot() {
        return pot;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public void setSmallBlind(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }
}
