package tower;

import java.util.ArrayList;
import shapes.Rectangle;

/**
 * CupHierarchical: taza especial jerarquica.
 * Al entrar a la torre va desplazando todos los objetos de menor tamanio hacia
 * arriba, insertandose en la posicion correcta segun su numero.
 * Si logra llegar al fondo de la torre (queda en la posicion mas baja), ya no
 * puede ser quitada.
 * Se distingue visualmente por una franja horizontal negra al centro de la taza.
 * Hereda de Cup.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 3.0
 */
public class CupHierarchical extends Cup {

    /** Indica si esta taza llego al fondo de la torre. */
    private boolean reachedBottom;

    /** Franja visual al centro de la taza. */
    private Rectangle stripe;
    private int stripeX, stripeY;

    /**
     * Constructor de CupHierarchical.
     * @param number numero de la taza
     * @param color  color de la taza
     */
    public CupHierarchical(int number, String color) {
        super(number, color);
        this.reachedBottom = false;
        int widthPx = number * SCALE - 2 * WALL;
        stripe = new Rectangle();
        stripe.changeSize(3, widthPx);
        stripe.changeColor("black");
        stripeX = DEFAULT_X;
        stripeY = DEFAULT_Y;
    }

    /**
     * Dibuja la taza hierarchical con su franja al centro.
     */
    @Override
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        super.draw(towerCenterX, towerBaseY, yPositionCm);
        int widthPx = number * SCALE;
        int xLeft = towerCenterX - widthPx / 2 + WALL;
        int heightPx = height * SCALE;
        int targetX = xLeft;
        int targetY = towerBaseY - yPositionCm * SCALE - heightPx / 2 - 1;
        moveTo(stripe, stripeX, stripeY, targetX, targetY);
        stripeX = targetX;
        stripeY = targetY;
        stripe.makeVisible();
    }

    /**
     * Oculta la taza hierarchical y su franja.
     */
    @Override
    public void erase() {
        super.erase();
        stripe.makeInvisible();
    }


    /**
     * Retorna el subtipo de esta taza.
     * @return "hierarchical"
     */
    @Override
    public String getSubtype() {
        return "hierarchical";
    }

    /**
     * Indica si esta taza llego al fondo y por tanto no puede quitarse.
     * @return true si llego al fondo de la torre
     */
    public boolean hasReachedBottom() {
        return reachedBottom;
    }

    /**
     * Inserta esta taza en la posicion correcta dentro de la torre.
     * La taza se coloca antes del primer elemento (cup o lid) que tenga
     * un numero menor al suyo, desplazando todo lo que esta por encima.
     * Si queda en la posicion 0 (el fondo), se marca como inamovible.
     *
     * @param items lista de elementos en la torre
     * @return el indice donde quedo insertada
     */
    public int insertHierarchically(ArrayList<StackItem> items) {
        // Buscar la primera posicion donde haya un elemento con numero menor
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getNumber() < this.number) {
                items.add(i, this);
                reachedBottom = (i == 0);
                return i;
            }
        }
        // Si todos los elementos existentes son mayores o iguales, va al final
        items.add(this);
        reachedBottom = false;
        return items.size() - 1;
    }
}