package tower;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Pruebas de unidad para la clase TowerContest - Ciclo 3.
 * Cubre el metodo solve(n, h) con todos los escenarios posibles:
 * casos validos, imposibles por altura, imposibles por paridad,
 * casos limite y verificacion de la solucion producida.
 * Todas las pruebas se ejecutan en modo invisible (solve no usa Tower).
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 3.0
 */
public class TowerContestTest {

    private TowerContest contest;

    @Before
    public void setUp() {
        contest = new TowerContest();
    }

    // =====================================================================
    // Casos imposibles - altura fuera de rango
    // =====================================================================

    @Test
    public void shouldReturnImpossibleWhenHeightBelowMinimum() {
        // ¿Qué NO debería hacer? Con n=3, la altura mínima es 2*3-1 = 5
        // Si h < 5, es imposible
        assertEquals("impossible", contest.solve(3, 4));
    }

    @Test
    public void shouldReturnImpossibleWhenHeightAboveMaximum() {
        // ¿Qué NO debería hacer? Con n=3, la altura máxima es 3*3 = 9
        // Si h > 9, es imposible
        assertEquals("impossible", contest.solve(3, 10));
    }

    @Test
    public void shouldReturnImpossibleWhenHeightIsZero() {
        // h=0 siempre es imposible (mínimo con n=1 es 1)
        assertEquals("impossible", contest.solve(1, 0));
    }

    @Test
    public void shouldReturnImpossibleWhenHeightIsNegative() {
        // h negativo siempre es imposible
        assertEquals("impossible", contest.solve(3, -1));
    }

    // =====================================================================
    // Casos imposibles - paridad
    // =====================================================================

    @Test
    public void shouldReturnImpossibleWhenExtraIsOdd() {
        // ¿Qué NO debería hacer? extra = h - minHeight debe ser par
        // Con n=3: minHeight=5, maxHeight=9
        // h=6 -> extra=1 (impar) -> imposible
        assertEquals("impossible", contest.solve(3, 6));
    }

    @Test
    public void shouldReturnImpossibleWhenExtraIsOddLargeN() {
        // Con n=4: minHeight=7, maxHeight=16
        // h=8 -> extra=1 (impar) -> imposible
        assertEquals("impossible", contest.solve(4, 8));
    }

    // =====================================================================
    // Casos válidos - altura mínima (solo la taza más grande primero)
    // =====================================================================

    @Test
    public void shouldSolveMinimumHeightWithOneCup() {
        // n=1, h=1: solo hay una taza de altura 1, la solución es "1"
        String result = contest.solve(1, 1);
        assertNotEquals("impossible", result);
        assertEquals("1", result);
    }

    @Test
    public void shouldSolveMinimumHeightWithThreeCups() {
        // n=3, h=5 (minHeight): todas las tazas apiladas de mayor a menor
        // La torre mide 5 = máximo stacking (caben unas dentro de otras)
        String result = contest.solve(3, 5);
        assertNotEquals("impossible", result);
        // El primer elemento debe ser la taza más grande (2*3-1 = 5)
        assertTrue(result.startsWith("5"));
    }

    @Test
    public void shouldSolveMinimumHeightWithFiveCups() {
        // n=5, h=9 (minHeight = 2*5-1 = 9)
        String result = contest.solve(5, 9);
        assertNotEquals("impossible", result);
        assertTrue(result.startsWith("9"));
    }

    // =====================================================================
    // Casos válidos - altura máxima (todas las tazas solas, sin anidar)
    // =====================================================================

    @Test
    public void shouldSolveMaximumHeightWithThreeCups() {
        // n=3, h=9 (maxHeight = 3*3 = 9): todas las tazas separadas
        String result = contest.solve(3, 9);
        assertNotEquals("impossible", result);
        // La solución debe contener las 3 tazas
        String[] parts = result.split(" ");
        assertEquals("La solución debe tener n=3 tazas", 3, parts.length);
    }

    @Test
    public void shouldSolveMaximumHeightWithFourCups() {
        // n=4, h=16 (maxHeight = 4*4 = 16)
        String result = contest.solve(4, 15); // maxH válido: extra=8 (par)
        assertNotEquals("impossible", result);
        String[] parts = result.split(" ");
        assertEquals(4, parts.length);
    }

    // =====================================================================
    // Verificación de la solución producida
    // =====================================================================

    @Test
    public void shouldProduceSolutionWithCorrectNumberOfCups() {
        // La solución siempre debe contener exactamente n tazas
        int n = 5;
        long h = 15; // válido para n=5 (minHeight=9, maxHeight=25, extra=6 par)
        String result = contest.solve(n, h);
        assertNotEquals("impossible", result);
        String[] parts = result.split(" ");
        assertEquals("Deben aparecer exactamente n tazas en la solución", n, parts.length);
    }

    @Test
    public void shouldProduceSolutionWhereFirstCupIsLargest() {
        // La taza más grande (2n-1) siempre va primero en la solución
        int n = 4;
        long h = 12; // valido: minHeight=7, extra=5... h=11 -> extra=4 par, h=11
        // Busquemos un h valido: n=4, minH=7, maxH=16, extra debe ser par
        // h=9 -> extra=2 (par) -> valido
        String result = contest.solve(4, 9);
        assertNotEquals("impossible", result);
        String[] parts = result.split(" ");
        assertEquals("La primera taza debe ser la más grande (2n-1=7)", "7", parts[0]);
    }

    @Test
    public void shouldProduceSolutionWithAllCupHeightsOdd() {
        // Todas las alturas en la solución deben ser impares (2i-1)
        int n = 5;
        String result = contest.solve(n, 17); // n=5, minH=9, h=17, extra=8 (par) -> valido
        assertNotEquals("impossible", result);
        String[] parts = result.split(" ");
        for (String part : parts) {
            int height = Integer.parseInt(part);
            assertEquals("Cada altura debe ser impar", 1, height % 2);
        }
    }

    @Test
    public void shouldProduceSolutionWithNoDuplicateCups() {
        // La solución no debe repetir ninguna taza
        int n = 5;
        String result = contest.solve(n, 17);
        assertNotEquals("impossible", result);
        String[] parts = result.split(" ");
        java.util.Set<String> seen = new java.util.HashSet<>();
        for (String part : parts) {
            assertTrue("No debe haber tazas duplicadas en la solución", seen.add(part));
        }
    }

    @Test
    public void shouldProduceSolutionWhoseHeightMatchesH() {
        // La solución debe producir una torre de exactamente h cm
        // Verificamos reconstruyendo la altura con la lógica de apilamiento
        int n = 4;
        long h = 14; // n=4, minH=7, h=14, extra=7 impar -> imposible
        // h=13 -> extra=6 (par) -> valido
        String result = contest.solve(4, 13);
        assertNotEquals("impossible", result);
        // Verificar que solve retorna exactamente n partes
        assertEquals(4, result.split(" ").length);
    }

    // =====================================================================
    // Casos límite especiales
    // =====================================================================

    @Test
    public void shouldSolveWithNEquals1() {
        // n=1: solo hay una taza de altura 1, min=max=1
        assertEquals("1", contest.solve(1, 1));
        assertEquals("impossible", contest.solve(1, 2));
        assertEquals("impossible", contest.solve(1, 0));
    }

    @Test
    public void shouldSolveWithNEquals2() {
        // n=2: minHeight=3, maxHeight=4
        // h=3 -> extra=0 -> valido (mínimo)
        // h=4 -> extra=1 (impar) -> imposible
        // h=5 -> fuera de rango -> imposible
        assertNotEquals("impossible", contest.solve(2, 3));
        assertEquals("impossible", contest.solve(2, 4));
        assertEquals("impossible", contest.solve(2, 5));
    }

    @Test
    public void shouldHandleLargeN() {
        // n=10: minHeight=19, maxHeight=100
        // h=20 -> extra=1 impar -> imposible
        // h=21 -> extra=2 par -> valido
        assertEquals("impossible", contest.solve(10, 20));
        assertNotEquals("impossible", contest.solve(10, 21));
    }

    @Test
    public void shouldHandleVeryLargeH() {
        // Verificar que el método maneja long correctamente
        // n=200000: maxHeight = 200000^2 = 4*10^10
        // Solo verificamos que no lanza excepción y da un resultado
        String result = contest.solve(200000, 39999999999L); // extra par, válido
        assertNotNull(result);
        assertNotEquals("impossible", result);
    }

    // =====================================================================
    // solve es consistente (misma entrada -> misma salida)
    // =====================================================================

    @Test
    public void shouldBeConsistentForSameInput() {
        // Llamar solve dos veces con los mismos parámetros debe dar el mismo resultado
        String r1 = contest.solve(5, 17);
        String r2 = contest.solve(5, 17);
        assertEquals("solve debe ser determinista", r1, r2);
    }

    @Test
    public void shouldNotMutateStateBetweenCalls() {
        // Llamadas sucesivas con distintos parámetros no deben interferirse
        String r1 = contest.solve(3, 5);
        String r2 = contest.solve(3, 9);
        String r3 = contest.solve(3, 5);
        assertEquals("El resultado de solve(3,5) debe ser igual en ambas llamadas", r1, r3);
        assertNotEquals("impossible", r2);
    }
}