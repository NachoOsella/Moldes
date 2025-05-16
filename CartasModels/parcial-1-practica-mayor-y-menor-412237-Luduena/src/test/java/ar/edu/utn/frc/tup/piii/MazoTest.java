package ar.edu.utn.frc.tup.piii;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;

public class MazoTest {
    
    private Mazo mazoOriginal;
    private Mazo mazoMezclado;

    @BeforeEach
    void setUp(){
        // Creamos un mazo nuevo sin mezclar para comparar
        mazoOriginal = new Mazo();
        
        // Creamos otro mazo y lo mezclamos para probar la mezcla
        mazoMezclado = new Mazo();
        mazoMezclado.mezclarCartas();
    }

    // TEST: verificar que después de mezclar, la cantidad de cartas sigue siendo la misma
    @Test
    public void testMazo_mismaCantidadDeCartas(){
        assertEquals(mazoOriginal.cartasRestantes(), mazoMezclado.cartasRestantes(), "Ambos mazos deben tener 40 cartas");
    }

    // TEST: verificar que las cartas mezcladas tienen el mismo contenido que el mazo original (mismas cartas)
    @Test
    public void testMazo_mismoContenido(){
        List<Carta> cartasOriginal = mazoOriginal.getCartas();
        List<Carta> cartasMezcladas = mazoMezclado.getCartas();

        // Verifica que todas las cartas mezcladas están en el original
        assertTrue(cartasOriginal.containsAll(cartasMezcladas),"Las cartas mezcladas deben tener las mismas cartas que el original");
        
        // Verifica que todas las cartas originales están en las mezcladas
        assertTrue(cartasMezcladas.containsAll(cartasOriginal), "Las cartas originales deben tener las mismas cartas que las mezcladas");
    }

    // TEST: verificar que el orden de las cartas cambia luego de mezclar
    @Test
    public void testMazo_ordenDiferente(){
        List<Carta> cartasOriginal = mazoOriginal.getCartas();
        List<Carta> cartasMezcladas = mazoMezclado.getCartas();

        boolean mismoOrden = true;
        
        // Recorremos y comparamos carta por carta para detectar diferencias
        for(int i = 0; i < cartasOriginal.size(); i++){
            if(!cartasOriginal.get(i).equals(cartasMezcladas.get(i))){
                // Si encontramos una carta en distinta posición, cambiamos el flag y salimos
                mismoOrden = false;
                break;
            }
        }

        // Se espera que el orden no sea el mismo después de mezclar
        assertFalse(mismoOrden, "Despues de mezclar, el orden debe ser diferente");
    }

    // TEST: verificar que al crear un mazo nuevo, tiene 40 cartas
    @Test
    public void testMazo_iniciaMazo(){
        assertEquals(40, mazoOriginal.cartasRestantes(), "El mazo debe tener 40 cartas al crearse");
    }

    // TEST: usando reflexión para invocar el método privado inicializarMazo() y verificar que crea 40 cartas
    @Test // Opción con Reflection
    public void testMazo_inicializarMazo() {
        // Encontramos el método privado inicializarMazo() de la clase Mazo
        Method metodo = ReflectionSupport.findMethod(Mazo.class, "inicializarMazo").get(); // sin parámetros
        
        // Permitimos el acceso a método privado para invocarlo
        metodo.setAccessible(true); 

        // Invocamos el método sobre la instancia mazoOriginal y obtenemos la lista de cartas
        @SuppressWarnings("unchecked")
        List<Carta> cartas = (List<Carta>) ReflectionSupport.invokeMethod(metodo, mazoOriginal);

        // Verificamos que haya creado 40 cartas
        assertEquals(40, cartas.size(), "El mazo debe tener 40 cartas después de inicializarse");
    }

    // TEST: verificar que sacar la primera carta la elimina y reduce el tamaño del mazo
    @Test
    public void testMazo_sacaYEliminaPrimerCarta(){
        int cantidadAntes = mazoOriginal.cartasRestantes();

        // Sacamos la primera carta del mazo
        Carta primera = mazoOriginal.primerCarta();

        // Comprobamos que la cantidad de cartas restantes disminuyó en 1
        assertEquals(cantidadAntes - 1, mazoOriginal.cartasRestantes(), "La funcion deberia eliminar la carta que saca");
        
        // Comprobamos que la carta sacada no es null (mazo estaba completo)
        assertNotNull(primera, "La primer carta no debe ser null si el mazo esta completo");
    }

    // TEST: verificar que cartasRestantes() devuelve la cantidad correcta tras sacar cartas
    @Test
    public void testMazo_devuelveLasCartasQueQuedan(){
        // Sacamos una carta para reducir el mazo
        mazoOriginal.primerCarta();

        // Ahora debe quedar 39 cartas
        assertEquals(39, mazoOriginal.cartasRestantes(), "Despues de sacar una carta, el mazo tiene que tener 39");
    }

    // TEST: verificar que getCartas() devuelve una lista sin elementos nulos
    @Test
    public void testMazo_devuelveUnArregloDeCartas(){
        List<Carta> cartas = mazoOriginal.getCartas();
        
        // Comprobamos que todas las cartas en la lista no sean null
        assertTrue(cartas.stream().allMatch(c -> c != null), "Todas las cartas en getCartas() deben ser distintas de null");
    }
}
