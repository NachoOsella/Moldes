package ar.edu.utn.frc.tup.lciii;

/**
 * Hello to Exam - ESCOBA DE 15
 *
 */
public class App 
{


    /**
     * This is the main program
     * IMPORTANTE: Este método no necesita ser modificado
     */
    public static void main( String[] args ) {
        System.out.println("¡Bienvenido al juego de Poker Texas Hold'em!");

        PokerGame game = new PokerGame();
        game.play();
    }
}
