package tower;
   
/**
 * Resuelve y simula el problema de la maraton de programacion:
 * Stacking Cups (Problem J - ICPC 2025).
 *
 * <p>Dadas {@code n} tazas de alturas 1, 3, 5, ..., 2n-1 y una altura objetivo {@code h},
 * encuentra el orden en que deben colocarse las tazas para que la torre tenga
 * exactamente {@code h} cm de altura.
 *
 * <p>El creador masivo y Contest solo usan elementos normales (Cup y Lid).
 *
 * <p>Relaciones:
 * <ul>
 *   <li>TowerContest USA Tower para la simulacion visual (asociacion).</li>
 * </ul>
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 4.0 (Ciclo 4 - movido a paquete tower)
 */
public class TowerContest {

    /**
     * Resuelve el problema de la maraton.
     *
     * <p>Dadas {@code n} tazas (alturas 1, 3, 5, ..., 2n-1) encuentra el orden
     * en que colocarlas para que la torre mida exactamente {@code h} cm.
     *
     * @param n numero de tazas (1 &lt;= n &lt;= 200000)
     * @param h altura objetivo en cm (1 &lt;= h &lt;= 4*10^10)
     * @return String con las alturas de las tazas en el orden de colocacion,
     *         separadas por espacios, o "impossible" si no es posible
     */
    public String solve(int n, long h) {
        long minHeight = 2L * n - 1;
        long maxHeight = (long) n * n;

        if (h < minHeight || h > maxHeight) return "impossible";

        long extra = h - minHeight;
        if (extra % 2 != 0) return "impossible";

        long target = extra / 2;
        boolean[] extracted = new boolean[n + 1];

        for (int i = n - 1; i >= 1 && target > 0; i--) {
            if (i <= target) {
                extracted[i + 1] = true;
                target -= i;
            }
        }

        if (target != 0) return "impossible";

        StringBuilder sb = new StringBuilder();

        // Primero las tazas anidadas de mayor a menor
        for (int i = n; i >= 1; i--) {
            if (!extracted[i]) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(2 * i - 1);
            }
        }

        // Luego las tazas extraidas de mayor a menor
        for (int i = n; i >= 1; i--) {
            if (extracted[i]) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(2 * i - 1);
            }
        }

        return sb.toString();
    }

    /**
     * Simula visualmente la solucion del problema usando la clase Tower.
     * Solo usa Cup normales (segun el requisito del ciclo 4).
     *
     * @param n numero de tazas
     * @param h altura objetivo en cm
     */
    public void simulate(int n, long h) {
        String result = solve(n, h);

        if (result.equals("impossible")) {
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "No es posible construir una torre de " + h + " cm con " + n + " tazas.",
                "Imposible",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String[] parts = result.split(" ");
        int[] order = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            int height = Integer.parseInt(parts[i]);
            order[i] = (height + 1) / 2;
        }

        Tower tower = new Tower(n, (int) h);
        tower.makeVisible();

        for (int idx : order) {
            tower.pushCup(idx);
        }
    }
}
