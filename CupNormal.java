package tower;

/**
 * CupNormal: taza estándar de la torre.
 * Tiene comportamiento normal: cabe dentro de otras tazas más grandes.
 * Hereda de Cup.
 * 
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 1.0
 */
public class CupNormal extends Cup {
    
    /**
     * Constructor de CupNormal
     * @param number número de la taza
     * @param color color de la taza
     */
    public CupNormal(int number, String color) {
        super(number, color);
    }
    
    /**
     * Retorna el subtipo de esta taza
     * @return "normal"
     */
    @Override
    public String getSubtype() {
        return "normal";
    }
}