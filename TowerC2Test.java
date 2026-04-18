package tower;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Casos de prueba de unidad para la clase Tower - Ciclo 2.
 * Todas las pruebas se ejecutan en modo invisible.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 2.0
 */
public class TowerC2Test {

    //Tower(cups): crear torre con n tazas

    /**
     * Crear una torre con 4 tazas debe generar tazas de alturas 1, 3, 5, 7.
     * La altura total debe ser 1+3+5+7 = 16.
     */
    @Test
    public void shouldCreateTowerWithCupsAndCorrectHeight() {
        Tower t = new Tower(4);
        assertEquals(16, t.height());
        assertTrue(t.ok());
    }

    /**
     * Crear una torre con 1 taza debe tener altura 1 (2*1-1 = 1).
     */
    @Test
    public void shouldCreateTowerWithOneCup() {
        Tower t = new Tower(1);
        assertEquals(1, t.height());
        assertTrue(t.ok());
    }

    /**
     * Crear una torre con 0 tazas no debe ser válido (ok() = false).
     */
    @Test
    public void shouldNotCreateTowerWithZeroCups() {
        Tower t = new Tower(0);
        assertFalse(t.ok());
    }

    /**
     * Crear una torre con número negativo de tazas no debe ser válido.
     */
    @Test
    public void shouldNotCreateTowerWithNegativeCups() {
        Tower t = new Tower(-3);
        assertFalse(t.ok());
    }

    /**
     * La torre creada con cups no debe tener tapas (stackingItems solo tazas).
     */
    @Test
    public void shouldCreateTowerWithOnlyCupsNoLids() {
        Tower t = new Tower(3);
        String[][] items = t.stackingItems();
        for (String[] item : items) {
            assertEquals("cup", item[0]);
        }
        assertTrue(t.ok());
    }

    /**
     * Torre con 3 tazas debe tener exactamente 3 items.
     */
    @Test
    public void shouldCreateTowerWithExactNumberOfCups() {
        Tower t = new Tower(3);
        String[][] items = t.stackingItems();
        assertEquals(3, items.length);
        assertTrue(t.ok());
    }

    //swap: intercambiar dos objetos

    /**
     * Intercambiar taza 1 con taza 2 debe cambiar sus posiciones en la torre.
     */
    @Test
    public void shouldSwapTwoCupsSuccessfully() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);
        t.pushCup(2);
        t.pushCup(3);

        // Antes: [cup1, cup2, cup3]
        String[][] before = t.stackingItems();
        assertEquals("1", before[0][1]);
        assertEquals("2", before[1][1]);

        t.swap(new String[]{"cup", "1"}, new String[]{"cup", "2"});
        assertTrue(t.ok());

        String[][] after = t.stackingItems();
        assertEquals("2", after[0][1]);
        assertEquals("1", after[1][1]);
    }

    /**
     * Intercambiar una taza con una tapa debe funcionar correctamente.
     */
    @Test
    public void shouldSwapCupWithLid() {
        Tower t = new Tower(5, 30);
        t.pushCup(2);
        t.pushLid(3);

        t.swap(new String[]{"cup", "2"}, new String[]{"lid", "3"});
        assertTrue(t.ok());

        String[][] items = t.stackingItems();
        assertEquals("lid", items[0][0]);
        assertEquals("cup", items[1][0]);
    }

    /**
     * Intercambiar un objeto consigo mismo no debe ser válido.
     */
    @Test
    public void shouldNotSwapItemWithItself() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);
        t.pushCup(2);

        t.swap(new String[]{"cup", "1"}, new String[]{"cup", "1"});
        assertFalse(t.ok());
    }

    /**
     * Intercambiar un objeto que no existe en la torre no debe ser válido.
     */
    @Test
    public void shouldNotSwapNonExistentItem() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);
        t.pushCup(2);

        t.swap(new String[]{"cup", "1"}, new String[]{"cup", "5"});
        assertFalse(t.ok());
    }

    /**
     * Intercambiar con parámetros nulos no debe ser válido.
     */
    @Test
    public void shouldNotSwapWithNullParameters() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);

        t.swap(null, new String[]{"cup", "1"});
        assertFalse(t.ok());
    }

    /**
     * Intercambiar con número inválido (no numérico) no debe ser válido.
     */
    @Test
    public void shouldNotSwapWithInvalidNumber() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);
        t.pushCup(2);

        t.swap(new String[]{"cup", "uno"}, new String[]{"cup", "2"});
        assertFalse(t.ok());
    }
    
    //cover: tapar tazas que tienen su tapa
    
    /**
     * cover() debe mover la tapa justo encima de su taza correspondiente.
     */
    @Test
    public void shouldCoverCupsWithMatchingLids() {
        Tower t = new Tower(5, 30);
        t.pushCup(2);
        t.pushCup(3);
        t.pushLid(2); // tapa de la taza 2, pero esta encima de cup3

        t.cover();
        assertTrue(t.ok());

        // Verificar que la tapa 2 esta justo despues de la taza 2
        String[][] items = t.stackingItems();
        boolean found = false;
        for (int i = 0; i < items.length - 1; i++) {
            if (items[i][0].equals("cup") && items[i][1].equals("2")
                    && items[i+1][0].equals("lid") && items[i+1][1].equals("2")) {
                found = true;
            }
        }
        assertTrue(found);
    }

    /**
     * cover() sobre una torre sin tapas no debe cambiar nada ni lanzar error.
     */
    @Test
    public void shouldNotFailCoverWithNoLids() {
        Tower t = new Tower(3);
        int heightBefore = t.height();
        t.cover();
        assertTrue(t.ok());
        assertEquals(heightBefore, t.height());
    }

    /**
     * Una taza ya tapada no debe ser movida de nuevo por cover().
     */
    @Test
    public void shouldNotMoveLidAlreadyOnItsCup() {
        Tower t = new Tower(5, 30);
        t.pushCup(2);
        t.pushLid(2); // ya está tapada

        String[][] before = t.stackingItems();
        t.cover();
        assertTrue(t.ok());

        String[][] after = t.stackingItems();
        assertEquals(before.length, after.length);
        for (int i = 0; i < before.length; i++) {
            assertEquals(before[i][0], after[i][0]);
            assertEquals(before[i][1], after[i][1]);
        }
    }

    /**
     * Tapa sin taza correspondiente en la torre no debe moverse con cover().
     */
    @Test
    public void shouldNotCoverLidWithoutMatchingCup() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);
        t.pushLid(3); // no hay cup 3

        String[][] before = t.stackingItems();
        t.cover();
        assertTrue(t.ok());

        String[][] after = t.stackingItems();
        assertEquals(before.length, after.length);
    }

    //swapToReduce: consultar intercambio que reduce altura
    
    /**
     * swapToReduce debe retornar un intercambio que efectivamente reduce la altura.
     */
    @Test
    public void shouldReturnSwapThatReducesHeight() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);
        t.pushCup(3);
        t.pushCup(2);

        int heightBefore = t.height();
        String[][] swap = t.swapToReduce();
        assertTrue(t.ok());

        if (swap.length > 0) {
            t.swap(swap[0], swap[1]);
            assertTrue(t.height() < heightBefore);
        }
    }

    /**
     * Si la torre ya está en su configuración de menor altura posible,
     * swapToReduce debe retornar un arreglo vacío.
     */
    @Test
    public void shouldReturnEmptyWhenNoSwapReducesHeight() {
        Tower t = new Tower(5, 30);
        // Orden de mayor a menor ya es el optimo para altura minima
        t.pushCup(3);
        t.pushCup(2);
        t.pushCup(1);

        String[][] swap = t.swapToReduce();
        assertTrue(t.ok());
        assertEquals(0, swap.length);
    }

    /**
     * swapToReduce sobre torre vacía debe retornar arreglo vacío sin error.
     */
    @Test
    public void shouldReturnEmptySwapOnEmptyTower() {
        Tower t = new Tower(5, 30);
        String[][] swap = t.swapToReduce();
        assertTrue(t.ok());
        assertEquals(0, swap.length);
    }

    /**
     * swapToReduce sobre torre con un solo elemento debe retornar vacío.
     */
    @Test
    public void shouldReturnEmptySwapWithSingleElement() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);

        String[][] swap = t.swapToReduce();
        assertTrue(t.ok());
        assertEquals(0, swap.length);
    }

    //lidedCups: tazas tapadas
    
    /**
     * lidedCups debe retornar los números de las tazas tapadas en orden ascendente.
     */
    @Test
    public void shouldReturnLidedCupsInAscendingOrder() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);
        t.pushLid(1);
        t.pushCup(3);
        t.pushLid(3);

        int[] lided = t.lidedCups();
        assertTrue(t.ok());
        assertEquals(2, lided.length);
        assertEquals(1, lided[0]);
        assertEquals(3, lided[1]);
    }

    /**
     * lidedCups debe retornar arreglo vacío si no hay ninguna taza tapada.
     */
    @Test
    public void shouldReturnEmptyLidedCupsWhenNoneAreCovered() {
        Tower t = new Tower(3);
        int[] lided = t.lidedCups();
        assertTrue(t.ok());
        assertEquals(0, lided.length);
    }

    /**
     * Una tapa que no está justo sobre su taza no debe contar como tapada.
     */
    @Test
    public void shouldNotCountLidNotDirectlyOnCupAsLided() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);
        t.pushCup(2);
        t.pushLid(1); // lid1 no esta justo sobre cup1

        int[] lided = t.lidedCups();
        assertTrue(t.ok());
        assertEquals(0, lided.length);
    }
}
