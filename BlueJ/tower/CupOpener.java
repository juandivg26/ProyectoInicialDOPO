package tower;

import java.util.ArrayList;
import shapes.Rectangle;
import shapes.Circle;

/**
 * CupOpener: taza especial que elimina todas las tapas que le impiden el paso.
 * Al entrar a la torre, elimina todas las tapas que se encuentran debajo de ella
 * (es decir, las que ya estaban en la torre antes de que ella llegara).
 * Se distingue visualmente por un círculo negro en el centro de la base.
 * Hereda de Cup.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 4.0
 */
public class CupOpener extends Cup {

    /** Círculo decorativo en el centro de la base */
    private Circle circle;
    private int circleX, circleY;

    /**
     * Constructor de CupOpener.
     * @param number numero de la taza
     * @param color  color de la taza
     */
    public CupOpener(int number, String color) {
        super(number, color);
        
        int size = SCALE - 8; 
        circle = new Circle();
        circle.changeDiameter(size);
        circle.changeColor("black");
        circleX = DEFAULT_X;
        circleY = DEFAULT_Y;
    }

    /**
     * Dibuja la taza opener con su franja identificadora cerca del borde superior.
     */
    @Override
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        super.draw(towerCenterX, towerBaseY, yPositionCm);
        int widthPx = number * SCALE;
        int xLeft = towerCenterX - widthPx / 2;
        int basePx = 1 * SCALE;
        int targetBaseY = towerBaseY - yPositionCm * SCALE - basePx;
        
        // Círculo centrado en la base
        int size = SCALE - 8;
        int targetX = xLeft + (widthPx - size) / 2;
        int targetY = targetBaseY + (basePx - size) / 2;
        
        moveCircle(circle, circleX, circleY, targetX, targetY);
        circleX = targetX;
        circleY = targetY;
        circle.makeVisible();
    }

    /**
     * Oculta la taza opener y su franja.
     */
    @Override
    public void erase() {
        super.erase();
        circle.makeInvisible();
    }

    /**
     * Retorna el subtipo de esta taza.
     * @return "opener"
     */
    @Override
    public String getSubtype() {
        return "opener";
    }

    /**
     * Elimina todas las tapas que estan en la torre antes de que esta taza entre,
     * es decir, las tapas que le impedian el paso.
     * Se recorre la lista de atras hacia adelante para no alterar los indices
     * mientras se eliminan elementos.
     *
     * @param items        lista de elementos en la torre
     * @param openerIndex  indice donde fue insertada esta taza (exclusive):
     *                     solo se eliminan tapas con indice menor a este valor
     */
    public void eliminateBlockingLids(ArrayList<StackItem> items, int openerIndex) {
        for (int i = openerIndex - 1; i >= 0; i--) {
            StackItem item = items.get(i);
            if (item.getType().equals("lid") && !item.getSubtype().equals("crazy")) {
                item.erase();
                items.remove(i);
                openerIndex--;
            }
        }
    }
}