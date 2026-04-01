package tower;

import java.util.ArrayList;

/**
 * CupOpener: taza especial que elimina todas las tapas que le impiden el paso.
 * Al entrar a la torre, elimina todas las tapas que se encuentran debajo de ella
 * (es decir, las que ya estaban en la torre antes de que ella llegara).
 * Hereda de Cup.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 2.0
 */
public class CupOpener extends Cup {

    /**
     * Constructor de CupOpener.
     * @param number numero de la taza
     * @param color  color de la taza
     */
    public CupOpener(int number, String color) {
        super(number, color);
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
            if (item.getType().equals("lid")) {
                item.erase();
                items.remove(i);
                openerIndex--;   // el opener se desplaza un lugar hacia abajo
            }
        }
    }
}