package ar.edu.utn.frc.tup.lciii;

import java.util.Objects;

public class Card {
    // Número de la carta (2-14, donde 11=J, 12=Q, 13=K, 14=A)
    private int number;

    // Palo de la carta (Corazones, Diamantes, etc.)
    private CardSuits suit;

    // Constructor que recibe número y palo
    public Card(int number, CardSuits suit) {
        this.number = number;
        this.suit = suit;
    }

    // Getter del número
    public int getNumber() {
        return number;
    }

    // Setter del número
    public void setNumber(int number) {
        this.number = number;
    }

    // Getter del palo
    public CardSuits getSuit() {
        return suit;
    }

    // Setter del palo
    public void setSuit(CardSuits suit) {
        this.suit = suit;
    }

    // Representación en texto de la carta, ej: "A de Corazones"
    @Override
    public String toString() {
        String nombre;
        switch (number) {
            case 11:
                nombre = "J"; // Jack
                break;
            case 12:
                nombre = "Q"; // Queen
                break;
            case 13:
                nombre = "K"; // King
                break;
            case 14:
                nombre = "A"; // Ace
                break;
            default:
                nombre = String.valueOf(number); // Números normales
        }
        return nombre + " de " + suit;
    }

    // Método equals para comparar dos cartas (mismo número y palo)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Misma referencia
        if (obj == null || getClass() != obj.getClass()) return false;

        Card other = (Card) obj;
        return number == other.number && suit == other.suit;
    }

    // Método hashCode compatible con equals
    @Override
    public int hashCode() {
        return Objects.hash(number, suit);
    }
}
