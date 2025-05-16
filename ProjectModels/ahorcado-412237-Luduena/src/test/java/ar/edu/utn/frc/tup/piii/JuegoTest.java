package ar.edu.utn.frc.tup.piii;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;

import static org.junit.jupiter.api.Assertions.*;

public class JuegoTest {

    private Juego juego;

    @BeforeEach
    void setUp() {
        // Antes de cada test, creo una nueva instancia de Juego para que los tests no se mezclen
        juego = new Juego();
    }

    // TESTS para el método addLetra()

    @Test
    public void agregaLetraSiNoExiste() {
        // Agrego la letra 'A' al conjunto de letras elegidas
        juego.addLetra('A');
        // Verifico que la letra 'a' (minúscula) esté efectivamente en la lista de letras elegidas
        assertTrue(juego.getLetrasElegidas().contains('a'));
    }

    @Test
    public void noAgregaLetraDuplicada(){
        // Agrego la letra 'B' dos veces
        juego.addLetra('B');
        juego.addLetra('B');
        // Verifico que la lista solo contenga un elemento, no duplicados
        assertEquals(1, juego.getLetrasElegidas().size());
    }

    @Test
    public void convierteLetraAMinuscula(){
        // Agrego la letra 'C' en mayúscula
        juego.addLetra('C');
        // Verifico que la lista contenga la letra en minúscula 'c'
        assertTrue(juego.getLetrasElegidas().contains('c'));
        // Verifico que no contenga la mayúscula 'C', para confirmar la conversión
        assertFalse(juego.getLetrasElegidas().contains('C'));
    }


    // TEST para el método generarPalabraModoOculto()

    @Test
    public void generaPalabraOculta(){
        // Uso Reflection para invocar el método privado generarPalabraModoOculto
        String resultado = (String) ReflectionSupport.invokeMethod(
            ReflectionSupport.findMethod(Juego.class, "generarPalabraModoOculto").get(),
            juego
        );

        // Obtengo la palabra original del juego
        String palabra = juego.getPalabraEnJuego();

        // Verifico que la longitud de la palabra oculta sea igual a la original
        assertEquals(palabra.length(), resultado.length());

        // Verifico que la palabra oculta contenga sólo guiones bajos (todo oculto al principio)
        assertTrue(resultado.matches("_+"), "La palabra oculta debe contener solo guiones bajos");
    }


    // TEST para el método getPalabraModoOculto()

    @Test
    public void obtenerPalabraOcultaConLetras() {
        String palabra = juego.getPalabraEnJuego();

        // Tomo la primera letra de la palabra para "elegirla"
        char primeraLetra = palabra.charAt(0);

        // Agrego esa letra al conjunto de letras elegidas
        juego.addLetra(primeraLetra);

        // Obtengo la palabra oculta (debería mostrar la primera letra visible)
        String resultado = juego.getPalabraModoOculto();

        // Verifico que la longitud sea igual a la palabra original
        assertEquals(palabra.length(), resultado.length());

        // Verifico que la primera letra en la palabra oculta sea igual a la letra elegida (en minúscula)
        assertTrue(Character.toLowerCase(resultado.charAt(0)) == Character.toLowerCase(primeraLetra),"La primera letra debería estar visible en la palabra oculta");

        // Verifico que el resto de las letras sean guiones bajos
        for (int i = 1; i < resultado.length(); i++) {
            assertEquals('_', resultado.charAt(i), "Las letras no elegidas deben ser guiones bajos");
        }

        // Verifico que el atributo interno palabraModoOculto esté actualizado correctamente
        assertEquals(resultado, juego.getPalabraModoOculto());
    }


    // TEST para el método calcularEstadoDelJuego()

    @Test
    public void calcularEstadoAcierto(){
        // Obtengo la palabra y tomo la primera letra
        String palabra = juego.getPalabraEnJuego();
        char letra = palabra.charAt(0);

        // Agrego esa letra para simular un acierto
        juego.addLetra(letra);
        // Calculo el estado del juego tras el acierto
        boolean terminado = juego.calcularEstadoDelJuego();

        // Verifico que el jugador haya ganado 2 puntos por la letra correcta
        assertEquals(2, juego.getPuntosJuego());
        // Verifico que las vidas no hayan cambiado (sigue con 6)
        assertEquals(6, juego.getVidasJugador());
        // Verifico que el juego no haya terminado aún
        assertFalse(terminado);
    }

    @Test
    public void calcularEstadoFallo(){
        // Obtengo la palabra y elijo una letra que NO esté en la palabra (busco una letra 'z' que no esté)
        String palabra = juego.getPalabraEnJuego().toLowerCase();
        char letra = 'z';
        while(palabra.contains(String.valueOf(letra))){
            letra--;
        }

        // Agrego la letra incorrecta para simular un fallo
        juego.addLetra(letra);
        // Calculo el estado del juego tras el fallo
        boolean terminado = juego.calcularEstadoDelJuego();

        // Verifico que no haya sumado puntos
        assertEquals(0, juego.getPuntosJuego());
        // Verifico que haya perdido una vida (de 6 a 5)
        assertEquals(5, juego.getVidasJugador());
        // Verifico que el juego no haya terminado aún
        assertFalse(terminado);
    }

    @Test
    public void calcularEstadoGano(){
        // Obtengo la palabra y agrego todas sus letras, simulando que el jugador adivinó toda la palabra
        String palabra = juego.getPalabraEnJuego();
        for(char letra : palabra.toCharArray()){
            juego.addLetra(letra);
            juego.calcularEstadoDelJuego();
        }

        // Verifico que el jugador haya ganado el juego
        assertTrue(juego.jugadorGano());
        // Verifico que calcularEstadoDelJuego indique que el juego terminó (true)
        assertTrue(juego.calcularEstadoDelJuego());
    }

    @Test
    public void calcularEstadoPerdio(){
        // Obtengo la palabra y elijo una letra que no está en la palabra para usarla como fallo
        String palabra = juego.getPalabraEnJuego().toLowerCase();
        char letra = 'z';

        // Busco una letra que no esté en la palabra
        while (palabra.contains(String.valueOf(letra))) {
            letra--;
        }

        // Simulo 6 fallos agregando letras incorrectas
        for (int i = 0; i < 6; i++) {
            juego.addLetra((char) (letra - i));
            juego.calcularEstadoDelJuego();
        }

        // Verifico que se hayan agotado las vidas
        assertEquals(0, juego.getVidasJugador());
        // Verifico que el juego haya terminado (perdió)
        assertTrue(juego.calcularEstadoDelJuego()); 
    }
}
