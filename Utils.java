import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * PokerUtils es una clase de utilidad que proporciona funciones para manejar
 * la lógica fundamental del juego de Poker Texas Hold'em.
 * Incluye funcionalidades para la representación de cartas, manejo del mazo,
 * evaluación y comparación de manos de poker.
 * Esta clase no puede ser instanciada.
 */
public final class PokerUtils {

    private PokerUtils() {
        // Clase de utilidad, no instanciable
    }

    // --- A. Representación de Cartas y Constantes ---

    /**
     * Representa los palos de una baraja de cartas.
     */
    public static enum Suit {
        HEARTS('♥'), DIAMONDS('♦'), CLUBS('♣'), SPADES('♠');

        private final char symbol;

        Suit(char symbol) {
            this.symbol = symbol;
        }

        /**
         * Obtiene el símbolo Unicode del palo.
         * @return El símbolo del palo.
         */
        public char getSymbol() {
            return symbol;
        }
    }

    /**
     * Representa los rangos de las cartas en una baraja.
     * El As (ACE) tiene el valor más alto (14) por defecto.
     */
    public static enum Rank {
        TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"), FIVE(5, "5"), SIX(6, "6"),
        SEVEN(7, "7"), EIGHT(8, "8"), NINE(9, "9"), TEN(10, "T"),
        JACK(11, "J"), QUEEN(12, "Q"), KING(13, "K"), ACE(14, "A");

        private final int value;
        private final String shortName;

        Rank(int value, String shortName) {
            this.value = value;
            this.shortName = shortName;
        }

        /**
         * Obtiene el valor numérico del rango.
         * (TWO=2, ..., KING=13, ACE=14).
         * @return El valor numérico del rango.
         */
        public int getValue() {
            return value;
        }

        /**
         * Obtiene la representación corta del rango (ej: 'A', 'K', 'T').
         * @return La representación corta del rango.
         */
        public String getShortName() {
            return shortName;
        }
    }

    /**
     * Representa una carta de poker, compuesta por un rango y un palo.
     * Esta clase es inmutable (al ser un record).
     *
     * @param rank El rango de la carta.
     * @param suit El palo de la carta.
     */
    public static record Card(Rank rank, Suit suit) implements Comparable<Card> {
        /**
         * Retorna una representación de cadena de la carta (ej: "A♥").
         * @return String representando la carta.
         */
        @Override
        public String toString() {
            return rank.getShortName() + suit.getSymbol();
        }

        /**
         * Compara esta carta con otra carta, primariamente por rango (descendente).
         * Este método es útil para ordenar listas de cartas.
         * @param other La otra carta a comparar.
         * @return un entero negativo, cero, o un entero positivo si el valor de rango de esta carta es
         *         mayor que, igual a, o menor que el de la carta especificada. (Orden descendente por valor de rango)
         */
        @Override
        public int compareTo(Card other) {
            // Para ordenar en forma descendente por valor de rango
            return Integer.compare(other.rank.getValue(), this.rank.getValue());
        }
    }

    // --- C. Evaluación de Manos (Definiciones de Enum y Record) ---
    // Se definen aquí para que estén disponibles para los métodos de evaluación.

    /**
     * Representa los posibles tipos de manos de poker, ordenados de la mejor a la peor.
     * El orden natural (ordinal) de estos enums se usa para la comparación.
     */
    public static enum HandRank {
        ROYAL_FLUSH,      // Escalera Real
        STRAIGHT_FLUSH,   // Escalera de Color
        FOUR_OF_A_KIND,   // Poker
        FULL_HOUSE,       // Full House
        FLUSH,            // Color
        STRAIGHT,         // Escalera
        THREE_OF_A_KIND,  // Trío
        TWO_PAIR,         // Doble Par
        ONE_PAIR,         // Par
        HIGH_CARD         // Carta Alta
    }

    /**
     * Representa el resultado de la evaluación de una mano de poker.
     * Esta clase es inmutable (al ser un record y contener una lista inmutable).
     *
     * @param handRank El tipo de mano (ej: FULL_HOUSE).
     * @param kickerRanks Lista de los rangos de las 5 cartas que componen la mano,
     *                    ordenadas de forma que permitan el desempate.
     *                    Por ejemplo, para un par de Ases con kickers K, Q, J,
     *                    la lista sería [ACE, ACE, KING, QUEEN, JACK].
     *                    Esta lista es una copia inmutable.
     */
    public static record HandEvaluationResult(HandRank handRank, List<Rank> kickerRanks)
        implements Comparable<HandEvaluationResult> {

        /**
         * Constructor canónico para HandEvaluationResult.
         * Asegura que kickerRanks sea una copia inmutable.
         * @param handRank El tipo de mano.
         * @param kickerRanks La lista de rangos para desempate.
         */
        public HandEvaluationResult {
            Objects.requireNonNull(handRank, "handRank no puede ser nulo.");
            Objects.requireNonNull(kickerRanks, "kickerRanks no puede ser nulo.");
            if (kickerRanks.size() != 5) {
                 throw new IllegalArgumentException("kickerRanks debe contener exactamente 5 rangos.");
            }
            // Asegurar inmutabilidad de la lista de kickers
            this.kickerRanks = List.copyOf(kickerRanks);
        }
        
        /**
         * Compara este resultado de evaluación con otro.
         * Primero compara por HandRank. Si son iguales, compara los kickerRanks.
         * @param other El otro HandEvaluationResult a comparar.
         * @return 1 si esta mano es mejor, -1 si la otra es mejor, 0 si son iguales.
         */
        @Override
        public int compareTo(HandEvaluationResult other) {
            return PokerUtils.compareHands(this, other);
        }

        /**
         * Retorna una representación de cadena del resultado de la evaluación.
         * @return String representando el resultado.
         */
        @Override
        public String toString() {
            return "HandRank: " + handRank + ", Kickers: " + 
                   kickerRanks.stream().map(Rank::getShortName).collect(Collectors.joining(", "));
        }
    }


    // --- B. Métodos del Mazo ---

    /**
     * Crea un mazo estándar de 52 cartas de poker.
     * El mazo retornado no está barajado.
     *
     * @return Una lista de 52 objetos {@link Card}.
     */
    public static List<Card> createDeck() {
        List<Card> deck = new ArrayList<>(52);
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        return deck;
    }

    /**
     * Baraja un mazo de cartas "in-place".
     * Modifica la lista original.
     *
     * @param deck La lista de {@link Card} a barajar. No debe ser nula.
     */
    public static void shuffleDeck(List<Card> deck) {
        Objects.requireNonNull(deck, "El mazo no puede ser nulo.");
        Collections.shuffle(deck);
    }

    /**
     * Retira y retorna la carta superior del mazo.
     * Convencionalmente, la carta "superior" es la última de la lista si se usa `remove(deck.size() - 1)`.
     * Modifica el mazo original.
     *
     * @param deck El mazo del cual repartir una carta. No debe ser nulo.
     * @return La {@link Card} retirada del mazo.
     * @throws NoSuchElementException si el mazo está vacío.
     */
    public static Card dealCard(List<Card> deck) {
        Objects.requireNonNull(deck, "El mazo no puede ser nulo.");
        if (deck.isEmpty()) {
            throw new NoSuchElementException("No se pueden repartir cartas de un mazo vacío.");
        }
        return deck.remove(deck.size() - 1);
    }


    // --- C. Evaluación de Manos (Método Principal) ---

    /**
     * Evalúa la mejor mano de poker de 5 cartas posible a partir de las cartas del jugador
     * y las cartas comunitarias.
     *
     * @param playerCards   Una lista de 2 cartas en mano del jugador. No debe ser nula y debe contener 2 cartas.
     * @param communityCards Una lista de 3 a 5 cartas comunitarias (Flop, Turn, o River). No debe ser nula y debe contener de 3 a 5 cartas.
     * @return El {@link HandEvaluationResult} de la mejor mano de 5 cartas encontrada.
     * @throws IllegalArgumentException si las entradas no cumplen los requisitos de tamaño.
     */
    public static HandEvaluationResult evaluateHand(List<Card> playerCards, List<Card> communityCards) {
        Objects.requireNonNull(playerCards, "playerCards no puede ser nulo.");
        Objects.requireNonNull(communityCards, "communityCards no puede ser nulo.");

        if (playerCards.size() != 2) {
            throw new IllegalArgumentException("El jugador debe tener exactamente 2 cartas.");
        }
        if (communityCards.size() < 3 || communityCards.size() > 5) {
            throw new IllegalArgumentException("Debe haber entre 3 y 5 cartas comunitarias.");
        }

        List<Card> allCards = new ArrayList<>(playerCards.size() + communityCards.size());
        allCards.addAll(playerCards);
        allCards.addAll(communityCards);

        // En Texas Hold'em, el número total de cartas (allCards.size()) será 5, 6 o 7.
        // Siempre se pueden formar manos de 5 cartas.
        // Por ejemplo, si allCards.size() es 5 (2 player + 3 flop), C(5,5) = 1 combinación.
        // Si allCards.size() es 7 (2 player + 5 river), C(7,5) = 21 combinaciones.

        List<List<Card>> fiveCardCombinations = new ArrayList<>();
        // El método generateCombinations espera una List<Card> no modificable internamente (o que no le importe la modificación)
        // y llena `fiveCardCombinations`.
        generateCombinationsRecursive(allCards, 5, 0, new ArrayList<>(), fiveCardCombinations);

        HandEvaluationResult bestHandResult = null;

        for (List<Card> fiveCardHand : fiveCardCombinations) {
            HandEvaluationResult currentHandResult = evaluateFiveCardHand(fiveCardHand);
            if (bestHandResult == null || currentHandResult.compareTo(bestHandResult) > 0) {
                bestHandResult = currentHandResult;
            }
        }
        return bestHandResult;
    }

    /**
     * Genera todas las combinaciones de k cartas a partir de una lista de cartas.
     * Método auxiliar recursivo.
     *
     * @param allCards Lista de cartas de la cual generar combinaciones.
     * @param k Tamaño de las combinaciones a generar (debe ser 5 para poker).
     * @param startPosition Índice de inicio para la selección de cartas en la recursión.
     * @param currentCombination Combinación actual en construcción.
     * @param allCombinations Lista para almacenar todas las combinaciones generadas.
     */
    private static void generateCombinationsRecursive(List<Card> allCards, int k, int startPosition,
                                                      List<Card> currentCombination, List<List<Card>> allCombinations) {
        // Si la combinación actual tiene k cartas, es una combinación completa.
        if (currentCombination.size() == k) {
            allCombinations.add(new ArrayList<>(currentCombination)); // Añadir una copia
            return;
        }

        // Si nos quedamos sin cartas para elegir (startPosition llega al final de allCards)
        if (startPosition >= allCards.size()) {
            return;
        }
        
        // Optimización: si las cartas restantes no son suficientes para completar k elementos
        // (allCards.size() - startPosition) < (k - currentCombination.size())
        if ((allCards.size() - startPosition) < (k - currentCombination.size())) {
            return;
        }

        // Opción 1: Incluir la carta en la posición startPosition
        currentCombination.add(allCards.get(startPosition));
        generateCombinationsRecursive(allCards, k, startPosition + 1, currentCombination, allCombinations);

        // Opción 2: No incluir la carta en la posición startPosition (backtrack)
        currentCombination.remove(currentCombination.size() - 1);
        generateCombinationsRecursive(allCards, k, startPosition + 1, currentCombination, allCombinations);
    }

    /**
     * Evalúa una mano específica de 5 cartas.
     * Este es un método interno llamado por {@link #evaluateHand(List, List)}.
     *
     * @param fiveCardHand Una lista de exactamente 5 cartas.
     * @return El {@link HandEvaluationResult} para esta mano de 5 cartas.
     */
    private static HandEvaluationResult evaluateFiveCardHand(List<Card> fiveCardHand) {
        // Ordenar las cartas por rango descendente para facilitar la evaluación.
        // Se crea una copia para no modificar la lista original de la combinación.
        List<Card> sortedHand = new ArrayList<>(fiveCardHand);
        Collections.sort(sortedHand); // Usa Card.compareTo, que ordena por rango descendente

        List<Rank> kickerRanks; // Lista de rangos para desempate

        // Verificar en orden de la mano más fuerte a la más débil
        if ((kickerRanks = checkRoyalFlush(sortedHand)) != null) {
            return new HandEvaluationResult(HandRank.ROYAL_FLUSH, kickerRanks);
        }
        if ((kickerRanks = checkStraightFlush(sortedHand)) != null) {
            return new HandEvaluationResult(HandRank.STRAIGHT_FLUSH, kickerRanks);
        }
        if ((kickerRanks = checkFourOfAKind(sortedHand)) != null) {
            return new HandEvaluationResult(HandRank.FOUR_OF_A_KIND, kickerRanks);
        }
        if ((kickerRanks = checkFullHouse(sortedHand)) != null) {
            return new HandEvaluationResult(HandRank.FULL_HOUSE, kickerRanks);
        }
        if ((kickerRanks = checkFlush(sortedHand)) != null) {
            return new HandEvaluationResult(HandRank.FLUSH, kickerRanks);
        }
        if ((kickerRanks = checkStraight(sortedHand)) != null) {
            return new HandEvaluationResult(HandRank.STRAIGHT, kickerRanks);
        }
        if ((kickerRanks = checkThreeOfAKind(sortedHand)) != null) {
            return new HandEvaluationResult(HandRank.THREE_OF_A_KIND, kickerRanks);
        }
        if ((kickerRanks = checkTwoPair(sortedHand)) != null) {
            return new HandEvaluationResult(HandRank.TWO_PAIR, kickerRanks);
        }
        if ((kickerRanks = checkOnePair(sortedHand)) != null) {
            return new HandEvaluationResult(HandRank.ONE_PAIR, kickerRanks);
        }
        
        // Si ninguna de las anteriores, es Carta Alta
        kickerRanks = getHighCardKickers(sortedHand);
        return new HandEvaluationResult(HandRank.HIGH_CARD, kickerRanks);
    }

    // --- Métodos Auxiliares de Evaluación de Manos de 5 Cartas ---
    // Estos métodos toman una List<Card> de 5 cartas, YA ORDENADA POR RANGO DESCENDENTE.
    // Retornan List<Rank> de 5 elementos para kickerRanks, o null si la mano no es del tipo evaluado.

    /** Chequea si la mano es Escalera Real. Devuelve lista de rangos [A,K,Q,J,T] o null. */
    private static List<Rank> checkRoyalFlush(List<Card> sortedHand) {
        // Escalera Real: A, K, Q, J, 10 del mismo palo.
        // La mano ya está ordenada por rango desc: A, K, Q, J, T
        boolean isFlush = isFlushInternal(sortedHand);
        if (!isFlush) return null;

        boolean isAceHighStraight = sortedHand.get(0).rank() == Rank.ACE &&
                                    sortedHand.get(1).rank() == Rank.KING &&
                                    sortedHand.get(2).rank() == Rank.QUEEN &&
                                    sortedHand.get(3).rank() == Rank.JACK &&
                                    sortedHand.get(4).rank() == Rank.TEN;
        
        if (isAceHighStraight) {
            return sortedHand.stream().map(Card::rank).collect(Collectors.toList());
        }
        return null;
    }
    
    /** Chequea si la mano es Escalera de Color. Devuelve lista de rangos de la escalera o null. */
    private static List<Rank> checkStraightFlush(List<Card> sortedHand) {
        boolean isFlush = isFlushInternal(sortedHand);
        if (!isFlush) return null;
        
        // Si es flush, verificar si también es escalera.
        // checkStraight ya devuelve los rangos ordenados correctamente para la escalera (incluyendo A-5).
        return checkStraight(sortedHand); // Reutiliza la lógica de escalera
    }

    /** Genera un mapa de conteo de rangos para una mano. */
    private static Map<Rank, Integer> getRankCounts(List<Card> hand) {
        Map<Rank, Integer> counts = new EnumMap<>(Rank.class); // EnumMap es eficiente para claves Enum
        for (Card card : hand) {
            counts.put(card.rank(), counts.getOrDefault(card.rank(), 0) + 1);
        }
        return counts;
    }

    /** Chequea si la mano es Poker (Four of a Kind). Devuelve [R,R,R,R,Kicker] o null. */
    private static List<Rank> checkFourOfAKind(List<Card> sortedHand) {
        Map<Rank, Integer> counts = getRankCounts(sortedHand);
        Rank quadRank = null;
        Rank kickerRank = null;

        for (Map.Entry<Rank, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == 4) {
                quadRank = entry.getKey();
            } else if (entry.getValue() == 1) { // El kicker
                kickerRank = entry.getKey();
            }
        }

        if (quadRank != null && kickerRank != null) {
            List<Rank> kickerRanks = new ArrayList<>(5);
            for(int i=0; i<4; ++i) kickerRanks.add(quadRank);
            kickerRanks.add(kickerRank);
            return kickerRanks;
        }
        return null;
    }

    /** Chequea si la mano es Full House. Devuelve [T,T,T,P,P] o null. */
    private static List<Rank> checkFullHouse(List<Card> sortedHand) {
        Map<Rank, Integer> counts = getRankCounts(sortedHand);
        // Full House tiene exactamente dos rangos distintos (un trío y un par)
        if (counts.size() != 2) return null; 

        Rank trioRank = null;
        Rank pairRank = null;

        for (Map.Entry<Rank, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == 3) {
                trioRank = entry.getKey();
            } else if (entry.getValue() == 2) {
                pairRank = entry.getKey();
            }
        }

        if (trioRank != null && pairRank != null) {
            List<Rank> kickerRanks = new ArrayList<>(5);
            for(int i=0; i<3; ++i) kickerRanks.add(trioRank);
            for(int i=0; i<2; ++i) kickerRanks.add(pairRank);
            return kickerRanks;
        }
        return null;
    }
    
    /** Helper interno para verificar si 5 cartas son del mismo palo. */
    private static boolean isFlushInternal(List<Card> hand) {
        // Asume que la mano tiene 5 cartas.
        Suit firstSuit = hand.get(0).suit();
        for (int i = 1; i < 5; i++) {
            if (hand.get(i).suit() != firstSuit) {
                return false;
            }
        }
        return true;
    }

    /** Chequea si la mano es Color (Flush). Devuelve [R1,R2,R3,R4,R5] ordenados o null. */
    private static List<Rank> checkFlush(List<Card> sortedHand) {
        if (isFlushInternal(sortedHand)) {
            // Para un flush, los "kickers" son simplemente los rangos de las cartas, ordenados.
            // Como sortedHand ya está ordenada por rango descendente, esto es directo.
            return sortedHand.stream().map(Card::rank).collect(Collectors.toList());
        }
        return null;
    }

    /** Chequea si la mano es Escalera (Straight). Devuelve [R_alto,...,R_bajo] o null. Maneja A-5. */
    private static List<Rank> checkStraight(List<Card> sortedHand) {
        // sortedHand está ordenada por rango descendente (ej: A, K, Q, J, T o 5, 4, 3, 2, A).
        
        // Caso especial: A-2-3-4-5 (Steel Wheel)
        // sortedHand estaría como [ACE, FIVE, FOUR, THREE, TWO] debido al ordenamiento por valor.
        boolean isAceLowStraight = sortedHand.get(0).rank() == Rank.ACE &&
                                   sortedHand.get(1).rank() == Rank.FIVE &&
                                   sortedHand.get(2).rank() == Rank.FOUR &&
                                   sortedHand.get(3).rank() == Rank.THREE &&
                                   sortedHand.get(4).rank() == Rank.TWO;
        if (isAceLowStraight) {
            // Para A-2-3-4-5, el As se trata como bajo. Los rangos para desempate son [FIVE, FOUR, THREE, TWO, ACE].
            return Arrays.asList(Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO, Rank.ACE);
        }

        // Caso general de escalera (ej: K-Q-J-T-9)
        boolean isNormalStraight = true;
        for (int i = 0; i < 4; i++) {
            // Verifica que el valor del rango de la carta actual sea uno más que el de la siguiente.
            if (sortedHand.get(i).rank().getValue() - sortedHand.get(i+1).rank().getValue() != 1) {
                isNormalStraight = false;
                break;
            }
        }

        if (isNormalStraight) {
            return sortedHand.stream().map(Card::rank).collect(Collectors.toList());
        }
        
        return null; // No es ninguna forma de escalera
    }

    /** Chequea si la mano es Trío (Three of a Kind). Devuelve [T,T,T,K1,K2] o null. */
    private static List<Rank> checkThreeOfAKind(List<Card> sortedHand) {
        Map<Rank, Integer> counts = getRankCounts(sortedHand);
        // Un trío debe tener 3 rangos distintos: el trío y dos kickers.
        // O 2 rangos distintos si los dos kickers forman un par (esto sería un Full House, ya chequeado).
        // Si hay 3 rangos distintos (AAA KQ), counts.size() == 3.
        if (counts.size() != 3) return null;

        Rank trioRank = null;
        List<Rank> kickers = new ArrayList<>(); // Para los dos kickers individuales

        for (Map.Entry<Rank, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == 3) {
                trioRank = entry.getKey();
            } else if (entry.getValue() == 1) { // Los kickers deben ser individuales
                kickers.add(entry.getKey());
            }
            // Si entry.getValue() == 2, sería un Full House (3+2), ya manejado.
            // Si entry.getValue() > 3, sería Four of a Kind, ya manejado.
        }

        if (trioRank != null && kickers.size() == 2) {
            // Ordenar kickers por valor descendente
            kickers.sort(Comparator.comparingInt(Rank::getValue).reversed());
            
            List<Rank> kickerRanks = new ArrayList<>(5);
            for(int i=0; i<3; ++i) kickerRanks.add(trioRank);
            kickerRanks.addAll(kickers); // Añade los dos kickers ordenados
            return kickerRanks;
        }
        return null;
    }

    /** Chequea si la mano es Doble Par (Two Pair). Devuelve [P1,P1,P2,P2,K] o null. */
    private static List<Rank> checkTwoPair(List<Card> sortedHand) {
        Map<Rank, Integer> counts = getRankCounts(sortedHand);
        // Dos pares y un kicker significan 3 rangos distintos.
        if (counts.size() != 3) return null; 

        List<Rank> pairRanks = new ArrayList<>(); // Para los rangos de los dos pares
        Rank kickerRank = null;                   // Para el kicker individual

        for (Map.Entry<Rank, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == 2) {
                pairRanks.add(entry.getKey());
            } else if (entry.getValue() == 1) {
                kickerRank = entry.getKey();
            }
            // Si getValue() es 3, sería Full House (ya chequeado) o Trío.
        }

        if (pairRanks.size() == 2 && kickerRank != null) {
            // Ordenar los rangos de los pares (el par más alto primero)
            pairRanks.sort(Comparator.comparingInt(Rank::getValue).reversed());
            
            List<Rank> kickerRanks = new ArrayList<>(5);
            kickerRanks.add(pairRanks.get(0)); // Par más alto
            kickerRanks.add(pairRanks.get(0));
            kickerRanks.add(pairRanks.get(1)); // Par más bajo
            kickerRanks.add(pairRanks.get(1));
            kickerRanks.add(kickerRank);       // Kicker
            return kickerRanks;
        }
        return null;
    }

    /** Chequea si la mano es Par (One Pair). Devuelve [P,P,K1,K2,K3] o null. */
    private static List<Rank> checkOnePair(List<Card> sortedHand) {
        Map<Rank, Integer> counts = getRankCounts(sortedHand);
        // Un par y 3 kickers individuales significan 4 rangos distintos.
        if (counts.size() != 4) return null; 

        Rank pairRank = null;             // Rango del par
        List<Rank> kickers = new ArrayList<>(); // Para los 3 kickers individuales

        for (Map.Entry<Rank, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == 2) {
                pairRank = entry.getKey();
            } else if (entry.getValue() == 1) { // Kickers individuales
                kickers.add(entry.getKey());
            }
            // Otros casos (3 de un tipo, etc.) ya habrían sido capturados.
        }

        if (pairRank != null && kickers.size() == 3) {
            // Ordenar kickers por valor descendente
            kickers.sort(Comparator.comparingInt(Rank::getValue).reversed());
            
            List<Rank> kickerRanks = new ArrayList<>(5);
            kickerRanks.add(pairRank); // Rango del par
            kickerRanks.add(pairRank);
            kickerRanks.addAll(kickers); // Añade los 3 kickers ordenados
            return kickerRanks;
        }
        return null;
    }

    /** Obtiene los rangos para una mano de Carta Alta. Devuelve [R1,R2,R3,R4,R5] ordenados. */
    private static List<Rank> getHighCardKickers(List<Card> sortedHand) {
        // Para Carta Alta, los "kickers" son simplemente los rangos de las 5 cartas, ordenados.
        // sortedHand ya está ordenada por rango descendente.
        return sortedHand.stream().map(Card::rank).collect(Collectors.toList());
    }


    // --- D. Comparación de Manos ---

    /**
     * Compara dos resultados de evaluación de manos para determinar cuál es mejor.
     *
     * @param eval1 El resultado de la primera mano. No debe ser nulo.
     * @param eval2 El resultado de la segunda mano. No debe ser nulo.
     * @return  <code>1</code> si <code>eval1</code> es mejor que <code>eval2</code>,
     *          <code>-1</code> si <code>eval2</code> es mejor que <code>eval1</code>,
     *          <code>0</code> si son un empate.
     */
    public static int compareHands(HandEvaluationResult eval1, HandEvaluationResult eval2) {
        Objects.requireNonNull(eval1, "eval1 no puede ser nulo.");
        Objects.requireNonNull(eval2, "eval2 no puede ser nulo.");

        // Comparar por HandRank (ordinal más bajo es mejor mano, ya que están definidos de mejor a peor)
        if (eval1.handRank().ordinal() < eval2.handRank().ordinal()) {
            return 1; // eval1 es mejor
        }
        if (eval1.handRank().ordinal() > eval2.handRank().ordinal()) {
            return -1; // eval2 es mejor
        }

        // Si HandRank es el mismo, comparar kickerRanks.
        // kickerRanks es una lista de 5 rangos, ordenados de forma específica para cada tipo de mano,
        // de manera que una comparación lexicográfica directa de los valores de los rangos funcione.
        List<Rank> kickers1 = eval1.kickerRanks();
        List<Rank> kickers2 = eval2.kickerRanks();

        for (int i = 0; i < kickers1.size(); i++) { // Ambas listas tienen 5 elementos
            // Comparar por valor de Rank (getValue() devuelve el valor numérico)
            if (kickers1.get(i).getValue() > kickers2.get(i).getValue()) {
                return 1; // kicker en esta posición de eval1 es mejor, entonces eval1 es mejor
            }
            if (kickers1.get(i).getValue() < kickers2.get(i).getValue()) {
                return -1; // kicker en esta posición de eval2 es mejor, entonces eval2 es mejor
            }
        }

        return 0; // Empate perfecto (mismo HandRank y mismos kickerRanks)
    }
    
    /*
    // --- Ejemplo de uso (opcional, puede ser eliminado o movido a una clase Main) ---
    public static void main(String[] args) {
        // Ejemplo de creación y barajado de mazo
        List<Card> deck = createDeck();
        System.out.println("Mazo creado (" + deck.size() + " cartas): " + deck.subList(0, Math.min(deck.size(), 10)) + "...");
        shuffleDeck(deck);
        System.out.println("Mazo barajado: " + deck.subList(0, Math.min(deck.size(), 10)) + "...");

        // Ejemplo de reparto de cartas
        System.out.println("\nRepartiendo 5 cartas:");
        List<Card> handForShow = new ArrayList<>();
        if (deck.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                handForShow.add(dealCard(deck));
            }
            System.out.println("Cartas repartidas: " + handForShow);
        } else {
            System.out.println("No hay suficientes cartas para repartir 5.");
        }
        System.out.println(deck.size() + " cartas restantes en el mazo.");

        // Ejemplo de evaluación de mano
        System.out.println("\n--- Ejemplo de Evaluación de Mano ---");
        List<Card> playerCards = Arrays.asList(new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.KING, Suit.HEARTS));
        List<Card> communityCards = Arrays.asList(
            new Card(Rank.QUEEN, Suit.HEARTS),
            new Card(Rank.JACK, Suit.HEARTS),
            new Card(Rank.TEN, Suit.HEARTS) // Esto forma una Escalera Real de Corazones
        );
        System.out.println("Cartas del jugador: " + playerCards);
        System.out.println("Cartas comunitarias: " + communityCards);

        HandEvaluationResult result = evaluateHand(playerCards, communityCards);
        System.out.println("Mejor mano: " + result); // Usa el toString() de HandEvaluationResult

        // Otro ejemplo: Full House
        playerCards = Arrays.asList(new Card(Rank.ACE, Suit.SPADES), new Card(Rank.ACE, Suit.CLUBS));
        communityCards = Arrays.asList(
            new Card(Rank.KING, Suit.DIAMONDS),
            new Card(Rank.KING, Suit.SPADES),
            new Card(Rank.ACE, Suit.DIAMONDS) // Ases full de Reyes
        );
        System.out.println("\nCartas del jugador: " + playerCards);
        System.out.println("Cartas comunitarias: " + communityCards);
        result = evaluateHand(playerCards, communityCards);
        System.out.println("Mejor mano: " + result);


        // Ejemplo de comparación
        System.out.println("\n--- Ejemplo de Comparación de Manos ---");
        // Mano 1: Par de Ases, kickers K, Q, J
        HandEvaluationResult hand1 = new HandEvaluationResult(HandRank.ONE_PAIR, 
            Arrays.asList(Rank.ACE, Rank.ACE, Rank.KING, Rank.QUEEN, Rank.JACK));
        // Mano 2: Par de Ases, kickers K, Q, T
        HandEvaluationResult hand2 = new HandEvaluationResult(HandRank.ONE_PAIR, 
            Arrays.asList(Rank.ACE, Rank.ACE, Rank.KING, Rank.QUEEN, Rank.TEN));
        // Mano 3: Dos Pares, Ases y Reyes, kicker Q
        HandEvaluationResult hand3 = new HandEvaluationResult(HandRank.TWO_PAIR,
            Arrays.asList(Rank.ACE, Rank.ACE, Rank.KING, Rank.KING, Rank.QUEEN));
        
        System.out.println("Mano 1 (" + hand1.handRank() + "): " + hand1.kickerRanks().stream().map(Rank::getShortName).collect(Collectors.toList()));
        System.out.println("Mano 2 (" + hand2.handRank() + "): " + hand2.kickerRanks().stream().map(Rank::getShortName).collect(Collectors.toList()));
        System.out.println("Mano 3 (" + hand3.handRank() + "): " + hand3.kickerRanks().stream().map(Rank::getShortName).collect(Collectors.toList()));

        System.out.print("Mano 1 vs Mano 2: ");
        int comparison12 = compareHands(hand1, hand2);
        if (comparison12 > 0) System.out.println("Mano 1 es mejor");
        else if (comparison12 < 0) System.out.println("Mano 2 es mejor");
        else System.out.println("Empate");

        System.out.print("Mano 1 vs Mano 3: ");
        int comparison13 = compareHands(hand1, hand3); // Par vs Doble Par
        if (comparison13 > 0) System.out.println("Mano 1 es mejor");
        else if (comparison13 < 0) System.out.println("Mano 3 es mejor"); // Esperado
        else System.out.println("Empate");
    }
    */
}
