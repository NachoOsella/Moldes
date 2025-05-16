package tup.lciii.modelo;

import org.junit.jupiter.api.Test;
import tup.lciii.modelo.enums.Palo;

import static org.junit.jupiter.api.Assertions.*;

public class ManoTest {

    @Test
    public void testBuscarCartaEnMano() {
        Mano mano = new Mano();
        Carta carta1 = new Carta(1, Palo.ESPADA);
        Carta carta2 = new Carta(5, Palo.BASTO);
        Carta carta3 = new Carta(10, Palo.ORO);
        mano.agregarCarta(carta1);
        mano.agregarCarta(carta2);
        mano.agregarCarta(carta3);

        Carta resultado = mano.buscarCartaEnMano(5, Palo.BASTO);
        assertNotNull(resultado);
        assertEquals(carta2, resultado);

        resultado = mano.buscarCartaEnMano(7, Palo.COPA);
        assertNull(resultado);
    }
}
