package tower;

import shapes.Rectangle;

/**
 * Una taza de la torre. Tiene forma de U (base + 2 paredes).
 * La altura de la taza i es 2i-1 cm.
 * Hereda de StackItem.
 * 
 * Clase ABSTRACTA que sirve como base para tipos específicos de tazas:
 * - CupNormal: comportamiento estándar
 * - CupOpener: elimina tapas que impiden el paso
 * - CupHierarchical: entra en la torre desplazando todo
 * 
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 3.0
 */
public abstract class Cup extends StackItem {
    
    protected Rectangle wallLeft;
    protected Rectangle wallRight;

    
    protected int leftX;
    protected int leftY;
    protected int rightX;
    protected int rightY;
    
    protected static final int WALL = 3;
    protected static final int SCALE = 15;
    protected static final int DEFAULT_X = 70;
    protected static final int DEFAULT_Y = 15;

    /**
     * Crea una taza para la torre.
     * La altura se calcula como 2*number - 1.
     * @param number numero de la taza
     * @param color color de la taza
     */
    public Cup(int number, String color) {
        super(number, color, 2 * number - 1);
        createShapes();
    }
    
    /**
     * Crea los rectangulos que forman la taza:
     * una base (fondo) y dos paredes (izquierda y derecha).
     */
    protected void createShapes() {
        int widthPx = number * SCALE;
        int heightPx = height * SCALE;
        int basePx = 1 * SCALE;
        
        base = new Rectangle();
        base.changeSize(basePx, widthPx);
        base.changeColor(color);
        baseX = DEFAULT_X;
        baseY = DEFAULT_Y;
        
        wallLeft = new Rectangle();
        wallLeft.changeSize(heightPx, WALL);
        wallLeft.changeColor(color);
        leftX = DEFAULT_X;
        leftY = DEFAULT_Y;
        
        wallRight = new Rectangle();
        wallRight.changeSize(heightPx, WALL);
        wallRight.changeColor(color);
        rightX = DEFAULT_X;
        rightY = DEFAULT_Y;
    }
    
    /**
     * Retorna el tipo de este elemento.
     * @return "cup"
     */
    @Override
    public String getType() {
        return "cup";
    }
    
    /**
     * Retorna el subtipo específico de taza
     * Cada subclase implementa esto
     * @return "normal", "opener", "hierarchical", etc.
     */
    public abstract String getSubtype();
    
    /**
     * Marca la taza como tapada o destapada.
     * Cambia la apariencia visual: las paredes se ponen negras
     * cuando esta tapada, y vuelven al color original cuando
     * esta destapada.
     * @param isCovered true si esta tapada
     */
    public void setCovered(boolean isCovered) {
        this.covered = isCovered;
        
        if (isCovered) {
            wallLeft.changeColor("black");
            wallRight.changeColor("black");
        } else {
            wallLeft.changeColor(color);
            wallRight.changeColor(color);
        }
    }
    
    /**
     * Dibuja la taza en la posicion indicada dentro de la torre.
     * @param towerCenterX centro horizontal de la torre en pixeles
     * @param towerBaseY posicion Y de la base de la torre en pixeles
     * @param yPositionCm posicion Y del elemento en cm desde la base
     */
    @Override
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        int widthPx = number * SCALE;
        int xLeft = towerCenterX - widthPx / 2;
        int heightPx = height * SCALE;
        int basePx = 1 * SCALE;
        
        int targetBaseX = xLeft;
        int targetBaseY = towerBaseY - yPositionCm * SCALE - basePx;
        moveTo(base, baseX, baseY, targetBaseX, targetBaseY);
        baseX = targetBaseX;
        baseY = targetBaseY;
        base.makeVisible();
        
        int targetLeftX = xLeft;
        int targetLeftY = towerBaseY - yPositionCm * SCALE - heightPx;
        moveTo(wallLeft, leftX, leftY, targetLeftX, targetLeftY);
        leftX = targetLeftX;
        leftY = targetLeftY;
        wallLeft.makeVisible();
        
        int targetRightX = xLeft + widthPx - WALL;
        int targetRightY = towerBaseY - yPositionCm * SCALE - heightPx;
        moveTo(wallRight, rightX, rightY, targetRightX, targetRightY);
        rightX = targetRightX;
        rightY = targetRightY;
        wallRight.makeVisible();
        
        drawn = true;
    }
    
    /**
     * Hace invisible la taza.
     * Oculta los tres rectangulos: base, pared izquierda y pared derecha.
     */
    @Override
    public void erase() {
        base.makeInvisible();
        wallLeft.makeInvisible();
        wallRight.makeInvisible();
        drawn = false;
    }
}