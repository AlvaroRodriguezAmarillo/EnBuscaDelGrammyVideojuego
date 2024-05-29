package juego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 * La clase Inicio define la ventana principal del juego "EN BUSCA DEL GRAMMY".
 * Esta ventana sirve como pantalla de lanzamiento, desde la cual el jugador puede iniciar el juego.
 * Incorpora un fondo musical y animaciones para mejorar la experiencia del usuario antes de comenzar el juego.
 * 
 * @author Ivan/Alvaro
 * @version 1.0
 */
public class Inicio extends JFrame {

    private PanelImagen panel = new PanelImagen();
    private JButton boton = new JButton("INICIO JUEGO");
    private JLabel gifLabel1;
    private JLabel gifLabel2;
    private JLabel logoLabel; 
    private AnuelPacman modelo;
    private Clip clip;

    /**
     * Constructor de Inicio. Configura la ventana de inicio con todos sus componentes:
     * icono de la aplicación, tamaño, ubicación, botón de inicio, animaciones GIF y música de fondo.
     */
    public Inicio() {
        ImageIcon icono = new ImageIcon(getClass().getResource("/imagenes/AnuelLogoJuego.png"));
        setIconImage(icono.getImage());

        this.setTitle("EN BUSCA DEL GRAMMY");
        this.setSize(new Dimension(1100, 700));
        this.setLocationRelativeTo(null);
        setResizable(false);

        boton.setPreferredSize(new Dimension(160, 70));
        boton.setBackground(new Color(226, 186, 52));
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == boton) {
                    if (clip != null) {
                        clip.stop();
                        clip.close();
                    }

                    EventQueue.invokeLater(() -> {
                        JFrame Jframe = new JFrame("EN BUSCA DEL GRAMMY");
                        AnuelPacman Panel = new AnuelPacman();
                        Jframe.setContentPane(Panel);
                        Jframe.setSize(380, 420);
                        Jframe.setLocationRelativeTo(null);
                        Jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        Jframe.setVisible(true);
                    });

                    Inicio.this.setVisible(false);
                    Inicio.this.dispose();
                }
            }
        });

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;

        panel.add(boton, gbc);

        ImageIcon gif1 = new ImageIcon("src/imagenes/AnuelMovimiento.gif");
        gifLabel1 = new JLabel(gif1);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        panel.add(gifLabel1, gbc);

        ImageIcon gif2 = new ImageIcon("src/imagenes/AnuelMovimiento.gif");
        gifLabel2 = new JLabel(gif2);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        panel.add(gifLabel2, gbc);

        this.add(panel, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            File audioFile = new File("src/sonidos/Soldado-Y-Profeta.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Punto de entrada principal del programa. Crea y muestra la ventana de inicio.
     * @param args Argumentos pasados por línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        Inicio frame = new Inicio();
        frame.setVisible(true);
    }
}