package tower;

/**
 * LidNormal: tapa estándar de la torre.
 * Tiene comportamiento normal: tapa su taza correspondiente.
 * Hereda de Lid.
 * 
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 1.0
 */
public class LidNormal extends Lid {
    
    /**
     * Constructor de LidNormal
     * @param number número de la tapa
     * @param color color de la tapa
     */
    public LidNormal(int number, String color) {
        super(number, color);
    }
    
    /**
     * Retorna el subtipo de esta tapa
     * @return "normal"
     */
    @Override
    public String getSubtype() {
        return "normal";
    }
}