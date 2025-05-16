package ar.edu.utn.frc.tup.piii;

import java.util.Objects;

public class Carta {
    private Palo palo;   // El palo de la carta (ORO, COPA, BASTO, ESPADA)
    private int valor;   // El valor numérico de la carta (1-7, 10-12)

    // Constructor que recibe valor y palo para crear la carta
    public Carta(int valor, Palo palo){
        this.valor = valor;
        this.palo = palo;
    }

    // Getter para obtener el palo de la carta
    public Palo getPalo() {
        return palo;
    }

    // Setter para modificar el palo de la carta
    public void setPalo(Palo palo) {
        this.palo = palo;
    }

    // Getter para obtener el valor de la carta
    public int getValor() {
        return valor;
    }

    // Setter para modificar el valor de la carta
    public void setValor(int valor) {
        this.valor = valor;
    }

    // Método toString para mostrar la carta en formato "valor de palo"
    @Override
    public String toString() {
        return valor + " de " + palo;
    }

    /**
     * Método equals para comparar dos cartas.
     * Dos cartas son iguales si tienen el mismo valor y palo.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;                 // Si es el mismo objeto, son iguales
        if (o == null || getClass() != o.getClass()) return false; // Si es distinto tipo, no son iguales
        Carta carta = (Carta) o;
        return valor == carta.valor && palo == carta.palo;
    }

    // Método hashCode que debe coincidir con equals para uso en colecciones
    @Override
    public int hashCode() {
        return Objects.hash(valor, palo);
    }
}
