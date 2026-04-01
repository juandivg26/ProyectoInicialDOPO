package tower;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import shapes.Rectangle;
import shapes.Canvas; 
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
 * @version 4.0
 */

public class Tower { 
    private static Canvas towerCanvas;
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
            StackItem cup = new CupNormal(i, getColor(i));
            items.add(cup);
        }
        
        this.lastOk = true;
    }
    
    // TAZAS 
    
    /**
     * Anade la taza normal i a la cima de la torre.
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
        
        StackItem cup = new CupNormal(i, getColor(i));
        items.add(cup);
        lastOk = true;
        redraw();
    }
    
        /**
     * Agrega una taza específica (con tipo) a la cima de la torre.
     * NUEVO MÉTODO CON SOBRECARGA
     * @param i numero de la taza
     * @param cupType tipo de taza: "normal", "opener", "hierarchical"
     */
    public void pushCup(int i, String cupType) {
        cupType = cupType.toLowerCase();
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
        
        if (cupType.equals("opener")) {
            CupOpener cup = new CupOpener(i, getColor(i));
            items.add(cup);
            int openerIndex = items.size() - 1;
            cup.eliminateBlockingLids(items, openerIndex);

        } else if (cupType.equals("hierarchical")) {
            CupHierarchical cup = new CupHierarchical(i, getColor(i));
            cup.insertHierarchically(items);

        } else if (cupType.equals("dominant")) {
            CupDominant cup = new CupDominant(i, getColor(i));
            cup.insertDominantly(items);

        } else {
            items.add(new CupNormal(i, getColor(i)));
        }
        
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
        // CupHierarchical que llego al fondo no puede quitarse
        StackItem cup = items.get(idx);
        if (cup.getSubtype().equals("hierarchical")) {
            CupHierarchical h = (CupHierarchical) cup;
            if (h.hasReachedBottom()) {
                lastOk = false;
                showError("La taza jerarquica " + i + " llego al fondo y no puede quitarse.");
                return;
            }
        }
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
     * Anade la tapa normal i a la cima de la torre.
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
        
        StackItem lid = new LidNormal(i, getColor(i));
        items.add(lid);
        lastOk = true;
        redraw();
    }
    
    /**
     * Agrega una tapa específica (con tipo) a la cima de la torre.
     * NUEVO MÉTODO CON SOBRECARGA
     * @param i numero de la tapa
     * @param lidType tipo de tapa: "normal", "fearful", "crazy"
     */
    public void pushLid(int i, String lidType) {
        lidType = lidType.toLowerCase();
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
        int lidHeight = 1;
        
        if (lidType.equals("crazy")) {
            lidHeight = 0;
        }
        
        if (i > width || computeHeight() + lidHeight > maxHeight) {
            lastOk = false;
            showError("La tapa " + i + " no cabe en la torre.");
            return;
        }
        
        // LidFearful no entra si su taza companiera no esta en la torre
        if (lidType.equals("fearful") && !existsItem("cup", i)) {
            lastOk = false;
            showError("La tapa fearful " + i + " no puede entrar: su taza companiera no esta en la torre.");
            return;
        }

        StackItem lid;
        if (lidType.equals("fearful")) {
            LidFearful fearful = new LidFearful(i, getColor(i));
            int cupIdx = findItem("cup", i);
            if (cupIdx != -1 && cupIdx == items.size() - 1) {
                fearful.setProtecting(true);
            }
            lid = fearful;
        } else if (lidType.equals("crazy")) {
            lid = new LidCrazy(i, getColor(i));
        } else {
            lid = new LidNormal(i, getColor(i));
        }

        // LidCrazy va al fondo de la torre, despues de las otras crazys existentes
        if (lidType.equals("crazy")) {
            int insertPos = 0;
            while (insertPos < items.size()
                   && items.get(insertPos).getSubtype().equals("crazy")) {
                insertPos++;
            }
            items.add(insertPos, lid);
        } else {
            items.add(lid);
        }
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
        // LidFearful no puede salir si esta protegiendo su taza
        if (top.getSubtype().equals("fearful")) {
            LidFearful fearful = (LidFearful) top;
            if (fearful.isProtecting()) {
                lastOk = false;
                showError("La tapa fearful esta protegiendo su taza y no puede salir.");
                return;
            }
        }
        top.erase();
        items.remove(items.size() - 1);
        lastOk = true;
        redraw();
    }
    
    /**
     * Elimina la tapa i de cualquier posicion en la torre.
     * Si la tapa es fearful y está protegiendo, no se puede eliminar.
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
        
        StackItem lid = items.get(idx);
        if (lid.getType().equals("lid")) {
            if (lid.getSubtype().equals("fearful")) {
                LidFearful fearful = (LidFearful) lid;
                if (fearful.isProtecting()) {
                    lastOk = false;
                    showError("La tapa " + i + " esta protegiendo su taza y no puede removerse.");
                    return;
                }
            }
        }
        
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
        // Recolectar numeros unicos presentes en la torre
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < items.size(); i++) {
            int num = items.get(i).getNumber();
            if (!numbers.contains(num)) {
                numbers.add(num);
            }
        }
        sortDescending(numbers);
        
        ArrayList<StackItem> oldItems = new ArrayList<StackItem>(items);
        eraseAllItems();
        items.clear();

        for (int i = 0; i < numbers.size(); i++) {
            int num = numbers.get(i);
            // Buscar el item original para preservar su subtipo
            for (int j = 0; j < oldItems.size(); j++) {
                StackItem si = oldItems.get(j);
                if (si.getNumber() == num && si.getType().equals("cup")) {
                    items.add(cloneItem(si));
                    break;
                }
            }
            for (int j = 0; j < oldItems.size(); j++) {
                StackItem si = oldItems.get(j);
                if (si.getNumber() == num && si.getType().equals("lid")) {
                    items.add(cloneItem(si));
                    break;
                }
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
        ArrayList<ArrayList<StackItem>> units = new ArrayList<ArrayList<StackItem>>();
        
        int i = 0;
        while (i < items.size()) {
            ArrayList<StackItem> unit = new ArrayList<StackItem>();
            unit.add(items.get(i));
            if (isCupCovered(i)) {
                unit.add(items.get(i + 1));
                i += 2;
            } else {
                i++;
            }
            units.add(unit);
        }
        
        eraseAllItems();
        items.clear();
        
        for (int u = units.size() - 1; u >= 0; u--) {
            ArrayList<StackItem> unit = units.get(u);
            for (int k = 0; k < unit.size(); k++) {
                StackItem si = unit.get(k);
                items.add(cloneItem(si));
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
                i = end1 + 1;
            } else if (i == start2) {
                for (int k = 0; k < block1.size(); k++) newItems.add(block1.get(k));
                i = end2 + 1;
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
                StackItem temp = items.get(i);
                items.set(i, items.get(j));
                items.set(j, temp);
                
                int newH = computeHeight();
                
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
     */
    private int computeHeight() {
        if (items.isEmpty()) return 0;

        int[] containerTop = new int[items.size()];
        int[] containerNumber = new int[items.size()];
        int stackSize = 0;

        int towerTop = 0;
        int currentBase = 0;

        for (int i = 0; i < items.size(); i++) {
            StackItem si = items.get(i);

            if (si.getType().equals("cup")) {
                int cupNum = si.getNumber();
                int cupHeight = si.getHeight();

                while (stackSize > 0 && containerNumber[stackSize - 1] <= cupNum) {
                    stackSize--;
                }

                if (stackSize > 0) {
                    currentBase = containerTop[stackSize - 1] - (2 * containerNumber[stackSize - 1] - 1) + 1;
                } else {
                    currentBase = towerTop;
                }

                int cupTop = currentBase + cupHeight;
                if (cupTop > towerTop) towerTop = cupTop;

                containerNumber[stackSize] = cupNum;
                containerTop[stackSize] = cupTop;
                stackSize++;

            } else {
                // LidCrazy tiene height=0 pero ocupa 1 cm visual en la base
                int lidHeight = si.getSubtype().equals("crazy") ? 1 : si.getHeight();
                int lidTop = towerTop + lidHeight;
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
     * @param type "cup" o "lid"
     * @param number numero del elemento
     * @return el StackItem creado
     */
    private StackItem createItem(String type, int number) {
        if (type.equals("cup")) {
            return new CupNormal(number, getColor(number));
        } else {
            return new LidNormal(number, getColor(number));
        }
    }

    /**
     * Crea una copia de un StackItem preservando su subtipo original.
     * Se usa en orderTower y reverseTower para no perder el tipo al reordenar.
     * @param original el item a clonar
     * @return nueva instancia del mismo subtipo y numero
     */
    private StackItem cloneItem(StackItem original) {
        int num = original.getNumber();
        String color = getColor(num);
        String subtype = original.getSubtype();

        if (original.getType().equals("cup")) {
            if (subtype.equals("opener"))      return new CupOpener(num, color);
            if (subtype.equals("hierarchical")) return new CupHierarchical(num, color);
            if (subtype.equals("dominant"))    return new CupDominant(num, color);
            return new CupNormal(num, color);
        } else {
            if (subtype.equals("fearful")) return new LidFearful(num, color);
            if (subtype.equals("crazy"))   return new LidCrazy(num, color);
            return new LidNormal(num, color);
        }
    }
    
    /**
     * Retorna el bloque de items que corresponden a un elemento y su contenido.
     * @param idx indice del item en la lista
     * @return lista de StackItems que forman el bloque
     */
    private ArrayList<StackItem> getBloque(int idx) {
        ArrayList<StackItem> bloque = new ArrayList<StackItem>();
        if (idx < 0 || idx >= items.size()) return bloque;
        StackItem si = items.get(idx);
        bloque.add(si);
        if (!si.getType().equals("cup")) return bloque;
        if (isCupCovered(idx)) {
            int cupNum = si.getNumber();
            for (int k = idx + 1; k < items.size(); k++) {
                bloque.add(items.get(k));
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
     */
    private void redraw() {
        if (!visible) return;
        
        eraseAllItems();
        eraseFrame();
        drawFrame();
        
        updateCoveredStatus();
        
        int[] positions = computePositions();

        // Dibujar en orden de tamaño ascendente:
        // tapas y tazas pequeñas primero, tazas grandes al final.
        // Asi las paredes de las contenedoras quedan encima en el canvas.
        int[] drawOrder = new int[items.size()];
        for (int i = 0; i < items.size(); i++) drawOrder[i] = i;
        for (int a = 0; a < drawOrder.length - 1; a++) {
            for (int b = 0; b < drawOrder.length - 1 - a; b++) {
                StackItem sa = items.get(drawOrder[b]);
                StackItem sb = items.get(drawOrder[b + 1]);
                int sizeA = sa.getType().equals("cup") ? sa.getNumber() : 0;
                int sizeB = sb.getType().equals("cup") ? sb.getNumber() : 0;
                if (sizeA > sizeB) {
                    int tmp = drawOrder[b];
                    drawOrder[b] = drawOrder[b + 1];
                    drawOrder[b + 1] = tmp;
                }
            }
        }
        for (int i = 0; i < drawOrder.length; i++) {
            items.get(drawOrder[i]).draw(CENTER_X, BASE_Y, positions[drawOrder[i]]);
        }
    }
    
    /**
     * Calcula la posicion Y base (en cm) de cada elemento de la lista.
     */
    private int[] computePositions() {
        int[] positions = new int[items.size()];
        if (items.isEmpty()) return positions;

        int[] stackIdx = new int[items.size()];
        int stackSize = 0;

        // LidCrazy ocupa la base. Cada una ocupa 1 cm visual.
        // Se asignan posiciones 0, 1, 2... segun cuantas haya.
        int towerTop = 0;
        int crazyCount = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getSubtype().equals("crazy")) {
                positions[i] = crazyCount;
                crazyCount++;
            }
        }
        towerTop = crazyCount; // las demas cosas arrancan desde aqui

        for (int i = 0; i < items.size(); i++) {
            StackItem si = items.get(i);

            // LidCrazy ya fue posicionada arriba, saltar
            if (si.getSubtype().equals("crazy")) continue;

            if (si.getType().equals("cup")) {
                int cupNum = si.getNumber();

                while (stackSize > 0) {
                    int topIdx = stackIdx[stackSize - 1];
                    if (items.get(topIdx).getNumber() <= cupNum) {
                        stackSize--;
                    } else {
                        break;
                    }
                }

                if (stackSize > 0) {
                    int contIdx = stackIdx[stackSize - 1];
                    positions[i] = positions[contIdx] + 1;
                } else {
                    positions[i] = towerTop;
                }

                int myTop = positions[i] + si.getHeight();
                if (myTop > towerTop) towerTop = myTop;

                stackIdx[stackSize] = i;
                stackSize++;

            } else {
                int lidNum = si.getNumber();

                boolean esTapaDeContenedora = (stackSize > 0)
                    && items.get(stackIdx[stackSize - 1]).getNumber() == lidNum;

                if (esTapaDeContenedora) {
                    int contIdx = stackIdx[stackSize - 1];
                    positions[i] = positions[contIdx] + items.get(contIdx).getHeight();
                    int lidTop = positions[i] + si.getHeight();
                    if (lidTop > towerTop) towerTop = lidTop;
                    stackSize--;
                } else if ((i > 0)
                    && items.get(i - 1).getType().equals("cup")
                    && items.get(i - 1).getNumber() == lidNum) {
                    positions[i] = positions[i - 1] + items.get(i - 1).getHeight();
                    int lidTop = positions[i] + si.getHeight();
                    if (lidTop > towerTop) towerTop = lidTop;
                } else {
                    positions[i] = towerTop;
                    towerTop += si.getHeight();
                }
            }
        }

        return positions;
    }
}