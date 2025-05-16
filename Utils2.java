import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

// Nota: Para un proyecto real, cada enum y clase principal usualmente estaría en su propio archivo .java.
// Este archivo único se genera según el requisito específico.

/**
 * Enum para representar los Palos de una carta de poker.
 */
enum Suit {
    CORAZONES("Corazones"),
    DIAMANTES("Diamantes"),
    PICAS("Picas"),
    TREBOLES("Tréboles");

    private final String nombre;

    Suit(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el nombre en español del palo.
     * @return Nombre del palo.
     */
    @Override
    public String toString() {
        return nombre;
    }
}

/**
 * Enum para representar los Rangos (valores) de una carta de poker.
 * Incluye un valor numérico para comparaciones y evaluación de escaleras.
 */
enum Rank {
    DOS("Dos", 2),
    TRES("Tres", 3),
    CUATRO("Cuatro", 4),
    CINCO("Cinco", 5),
    SEIS("Seis", 6),
    SIETE("Siete", 7),
    OCHO("Ocho", 8),
    NUEVE("Nueve", 9),
    DIEZ("Diez", 10),
    JOTA("Jota", 11), // Jack
    REINA("Reina", 12), // Queen
    REY("Rey", 13), // King
    AS("As", 14); // Ace, por defecto alto (14). El caso As-bajo (A-2-3-4-5) se maneja en el evaluador de escaleras.

    private final String nombre;
    private final int valor;

    Rank(String nombre, int valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    /**
     * Obtiene el valor numérico del rango.
     * Este valor se usa para comparaciones y para determinar la fuerza de las manos.
     * (Ej. As = 14, Rey = 13, ..., Dos = 2).
     * @return El valor numérico del rango.
     */
    public int getValor() {
        return valor;
    }

    /**
     * Devuelve el nombre en español del rango.
     * @return Nombre del rango.
     */
    @Override
    public String toString() {
        return nombre;
    }
}

/**
 * Representa una carta de poker individual.
 * Contiene un palo (Suit) y un rango (Rank).
 * Es {@link Comparable} para facilitar el ordenamiento de listas de cartas,
 * principalmente por rango.
 */
class Card implements Comparable<Card> {
    private final Suit suit;
    private final Rank rank;

    /**
     * Constructor para crear una nueva carta.
     * @param suit El palo de la carta (Corazones, Diamantes, Picas, Tréboles).
     * @param rank El rango de la carta (As, Rey, ..., Dos).
     */
    public Card(Suit suit, Rank rank) {
        if (suit == null || rank == null) {
            throw new IllegalArgumentException("El palo y el rango no pueden ser nulos.");
        }
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Obtiene el palo de la carta.
     * @return El {@link Suit} de la carta.
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Obtiene el rango de la carta.
     * @return El {@link Rank} de la carta.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Obtiene el valor numérico del rango de la carta.
     * El As tiene un valor de 14. Para escaleras A-2-3-4-5, la lógica específica
     * de evaluación de manos debe considerar al As como valor 1 si es necesario.
     * @return El valor numérico del rango (2 para Dos, ..., 14 para As).
     */
    public int getRankValue() {
        return rank.getValor();
    }

    /**
     * Devuelve una representación en String de la carta.
     * Ejemplo: "As de Corazones".
     * @return Una cadena de texto que representa la carta.
     */
    @Override
    public String toString() {
        return rank.toString() + " de " + suit.toString();
    }

    /**
     * Compara esta carta con otra carta, primariamente por el valor de su rango.
     * Si los rangos son iguales, esta implementación considera las cartas "iguales"
     * en términos de valor para una mano de poker (el palo no desempata rangos de cartas individuales).
     * Para un ordenamiento total y único (ej. al inicializar un mazo), se podría incluir el palo.
     * @param other La otra carta con la que comparar.
     * @return Un valor negativo si esta carta es de menor rango,
     *         un valor positivo si esta carta es de mayor rango,
     *         o cero si los rangos son iguales.
     */
    @Override
    public int compareTo(Card other) {
        return Integer.compare(this.rank.getValor(), other.rank.getValor());
    }

    /**
     * Verifica si dos objetos Carta son iguales (mismo palo y rango).
     * @param o El objeto a comparar con esta carta.
     * @return {@code true} si los objetos son la misma carta (igual palo y rango), {@code false} de lo contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && rank == card.rank;
    }

    /**
     * Genera un código hash para la carta, basado en su palo y rango.
     * @return El código hash de la carta.
     */
    @Override
    public int hashCode() {
        int result = suit.hashCode();
        result = 31 * result + rank.hashCode();
        return result;
    }
}

/**
 * Representa un mazo (baraja) de cartas de poker.
 * Por defecto, se inicializa con 52 cartas estándar.
 */
class Deck {
    private List<Card> cards;
    private final Random random; // Para barajar

    /**
     * Constructor para crear un mazo estándar de 52 cartas.
     * Las cartas se crean y se añaden al mazo, listas para ser barajadas.
     */
    public Deck() {
        this.cards = new ArrayList<>(52);
        this.random = new Random();
        initializeDeck();
    }

    /**
     * Inicializa el mazo con las 52 cartas estándar (4 palos, 13 rangos).
     */
    private void initializeDeck() {
        this.cards.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                this.cards.add(new Card(suit, rank));
            }
        }
    }

    /**
     * Baraja (mezcla) las cartas en el mazo de forma aleatoria.
     * Utiliza {@link Collections#shuffle(List, Random)}.
     */
    public void shuffle() {
        Collections.shuffle(this.cards, this.random);
    }

    /**
     * Reparte una carta del tope del mazo.
     * La carta repartida se elimina del mazo. Se considera el "tope" como el final de la lista.
     * @return La {@link Card} del tope del mazo, o {@code null} si el mazo está vacío.
     */
    public Card dealCard() {
        if (isEmpty()) {
            // Opcionalmente, podría lanzar una excepción como IllegalStateException.
            System.err.println("Error: Mazo vacío, no se puede repartir carta.");
            return null;
        }
        return this.cards.remove(this.cards.size() - 1); // LIFO
    }

    /**
     * Verifica si el mazo está vacío.
     * @return {@code true} si el mazo no tiene cartas, {@code false} de lo contrario.
     */
    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    /**
     * Devuelve el número de cartas restantes en el mazo.
     * @return El tamaño actual del mazo.
     */
    public int size() {
        return this.cards.size();
    }

    /**
     * Restaura el mazo a su estado original de 52 cartas y lo baraja.
     * Útil para iniciar una nueva partida o ronda.
     */
    public void resetAndShuffle() {
        initializeDeck();
        shuffle();
    }
}

/**
 * Enum para representar el estado de un jugador en la partida de poker.
 */
enum PlayerStatus {
    ESPERANDO_ACCION, // El jugador está esperando su turno para actuar.
    ACTIVO,           // El jugador está activo en la mano actual.
    RETIRADO,         // El jugador se ha retirado (fold) de la mano actual.
    ALL_IN,           // El jugador ha apostado todas sus fichas y está activo pero no puede realizar más apuestas.
    FUERA_DEL_JUEGO   // El jugador ha perdido todas sus fichas y no puede continuar en el juego.
}

/**
 * Representa a un jugador de poker.
 * Contiene información sobre su nombre, mano, fichas y estado actual.
 */
class Player {
    private final String name;
    private final List<Card> hand; // Mano de 2 cartas privadas del jugador
    private int chips;
    private PlayerStatus status;
    private int currentBetInRound; // Fichas apostadas por el jugador en la ronda de apuestas actual

    /**
     * Constructor para un nuevo jugador.
     * @param name El nombre del jugador.
     * @param initialChips La cantidad inicial de fichas del jugador.
     */
    public Player(String name, int initialChips) {
        this.name = name;
        this.chips = initialChips;
        this.hand = new ArrayList<>(2);
        this.status = PlayerStatus.ACTIVO; // Por defecto, un jugador nuevo está activo.
        this.currentBetInRound = 0;
    }

    /**
     * El jugador recibe una carta (generalmente del repartidor).
     * Se añade a su mano si tiene menos de 2 cartas.
     * @param card La {@link Card} a añadir a la mano del jugador.
     */
    public void receiveCard(Card card) {
        if (this.hand.size() < 2) {
            this.hand.add(card);
        }
        // TODO: Considerar si se debe lanzar una excepción si ya tiene 2 cartas o manejarlo de otra forma.
    }

    /**
     * Limpia la mano del jugador (descarta las cartas).
     * Usualmente se llama al final de una ronda o al inicio de una nueva.
     */
    public void clearHand() {
        this.hand.clear();
    }
    
    /**
     * El jugador realiza una apuesta.
     * Reduce las fichas del jugador por la cantidad apostada.
     * Si la cantidad es mayor o igual a las fichas del jugador, este va All-In.
     * @param amount La cantidad a apostar.
     * @return La cantidad efectivamente apostada (puede ser menor que {@code amount} si el jugador va All-In).
     */
    public int placeBet(int amount) {
        if (amount <= 0) {
            // No se puede apostar una cantidad negativa o cero, a menos que sea un 'check' manejado externamente.
            // Para una apuesta real, debe ser > 0.
            return 0; 
        }

        int betAmount = Math.min(amount, this.chips); // No puede apostar más de lo que tiene.
        this.chips -= betAmount;
        this.currentBetInRound += betAmount;

        if (this.chips == 0) {
            this.status = PlayerStatus.ALL_IN;
        }
        return betAmount;
    }

    /**
     * El jugador sube la apuesta actual.
     * Primero iguala la apuesta más alta y luego añade la cantidad de subida.
     * @param raiseAmount La cantidad adicional a la apuesta actual para subir.
     * @param currentHighestBetOnTable La apuesta más alta en la mesa que se necesita igualar antes de subir.
     * @return La cantidad total apostada por el jugador en esta acción de subida.
     */
    public int raiseBet(int raiseAmount, int currentHighestBetOnTable) {
        int amountToCall = currentHighestBetOnTable - this.currentBetInRound;
        int totalBetForThisAction = amountToCall + raiseAmount;
        return placeBet(totalBetForThisAction);
    }

    /**
     * El jugador iguala la apuesta actual en la mesa.
     * @param totalAmountToCall La cantidad total que el jugador debe tener en el pozo para esta ronda
     *                          para igualar la apuesta más alta.
     * @return La cantidad efectivamente apostada para igualar.
     */
    public int callBet(int totalAmountToCall) {
        int neededToCall = totalAmountToCall - this.currentBetInRound;
        return placeBet(neededToCall);
    }

    /**
     * El jugador se retira de la mano actual (fold).
     * Cambia el estado del jugador a {@link PlayerStatus#RETIRADO}.
     */
    public void fold() {
        this.status = PlayerStatus.RETIRADO;
        // Opcionalmente, las cartas podrían limpiarse aquí o mantenerse para mostrarlas al final si es necesario.
        // clearHand(); // Depende de las reglas de visualización del juego.
    }

    /**
     * El jugador pasa (check), si no hay apuestas pendientes que igualar.
     * No cambia las fichas. El estado puede cambiar de ESPERANDO_ACCION a ACTIVO.
     * La validación de si un check es legal (no hay apuestas pendientes) debe hacerse en {@link GameLogic}.
     */
    public void check() {
        // Lógica de validación (ej. currentBetInRound == highestBetOnTable) estaría en GameLogic.
        if (this.status == PlayerStatus.ESPERANDO_ACCION) {
            this.status = PlayerStatus.ACTIVO;
        }
        // Si ya está ACTIVO y hace check, el estado no cambia.
    }

    /**
     * El jugador apuesta todas sus fichas restantes (All-In).
     * @return La cantidad total de fichas apostadas (todas sus fichas restantes).
     */
    public int goAllIn() {
        return placeBet(this.chips); // Apuesta todas las fichas que tiene.
    }
    
    /**
     * Resetea la apuesta del jugador para una nueva ronda de apuestas (ej. del pre-flop al flop).
     * Se llama al final de una calle de apuestas.
     */
    public void resetCurrentBetInRound() {
        this.currentBetInRound = 0;
    }
    
    /**
     * Añade fichas al stack del jugador (ej. al ganar un pozo).
     * @param amount La cantidad de fichas a añadir.
     */
    public void addChips(int amount) {
        if (amount < 0) return; // No se pueden añadir fichas negativas
        this.chips += amount;
    }

    // Getters y Setters básicos
    public String getName() { return name; }
    public List<Card> getHand() { return Collections.unmodifiableList(hand); } // Devuelve copia inmutable para proteger la mano
    public int getChips() { return chips; }
    public PlayerStatus getStatus() { return status; }
    public void setStatus(PlayerStatus status) { this.status = status; }
    public int getCurrentBetInRound() { return currentBetInRound; }

    @Override
    public String toString() {
        return name + " (Fichas: " + chips + ", Estado: " + status + ", Apuesta actual: " + currentBetInRound + ")";
    }
}

/**
 * Enum para los tipos de manos de poker, ordenados de menor a mayor valor de mano.
 * El orden ordinal de estos enums (de CARTA_ALTA a ESCALERA_REAL) es crucial para la comparación.
 */
enum HandRankValue {
    CARTA_ALTA,      // High Card
    PAR,             // One Pair
    DOBLE_PAR,       // Two Pair
    TRIO,            // Three of a Kind
    ESCALERA,        // Straight
    COLOR,           // Flush
    FULL_HOUSE,      // Full House
    POKER,           // Four of a Kind (Quad)
    ESCALERA_COLOR,  // Straight Flush
    ESCALERA_REAL    // Royal Flush
}

/**
 * Representa una mano de poker evaluada (ej. Par de Ases, Escalera Real).
 * Contiene el tipo de mano ({@link HandRankValue}) y las 5 cartas que la componen,
 * ordenadas de una manera específica para facilitar los desempates.
 * Implementa {@link Comparable} para poder determinar la mano ganadora entre varias {@code PokerHand}.
 */
class PokerHand implements Comparable<PokerHand> {
    private final HandRankValue handRankValue;
    private final List<Card> bestFiveCards; // Las 5 cartas que forman la mano, ordenadas por importancia para desempate.

    /**
     * Constructor para una mano de poker evaluada.
     * @param handRankValue El tipo de mano (ej. PAR, ESCALERA_REAL) según {@link HandRankValue}.
     * @param bestFiveCards La lista de 5 cartas que forman la mejor mano.
     *                      Estas cartas deben estar ordenadas de forma que la comparación directa
     *                      de sus rangos (después de comparar {@code handRankValue}) sirva para desempates.
     *                      Ejemplos de orden:
     *                      - Para un Par de Ases con K, Q, J kickers: [AS, AD, KH, QS, JC]
     *                      - Para un Full House de Reyes con Damas: [KH, KS, KD, QH, QJ]
     *                      - Para una Escalera A-5 (As bajo): [5H, 4S, 3D, 2C, AC] (el As se trata como el más bajo)
     * @throws IllegalArgumentException si {@code bestFiveCards} no contiene exactamente 5 cartas.
     */
    public PokerHand(HandRankValue handRankValue, List<Card> bestFiveCards) {
        if (bestFiveCards == null || bestFiveCards.size() != 5) {
            throw new IllegalArgumentException("bestFiveCards debe contener exactamente 5 cartas.");
        }
        this.handRankValue = handRankValue;
        // Se guarda una copia inmutable para asegurar la integridad de la mano evaluada.
        this.bestFiveCards = Collections.unmodifiableList(new ArrayList<>(bestFiveCards));
    }

    /**
     * Obtiene el tipo de mano (ej. PAR, ESCALERA_REAL).
     * @return El {@link HandRankValue} de esta mano de poker.
     */
    public HandRankValue getHandRankValue() {
        return handRankValue;
    }

    /**
     * Obtiene la lista de las 5 cartas que componen la mejor mano.
     * Estas cartas están ordenadas por relevancia para facilitar los desempates.
     * @return Una lista inmutable de las 5 mejores {@link Card} que forman esta mano.
     */
    public List<Card> getBestFiveCards() {
        return bestFiveCards;
    }

    /**
     * Compara esta mano de poker con otra para determinar cuál es más fuerte.
     * La comparación se basa primero en el {@link HandRankValue} (tipo de mano).
     * Si los tipos de mano son iguales, se comparan los rangos de las cartas en {@code bestFiveCards}
     * una por una, en el orden en que están almacenadas, para resolver empates.
     * @param other La otra {@link PokerHand} a comparar.
     * @return Un valor negativo si esta mano es peor que {@code other},
     *         un valor positivo si esta mano es mejor que {@code other},
     *         o cero si ambas manos son idénticas en valor.
     */
    @Override
    public int compareTo(PokerHand other) {
        // Primero, comparar por el tipo de mano (HandRankValue).
        // El ordinal del enum se usa aquí, asumiendo que los enums están definidos de menor a mayor valor.
        int rankComparison = Integer.compare(this.handRankValue.ordinal(), other.handRankValue.ordinal());
        if (rankComparison != 0) {
            return rankComparison;
        }

        // Si el tipo de mano es el mismo, comparar por los valores de las cartas en bestFiveCards.
        // Se asume que bestFiveCards están ordenadas por importancia para el desempate
        // (ej. para Dos Pares AA KK Q, las cartas estarían ordenadas como [A,A,K,K,Q]).
        for (int i = 0; i < this.bestFiveCards.size(); i++) {
            // Para la escalera A-2-3-4-5, el As tiene valor bajo. La lista bestFiveCards
            // para A-2-3-4-5 debe ser [5,4,3,2,A]. El getRankValue() del As es 14.
            // Esto requiere un manejo especial si getRankValue() se usa directamente.
            // Asumimos que el orden en bestFiveCards y los valores de Rank son consistentes.
            // Para A-2-3-4-5, la carta 'AS' (valor 14) está al final y es la más baja en esa escalera.
            // Si una carta es As y la mano es Escalera A-5, su valor efectivo para esta comparación debe ser 1.
            // Esta lógica de "valor efectivo" se puede encapsular en la construcción de bestFiveCards.
            // Por ahora, usamos getRankValue() directamente.
            // Para la escalera As-bajo (5,4,3,2,A), el As (valor 14) es la última carta.
            // Esto funciona si comparamos con otra escalera 5-alta. ej. 5,4,3,2,A vs 5,4,3,2,A.
            // Pero si comparamos 6,5,4,3,2 vs 5,4,3,2,A (As-low), el As (14) parecería más alto que el 2.
            // Solución: La lista bestFiveCards para A-2-3-4-5 debe ser ordenada como [5,4,3,2,A],
            // y en la comparación, el As en esta posición debe tratarse como el valor más bajo (1).
            // Sin embargo, una forma más simple es que Rank.AS.getValor() siempre sea 14, y
            // la lista para A-2-3-4-5 se ordene así: [Card(5), Card(4), Card(3), Card(2), Card(AS)].
            // El comparador de PokerHand debe ser consciente de esto para Escaleras con AS bajo.
            // La implementación actual es genérica y depende de que bestFiveCards esté bien ordenada.
            // Por simplicidad, esta plantilla asume que los `getRankValue()` son directamente comparables
            // tras el ordenamiento de `bestFiveCards` realizado por los `detect...` métodos.

            int cardComparison = Integer.compare(
                this.bestFiveCards.get(i).getRankValue(),
                other.bestFiveCards.get(i).getRankValue()
            );
            // Excepción: Si la mano es una escalera A-5 (ej. 5,4,3,2,A) y la carta es un As, tratarla como valor 1.
            // Esta lógica es compleja y podría requerir que PokerHand almacene el valor efectivo de la escalera.
            // Para el template, nos mantenemos simples.
            // El orden de `bestFiveCards` es clave. Para A-2-3-4-5 (5-high straight), la lista es [5,4,3,2,A].
            // Si A es el último, su valor 14 es mayor que un 2 en un 6-high straight [6,5,4,3,2].
            // Esto es incorrecto. La comparación de A-low straights necesita un tratamiento especial.
            // Una solución es que las cartas en bestFiveCards para A-low straight tengan un valor efectivo para 'A' de 1.
            // Esta plantilla no implementa esa fineza, pero lo anota como un punto importante.
            // Los detectores deben poner las cartas en el orden correcto:
            // Straight 6,5,4,3,2. bestFiveCards: [6,5,4,3,2]
            // Straight A,K,Q,J,T. bestFiveCards: [A,K,Q,J,T]
            // Straight 5,4,3,2,A. bestFiveCards: [5,4,3,2,A] (el A es el más bajo de este grupo)
            // El comparador general funciona si el orden es estrictamente el de la "fuerza" de la carta en esa mano.

            if (cardComparison != 0) {
                return cardComparison;
            }
        }
        return 0; // Las manos son idénticas en valor.
    }

    @Override
    public String toString() {
        // Muestra el tipo de mano y las cartas que la componen (solo sus rangos para brevedad).
        return handRankValue.toString() + ": " + bestFiveCards.stream()
                .map(c -> c.getRank().toString()) 
                .collect(Collectors.joining(", "));
    }
}

/**
 * Clase de utilidad para evaluar manos de poker en Texas Hold'em.
 * Contiene métodos estáticos para determinar la mejor mano de 5 cartas
 * que un jugador puede formar a partir de sus 2 cartas privadas y las 3 a 5 cartas comunitarias.
 */
class HandEvaluator {

    /**
     * Evalúa la mejor mano de poker de 5 cartas posible a partir de las cartas privadas del jugador
     * y las cartas comunitarias disponibles en la mesa.
     * @param playerCards Las 2 cartas privadas del jugador.
     * @param communityCards Las 3 a 5 cartas comunitarias (Flop, Turn, River).
     * @return La {@link PokerHand} más fuerte que se puede formar con cualquier combinación de 5 cartas
     *         de las disponibles (2 del jugador + 3-5 comunitarias). Retorna {@code null} si no se pueden
     *         formar 5 cartas (ej. entrada inválida).
     */
    public static PokerHand evaluateBestHand(List<Card> playerCards, List<Card> communityCards) {
        List<Card> allAvailableCards = new ArrayList<>(playerCards);
        allAvailableCards.addAll(communityCards);

        if (allAvailableCards.size() < 5) {
            // No hay suficientes cartas para formar una mano de 5.
            // Esto podría ser una situación anómala o un error de lógica en el juego.
            System.err.println("Error: Menos de 5 cartas disponibles para evaluar.");
            return null;
        }
        
        // Generar todas las combinaciones posibles de 5 cartas a partir de las cartas disponibles.
        // Si hay 7 cartas (2 privadas + 5 comunitarias), esto es 7C5 = 21 combinaciones.
        // Si hay 6 cartas (2 privadas + 4 comunitarias en el Turn), esto es 6C5 = 6 combinaciones.
        // Si hay 5 cartas (2 privadas + 3 comunitarias en el Flop), esto es 5C5 = 1 combinación.
        List<List<Card>> fiveCardCombinations = generateCombinations(allAvailableCards, 5);
        
        PokerHand bestHandSoFar = null;

        // Evaluar cada combinación de 5 cartas y quedarse con la mejor.
        for (List<Card> fiveCardHand : fiveCardCombinations) {
            PokerHand currentEvaluatedHand = evaluateSingleFiveCardHand(fiveCardHand);
            if (currentEvaluatedHand != null) {
                if (bestHandSoFar == null || currentEvaluatedHand.compareTo(bestHandSoFar) > 0) {
                    bestHandSoFar = currentEvaluatedHand;
                }
            }
        }
        return bestHandSoFar;
    }

    /**
     * Genera todas las combinaciones de k cartas a partir de una lista de cartas.
     * @param cards La lista de cartas de la cual generar combinaciones.
     * @param k El tamaño de cada combinación (normalmente 5 para poker).
     * @return Una lista de todas las combinaciones posibles (cada combinación es una lista de k cartas).
     */
    private static List<List<Card>> generateCombinations(List<Card> cards, int k) {
        List<List<Card>> allCombinations = new ArrayList<>();
        if (cards == null || cards.size() < k || k < 0) {
            return allCombinations; // Entrada inválida o no hay suficientes cartas.
        }
        List<Card> currentCombination = new ArrayList<>();
        // Iniciar la recursión para encontrar combinaciones.
        generateCombinationsRecursive(cards, k, 0, currentCombination, allCombinations);
        return allCombinations;
    }

    /**
     * Método auxiliar recursivo para generar combinaciones de cartas.
     * @param allCards Lista completa de cartas de donde elegir.
     * @param k_remaining Número de cartas restantes a elegir para la combinación actual.
     * @param startPosition Índice de inicio en {@code allCards} para la elección actual (evita duplicados y asegura orden).
     * @param currentCombination La combinación que se está construyendo actualmente.
     * @param allCombinations Lista de salida donde se almacenan todas las combinaciones completas.
     */
    private static void generateCombinationsRecursive(List<Card> allCards, int k_remaining, int startPosition,
                                                      List<Card> currentCombination, List<List<Card>> allCombinations) {
        if (k_remaining == 0) {
            // Se ha formado una combinación completa de k cartas. Añadir una copia.
            allCombinations.add(new ArrayList<>(currentCombination));
            return;
        }

        // Optimización: si las cartas restantes en allCards no son suficientes para completar k_remaining elementos.
        if (allCards.size() - startPosition < k_remaining) {
            return;
        }

        // Iterar sobre las cartas disponibles a partir de startPosition.
        for (int i = startPosition; i < allCards.size(); i++) {
            // Incluir la carta actual en la combinación.
            currentCombination.add(allCards.get(i));
            // Llamada recursiva para elegir las k_remaining-1 cartas restantes, empezando desde la siguiente posición.
            generateCombinationsRecursive(allCards, k_remaining - 1, i + 1, currentCombination, allCombinations);
            // Backtrack: remover la carta actual para probar otras combinaciones en la siguiente iteración del bucle.
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
    
    /**
     * Evalúa una única mano de 5 cartas y determina su tipo (ej. Par, Escalera, Color).
     * @param fiveCardHand Una lista de exactamente 5 {@link Card}.
     * @return La {@link PokerHand} correspondiente a esas 5 cartas.
     * @throws IllegalArgumentException si {@code fiveCardHand} no contiene exactamente 5 cartas.
     */
    private static PokerHand evaluateSingleFiveCardHand(List<Card> fiveCardHand) {
        if (fiveCardHand == null || fiveCardHand.size() != 5) {
            throw new IllegalArgumentException("Se requieren exactamente 5 cartas para evaluar una mano individual.");
        }

        // Crear una copia para ordenar sin modificar la lista original de la combinación.
        List<Card> sortedHand = new ArrayList<>(fiveCardHand);
        // Ordenar las 5 cartas por rango descendente (As alto, Dos bajo).
        // Esto facilita la detección de patrones como escaleras y grupos de rangos.
        Collections.sort(sortedHand, Collections.reverseOrder()); 

        // Intentar detectar cada tipo de mano, desde la más alta (Escalera Real) a la más baja (Carta Alta).
        // El primer tipo de mano detectado (el más fuerte) es el que se retorna.
        PokerHand hand;
        if ((hand = detectRoyalFlush(sortedHand)) != null) return hand;
        if ((hand = detectStraightFlush(sortedHand)) != null) return hand;
        if ((hand = detectFourOfAKind(sortedHand)) != null) return hand;
        if ((hand = detectFullHouse(sortedHand)) != null) return hand;
        if ((hand = detectFlush(sortedHand)) != null) return hand;
        if ((hand = detectStraight(sortedHand)) != null) return hand;
        if ((hand = detectThreeOfAKind(sortedHand)) != null) return hand;
        if ((hand = detectTwoPair(sortedHand)) != null) return hand;
        if ((hand = detectOnePair(sortedHand)) != null) return hand;
        
        // Si no es ninguna de las anteriores, la mano se clasifica como Carta Alta.
        return detectHighCard(sortedHand);
    }

    // --- Métodos auxiliares para detectar tipos de mano específicos ---
    // Todos estos métodos toman una List<Card> de 5 cartas, que se asume que ya está
    // ORDENADA POR RANGO DESCENDENTE (As = 14, ..., Dos = 2).
    // Devuelven un objeto PokerHand si la condición para ese tipo de mano se cumple,
    // o null si no se cumple.
    // La lista 'bestFiveCards' dentro del objeto PokerHand devuelto debe estar
    // ordenada de manera específica para facilitar los desempates.

    private static PokerHand detectRoyalFlush(List<Card> hand) {
        // Una Escalera Real es una Escalera de Color con la carta más alta siendo un As (A-K-Q-J-10 del mismo palo).
        // Precondición: 'hand' está ordenada por rango descendente.
        boolean isFlush = isSameSuit(hand);
        if (!isFlush) return null; // No es color, no puede ser Escalera Real.

        // Verificar si los rangos son A, K, Q, J, 10.
        boolean isAceHighStraight = hand.get(0).getRank() == Rank.AS &&
                                  hand.get(1).getRank() == Rank.REY &&
                                  hand.get(2).getRank() == Rank.REINA &&
                                  hand.get(3).getRank() == Rank.JOTA &&
                                  hand.get(4).getRank() == Rank.DIEZ;
        
        if (isAceHighStraight) {
            // 'hand' ya está ordenada como A,K,Q,J,10, que es el orden correcto para desempates (aunque no hay desempate para Royal Flush).
            return new PokerHand(HandRankValue.ESCALERA_REAL, new ArrayList<>(hand));
        }
        return null;
    }

    private static PokerHand detectStraightFlush(List<Card> hand) {
        // Una Escalera de Color son 5 cartas consecutivas del mismo palo.
        // No puede ser una Escalera Real (esa se detecta y retorna antes).
        // Precondición: 'hand' está ordenada por rango descendente.
        boolean isFlush = isSameSuit(hand);
        if (!isFlush) return null; // No es color, no puede ser Escalera de Color.

        // Caso especial: Escalera de Color As-bajo (A-2-3-4-5 del mismo palo).
        // En 'hand' (ordenada A-5-4-3-2), el As está en hand.get(0).
        boolean isAceLowStraight = hand.get(0).getRank() == Rank.AS &&
                                   hand.get(1).getRank() == Rank.CINCO &&
                                   hand.get(2).getRank() == Rank.CUATRO &&
                                   hand.get(3).getRank() == Rank.TRES &&
                                   hand.get(4).getRank() == Rank.DOS;
        if (isAceLowStraight) {
            // Reordenar para desempate: 5,4,3,2,A. El As actúa como la carta más baja.
            List<Card> aceLowOrder = Arrays.asList(hand.get(1), hand.get(2), hand.get(3), hand.get(4), hand.get(0));
            return new PokerHand(HandRankValue.ESCALERA_COLOR, aceLowOrder);
        }
        
        // Verificar Escalera de Color normal (no As-alta, ya que sería Royal Flush y se detectó antes).
        boolean isNormalStraight = true;
        for (int i = 0; i < 4; i++) {
            if (hand.get(i).getRankValue() != hand.get(i+1).getRankValue() + 1) {
                isNormalStraight = false;
                break;
            }
        }
        if (isNormalStraight) {
            // Si es una Escalera de Color As-alta (A,K,Q,J,10), ya habría sido detectada como Escalera Real.
            // Por lo tanto, si llegamos aquí y es As-alta, algo está mal o es un caso no esperado.
            // Esta función asume que detectRoyalFlush se llamó antes.
            // 'hand' ya está ordenada correctamente para desempate (ej. 9,8,7,6,5).
            return new PokerHand(HandRankValue.ESCALERA_COLOR, new ArrayList<>(hand));
        }
        return null;
    }

    private static PokerHand detectFourOfAKind(List<Card> hand) {
        // Poker son 4 cartas del mismo rango.
        // Precondición: 'hand' está ordenada por rango descendente.
        // Ejemplos de orden en 'hand': AAAA K (Poker de Ases, kicker Rey) o K AAAA (Poker de Ases, kicker Rey).
        // La mano de 5 cartas se ordena: [Quad1, Quad2, Quad3, Quad4, Kicker].
        Map<Rank, List<Card>> rankGroups = groupCardsByRank(hand);
        List<Card> quads = null;
        Card kicker = null;

        for (Map.Entry<Rank, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 4) {
                quads = entry.getValue();
            } else if (entry.getValue().size() == 1) {
                kicker = entry.getValue().get(0);
            }
        }

        if (quads != null && kicker != null) {
            List<Card> bestFive = new ArrayList<>(quads); // Añadir las 4 cartas del poker.
            bestFive.add(kicker); // Añadir el kicker.
            // El orden (quads primero, luego kicker) es correcto para desempate.
            return new PokerHand(HandRankValue.POKER, bestFive);
        }
        return null;
    }

    private static PokerHand detectFullHouse(List<Card> hand) {
        // Un Full House son 3 cartas de un rango y 2 cartas de otro rango.
        // Precondición: 'hand' está ordenada por rango descendente.
        // La mano de 5 cartas se ordena: [Trio1, Trio2, Trio3, Pair1, Pair2].
        Map<Rank, List<Card>> rankGroups = groupCardsByRank(hand);
        List<Card> trio = null;
        List<Card> pair = null;

        for (List<Card> group : rankGroups.values()) {
            if (group.size() == 3) trio = group;
            else if (group.size() == 2) pair = group;
        }

        if (trio != null && pair != null) {
            List<Card> bestFive = new ArrayList<>(trio); // Añadir el trío.
            bestFive.addAll(pair); // Añadir el par.
            // El orden (trío primero, luego par) es correcto para desempate.
            return new PokerHand(HandRankValue.FULL_HOUSE, bestFive);
        }
        return null;
    }

    private static PokerHand detectFlush(List<Card> hand) {
        // Color (Flush) son 5 cartas del mismo palo, que no forman una escalera.
        // Precondición: 'hand' está ordenada por rango descendente.
        // La mano de 5 cartas se ordena por rango descendente.
        // Si es Escalera de Color o Real, ya se habrían detectado.
        if (isSameSuit(hand)) {
            // Verificar que no es una escalera (si lo fuera, sería Escalera de Color / Real).
            // Esta verificación es implícita porque detectStraightFlush/RoyalFlush se llaman antes.
            // 'hand' ya está ordenada por rango descendente, que es el orden correcto para desempate de Color.
            return new PokerHand(HandRankValue.COLOR, new ArrayList<>(hand));
        }
        return null;
    }

    private static PokerHand detectStraight(List<Card> hand) {
        // Escalera (Straight) son 5 cartas de rangos consecutivos, no todas del mismo palo.
        // Precondición: 'hand' está ordenada por rango descendente.
        // Si es Escalera de Color, ya se habría detectado.
        if (isSameSuit(hand)) return null; // Sería Escalera de Color, ya manejada.

        // Caso especial: Escalera As-bajo (A-2-3-4-5).
        // En 'hand' (ordenada A-5-4-3-2), el As está en hand.get(0).
        boolean isAceLowStraight = hand.get(0).getRank() == Rank.AS &&
                                   hand.get(1).getRank() == Rank.CINCO &&
                                   hand.get(2).getRank() == Rank.CUATRO &&
                                   hand.get(3).getRank() == Rank.TRES &&
                                   hand.get(4).getRank() == Rank.DOS;
        if (isAceLowStraight) {
            // Reordenar para desempate: 5,4,3,2,A. El As actúa como la carta más baja.
            List<Card> aceLowOrder = Arrays.asList(hand.get(1), hand.get(2), hand.get(3), hand.get(4), hand.get(0));
            return new PokerHand(HandRankValue.ESCALERA, aceLowOrder);
        }
        
        // Verificar Escalera normal (ej. 10-9-8-7-6).
        boolean isNormalStraight = true;
        for (int i = 0; i < 4; i++) {
            if (hand.get(i).getRankValue() != hand.get(i+1).getRankValue() + 1) {
                isNormalStraight = false;
                break;
            }
        }
        if (isNormalStraight) {
            // 'hand' ya está ordenada correctamente para desempate (ej. 10,9,8,7,6 o A,K,Q,J,10).
            return new PokerHand(HandRankValue.ESCALERA, new ArrayList<>(hand));
        }
        return null;
    }

    private static PokerHand detectThreeOfAKind(List<Card> hand) {
        // Trío (Three of a Kind) son 3 cartas del mismo rango, y 2 kickers no pareados.
        // Precondición: 'hand' está ordenada por rango descendente.
        // La mano de 5 cartas se ordena: [Trio1, Trio2, Trio3, Kicker1, Kicker2].
        Map<Rank, List<Card>> rankGroups = groupCardsByRank(hand);
        List<Card> trioCards = null;
        List<Card> kickers = new ArrayList<>();

        for (Map.Entry<Rank, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 3) {
                trioCards = entry.getValue();
            } else { // Cartas que no son parte del trío son kickers.
                kickers.addAll(entry.getValue()); 
            }
        }
        
        if (trioCards != null && kickers.size() == 2) { // Debe haber exactamente 2 kickers.
            Collections.sort(kickers, Collections.reverseOrder()); // Ordenar kickers de mayor a menor.
            List<Card> bestFive = new ArrayList<>(trioCards);
            bestFive.addAll(kickers); // Añadir los 2 kickers.
            return new PokerHand(HandRankValue.TRIO, bestFive);
        }
        return null;
    }

    private static PokerHand detectTwoPair(List<Card> hand) {
        // Doble Par (Two Pair) son dos pares de rangos diferentes, y un kicker.
        // Precondición: 'hand' está ordenada por rango descendente.
        // La mano de 5 cartas se ordena: [HighPair1, HighPair2, LowPair1, LowPair2, Kicker].
        Map<Rank, List<Card>> rankGroups = groupCardsByRank(hand);
        List<List<Card>> pairs = new ArrayList<>();
        Card kicker = null;

        for (List<Card> group : rankGroups.values()) {
            if (group.size() == 2) {
                pairs.add(group);
            } else if (group.size() == 1) {
                // Si hay varios kickers, la mano no está ordenada de forma que esto funcione directamente.
                // La mano 'hand' (5 cartas) está ordenada. rankGroups las agrupa.
                // Si hay dos pares, sobra una carta que es el kicker.
                kicker = group.get(0);
            }
        }

        if (pairs.size() == 2 && kicker != null) {
            // Ordenar los pares por rango (el par más alto primero).
            // Si 'hand' está ordenada, el primer par encontrado al iterar rankGroups no es necesariamente el más alto.
            // Por eso, se ordenan explícitamente aquí.
            if (pairs.get(0).get(0).getRankValue() < pairs.get(1).get(0).getRankValue()) {
                Collections.swap(pairs, 0, 1); // Asegurar que el par más alto esté primero.
            }
            
            List<Card> bestFive = new ArrayList<>();
            bestFive.addAll(pairs.get(0)); // Par más alto.
            bestFive.addAll(pairs.get(1)); // Par más bajo.
            bestFive.add(kicker);          // Kicker.
            return new PokerHand(HandRankValue.DOBLE_PAR, bestFive);
        }
        return null;
    }

    private static PokerHand detectOnePair(List<Card> hand) {
        // Par (One Pair) es un par de cartas del mismo rango, y 3 kickers no pareados.
        // Precondición: 'hand' está ordenada por rango descendente.
        // La mano de 5 cartas se ordena: [Pair1, Pair2, Kicker1, Kicker2, Kicker3].
        Map<Rank, List<Card>> rankGroups = groupCardsByRank(hand);
        List<Card> pairCards = null;
        List<Card> kickers = new ArrayList<>();

        for (Map.Entry<Rank, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 2) {
                pairCards = entry.getValue();
            } else if (entry.getValue().size() == 1) { // Cada carta individual es un kicker potencial.
                kickers.add(entry.getValue().get(0));
            }
        }

        if (pairCards != null && kickers.size() == 3) { // Debe haber exactamente 3 kickers.
            Collections.sort(kickers, Collections.reverseOrder()); // Ordenar kickers de mayor a menor.
            List<Card> bestFive = new ArrayList<>(pairCards);
            bestFive.addAll(kickers); // Añadir los 3 kickers.
            return new PokerHand(HandRankValue.PAR, bestFive);
        }
        return null;
    }

    private static PokerHand detectHighCard(List<Card> hand) {
        // Carta Alta (High Card) es cualquier mano que no forme ninguno de los tipos anteriores.
        // Se clasifica por la carta de mayor rango.
        // Precondición: 'hand' está ordenada por rango descendente.
        // 'hand' ya está en el orden correcto para desempate (todas las 5 cartas son "kickers").
        return new PokerHand(HandRankValue.CARTA_ALTA, new ArrayList<>(hand));
    }

    // --- Métodos de utilidad para la evaluación ---

    /**
     * Agrupa las cartas de una mano por su rango.
     * @param hand Lista de cartas (usualmente 5 cartas).
     * @return Un {@code Map} donde la clave es el {@link Rank} y el valor es una {@code List<Card>}
     *         de todas las cartas en la mano que tienen ese rango.
     */
    private static Map<Rank, List<Card>> groupCardsByRank(List<Card> hand) {
        Map<Rank, List<Card>> groups = new HashMap<>();
        for (Card card : hand) {
            groups.computeIfAbsent(card.getRank(), k -> new ArrayList<>()).add(card);
        }
        return groups;
    }

    /**
     * Verifica si todas las cartas en la mano son del mismo palo (es decir, si forman un color).
     * @param hand Lista de 5 cartas.
     * @return {@code true} si todas las cartas son del mismo palo, {@code false} de lo contrario.
     */
    private static boolean isSameSuit(List<Card> hand) {
        if (hand == null || hand.isEmpty()) return false;
        Suit firstSuit = hand.get(0).getSuit();
        for (int i = 1; i < hand.size(); i++) {
            if (hand.get(i).getSuit() != firstSuit) {
                return false;
            }
        }
        return true;
    }
}

/**
 * Gestiona la lógica principal del juego de Poker Texas Hold'em.
 * Incluye el manejo de rondas, apuestas, reparto de cartas y determinación de ganadores.
 * Esta clase es un esqueleto y requeriría una implementación más detallada para un juego funcional.
 */
class GameLogic {
    private List<Player> players;
    private Deck deck;
    private List<Card> communityCards;
    private int mainPot;
    private int currentHighestBetInRound; // La apuesta más alta que se debe igualar en la ronda actual.
    private int currentPlayerTurnIndex;   // Índice del jugador cuyo turno es.
    private int dealerButtonPosition;     // Índice del jugador que tiene el botón de dealer.
    private int smallBlindAmount;         // Cantidad de la ciega pequeña.
    private int bigBlindAmount;           // Cantidad de la ciega grande.


    /**
     * Constructor para la lógica del juego.
     * @param players Lista de {@link Player} que participarán en el juego.
     * @param sbAmount Monto de la ciega pequeña.
     * @param bbAmount Monto de la ciega grande.
     */
    public GameLogic(List<Player> players, int sbAmount, int bbAmount) {
        this.players = new ArrayList<>(players);
        this.deck = new Deck();
        this.communityCards = new ArrayList<>(5);
        this.mainPot = 0;
        this.currentHighestBetInRound = 0;
        this.dealerButtonPosition = -1; // Se asignará al inicio de la primera ronda.
        // TODO: Validar que haya al menos 2 jugadores.
        this.smallBlindAmount = sbAmount;
        this.bigBlindAmount = bbAmount;
    }

    /**
     * Inicia una nueva ronda (o "mano") del juego.
     * Implica recoger cartas de la ronda anterior, barajar el mazo,
     * mover el botón del dealer, asignar ciegas (blinds) y repartir cartas iniciales.
     */
    public void startNewRound() {
        System.out.println("\n--- INICIANDO NUEVA RONDA ---");
        // 1. Limpiar estados de la ronda anterior para jugadores y mesa.
        for (Player player : players) {
            player.clearHand();
            // Solo jugadores con fichas pueden seguir jugando.
            if (player.getChips() > 0) {
                player.setStatus(PlayerStatus.ACTIVO); 
            } else {
                player.setStatus(PlayerStatus.FUERA_DEL_JUEGO);
            }
            player.resetCurrentBetInRound();
        }
        communityCards.clear();
        mainPot = 0;
        currentHighestBetInRound = 0;

        // 2. Barajar el mazo.
        deck.resetAndShuffle();

        // 3. Mover el botón del dealer al siguiente jugador activo.
        // TODO: Implementar una lógica más robusta para encontrar el siguiente jugador activo para el dealer.
        dealerButtonPosition = (dealerButtonPosition + 1) % players.size();
        System.out.println("Botón del Dealer está en: " + players.get(dealerButtonPosition).getName());
        
        // 4. Asignar ciegas (blinds).
        // TODO: Implementar lógica completa de ciegas, incluyendo casos de pocos jugadores o all-ins.
        // Esta es una implementación simplificada.
        Player smallBlindPlayer = players.get((dealerButtonPosition + 1) % players.size());
        Player bigBlindPlayer = players.get((dealerButtonPosition + 2) % players.size());
        
        System.out.println(smallBlindPlayer.getName() + " pone ciega pequeña de " + smallBlindAmount);
        mainPot += smallBlindPlayer.placeBet(smallBlindAmount);
        
        System.out.println(bigBlindPlayer.getName() + " pone ciega grande de " + bigBlindAmount);
        mainPot += bigBlindPlayer.placeBet(bigBlindAmount);
        
        currentHighestBetInRound = bigBlindAmount;
        // El primer jugador en actuar es el que está a la izquierda de la ciega grande.
        currentPlayerTurnIndex = (dealerButtonPosition + 3) % players.size();

        System.out.println("Mazo barajado. Ciega pequeña y grande posteadas.");
        
        // 5. Repartir cartas iniciales (hole cards).
        dealInitialCards();
    }

    /**
     * Reparte las 2 cartas iniciales (hole cards) a cada jugador activo.
     */
    public void dealInitialCards() {
        System.out.println("Repartiendo cartas iniciales...");
        // Se reparten dos "vueltas" de una carta a cada jugador.
        // Empezando por el jugador a la izquierda del dealer (Small Blind).
        int startIndex = (dealerButtonPosition + 1) % players.size();
        for (int i = 0; i < 2; i++) { // Dos cartas por jugador.
            for (int j = 0; j < players.size(); j++) {
                Player currentPlayer = players.get((startIndex + j) % players.size());
                // Solo repartir a jugadores que no están fuera del juego.
                if (currentPlayer.getStatus() != PlayerStatus.FUERA_DEL_JUEGO) { 
                    if (!deck.isEmpty()) {
                        currentPlayer.receiveCard(deck.dealCard());
                    } else {
                        System.err.println("Error: Mazo vacío durante el reparto inicial.");
                        return; // Salir si no hay cartas.
                    }
                }
            }
        }
        // Opcional: Mostrar las manos de los jugadores (solo para depuración).
        // for (Player p : players) {
        //     if (p.getStatus() != PlayerStatus.FUERA_DEL_JUEGO) 
        //         System.out.println(p.getName() + " recibe: " + p.getHand());
        // }
    }

    /**
     * Gestiona una ronda de apuestas (ej. pre-flop, flop, turn, river).
     * Administra turnos, y acciones de los jugadores como apostar, subir, igualar, retirarse.
     * @param bettingRoundName Nombre de la ronda (ej. "Pre-Flop", "Flop") para logging o UI.
     */
    public void conductBettingRound(String bettingRoundName) {
        System.out.println("\n--- Inicio de Ronda de Apuestas: " + bettingRoundName + " ---");
        // TODO: Implementar lógica detallada de la ronda de apuestas.
        // Esta es una sección compleja que debe manejar:
        // - Quién empieza la ronda de apuestas (depende si es pre-flop o post-flop).
        // - Iterar por los jugadores activos permitiéndoles actuar (check, bet, call, raise, fold).
        // - La ronda continúa hasta que todos los jugadores activos hayan igualado la apuesta más alta
        //   o se hayan retirado, y hayan tenido al menos una oportunidad de actuar frente a la última apuesta/subida.
        // - Manejar all-ins y la creación de pozos secundarios (side pots) si es necesario.
        // - Actualizar el pozo principal (mainPot) y la apuesta actual (currentHighestBetInRound).

        // Lógica de placeholder para la plantilla:
        System.out.println("Pozo actual: " + mainPot + ", Apuesta a igualar: " + currentHighestBetInRound);
        int playersToAct = countPlayersInHand(); // Jugadores que aún no se han retirado.
        int actionsThisCycle = 0; // Contador para asegurar que todos actúen una vez por cada apuesta/subida.
        boolean betOrRaiseOccurred = false; // Para saber si alguien subió y se necesita otra vuelta.
        
        // Determinar el primer jugador en actuar para esta ronda.
        // Pre-flop: izquierda de Big Blind. Post-flop: izquierda del Dealer.
        if (bettingRoundName.equals("Pre-Flop")) {
            // currentPlayerTurnIndex ya está seteado después de las ciegas.
        } else { // Flop, Turn, River
            currentPlayerTurnIndex = (dealerButtonPosition + 1) % players.size();
            // Saltar jugadores retirados o all-in que no pueden actuar.
            while(players.get(currentPlayerTurnIndex).getStatus() == PlayerStatus.RETIRADO ||
                  players.get(currentPlayerTurnIndex).getStatus() == PlayerStatus.ALL_IN ||
                  players.get(currentPlayerTurnIndex).getStatus() == PlayerStatus.FUERA_DEL_JUEGO) {
                currentPlayerTurnIndex = (currentPlayerTurnIndex + 1) % players.size();
            }
            currentHighestBetInRound = 0; // En nuevas calles (Flop, Turn, River), la apuesta inicial es 0.
            // Todos los jugadores resetean su currentBetInRound al inicio de la calle (hecho al final de conductBettingRound anterior).
        }

        // Bucle principal de la ronda de apuestas (simplificado).
        // Necesita una lógica más robusta para manejar múltiples subidas y asegurar que todos actúen.
        // La condición de parada es compleja: todos los que no se han retirado han igualado la última apuesta,
        // o están all-in, y todos han tenido la oportunidad de actuar.
        do {
            betOrRaiseOccurred = false;
            actionsThisCycle = 0; // Reset para la nueva "sub-ronda" después de una apuesta/subida.
            int playersCheckedOrCalledThisCycle = 0;

            for (int i = 0; i < players.size(); i++) { // Iterar una vez por todos los jugadores.
                Player currentPlayer = players.get(currentPlayerTurnIndex);
                
                if (currentPlayer.getStatus() == PlayerStatus.ACTIVO || currentPlayer.getStatus() == PlayerStatus.ESPERANDO_ACCION) {
                    System.out.println("Turno de " + currentPlayer.getName() + " (Fichas: " + currentPlayer.getChips() + ")");
                    // Lógica de acción del jugador (simulada aquí, en un juego real sería entrada del usuario o IA).
                    // Ejemplo de acción simulada:
                    if (currentPlayer.getCurrentBetInRound() < currentHighestBetInRound) {
                        // Debe igualar, subir o retirarse.
                        System.out.println(currentPlayer.getName() + " iguala " + (currentHighestBetInRound - currentPlayer.getCurrentBetInRound()));
                        mainPot += currentPlayer.callBet(currentHighestBetInRound); 
                        playersCheckedOrCalledThisCycle++;
                    } else {
                        // Puede pasar (check) o apostar (bet).
                        System.out.println(currentPlayer.getName() + " pasa (check).");
                        currentPlayer.check();
                        playersCheckedOrCalledThisCycle++;
                    }
                    // TODO: Implementar opciones de raise, fold, all-in.
                    // Si hay un raise: currentHighestBetInRound se actualiza, betOrRaiseOccurred = true;
                    //                  playersCheckedOrCalledThisCycle se resetea (o se maneja de otra forma).
                }
                actionsThisCycle++;
                currentPlayerTurnIndex = (currentPlayerTurnIndex + 1) % players.size(); // Avanzar al siguiente jugador.
                if (actionsThisCycle >= playersToAct) break; // Evitar bucles infinitos si algo sale mal.
            }
            // Esta condición de bucle es simplificada. Una real sería más compleja.
            // Debería continuar si hubo una apuesta o subida y no todos han actuado ante ella.
        } while (betOrRaiseOccurred); 

        // Al final de la ronda de apuestas, recoger todas las apuestas en el bote principal.
        // (Esto ya se hace cuando los jugadores apuestan, mainPot se actualiza).
        // Resetear currentBetInRound para cada jugador para la siguiente calle.
        for (Player player : players) {
            player.resetCurrentBetInRound();
        }
        // currentHighestBetInRound se resetea a 0 al inicio de la siguiente calle (Flop, Turn, River).
        // Para Pre-Flop, ya está seteado por la Big Blind.

        System.out.println("--- Fin de Ronda de Apuestas: " + bettingRoundName + ". Pozo total: " + mainPot + " ---");
    }

    /**
     * Reparte el Flop: quema una carta del mazo y luego reparte 3 cartas comunitarias boca arriba.
     */
    public void dealFlop() {
        if (deck.isEmpty() || deck.size() < 4) { // Necesita 1 para quemar + 3 para el flop.
            System.err.println("No hay suficientes cartas para repartir el Flop.");
            return;
        }
        deck.dealCard(); // Quemar una carta.

        if (communityCards.isEmpty()) { // Asegurar que el flop solo se reparte una vez por ronda.
            System.out.print("Flop: ");
            for (int i = 0; i < 3; i++) {
                Card flopCard = deck.dealCard();
                communityCards.add(flopCard);
                System.out.print(flopCard + (i < 2 ? ", " : "\n"));
            }
        }
    }

    /**
     * Reparte el Turn: quema una carta y luego reparte 1 carta comunitaria (la cuarta).
     */
    public void dealTurn() {
        if (deck.isEmpty() || deck.size() < 2) { // Necesita 1 para quemar + 1 para el turn.
             System.err.println("No hay suficientes cartas para repartir el Turn.");
            return;
        }
        deck.dealCard(); // Quemar.

        if (communityCards.size() == 3) { // Asegurar que el Turn se reparte después del Flop.
            Card turnCard = deck.dealCard();
            communityCards.add(turnCard);
            System.out.println("Turn: " + turnCard);
            System.out.println("Cartas Comunitarias actuales: " + communityCards.toString());
        }
    }

    /**
     * Reparte el River: quema una carta y luego reparte 1 carta comunitaria (la quinta y última).
     */
    public void dealRiver() {
        if (deck.isEmpty() || deck.size() < 2) { // Necesita 1 para quemar + 1 para el river.
            System.err.println("No hay suficientes cartas para repartir el River.");
            return;
        }
        deck.dealCard(); // Quemar.

        if (communityCards.size() == 4) { // Asegurar que el River se reparte después del Turn.
            Card riverCard = deck.dealCard();
            communityCards.add(riverCard);
            System.out.println("River: " + riverCard);
            System.out.println("Cartas Comunitarias totales: " + communityCards.toString());
        }
    }

    /**
     * Determina el/los ganador(es) de la mano entre los jugadores que llegaron al showdown (mostrar cartas).
     * Utiliza el {@link HandEvaluator} para cada jugador elegible, compara las {@link PokerHand}
     * y determina el/los ganador(es).
     * @return Una lista de {@link Player} ganadores (puede ser más de uno en caso de empate).
     */
    public List<Player> determineWinner() {
        List<Player> showdownPlayers = new ArrayList<>();
        for (Player player : players) {
            // Solo jugadores que no se han retirado y están en la mano (activos o all-in).
            if (player.getStatus() == PlayerStatus.ACTIVO || player.getStatus() == PlayerStatus.ALL_IN) {
                showdownPlayers.add(player);
            }
        }

        if (showdownPlayers.isEmpty()) {
            System.out.println("No hay jugadores para el showdown.");
            // Esto no debería ocurrir si el juego se maneja correctamente.
            // Si todos se retiraron excepto uno, ese jugador gana el pozo antes del showdown.
            // TODO: Manejar el caso de un solo jugador restante antes de llegar al showdown formal.
            return Collections.emptyList();
        }
        
        if (showdownPlayers.size() == 1) {
            // Si solo un jugador llega al showdown (porque otros se retiraron), ese jugador gana.
            System.out.println("Ganador por ser el único restante: " + showdownPlayers.get(0).getName());
            return showdownPlayers; 
        }

        System.out.println("\n--- SHOWDOWN ---");
        if (!communityCards.isEmpty()) {
             System.out.println("Cartas comunitarias: " + communityCards.stream().map(Card::toString).collect(Collectors.joining(", ")));
        }
        
        PokerHand bestOverallHand = null;
        List<Player> winners = new ArrayList<>();

        for (Player player : showdownPlayers) {
            System.out.println(player.getName() + " muestra: " + player.getHand().stream().map(Card::toString).collect(Collectors.joining(", ")));
            PokerHand playerBestHand = HandEvaluator.evaluateBestHand(player.getHand(), communityCards);
            
            if (playerBestHand == null) {
                System.out.println(player.getName() + " no pudo formar una mano (error o datos insuficientes).");
                continue; 
            }
            System.out.println(player.getName() + " tiene: " + playerBestHand);

            if (bestOverallHand == null || playerBestHand.compareTo(bestOverallHand) > 0) {
                bestOverallHand = playerBestHand;
                winners.clear();
                winners.add(player);
            } else if (playerBestHand.compareTo(bestOverallHand) == 0) {
                // Empate entre esta mano y la mejor mano actual.
                winners.add(player);
            }
        }
        
        if (!winners.isEmpty()) {
             System.out.println("\nMejor mano de la ronda: " + bestOverallHand);
             System.out.print("Ganador(es) de la ronda: ");
             System.out.println(winners.stream().map(Player::getName).collect(Collectors.joining(", ")));
        } else {
            System.out.println("No se pudo determinar un ganador claro en el showdown.");
        }
        return winners;
    }

    /**
     * Distribuye el pozo principal (y pozos secundarios, si se implementan) entre los ganadores.
     * @param winners La lista de {@link Player} que ganaron la mano.
     */
    public void distributePot(List<Player> winners) {
        if (winners == null || winners.isEmpty()) {
            // Si no hay ganadores (ej. todos se retiraron y el pozo fue reclamado antes), no hay nada que distribuir aquí.
            // O si determineWinner() falló por alguna razón.
            System.out.println("No hay ganadores para distribuir el pozo.");
            return;
        }

        // TODO: Implementar lógica para pozos secundarios (side pots).
        // Esta es una simplificación para un solo pozo principal (mainPot).
        // La lógica de side pots es compleja y requiere rastrear cuánto apostó cada jugador
        // y quién es elegible para cada parte del pozo total.

        if (mainPot > 0) {
            int sharePerWinner = mainPot / winners.size();
            System.out.println("\nDistribuyendo pozo de " + mainPot + " fichas entre " + winners.size() + " ganador(es).");
            for (Player winner : winners) {
                winner.addChips(sharePerWinner);
                System.out.println(winner.getName() + " recibe " + sharePerWinner + " fichas. Nuevo total: " + winner.getChips());
            }
            
            // Manejar fichas sobrantes por división impar (si las hay).
            // Usualmente se dan al primer ganador a la izquierda del botón de dealer.
            int remainderChips = mainPot % winners.size();
            if (remainderChips > 0 && !winners.isEmpty()) {
                // TODO: Distribuir el resto de forma justa según las reglas del poker (ej. al jugador más cercano al dealer).
                // Por simplicidad, se lo damos al primer ganador en la lista de ganadores.
                winners.get(0).addChips(remainderChips);
                 System.out.println(winners.get(0).getName() + " recibe " + remainderChips + " fichas adicionales (resto de la división).");
            }
            mainPot = 0; // El pozo principal ha sido distribuido.
        }
    }
    
    /**
     * Método de ejemplo para simular el flujo de una mano completa de Texas Hold'em.
     * Esto es un esqueleto y necesitaría ser llamado en un bucle para un juego continuo,
     * con más interacción y lógica de estado del juego.
     */
    public void playHandCycle() {
        startNewRound(); // Incluye reparto inicial de cartas.

        if (countPlayersInHand() < 2 && communityCards.isEmpty()) { // Menos de 2 jugadores para iniciar pre-flop.
            System.out.println("No hay suficientes jugadores para continuar la mano.");
            // TODO: Determinar quién se lleva las ciegas si la mano no puede empezar.
            return;
        }

        // Ronda de apuestas Pre-Flop
        conductBettingRound("Pre-Flop");
        if (resolveHandIfFoldToWinner()) return; // Si todos menos uno se retiraron.

        // Flop
        dealFlop();
        conductBettingRound("Flop");
        if (resolveHandIfFoldToWinner()) return;

        // Turn
        dealTurn();
        conductBettingRound("Turn");
        if (resolveHandIfFoldToWinner()) return;

        // River
        dealRiver();
        conductBettingRound("River");
        // No es necesario chequear resolveHandIfFoldToWinner() aquí,
        // ya que si se llega al showdown, determineWinner() lo manejará.

        // Showdown y distribución del pozo
        List<Player> winners = determineWinner();
        distributePot(winners);
    }

    /**
     * Auxiliar para verificar si la mano termina porque todos menos uno se retiraron.
     * Si es así, distribuye el pozo al ganador y retorna true.
     * @return true si la mano terminó por retiradas, false si continúa.
     */
    private boolean resolveHandIfFoldToWinner() {
        List<Player> activePlayers = new ArrayList<>();
        for (Player p : players) {
            if (p.getStatus() == PlayerStatus.ACTIVO || p.getStatus() == PlayerStatus.ALL_IN) {
                activePlayers.add(p);
            }
        }
        if (activePlayers.size() == 1) {
            System.out.println(activePlayers.get(0).getName() + " gana el pozo de " + mainPot + " porque todos los demás se retiraron.");
            distributePot(activePlayers);
            return true;
        }
        return false;
    }
    
    /**
     * Cuenta cuántos jugadores siguen en la mano (no retirados ni fuera del juego).
     * @return Número de jugadores activos o all-in.
     */
    private int countPlayersInHand() {
        int count = 0;
        for (Player p : players) {
            if (p.getStatus() == PlayerStatus.ACTIVO || p.getStatus() == PlayerStatus.ALL_IN) {
                count++;
            }
        }
        return count;
    }

    // Getters para el estado del juego (podrían ser necesarios para una UI o pruebas)
    public List<Card> getCommunityCards() { return Collections.unmodifiableList(communityCards); }
    public int getMainPot() { return mainPot; }


    /**
     * Método main de ejemplo para demostrar el uso de las clases y la lógica del juego.
     * Crea jugadores, inicia la lógica del juego y simula una mano.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Crear jugadores para la partida.
        Player player1 = new Player("Alice", 1000);
        Player player2 = new Player("Bob", 1000);
        Player player3 = new Player("Charlie", 1000);

        List<Player> gamePlayers = new ArrayList<>(Arrays.asList(player1, player2, player3));

        // Configurar e iniciar la lógica del juego.
        int smallBlind = 10;
        int bigBlind = 20;
        GameLogic game = new GameLogic(gamePlayers, smallBlind, bigBlind);
        
        // Simular una mano del juego.
        // En un juego real, esto estaría en un bucle, y las acciones de los jugadores
        // provendrían de una interfaz de usuario o de una IA.
        game.playHandCycle();
        
        // Se podría llamar a game.playHandCycle() repetidamente para jugar múltiples manos.

        System.out.println("\n--- Fin de la simulación de ejemplo ---");
        System.out.println("Estado final de las fichas de los jugadores:");
        for (Player p : gamePlayers) {
            System.out.println(p.getName() + ": " + p.getChips() + " fichas.");
        }
    }
}
// Fin del archivo Java único.
