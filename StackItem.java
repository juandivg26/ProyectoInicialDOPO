/**
 * Un elemento de la torre. Puede ser una taza o una tapa.
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 2.0
 */
public class StackItem {
    
    private String type;
    private int number;
    private String color;
    private int height;
    private boolean covered;
    
    private Rectangle base;
    private Rectangle wallLeft;
    private Rectangle wallRight;
    
    private int baseX;
    private int baseY;
    private int leftX;
    private int leftY;
    private int rightX;
    private int rightY;
    
    private boolean drawn;
    
    private static final int SCALE = 15;
    private static final int WALL = 3;
    private static final int DEFAULT_X = 70;
    private static final int DEFAULT_Y = 15;

    /**
     * Crea un elemento para la torre.
     * @param type "cup" o "lid"
     * @param number numero del elemento
     * @param color color del elemento
     */
    public StackItem(String type, int number, String color) {
        this.type = type;
        this.number = number;
        this.color = color;
        this.drawn = false;
        this.covered = false;
        
        if (type.equals("cup")) {
            this.height = 2 * number - 1;
        } else {
            this.height = 1;
        }
        
        createShapes();
    }
    
    private void createShapes() {
        int widthPx = number * SCALE;
        
        if (type.equals("cup")) {
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
            
        } else {
            base = new Rectangle();
            base.changeSize(1 * SCALE, widthPx);
            base.changeColor(color);
            baseX = DEFAULT_X;
            baseY = DEFAULT_Y;
        }
    }
    
    public String getType() {
        return type;
    }
    
    public int getNumber() {
        return number;
    }
    
    public int getHeight() {
        return height;
    }
    
    public String getColor() {
        return color;
    }
    
    /**
     * Indica si la taza esta tapada.
     */
    public boolean isCovered() {
        return covered;
    }
    
    /**
     * Marca la taza como tapada o destapada.
     * Cambia la apariencia visual: las paredes se ponen blancas.
     * @param isCovered true si esta tapada
     */
    public void setCovered(boolean isCovered) {
        if (!type.equals("cup")) return;
        this.covered = isCovered;
        
        if (isCovered) {
            wallLeft.changeColor("white");
            wallRight.changeColor("white");
        } else {
            wallLeft.changeColor(color);
            wallRight.changeColor(color);
        }
    }
    
    /**
     * Dibuja el elemento en la posicion indicada.
     * @param towerCenterX centro horizontal de la torre en pixeles
     * @param towerBaseY posicion Y de la base de la torre en pixeles
     * @param yPositionCm posicion Y del elemento en cm desde la base
     */
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        int widthPx = number * SCALE;
        int xLeft = towerCenterX - widthPx / 2;
        
        if (type.equals("cup")) {
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
            
        } else {
            int targetX = xLeft;
            int targetY = towerBaseY - yPositionCm * SCALE - 1 * SCALE;
            moveTo(base, baseX, baseY, targetX, targetY);
            baseX = targetX;
            baseY = targetY;
            base.makeVisible();
        }
        
        drawn = true;
    }
    
    /**
     * Hace invisible el elemento.
     */
    public void erase() {
        if (type.equals("cup")) {
            base.makeInvisible();
            wallLeft.makeInvisible();
            wallRight.makeInvisible();
        } else {
            base.makeInvisible();
        }
        drawn = false;
    }
    
    private void moveTo(Rectangle rect, int currentX, int currentY,
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
