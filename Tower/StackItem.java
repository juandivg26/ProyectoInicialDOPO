package tower;
 
import shapes.Rectangle;

/**
 * Un elemento de la torre. Puede ser una taza (Cup) o una tapa (Lid).
 * Clase abstracta que define el comportamiento comun de todos los elementos.
 * 
 * Cup y Lid heredan de esta clase e implementan su propio dibujo.
 * 
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 3.0
 */
public abstract class StackItem {
    
    protected int number;
    protected String color;
    protected int height;
    protected boolean covered;
    
    protected Rectangle base;
    
    protected int baseX;
    protected int baseY;
    
    protected boolean drawn;
    
    protected static final int SCALE = 15;
    protected static final int DEFAULT_X = 70;
    protected static final int DEFAULT_Y = 15;

    /**
     * Crea un elemento para la torre.
     * @param number numero del elemento
     * @param color color del elemento
     * @param height altura en cm del elemento
     */
    public StackItem(int number, String color, int height) {
        this.number = number;
        this.color = color;
        this.height = height;
        this.drawn = false;
        this.covered = false;
    }
    
    /**
     * Retorna el tipo del elemento.
     * Cada subclase retorna su tipo: "cup" o "lid".
     * @return tipo del elemento
     */
    public abstract String getType();
    
    /**
     * Retorna el subtipo específico del elemento.
     * Para cups: "normal", "opener", "hierarchical"
     * Para lids: "normal", "fearful", "crazy"
     * @return subtipo
     */
    public abstract String getSubtype();
    
    /**
     * Dibuja el elemento en la posicion indicada.
     * Cada subclase implementa su propio dibujo.
     * @param towerCenterX centro horizontal de la torre en pixeles
     * @param towerBaseY posicion Y de la base de la torre en pixeles
     * @param yPositionCm posicion Y del elemento en cm desde la base
     */
    public abstract void draw(int towerCenterX, int towerBaseY, int yPositionCm);
    
    /**
     * Hace invisible el elemento.
     * Cada subclase oculta sus propios rectangulos.
     */
    public abstract void erase();
    
    /**
     * Retorna el numero del elemento.
     * @return numero del elemento
     */
    public int getNumber() {
        return number;
    }
    
    /**
     * Retorna la altura del elemento en cm.
     * @return altura en cm
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Retorna el color del elemento.
     * @return color del elemento
     */
    public String getColor() {
        return color;
    }
    
    /**
     * Indica si el elemento esta tapado.
     * @return true si esta tapado
     */
    public boolean isCovered() {
        return covered;
    }
    
    /**
     * Marca el elemento como tapado o destapado.
     * Por defecto no hace nada. Cup lo sobreescribe
     * para cambiar el color de las paredes.
     * @param isCovered true si esta tapado
     */
    public void setCovered(boolean isCovered) {
        this.covered = isCovered;
    }
    
    /**
     * Mueve un rectangulo desde su posicion actual a una nueva.
     * Calcula la diferencia entre posicion actual y destino
     * y mueve el rectangulo esa distancia.
     * @param rect el rectangulo a mover
     * @param currentX posicion X actual
     * @param currentY posicion Y actual
     * @param targetX posicion X destino
     * @param targetY posicion Y destino
     */
    protected void moveTo(Rectangle rect, int currentX, int currentY,
                        int targetX, int targetY) {
        int dx = targetX - currentX;
        int dy = targetY - currentY;
        if (dx != 0) {
            rect.moveHorizontal(dx);
        }
        if (dy != 0) {
            rect.moveVertical(dy);
        }
    }
}