package ar.edu.utn.frc.tup.piii;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MayorMenorTest {
    private MayorMenor juego;
    private Mazo mazo;

    @BeforeEach
    void setUp() {
        // Creamos una instancia nueva del juego sin Scanner (constructor normal)
        juego = new MayorMenor(); 
        
        // Creamos un nuevo mazo con todas las cartas
        mazo = new Mazo();
        
        // Simulamos la entrada de usuario con "M\n" para la decisión "Mayor"
        Scanner scannerSimulado = new Scanner("M\n");
        
        // Creamos el juego usando el Scanner simulado para entradas controladas en tests
        juego = new MayorMenor(scannerSimulado);
    }

    // TEST para pedirNombreJugador()
    @Test
    public void mayorMenorTest_pedirNombreJugador(){
        // Simulamos que el usuario escribe "Juan" y luego Enter
        String entradaSimulada = "Juan\n";
        Scanner scannerFalso = new Scanner(entradaSimulada);
        
        // Creamos un juego con el Scanner simulado
        MayorMenor juego = new MayorMenor(scannerFalso);

        // Llamamos al método que pide el nombre
        String nombre = juego.pedirNombreJugador();

        // Verificamos que el nombre devuelto sea "Juan"
        assertEquals("Juan", nombre, "El nombre ingresado debe ser 'Juan'");
    }

    // TEST para getPrimerCarta(): debe devolver y eliminar la primera carta del mazo
    @Test
    void testGetPrimerCarta_devuelveYEliminaLaPrimera() {
        // Cantidad de cartas antes de sacar la primera
        int cantidadAntes = mazo.cartasRestantes();

        // Guardamos la primera carta esperada (sin modificar mazo)
        Carta primera = mazo.getCartas().get(0);
        
        // Obtenemos la primera carta usando el método que se prueba
        Carta cartaDevuelta = juego.getPrimerCarta(mazo);

        // Comprobamos que la carta devuelta sea la misma que la primera del mazo original
        assertEquals(primera, cartaDevuelta, "Debe devolver la primera carta del mazo");
        
        // Verificamos que la carta haya sido eliminada (cantidad disminuyó en 1)
        assertEquals(cantidadAntes - 1, mazo.cartasRestantes(), "Debe eliminar la carta del mazo");
    }

    // TEST para getSegundaCarta(): debe devolver y eliminar la segunda carta original del mazo
    @Test
    void testGetSegundaCarta_devuelveYEliminaLaSegunda() {
        // Guardamos la segunda carta original
        Carta segunda = mazo.getCartas().get(1);

        // Simulamos que ya se sacó la primera carta del mazo
        mazo.primerCarta();

        // Obtenemos la segunda carta con el método a probar
        Carta cartaDevuelta = juego.getSegundaCarta(mazo);

        // Verificamos que sea la segunda carta original
        assertEquals(segunda, cartaDevuelta, "Debe devolver la segunda carta original del mazo");
    }

    // TEST para decisionPlayer() que devuelve la decisión "M"
    @Test
    void testDecisionPlayer_devuelveM() {
        // Obtenemos la decisión del jugador (simulada en setUp con "M\n")
        String decision = juego.decisionPlayer();
        
        // Verificamos que devuelva "M"
        assertEquals("M", decision, "Debe devolver la decisión ingresada 'M'");
    }

    // TEST para decisionPlayer() que devuelve la decisión "m"
    @Test
    void testDecisionPlayer_devuelveMenor() {
        // Creamos juego con Scanner que simula la entrada "m\n"
        juego = new MayorMenor(new Scanner("m\n"));
        
        // Obtenemos la decisión
        String decision = juego.decisionPlayer();
        
        // Verificamos que devuelva "m"
        assertEquals("m", decision, "Debe devolver la decisión ingresada 'm'");
    }

    // TEST para compararCartas() cuando la decisión es correcta y la segunda carta es mayor
    @Test
    void testCompararCartas_correctoMayor() {
        Carta carta1 = new Carta(3, Palo.ORO);
        Carta carta2 = new Carta(7, Palo.COPA);

        // Se espera true porque la segunda carta es mayor y la decisión fue "M"
        boolean resultado = juego.compararCartas(carta1, carta2, "M");

        assertTrue(resultado, "Debe devolver true si la segunda carta es mayor y la decisión fue 'M'");
    }

    // TEST para compararCartas() cuando la decisión es correcta y la segunda carta es menor
    @Test
    void testCompararCartas_correctoMenor() {
        Carta carta1 = new Carta(10, Palo.BASTO);
        Carta carta2 = new Carta(5, Palo.ESPADA);

        // Se espera true porque la segunda carta es menor y la decisión fue "m"
        boolean resultado = juego.compararCartas(carta1, carta2, "m");

        assertTrue(resultado, "Debe devolver true si la segunda carta es menor y la decisión fue 'm'");
    }

    // TEST para compararCartas() cuando las cartas son iguales (valor)
    @Test
    void testCompararCartas_igualValor() {
        Carta carta1 = new Carta(6, Palo.ORO);
        Carta carta2 = new Carta(6, Palo.BASTO);

        // Se espera true sin importar la decisión porque los valores son iguales
        boolean resultado = juego.compararCartas(carta1, carta2, "M");

        assertTrue(resultado, "Debe devolver true si las cartas son iguales, sin importar la decisión");
    }

    // TEST para compararCartas() cuando la decisión es incorrecta
    @Test
    void testCompararCartas_incorrecto() {
        Carta carta1 = new Carta(4, Palo.ESPADA);
        Carta carta2 = new Carta(9, Palo.COPA);

        // Se espera false porque se eligió "m" pero la carta es mayor
        boolean resultado = juego.compararCartas(carta1, carta2, "m");

        assertFalse(resultado, "Debe devolver false si la decisión es incorrecta");
    }

    // TEST para validarRespuesta() con mayúscula válida
    @Test
    void testValidarRespuesta_Mayuscula() {
        assertTrue(juego.validarRespuesta("M"), "Debe aceptar 'M' como válido");
    }

    // TEST para validarRespuesta() con minúscula válida
    @Test
    void testValidarRespuesta_minuscula() {
        assertTrue(juego.validarRespuesta("m"), "Debe aceptar 'm' como válido");
    }

    // TEST para validarRespuesta() con valor inválido
    @Test
    void testValidarRespuesta_invalido() {
        assertFalse(juego.validarRespuesta("X"), "Debe rechazar 'X' como inválido");
    }

    // TEST para validarRespuesta() con cadena vacía
    @Test
    void testValidarRespuesta_vacio() {
        assertFalse(juego.validarRespuesta(""), "Debe rechazar una cadena vacía");
    }

    // TEST para seguirJugando() con entrada "S" mayúscula → debe devolver true
    @Test
    void testSeguirJugando_siEsS_mayuscula() {
        MayorMenor juego = new MayorMenor(new Scanner("S\n"));
        assertTrue(juego.seguirJugando(), "Debe devolver true si el jugador escribe 'S'");
    }

    // TEST para seguirJugando() con entrada "s" minúscula → debe devolver true
    @Test
    void testSeguirJugando_siEsS_minuscula() {
        MayorMenor juego = new MayorMenor(new Scanner("s\n"));
        assertTrue(juego.seguirJugando(), "Debe devolver true si el jugador escribe 's'");
    }

    // TEST para seguirJugando() con entrada "N" mayúscula → debe devolver false
    @Test
    void testSeguirJugando_siEsN_mayuscula() {
        MayorMenor juego = new MayorMenor(new Scanner("N\n"));
        assertFalse(juego.seguirJugando(), "Debe devolver false si el jugador escribe 'N'");
    }

    // TEST para seguirJugando() con entrada inválida → debe devolver false
    @Test
    void testSeguirJugando_siEsOtraLetra() {
        MayorMenor juego = new MayorMenor(new Scanner("X\n"));
        assertFalse(juego.seguirJugando(), "Debe devolver false si el jugador escribe una letra inválida");
    }
}
