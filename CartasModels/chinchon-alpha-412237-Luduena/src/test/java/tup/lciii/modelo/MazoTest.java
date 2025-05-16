package tup.lciii.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;


public class MazoTest {
    @Test
    public void testGetMazoLenght(){
        Mazo mazo = new Mazo();
        mazo.generarMazo();
        int expectedSize = 48;
        assertEquals(expectedSize, mazo.getMazoLength());
    }

    @Test
    public void testMezclarMazo() {
        Mazo mazo = new Mazo();
        mazo.generarMazo();

        List<Carta> mazoAntesDeMezclar = new ArrayList<>(mazo.getMazo());

        mazo.mezclarMazo();

        assertEquals(mazoAntesDeMezclar.size(), mazo.getMazoLength());

        assertTrue(mazo.getMazo().containsAll(mazoAntesDeMezclar));
        assertFalse(mazo.getMazo().equals(mazoAntesDeMezclar));
    }
}
