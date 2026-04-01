package shapes;

import java.awt.Graphics2D;
import java.awt.Color;

/**
 * Círculo: una forma geométrica que hereda de Shape
 * 
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 1.0
 */
public class Circle extends Shape {
    
    private int diameter;
    
    /**
     * Constructor de Circle
     */
    public Circle() {
        super();
        this.diameter = 30;
    }
    
    /**
     * Cambia el diámetro del círculo
     * @param newDiameter nuevo diámetro
     */
    public void changeDiameter(int newDiameter) {
        this.diameter = newDiameter;
    }
    
    /**
     * Retorna el diámetro del círculo
     * @return diámetro actual
     */
    public int getDiameter() {
        return diameter;
    }
    
    /**
     * Dibuja el círculo
     */
    @Override
    public void draw(Graphics2D g) {
        if (!isVisible) return;
        
        g.setColor(getColorFromString(color));
        g.fillOval(xPosition, yPosition, diameter, diameter);
    }
    
    /**
     * Borra el círculo de la pantalla
     */
    @Override
    public void erase() {
        isVisible = false;
    }
    
    /**
     * Mueve el círculo horizontalmente
     */
    @Override
    public void moveHorizontal(int distance) {
        xPosition += distance;
    }
    
    /**
     * Mueve el círculo verticalmente
     */
    @Override
    public void moveVertical(int distance) {
        yPosition += distance;
    }
    
    /**
     * Hace visible el círculo
     */
    public void makeVisible() {
        isVisible = true;
    }
    
    /**
     * Hace invisible el círculo
     */
    public void makeInvisible() {
        isVisible = false;
    }
    
    /**
     * Convierte un nombre de color (String) a un objeto Color de Java
     */
    private Color getColorFromString(String colorName) {
        if (colorName.equals("red")) return Color.RED;
        if (colorName.equals("blue")) return Color.BLUE;
        if (colorName.equals("green")) return Color.GREEN;
        if (colorName.equals("yellow")) return Color.YELLOW;
        if (colorName.equals("magenta")) return Color.MAGENTA;
        if (colorName.equals("white")) return Color.WHITE;
        return Color.BLACK;
    }
}