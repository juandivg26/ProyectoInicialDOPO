import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Simulador de torre de tazas y tapas.
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 2.0
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
            StackItem cup = new StackItem("cup", i, getColor(i));
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
        
        StackItem cup = new StackItem("cup", i, getColor(i));
        items.add(cup);
        lastOk = true;
        redraw();
    }
    
    /**
     * Elimina la taza de la cima (si la cima es una taza).
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
        top.erase();
        items.remove(items.size() - 1);
        lastOk = true;
        redraw();
    }
    
    /**
     * Elimina la taza i de cualquier posicion en la torre.
     * @param i numero de la taza a eliminar
     */
    public void removeCup(int i) {
        int idx = findItem("cup", i);
        if (idx == -1) {
            lastOk = false;
            showError("La taza " + i + " no esta en la torre.");
            return;
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
        
        StackItem lid = new StackItem("lid", i, getColor(i));
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
     * @param i numero de la tapa a eliminar
     */
    public void removeLid(int i) {
        int idx = findItem("lid", i);
        if (idx == -1) {
            lastOk = false;
            showError("La tapa " + i + " no esta en la torre.");
            return;
        }
        items.get(idx).erase();
        items.remove(idx);
        lastOk = true;
        redraw();
    }
    
    //ORGANIZAR
    
    /**
     * Ordena de mayor a menor. El menor queda en la cima.
     * Si taza y tapa del mismo numero estan, la tapa va sobre la taza.
     */
    public void orderTower() {
        ArrayList<String> oldTypes = new ArrayList<String>();
        ArrayList<Integer> oldNumbers = new ArrayList<Integer>();
        for (int i = 0; i < items.size(); i++) {
            oldTypes.add(items.get(i).getType());
            oldNumbers.add(items.get(i).getNumber());
        }
        
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
        
        int currentHeight = 0;
        
        for (int i = 0; i < numbers.size(); i++) {
            int num = numbers.get(i);
            boolean hasCup = hasInLists(oldTypes, oldNumbers, "cup", num);
            boolean hasLid = hasInLists(oldTypes, oldNumbers, "lid", num);
            
            if (hasCup) {
                int cupHeight = 2 * num - 1;
                if (num <= width && currentHeight + cupHeight <= maxHeight) {
                    items.add(new StackItem("cup", num, getColor(num)));
                    currentHeight += cupHeight;
                } else {
                    continue;
                }
            }
            
            if (hasLid) {
                if (num <= width && currentHeight + 1 <= maxHeight) {
                    items.add(new StackItem("lid", num, getColor(num)));
                    currentHeight += 1;
                }
            }
        }
        
        lastOk = true;
        redraw();
    }
    
    /**
     * Invierte el orden de los elementos.
     */
    public void reverseTower() {
        ArrayList<String> revTypes = new ArrayList<String>();
        ArrayList<Integer> revNumbers = new ArrayList<Integer>();
        
        for (int i = items.size() - 1; i >= 0; i--) {
            revTypes.add(items.get(i).getType());
            revNumbers.add(items.get(i).getNumber());
        }
        
        eraseAllItems();
        items.clear();
        
        int currentHeight = 0;
        for (int i = 0; i < revTypes.size(); i++) {
            String type = revTypes.get(i);
            int num = revNumbers.get(i);
            int itemH;
            if (type.equals("cup")) {
                itemH = 2 * num - 1;
            } else {
                itemH = 1;
            }
            
            if (num <= width && currentHeight + itemH <= maxHeight) {
                items.add(new StackItem(type, num, getColor(num)));
                currentHeight += itemH;
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
        int num1;
        int num2;
        
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
        
        if (idx1 == -1) {
            lastOk = false;
            showError("No se encontro " + type1 + " " + num1 + ".");
            return;
        }
        if (idx2 == -1) {
            lastOk = false;
            showError("No se encontro " + type2 + " " + num2 + ".");
            return;
        }
        if (idx1 == idx2) {
            lastOk = false;
            showError("No se puede intercambiar un objeto consigo mismo.");
            return;
        }
        
        StackItem temp = items.get(idx1);
        items.set(idx1, items.get(idx2));
        items.set(idx2, temp);
        
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
     */
    public int height() {
        lastOk = true;
        return computeHeight();
    }
    
    /**
     * Retorna los numeros de las tazas tapadas.
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
    
    public void makeVisible() {
        visible = true;
        lastOk = true;
        redraw();
    }
    
    public void makeInvisible() {
        visible = false;
        eraseAllItems();
        eraseFrame();
        lastOk = true;
    }
    
    public void exit() {
        makeInvisible();
        items.clear();
        lastOk = true;
        System.exit(0);
    }
    
    public boolean ok() {
        return lastOk;
    }
    
    //METODOS PRIVADOS
    
    private int computeHeight() {
        int h = 0;
        for (int i = 0; i < items.size(); i++) {
            h += items.get(i).getHeight();
        }
        return h;
    }
    
    private String getColor(int number) {
        return COLORS[(number - 1) % COLORS.length];
    }
    
    private boolean existsItem(String type, int number) {
        for (int i = 0; i < items.size(); i++) {
            StackItem si = items.get(i);
            if (si.getType().equals(type) && si.getNumber() == number) {
                return true;
            }
        }
        return false;
    }
    
    private int findItem(String type, int number) {
        for (int i = 0; i < items.size(); i++) {
            StackItem si = items.get(i);
            if (si.getType().equals(type) && si.getNumber() == number) {
                return i;
            }
        }
        return -1;
    }
    
    private boolean hasInLists(ArrayList<String> types, ArrayList<Integer> nums,
                               String type, int number) {
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).equals(type) && nums.get(i) == number) {
                return true;
            }
        }
        return false;
    }
    
    private void eraseAllItems() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).erase();
        }
    }
    
    private void showError(String message) {
        if (visible) {
            JOptionPane.showMessageDialog(null, message, "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
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
    
    private void moveRect(Rectangle rect, int curX, int curY,
                          int tarX, int tarY) {
        int dx = tarX - curX;
        int dy = tarY - curY;
        if (dx != 0) rect.moveHorizontal(dx);
        if (dy != 0) rect.moveVertical(dy);
    }
    
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
    
    private void eraseFrame() {
        towerBase.makeInvisible();
        towerLeft.makeInvisible();
        towerRight.makeInvisible();
        for (int i = 0; i < heightMarks.size(); i++) {
            heightMarks.get(i).makeInvisible();
        }
    }
    
    private void redraw() {
        if (!visible) return;
        
        eraseAllItems();
        eraseFrame();
        drawFrame();
        
        updateCoveredStatus();
        
        int currentY = 0;
        for (int i = 0; i < items.size(); i++) {
            StackItem si = items.get(i);
            si.draw(CENTER_X, BASE_Y, currentY);
            currentY += si.getHeight();
        }
    }
}
