package ar.edu.utn.frc.tup.piii;

import java.util.ArrayList;
import java.util.List;

public class Juego {

    private static PalabrasService palabrasService = new PalabrasService();

    private String palabraEnJuego;
    private String palabraModoOculto;
    private List<Character> letrasElegidas;
    private Integer vidasJugador;
    private Integer puntosJuego;

    public Juego() {
        this.palabraEnJuego = palabrasService.getPalabra();
        this.letrasElegidas = new ArrayList<>();
        this.vidasJugador = 6;
        this.puntosJuego = 0;
        this.palabraModoOculto = generarPalabraModoOculto();
    }

    public String getPalabraEnJuego() {
        return palabraEnJuego;
    }

    public List<Character> getLetrasElegidas() {
        return letrasElegidas;
    }

    public Integer getVidasJugador() {
        return vidasJugador;
    }

    public Integer getPuntosJuego() {
        return puntosJuego;
    }

    public boolean jugadorGano() {
    // El jugador gana cuando no quedan guiones bajos en palabraModoOculto
        return !palabraModoOculto.contains("_");
    }

    public String obtenerPalabraCompleta() {
        // Retorna la palabra original en juego
        return palabraEnJuego;
    }


    /**
    * Solo agrega la letra si no estaba antes, sin lógica extra.
    */
    public void addLetra(Character letra) {
        // Convierto la letra a minúscula para mantener uniformidad en la lista
        letra = Character.toLowerCase(letra);
        
        // Verifico si la letra aún no fue elegida previamente
        if (!letrasElegidas.contains(letra)) {
            // Si no está, la agrego a la lista de letras elegidas
            letrasElegidas.add(letra);
        }
    }


    /**
    * Genera la palabra oculta según las letras elegidas.
    */
    private String generarPalabraModoOculto() {
        // Creo un StringBuilder para construir la palabra oculta de forma eficiente
        StringBuilder modoOculto = new StringBuilder();
        
        // Recorro cada carácter de la palabra en juego (la palabra que se debe adivinar)
        for (char letra : palabraEnJuego.toCharArray()) {
            // Si la letra ya fue elegida (adivinada) por el jugador
            if (letrasElegidas.contains(letra)) {
                // Agrego esa letra visible en la palabra oculta
                modoOculto.append(letra);
            } else {
                // Si la letra NO fue elegida, agrego un guion bajo para ocultarla
                modoOculto.append('_');
            }
        }
        
        // Retorno la palabra oculta ya armada como un String
        return modoOculto.toString();
    }


    /**
    * Actualiza y devuelve la palabra en modo oculto.
    */
    public String getPalabraModoOculto() {
        // Genera la versión oculta de la palabra (por ejemplo, con guiones o asteriscos)
        this.palabraModoOculto = generarPalabraModoOculto();
        // Retorna la palabra ya oculta para mostrarla al usuario
        return this.palabraModoOculto;
    }


    /**
     * Calcula el estado del juego: vida, puntaje, si terminó.
     * No debe llamarse más de una vez por letra ingresada.
     */
    public Boolean calcularEstadoDelJuego() {
        // Guardar palabra oculta antes de actualizar
        String palabraOcultaAntes = palabraModoOculto;

        // Actualizar palabra oculta con letras elegidas actuales
        getPalabraModoOculto();

        // Si la palabra oculta cambió, hubo acierto
        boolean acierto = !palabraModoOculto.equals(palabraOcultaAntes);

        if (acierto) {
            // Sumar puntos por acierto
            puntosJuego += 2;
        } else {
            // Fallo: descontar vida y restar punto sin bajar de cero
            vidasJugador--;
            if (puntosJuego > 0) {
                puntosJuego--;
            }
        }

        // Retorna true si el juego terminó (ganó o perdió)
        return !palabraModoOculto.contains("_") || vidasJugador <= 0;
    }

    /**
     * Muestra estado del juego: palabra oculta, vidas, puntaje y letras elegidas.
     */
    public void dibujar() {
        StringBuilder palabraVisible = new StringBuilder();
        for (char c : getPalabraModoOculto().toCharArray()) {
            palabraVisible.append(c).append(' ');
        }

        System.out.println("Palabra: " + palabraVisible.toString().trim());
        System.out.println("Vidas restantes: " + vidasJugador);
        System.out.println("Puntaje: " + puntosJuego);
        System.out.println("Letras elegidas: " + letrasElegidas);
        System.out.println("--------------------------------");
    }
}
