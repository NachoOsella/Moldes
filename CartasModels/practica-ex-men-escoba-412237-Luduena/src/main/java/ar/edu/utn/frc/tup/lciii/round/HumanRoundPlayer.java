package ar.edu.utn.frc.tup.lciii.round;

import ar.edu.utn.frc.tup.lciii.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class HumanRoundPlayer extends RoundPlayer {

    public HumanRoundPlayer(User player) {
        super(player);
    }

    public HumanRoundPlayer() {
        super();
    }

    /**
     * Este método representa el turno de un jugador humano.
     * @param tableCards
     */
    @Override
    public void playTurn(List<Card> tableCards) {
        LetterByLetterPrinter.println(System.lineSeparator() + "============================================");
        LetterByLetterPrinter.println("Is PLAYER turn...");
        showCardsOnTheTable(tableCards);
        int rta = getCardToPlay();

        Card selectedCard = this.getHandCards().get(rta);
        List<Card> conjunto = new ArrayList<>(tableCards);
        conjunto.add(selectedCard);
        List<List<Card>> subconjuntosAux = obtenerSubconjuntos(conjunto);
        subconjuntosAux.removeIf(c -> !c.contains(selectedCard));
        subconjuntosAux.removeIf(c -> c.stream().mapToInt(Card::getValue).sum() != 15);
        List<List<Card>> subconjuntos = new ArrayList<>(subconjuntosAux);

        if(subconjuntos.isEmpty()) {
            LetterByLetterPrinter.println("You can't make 15 with this card. The card, was added to the table.");
            tableCards.add(selectedCard);
            this.getHandCards().remove(selectedCard);
        } else {
            if(subconjuntos.size() == 1) {
                List<Card> selectedCards = subconjuntos.get(0);
                takingCardsFromTable(tableCards, selectedCards);
            } else {
                int rtaSubconjunto = getSubconjuntoToPlay(subconjuntos);
                List<Card> selectedCards = subconjuntos.get(rtaSubconjunto);
                takingCardsFromTable(tableCards, selectedCards);
            }
        }
        LetterByLetterPrinter.println("============================================" + System.lineSeparator());
    }

    /**
     * Método para seleccionar una carta de la mano del jugador.
     * Si el jugador selecciona un índice inválido, se le pedirá que seleccione una carta válida.
     * Si el jugador ingresa un valor no numérico, se le pedirá que ingrese un número.
     *
     * @return el índice de la carta seleccionada.
     *
     * @see #getSubconjuntoToPlay(List)
     * @see LetterByLetterPrinter#println(String)
     */
    private Integer getCardToPlay() {
        // TODO: Implementar lógica para seleccionar la carta a jugar siguiendo estos pasos:
        //   1. Mostrar este mensaje "This are the cards in your hand:"
        //   2. Mostrar las cartas en la mano del jugador con este formato "i - carta" donde i es el índice de la carta.
        //   3. Mostrar este mensaje "Please, select a card to play:"
        //   4. Pedir al usuario que seleccione una carta usando el scanner.nextInt().
        //    IMPORTANTE: Controlar respuesta valida del usuario, si es inválida,
        //    mostrar el mensaje "Invalid input. Please, insert a number to get a valid option."
        //   5. Retornar el índice de la carta seleccionada.
        //    IMPORTANTE: Retornar un número válido, donde el indice es mayor = 0 y menor al tamaño
        //    de la listad e cartas en la mano. Si el valor no se encuentra en el rango,
        //    mostrar el mensaje "Invalid card. Please, select a valid card."
        
        
        // Imprime el mensaje inicial indicando que se mostrarán las cartas en la mano
        LetterByLetterPrinter.println("This are the cards in your hand:");

        // Recorre todas las cartas en la mano y las imprime con su índice
        for(int i = 0; i < getHandCards().size(); i++){
            // Imprime el índice y la representación en texto de la carta en esa posición
            LetterByLetterPrinter.println( i + " - " + getHandCards().get(i).toString());
        }

        // Pide al usuario que seleccione una carta para jugar
        LetterByLetterPrinter.println("Please, select a card to play:");

        // Inicializa la variable de entrada en -1 (valor inválido)
        int input = -1;

        try{
            // Intenta leer un número entero ingresado por el usuario
            input = scanner.nextInt();
        }catch(InputMismatchException e){
            // Si no es un entero, entra en este bloque para manejar el error
            while (!scanner.hasNextInt()){
                // Informa al usuario que la entrada es inválida y pide un número válido
                LetterByLetterPrinter.println("Invalid input. Please, insert a number to get a valid option.");
                // Intenta leer nuevamente la entrada
                input = scanner.nextInt();
            }
        }

        // Mientras el número ingresado sea menor que 0 o mayor que el índice máximo de las cartas
        while (input < 0 || input > getHandCards().size()){
            // Informa que la carta seleccionada es inválida y pide una válida
            LetterByLetterPrinter.println("Invalid card. Please, select a valid card.");
            // Lee un nuevo número ingresado por el usuario
            input = scanner.nextInt();
        }

        // Retorna el índice válido de la carta seleccionada
        return input;

    }

    /**
     * Método para seleccionar un subconjunto de cartas.
     * Si el jugador selecciona un índice inválido, se le pedirá que seleccione una opción válida.
     * Si el jugador ingresa un valor no numérico, se le pedirá que ingrese un número.
     *
     * @param subconjuntos lista de subconjuntos a seleccionar.
     * @return el índice del subconjunto seleccionado.
     */
    private int getSubconjuntoToPlay(List<List<Card>> subconjuntos) {
        boolean isValidSubconjunto = false;
        int rtaSubconjunto = -1;
        do {
            LetterByLetterPrinter.println("You can make 15 with this cards. Choose an option to put in your personal deck:");
            for (int i = 0; i < subconjuntos.size(); i++) {
                String listCards = subconjuntos.get(i).stream().map(Card::toString).reduce("", (a, b) -> a + " - " + b);
                LetterByLetterPrinter.println(i + listCards);
            }
            try {
                rtaSubconjunto = scanner.nextInt();
                if(rtaSubconjunto >= 0 && rtaSubconjunto < subconjuntos.size()) {
                    isValidSubconjunto = true;
                } else {
                    rtaSubconjunto = -1;
                    LetterByLetterPrinter.println("Invalid answer. Please, select a valid answer.");
                }
            } catch (InputMismatchException e) {
                LetterByLetterPrinter.println("Invalid input. Please, insert a number to get a valid option.");
                scanner.next();
            }
        } while (!isValidSubconjunto);
        return rtaSubconjunto;
    }
}
