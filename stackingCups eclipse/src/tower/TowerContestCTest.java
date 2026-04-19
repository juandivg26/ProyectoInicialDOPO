package tower;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Pruebas comunes (colectivas) para la clase TowerContest - Ciclo 3.
 * Nombres de pruebas con iniciales MV (Madero - Valderrama).
 * Cubre escenarios adicionales de solve(n, h) enfocados en
 * correctitud de la solución y casos borde no triviales.
 * Todas las pruebas se ejecutan en modo invisible.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 3.0
 */
public class TowerContestCTest {

    private TowerContest contest;

    @Before
    public void setUp() {
        contest = new TowerContest();
    }

    // =====================================================================
    // Imposibles - rango y paridad
    // =====================================================================

    @Test
    public void accordingMVShouldReturnImpossibleBelowMin() {
        // h menor que minHeight (2n-1) es siempre imposible
        assertEquals("impossible", contest.solve(5, 8));
    }

    @Test
    public void accordingMVShouldReturnImpossibleAboveMax() {
        // h mayor que maxHeight (n^2) es siempre imposible
        assertEquals("impossible", contest.solve(5, 26));
    }

    @Test
    public void accordingMVShouldReturnImpossibleOddExtra() {
        // n=5, minH=9, h=10 -> extra=1 (impar) -> imposible
        assertEquals("impossible", contest.solve(5, 10));
    }

    @Test
    public void accordingMVShouldReturnImpossibleOddExtraEdge() {
        // n=3, minH=5, h=8 -> extra=3 (impar) -> imposible
        assertEquals("impossible", contest.solve(3, 8));
    }

    // =====================================================================
    // Válidos - verificaciones de estructura
    // =====================================================================

    @Test
    public void accordingMVShouldSolveAndReturnNElements() {
        // solve(n, h) válido siempre retorna exactamente n elementos
        int n = 6;
        // n=6: minH=11, maxH=36, h=13 -> extra=2 (par) -> valido
        String result = contest.solve(n, 13);
        assertNotEquals("impossible", result);
        assertEquals(n, result.split(" ").length);
    }

    @Test
    public void accordingMVShouldStartWithLargestCup() {
        // La primera taza siempre es la más grande (2n-1)
        // n=6: 2*6-1 = 11
        String result = contest.solve(6, 13);
        assertNotEquals("impossible", result);
        assertEquals("11", result.split(" ")[0]);
    }

    @Test
    public void accordingMVShouldContainAllCupHeights() {
        // La solución debe contener todas las alturas 1,3,5,...,2n-1 exactamente una vez
        int n = 4; // alturas: 1,3,5,7
        // h=7 (minH) -> extra=0 -> valido
        String result = contest.solve(n, 7);
        assertNotEquals("impossible", result);

        java.util.Set<String> expected = new java.util.HashSet<>();
        for (int i = 1; i <= n; i++) expected.add(String.valueOf(2 * i - 1));

        String[] parts = result.split(" ");
        java.util.Set<String> got = new java.util.HashSet<>(java.util.Arrays.asList(parts));
        assertEquals("La solución debe contener exactamente las n alturas impares", expected, got);
    }

    @Test
    public void accordingMVShouldContainAllCupHeightsForMaxH() {
        // También para h máxima
        int n = 4; // maxH=16
        String result = contest.solve(n, 15); // n=4, h=15 válido (extra=8 par)
        assertNotEquals("impossible", result);

        java.util.Set<String> expected = new java.util.HashSet<>();
        for (int i = 1; i <= n; i++) expected.add(String.valueOf(2 * i - 1));

        java.util.Set<String> got = new java.util.HashSet<>(
            java.util.Arrays.asList(result.split(" ")));
        assertEquals(expected, got);
    }

    // =====================================================================
    // Casos límite n=1 y n=2
    // =====================================================================

    @Test
    public void accordingMVShouldSolveN1H1() {
        // El caso más pequeño posible
        assertEquals("1", contest.solve(1, 1));
    }

    @Test
    public void accordingMVShouldReturnImpossibleN1H2() {
        // Con n=1 solo existe h=1
        assertEquals("impossible", contest.solve(1, 2));
    }

    @Test
    public void accordingMVShouldSolveN2MinHeight() {
        // n=2, h=3 (minH): "3 1"
        String result = contest.solve(2, 3);
        assertNotEquals("impossible", result);
        assertEquals(2, result.split(" ").length);
        assertEquals("3", result.split(" ")[0]);
    }

    // =====================================================================
    // Determinismo y robustez
    // =====================================================================

    @Test
    public void accordingMVShouldBeDeterministic() {
        // Misma entrada -> misma salida, siempre
        String r1 = contest.solve(6, 13);
        String r2 = contest.solve(6, 13);
        assertEquals(r1, r2);
    }

    @Test
    public void accordingMVShouldNotAffectOtherCalls() {
        // Una llamada no debe afectar el resultado de la siguiente
        contest.solve(5, 9);
        contest.solve(5, 25);
        String r = contest.solve(5, 17);
        assertNotEquals("impossible", r);
        assertEquals(5, r.split(" ").length);
    }

    @Test
    public void accordingMVShouldHandleLargeNWithoutException() {
        // n grande no debe lanzar excepción ni desbordamiento
        String result = contest.solve(1000, 999001L); // maxH = 1000^2 = 1000000, minH=1999
        assertNotNull(result);
    }
}