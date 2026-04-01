package shapes;

import java.awt.Graphics2D;
import java.awt.Color;

/**
 * Rectángulo: una forma geométrica que hereda de Shape
 */
public class Rectangle extends Shape {
    
    private int width;
    private int height;
    private static Canvas canvas;
    
    /**
     * Constructor de Rectangle
     */
    public Rectangle() {
        super();
        this.width = 50;
        this.height = 50;
        // NO inicializar canvas aquí, solo cuando se necesite
    }
    
    /**
     * Obtiene el Canvas singleton (inicialización lazy)
     */
    private static Canvas getSharedCanvas() {
        if (canvas == null) {
            canvas = Canvas.getCanvas();
        }
        return canvas;
    }
    
    /**
     * Cambia el tamaño del rectángulo
     */
    public void changeSize(int newHeight, int newWidth) {
        this.height = newHeight;
        this.width = newWidth;
    }
    
    /**
     * Retorna el ancho del rectángulo
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Retorna el alto del rectángulo
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Dibuja el rectángulo
     */
    @Override
    public void draw(Graphics2D g) {
        if (!isVisible) return;
        g.setColor(getColorFromString(color));
        g.fillRect(xPosition, yPosition, width, height);
    }
    
    /**
     * Borra el rectángulo de la pantalla
     */
    @Override
    public void erase() {
        isVisible = false;
    }
    
    /**
     * Mueve el rectángulo horizontalmente
     */
    @Override
    public void moveHorizontal(int distance) {
        xPosition += distance;
    }
    
    /**
     * Mueve el rectángulo verticalmente
     */
    @Override
    public void moveVertical(int distance) {
        yPosition += distance;
    }
    
    /**
     * Hace visible el rectángulo
     */
    public void makeVisible() {
        isVisible = true;
        Canvas c = getSharedCanvas();
        c.draw(this, color, this);
    }
    
    /**
     * Hace invisible el rectángulo
     */
    public void makeInvisible() {
        isVisible = false;
        Canvas c = getSharedCanvas();
        c.erase(this);
    }
    
    /**
     * Cambia el color del rectángulo
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