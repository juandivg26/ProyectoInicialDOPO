import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Simulador de torre de tazas y tapas.
 * @author Juan David
 * @version 1.0
 */
public class Tower {
    
    private int width;
    private int maxHeight;
    private boolean visible;
    private boolean lastOk;
    private ArrayList<StackItem> items;
    
    // Linea base de la torre
    private Rectangle lineaBase;
    private int lineaBaseX;
    private int lineaBaseY;
    
    private static final String[] COLORS = {
        "red", "blue", "green", "yellow", "magenta", "black"
    };
    private static final int SCALE = 15;
    private static final int CENTER_X = 150;
    private static final int BASE_Y = 280;

    /**
     * Crea una torre.
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
        
        // Crear la linea base
        lineaBase = new Rectangle();
        lineaBase.changeSize(2, width * SCALE);
        lineaBase.changeColor("black");
        lineaBaseX = 70;
        lineaBaseY = 15;
    }
    
    /**
     * Anade la taza i a la cima de la torre.
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
     * Elimina la taza de la cima.
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
     * Elimina la taza i de cualquier posicion.
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
    
    /**
     * Anade la tapa i a la cima de la torre.
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
     * Elimina la tapa de la cima.
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
     * Elimina la tapa i de cualquier posicion.
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
    
    /**
     * Retorna la altura total en cm.
     */
    public int height() {
        lastOk = true;
        return computeHeight();
    }
    
    /**
     * Hace visible el simulador.
     */
    public void makeVisible() {
        visible = true;
        lastOk = true;
        redraw();
    }
    
    /**
     * Hace invisible el simulador.
     */
    public void makeInvisible() {
        visible = false;
        eraseAll();
        lineaBase.makeInvisible();
        lastOk = true;
    }
    
    /**
     * Indica si la ultima operacion fue exitosa.
     */
    public boolean ok() {
        return lastOk;
    }
    
    // ---- metodos privados ----
    
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
    
    private void eraseAll() {
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
    
    private void moverRect(Rectangle rect, int ax, int ay, int dx, int dy) {
        int mx = dx - ax;
        int my = dy - ay;
        if (mx != 0) rect.moveHorizontal(mx);
        if (my != 0) rect.moveVertical(my);
    }
    
    private void redraw() {
        if (!visible) return;
        
        eraseAll();
        lineaBase.makeInvisible();
        
        // Dibujar linea base
        int leftX = CENTER_X - (width * SCALE) / 2;
        moverRect(lineaBase, lineaBaseX, lineaBaseY, leftX, BASE_Y);
        lineaBaseX = leftX;
        lineaBaseY = BASE_Y;
        lineaBase.makeVisible();
        
        // Dibujar items de base a cima
        int currentY = 0;
        for (int i = 0; i < items.size(); i++) {
            StackItem si = items.get(i);
            si.draw(CENTER_X, BASE_Y, currentY);
            currentY += si.getHeight();
        }
    }
}