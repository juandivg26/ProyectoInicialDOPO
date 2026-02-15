/**
 * Un elemento de la torre. Puede ser una taza o una tapa.
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 1.0
 */
public class StackItem {
    
    private String type;
    private int number;
    private String color;
    private int height;
    
    private Rectangle base;
    private Rectangle wallLeft;
    private Rectangle wallRight;
    
    private int baseX;
    private int baseY;
    private int leftX;
    private int leftY;
    private int rightX;
    private int rightY;
    
    private static final int SCALE = 15;
    private static final int WALL = 3;

    public StackItem(String type, int number, String color) {
        this.type = type;
        this.number = number;
        this.color = color;
        if (type.equals("cup")) {
            this.height = 2 * number - 1;
        } else {
            this.height = 1;
        }
        crearFormas();
    }
    
    private void crearFormas() {
        int ancho = number * SCALE;
        if (type.equals("cup")) {
            int alto = height * SCALE;
            
            base = new Rectangle();
            base.changeSize(1 * SCALE, ancho);
            base.changeColor(color);
            baseX = 70;
            baseY = 15;
            
            wallLeft = new Rectangle();
            wallLeft.changeSize(alto, WALL);
            wallLeft.changeColor(color);
            leftX = 70;
            leftY = 15;
            
            wallRight = new Rectangle();
            wallRight.changeSize(alto, WALL);
            wallRight.changeColor(color);
            rightX = 70;
            rightY = 15;
        } else {
            base = new Rectangle();
            base.changeSize(1 * SCALE, ancho);
            base.changeColor(color);
            baseX = 70;
            baseY = 15;
        }
    }
    
    public String getType() { return type; }
    public int getNumber() { return number; }
    public int getHeight() { return height; }
    public String getColor() { return color; }
    
    public void draw(int centroX, int bt, int posY) {
        int ancho = number * SCALE;
        int xIzq = centroX - ancho / 2;
        
        if (type.equals("cup")) {
            int alto = height * SCALE;
            
            int bx = xIzq;
            int by = bt - posY * SCALE - 1 * SCALE;
            mover(base, baseX, baseY, bx, by);
            baseX = bx;
            baseY = by;
            base.makeVisible();
            
            int lx = xIzq;
            int ly = bt - posY * SCALE - alto;
            mover(wallLeft, leftX, leftY, lx, ly);
            leftX = lx;
            leftY = ly;
            wallLeft.makeVisible();
            
            int rx = xIzq + ancho - WALL;
            int ry = bt - posY * SCALE - alto;
            mover(wallRight, rightX, rightY, rx, ry);
            rightX = rx;
            rightY = ry;
            wallRight.makeVisible();
        } else {
            int tx = xIzq;
            int ty = bt - posY * SCALE - 1 * SCALE;
            mover(base, baseX, baseY, tx, ty);
            baseX = tx;
            baseY = ty;
            base.makeVisible();
        }
    }
    
    public void erase() {
        if (type.equals("cup")) {
            base.makeInvisible();
            wallLeft.makeInvisible();
            wallRight.makeInvisible();
        } else {
            base.makeInvisible();
        }
    }
    
    private void mover(Rectangle rect, int ax, int ay, int dx, int dy) {
        int moveX = dx - ax;
        int moveY = dy - ay;
        if (moveX != 0) rect.moveHorizontal(moveX);
        if (moveY != 0) rect.moveVertical(moveY);
    }
}
