package tower;

/**
 * LidCrazy: tapa especial loca.
 * En lugar de tapar su taza (altura = 1), se pone de BASE (altura = 0).
 * Esto significa que NO suma altura a la torre.
 * Hereda de Lid.
 * 
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 1.0
 */
public class LidCrazy extends Lid {
    
    /**
     * Constructor de LidCrazy
     * @param number número de la tapa
     * @param color color de la tapa
     */
    public LidCrazy(int number, String color) {
        super(number, color);
        // Sobrescribimos la altura: en lugar de 1, es 0
        this.height = 0;
    }
    
    /**
     * Retorna el subtipo de esta tapa
     * @return "crazy"
     */
    @Override
    public String getSubtype() {
        return "crazy";
    }
    
    /**
     * Sobrescribimos getHeight() para retornar 0 en lugar de 1.
     * Una tapa loca NO suma altura a la torre.
     * 
     * @return 0 (no suma altura)
     */
    @Override
    public int getHeight() {
        return 0;
    }
}