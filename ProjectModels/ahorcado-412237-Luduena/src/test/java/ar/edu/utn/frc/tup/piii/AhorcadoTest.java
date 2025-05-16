package ar.edu.utn.frc.tup.piii;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;

public class AhorcadoTest {
    private Ahorcado ahorcado;

    @BeforeEach
    void setUp() {
        // Antes de cada test, creo una nueva instancia de Ahorcado para que los tests no se mezclen
        ahorcado = new Ahorcado();
    }

    // TEST para cargarJugador()

    @Test
    public void testCargarJugador_AsignaNombreCorrectamente() {
        // Simulo que el usuario ingresa "Juan" y presiona Enter
        String inputSimulado = "Juan\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputSimulado.getBytes());

        // Reemplazo el Scanner interno para que lea desde el input simulado
        ahorcado.setScanner(new java.util.Scanner(in));

        // Ejecuto el método cargarJugador que pide el nombre al usuario
        ahorcado.cargarJugador();

        // Verifico que el jugador no sea nulo y que su nombre sea "Juan"
        assertNotNull(ahorcado.getJugador());
        assertEquals("Juan", ahorcado.getJugador().getNombre());
    }

    // TEST para pedirLetra()

    @Test
    public void testPedirLetra_EntradaValidaDevuelveLetraMayuscula() {
        // Simulo que el usuario ingresa la letra 'a'
        String inputSimulado = "a\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputSimulado.getBytes());
        ahorcado.setScanner(new java.util.Scanner(in));

        // Llamo al método pedirLetra que debería devolver la letra en mayúscula
        Character letra = ahorcado.pedirLetra();

        // Verifico que la letra no sea nula y que sea la 'A' en mayúscula
        assertNotNull(letra);
        assertEquals('A', letra);
    }

    @Test
    public void testPedirLetra_EntradaInvalidaLanzaError() {
        // Simulo que el usuario ingresa "12", que no es válido
        String inputSimulado = "12\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputSimulado.getBytes());
        ahorcado.setScanner(new java.util.Scanner(in));

        // Espero que al llamar pedirLetra lance un Error por entrada inválida
        assertThrows(Error.class, () -> {
            ahorcado.pedirLetra();
        });
    }

    // TEST para addPuntajePartida()

    @Test
    public void testAddPuntajePartida_SumaCorrectamente() {
        // Agrego 10 puntos al puntaje de la partida
        ahorcado.addPuntajePartida(10);
        // Verifico que el puntaje sea 10
        assertEquals(10, ahorcado.getPuntajePartida());

        // Agrego 5 puntos más
        ahorcado.addPuntajePartida(5);
        // Verifico que el puntaje acumulado sea 15
        assertEquals(15, ahorcado.getPuntajePartida());
    }

    @Test
    public void testAddPuntajePartida_NullNoModificaPuntaje() {
        // Agrego 7 puntos
        ahorcado.addPuntajePartida(7);
        // Intento agregar null (que no debería afectar el puntaje)
        ahorcado.addPuntajePartida(null);
        // Verifico que el puntaje siga siendo 7
        assertEquals(7, ahorcado.getPuntajePartida());
    }

    // TEST para getCharacterFromInput()

    @Test
    public void retornaLetraMayusculaSiInputValido() {
        // Invoco el método privado getCharacterFromInput con input "a"
        Character resultado = (Character) ReflectionSupport
                .invokeMethod(ReflectionSupport.findMethod(Ahorcado.class, "getCharacterFromInput", String.class).get(), ahorcado, "a");
        // Espero que devuelva 'A' en mayúscula
        assertEquals('A', resultado);
    }

    @Test
    public void retornaNullSiInputEsVacio() {
        // Invoco getCharacterFromInput con input vacío ""
        Character resultado = (Character) ReflectionSupport.invokeMethod(ReflectionSupport.findMethod(Ahorcado.class, "getCharacterFromInput", String.class).get(), ahorcado, "");
        // Espero que devuelva null por ser input inválido
        assertNull(resultado);
    }

    @Test
    public void retornaNullSiInputTieneMasDeUnCaracter() {
        // Invoco getCharacterFromInput con input "ab" (más de un caracter)
        Character resultado = (Character) ReflectionSupport.invokeMethod(ReflectionSupport.findMethod(Ahorcado.class, "getCharacterFromInput", String.class).get(), ahorcado, "ab");
        // Espero que devuelva null
        assertNull(resultado);
    }

    @Test
    public void retornaNullSiInputNoEsLetra() {
        // Invoco getCharacterFromInput con input "9" (no es letra)
        Character resultado = (Character) ReflectionSupport.invokeMethod(ReflectionSupport.findMethod(Ahorcado.class, "getCharacterFromInput", String.class).get(), ahorcado, "9");
        // Espero que devuelva null
        assertNull(resultado);
    }

    @Test
    public void aceptaLetraÑ() {
        // Invoco getCharacterFromInput con input "ñ"
        Character resultado = (Character) ReflectionSupport.invokeMethod(ReflectionSupport.findMethod(Ahorcado.class, "getCharacterFromInput", String.class).get(), ahorcado, "ñ");
        // Espero que devuelva 'Ñ' mayúscula (la Ñ es válida)
        assertEquals('Ñ', resultado);
    }

    // TEST para validarInputLetra()

    @Test
    public void testValidarInputLetra_DevuelveTrueSiEsLetra() {
        // Invoco validarInputLetra con input "a"
        Boolean resultado = (Boolean) ReflectionSupport.invokeMethod(ReflectionSupport.findMethod(Ahorcado.class, "validarInputLetra", String.class).get(),ahorcado,"a");
        // Espero true porque es una letra válida
        assertTrue(resultado);
    }

    @Test
    public void testValidarInputLetra_DevuelveTrueSiEsÑ() {
        // Invoco validarInputLetra con input "ñ"
        Boolean resultado = (Boolean) ReflectionSupport.invokeMethod(ReflectionSupport.findMethod(Ahorcado.class, "validarInputLetra", String.class).get(),ahorcado,"ñ");
        // Espero true porque es la letra Ñ válida
        assertTrue(resultado);
    }

    @Test
    public void testValidarInputLetra_DevuelveFalseSiEsNumero() {
        // Invoco validarInputLetra con input "4" (número)
        Boolean resultado = (Boolean) ReflectionSupport.invokeMethod(ReflectionSupport.findMethod(Ahorcado.class, "validarInputLetra", String.class).get(), ahorcado,"4");
        // Espero false porque no es letra válida
        assertFalse(resultado);
    }

    @Test
    public void testValidarInputLetra_DevuelveFalseSiEsSimbolo() {
        // Invoco validarInputLetra con input "%"
        Boolean resultado = (Boolean) ReflectionSupport.invokeMethod(ReflectionSupport.findMethod(Ahorcado.class, "validarInputLetra", String.class).get(), ahorcado,"%");
        // Espero false porque no es letra válida
        assertFalse(resultado);
    }

    @Test
    public void testValidarInputLetra_DevuelveFalseSiEsNull() {
        // Invoco validarInputLetra con null
        Boolean resultado = (Boolean) ReflectionSupport.invokeMethod(ReflectionSupport.findMethod(Ahorcado.class, "validarInputLetra", String.class).get(),ahorcado,(Object) null);
        // Espero false porque input es null
        assertFalse(resultado);
    }

    @Test
    public void testValidarInputLetra_DevuelveFalseSiTieneMasDeUnaLetra() {
        // Invoco validarInputLetra con "ab" (más de un caracter)
        Boolean resultado = (Boolean) ReflectionSupport.invokeMethod(ReflectionSupport.findMethod(Ahorcado.class, "validarInputLetra", String.class).get(),ahorcado,"ab");
        // Espero false porque el input no es una única letra
        assertFalse(resultado);
    }
}
