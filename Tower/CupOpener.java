package tower;

import java.util.ArrayList;
import shapes.Rectangle;

/**
 * CupOpener: taza especial que elimina todas las tapas que le impiden el paso.
 * Al entrar a la torre, elimina todas las tapas que se encuentran debajo de ella
 * (es decir, las que ya estaban en la torre antes de que ella llegara).
 * Se distingue visualmente por una franja horizontal negra cerca del borde superior.
 * Hereda de Cup.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 3.0
 */
public class CupOpener extends Cup {

    /** Franja visual cerca del borde superior de la taza. */
    private Rectangle stripe;
    private int stripeX, stripeY;

    /**
     * Constructor de CupOpener.
     * @param number numero de la taza
     * @param color  color de la taza
     */
    public CupOpener(int number, String color) {
        super(number, color);
        int widthPx = number * SCALE - 2 * WALL;
        stripe = new Rectangle();
        stripe.changeSize(3, widthPx);
        stripe.changeColor("black");
        stripeX = DEFAULT_X;
        stripeY = DEFAULT_Y;
    }

    /**
     * Dibuja la taza opener con su franja identificadora cerca del borde superior.
     */
    @Override
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        super.draw(towerCenterX, towerBaseY, yPositionCm);
        int widthPx = number * SCALE;
        int xLeft = towerCenterX - widthPx / 2 + WALL;
        int heightPx = height * SCALE;
        int targetX = xLeft;
        int targetY = towerBaseY - yPositionCm * SCALE - heightPx + 2;
        moveTo(stripe, stripeX, stripeY, targetX, targetY);
        stripeX = targetX;
        stripeY = targetY;
        stripe.makeVisible();
    }

    /**
     * Oculta la taza opener y su franja.
     */
    @Override
    public void erase() {
        super.erase();
        stripe.makeInvisible();
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