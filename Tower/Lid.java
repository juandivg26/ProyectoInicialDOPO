package tower;

import shapes.Rectangle;
import shapes.Triangle;

/**
 * Una tapa de la torre. Es un rectangulo solido.
 * La altura de cualquier tapa es 1 cm (excepto LidCrazy que es diferente).
 * Hereda de StackItem.
 * 
 * Clase ABSTRACTA que sirve como base para tipos específicos de tapas:
 * - LidNormal: comportamiento estándar, altura = 1
 * - LidFearful: no sale si está tapando su taza
 * - LidCrazy: se pone de base, altura = 0
 * 
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 3.0
 */
public abstract class Lid extends StackItem {

    protected static final int SCALE = 15;
    protected static final int DEFAULT_X = 70;
    protected static final int DEFAULT_Y = 15;

    /**
     * Crea una tapa para la torre.
     * La altura por defecto es 1 cm (pero LidCrazy lo sobrescribe).
     * @param number numero de la tapa
     * @param color color de la tapa
     */
    public Lid(int number, String color) {
        super(number, color, 1);
        createShape();
    }
    
    /**
     * Crea el rectangulo que forma la tapa.
     * La tapa es un solo rectangulo solido.
     */
    protected void createShape() {
        int widthPx = number * SCALE;
        
        base = new Rectangle();
        base.changeSize(1 * SCALE, widthPx);
        base.changeColor(color);
        baseX = DEFAULT_X;
        baseY = DEFAULT_Y;
    }
    
    /**
     * Retorna el tipo de este elemento.
     * @return "lid"
     */
    @Override
    public String getType() {
        return "lid";
    }
    
    /**
     * Retorna el subtipo específico de tapa
     * Cada subclase implementa esto
     * @return "normal", "fearful", "crazy", etc.
     */
    public abstract String getSubtype();
    
    /**
     * Dibuja la tapa en la posicion indicada dentro de la torre.
     * @param towerCenterX centro horizontal de la torre en pixeles
     * @param towerBaseY posicion Y de la base de la torre en pixeles
     * @param yPositionCm posicion Y del elemento en cm desde la base
     */
    @Override
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        int widthPx = number * SCALE;
        int xLeft = towerCenterX - widthPx / 2;
        
        int targetX = xLeft;
        int targetY = towerBaseY - yPositionCm * SCALE - 1 * SCALE;
        moveTo(base, baseX, baseY, targetX, targetY);
        baseX = targetX;
        baseY = targetY;
        base.makeVisible();
        
        drawn = true;
    }
    
    /**
     * Hace invisible la tapa.
     * Oculta el rectangulo de la tapa.
     */
    @Override
    public void erase() {
        base.makeInvisible();
        drawn = false;
    }
}
