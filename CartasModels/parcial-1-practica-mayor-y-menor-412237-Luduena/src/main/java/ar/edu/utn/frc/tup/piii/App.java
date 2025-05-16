package ar.edu.utn.frc.tup.piii;

/**
 * Clase principal que ejecuta el juego Mayor/Menor
 */
public class App {

    private static MayorMenor mayorMenor;  // Instancia para manejar la lógica del juego y la interacción con el usuario
    private static Mazo mazo;               // El mazo de cartas
    private static Player player;           // El jugador actual

    /**
     * Método main: punto de entrada del programa
     */
    public static void main(String[] args) {
        mayorMenor = new MayorMenor();  // Crear instancia del juego
        mazo = new Mazo();              // Crear el mazo nuevo
        System.out.println("Hello, Practica Parcial 1 - MAYOR/MENOR.");

        mayorMenor.bienvenida();                 // Mostrar mensaje de bienvenida
        String nombre = mayorMenor.pedirNombreJugador();  // Pedir nombre del jugador
        player = new Player(nombre);             // Crear jugador con el nombre ingresado

        mazo.mezclarCartas();                    // Mezclar las cartas antes de empezar
        boolean jugarOtraVez = true;             // Control para repetir partidas

        // Bucle principal para jugar múltiples partidas si el usuario quiere
        while (jugarOtraVez) {
            boolean seguirJugando = true;        // Control para seguir dentro de la partida actual

            // Bucle de la partida actual
            while (seguirJugando) {
                Carta primerCarta = mayorMenor.getPrimerCarta(mazo);  // Sacar y mostrar la primera carta
                if (primerCarta == null) {                             // Si no quedan cartas, termina la partida
                    System.out.println("¡No quedan cartas! Ganaste la partida.");
                    break;
                }

                // Pedir al jugador su decisión y validar entrada
                String decision = mayorMenor.decisionPlayer();
                while (!mayorMenor.validarRespuesta(decision)) {
                    decision = mayorMenor.decisionPlayer();
                }

                Carta segundaCarta = mayorMenor.getSegundaCarta(mazo);  // Sacar y mostrar la segunda carta
                if (segundaCarta == null) {                             // Si no quedan cartas para comparar, termina la partida
                    System.out.println("¡No quedan más cartas! Ganaste la partida.");
                    break;
                }

                // Comparar cartas y obtener resultado (true para continuar, false para perder)
                seguirJugando = mayorMenor.compararCartas(primerCarta, segundaCarta, decision);

                player.incrementarAciertos();                            // Contar acierto si acertó la comparación
                System.out.println("La cantidad de aciertos fue de: " + player.getAciertos());
                System.out.println("La cantidad de cartas restantes es: " + mazo.cartasRestantes());
            }

            // Preguntar si el jugador quiere jugar otra partida
            jugarOtraVez = mayorMenor.seguirJugando();
            if (jugarOtraVez) {
                player.reiniciarAciertos();        // Reiniciar aciertos para nueva partida
                player.setErrores(0);               // Reiniciar errores para nueva partida
                mazo = new Mazo();                  // Crear un nuevo mazo
                mazo.mezclarCartas();               // Mezclar el nuevo mazo
            }
        }

        // Mostrar estadísticas finales cuando el jugador decida terminar
        System.out.println("Estadísticas finales de " + player.getNombre() + ":");
        System.out.println("Partidas ganadas: " + player.getPartidasGanadas());
        System.out.println("Partidas perdidas: " + player.getPartidasPerdidas());
        System.out.println("Aciertos en la última partida: " + player.getAciertos());
        System.out.println("Gracias por jugar. ¡Hasta la próxima!");
    }
}
