import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Simulador de torre de tazas y tapas.
 * 
 * Relaciones:
 * - Tower TIENE una coleccion de StackItem (agregacion).
 *   Un StackItem puede ser Cup o Lid (polimorfismo).
 * - Tower USA Rectangle para el marco visual (asociacion).
 * - Cup y Lid HEREDAN de StackItem (herencia).
 * 
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 3.0
 */
public class Tower { 
    
    private int width;
    private int maxHeight;
    private boolean visible;
    private boolean lastOk;
    
    private ArrayList<StackItem> items;
    
    private Rectangle towerBase;
    private Rectangle towerLeft;
    private Rectangle towerRight;
    private ArrayList<Rectangle> heightMarks;
    
    private int tBaseX, tBaseY;
    private int tLeftX, tLeftY;
    private int tRightX, tRightY;
    private ArrayList<Integer> markX;
    private ArrayList<Integer> markY;
    
    private static final String[] COLORS = {
        "red", "blue", "green", "yellow", "magenta", "black"
    };
    
    private static final int SCALE = 15;
    private static final int FRAME = 2;
    private static final int CENTER_X = 150;
    private static final int BASE_Y = 280;
    private static final int DEFAULT_X = 70;
    private static final int DEFAULT_Y = 15;

    /**
     * Crea una torre con el ancho y alto maximo dados (en cm).
     * @param width ancho maximo en cm
     * @param maxHeight alto maximo en cm
     */
    public Tower(int width, int maxHeight) {
        this.visible = false;
        this.items = new ArrayList<StackItem>();
        
        if (width <= 0 || maxHeight <= 0) {
            this.width = 1;
            this.maxHeight = 1;
            this.lastOk = false;
        } else {
            this.width = width;
            this.maxHeight = maxHeight;
            this.lastOk = true;
        }
        
        createFrame();
    }
    
    /**
     * Crea una torre con las tazas de 1 a cups.
     * El ancho es cups y el alto es la suma de las alturas.
     * @param cups numero de tazas a crear
     */
    public Tower(int cups) {
        this.visible = false;
        this.items = new ArrayList<StackItem>();
        
        if (cups <= 0) {
            this.width = 1;
            this.maxHeight = 1;
            this.lastOk = false;
            createFrame();
            return;
        }
        
        this.width = cups;
        
        int totalHeight = 0;
        for (int i = 1; i <= cups; i++) {
            totalHeight += (2 * i - 1);
        }
        this.maxHeight = totalHeight;
        
        createFrame();
        
        for (int i = 1; i <= cups; i++) {
            StackItem cup = new Cup(i, getColor(i));
            items.add(cup);
        }
        
        this.lastOk = true;
    }
    
    // TAZAS 
    
    /**
     * Anade la taza i a la cima de la torre.
     * @param i numero de la taza (altura = 2i-1)
     */
    public void pushCup(int i) {
        if (i <= 0) {
            lastOk = false;
            showError("El numero de taza debe ser positivo.");
            return;
        }
        if (existsItem("cup", i)) {
            lastOk = false;
            showError("Ya existe la taza " + i + ".");
            return;
        }
        int cupHeight = 2 * i - 1;
        if (i > width || computeHeight() + cupHeight > maxHeight) {
            lastOk = false;
            showError("La taza " + i + " no cabe en la torre.");
            return;
        }
        
        StackItem cup = new Cup(i, getColor(i));
        items.add(cup);
        lastOk = true;
        redraw();
    }
    
    /**
     * Elimina la taza de la cima (si la cima es una taza).
     * Si la taza esta tapada, elimina tambien la tapa.
     */
    public void popCup() {
        if (items.isEmpty()) {
            lastOk = false;
            showError("La torre esta vacia.");
            return;
        }
        StackItem top = items.get(items.size() - 1);
        if (!top.getType().equals("cup")) {
            lastOk = false;
            showError("El elemento en la cima no es una taza.");
            return;
        }
        int idx = items.size() - 1;
        // Si esta tapada, eliminar primero la tapa
        if (isCupCovered(idx)) {
            items.get(idx + 1).erase();
            items.remove(idx + 1);
        }
        top.erase();
        items.remove(idx);
        lastOk = true;
        redraw();
    }
    
    /**
     * Elimina la taza i de cualquier posicion en la torre.
     * Si la taza esta tapada, elimina tambien la tapa ya que forman un solo objeto.
     * @param i numero de la taza a eliminar
     */
    public void removeCup(int i) {
        int idx = findItem("cup", i);
        if (idx == -1) {
            lastOk = false;
            showError("La taza " + i + " no esta en la torre.");
            return;
        }
        // Si la taza esta tapada, eliminar tambien la tapa
        if (isCupCovered(idx)) {
            items.get(idx + 1).erase();
            items.remove(idx + 1);
        }
        items.get(idx).erase();
        items.remove(idx);
        lastOk = true;
        redraw();
    }
    
    // TAPAS
    
    /**
     * Anade la tapa i a la cima de la torre.
     * @param i numero de la tapa (altura = 1 cm)
     */
    public void pushLid(int i) {
        if (i <= 0) {
            lastOk = false;
            showError("El numero de tapa debe ser positivo.");
            return;
        }
        if (existsItem("lid", i)) {
            lastOk = false;
            showError("Ya existe la tapa " + i + ".");
            return;
        }
        if (i > width || computeHeight() + 1 > maxHeight) {
            lastOk = false;
            showError("La tapa " + i + " no cabe en la torre.");
            return;
        }
        
        StackItem lid = new Lid(i, getColor(i));
        items.add(lid);
        lastOk = true;
        redraw();
    }
    
    /**
     * Elimina la tapa de la cima (si la cima es una tapa).
     */
    public void popLid() {
        if (items.isEmpty()) {
            lastOk = false;
            showError("La torre esta vacia.");
            return;
        }
        StackItem top = items.get(items.size() - 1);
        if (!top.getType().equals("lid")) {
            lastOk = false;
            showError("El elemento en la cima no es una tapa.");
            return;
        }
        top.erase();
        items.remove(items.size() - 1);
        lastOk = true;
        redraw();
    }
    
    /**
     * Elimina la tapa i de cualquier posicion en la torre.
     * Si la tapa estaba sobre su taza (tapandola), la taza queda destapada.
     * @param i numero de la tapa a eliminar
     */
    public void removeLid(int i) {
        int idx = findItem("lid", i);
        if (idx == -1) {
            lastOk = false;
            showError("La tapa " + i + " no esta en la torre.");
            return;
        }
        // Si habia una taza tapada justo debajo, destaparla
        if (idx > 0) {
            StackItem below = items.get(idx - 1);
            if (below.getType().equals("cup") && below.getNumber() == i) {
                below.setCovered(false);
            }
        }
        items.get(idx).erase();
        items.remove(idx);
        lastOk = true;
        redraw();
    }
    
    //ORGANIZAR
    
    /**
     * Ordena de mayor a menor. El menor queda en la cima.
     * Si taza y tapa del mismo numero estan en la torre, la tapa va sobre la taza.
     * No se descartan elementos: todos se conservan en el nuevo orden.
     */
    public void orderTower() {
        // Guardar todos los items actuales
        ArrayList<String> oldTypes = new ArrayList<String>();
        ArrayList<Integer> oldNumbers = new ArrayList<Integer>();
        for (int i = 0; i < items.size(); i++) {
            oldTypes.add(items.get(i).getType());
            oldNumbers.add(items.get(i).getNumber());
        }
        
        // Obtener numeros unicos y ordenarlos de mayor a menor
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < oldNumbers.size(); i++) {
            int num = oldNumbers.get(i);
            if (!numbers.contains(num)) {
                numbers.add(num);
            }
        }
        sortDescending(numbers);
        
        eraseAllItems();
        items.clear();
        
        // Reconstruir: para cada numero (mayor a menor),
        // primero la taza si existe, luego la tapa si existe
        for (int i = 0; i < numbers.size(); i++) {
            int num = numbers.get(i);
            boolean hasCup = hasInLists(oldTypes, oldNumbers, "cup", num);
            boolean hasLid = hasInLists(oldTypes, oldNumbers, "lid", num);
            
            if (hasCup) {
                items.add(new Cup(num, getColor(num)));
            }
            if (hasLid) {
                items.add(new Lid(num, getColor(num)));
            }
        }
        
        lastOk = true;
        redraw();
    }
    
    /**
     * Invierte el orden de los elementos.
     * Las tazas tapadas se tratan como una unidad: taza+tapa se invierten juntas.
     * No se descartan elementos: todos los items se conservan en orden inverso.
     */
    public void reverseTower() {
        // Construir lista de "unidades" a invertir.
        // Una taza tapada cuenta como una unidad de dos elementos.
        ArrayList<ArrayList<StackItem>> units = new ArrayList<ArrayList<StackItem>>();
        
        int i = 0;
        while (i < items.size()) {
            ArrayList<StackItem> unit = new ArrayList<StackItem>();
            unit.add(items.get(i));
            // Si es una taza tapada, incluir la tapa en la misma unidad
            if (isCupCovered(i)) {
                unit.add(items.get(i + 1));
                i += 2;
            } else {
                i++;
            }
            units.add(unit);
        }
        
        // Borrar visualmente todos los items
        eraseAllItems();
        items.clear();
        
        // Reconstruir en orden inverso de unidades
        for (int u = units.size() - 1; u >= 0; u--) {
            ArrayList<StackItem> unit = units.get(u);
            for (int k = 0; k < unit.size(); k++) {
                StackItem si = unit.get(k);
                // Recrear el item para que se dibuje correctamente
                items.add(createItem(si.getType(), si.getNumber()));
            }
        }
        
        lastOk = true;
        redraw();
    }
    
    /**
     * Intercambia la posicion de dos objetos en la torre.
     * @param o1 primer objeto {tipo, numero}
     * @param o2 segundo objeto {tipo, numero}
     */
    public void swap(String[] o1, String[] o2) {
        if (o1 == null || o2 == null || o1.length != 2 || o2.length != 2) {
            lastOk = false;
            showError("Los objetos deben tener tipo y numero.");
            return;
        }
    
        String type1 = o1[0];
        String type2 = o2[0];
        int num1, num2;
    
        try {
            num1 = Integer.parseInt(o1[1]);
            num2 = Integer.parseInt(o2[1]);
        } catch (Exception e) {
            lastOk = false;
            showError("Los numeros no son validos.");
            return;
        }
    
        int idx1 = findItem(type1, num1);
        int idx2 = findItem(type2, num2);
    
        if (idx1 == -1 || idx2 == -1) {
            lastOk = false;
            showError("No se encontro alguno de los objetos.");
            return;
        }
        if (idx1 == idx2) {
            lastOk = false;
            showError("No se puede intercambiar un objeto consigo mismo.");
            return;
        }
     
        // Normalizar: bloque1 siempre antes que bloque2
        if (idx1 > idx2) {
            int tmpI = idx1; idx1 = idx2; idx2 = tmpI;
            String tmpT = type1; type1 = type2; type2 = tmpT;
            int tmpN = num1; num1 = num2; num2 = tmpN;
        }
    
        ArrayList<StackItem> block1 = getBloque(idx1);
        ArrayList<StackItem> block2 = getBloque(idx2);
    
        int start1 = idx1;
        int end1 = idx1 + block1.size() - 1;
        int start2 = idx2;
        int end2 = idx2 + block2.size() - 1;
    
        // Si se traslapan, invalidar (protección extra)
        if (end1 >= start2) {
            lastOk = false;
            showError("No se puede intercambiar bloques traslapados.");
            return;
        }
    
        ArrayList<StackItem> newItems = new ArrayList<StackItem>();
        int i = 0;
        while (i < items.size()) {
            if (i == start1) {
                for (int k = 0; k < block2.size(); k++) newItems.add(block2.get(k));
                i = end1 + 1; // saltar bloque1 original
            } else if (i == start2) {
                for (int k = 0; k < block1.size(); k++) newItems.add(block1.get(k));
                i = end2 + 1; // saltar bloque2 original
            } else {
                newItems.add(items.get(i));
                i++;
            }
        }
    
        items = newItems;
        lastOk = true;
        redraw();
    }

    
    /**
     * Tapa las tazas que tienen sus tapas en la torre.
     * Mueve cada tapa justo encima de su taza correspondiente.
     */
    public void cover() {
        ArrayList<Integer> cupsToCover = new ArrayList<Integer>();
        
        for (int i = 0; i < items.size(); i++) {
            StackItem si = items.get(i);
            if (si.getType().equals("cup")) {
                if (existsItem("lid", si.getNumber())) {
                    boolean alreadyCovered = false;
                    if (i + 1 < items.size()) {
                        StackItem next = items.get(i + 1);
                        if (next.getType().equals("lid") 
                            && next.getNumber() == si.getNumber()) {
                            alreadyCovered = true;
                        }
                    }
                    if (!alreadyCovered) {
                        cupsToCover.add(si.getNumber());
                    }
                }
            }
        }
        
        for (int c = 0; c < cupsToCover.size(); c++) {
            int cupNum = cupsToCover.get(c);
            
            int lidIdx = findItem("lid", cupNum);
            if (lidIdx == -1) continue;
            
            StackItem lid = items.get(lidIdx);
            items.remove(lidIdx);
            
            int cupIdx = findItem("cup", cupNum);
            if (cupIdx == -1) {
                items.add(lid);
                continue;
            }
            
            items.add(cupIdx + 1, lid);
        }
        
        lastOk = true;
        redraw();
    }
    
    //CONSULTAS
    
    /**
     * Retorna la altura total de la torre en cm.
     * @return altura total en cm
     */
    public int height() {
        lastOk = true;
        return computeHeight();
    }
    
    /**
     * Retorna los numeros de las tazas tapadas.
     * Una taza esta tapada si su tapa esta justo encima.
     * @return array con los numeros de las tazas tapadas en orden ascendente
     */
    public int[] lidedCups() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        
        for (int i = 0; i < items.size() - 1; i++) {
            StackItem current = items.get(i);
            StackItem next = items.get(i + 1);
            
            if (current.getType().equals("cup") && next.getType().equals("lid")
                && current.getNumber() == next.getNumber()) {
                result.add(current.getNumber());
            }
        }
        
        sortAscending(result);
        
        int[] arr = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            arr[i] = result.get(i);
        }
        lastOk = true;
        return arr;
    }
    
    /**
     * Retorna informacion de los elementos de base a cima.
     * @return array 2D con {tipo, numero} de cada elemento
     */
    public String[][] stackingItems() {
        String[][] result = new String[items.size()][2];
        for (int i = 0; i < items.size(); i++) {
            result[i][0] = items.get(i).getType();
            result[i][1] = "" + items.get(i).getNumber();
        }
        lastOk = true;
        return result;
    }
    
    /**
     * Retorna un intercambio que reduzca la altura.
     * @return los dos objetos a intercambiar, o array vacio si no hay
     */
    public String[][] swapToReduce() {
        int currentH = computeHeight();
        
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size(); j++) {
                // Simular intercambio
                StackItem temp = items.get(i);
                items.set(i, items.get(j));
                items.set(j, temp);
                
                int newH = computeHeight();
                
                // Deshacer intercambio
                temp = items.get(i);
                items.set(i, items.get(j));
                items.set(j, temp);
                
                if (newH < currentH) {
                    String[][] result = new String[2][2];
                    result[0][0] = items.get(i).getType();
                    result[0][1] = "" + items.get(i).getNumber();
                    result[1][0] = items.get(j).getType();
                    result[1][1] = "" + items.get(j).getNumber();
                    lastOk = true;
                    return result;
                }
            }
        }
        
        lastOk = true;
        return new String[0][0];
    }
    
    //VISIBILIDAD
    
    /**
     * Hace visible la torre y todos sus elementos.
     */
    public void makeVisible() {
        visible = true;
        lastOk = true;
        redraw();
    }
    
    /**
     * Hace invisible la torre y todos sus elementos.
     */
    public void makeInvisible() {
        visible = false;
        eraseAllItems();
        eraseFrame();
        lastOk = true;
    }
    
    /**
     * Cierra el simulador.
     */
    public void exit() {
        makeInvisible();
        items.clear();
        lastOk = true;
        System.exit(0);
    }
    
    /**
     * Retorna si la ultima operacion fue exitosa.
     * @return true si la ultima operacion fue exitosa
     */
    public boolean ok() {
        return lastOk;
    }
    
    //METODOS PRIVADOS
    
    /**
     * Calcula la altura real de la torre considerando el anidado de tazas.
     *
     * Regla fisica del problema:
     * - Cada elemento se coloca encima del anterior.
     * - Una taza pequeña (indice menor) PUEDE ir dentro de una grande (indice mayor).
     * - Cuando una taza esta dentro de otra, su base queda elevada 1 cm
     *   (el grosor de la base de la taza contenedora).
     * - La altura real de la torre es la posicion Y maxima alcanzada por cualquier elemento.
     *
     * Algoritmo:
     * Llevamos un registro de la "taza contenedora actual" (la mas grande abierta).
     * Cuando colocamos un elemento:
     *   - Si es una taza que CABE dentro de la contenedora actual (numero menor),
     *     su base empieza en (base_contenedora + 1) y su tope es (base + altura_taza).
     *   - Si NO cabe (numero mayor o es tapa sin contenedora), se apila encima del tope actual.
     * La altura de la torre es el tope maximo alcanzado.
     *
     * @return altura total real en cm
     */
    private int computeHeight() {
        if (items.isEmpty()) return 0;

        // Pila de contenedoras: guarda el tope de cada taza contenedora abierta
        // Usamos un arreglo simple como pila
        int[] containerTop = new int[items.size()];
        int[] containerNumber = new int[items.size()];
        int stackSize = 0;

        int towerTop = 0; // maximo tope alcanzado
        int currentBase = 0; // base donde se coloca el proximo elemento

        for (int i = 0; i < items.size(); i++) {
            StackItem si = items.get(i);

            if (si.getType().equals("cup")) {
                int cupNum = si.getNumber();
                int cupHeight = si.getHeight(); // 2*num - 1

                // Buscar si cabe dentro de alguna contenedora abierta
                // Cabe dentro de la contenedora si cupNum < containerNumber
                // Buscar la contenedora mas pequeña que la contenga
                // (la que esta en el tope de la pila de contenedoras)
                while (stackSize > 0 && containerNumber[stackSize - 1] <= cupNum) {
                    // Esta contenedora no puede contener esta taza, cerrarla
                    stackSize--;
                }

                if (stackSize > 0) {
                    // La taza cabe dentro de la contenedora del tope de la pila
                    // Su base empieza 1 cm arriba de la base de la contenedora
                    // (el grosor de la base de la contenedora)
                    currentBase = containerTop[stackSize - 1] - (2 * containerNumber[stackSize - 1] - 1) + 1;
                } else {
                    // No hay contenedora: se apila encima del tope actual
                    currentBase = towerTop;
                }

                int cupTop = currentBase + cupHeight;
                if (cupTop > towerTop) towerTop = cupTop;

                // Esta taza puede ser contenedora para las siguientes
                containerNumber[stackSize] = cupNum;
                containerTop[stackSize] = cupTop;
                stackSize++;

            } else {
                // Es una tapa (height = 1), se apila encima del tope actual
                int lidTop = towerTop + 1;
                if (lidTop > towerTop) towerTop = lidTop;
            }
        }

        return towerTop;
    }
    
    /**
     * Retorna el color asignado a un numero de forma ciclica.
     * @param number numero del elemento
     * @return color asignado
     */
    private String getColor(int number) {
        return COLORS[(number - 1) % COLORS.length];
    }
    
    /**
     * Crea un StackItem (Cup o Lid) segun el tipo indicado.
     * Usa polimorfismo: retorna StackItem pero crea Cup o Lid.
     * @param type "cup" o "lid"
     * @param number numero del elemento
     * @return el StackItem creado
     */
    private StackItem createItem(String type, int number) {
        if (type.equals("cup")) {
            return new Cup(number, getColor(number));
        } else {
            return new Lid(number, getColor(number));
        }
    }
    
    /**
     * Retorna el bloque de items que corresponden a un elemento y su contenido.
     * Para una taza abierta: solo la taza (bloque de 1).
     * Para una taza tapada: la taza + todo su contenido + su tapa.
     * Para una tapa o item sin contenido: bloque de 1.
     * @param idx indice del item en la lista
     * @return lista de StackItems que forman el bloque
     */
    private ArrayList<StackItem> getBloque(int idx) {
        ArrayList<StackItem> bloque = new ArrayList<StackItem>();
        if (idx < 0 || idx >= items.size()) return bloque;
        StackItem si = items.get(idx);
        bloque.add(si);
        if (!si.getType().equals("cup")) return bloque;
        // Si esta tapada, incluir todo hasta la tapa (inclusive)
        if (isCupCovered(idx)) {
            // Incluir items internos + tapa
            int cupNum = si.getNumber();
            for (int k = idx + 1; k < items.size(); k++) {
                bloque.add(items.get(k));
                // Parar cuando encontramos la tapa de esta taza
                if (items.get(k).getType().equals("lid") && items.get(k).getNumber() == cupNum) {
                    break;
                }
            }
        }
        return bloque;
    }

    /**
     * Verifica si la taza en el indice dado esta tapada.
     * Una taza esta tapada si el elemento justo siguiente es su tapa.
     * @param cupIdx indice de la taza en items
     * @return true si la taza tiene su tapa justo encima
     */
    private boolean isCupCovered(int cupIdx) {
        if (cupIdx < 0 || cupIdx >= items.size()) return false;
        StackItem cup = items.get(cupIdx);
        if (!cup.getType().equals("cup")) return false;
        if (cupIdx + 1 >= items.size()) return false;
        StackItem next = items.get(cupIdx + 1);
        return next.getType().equals("lid") && next.getNumber() == cup.getNumber();
    }
    
    /**
     * Verifica si existe un item con el tipo y numero dados.
     * @param type tipo del item ("cup" o "lid")
     * @param number numero del item
     * @return true si existe
     */
    private boolean existsItem(String type, int number) {
        for (int i = 0; i < items.size(); i++) {
            StackItem si = items.get(i);
            if (si.getType().equals(type) && si.getNumber() == number) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Busca la posicion de un item con el tipo y numero dados.
     * @param type tipo del item ("cup" o "lid")
     * @param number numero del item
     * @return indice del item, o -1 si no existe
     */
    private int findItem(String type, int number) {
        for (int i = 0; i < items.size(); i++) {
            StackItem si = items.get(i);
            if (si.getType().equals(type) && si.getNumber() == number) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Verifica si un tipo y numero existen en dos listas paralelas.
     * @param types lista de tipos
     * @param nums lista de numeros
     * @param type tipo a buscar
     * @param number numero a buscar
     * @return true si se encuentra la combinacion
     */
    private boolean hasInLists(ArrayList<String> types, ArrayList<Integer> nums,
                               String type, int number) {
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).equals(type) && nums.get(i) == number) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Borra todos los items de la pantalla.
     */
    private void eraseAllItems() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).erase();
        }
    }
    
    /**
     * Muestra un mensaje de error si la torre es visible.
     * @param message mensaje de error
     */
    private void showError(String message) {
        if (visible) {
            JOptionPane.showMessageDialog(null, message, "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Ordena una lista de enteros de mayor a menor usando bubble sort.
     * @param list lista a ordenar
     */
    private void sortDescending(ArrayList<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1 - i; j++) {
                if (list.get(j) < list.get(j + 1)) {
                    int temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
    
    /**
     * Ordena una lista de enteros de menor a mayor usando bubble sort.
     * @param list lista a ordenar
     */
    private void sortAscending(ArrayList<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1 - i; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    int temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
    
    /**
     * Actualiza el estado visual de las tazas tapadas.
     * Recorre todos los items y marca como tapada la taza
     * que tiene su tapa justo encima.
     */
    private void updateCoveredStatus() {
        for (int i = 0; i < items.size(); i++) {
            StackItem si = items.get(i);
            if (si.getType().equals("cup")) {
                boolean isCovered = false;
                if (i + 1 < items.size()) {
                    StackItem next = items.get(i + 1);
                    if (next.getType().equals("lid") 
                        && next.getNumber() == si.getNumber()) {
                        isCovered = true;
                    }
                }
                si.setCovered(isCovered);
            }
        }
    }
    
    // ---- Marco visual de la torre ----
    
    /**
     * Crea los rectangulos del marco de la torre.
     */
    private void createFrame() {
        int widthPx = width * SCALE;
        int heightPx = maxHeight * SCALE;
        
        towerBase = new Rectangle();
        towerBase.changeSize(FRAME, widthPx + 2 * FRAME);
        towerBase.changeColor("black");
        tBaseX = DEFAULT_X;
        tBaseY = DEFAULT_Y;
        
        towerLeft = new Rectangle();
        towerLeft.changeSize(heightPx + FRAME, FRAME);
        towerLeft.changeColor("black");
        tLeftX = DEFAULT_X;
        tLeftY = DEFAULT_Y;
        
        towerRight = new Rectangle();
        towerRight.changeSize(heightPx + FRAME, FRAME);
        towerRight.changeColor("black");
        tRightX = DEFAULT_X;
        tRightY = DEFAULT_Y;
        
        heightMarks = new ArrayList<Rectangle>();
        markX = new ArrayList<Integer>();
        markY = new ArrayList<Integer>();
        for (int i = 0; i < maxHeight; i++) {
            Rectangle m = new Rectangle();
            m.changeSize(1, widthPx);
            m.changeColor("black");
            heightMarks.add(m);
            markX.add(DEFAULT_X);
            markY.add(DEFAULT_Y);
        }
    }
    
    /**
     * Mueve un rectangulo del marco a una posicion.
     */
    private void moveRect(Rectangle rect, int curX, int curY,
                          int tarX, int tarY) {
        int dx = tarX - curX;
        int dy = tarY - curY;
        if (dx != 0) rect.moveHorizontal(dx);
        if (dy != 0) rect.moveVertical(dy);
    }
    
    /**
     * Dibuja el marco de la torre.
     */
    private void drawFrame() {
        if (!visible) return;
        int widthPx = width * SCALE;
        int heightPx = maxHeight * SCALE;
        int leftX = CENTER_X - widthPx / 2;
        
        int bx = leftX - FRAME;
        int by = BASE_Y;
        moveRect(towerBase, tBaseX, tBaseY, bx, by);
        tBaseX = bx;
        tBaseY = by;
        towerBase.makeVisible();
        
        int ly = BASE_Y - heightPx;
        moveRect(towerLeft, tLeftX, tLeftY, leftX - FRAME, ly);
        tLeftX = leftX - FRAME;
        tLeftY = ly;
        towerLeft.makeVisible();
        
        int rx = leftX + widthPx;
        moveRect(towerRight, tRightX, tRightY, rx, ly);
        tRightX = rx;
        tRightY = ly;
        towerRight.makeVisible();
        
        for (int i = 0; i < maxHeight; i++) {
            Rectangle m = heightMarks.get(i);
            int mx = leftX;
            int my = BASE_Y - (i + 1) * SCALE;
            moveRect(m, markX.get(i), markY.get(i), mx, my);
            markX.set(i, mx);
            markY.set(i, my);
            m.makeVisible();
        }
    }
    
    /**
     * Borra el marco de la torre de la pantalla.
     */
    private void eraseFrame() {
        towerBase.makeInvisible();
        towerLeft.makeInvisible();
        towerRight.makeInvisible();
        for (int i = 0; i < heightMarks.size(); i++) {
            heightMarks.get(i).makeInvisible();
        }
    }
    
    /**
     * Redibuja toda la torre: marco, estado de tapado y todos los items.
     * El orden de dibujo es de mayor a menor numero para que las tazas
     * pequenas (que van dentro) se dibujen encima visualmente.
     */
    private void redraw() {
        if (!visible) return;
        
        eraseAllItems();
        eraseFrame();
        drawFrame();
        
        updateCoveredStatus();
        
        int[] positions = computePositions();
        
        // Dibujar primero las tazas grandes (contenedoras) y luego las pequenas
        // para que las pequenas queden visualmente encima.
        // Ordenar indices por numero de taza descendente para dibujo correcto.
        // Primero dibujar cups de mayor a menor numero, luego las tapas encima.
        
        // Paso 1: dibujar todas las tazas de mayor numero a menor
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getType().equals("cup")) {
                items.get(i).draw(CENTER_X, BASE_Y, positions[i]);
            }
        }
        // Paso 2: dibujar todas las tapas encima
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getType().equals("lid")) {
                items.get(i).draw(CENTER_X, BASE_Y, positions[i]);
            }
        }
    }
    
    /**
     * Calcula la posicion Y base (en cm) de cada elemento de la lista.
     *
     * Modelo de caja fisica:
     * - La lista items representa el orden de colocacion de abajo hacia arriba.
     * - Una taza ABIERTA (no tapada) puede contener tazas menores que vengan despues en la lista.
     * - Una taza TAPADA (su tapa aparece justo despues en la lista) esta cerrada:
     *     * Nada puede entrar dentro de ella.
     *     * Todo lo que tenia dentro antes de taparse sigue adentro.
     * - Cuando se mueve una taza tapada, su contenido (items entre la taza y su tapa) va con ella.
     *
     * Algoritmo:
     * Recorremos la lista. Mantenemos una pila de "contenedoras abiertas".
     * Una contenedora se cierra cuando encontramos su tapa en la lista.
     * Al cerrarla, los items que estaban dentro de ella quedan fijos en sus posiciones.
     * Los items que vienen despues de la tapa se apilan encima del tope de la torre.
     *
     * @return array con la posicion base (cm desde la base) de cada item
     */
    private int[] computePositions() {
        int[] positions = new int[items.size()];
        if (items.isEmpty()) return positions;

        // Pila de contenedoras abiertas: guardamos su indice en items
        int[] stackIdx = new int[items.size()];
        int stackSize = 0;
        int towerTop = 0; // tope maximo actual de la torre

        for (int i = 0; i < items.size(); i++) {
            StackItem si = items.get(i);

            if (si.getType().equals("cup")) {
                int cupNum = si.getNumber();

                // Cerrar contenedoras de la pila que no pueden contener esta taza
                // (su numero es menor o igual al de la taza actual)
                while (stackSize > 0) {
                    int topIdx = stackIdx[stackSize - 1];
                    if (items.get(topIdx).getNumber() <= cupNum) {
                        stackSize--;
                    } else {
                        break;
                    }
                }

                if (stackSize > 0) {
                    // Entra dentro de la contenedora del tope de la pila
                    int contIdx = stackIdx[stackSize - 1];
                    positions[i] = positions[contIdx] + 1;
                } else {
                    // No hay contenedora: va encima del tope de la torre
                    positions[i] = towerTop;
                }

                int myTop = positions[i] + si.getHeight();
                if (myTop > towerTop) towerTop = myTop;

                // Esta taza es candidata a ser contenedora de las siguientes
                stackIdx[stackSize] = i;
                stackSize++;

            } else {
                // Es una tapa (lid)
                int lidNum = si.getNumber();

                // Verificar si es la tapa de la contenedora del tope de la pila
                boolean esTapaDeContenedora = (stackSize > 0)
                    && items.get(stackIdx[stackSize - 1]).getNumber() == lidNum;

                if (esTapaDeContenedora) {
                    // Va justo encima de su taza (cover)
                    int contIdx = stackIdx[stackSize - 1];
                    positions[i] = positions[contIdx] + items.get(contIdx).getHeight();
                    int lidTop = positions[i] + 1;
                    if (lidTop > towerTop) towerTop = lidTop;
                    // Cerrar esta contenedora: ya esta tapada
                    stackSize--;
                } else if ((i > 0)
                    && items.get(i - 1).getType().equals("cup")
                    && items.get(i - 1).getNumber() == lidNum) {
                    // Esta justo despues de su taza aunque no sea del tope
                    // (cover de taza que no es contenedora activa)
                    positions[i] = positions[i - 1] + items.get(i - 1).getHeight();
                    int lidTop = positions[i] + 1;
                    if (lidTop > towerTop) towerTop = lidTop;
                } else {
                    // pushLid normal: va encima del tope
                    positions[i] = towerTop;
                    towerTop += 1;
                }
            }
        }

        return positions;
    }
}
