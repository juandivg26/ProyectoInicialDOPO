package tower;

import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.JOptionPane;

/**
 * Pruebas de aceptacion para el Ciclo 4 - Stacking Cups.
 * Cada prueba se ejecuta en modo VISIBLE para que el usuario pueda
 * ver el comportamiento en pantalla y confirmar si lo acepta.
 * Incluye esperas entre operaciones para facilitar la observacion.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 4.0
 */
public class TowerAtest {

    /** Pausa en milisegundos entre operaciones para que el usuario pueda ver */
    private static final int DELAY = 800;

    /**
     * Espera el tiempo indicado.
     * @param ms milisegundos a esperar
     */
    private void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Pregunta al usuario si acepta lo que ve en pantalla.
     * @param message descripcion de lo que deberia verse
     * @return true si el usuario acepta
     */
    private boolean askUser(String message) {
        int result = JOptionPane.showConfirmDialog(
            null,
            message,
            "Prueba de Aceptacion - Ciclo 4",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    // =====================================================================
    // Prueba de aceptacion 1:
    // CupOpener elimina todas las tapas que le bloquean el camino
    // =====================================================================

    /**
     * Escenario: Una torre con varias tazas tapadas recibe una CupOpener.
     * El usuario debe ver como al entrar la opener, todas las tapas desaparecen
     * y solo quedan las tazas.
     *
     * Pasos visibles:
     * 1. Se construye torre con Cup5+Lid5, Cup3+Lid3, Cup2+Lid2
     * 2. Entra CupOpener 4 - las tres tapas desaparecen
     * 3. La torre queda con 4 tazas sin ninguna tapa
     */
    @Test
    public void acceptanceCupOpenerCleansAllLids() {
        Tower tower = new Tower(6, 50);
        tower.makeVisible();
        wait(DELAY);

        // Construir escenario con tazas tapadas
        tower.pushCup(5);   wait(DELAY);
        tower.pushLid(5);   wait(DELAY);
        tower.pushCup(3);   wait(DELAY);
        tower.pushLid(3);   wait(DELAY);
        tower.pushCup(2);   wait(DELAY);
        tower.pushLid(2);   wait(DELAY);

        JOptionPane.showMessageDialog(null,
            "Estado inicial: Torre con Cup5+Lid5, Cup3+Lid3, Cup2+Lid2.\n" +
            "Ahora entrara una CupOpener 4 que eliminara todas las tapas.",
            "Prueba de Aceptacion 1", JOptionPane.INFORMATION_MESSAGE);

        // Entra la CupOpener
        tower.pushCup(4, "opener");
        wait(DELAY);

        // Verificaciones logicas
        String[][] items = tower.stackingItems();
        int cupCount = 0, lidCount = 0;
        for (String[] item : items) {
            if (item[0].equals("cup")) cupCount++;
            if (item[0].equals("lid"))  lidCount++;
        }
        assertEquals("Deben quedar 4 tazas", 4, cupCount);
        assertEquals("No deben quedar tapas", 0, lidCount);
        assertTrue("La operacion debe ser exitosa", tower.ok());

        // Pregunta de aceptacion al usuario
        boolean accepted = askUser(
            "La torre debe mostrar 4 tazas (Cup2, Cup3, Cup4, Cup5) sin ninguna tapa.\n" +
            "Las tapas desaparecieron al entrar la CupOpener 4.\n\n" +
            "¿El comportamiento visual es correcto?"
        );

        tower.makeInvisible();
        assertTrue("El usuario no acepto la prueba de CupOpener", accepted);
    }

    // =====================================================================
    // Prueba de aceptacion 2:
    // LidCrazy va a la base, LidFearful protege su taza, CupDominant elimina menores
    // =====================================================================

    /**
     * Escenario: Se construye una torre usando los tres tipos nuevos de tapa y taza.
     * El usuario debe ver como cada tipo se comporta de forma especial y diferente.
     *
     * Pasos visibles:
     * 1. Cup5 entra normalmente
     * 2. LidCrazy5 se va a la BASE (no a la cima)
     * 3. Cup3 entra, LidFearful3 la tapa y no se puede quitar
     * 4. Cup1 entra, CupDominant4 elimina Cup1 y Cup3 (menores que 4)
     */
    @Test
    public void acceptanceMixedTypesWorkCorrectlyTogether() {
        Tower tower = new Tower(6, 50);
        tower.makeVisible();
        wait(DELAY);

        // Paso 1: Cup5 entra normal
        tower.pushCup(5);
        wait(DELAY);

        // Paso 2: LidCrazy5 va a la base
        tower.pushLid(5, "crazy");
        wait(DELAY);

        boolean crazyOk = askUser(
            "La LidCrazy 5 debe aparecer en la BASE de la torre (abajo del todo),\n" +
            "y NO encima de la Cup5.\n\n" +
            "¿Se ve correctamente?"
        );
        assertTrue("El usuario no acepto el comportamiento de LidCrazy", crazyOk);

        // Verificacion logica de LidCrazy
        String[][] afterCrazy = tower.stackingItems();
        assertEquals("LidCrazy debe ser el primer elemento", "lid", afterCrazy[0][0]);
        assertEquals("5", afterCrazy[0][1]);

        // Paso 3: Cup3 entra, LidFearful3 la tapa
        tower.pushCup(3);            wait(DELAY);
        tower.pushLid(3, "fearful"); wait(DELAY);

        JOptionPane.showMessageDialog(null,
            "LidFearful 3 tapo a Cup3.\n" +
            "Ahora se intentara quitar la LidFearful 3 — deberia fallar\n" +
            "porque esta protegiendo a su taza.",
            "Prueba de Aceptacion 2", JOptionPane.INFORMATION_MESSAGE);

        tower.removeLid(3); // debe fallar
        assertFalse("LidFearful protegiendo no debe poder quitarse", tower.ok());
        wait(DELAY);

        // Paso 4: Cup1 entra, CupDominant4 elimina menores
        tower.pushCup(1); wait(DELAY);

        JOptionPane.showMessageDialog(null,
            "Cup1 entro a la torre.\n" +
            "Ahora entrara CupDominant 4 que eliminara Cup1 y Cup3 (son menores que 4).",
            "Prueba de Aceptacion 2", JOptionPane.INFORMATION_MESSAGE);

        tower.pushCup(4, "dominant"); wait(DELAY);
        assertTrue("CupDominant debe entrar exitosamente", tower.ok());

        // Verificaciones logicas finales
        String[][] finalItems = tower.stackingItems();
        boolean cup1Exists = false, cup3Exists = false;
        boolean cup4Exists = false, cup5Exists = false;
        boolean lidCrazyExists = false;
        for (String[] item : finalItems) {
            if (item[0].equals("cup") && item[1].equals("1")) cup1Exists = true;
            if (item[0].equals("cup") && item[1].equals("3")) cup3Exists = true;
            if (item[0].equals("cup") && item[1].equals("4")) cup4Exists = true;
            if (item[0].equals("cup") && item[1].equals("5")) cup5Exists = true;
            if (item[0].equals("lid") && item[1].equals("5")) lidCrazyExists = true;
        }
        assertFalse("CupDominant4 debio eliminar Cup1", cup1Exists);
        assertFalse("CupDominant4 debio eliminar Cup3", cup3Exists);
        assertTrue("Cup4 debe estar en la torre", cup4Exists);
        assertTrue("Cup5 debe seguir en la torre", cup5Exists);
        assertTrue("LidCrazy5 en la base debe seguir", lidCrazyExists);

        // Pregunta final de aceptacion
        boolean accepted = askUser(
            "Estado final esperado de la torre:\n" +
            "  - LidCrazy5 en la base\n" +
            "  - Cup5 y Cup4 Dominant visibles\n" +
            "  - Cup1 y Cup3 fueron eliminadas por CupDominant\n\n" +
            "¿El comportamiento visual es correcto?"
        );

        tower.makeInvisible();
        assertTrue("El usuario no acepto la prueba de tipos mixtos", accepted);
    }
} 