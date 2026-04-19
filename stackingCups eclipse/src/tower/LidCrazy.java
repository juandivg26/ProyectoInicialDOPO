package tower;
import shapes.Triangle;

/**
 * LidCrazy: tapa especial loca.
 * Se coloca DEBAJO de su taza (no en la base).
 * Se distingue visualmente por un triángulo negro centrado.
 * Hereda de Lid.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 3.0 (Corregido: ahora va debajo de su taza)
 */
public class LidCrazy extends Lid {
    
    private Triangle figura;
    private int figuraX, figuraY;

    /**
     * Constructor de LidCrazy.
     * @param number numero de la tapa
     * @param color  color de la tapa
     */
    public LidCrazy(int number, String color) {
        super(number, color);
        this.height = 1;
        
        figura = new Triangle();
        figura.changeSize(SCALE - 6, SCALE - 6);
        figura.changeColor("black");
        
        figuraX = DEFAULT_X;
        figuraY = DEFAULT_Y;
    }

    /**
     * Dibuja la tapa crazy con su triángulo centrado.
     * Se dibuja debajo de su taza, en la posición calculada.
     */
    @Override
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        // Dibujar el rectángulo base de la tapa
        int widthPx = number * SCALE;
        int xLeft = towerCenterX - widthPx / 2;
        
        int targetX = xLeft;
        int targetY = towerBaseY - yPositionCm * SCALE - SCALE;
        moveTo(base, baseX, baseY, targetX, targetY);
        baseX = targetX;
        baseY = targetY;
        base.makeVisible();
        
        // Dibujar el triángulo decorativo centrado
        int size = SCALE - 6;
        int triX = xLeft + (widthPx - size) / 2;
        int triY = targetY + (SCALE - size) / 2;
        
        moveTriangle(figura, figuraX, figuraY, triX, triY);
        figuraX = triX;
        figuraY = triY;
        figura.makeVisible();
        
        drawn = true;
    }

    @Override
    public void erase() {
        super.erase();
        figura.makeInvisible();
    }

    @Override
    public String getSubtype() { 
        return "crazy"; 
    }

    @Override
    public int getHeight() { 
        return 1;  
    }
}