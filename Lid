/**
 * Una tapa de la torre. Es un rectangulo solido.
 * La altura de cualquier tapa es 1 cm.
 * Hereda de StackItem.
 * 
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 3.0
 */
public class Lid extends StackItem {

    /**
     * Crea una tapa para la torre.
     * La altura siempre es 1 cm.
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
    private void createShape() {
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
    public String getType() {
        return "lid";
    }
    
    /**
     * Dibuja la tapa en la posicion indicada dentro de la torre.
     * Posiciona el rectangulo solido en la posicion vertical correcta.
     * Implementa el metodo abstracto del padre.
     * @param towerCenterX centro horizontal de la torre en pixeles
     * @param towerBaseY posicion Y de la base de la torre en pixeles
     * @param yPositionCm posicion Y del elemento en cm desde la base
     */
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
     * Implementa el metodo abstracto del padre.
     */
    public void erase() {
        base.makeInvisible();
        drawn = false;
    }
}
