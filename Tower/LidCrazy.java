package tower;

import shapes.Rectangle;

/**
 * LidCrazy: tapa especial loca.
 * En lugar de tapar su taza, se ubica de base (altura = 0).
 * Se distingue visualmente por una franja horizontal negra que ocupa todo su ancho interior.
 * Hereda de Lid.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 2.0
 */
public class LidCrazy extends Lid {

    /** Franja horizontal que cruza toda la tapa. */
    private Rectangle stripe;
    private int stripeX, stripeY;

    /**
     * Constructor de LidCrazy.
     * @param number numero de la tapa
     * @param color  color de la tapa
     */
    public LidCrazy(int number, String color) {
        super(number, color);
        this.height = 0;
        int widthPx = number * SCALE;
        stripe = new Rectangle();
        stripe.changeSize(3, widthPx);
        stripe.changeColor("black");
        stripeX = DEFAULT_X;
        stripeY = DEFAULT_Y;
    }

    /**
     * Dibuja la tapa crazy con su franja horizontal al centro.
     */
    @Override
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        super.draw(towerCenterX, towerBaseY, yPositionCm);
        int widthPx = number * SCALE;
        int xLeft = towerCenterX - widthPx / 2;
        int targetX = xLeft;
        int targetY = towerBaseY - yPositionCm * SCALE - SCALE / 2;
        moveTo(stripe, stripeX, stripeY, targetX, targetY);
        stripeX = targetX;
        stripeY = targetY;
        stripe.makeVisible();
    }

    /**
     * Oculta la tapa crazy y su franja.
     */
    @Override
    public void erase() {
        super.erase();
        stripe.makeInvisible();
    }

    @Override
    public String getSubtype() { return "crazy"; }

    @Override
    public int getHeight() { return 0; }
}
