package ar.edu.utn.frc.tup.piii;

import java.util.Scanner;

public class Ahorcado {

    private Scanner scanner;
    private Jugador jugador;
    private Integer puntajePartida;

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Integer getPuntajePartida() {
        return puntajePartida;
    }

    public void setPuntajePartida(Integer puntajePartida) {
        this.puntajePartida = puntajePartida;
    }

    public Ahorcado() {
        this.scanner = new Scanner(System.in);
        this.jugador = null;
        this.puntajePartida = 0;
    }

    public void bienvenida() {
        // Escribir el codigo de bienvenida
        System.out.println("Bienvenido al juego del Ahorcado");
    }

    /**
     * Este metodo gestiona todo el proiceso de pedir los datos al jugador
     * y retornarlos al programa principal para poder Jugar
     *
     */
    public void cargarJugador() {
        // Escribir el codigo para generar un jugador a partir del nombre
        System.out.println("Ingrese su nombre: ");
        String nombre = scanner.nextLine();
        this.jugador = new Jugador(nombre);
        System.out.println(nombre + " Adivina la palabra!");
    }

    /**
     * Este metodo gestiona todo el proceso de pedir una letra al jugador,
     * validar que la entrada por panatañña sea una correcta y retornar
     * dicha entrada como un Character de Java.
     * @return el caracter ingresado por usuario
     */
    public Character pedirLetra() {
        /*
            Escribir el codigo para pedir una letra, y cargarlo en "letra"
         */
        Character letra = null;
        do {
            System.out.println("Ingrese una letra");
            String input = scanner.nextLine();
            if(validarInputLetra(input)) {
                letra = getCharacterFromInput(input);
            } else {
                /*
                    Mostrar el error por pantalla
                 */
                throw new Error("La letra no esta en la palabra.");
            }
        } while (letra == null);
        return letra;
    }

    public void addPuntajePartida(Integer puntosJuego) {
        if (puntosJuego != null) {
            this.puntajePartida += puntosJuego;
        }
    }

    /**
    * Este metodo retorna el string de input como un caracter que representa una letra
    * del idioma español en mayusculas.
    *
    * @param input
    * @return el caracter ingresado por usuario
    */
    private Character getCharacterFromInput(String input) {
        // Verifico si el input es una letra válida (llama a otro método que valida eso)
        if (validarInputLetra(input)) {
            // Si es válido, tomo el primer carácter de la cadena input
            // y lo convierto a mayúscula para devolverlo
            return Character.toUpperCase(input.charAt(0));
        }
        // Si no es válido, retorno null indicando que no es un carácter válido
        return null;
    }


    /**
    * Este metodo retorna true si el input leido de scanner es traducible a una letra
    * del idioma español.
    * @param input
    * @return true si la entrada fue apropiada, o false si no lo fue
    */
    private Boolean validarInputLetra(String input) {
        // Verifico que el input no sea null y que tenga exactamente un carácter
        if (input == null || input.length() != 1) {
            // Si es null o tiene más/menos de un carácter, no es válido
            return false;
        }
        
        // Convierto el carácter a mayúscula para facilitar la comparación
        char c = Character.toUpperCase(input.charAt(0));
        
        // Retorno true si el carácter está entre 'A' y 'Z' (letras del alfabeto inglés)
        // o si es la letra 'Ñ', que es específica del idioma español.
        return (c >= 'A' && c <= 'Z') || c == 'Ñ';
    }


    /**
     * Este metodo gestiona todo el proceso para preguntar si se desea volver a jugar, si es así, retorna true
     * sino, retorna false
     *
     * @return la seleccion del jugador de volver a jugar
     */
    public Boolean getPlayAgain() {
        String input;
        do {
            System.out.println("¿Desea jugar otra vez? (s/n)");
            input = scanner.nextLine().trim().toLowerCase();
        } 
        while (!input.equals("s") && !input.equals("n"));
        return input.equals("s");
    }

    public void mostrarResultadoFinal(Juego juego) {
        // Verifico si el jugador ganó el juego
        if (juego.jugadorGano()) {
            // Si ganó, imprimo un mensaje de felicitaciones con la palabra completa que adivinó
            System.out.println("¡Felicitaciones! Adivinaste la palabra: " + juego.obtenerPalabraCompleta());
        } 
        else {
            // Si no ganó, imprimo el mensaje de "Game over" y muestro cuál era la palabra correcta
            System.out.println("Game over. La palabra correcta era: " + juego.obtenerPalabraCompleta());
        }
    }



    /**
     * Este metodo gestiona el proceso de despedia de la partida, muestra los puntajes y partidas jugadas
     * (ganadas y perdidas) y termina la aplicación.
     */
    public void despedida() {
        System.out.println("Gracias por jugar al Ahorcado.");
        System.out.println("Puntaje total de la partida: " + this.puntajePartida);
        System.out.println("¡Hasta luego!");
        scanner.close();
    }
}
