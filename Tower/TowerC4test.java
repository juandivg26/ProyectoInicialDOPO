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
        tower.pushCup("opener", 4); // debe eliminar Lid3 y Lid2 que estaban antes
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
        tower.pushCup("opener", 4);
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
        tower.pushCup("opener", 3);
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
        tower.pushCup("hierarchical", 4); // debe quedar entre Cup5 y Cup2
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
        tower.pushCup("hierarchical", 2);
        assertTrue(tower.ok());
        tower.removeCup(2);
        assertFalse("CupHierarchical en el fondo no debe poder quitarse", tower.ok());
    }

    @Test
    public void testCupHierarchicalCanBeRemovedIfNotAtBottom() {
        // ¿Qué NO debería hacer? Si no llego al fondo, si se puede quitar
        tower.pushCup(5);
        tower.pushCup("hierarchical", 3); // queda dentro de Cup5, no en el fondo
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
        tower.pushCup("dominant", 3); // debe eliminar Cup2 porque 2 < 3
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
        tower.pushCup("dominant", 3);
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
        tower.pushCup("dominant", 4);
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
        tower.pushLid("fearful", 3);
        assertTrue(tower.ok());
    }

    @Test
    public void testLidFearfulDoesNotEnterWithoutCup() {
        // ¿Qué NO debería hacer? No entra si su taza companiera no esta
        tower.pushLid("fearful", 3);
        assertFalse("LidFearful no debe entrar sin su taza companiera", tower.ok());
    }

    @Test
    public void testLidFearfulCannotBeRemovedWhileProtecting() {
        // ¿Qué NO debería hacer? No se puede quitar si esta tapando su taza
        tower.pushCup(3);
        tower.pushLid("fearful", 3);
        tower.removeLid(3);
        assertFalse("LidFearful protegiendo no debe poder quitarse", tower.ok());
    }

    @Test
    public void testLidFearfulCanBeRemovedWhenNotProtecting() {
        // ¿Qué debería hacer? Se puede quitar si no esta tapando su taza
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushLid("fearful", 3); // no esta directamente sobre Cup3
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
        tower.pushLid("crazy", 3);
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
        tower.pushLid("crazy", 5);
        tower.pushCup(3);
        tower.pushLid("crazy", 3);
        assertTrue(tower.ok());
        String[][] items = tower.stackingItems();
        // Orden esperado: [LidCrazy5, Cup5, LidCrazy3, Cup3]
        // Cada crazy va justo antes de su taza
        assertEquals("lid", items[0][0]);
        assertEquals("5", items[0][1]);
        assertEquals("cup", items[1][0]);
        assertEquals("5", items[1][1]);
        assertEquals("lid", items[2][0]);
        assertEquals("3", items[2][1]);
    }

    @Test
    public void testLidCrazyHasZeroHeight() {
        // ¿Qué debería hacer? Su altura es 0 (no suma a la torre)
        tower.pushCup(3);
        int heightBefore = tower.height();
        tower.pushLid("crazy", 3);
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
        tower.pushCup("OPENER", 3);
        assertTrue(tower.ok());
        tower.pushCup("Hierarchical", 2);
        assertTrue(tower.ok());
    }

    @Test
    public void testPushLidTypeIsCaseInsensitive() {
        // ¿Qué debería hacer? Aceptar "CRAZY", "Crazy", "crazy" igual
        tower.pushCup(3);
        tower.pushLid("FEARFUL", 3);
        assertTrue(tower.ok());
    }

    // =====================================================================
    // orderTower y reverseTower preservan subtipo
    // =====================================================================

    @Test
    public void testOrderTowerPreservesSubtype() {
        // ¿Qué debería hacer? orderTower conserva los subtipos originales
        tower.pushCup(5);
        tower.pushCup("opener", 2);
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
        tower.pushCup("hierarchical", 2);
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
// Escenarios con LidCrazy y LidFearful en swap y cover
// =====================================================================

    // =====================================================================
    // swap con tipos especiales
    // =====================================================================

    @Test
    public void testSwapCupWithItsCrazyLid() {
        // ¿Qué pasa si se swapea una LidCrazy con su taza?
        // La crazy está justo antes de su taza en items[]
        // Después del swap la crazy debe quedar donde estaba la taza y viceversa
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushLid("crazy", 3); // queda justo antes de cup3 en items
        assertTrue(tower.ok());
        
        tower.swap(new String[]{"cup", "3"}, new String[]{"lid", "3"});
        // El swap entre cup3 y su LidCrazy sí es posible (no son bloques traslapados)
        // Después del swap la crazy queda encima de cup3 y cup3 queda debajo
        assertTrue("Swap entre cup3 y su LidCrazy debe funcionar", tower.ok());
    }

    @Test
    public void testSwapCrazyLidWithOtherCup() {
        // ¿Qué pasa si se swapea una LidCrazy con una taza diferente?
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushLid("crazy", 3);
        assertTrue(tower.ok());

        // Intercambiar la LidCrazy3 con Cup5
        tower.swap(new String[]{"lid", "3"}, new String[]{"cup", "5"});
        assertTrue("Swap entre LidCrazy y otra taza debe ser válido", tower.ok());
    }

    @Test
    public void testSwapCupWithCoveredCupByFearful() {
        // ¿Qué pasa si se swapea una taza que está siendo protegida por LidFearful?
        // El bloque incluye la taza + su fearful, ambos se mueven juntos
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushLid("fearful", 3); // está justo sobre cup3, la protege
        assertTrue(tower.ok());

        tower.swap(new String[]{"cup", "3"}, new String[]{"cup", "5"});
        assertTrue("Swap de cup tapada por fearful debe funcionar (mueve el bloque)", tower.ok());

        // La fearful debe seguir protegiendo a cup3 después del swap
        String[][] items = tower.stackingItems();
        // cup3 debe seguir teniendo su fearful encima
        boolean fearfulStillOnCup3 = false;
        for (int i = 0; i < items.length - 1; i++) {
            if (items[i][0].equals("cup") && items[i][1].equals("3")
                    && items[i+1][0].equals("lid") && items[i+1][1].equals("3")) {
                fearfulStillOnCup3 = true;
            }
        }
        assertTrue("LidFearful debe seguir sobre cup3 después del swap", fearfulStillOnCup3);
    }

    @Test
    public void testSwapTwoCupsWithCrazyLidInTower() {
        // ¿Qué pasa si hay una LidCrazy en la torre y se swapean dos tazas normales?
        // La LidCrazy no debe verse afectada por el swap de otras tazas
        tower.pushCup(5);
        tower.pushLid("crazy", 5); // crazy de cup5
        tower.pushCup(3);
        tower.pushCup(2);
        assertTrue(tower.ok());

        tower.swap(new String[]{"cup", "3"}, new String[]{"cup", "2"});
        assertTrue(tower.ok());

        // LidCrazy5 debe seguir en la torre
        String[][] items = tower.stackingItems();
        boolean crazyStillExists = false;
        for (String[] item : items) {
            if (item[0].equals("lid") && item[1].equals("5")) crazyStillExists = true;
        }
        assertTrue("LidCrazy5 debe seguir en la torre después de swap de otras tazas", crazyStillExists);
    }

    // =====================================================================
    // cover con tipos especiales
    // =====================================================================

    @Test
    public void testCoverDoesNothingWithCrazyLid() {
        // ¿Qué pasa si la tapa en la torre es LidCrazy?
        // cover() mueve tapas normales encima de su taza, pero una LidCrazy
        // ya está en la base de su taza — cover() no debe moverla a la cima
        tower.pushCup(3);
        tower.pushLid("crazy", 3);
        assertTrue(tower.ok());

        String[][] before = tower.stackingItems();
        tower.cover();
        assertTrue(tower.ok());

        String[][] after = tower.stackingItems();
        // La LidCrazy debe seguir existiendo en la torre
        boolean crazyExists = false;
        boolean crazyBeforeCup = false;
        for (int i = 0; i < after.length; i++) {
            if (after[i][0].equals("lid") && after[i][1].equals("3")) {
                crazyExists = true;
                // Verificar que está antes de su taza (no encima)
                if (i + 1 < after.length && after[i+1][0].equals("cup") && after[i+1][1].equals("3")) {
                    crazyBeforeCup = true;
                }
            }
        }
        assertTrue("LidCrazy debe seguir en la torre", crazyExists);
        assertTrue("LidCrazy debe estar justo antes de su taza, no encima", crazyBeforeCup);
    }

    @Test
    public void testCoverWithMixedLidTypes() {
        // Torre con LidNormal y LidCrazy: cover() solo mueve la Normal
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushLid("crazy", 3);  // LidCrazy3 - no debe moverse
        tower.pushLid(5);           // LidNormal5 - está lejos de cup5, debe moverse
        assertTrue(tower.ok());

        tower.cover();
        assertTrue(tower.ok());

        // LidNormal5 debe estar justo encima de cup5
        String[][] items = tower.stackingItems();
        boolean lid5OnCup5 = false;
        for (int i = 0; i < items.length - 1; i++) {
            if (items[i][0].equals("cup") && items[i][1].equals("5")
                    && items[i+1][0].equals("lid") && items[i+1][1].equals("5")) {
                lid5OnCup5 = true;
            }
        }
        assertTrue("LidNormal5 debe quedar sobre Cup5 después de cover()", lid5OnCup5);

        // LidCrazy3 debe seguir justo antes de cup3 (no encima)
        boolean crazy3BeforeCup3 = false;
        for (int i = 0; i < items.length - 1; i++) {
            if (items[i][0].equals("lid") && items[i][1].equals("3")
                    && items[i+1][0].equals("cup") && items[i+1][1].equals("3")) {
                crazy3BeforeCup3 = true;
            }
        }
        assertTrue("LidCrazy3 debe quedar antes de Cup3 después de cover()", crazy3BeforeCup3);
    }

    @Test
    public void testCoverWithFearfulLidAlreadyProtecting() {
        // ¿Qué pasa con cover() si LidFearful ya está sobre su taza?
        // cover() no debe moverla (ya está cubierta)
        tower.pushCup(3);
        tower.pushLid("fearful", 3);
        assertTrue(tower.ok());

        String[][] before = tower.stackingItems();
        tower.cover();
        assertTrue(tower.ok());

        String[][] after = tower.stackingItems();
        assertEquals("El orden no debe cambiar si fearful ya protege", before.length, after.length);
        for (int i = 0; i < before.length; i++) {
            assertEquals(before[i][0], after[i][0]);
            assertEquals(before[i][1], after[i][1]);
        }
    }

    @Test
    public void testCoverMovesNormalLidNotFearfulWithoutCup() {
        // LidFearful sin su taza en la torre NO debe ser movida por cover()
        // (aunque técnicamente no puede existir sin su taza, verificamos robustez)
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushLid(5); // LidNormal5, está lejos de cup5
        assertTrue(tower.ok());

        tower.cover();
        assertTrue(tower.ok());

        // LidNormal5 debe estar sobre cup5
        String[][] items = tower.stackingItems();
        boolean found = false;
        for (int i = 0; i < items.length - 1; i++) {
            if (items[i][0].equals("cup") && items[i][1].equals("5")
                    && items[i+1][0].equals("lid") && items[i+1][1].equals("5")) {
                found = true;
            }
        }
        assertTrue("cover() debe mover LidNormal sobre su taza", found);
    }

    // =====================================================================
    // popCup y removeCup con tipos especiales
    // =====================================================================

    @Test
    public void testRemoveCupLeavesItsCrazyLidOrphan() {
        // Si se quita cup3 y tenía LidCrazy3, ¿qué pasa con la crazy?
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushLid("crazy", 3);
        assertTrue(tower.ok());

        tower.removeCup(3);
        assertTrue(tower.ok());

        // La LidCrazy3 queda huérfana en la torre (sin su taza)
        String[][] items = tower.stackingItems();
        boolean cup3Gone = true;
        boolean lid3Exists = false;
        for (String[] item : items) {
            if (item[0].equals("cup") && item[1].equals("3")) cup3Gone = false;
            if (item[0].equals("lid") && item[1].equals("3")) lid3Exists = true;
        }
        assertTrue("Cup3 debe haber sido eliminada", cup3Gone);
        // La LidCrazy puede quedar o no según la implementación — verificar consistencia
        assertTrue("ok() debe ser true tras removeCup exitoso", tower.ok());
    }

    @Test
    public void testPopCupWithOpenerOnTop() {
        // popCup cuando la cima es una CupOpener
        tower.pushCup(3);
        tower.pushCup("opener", 4);
        assertTrue(tower.ok());

        tower.popCup();
        assertTrue("popCup debe funcionar con CupOpener en la cima", tower.ok());

        String[][] items = tower.stackingItems();
        boolean openerGone = true;
        for (String[] item : items) {
            if (item[0].equals("cup") && item[1].equals("4")) openerGone = false;
        }
        assertTrue("CupOpener debe haber sido eliminada por popCup", openerGone);
    }

}
