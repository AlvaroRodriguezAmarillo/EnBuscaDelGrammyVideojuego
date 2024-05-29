package juego;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * La clase PanelImagen extiende JPanel para proporcionar un panel personalizado que muestra una imagen de fondo.
 * La imagen se ajustará para cubrir todo el panel, adaptándose al tamaño actual del mismo.
 * @author Ivan/Alvaro
 * @version 1.0
 */

public class PanelImagen extends JPanel {

    /**
     * Sobrescribe el método paint de la clase JPanel para dibujar la imagen de fondo en el panel.
     * La imagen se escala para ajustarse al tamaño actual del panel. Este método se llama automáticamente por la JVM.
     * 
     * @param g El contexto gráfico que se utiliza para pintar. Proporcionado por la JVM.
     */
    @Override
    public void paint(Graphics g) {
        // Obtiene las dimensiones actuales del panel.
        Dimension dimension = this.getSize();
        
        // Carga la imagen de fondo.
        ImageIcon icon = new ImageIcon(getClass().getResource("/imagenes/inicioImagen.jpeg"));
        
        // Dibuja la imagen en el panel, ajustándola para cubrir completamente el fondo.
        g.drawImage(icon.getImage(), 0, 0, dimension.width, dimension.height, null);
        
        // Establece el panel como no opaco para permitir que se vean las imágenes y los colores de fondo.
        setOpaque(false);
        
        // Llama a paintChildren para asegurar que los componentes hijos del panel se pinten correctamente sobre la imagen de fondo.
        super.paintChildren(g);
    }
}