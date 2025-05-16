package ar.edu.utn.frc.tup.piii;

import java.util.Scanner;

public class MayorMenor {
    
    Scanner scanner; // Scanner para leer la entrada del usuario

    // Constructor que recibe un Scanner externo (útil para tests o simulaciones)
    public MayorMenor(Scanner scanner) {
        this.scanner = scanner;
    }

    // Constructor por defecto que crea un Scanner para System.in (entrada estándar)
    public MayorMenor() {
        this(new Scanner(System.in));
    }

    // Método que muestra un mensaje de bienvenida al juego
    public void bienvenida() {
        System.out.println("Bienvenido al juego de Mayor - Menor");
    }

    // Método para pedir el nombre del jugador y devolverlo
    public String pedirNombreJugador() {
        System.out.println("Ingrese su nombre: ");
        String nombre = scanner.nextLine(); // Lee una línea de texto desde entrada
        return nombre;
    }

    // Método que obtiene y muestra la primera carta del mazo
    public Carta getPrimerCarta(Mazo mazo) {
        Carta carta = mazo.primerCarta(); // Saca la primera carta del mazo (la elimina)
        System.out.println("La primer carta es: " + carta.getValor() + " de " + carta.getPalo());
        return carta;
    }

    // Método que obtiene y muestra la segunda carta del mazo
    public Carta getSegundaCarta(Mazo mazo){
        Carta carta = mazo.primerCarta(); // Saca la siguiente carta del mazo
        System.out.println("La primer carta es: " + carta.getValor() + " de " + carta.getPalo());
        return carta;
    }

    // Método que pide al jugador su decisión: si la siguiente carta es mayor o menor
    public String decisionPlayer(){
        System.out.println("La siguiente carta es Mayor(M) o menor(m)?");
        String decision = scanner.nextLine(); // Lee la decisión
        return decision;
    }

    /**
     * Método que compara dos cartas con la decisión del jugador.
     * @param cartaActual la carta mostrada actualmente
     * @param segundaCarta la siguiente carta sacada
     * @param decision la decisión del jugador ("M" o "m")
     * @return true si la decisión es correcta, false si es incorrecta
     */
    public boolean compararCartas(Carta cartaActual, Carta segundaCarta, String decision){
        if(decision.equals("M") && cartaActual.getValor() < segundaCarta.getValor()){
            System.out.println("¡Correcto! La carta es mayor.");
            return true;
        }
        else if(decision.equals("m") && cartaActual.getValor() > segundaCarta.getValor()){
            System.out.println("¡Correcto! La carta es menor.");
            return true;
        }
        else if(cartaActual.getValor() == segundaCarta.getValor()){
            System.out.println("¡Correcto! Las cartas eran iguales");
            return true;
        }
        else{
            System.out.println("¡Incorrecto! Perdiste");
            return false;
        }
    }

    // Método para validar que la decisión ingresada sea válida ("M" o "m")
    public boolean validarRespuesta(String decision){
        if(decision.equals("M") || decision.equals("m")){
            return true;
        }
        else{
            System.out.println("Respuesta no valida, ingrese M o m");
            return false;
        }
    }

    // Método que pregunta si el jugador quiere seguir jugando
    public boolean seguirJugando() {
        System.out.println("¿Quiere seguir jugando? (S/N)");
        String respuesta = scanner.nextLine().trim().toUpperCase(); // Lee y normaliza la respuesta
        return respuesta.equals("S"); // Retorna true si la respuesta es "S"
    }

}
