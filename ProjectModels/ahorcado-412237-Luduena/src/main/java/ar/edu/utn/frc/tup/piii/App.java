package ar.edu.utn.frc.tup.piii;

/**
 * Hello to Practica Parcial 1 - Ahorcado
 *
 */
public class App 
{

    private static Ahorcado ahorcado = new Ahorcado();

    /**
     * This is the main program
     * 
     */
    public static void main( String[] args ) {
        System.out.println("Hello, Practica Parcial 1 - Ahorcado.");
        ahorcado.bienvenida();
        ahorcado.cargarJugador();
        Boolean playAgain = true;
        do {
            Juego juego = new Juego();
            Boolean terminado = false;
            do {
                juego.dibujar();
                Character letra = ahorcado.pedirLetra();
                juego.addLetra(letra);
                terminado = juego.calcularEstadoDelJuego();
                if (terminado) {
                    ahorcado.addPuntajePartida(juego.getPuntosJuego());
                    ahorcado.mostrarResultadoFinal(juego);
                    playAgain = ahorcado.getPlayAgain();
                }
            } while (!terminado);
        } while (playAgain) ;
        ahorcado.despedida();
    }
}
