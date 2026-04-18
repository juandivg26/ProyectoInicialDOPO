package tower;

import java.util.ArrayList;
import shapes.Rectangle;
import shapes.Circle;

/**
 * CupHierarchical: taza especial jerarquica.
 * Al entrar a la torre va desplazando todos los objetos de menor tamaño hacia
 * arriba, insertandose en la posicion correcta segun su numero.
 * Si logra llegar al fondo de la torre (queda en la posicion mas baja), ya no
 * puede ser quitada.
 * Se distingue visualmente por DOS círculos negros uno al lado del otro en la base.
 * Hereda de Cup.
 *
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 4.0
 */
public class CupHierarchical extends Cup {

    /** Indica si esta taza llego al fondo de la torre. */
    private boolean reachedBottom;

    /** Círculo izquierdo */
    private Circle circleLeft;
    private int circleLeftX, circleLeftY;

    /** Círculo derecho */
    private Circle circleRight;
    private int circleRightX, circleRightY;

    /**
     * Constructor de CupHierarchical.
     * @param number numero de la taza
     * @param color  color de la taza
     */
    public CupHierarchical(int number, String color) {
        super(number, color);
        this.reachedBottom = false;
        int size = SCALE - 8;
        
        // Círculo izquierdo
        circleLeft = new Circle();
        circleLeft.changeDiameter(size);
        circleLeft.changeColor("black");
        circleLeftX = DEFAULT_X;
        circleLeftY = DEFAULT_Y;
        
        // Círculo derecho
        circleRight = new Circle();
        circleRight.changeDiameter(size);
        circleRight.changeColor("black");
        circleRightX = DEFAULT_X;
        circleRightY = DEFAULT_Y;
    }
    

    /**
     * Dibuja la taza hierarchical con su franja al centro.
     */
    @Override
    public void draw(int towerCenterX, int towerBaseY, int yPositionCm) {
        super.draw(towerCenterX, towerBaseY, yPositionCm);
        int widthPx = number * SCALE;
        int xLeft = towerCenterX - widthPx / 2;
        int basePx = 1 * SCALE;
        int targetBaseY = towerBaseY - yPositionCm * SCALE - basePx;
        
        int size = SCALE - 8;
        int spacing = 6;  // Espacio entre círculos
        int totalWidth = size * 2 + spacing;
        int startX = xLeft + (widthPx - totalWidth) / 2;
        int targetY = targetBaseY + (basePx - size) / 2;
        
        // Círculo izquierdo
        int targetLeftX = startX;
        moveCircle(circleLeft, circleLeftX, circleLeftY, targetLeftX, targetY);
        circleLeftX = targetLeftX;
        circleLeftY = targetY;
        circleLeft.makeVisible();
        
        // Círculo derecho
        int targetRightX = startX + size + spacing;
        moveCircle(circleRight, circleRightX, circleRightY, targetRightX, targetY);
        circleRightX = targetRightX;
        circleRightY = targetY;
        circleRight.makeVisible();
    }

    /**
     * Oculta la taza hierarchical y su franja.
     */
    @Override
    public void erase() {
        super.erase();
        circleLeft.makeInvisible();
        circleRight.makeInvisible();
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
