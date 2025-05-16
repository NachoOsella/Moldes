package ar.edu.utn.frc.tup.piii;

public class Player {
    private String nombre;           // Nombre del jugador
    private int partidasGanadas;     // Contador de partidas ganadas
    private int partidasPerdidas;    // Contador de partidas perdidas
    private int aciertos;            // Contador de aciertos (respuestas correctas)
    private int errores;             // Contador de errores (respuestas incorrectas)

    // Constructor que inicializa el nombre y pone todos los contadores en cero
    public Player(String nombre) {
        this.nombre = nombre;
        this.partidasGanadas = 0;
        this.partidasPerdidas = 0;
        this.aciertos = 0;
        this.errores = 0;
    }

    // Getter y setter para el nombre del jugador
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter y setter para partidas ganadas
    public int getPartidasGanadas() {
        return partidasGanadas;
    }
    public void setPartidasGanadas(int partidasGanadas) {
        this.partidasGanadas = partidasGanadas;
    }

    // Getter y setter para partidas perdidas
    public int getPartidasPerdidas() {
        return partidasPerdidas;
    }
    public void setPartidasPerdidas(int partidasPerdidas) {
        this.partidasPerdidas = partidasPerdidas;
    }

    // Getter y setter para aciertos
    public int getAciertos() {
        return aciertos;
    }
    public void setAciertos(int aciertos) {
        this.aciertos = aciertos;
    }

    // Método para reiniciar los aciertos a cero
    public void reiniciarAciertos() {
        aciertos = 0;
    }

    // Getter y setter para errores
    public int getErrores() {
        return errores;
    }
    public void setErrores(int errores) {
        this.errores = errores;
    }

    // Métodos para incrementar cada contador (más práctico para contar)
    public void incrementarPartidasGanadas() {
        this.partidasGanadas++;
    }

    public void incrementarPartidasPerdidas() {
        this.partidasPerdidas++;
    }

    public void incrementarAciertos() {
        this.aciertos++;
    }

    public void incrementarErrores() {
        this.errores++;
    }
}
