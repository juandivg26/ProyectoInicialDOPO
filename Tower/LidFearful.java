package tower;

/**
 * LidFearful: tapa especial asustada.
 * Tiene dos comportamientos especiales respecto a la tapa normal:
 * <ul>
 *   <li>No entra a la torre si su taza companiera no esta en ella.</li>
 *   <li>No puede removerse si en este momento esta tapando (justo encima)
 *       a su taza companiera.</li>
 * </ul>
 * La verificacion de entrada la realiza Tower antes de llamar al constructor;
 * la verificacion de salida la realiza Tower en removeLid / popLid.
 * Hereda de Lid.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 2.0
 */
public class LidFearful extends Lid {

    /**
     * Indica si esta tapa esta actualmente tapando (justo encima de)
     * su taza companiera. Se actualiza en Tower cada vez que se redibuja.
     */
    private boolean isProtectingCup;

    /**
     * Constructor de LidFearful.
     * @param number numero de la tapa
     * @param color  color de la tapa
     */
    public LidFearful(int number, String color) {
        super(number, color);
        this.isProtectingCup = false;
    }

    /**
     * Retorna el subtipo de esta tapa.
     * @return "fearful"
     */
    @Override
    public String getSubtype() {
        return "fearful";
    }

    /**
     * Indica si esta tapa puede salir de la torre.
     * Una tapa fearful NO puede removerse si en este momento esta
     * cubriendo (justo encima de) su taza companiera.
     *
     * @return true si puede removerse, false si esta protegiendo su taza
     */
    public boolean canBeRemoved() {
        return !isProtectingCup;
    }

    /**
     * Actualiza si esta tapa esta protegiendo su taza companiera.
     * Tower llama a este metodo en updateCoveredStatus() y en pushLid().
     *
     * @param protecting true si esta justo encima de su taza
     */
    public void setProtecting(boolean protecting) {
        this.isProtectingCup = protecting;
    }

    /**
     * Retorna si esta tapa esta protegiendo su taza.
     * @return true si esta protegiendo
     */
    public boolean isProtecting() {
        return isProtectingCup;
    }
}