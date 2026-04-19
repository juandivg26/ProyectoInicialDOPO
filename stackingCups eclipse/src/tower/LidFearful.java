package tower;
import shapes.Rectangle;
import shapes.Triangle;

/**
 * LidFearful: tapa especial asustada.
 * No entra si su taza compañera no esta en la torre.
 * No puede removerse si esta tapando a su taza compañera.
 * Se distingue visualmente por DOS triángulos negros uno al lado del otro.
 * Hereda de Lid.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 4.0 (Dos triángulos lado a lado)
 */
public class LidFearful extends Lid {

    private boolean isProtectingCup;

    /** Triángulo izquierdo */
    private Triangle triangleLeft;
    private int triLeftX, triLeftY;

    /** Triángulo derecho */
    private Triangle triangleRight;
    private int triRightX, triRightY;

    /**
     * Constructor de LidFearful.
     * @param number numero de la tapa
     * @param color  color de la tapa
     */
    public LidFearful(int number, String color) {
        super(number, color);
        this.isProtectingCup = false;
        
        // Crear triángulo izquierdo
        triangleLeft = new Triangle();
        triangleLeft.changeSize(SCALE - 8, SCALE - 8);
        triangleLeft.changeColor("black");
        triLeftX = DEFAULT_X;
        triLeftY = DEFAULT_Y;
        
        // Crear triángulo derecho
        triangleRight = new Triangle();
        triangleRight.changeSize(SCALE - 8, SCALE - 8);
        triangleRight.changeColor("black");
        triRightX = DEFAULT_X;
        triRightY = DEFAULT_Y;
    }


    /**
     * Dibuja la tapa fearful con dos triángulos lado a lado.
     */
    @Override
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        int widthPx = number * SCALE;
        int xLeft = towerCenterX - widthPx / 2;
        
        int targetX = xLeft;
        int targetY = towerBaseY - yPositionCm * SCALE - SCALE;
        moveTo(base, baseX, baseY, targetX, targetY);
        baseX = targetX;
        baseY = targetY;
        base.makeVisible();

        int size = SCALE - 8;  // Tamaño del triángulo
        int spacing = 4;       // Espacio entre los dos triángulos
        
        // Calcular ancho total de los dos triángulos + espacio
        int totalWidth = size * 2 + spacing;
        // Posición inicial para centrar el par de triángulos
        int startX = xLeft + (widthPx - totalWidth) / 2;
        
        // Triángulo izquierdo
        int triLeftXPos = startX;
        int triYPos = targetY + (SCALE - size) / 2;
        
        moveTriangle(triangleLeft, triLeftX, triLeftY, triLeftXPos, triYPos);
        triLeftX = triLeftXPos;
        triLeftY = triYPos;
        triangleLeft.makeVisible();
        
        // Triángulo derecho
        int triRightXPos = startX + size + spacing;
        
        moveTriangle(triangleRight, triRightX, triRightY, triRightXPos, triYPos);
        triRightX = triRightXPos;
        triRightY = triYPos;
        triangleRight.makeVisible();
        
        drawn = true;
    }

    /**
     * Oculta la tapa fearful y sus triángulos.
     */
    @Override
    public void erase() {
        super.erase();
        triangleLeft.makeInvisible();
        triangleRight.makeInvisible();
    }

    @Override
    public String getSubtype() { 
        return "fearful"; 
    }

    public boolean canBeRemoved() { 
        return !isProtectingCup; 
    }

    public void setProtecting(boolean protecting) { 
        this.isProtectingCup = protecting; 
    }

    public boolean isProtecting() { 
        return isProtectingCup; 
    }
}