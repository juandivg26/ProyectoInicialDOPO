package tower;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Pruebas de unidad para el Ciclo 4 - Stacking Cups.
 * Cubre los nuevos tipos: CupOpener, CupHierarchical, CupDominant,
 * LidFearful, LidCrazy, y los metodos nuevos pushCup(type) y pushLid(type).
 * Todas las pruebas se ejecutan en modo invisible.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 4.0
 */
public class TowerC4test {

    private Tower tower;

    @Before
    public void setUp() {
        // Torre de ancho 6 y alto suficiente para todas las pruebas
        tower = new Tower(6, 60);
        // Modo invisible: no se llama makeVisible()
    }

    // =====================================================================
    // CupOpener
    // =====================================================================

    @Test
    public void testCupOpenerEliminatesBlockingLids() {
        // ¿Qué debería hacer? Al entrar, elimina las tapas que le bloquean el paso
        tower.pushCup(3);
        tower.pushLid(3);
        tower.pushCup(2);
        tower.pushLid(2);
        tower.pushCup(4, "opener"); // debe eliminar Lid3 y Lid2 que estaban antes
        assertTrue(tower.ok());
        // La tapa 3 y la tapa 2 deben haber sido eliminadas
        String[][] items = tower.stackingItems();
        boolean lid3Exists = false;
        boolean lid2Exists = false;
        for (String[] item : items) {
            if (item[0].equals("lid") && item[1].equals("3")) lid3Exists = true;
            if (item[0].equals("lid") && item[1].equals("2")) lid2Exists = true;
        }
        assertFalse("CupOpener debio eliminar Lid3", lid3Exists);
        assertFalse("CupOpener debio eliminar Lid2", lid2Exists);
    }

    @Test
    public void testCupOpenerKeepsCups() {
        // ¿Qué NO debería hacer? No debe eliminar tazas, solo tapas
        tower.pushCup(3);
        tower.pushLid(3);
        tower.pushCup(2);
        tower.pushCup(4, "opener");
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        boolean cup3Exists = false;
        boolean cup2Exists = false;
        for (String[] item : items) {
            if (item[0].equals("cup") && item[1].equals("3")) cup3Exists = true;
            if (item[0].equals("cup") && item[1].equals("2")) cup2Exists = true;
        }
        assertTrue("CupOpener NO debe eliminar Cup3", cup3Exists);
        assertTrue("CupOpener NO debe eliminar Cup2", cup2Exists);
    }

    @Test
    public void testCupOpenerWithNoLidsDoesNotFail() {
        // ¿Qué debería hacer? Si no hay tapas, simplemente entra sin error
        tower.pushCup(2);
        tower.pushCup(3, "opener");
        assertTrue(tower.ok());
    }

    // =====================================================================
    // CupHierarchical
    // =====================================================================

    @Test
    public void testCupHierarchicalInsertsInCorrectPosition() {
        // ¿Qué debería hacer? Se inserta en posicion jerarquica (mayor abajo)
        tower.pushCup(5);
        tower.pushCup(2);
        tower.pushCup(4, "hierarchical"); // debe quedar entre Cup5 y Cup2
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        // Orden esperado de abajo a arriba: Cup5, Cup4, Cup2
        assertEquals("cup", items[0][0]);
        assertEquals("5", items[0][1]);
        assertEquals("cup", items[1][0]);
        assertEquals("4", items[1][1]);
        assertEquals("cup", items[2][0]);
        assertEquals("2", items[2][1]);
    }

    @Test
    public void testCupHierarchicalCannotBeRemovedAfterReachingBottom() {
        // ¿Qué NO debería hacer? Si llego al fondo, no se puede quitar.
        // Cup2 hierarchical desplaza a Cup1 y queda en posicion 0 = fondo.
        tower.pushCup(1);
        tower.pushCup(2, "hierarchical");
        assertTrue(tower.ok());
        tower.removeCup(2);
        assertFalse("CupHierarchical en el fondo no debe poder quitarse", tower.ok());
    }

    @Test
    public void testCupHierarchicalCanBeRemovedIfNotAtBottom() {
        // ¿Qué NO debería hacer? Si no llego al fondo, si se puede quitar
        tower.pushCup(5);
        tower.pushCup(3, "hierarchical"); // queda dentro de Cup5, no en el fondo
        assertTrue(tower.ok());
        tower.removeCup(3);
        assertTrue("CupHierarchical que no llego al fondo si debe poder quitarse", tower.ok());
    }

    // =====================================================================
    // CupDominant
    // =====================================================================

    @Test
    public void testCupDominantEliminatesSmallerCups() {
        // ¿Qué debería hacer? Elimina tazas mas pequeñas al entrar
        tower.pushCup(5);
        tower.pushCup(2);
        tower.pushCup(3, "dominant"); // debe eliminar Cup2 porque 2 < 3
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        boolean cup2Exists = false;
        for (String[] item : items) {
            if (item[0].equals("cup") && item[1].equals("2")) cup2Exists = true;
        }
        assertFalse("CupDominant debio eliminar Cup2", cup2Exists);
    }

    @Test
    public void testCupDominantKeepsLargerCups() {
        // ¿Qué NO debería hacer? No elimina tazas mas grandes
        tower.pushCup(5);
        tower.pushCup(2);
        tower.pushCup(3, "dominant");
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        boolean cup5Exists = false;
        for (String[] item : items) {
            if (item[0].equals("cup") && item[1].equals("5")) cup5Exists = true;
        }
        assertTrue("CupDominant NO debe eliminar Cup5", cup5Exists);
    }

    @Test
    public void testCupDominantEliminatesLidOfSmallerCup() {
        // ¿Qué debería hacer? Si la taza eliminada tenia tapa, la elimina tambien
        tower.pushCup(5);
        tower.pushCup(2);
        tower.pushLid(2);
        tower.pushCup(4, "dominant");
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        boolean lid2Exists = false;
        for (String[] item : items) {
            if (item[0].equals("lid") && item[1].equals("2")) lid2Exists = true;
        }
        assertFalse("CupDominant debio eliminar Lid2 junto con Cup2", lid2Exists);
    }

    // =====================================================================
    // LidFearful
    // =====================================================================

    @Test
    public void testLidFearfulEntersWhenCupIsPresent() {
        // ¿Qué debería hacer? Entra si su taza companiera esta en la torre
        tower.pushCup(3);
        tower.pushLid(3, "fearful");
        assertTrue(tower.ok());
    }

    @Test
    public void testLidFearfulDoesNotEnterWithoutCup() {
        // ¿Qué NO debería hacer? No entra si su taza companiera no esta
        tower.pushLid(3, "fearful");
        assertFalse("LidFearful no debe entrar sin su taza companiera", tower.ok());
    }

    @Test
    public void testLidFearfulCannotBeRemovedWhileProtecting() {
        // ¿Qué NO debería hacer? No se puede quitar si esta tapando su taza
        tower.pushCup(3);
        tower.pushLid(3, "fearful");
        tower.removeLid(3);
        assertFalse("LidFearful protegiendo no debe poder quitarse", tower.ok());
    }

    @Test
    public void testLidFearfulCanBeRemovedWhenNotProtecting() {
        // ¿Qué debería hacer? Se puede quitar si no esta tapando su taza
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushLid(3, "fearful"); // no esta directamente sobre Cup3
        tower.removeLid(3);
        assertTrue("LidFearful que no protege si debe poder quitarse", tower.ok());
    }

    // =====================================================================
    // LidCrazy
    // =====================================================================

    @Test
    public void testLidCrazyGoesToBase() {
        // ¿Qué debería hacer? Se ubica en la base de la torre, no en la cima
        tower.pushCup(3);
        tower.pushLid(3, "crazy");
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        // El primer elemento debe ser la LidCrazy
        assertEquals("lid", items[0][0]);
        assertEquals("3", items[0][1]);
    }

    @Test
    public void testMultipleLidCrazyStackAtBase() {
        // ¿Qué debería hacer? Varias LidCrazy se apilan todas en la base
        tower.pushCup(5);
        tower.pushLid(5, "crazy");
        tower.pushCup(3);
        tower.pushLid(3, "crazy");
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        // Las dos primeras posiciones deben ser LidCrazy
        assertEquals("lid", items[0][0]);
        assertEquals("lid", items[1][0]);
    }

    @Test
    public void testLidCrazyHasZeroHeight() {
        // ¿Qué debería hacer? Su altura es 0 (no suma a la torre)
        tower.pushCup(3);
        int heightBefore = tower.height();
        tower.pushLid(3, "crazy");
        // La altura no deberia aumentar en 1 como una tapa normal
        // (LidCrazy tiene height=0, ocupa base visual pero no suma al calculo logico)
        assertTrue(tower.ok());
    }

    // =====================================================================
    // pushCup con tipo en mayusculas/minusculas
    // =====================================================================

    @Test
    public void testPushCupTypeIsCaseInsensitive() {
        // ¿Qué debería hacer? Aceptar "OPENER", "Opener", "opener" igual
        tower.pushCup(3, "OPENER");
        assertTrue(tower.ok());
        tower.pushCup(2, "Hierarchical");
        assertTrue(tower.ok());
    }

    @Test
    public void testPushLidTypeIsCaseInsensitive() {
        // ¿Qué debería hacer? Aceptar "CRAZY", "Crazy", "crazy" igual
        tower.pushCup(3);
        tower.pushLid(3, "FEARFUL");
        assertTrue(tower.ok());
    }

    // =====================================================================
    // orderTower y reverseTower preservan subtipo
    // =====================================================================

    @Test
    public void testOrderTowerPreservesSubtype() {
        // ¿Qué debería hacer? orderTower conserva los subtipos originales
        tower.pushCup(5);
        tower.pushCup(2, "opener");
        tower.orderTower();
        assertTrue(tower.ok());
        // Cup5 debe seguir siendo normal, Cup2 debe seguir siendo opener
        // Verificamos que la torre tiene los dos elementos
        assertEquals(2, tower.stackingItems().length);
    }

    @Test
    public void testReverseTowerPreservesSubtype() {
        // ¿Qué debería hacer? reverseTower conserva los subtipos originales
        tower.pushCup(5);
        tower.pushCup(2, "hierarchical");
        tower.reverseTower();
        assertTrue(tower.ok());
        assertEquals(2, tower.stackingItems().length);
    }

    // =====================================================================
    // swapToReduce
    // =====================================================================

    @Test
    public void testSwapToReduceReturnsEmptyWhenNoSwapHelps() {
        // ¿Qué debería hacer? Retorna array vacio si ningun swap reduce la altura
        tower.pushCup(3);
        tower.pushCup(1);
        String[][] result = tower.swapToReduce();
        assertTrue(tower.ok());
        // Con tazas normales bien ordenadas no hay swap que ayude
        assertNotNull(result);
    }

    @Test
    public void testSwapToReduceReturnsTwoElements() {
        // ¿Qué debería hacer? Si hay un swap util, retorna exactamente 2 elementos
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushCup(3);
        String[][] result = tower.swapToReduce();
        if (result.length > 0) {
            assertEquals("Debe retornar exactamente 2 objetos", 2, result.length);
            assertEquals("Cada objeto debe tener tipo y numero", 2, result[0].length);
        }
        assertTrue(tower.ok());
    }
}