package tower;

import shapes.Rectangle;

/**
 * LidFearful: tapa especial asustada.
 * No entra si su taza companiera no esta en la torre.
 * No puede removerse si esta tapando a su taza companiera.
 * Se distingue visualmente por una franja vertical negra al centro.
 * Hereda de Lid.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 3.0
 */
public class LidFearful extends Lid {

    private boolean isProtectingCup;

    /** Franja vertical al centro de la tapa. */
    private Rectangle stripe;
    private int stripeX, stripeY;

    /**
     * Constructor de LidFearful.
     * @param number numero de la tapa
     * @param color  color de la tapa
     */
    public LidFearful(int number, String color) {
        super(number, color);
        this.isProtectingCup = false;
        stripe = new Rectangle();
        stripe.changeSize(1 * SCALE, 3);
        stripe.changeColor("black");
        stripeX = DEFAULT_X;
        stripeY = DEFAULT_Y;
    }

    /**
     * Dibuja la tapa fearful con su franja vertical al centro.
     */
    @Override
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        super.draw(towerCenterX, towerBaseY, yPositionCm);
        int targetX = towerCenterX - 1;
        int targetY = towerBaseY - yPositionCm * SCALE - 1 * SCALE;
        moveTo(stripe, stripeX, stripeY, targetX, targetY);
        stripeX = targetX;
        stripeY = targetY;
        stripe.makeVisible();
    }

    /**
     * Oculta la tapa fearful y su franja.
     */
    @Override
    public void erase() {
        super.erase();
        stripe.makeInvisible();
    }

    @Override
    public String getSubtype() { return "fearful"; }

    public boolean canBeRemoved() { return !isProtectingCup; }

    public void setProtecting(boolean protecting) { this.isProtectingCup = protecting; }

    public boolean isProtecting() { return isProtectingCup; }
}
