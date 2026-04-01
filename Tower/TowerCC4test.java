package tower;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Pruebas comunes (colectivas) para el Ciclo 4 - Stacking Cups.
 * Nombres de pruebas con iniciales MV (Madero - Valderrama).
 * Todas las pruebas se ejecutan en modo invisible.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 4.0
 */
public class TowerCC4test {

    private Tower tower;

    @Before
    public void setUp() {
        tower = new Tower(6, 60);
    }

    // =====================================================================
    // CupOpener - pruebas MV
    // =====================================================================

    @Test
    public void accordingMVShouldOpenerRemoveOnlyLidsNotCups() {
        // CupOpener elimina tapas pero conserva tazas
        tower.pushCup(3);
        tower.pushLid(3);
        tower.pushCup(2);
        tower.pushCup(4, "opener");
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        int cupCount = 0;
        int lidCount = 0;
        for (String[] item : items) {
            if (item[0].equals("cup")) cupCount++;
            if (item[0].equals("lid")) lidCount++;
        }
        assertEquals("Deben quedar 3 tazas", 3, cupCount);
        assertEquals("No deben quedar tapas (opener las elimino)", 0, lidCount);
    }

    @Test
    public void accordingMVShouldOpenerSucceedWithEmptyTower() {
        // CupOpener en torre sin tapas simplemente entra
        tower.pushCup(3, "opener");
        assertTrue(tower.ok());
        assertEquals(1, tower.stackingItems().length);
    }

    @Test
    public void accordingMVShouldNotAllowDuplicateCup() {
        // No se puede pushCup con un numero que ya existe
        tower.pushCup(3);
        tower.pushCup(3, "opener");
        assertFalse("No debe permitir dos tazas con el mismo numero", tower.ok());
    }

    // =====================================================================
    // CupHierarchical - pruebas MV
    // =====================================================================

    @Test
    public void accordingMVShouldHierarchicalPushSmallerCupsUp() {
        // CupHierarchical desplaza tazas menores hacia arriba
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushCup(4, "hierarchical");
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        // Cup4 debe quedar debajo de Cup2 y Cup1
        int idx4 = -1, idx2 = -1;
        for (int i = 0; i < items.length; i++) {
            if (items[i][0].equals("cup") && items[i][1].equals("4")) idx4 = i;
            if (items[i][0].equals("cup") && items[i][1].equals("2")) idx2 = i;
        }
        assertTrue("Cup4 debe quedar debajo de Cup2", idx4 < idx2);
    }

    @Test
    public void accordingMVShouldNotRemoveHierarchicalAtBottom() {
        // CupHierarchical que llego al fondo es inamovible.
        // Cup3 hierarchical desplaza a Cup1 y queda en posicion 0 = fondo.
        tower.pushCup(1);
        tower.pushCup(3, "hierarchical");
        assertTrue(tower.ok());
        tower.removeCup(3);
        assertFalse("CupHierarchical en el fondo no debe poder quitarse", tower.ok());
    }

    // =====================================================================
    // CupDominant - pruebas MV
    // =====================================================================

    @Test
    public void accordingMVShouldDominantEliminateAllSmallerCups() {
        // CupDominant elimina TODAS las tazas menores, no solo una
        tower.pushCup(5);
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup(4, "dominant");
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        for (String[] item : items) {
            if (item[0].equals("cup")) {
                int num = Integer.parseInt(item[1]);
                assertTrue("CupDominant no debe dejar tazas menores", num >= 4);
            }
        }
    }

    @Test
    public void accordingMVShouldDominantNotEliminateEqualCup() {
        // CupDominant no elimina tazas del mismo numero (no puede existir igual)
        // Solo verifica que la operacion es exitosa
        tower.pushCup(5);
        tower.pushCup(3, "dominant");
        assertTrue(tower.ok());
    }

    // =====================================================================
    // LidFearful - pruebas MV
    // =====================================================================

    @Test
    public void accordingMVShouldFearfulEnterOnlyWithCompanionCup() {
        // LidFearful solo entra si su taza esta en la torre
        tower.pushCup(2);
        tower.pushLid(3, "fearful"); // taza 3 no esta
        assertFalse("LidFearful no debe entrar sin su taza", tower.ok());

        tower.pushCup(3);
        tower.pushLid(3, "fearful"); // ahora si esta
        assertTrue("LidFearful debe entrar con su taza presente", tower.ok());
    }

    @Test
    public void accordingMVShouldFearfulNotLeaveWhenProtecting() {
        // LidFearful tapando su taza no puede salir
        tower.pushCup(3);
        tower.pushLid(3, "fearful");
        tower.popLid();
        assertFalse("LidFearful protegiendo no debe poder salir con popLid", tower.ok());
    }

    @Test
    public void accordingMVShouldFearfulLeaveWhenNotProtecting() {
        // LidFearful que no esta sobre su taza si puede salir
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushLid(3, "fearful"); // esta sobre Cup2, no sobre Cup3
        tower.removeLid(3);
        assertTrue("LidFearful sin proteger debe poder quitarse", tower.ok());
    }

    // =====================================================================
    // LidCrazy - pruebas MV
    // =====================================================================

    @Test
    public void accordingMVShouldCrazyGoToBaseNotTop() {
        // LidCrazy va a la base, no a la cima
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushLid(3, "crazy");
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        assertEquals("El primer elemento debe ser LidCrazy", "lid", items[0][0]);
        assertEquals("3", items[0][1]);
    }

    @Test
    public void accordingMVShouldMultipleCrazyStackInOrder() {
        // Varias LidCrazy se acumulan en la base en orden de llegada
        tower.pushCup(5);
        tower.pushLid(5, "crazy");
        tower.pushCup(3);
        tower.pushLid(3, "crazy");
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        assertEquals("lid", items[0][0]);
        assertEquals("lid", items[1][0]);
        // Las tazas deben estar encima de las crazys
        assertEquals("cup", items[2][0]);
    }

    // =====================================================================
    // Interacciones entre tipos
    // =====================================================================

    @Test
    public void accordingMVShouldOpenerNotAffectCrazyLids() {
        // CupOpener solo elimina tapas normales/fearful, no las crazy de la base
        tower.pushCup(5);
        tower.pushLid(5, "crazy");
        tower.pushCup(3);
        tower.pushLid(3);
        tower.pushCup(4, "opener");
        assertTrue(tower.ok());
        // La LidCrazy 5 debe seguir en la base
        String[][] items = tower.stackingItems();
        boolean crazyStillExists = false;
        for (String[] item : items) {
            if (item[0].equals("lid") && item[1].equals("5")) crazyStillExists = true;
        }
        assertTrue("LidCrazy en la base no debe ser eliminada por CupOpener", crazyStillExists);
    }

    @Test
    public void accordingMVShouldTowerHeightBeCorrectWithMixedTypes() {
        // La altura de la torre es correcta con tipos mezclados
        tower.pushCup(3); // altura 5
        tower.pushLid(3); // altura 1
        assertTrue(tower.ok());
        int h = tower.height();
        assertTrue("La altura debe ser positiva con elementos en la torre", h > 0);
    }
}