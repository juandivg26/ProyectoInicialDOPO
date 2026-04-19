package shapes;

import java.awt.Graphics2D;

/**
 * Clase abstracta base para todas las formas geométricas.
 * Define la interfaz común que todas las formas deben implementar.
 * 
 * @author Juan Diego Valderrama Gaviria y Jhonatan Madero
 * @version 1.0
 */
public abstract class Shape {
    
    protected String color;
    protected int xPosition;
    protected int yPosition;
    protected boolean isVisible;
    
    /**
     * Constructor de Shape
     */
    public Shape() {
        this.color = "black";
        this.xPosition = 0;
        this.yPosition = 0;
        this.isVisible = false;
    }
    
    /**
     * Dibuja la forma en un contexto gráfico 2D
     * @param g el contexto gráfico donde dibujar
     */
    public abstract void draw(Graphics2D g);
    
    /**
     * Borra la forma de la pantalla
     */
    public abstract void erase();
    
    /**
     * Cambia el color de la forma
     * @param newColor el nuevo color
     */
    public void changeColor(String newColor) {
        this.color = newColor;
    }
    
    /**
     * Mueve la forma horizontalmente
     * @param distance distancia a mover (positiva = derecha, negativa = izquierda)
     */
    public abstract void moveHorizontal(int distance);
    
    /**
     * Mueve la forma verticalmente
     * @param distance distancia a mover (positiva = arriba, negativa = abajo)
     */
    public abstract void moveVertical(int distance);
    
    /**
     * Retorna la posición X de la forma
     * @return posición X
     */
    public int getXPosition() {
        return xPosition;
    }
    
    /**
     * Retorna la posición Y de la forma
     * @return posición Y
     */
    public int getYPosition() {
        return yPosition;
    }
    
    /**
     * Retorna el color de la forma
     * @return color actual
     */
    public String getColor() {
        return color;
    }
    
    /**
     * Verifica si la forma es visible
     * @return true si es visible, false si no
     */
    public boolean isVisible() {
        return isVisible;
    }
}