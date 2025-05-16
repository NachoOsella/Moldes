package ar.edu.utn.frc.tup.lciii;

import ar.edu.utn.frc.tup.lciii.round.EscobaMatchRound;
import ar.edu.utn.frc.tup.lciii.round.RoundPlayer;

import java.util.Scanner;

/**
 * Esta clase representa una partida completa del juego.
 */
public class EscobaMatch {

    /**
     * Scanner para capturar las entradas del usuario
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * Jugador asignado al usuario
     */
    private User humanUser;

    /**
     * Jugador asignado a la app
     */
    private User appUser;

    /**
     * Puntos del jugador humano
     */
    private Integer humanUserPoints;

    /**
     * Puntos de la app
     */
    private Integer appUserPoints;

    /**
     * Jugador que gano el juego
     */
    private User winner;

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public User getHumanUser() {
        return humanUser;
    }

    public void setHumanUser(User humanUser) {
        this.humanUser = humanUser;
    }

    public User getAppUser() {
        return appUser;
    }

    public void setAppUser(User appUser) {
        this.appUser = appUser;
    }

    public Integer getHumanUserPoints() {
        return humanUserPoints;
    }

    public void setHumanUserPoints(Integer humanUserPoints) {
        this.humanUserPoints = humanUserPoints;
    }

    public Integer getAppUserPoints() {
        return appUserPoints;
    }

    public void setAppUserPoints(Integer appUserPoints) {
        this.appUserPoints = appUserPoints;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    /**
     * Constructor de la clase EscobaMatch
     * Este constructor inicializa los atributos humanUser, appUser, humanUserPoints y appUserPoints.
     *
     * @param humanUser jugador que representa a la persona
     * @param appUser jugador que representa a la app
     */
    public EscobaMatch(User humanUser, User appUser) {
        this.humanUser = humanUser;
        this.appUser = appUser;
        this.humanUserPoints = 0;
        this.appUserPoints = 0;
        this.winner = null;
    }

    /**
     * Este método define si la partida terminó.
     * El juego termina cuando uno de los dos jugadores (El player o la app)
     * ha obtenido más de 15 punto y más puntos que su oponente.
     * Cuando el juego termina, este método setea en el atributo winner quien ganó.
     *
     * @see #winner
     *
     * @return true si el juego terminó, false si aún no hay un ganador
     */
    public Boolean isFinish() {
        // TODO: Validar si algún jugador ya llegó a los 15 puntos y obtuvo mas puntos que el oponente.
        //  Si algún jugador ganó, setear el ganador en winner y mostrar el siguiente mensaje:
        //  "The player [nombre del jugador ganador] won with [puntos del jugador] points."
        //  y retornar true si hubo un ganador.
        //  Si no hay ganador, mostrar el siguiente mensaje:
        //  "The game is not over yet."
        //  "The player [nombre del jugador humano] has [puntos del jugador humano] points."
        //  "The player [nombre del jugador APP] has [puntos del jugador app] points."

        // Verifica si el jugador humano tiene al menos 15 puntos y además tiene más puntos que la app
        if(humanUserPoints >= 15 && humanUserPoints > appUserPoints){
            // Asigna al ganador al jugador humano
            winner = humanUser;
            // Imprime un mensaje indicando que el jugador humano ganó con su puntaje
            LetterByLetterPrinter.println(String.format("The player %s won with %d   points.", humanUser, humanUserPoints));
            // Retorna true indicando que el juego terminó
            return true;

        // Verifica si el jugador de la app tiene al menos 15 puntos y además tiene más puntos que el humano
        }else if (appUserPoints >= 15 && appUserPoints > humanUserPoints){
            // Asigna al ganador al jugador de la app
            winner = appUser;
            // Imprime un mensaje indicando que el jugador de la app ganó con su puntaje
            LetterByLetterPrinter.println(String.format("The player %s won with %d points.", appUser, appUserPoints));
            // Retorna true indicando que el juego terminó
            return true;

        // Si ninguna de las condiciones anteriores se cumple, el juego continúa
        }else{
            // Imprime mensaje indicando que el juego no ha terminado
            LetterByLetterPrinter.println("The game is not over yet.");
            // Muestra los puntos actuales del jugador humano
            LetterByLetterPrinter.println(String.format("The player %s has %d points.", humanUser, humanUserPoints));
            // Muestra los puntos actuales del jugador de la app
            LetterByLetterPrinter.println(String.format("The player %s has %d points.", appUser, appUserPoints));
            // Retorna false indicando que el juego sigue en curso
            return false;
        }

    }

    /**
     * Suma en los puntos de la partida, los puntos obtenidos en una ronda por cada jugado.
     *
     * @param escobaMatchRound ronda jugada a sumar en los puntos del partido
     *
     * @see EscobaMatch#humanUserPoints
     * @see EscobaMatch#appUserPoints
     * @see EscobaMatchRound#getRoundPlayerHuman()
     * @see EscobaMatchRound#getRoundPlayerApp()
     * @see RoundPlayer#getRoundPoints()
     */
    public void calculateScore(EscobaMatchRound escobaMatchRound) {
        humanUserPoints += escobaMatchRound.getRoundPlayerHuman().getRoundPoints();
        appUserPoints += escobaMatchRound.getRoundPlayerApp().getRoundPoints();
    }
}
