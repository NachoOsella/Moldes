package ar.edu.utn.frc.tup.lciii;

import java.util.*;

public class PokerHand {

    // Método principal: determina el índice del jugador ganador entre varias manos y las cartas comunitarias
    public int returnWinner(List<List<Card>> hands, List<Card> tableCards) {
        int winnerIndex = 0; // Índice del jugador con la mejor mano
        PokerResult bestResult = null; // Resultado de la mejor mano encontrada

        for (int i = 0; i < hands.size(); i++) {
            // Combina las cartas del jugador con las comunitarias
            List<Card> fullHand = new ArrayList<>(hands.get(i));
            fullHand.addAll(tableCards);

            // Evalúa la mejor jugada posible con esas cartas
            PokerResult currentResult = evaluateHand(fullHand);

            // Si es la primera mano o es mejor que la actual mejor, se actualiza
            if (bestResult == null || currentResult.compareTo(bestResult) > 0) {
                bestResult = currentResult;
                winnerIndex = i;
            }
        }

        return winnerIndex; // Devuelve el índice del jugador ganador
    }

    // Evalúa una mano de cartas combinadas y devuelve un PokerResult con la mejor jugada y carta alta
    PokerResult evaluateHand(List<Card> cards) {
        List<Hands> possibleHands = new ArrayList<>();

        // Verifica si hay escalera color
        Hands straightFlush = hasStraightFlush(cards);
        if (straightFlush != null) {
            possibleHands.add(straightFlush);
        }

        // Verifica pares, tríos, full, poker
        Hands repeatHands = returnRepeatHands(cards);
        if (repeatHands != null) {
            possibleHands.add(repeatHands);
        }

        // Verifica si hay color (flush)
        Hands flush = hasFlush(cards);
        if (flush != null) {
            possibleHands.add(flush);
        }

        // Verifica si hay escalera
        Hands straight = hasStraight(cards);
        if (straight != null) {
            possibleHands.add(straight);
        }

        // Inicialmente considera la peor mano posible
        Hands bestHand = Hands.HIGH_CARD;

        // Busca la mejor mano entre las posibles
        for (Hands hand : possibleHands) {
            if (hand != null && hand.ordinal() < bestHand.ordinal()) {
                bestHand = hand;
            }
        }

        // Obtiene la carta más alta para desempates
        Card highCard = getHighestCard(cards);

        // Devuelve el resultado final de la evaluación
        return new PokerResult(bestHand, highCard.getNumber());
    }

    // Retorna la carta más alta entre todas
    private Card getHighestCard(List<Card> cards) {
        return cards.stream().max(Comparator.comparingInt(Card::getNumber)).orElse(null);
    }

    // Verifica repeticiones (par, doble par, trío, full house, poker)
    private Hands returnRepeatHands(List<Card> cards) {
        Map<Integer, Integer> freq = new HashMap<>();

        // Cuenta la frecuencia de cada número
        for (Card c : cards) {
            freq.put(c.getNumber(), freq.getOrDefault(c.getNumber(), 0) + 1);
        }

        int pair = 0, trips = 0, quads = 0;

        // Clasifica las cantidades encontradas
        for (int count : freq.values()) {
            if (count == 4) quads++;
            else if (count == 3) trips++;
            else if (count == 2) pair++;
        }

        // Determina la mejor mano de repeticiones
        if (quads > 0) return Hands.POKER;
        if (trips > 0 && pair > 0) return Hands.FULL_HOUSE;
        if (trips > 0) return Hands.THREE_OF_A_KIND;
        if (pair >= 2) return Hands.TWO_PAIR;
        if (pair == 1) return Hands.PAIR;

        return null;
    }

    // Verifica si hay color (5 cartas del mismo palo)
    private Hands hasFlush(List<Card> cards) {
        Map<CardSuits, List<Card>> suits = new HashMap<>();

        // Agrupa cartas por palo
        for (Card card : cards) {
            suits.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }

        // Busca si hay al menos 5 del mismo palo
        for (List<Card> suitGroup : suits.values()) {
            if (suitGroup.size() >= 5) return Hands.FLUSH;
        }

        return null;
    }

    // Verifica si hay escalera (5 cartas consecutivas)
    public Hands hasStraight(List<Card> cards) {
        Set<Integer> ranks = new HashSet<>();

        // Usa un set para evitar duplicados
        for (Card card : cards) {
            ranks.add(card.getNumber());
            if (card.getNumber() == 14) ranks.add(1); // A puede ser bajo también (para A-2-3-4-5)
        }

        List<Integer> sorted = new ArrayList<>(ranks);
        Collections.sort(sorted);

        int count = 1;
        for (int i = 1; i < sorted.size(); i++) {
            if (sorted.get(i) == sorted.get(i - 1) + 1) {
                count++;
                if (count == 5) return Hands.STRAIGHT;
            } else {
                count = 1;
            }
        }

        return null;
    }

    // Verifica si hay escalera color (straight flush)
    private Hands hasStraightFlush(List<Card> cards) {
        Map<CardSuits, List<Card>> suitGroups = new HashMap<>();

        // Agrupa cartas por palo
        for (Card card : cards) {
            suitGroups.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }

        // Por cada grupo de cartas del mismo palo, verifica si hay escalera
        for (List<Card> suitedCards : suitGroups.values()) {
            if (suitedCards.size() >= 5 && hasStraight(suitedCards) != null) {
                return Hands.STRAIGHT_FLUSH;
            }
        }

        return null;
    }
}
