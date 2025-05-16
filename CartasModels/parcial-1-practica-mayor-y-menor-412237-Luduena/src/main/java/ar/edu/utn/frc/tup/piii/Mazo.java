package ar.edu.utn.frc.tup.piii;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Mazo {

    private List<Carta> cartas; // Lista que contiene las cartas del mazo

    public Mazo() {
        // Al crear un mazo, inicializamos la lista con las 40 cartas españolas
        this.cartas = inicializarMazo();
    }

    /**
     * Este método mezcla las cartas del mazo usando Collections.shuffle,
     * que reordena aleatoriamente los elementos de la lista.
     */
    public void mezclarCartas() {
        Collections.shuffle(cartas);
    }

    /**
     * Método privado que crea el mazo completo de 40 cartas españolas.
     * Recorre todos los palos y valores, excepto los valores 8 y 9 que no se usan.
     * Crea cada carta y la agrega a la lista.
     * @return lista con las 40 cartas iniciales
     */
    private List<Carta> inicializarMazo() {
        List<Carta> cartas = new ArrayList<>();
        // Recorremos todos los palos definidos en la enumeración Palo
        for(Palo palo : Palo.values()){
            // Recorremos valores del 1 al 12, excluyendo 8 y 9
            for(int valor = 1; valor <= 12; valor++){
                if(valor != 8 && valor != 9){
                    Carta carta = new Carta(valor, palo);
                    cartas.add(carta); // Agregamos la carta al mazo
                }
            }
        }
        return cartas; // Devolvemos la lista completa de cartas
    }

    /**
     * Saca y elimina la primera carta del mazo.
     * Si el mazo está vacío, devuelve null.
     * @return la carta sacada o null si no hay cartas
     */
    public Carta primerCarta(){
        if(!cartas.isEmpty()){
            return cartas.remove(0); // Remueve y retorna la carta en la posición 0
        }
        return null;
    }

    /**
     * Devuelve la cantidad de cartas que quedan en el mazo.
     * @return cantidad de cartas restantes
     */
    public int cartasRestantes() {
        return cartas.size();
    }

    /**
     * Devuelve una copia del arreglo de cartas actuales para evitar
     * modificaciones externas directas sobre la lista interna.
     * @return lista nueva con las cartas actuales
     */
    public List<Carta> getCartas() {
        return new ArrayList<>(cartas);
    }
}
