package tower;

import java.util.ArrayList;
import shapes.Rectangle;

/**
 * CupDominant: taza especial dominante.
 * Al entrar a la torre se inserta en su posicion jerarquica correcta
 * (como CupHierarchical) y ademas elimina todas las tazas mas pequenas
 * que ya estaban dentro de ella.
 * Se distingue visualmente por dos franjas horizontales negras (arriba y abajo).
 * Hereda de Cup.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 2.0
 */
public class CupDominant extends Cup {

    /** Franja superior. */
    private Rectangle stripeTop;
    private int stripeTopX, stripeTopY;

    /** Franja inferior. */
    private Rectangle stripeBot;
    private int stripeBotX, stripeBotY;

    /**
     * Constructor de CupDominant.
     * @param number numero de la taza
     * @param color  color de la taza
     */
    public CupDominant(int number, String color) {
        super(number, color);
        int widthPx = number * SCALE - 2 * WALL;
        stripeTop = new Rectangle();
        stripeTop.changeSize(3, widthPx);
        stripeTop.changeColor("black");
        stripeTopX = DEFAULT_X;
        stripeTopY = DEFAULT_Y;

        stripeBot = new Rectangle();
        stripeBot.changeSize(3, widthPx);
        stripeBot.changeColor("black");
        stripeBotX = DEFAULT_X;
        stripeBotY = DEFAULT_Y;
    }

    /**
     * Dibuja la taza dominant con dos franjas: una arriba y una abajo.
     */
    @Override
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        super.draw(towerCenterX, towerBaseY, yPositionCm);
        int widthPx = number * SCALE;
        int xLeft = towerCenterX - widthPx / 2 + WALL;
        int heightPx = height * SCALE;

        int targetTopX = xLeft;
        int targetTopY = towerBaseY - yPositionCm * SCALE - heightPx + 2;
        moveTo(stripeTop, stripeTopX, stripeTopY, targetTopX, targetTopY);
        stripeTopX = targetTopX;
        stripeTopY = targetTopY;
        stripeTop.makeVisible();

        int targetBotX = xLeft;
        int targetBotY = towerBaseY - yPositionCm * SCALE - SCALE - 3;
        moveTo(stripeBot, stripeBotX, stripeBotY, targetBotX, targetBotY);
        stripeBotX = targetBotX;
        stripeBotY = targetBotY;
        stripeBot.makeVisible();
    }

    /**
     * Oculta la taza dominant y sus dos franjas.
     */
    @Override
    public void erase() {
        super.erase();
        stripeTop.makeInvisible();
        stripeBot.makeInvisible();
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