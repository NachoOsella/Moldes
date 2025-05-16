package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EscobaGameTest {

    Scanner scanner = Mockito.mock(Scanner.class);

    EscobaGame escobaGame = Mockito.spy(EscobaGame.class);

    private final PrintStream systemOut = System.out;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        escobaGame.setScanner(scanner);
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setOut(systemOut);
    }

    @Test
    void welcomeMessage() {
        String expected = "Welcome to the 'Escoba de 15' game!" + System.lineSeparator();
        escobaGame.welcomeMessage();
        assertEquals(expected, getOutput());
    }

    @Test
    void createHumanUser() {
        when(scanner.nextLine()).thenReturn("Hernán");
        User user = escobaGame.createHumanUser();
        assertEquals("Hernán", user.getName());
    }

    @Test
    void createAppUser() {
        User user = escobaGame.createAppUser();
        assertEquals("APP", user.getName());
    }

    @Test
    void wantPlayAgain_TrueAnswer() {
        when(scanner.nextLine()).thenReturn("y");
        Boolean result = escobaGame.wantPlayAgain();
        assertTrue(result);
    }

    @Test
    void wantPlayAgain_FalseAnswer() {
        when(scanner.nextLine()).thenReturn("n");
        Boolean result = escobaGame.wantPlayAgain();
        assertFalse(result);
    }

    @Test
    void getYesNoAnswerTest_YesAnswer() {
        // TODO: Implementar el test para el método getYesNoAnswer de manera tal que se
        //  pruebe que el método retorna false si se ingresa "n" o "N"
        
        // Declara variables para almacenar los resultados de las llamadas al método
        Boolean resn;
        Boolean resN;

        // Verifica si existe el método getYesNoAnswer(String) en la clase EscobaGame usando reflexión
        if (ReflectionSupport.findMethod(EscobaGame.class, "getYesNoAnswer", String.class).isPresent()) {

            // Invoca el método con el string "n" y almacena el resultado
            resn = (Boolean) ReflectionSupport.invokeMethod(
                ReflectionSupport.findMethod(EscobaGame.class, "getYesNoAnswer", String.class).get(),
                escobaGame,
                "n"
            );

            // Invoca el método con el string "N" y almacena el resultado
            resN = (Boolean) ReflectionSupport.invokeMethod(
                ReflectionSupport.findMethod(EscobaGame.class, "getYesNoAnswer", String.class).get(),
                escobaGame,
                "N"
            );

            // Verifica que en ambos casos el resultado sea false
            assertFalse(resn);
            assertFalse(resN);
        } else {
            // Falla el test si no se encuentra el método
            fail("No such method getYesNoAnswer");
        }
    }

    @Test
    void getYesNoAnswerTest_NoAnswer() {
        // TODO: Implementar el test para el método getYesNoAnswer de manera tal que se
        //  pruebe que el método retorna true si se ingresa "y" o "Y"
        
        // Declara variables para almacenar los resultados de las llamadas al método
        Boolean resy;
        Boolean resY;

        // Verifica si existe el método getYesNoAnswer(String) en la clase EscobaGame usando reflexión
        if(ReflectionSupport.findMethod(EscobaGame.class, "getYesNoAnswer", String.class).isPresent()) {

            // Invoca el método con el string "y" y guarda el resultado
            resy = (Boolean) ReflectionSupport.invokeMethod(
                ReflectionSupport.findMethod(EscobaGame.class, "getYesNoAnswer", String.class).get(),
                escobaGame,
                "y"
            );

            // Invoca el método con el string "Y" y guarda el resultado
            resY = (Boolean) ReflectionSupport.invokeMethod(
                ReflectionSupport.findMethod(EscobaGame.class, "getYesNoAnswer", String.class).get(),
                escobaGame,
                "Y"
            );

            // Verifica que en ambos casos el método retorne true
            assertTrue(resy);
            assertTrue(resY);
        } else {
            // Falla el test si el método no existe
            fail("No such method getYesNoAnswer");
        }
    }

    @Test
    void getYesNoAnswerTest_NullAnswer() {
        // TODO: Implementar el test para el método getYesNoAnswer de manera tal que se
        //  pruebe que el método retorna null si se ingresa algo distinto de "y", "Y", "n" o "N"
        
        // Declara variables para guardar los resultados de las llamadas al método
        Boolean res1;
        Boolean res2;

        // Verifica si el método getYesNoAnswer(String) existe en la clase EscobaGame
        if(ReflectionSupport.findMethod(EscobaGame.class, "getYesNoAnswer", String.class).isPresent()) {

            // Invoca el método con una cadena inválida "maybe" y guarda el resultado
            res1 = (Boolean) ReflectionSupport.invokeMethod(
                ReflectionSupport.findMethod(EscobaGame.class, "getYesNoAnswer", String.class).get(),
                escobaGame,
                "maybe"
            );

            // Invoca el método con otra cadena inválida "123" y guarda el resultado
            res2 = (Boolean) ReflectionSupport.invokeMethod(
                ReflectionSupport.findMethod(EscobaGame.class, "getYesNoAnswer", String.class).get(),
                escobaGame,
                "123"
            );

            // Verifica que ambos resultados sean null, ya que las entradas no son válidas
            assertNull(res1);
            assertNull(res2);
        } else {
            // Falla el test si el método no existe
            fail("No such method getYesNoAnswer");
        }
    }

    private String getOutput() {
        return testOut.toString();
    }
}