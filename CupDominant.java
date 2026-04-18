package tower;
import java.util.ArrayList;
import shapes.Rectangle;
import shapes.Circle;
import shapes.Triangle;

/**
 * CupDominant: taza especial dominante.
 * Al entrar a la torre se inserta en su posicion jerarquica correcta
 * (como CupHierarchical) y ademas elimina todas las tazas mas pequenas
 * que ya estaban dentro de ella.
 * Se distingue visualmente por: CÍRCULO - TRIÁNGULO - CÍRCULO en la base.
 * Hereda de Cup.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 4.0
 */
public class CupDominant extends Cup {

    /** Círculo izquierdo */
    private Circle circleLeft;
    private int circleLeftX, circleLeftY;

    /** Triángulo central */
    private Triangle triangle;
    private int triangleX, triangleY;

    /** Círculo derecho */
    private Circle circleRight;
    private int circleRightX, circleRightY;

    /**
     * Constructor de CupDominant.
     * @param number numero de la taza
     * @param color  color de la taza
     */
    public CupDominant(int number, String color) {
        super(number, color);
        int size = SCALE - 8;
        
        // Círculo izquierdo
        circleLeft = new Circle();
        circleLeft.changeDiameter(size);
        circleLeft.changeColor("black");
        circleLeftX = DEFAULT_X;
        circleLeftY = DEFAULT_Y;
        
        // Triángulo central
        triangle = new Triangle();
        triangle.changeSize(size, size);
        triangle.changeColor("black");
        triangleX = DEFAULT_X;
        triangleY = DEFAULT_Y;
        
        // Círculo derecho
        circleRight = new Circle();
        circleRight.changeDiameter(size);
        circleRight.changeColor("black");
        circleRightX = DEFAULT_X;
        circleRightY = DEFAULT_Y;
    }

    /**
     * Dibuja la taza dominant con dos franjas: una arriba y una abajo.
     */
    @Override
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        super.draw(towerCenterX, towerBaseY, yPositionCm);
        int widthPx = number * SCALE;
        int xLeft = towerCenterX - widthPx / 2;
        int basePx = 1 * SCALE;
        int targetBaseY = towerBaseY - yPositionCm * SCALE - basePx;
        
        int size = SCALE - 8;
        int spacing = 4;  // Espacio entre figuras
        int totalWidth = size * 3 + spacing * 2;  // 3 figuras, 2 espacios
        int startX = xLeft + (widthPx - totalWidth) / 2;
        int targetY = targetBaseY + (basePx - size) / 2;
        
        // Círculo izquierdo
        int targetLeftX = startX;
        moveCircle(circleLeft, circleLeftX, circleLeftY, targetLeftX, targetY);
        circleLeftX = targetLeftX;
        circleLeftY = targetY;
        circleLeft.makeVisible();
        
        // Triángulo central
        int targetTriX = startX + size + spacing;
        moveTriangle(triangle, triangleX, triangleY, targetTriX, targetY);
        triangleX = targetTriX;
        triangleY = targetY;
        triangle.makeVisible();
        
        // Círculo derecho
        int targetRightX = startX + (size + spacing) * 2;
        moveCircle(circleRight, circleRightX, circleRightY, targetRightX, targetY);
        circleRightX = targetRightX;
        circleRightY = targetY;
        circleRight.makeVisible();
    }

    /**
     * Oculta la taza dominant y sus dos franjas.
     */
    @Override
    public void erase() {
        super.erase();
        circleLeft.makeInvisible();
        triangle.makeInvisible();
        circleRight.makeInvisible();
    }

    /**
     * Retorna el subtipo de esta taza.
     * @return "dominant"
     */
    @Override
    public String getSubtype() {
        return "dominant";
    }

    /**
     * Inserta esta taza en la torre de forma dominante:
     * 1. Elimina todas las tazas (y sus tapas) con numero menor al suyo.
     * 2. Se inserta en la posicion jerarquica correcta.
     *
     * @param items lista de elementos en la torre
     */
    public void insertDominantly(ArrayList<StackItem> items) {
        // Paso 1: eliminar tazas mas pequenas y sus tapas asociadas
        for (int i = items.size() - 1; i >= 0; i--) {
            StackItem item = items.get(i);
            if (item.getType().equals("cup") && item.getNumber() < this.number) {
                // Si la taza tiene su tapa justo encima, eliminarla tambien
                if (i + 1 < items.size()) {
                    StackItem next = items.get(i + 1);
                    if (next.getType().equals("lid") && next.getNumber() == item.getNumber()) {
                        next.erase();
                        items.remove(i + 1);
                    }
                }
                item.erase();
                items.remove(i);
            }
        }

        // Paso 2: insertar en la posicion jerarquica correcta
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getNumber() < this.number) {
                items.add(i, this);
                return;
            }
        }
        items.add(this);
    }
}