/**
 * Resuelve y simula el problema de la maraton de programacion:
 * Stacking Cups
 *
 * Dadas n tazas de alturas 1, 3, 5, ..., 2n-1 y una altura objetivo h,
 * encuentra el orden en que deben colocarse las tazas para que la torre
 * tenga exactamente h cm de altura. Luego puede simular visualmente
 * la solucion usando la clase Tower.
 *
 * IMPORTANTE: Esta clase resuelve el problema de la maraton.
 * La clase Tower se usa UNICAMENTE para simular, no para resolver.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 1.0
 */
public class TowerContest {

    /**
     * Resuelve el problema de la maraton.
     *
     * Dadas n tazas (alturas 1, 3, 5, ..., 2n-1) encuentra el orden
     * en que colocarlas para que la torre mida exactamente h cm.
     *
     *
     * @param n numero de tazas (1 <= n <= 200000)
     * @param h altura objetivo en cm (1 <= h <= 4*10^10)
     * @return String con las alturas de las tazas en el orden de colocacion,
     *         separadas por espacios, o "impossible" si no es posible.
     */
    public String solve(int n, long h) {
        // Altura minima: todas anidadas, solo la mas grande cuenta = 2n-1
        long minHeight = 2L * n - 1;

        // Altura maxima: todas separadas = suma de 1+3+5+...+(2n-1) = n^2
        long maxHeight = (long) n * n;

        // Verificar si h esta en el rango posible
        if (h < minHeight || h > maxHeight) {
            return "impossible";
        }

        // Cuanto extra necesitamos sobre la altura minima
        long extra = h - minHeight;

        // Las contribuciones extra disponibles al sacar la taza i del nido son:
        // taza i contribuye (2i-1 - 1) = 2(i-1) extra, para i = 2..n
        // Es decir: 2, 4, 6, ..., 2(n-1)
        // extra debe ser suma de subconjunto de estos valores
        // Como todos son pares, extra debe ser par
        if (extra % 2 != 0) {
            return "impossible";
        }

        // Necesitamos expresar extra/2 como suma de subconjunto de {1,2,...,n-1}
        long target = extra / 2;

        // Greedy: tomamos los valores mas grandes primero
        // marcamos cuales tazas se "sacan" del nido
        boolean[] extracted = new boolean[n + 1]; // extracted[i] = true si taza i va separada

        for (int i = n - 1; i >= 1 && target > 0; i--) {
            if (i <= target) {
                extracted[i + 1] = true; // taza i+1 tiene contribucion extra i
                target -= i;
            }
        }

        if (target != 0) {
            return "impossible";
        }

        // Construir el orden de colocacion:
        // Las tazas "no extraidas" van anidadas de mayor a menor primero
        // Las tazas "extraidas" van encima, tambien de mayor a menor
        // El orden correcto es: primero las anidadas (de mayor a menor),
        // luego las extraidas (de mayor a menor encima de la torre)

        // En realidad el orden de salida es la secuencia de colocacion:
        // colocamos primero la base (mayor anidada), luego las que van encima

        StringBuilder sb = new StringBuilder();

        // Primero las tazas anidadas de mayor a menor (forman la base)
        for (int i = n; i >= 1; i--) {
            if (!extracted[i]) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(2 * i - 1);
            }
        }

        // Luego las tazas extraidas de mayor a menor (van encima)
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
     *
     * Si existe solucion, crea una torre con las tazas en el orden correcto
     * y la muestra graficamente. Si no existe solucion, muestra un mensaje.
     *
     * La clase Tower se usa UNICAMENTE para la simulacion visual.
     * El calculo de la solucion lo hace solve().
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

        // Parsear la solucion: obtener los indices de las tazas en orden
        String[] parts = result.split(" ");
        int[] order = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            int height = Integer.parseInt(parts[i]);
            order[i] = (height + 1) / 2; // indice de la taza: i = (2i-1+1)/2
        }

        // Crear la torre con ancho n y altura h
        Tower tower = new Tower(n, (int) h);
        tower.makeVisible();

        // Agregar las tazas en el orden de la solucion
        for (int i = 0; i < order.length; i++) {
            tower.pushCup(order[i]);
        }
    }
}
