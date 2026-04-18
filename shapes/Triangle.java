package shapes;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;

/**
 * Triángulo: una forma geométrica que hereda de Shape
 * 
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 1.0
 */
public class Triangle extends Shape {
    
    private int width;
    private int height;
    private static Canvas canvas;
    private static Canvas getSharedCanvas() {  
        if (Triangle.canvas == null) {
            Triangle.canvas = Canvas.getCanvas();
        }
        return Triangle.canvas;
    }
    
    /**
     * Constructor de Triangle
     */
    public Triangle() {
        super();
        this.width = 50;
        this.height = 50;
    }
    
    /**
     * Cambia el tamaño del triángulo
     * @param newHeight nuevo alto
     * @param newWidth nuevo ancho
     */
    public void changeSize(int newHeight, int newWidth) {
        this.height = newHeight;
        this.width = newWidth;
    }
    
    /**
     * Retorna el ancho del triángulo
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Retorna el alto del triángulo
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Dibuja el triángulo
     */
    @Override
    public void draw(Graphics2D g) {
        if (!isVisible) return;
        
        int[] xPoints = {xPosition, xPosition + width / 2, xPosition + width};
        int[] yPoints = {yPosition + height, yPosition, yPosition + height};
        
        Polygon triangle = new Polygon(xPoints, yPoints, 3);
        
        g.setColor(getColorFromString(color));
        g.fillPolygon(triangle);
    }
    
    /**
     * Borra el triángulo de la pantalla
     */
    @Override
    public void erase() {
        isVisible = false;
    }
    
    /**
     * Mueve el triángulo horizontalmente
     */
    @Override
    public void moveHorizontal(final int distance) {
        xPosition += distance;
        if (this.isVisible) {
            Canvas c = getSharedCanvas();
            c.draw(this, this.color, this);
        }
    }
    
    /**
     * Mueve el triángulo verticalmente
     */
    @Override
    public void moveVertical(final int distance) {
        yPosition += distance;
        if (this.isVisible) {
            Canvas c = getSharedCanvas();
            c.draw(this, this.color, this);
        }
    }
    
    /**
     * Hace visible el triángulo
     */
    public void makeVisible() {
        isVisible = true;
        final Canvas c = getSharedCanvas();
        c.draw(this, this.color, this);
    }
    
    /**
     * Hace invisible el triángulo
     */
    public void makeInvisible() {
        isVisible = false;
        final Canvas c = getSharedCanvas();
        c.erase(this);
    }
    
    /**
     * Cambia el color del triángulo
     */
    @Override
    public void changeColor(String newColor) {
        super.changeColor(newColor);
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
