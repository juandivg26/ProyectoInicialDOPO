import static org.junit.Assert.*;
import org.junit.*;

/**
 * Propuesta de casos de prueba colectivos para la clase Tower - Ciclo 2.
 * Todas las pruebas se ejecutan en modo invisible.
 *
 * Autores: Madero - Valderrama (MV)
 * Convención: accordingMVShould... / accordingMVShouldNot...
 *
 * @version 2.0
 */
public class TowerCC2Test {

    //Tower(cups)
    
    /**
     * Una torre con 2 tazas debe tener altura 4 (1+3).
     */
    @Test
    public void accordingMVShouldHaveHeightFourWithTwoCups() {
        Tower t = new Tower(2);
        assertEquals(4, t.height());
        assertTrue(t.ok());
    }

    /**
     * Una torre creada con cups debe tener exactamente ese número de elementos.
     */
    @Test
    public void accordingMVShouldHaveCorrectNumberOfItems() {
        Tower t = new Tower(5);
        assertEquals(5, t.stackingItems().length);
        assertTrue(t.ok());
    }

    //swap
    
    /**
     * Hacer swap dos veces entre los mismos elementos debe dejar la torre igual.
     */
    @Test
    public void accordingMVShouldRestoreOrderAfterDoubleSwap() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);
        t.pushCup(2);

        String[][] before = t.stackingItems();
        t.swap(new String[]{"cup", "1"}, new String[]{"cup", "2"});
        t.swap(new String[]{"cup", "1"}, new String[]{"cup", "2"});
        assertTrue(t.ok());

        String[][] after = t.stackingItems();
        assertEquals(before[0][1], after[0][1]);
        assertEquals(before[1][1], after[1][1]);
    }

    /**
     * Intercambiar dos tapas debe funcionar correctamente.
     */
    @Test
    public void accordingMVShouldSwapTwoLids() {
        Tower t = new Tower(5, 30);
        t.pushLid(1);
        t.pushLid(2);

        t.swap(new String[]{"lid", "1"}, new String[]{"lid", "2"});
        assertTrue(t.ok());

        String[][] items = t.stackingItems();
        assertEquals("2", items[0][1]);
        assertEquals("1", items[1][1]);
    }

    //REQUISITO 12 - cover
    
    /**
     * Después de cover, lidedCups debe incluir la taza que fue tapada.
     */
    @Test
    public void accordingMVShouldAppearInLidedCupsAfterCover() {
        Tower t = new Tower(5, 30);
        t.pushCup(2);
        t.pushCup(3);
        t.pushLid(2);

        t.cover();
        assertTrue(t.ok());

        int[] lided = t.lidedCups();
        assertEquals(1, lided.length);
        assertEquals(2, lided[0]);
    }

    /**
     * cover no debe cambiar el número total de elementos de la torre.
     */
    @Test
    public void accordingMVShouldNotChangeNumberOfItemsAfterCover() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);
        t.pushCup(2);
        t.pushLid(1);

        int itemsBefore = t.stackingItems().length;
        t.cover();
        assertTrue(t.ok());
        assertEquals(itemsBefore, t.stackingItems().length);
    }

    //swapToReduce

    /**
     * swapToReduce debe retornar exactamente 2 objetos (los dos a intercambiar).
     */
    @Test
    public void accordingMVShouldReturnTwoObjectsInSwapToReduce() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);
        t.pushCup(3);
        t.pushCup(2);

        String[][] swap = t.swapToReduce();
        assertTrue(t.ok());

        if (swap.length > 0) {
            assertEquals(2, swap.length);
        }
    }

    /**
     * Cada objeto retornado por swapToReduce debe tener tipo y número.
     */
    @Test
    public void accordingMVShouldReturnObjectsWithTypeAndNumber() {
        Tower t = new Tower(5, 30);
        t.pushCup(1);
        t.pushCup(3);
        t.pushCup(2);

        String[][] swap = t.swapToReduce();
        assertTrue(t.ok());

        if (swap.length > 0) {
            assertNotNull(swap[0][0]);
            assertNotNull(swap[0][1]);
            assertNotNull(swap[1][0]);
            assertNotNull(swap[1][1]);
        }
    }
}
