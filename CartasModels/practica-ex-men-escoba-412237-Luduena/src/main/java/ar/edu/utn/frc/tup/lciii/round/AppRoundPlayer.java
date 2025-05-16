package ar.edu.utn.frc.tup.lciii.round;

import ar.edu.utn.frc.tup.lciii.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppRoundPlayer extends RoundPlayer {

    private static final Card SEVEN_ORO = new Card(CardSuit.OROS, 7, 7);

    public AppRoundPlayer(User player) {
        super(player);
    }

    @Override
    public void playTurn(List<Card> tableCards) {
        LetterByLetterPrinter.println("Is APP turn...");
        showCardsOnTheTable(tableCards);
        List<Card> selectedCards = selectCardsApp(tableCards);
        if(selectedCards == null) {
            LetterByLetterPrinter.println("The app can't make 15 with the cards. The app, added a card to the table.");
            Card cardToDiscard = getCardToDiscard();
            tableCards.add(cardToDiscard);
            this.getHandCards().remove(cardToDiscard);
        } else {
            takingCardsFromTable(tableCards, selectedCards);
        }
    }

    private Card getCardToDiscard() {
        return this.getHandCards().get(this.getHandCards().size() - 1);
    }

    /**
     * Este método selecciona las cartas que el jugador APP va a jugar.
     * Para esto sigue la siguiente lógica:
     * 1. Busca todas las combinaciones posibles de cartas entre las que están en la mesa y las que tiene en la mano.
     * Siempre que estas sumen 15.
     * 2. Ordena las combinaciones de mayor a menor por cantidad de cartas,
     * quedando arriba de la lista la que más cartas tiene.
     * 3. Si no encuentra ninguna combinación que sume 15, retorna null.
     * 4. Si encuentra solo una combinación que sume 15, selecciona dicha combinación.
     * 5. Si encuentra más de una combinación que sume 15, selecciona por el siguiente orden de prioridad:
     *   a. La combinación que contenga una escoba.
     *   b. La combinación que contenga el 7 de oro.
     *   c. La combinación que contenga más cartas de oro.
     *   d. La combinación que contenga más 7.
     *   e. La combinación que contenga más cartas. En este caso,
     *   la primera de la lista, ya la misma está ordenada por cantidad de cartas.
     *
     * @param tableCards cartas en la mesa.
     *
     * @return lista de cartas seleccionadas por el jugador APP.
     */
    private List<Card> selectCardsApp(List<Card> tableCards) {
        List<Card> selectedCards;
        List<List<Card>> subconjuntos = new ArrayList<>();
        for (Card handCard : this.getHandCards()) {
            List<Card> conjunto = new ArrayList<>(tableCards);
            conjunto.add(handCard);
            List<List<Card>> subconjuntosAux = obtenerSubconjuntos(conjunto);
            subconjuntosAux.removeIf(c -> !c.contains(handCard));
            subconjuntosAux.removeIf(c -> c.stream().mapToInt(Card::getValue).sum() != 15);
            subconjuntos.addAll(subconjuntosAux);
        }

        // TODO: Implementar método a partir de este punto siguiendo estas instrucciones:
        //  1. Si subconjuntos está vacío, retornar null.
        //  2. Ordenar subconjuntos de mayor a menor por cantidad de cartas.
        //  3. Llamar al método getCardsWithEscoba, si retorna algo diferente de null, retornar el valor.
        //  4. Llamar al método getCardsWithSevenOro, si retorna algo diferente de null, retornar el valor.
        //  5. Llamar al método getCardsWithMoreOros, si retorna algo diferente de null, retornar el valor.
        //  6. Llamar al método getCardsWithSeven, si retorna algo diferente de null, retornar el valor.
        //  7. Si no se cumple ninguna de las condiciones anteriores, retornar el primer subconjunto con mas cartas.

        // 1. Verifica si la lista 'subconjuntos' está vacía
        if(subconjuntos.isEmpty()){
            // Si está vacía, retorna null
            return  null;
        }

        // Variable para controlar si hubo intercambios en la ordenación
        boolean huboOrdenamientos;

        // Bucle do-while para ordenar la lista 'subconjuntos' de mayor a menor según la cantidad de cartas
        do{
            // Inicializa en falso antes de cada pasada
            huboOrdenamientos = false;
            // Recorre la lista desde el segundo elemento hasta el final
            for (int i = 1; i < subconjuntos.size() ; i++){
                // Compara el tamaño del subconjunto actual con el anterior
                if(subconjuntos.get(i).size() > subconjuntos.get(i -1).size()){
                    // Si el actual es mayor, se intercambian las posiciones
                    List<Card> temp = subconjuntos.get(i -1);
                    subconjuntos.set(i -1, subconjuntos.get(i) );
                    subconjuntos.set(i, temp);
                    // Marca que hubo un intercambio para continuar el ciclo
                    huboOrdenamientos = true;
                }
            }
        // Repite mientras haya intercambios, asegurando que la lista quede ordenada
        }while (huboOrdenamientos);

        // Intenta obtener cartas con "Escoba" usando el método correspondiente
        if(getCardsWithEscoba(subconjuntos, tableCards.size() + 1) != null){
            // Si el resultado no es null, retorna ese resultado
            return getCardsWithEscoba(subconjuntos, tableCards.size() + 1);
        // Si no, intenta obtener cartas con "Siete de Oro"
        }else if(getCardsWithSevenOro(subconjuntos) != null){
            // Si el resultado no es null, retorna ese resultado
            return getCardsWithSevenOro(subconjuntos);
        // Si no, intenta obtener cartas con la mayoría de "Oros"
        }else if(getCardsWithMoreOros(subconjuntos) != null){
            // Si el resultado no es null, retorna ese resultado
            return getCardsWithMoreOros(subconjuntos);
        // Si no, intenta obtener cartas con "Siete" (cualquier palo)
        } else if (getCardsWithSeven(subconjuntos) != null) {
            // Si el resultado no es null, retorna ese resultado
            return getCardsWithSeven(subconjuntos);
        // Si ninguna condición se cumple, retorna el subconjunto con más cartas (primero de la lista ordenada)
        }else{
            return subconjuntos.get(0);
        }
    }

    /**
     * Método que obtiene el subconjunto que contiene más cartas del tipo 7 o null si no existe ninguno.
     *
     * @param subconjuntos lista de subconjuntos en los que buscar.
     * @return el subconjunto que contiene más 7 o null si no existe.
     */
    private List<Card> getCardsWithSeven(List<List<Card>> subconjuntos) {
        // TODO: Implementar lógica para seleccionar el subconjunto que contenga más 7
        // Crea una lista para almacenar la cantidad de cartas con número 7 en cada subconjunto
        List<Integer> contadores = new ArrayList<>();

        // Recorre cada subconjunto dentro de la lista 'subconjuntos'
        for (List<Card> subconjunto : subconjuntos) {
            // Inicializa un contador para contar cuántas cartas con número 7 hay en el subconjunto actual
            Integer contadorDeSietes = 0;

            // Recorre cada carta dentro del subconjunto actual
            for (Card card : subconjunto) {
                // Verifica si la carta tiene el número 7
                if (card.getNumber() == 7) {
                    // Si es así, incrementa el contador de sietes
                    contadorDeSietes++;
                }
            }

            // Agrega el conteo de sietes de este subconjunto a la lista de contadores
            contadores.add(contadorDeSietes);
        }

        // Busca el mayor valor dentro de la lista de contadores (máximo número de sietes en algún subconjunto)
        Integer mayor = (Collections.max(contadores));

        // Verifica si el mayor es null (aunque no debería serlo si hay subconjuntos)
        if(mayor == null){
            // Retorna null si no se encontró ningún máximo
            return null;
        }else{
            // Retorna el subconjunto que tiene la mayor cantidad de cartas con número 7
            return subconjuntos.get(contadores.indexOf(mayor));
        }

    }

    /**
     * Método que obtiene el subconjunto que contiene una escoba o null si no existe ninguno.
     * Este método valída que un subconjunto de cartas sea igual a la
     * cantidad pasada por parámetro (cartas de la mesa + 1).
     *
     * @param subconjuntos lista de subconjuntos en los que buscar.
     * @param cardsQuantityToEscoba cantidad de cartas necesarias para hacer escoba. Igual a cartas de la mesa + 1.
     * @return el subconjunto que contiene una escoba o null si no existe.
     */
    private List<Card> getCardsWithEscoba(List<List<Card>> subconjuntos, Integer cardsQuantityToEscoba) {
        // TODO: Implementar lógica para seleccionar el subconjunto que contenga una escoba
        // Recorre cada subconjunto dentro de la lista 'subconjuntos'
        for (List<Card> subconjunto : subconjuntos){
            // Inicializa la suma de valores de las cartas en cero
            int suma = 0;
            
            // Verifica si el tamaño del subconjunto es igual a la cantidad requerida para "Escoba"
            if(subconjunto.size() == cardsQuantityToEscoba){
                // Suma el valor de cada carta dentro del subconjunto
                for( Card card : subconjunto){
                    suma += card.getValue();
                }
                // Si la suma de valores es exactamente 15, retorna este subconjunto
                if (suma == 15){
                    return subconjunto;
                }
            }
        }
        // Si ningún subconjunto cumple con las condiciones, retorna null
        return null;

    }

    /**
     * Método que obtiene el subconjunto que contiene el 7 de oro o null si no existe ninguno.
     *
     * @param subconjuntos lista de subconjuntos en los que buscar.
     * @return el subconjunto que contiene el 7 de oro o null si no existe.
     */
    private List<Card> getCardsWithSevenOro(List<List<Card>> subconjuntos) {
        // TODO: Implementar lógica para seleccionar el subconjunto que contenga el 7 de oro
        // Recorre cada subconjunto dentro de la lista 'subconjuntos'
        for(List<Card> subconjunto : subconjuntos){
            // Verifica si el subconjunto contiene la carta "Siete de Oro" (SEVEN_ORO)
            if(subconjunto.contains(SEVEN_ORO)){
                // Si la contiene, retorna ese subconjunto
                return subconjunto;
            }
        }
        // Si ningún subconjunto contiene el "Siete de Oro", retorna null
        return null;
    }

    /**
     * Método que obtiene el subconjunto que contiene más cartas de oro o null si no existe ninguno.
     *
     * @param subconjuntos lista de subconjuntos en los que buscar.
     * @return el subconjunto que contiene más oros o null si no existe.
     */
    private List<Card> getCardsWithMoreOros(List<List<Card>> subconjuntos) {
        // TODO: Implementar lógica para seleccionar el subconjunto que contenga más cartas de oro
        // Crea una lista para almacenar la cantidad de cartas del palo "Oros" en cada subconjunto
        List<Integer> contadores = new ArrayList<>();

        // Recorre cada subconjunto en la lista 'subconjuntos'
        for (List<Card> subconjunto : subconjuntos) {
            // Inicializa un contador para contar cartas del palo "Oros" en el subconjunto actual
            Integer contadorDeOros = 0;

            // Recorre cada carta dentro del subconjunto
            for (Card card : subconjunto) {
                // Verifica si la carta es del palo "Oros"
                if (card.getCardSuit() == CardSuit.OROS) {
                    // Incrementa el contador si la carta es de "Oros"
                    contadorDeOros++;
                }
            }

            // Agrega el contador de "Oros" de este subconjunto a la lista de contadores
            contadores.add(contadorDeOros);
        }

        // Obtiene el valor máximo dentro de la lista de contadores (subconjunto con más "Oros")
        Integer mayor = (Collections.max(contadores));

        // Verifica si el máximo es null (no debería serlo si hay subconjuntos)
        if(mayor == null){
            // Retorna null si no se encontró ningún máximo
            return null;
        }else{
            // Retorna el subconjunto que tiene la mayor cantidad de cartas "Oros"
            return subconjuntos.get(contadores.indexOf(mayor));
        }

    }

}
