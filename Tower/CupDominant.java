package tower;

import java.util.ArrayList;

/**
 * CupDominant: taza especial dominante.
 * Al entrar a la torre se inserta en su posicion jerarquica correcta
 * (como CupHierarchical) y ademas elimina todas las tazas mas pequenas
 * que ya estaban dentro de ella, pues no tolera que haya tazas menores
 * a su alrededor.
 *
 * Ejemplo: si la torre tiene [Cup5, Cup2] y entra CupDominant3,
 * la Cup2 es eliminada y la torre queda [Cup5, CupDominant3].
 *
 * Hereda de Cup.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 1.0
 */
public class CupDominant extends Cup {

    /**
     * Constructor de CupDominant.
     * @param number numero de la taza
     * @param color  color de la taza
     */
    public CupDominant(int number, String color) {
        super(number, color);
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