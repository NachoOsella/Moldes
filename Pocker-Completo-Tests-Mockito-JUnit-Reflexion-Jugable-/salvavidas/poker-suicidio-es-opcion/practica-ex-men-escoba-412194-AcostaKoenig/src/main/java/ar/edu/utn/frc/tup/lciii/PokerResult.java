package ar.edu.utn.frc.tup.lciii;

// Clase que representa el resultado de una mano de póker
// Implementa Comparable para poder comparar dos resultados entre sí
public class PokerResult implements Comparable<PokerResult> {

    // Mano obtenida (por ejemplo: escalera, color, par, etc.)
    Hands hand;

    // Carta más alta que se usa para desempatar si las manos son iguales
    int highCard;

    // Constructor: inicializa el tipo de mano y la carta más alta
    PokerResult(Hands hand, int highCard) {
        this.hand = hand;
        this.highCard = highCard;
    }

    // Implementación del método compareTo para comparar dos resultados de póker
    @Override
    public int compareTo(PokerResult other) {
        // Compara primero el tipo de mano según su posición en el enum (mejor mano tiene menor índice)
        if (this.hand.ordinal() != other.hand.ordinal()) {
            // Se usa Integer.compare con los valores invertidos porque una mano más alta debe "ganar"
            return Integer.compare(other.hand.ordinal(), this.hand.ordinal());
        }

        // Si las manos son iguales, compara la carta más alta
        return Integer.compare(this.highCard, other.highCard);
    }
}
