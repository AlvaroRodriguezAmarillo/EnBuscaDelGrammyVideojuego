package juego;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * Implementa una version del clasico juego Pacman, personalizada con tematica y elementos especificos.
 * Este juego incluye funcionalidades como musica de fondo continua y manejo de eventos de teclado
 * para el control de Pacman. Extiende {@link JPanel} e implementa {@link ActionListener}
 * para gestionar eventos dentro del juego.

 * @author Ivan/Alvaro
 * @version 1.0

 */


public class AnuelPacman extends JPanel implements ActionListener {
	private Clip backgroundSound;{

		try {

			File audio = new File("src/sonidos/MusicaFondo.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audio);
			backgroundSound = AudioSystem.getClip();
			backgroundSound.open(audioStream);
			backgroundSound.loop(Clip.LOOP_CONTINUOUSLY);

		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();

		}

	}


	private Dimension d;
	private final Font smallFont = new Font("Arial", Font.BOLD, 14);
	private boolean EnJuego = false;
	private boolean muerte = false;
	private final int TamanoBloque = 24;
	private final int NumBloques = 15;
	private final int TamanoPantalla = NumBloques * TamanoBloque;
	private final int MaxPolicia = 12;
	private final int AnuelVelocidad = 6;
	private int NumPolicia = 5;
	private int Vidas, Puntos;
	private int[] dx, dy;
	private int[] policia_x, policia_y, policia_dx, policia_dy, policiaVelocidad;
	private Image grammy, policia;
	private Image arriba, abajo, izquierda, derecha;
	private int anuel_x, anuel_y, anueld_x, anueld_y;
	private int req_dx, req_dy;

	private final short Mapa[] = {

			19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
			17, 16, 24, 24, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
			17, 20,  0,  0,  0, 17, 16, 16, 24, 16, 16, 24, 16, 16, 20,
			17, 20,  0,  0,  0, 17, 16, 20,  0, 17, 20,  0, 17, 16, 20,
			17, 16, 18, 18, 18, 16, 16, 16, 18, 16, 16, 18, 16, 16, 20,
			17, 16, 16, 16, 16, 16, 16, 16, 24, 16, 16, 24, 16, 16, 20,
			17, 16, 16, 16, 16, 16, 16, 20,  0, 17, 20,  0, 17, 16, 20,
			17, 16, 16, 16, 24, 16, 16, 16, 18, 16, 16, 18, 16, 16, 20,
			17, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
			17, 16, 24, 28,  0, 25, 24, 16, 16, 16, 24, 16, 16, 16, 20,
			17, 20,  0,  0,  0, 0,   0, 17, 16, 20,  0, 17, 16, 16, 20,
			17, 16, 18, 22,  0, 19, 18, 16, 24, 28,  0, 25, 24, 16, 20,
			17, 16, 16, 20,  0, 17, 16, 20,  0,  0,  0,  0,  0, 17, 20,
			17, 16, 16, 16, 18, 16, 16, 16, 18, 18, 18, 18, 18, 16, 20,
			25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28

	};

	private final int Velocidades[] = {1, 2, 3, 4, 6, 8};
	private final int VelocidadMax = 6;
	private int VelocidadInicio = 3;
	private short[] screenData;
	private Timer timer;


	public AnuelPacman() {

		MostrarImagenes();
		Variables();
		addKeyListener(new TAdapter());
		setFocusable(true);
		InicioNivel();

	}


	private void MostrarImagenes() {
		
		abajo = new ImageIcon("src/imagenes/AnuelAbajo.gif").getImage();
		arriba = new ImageIcon("src/imagenes/AnuelArriba.gif").getImage();
		izquierda = new ImageIcon("src/imagenes/AnuelIzq.gif").getImage();
		derecha = new ImageIcon("src/imagenes/AnuelDcha.gif").getImage();
		policia = new ImageIcon("src/imagenes/Policia.gif").getImage();
		grammy = new ImageIcon("src/imagenes/VidasGrammy.png").getImage();


	}

	private void Variables() {

		screenData = new short[NumBloques * NumBloques];
		d = new Dimension(400, 400);
		
		policia_x = new int[MaxPolicia];
		policia_dx = new int[MaxPolicia];
		policia_y = new int[MaxPolicia];
		policia_dy = new int[MaxPolicia];
		policiaVelocidad = new int[MaxPolicia];
		dx = new int[4];
		dy = new int[4];


		timer = new Timer(40, this);
		timer.start();

	}

	/**
	 * Controla el flujo principal del juego, incluyendo la verificacion de la muerte de Anuel,
	 * el movimiento de Anuel y los policias, y la verificacion del estado del laberinto.
	 *

	 * @param g2d Contexto grafico utilizado para dibujar los elementos del juego.
	 */



	private void Jugar(Graphics2D g2d) {

		if (muerte) {
			Muerte();

		} else {
			if (!JuegoGanado()) {
				MoverAnuel();
				MostrarAnuel(g2d);
				MoverPolicia(g2d);
				VerificarLaberinto();

			} else {
				MostrarFinalJuego(g2d, "¡HAS GANADO!");

				req_dx = 0;
				req_dy = 0;
				anueld_x = 0;
				anueld_y = 0;

				for (int i = 0; i < NumPolicia; i++) {
					policia_dx[i] = 0;
					policia_dy[i] = 0;

				}

			}

		}

	}



	/**
	 * Verifica si el juego ha sido ganado al comprobar si todos los puntos han sido recolectados.
	 *

	 * @return true si el juego ha sido ganado, false en caso contrario.
	 */

	private boolean JuegoGanado() {

		for (int i = 0; i < NumBloques * NumBloques; i++) {
			if ((screenData[i] & 16) != 0) {

				return false; 

			}

		}

		return true; 

	}

	/**
	 * Muestra un mensaje de fin de juego y un grafico asociado cuando el jugador gana el juego.
	 *

	 * @param g2d Contexto grafico utilizado para dibujar el mensaje y el grafico de fin de juego.
	 * @param message Mensaje de fin de juego a mostrar.

	 */

	private void MostrarFinalJuego(Graphics2D g2d, String message) {

		ImageIcon gif = new ImageIcon("src/imagenes/Creditos1.gif");
		Image Imagen = gif.getImage();

		g2d.drawImage(Imagen, 0, 0, TamanoPantalla, TamanoPantalla, null);

	}

	/**
	 * Muestra la pantalla de inicio del juego, con instrucciones para comenzar a jugar.
	 *
	 *
	 * @param g2d Contexto grafico utilizado para dibujar la pantalla de inicio.
	 */

	private void MostrarInicioJuego(Graphics2D g2d) {

		ImageIcon backgroundImageIcon = new ImageIcon("src/imagenes/AnuelBaile.gif");
		Image backgroundImage = backgroundImageIcon.getImage();

		g2d.drawImage(backgroundImage, 0, 0, TamanoPantalla, TamanoPantalla, null);

		String mensaje = "PULSA ESPACIO PARA COMENZAR";
		FontMetrics fm = g2d.getFontMetrics();


		int AnchoTexto = fm.stringWidth(mensaje);
		int AltoTexto = fm.getHeight();
		int x = (TamanoPantalla - AnchoTexto) / 2;
		int y = (TamanoPantalla - AltoTexto) / 2 + fm.getAscent();

		g2d.setColor(Color.YELLOW );
		g2d.drawString(mensaje, x, y);

	}


	/**
	 * Dibuja el puntaje actual y las vidas restantes en la pantalla.
	 *

	 * @param g Contexto grafico utilizado para dibujar el puntaje y las vidas.
	 */

	private void MostrarPuntos(Graphics2D g) {

		g.setFont(smallFont);
		g.setColor(Color.white);

		String s = "Puntos: " + Puntos;

		g.drawString(s, TamanoPantalla / 2 + 96, TamanoPantalla + 16);

		for (int i = 0; i < Vidas; i++) {
			g.drawImage(grammy, i * 28 + 8, TamanoPantalla + 1, this);

		}

	}

	private void VerificarLaberinto() {

		int i = 0;
		boolean finalizado = true;

		while (i < NumBloques * NumBloques && finalizado) {

			if ((screenData[i]) != 0) {
				finalizado = false;

			}

			i++;

		}
		if (finalizado) {
			Puntos += 50;

			if (NumPolicia < MaxPolicia) {
				NumPolicia++;

			}

			if (VelocidadInicio < VelocidadMax) {
				VelocidadInicio++;

			}

			InicioNivel();
		}

	}


	private void Muerte() {

		Vidas--;
		
		if (Vidas == 0) {

			EnJuego = false;

		}

		ContinuarNivel();

	}

	/**
	 * Mueve a los policias dentro del laberinto. Este metodo decide la nueva direccion de los policias
	 * basandose en la posicion actual y las condiciones del laberinto para simular una persecucion.
	 * Ademas, verifica si hay colisiones con Anuel para determinar si el jugador ha perdido una vida.
	 *

	 * @param g2d Contexto grafico utilizado para dibujar los policias en el laberinto.
	 */

	
	private void MoverPolicia(Graphics2D g2d) {

		int posicion;
		int contador;

		for (int i = 0; i < NumPolicia; i++) {

			if (policia_x[i] % TamanoBloque == 0 && policia_y[i] % TamanoBloque == 0) {
				posicion = policia_x[i] / TamanoBloque + NumBloques * (int) (policia_y[i] / TamanoBloque);
				contador = 0;

				if ((screenData[posicion] & 1) == 0 && policia_dx[i] != 1) {
					dx[contador] = -1;
					dy[contador] = 0;
					contador++;

				}

				if ((screenData[posicion] & 2) == 0 && policia_dy[i] != 1) {
					dx[contador] = 0;
					dy[contador] = -1;
					contador++;

				}

				if ((screenData[posicion] & 4) == 0 && policia_dx[i] != -1) {
					dx[contador] = 1;
					dy[contador] = 0;
					contador++;
				}

				if ((screenData[posicion] & 8) == 0 && policia_dy[i] != -1) {
					dx[contador] = 0;
					dy[contador] = 1;
					contador++;

				}

				if (contador == 0) {

					if ((screenData[posicion] & 15) == 15) {
						policia_dx[i] = 0;
						policia_dy[i] = 0;

					} else {
						policia_dx[i] = -policia_dx[i];
						policia_dy[i] = -policia_dy[i];

					}

				} else {
					contador = (int) (Math.random() * contador);
					
					if (contador > 3) {
						contador = 3;

					}

					policia_dx[i] = dx[contador];
					policia_dy[i] = dy[contador];

				}

			}

			policia_x[i] = policia_x[i] + (policia_dx[i] * policiaVelocidad[i]);
			policia_y[i] = policia_y[i] + (policia_dy[i] * policiaVelocidad[i]);

			MostrarPolicia(g2d, policia_x[i] + 1, policia_y[i] + 1);

			if (anuel_x > (policia_x[i] - 12) && anuel_x < (policia_x[i] + 12)
					&& anuel_y > (policia_y[i] - 12) && anuel_y < (policia_y[i] + 12)
					&& EnJuego) {

				muerte = true;

			}

		}

	}

	/**
	 * Dibuja un policia en la ubicacion especificada.
	 *
	 *
	 * @param g2d Contexto grafico utilizado para dibujar el policia.
	 * @param x Coordenada x donde se dibujara el policia.
	 * @param y Coordenada y donde se dibujara el policia.

	 */

	private void MostrarPolicia(Graphics2D g2d, int x, int y) {
		g2d.drawImage(policia, x, y, this);

	}


	private void MoverAnuel() {
		
		int posicion;
		short ch;

		if (anuel_x % TamanoBloque == 0 && anuel_y % TamanoBloque == 0) {
			posicion = anuel_x / TamanoBloque + NumBloques * (int) (anuel_y / TamanoBloque);
			ch = screenData[posicion];

			if ((ch & 16) != 0) {
				screenData[posicion] = (short) (ch & 15);
				Puntos++;

			}

			if (req_dx != 0 || req_dy != 0) {
				if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)

						|| (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
						|| (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
						|| (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {

					anueld_x = req_dx;
					anueld_y = req_dy;

				}

			}

			if ((anueld_x == -1 && anueld_y == 0 && (ch & 1) != 0)

					|| (anueld_x == 1 && anueld_y == 0 && (ch & 4) != 0)
					|| (anueld_x == 0 && anueld_y == -1 && (ch & 2) != 0)
					|| (anueld_x == 0 && anueld_y == 1 && (ch & 8) != 0)) {

				anueld_x = 0;
				anueld_y = 0;

			}

		}

		anuel_x = anuel_x + AnuelVelocidad * anueld_x;
		anuel_y = anuel_y + AnuelVelocidad * anueld_y;

	}

	/**
	 * Dibuja a Anuel en su posicion actual, cambiando su direccion de acuerdo a la entrada del jugador.
	 *
	 * @param g2d Contexto grafico utilizado para dibujar a Anuel.
	 */

	private void MostrarAnuel(Graphics2D g2d) {

		if (req_dx == -1) {
			g2d.drawImage(izquierda, anuel_x + 1, anuel_y + 1, this);

		} else if (req_dx == 1) {
			g2d.drawImage(derecha, anuel_x + 1, anuel_y + 1, this);

		} else if (req_dy == -1) {
			g2d.drawImage(arriba, anuel_x + 1, anuel_y + 1, this);

		} else {
			g2d.drawImage(abajo, anuel_x + 1, anuel_y + 1, this);

		}

	}

	private void MostrarLaberinto(Graphics2D g2d) {

		short i = 0;
		int x;
		int y;

		for (y = 0; y < TamanoPantalla; y += TamanoBloque) {
			for (x = 0; x < TamanoPantalla; x += TamanoBloque) {
				g2d.setColor(new Color(241, 207, 8));
				g2d.setStroke(new BasicStroke(5));

				if ((Mapa[i] == 0)) {
					g2d.fillRect(x, y, TamanoBloque, TamanoBloque);

				}

				if ((screenData[i] & 1) != 0) {
					g2d.drawLine(x, y, x, y + TamanoBloque - 1);

				}

				if ((screenData[i] & 2) != 0) {
					g2d.drawLine(x, y, x + TamanoBloque - 1, y);

				}

				if ((screenData[i] & 4) != 0) {
					g2d.drawLine(x + TamanoBloque - 1, y, x + TamanoBloque - 1, y + TamanoBloque - 1);

				}



				if ((screenData[i] & 8) != 0) {
					g2d.drawLine(x, y + TamanoBloque - 1, x + TamanoBloque - 1, y + TamanoBloque - 1);

				}

				if ((screenData[i] & 16) != 0) {
					g2d.setColor(new Color(255,255,255));
					g2d.fillOval(x + 10, y + 10, 6, 6);

				}

				i++;

			}

		}

	}



	private void InicioJuego() {

		Vidas = 3;
		Puntos = 0;
		InicioNivel();
		NumPolicia = 5;
		VelocidadInicio = 3;

	}


	private void InicioNivel() {

		int i;
		for (i = 0; i < NumBloques * NumBloques; i++) {
			screenData[i] = Mapa[i];

		}

		ContinuarNivel();

	}

	
	private void ContinuarNivel() {

		int dx = 1;
		int random;
		
		for (int i = 0; i < NumPolicia; i++) {

			policia_y[i] = 4 * TamanoBloque; 
			policia_x[i] = 4 * TamanoBloque;
			policia_dy[i] = 0;
			policia_dx[i] = dx;
			dx = -dx;
			random = (int) (Math.random() * (VelocidadInicio + 1));

			if (random > VelocidadInicio) {
				random = VelocidadInicio;

			}

			policiaVelocidad[i] = Velocidades[random];

		}

		anuel_x = 7 * TamanoBloque;
		anuel_y = 11 * TamanoBloque;
		anueld_x = 0; 
		anueld_y = 0;
		req_dx = 0; 
		req_dy = 0;
		muerte = false;

	}

	/**
	 * Metodo sobreescrito de JPanel que se llama automaticamente para pintar los componentes del juego.
	 * Este metodo es responsable de dibujar el laberinto, a Anuel, a los policias, y la puntuacion actual.
	 *

	 * @param g El contexto grafico en el que se dibujaran los componentes.
	 */



	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, d.width, d.height);

		MostrarLaberinto(g2d);
		MostrarPuntos(g2d);


		if (EnJuego) {
			Jugar(g2d);
			
		} else {
			MostrarInicioJuego(g2d);

		}

		Toolkit.getDefaultToolkit().sync();
		g2d.dispose();

	}





	class TAdapter extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();
			
			if (EnJuego) {
				if (key == KeyEvent.VK_LEFT) {
					req_dx = -1;
					req_dy = 0;

				} else if (key == KeyEvent.VK_RIGHT) {
					req_dx = 1;
					req_dy = 0;

				} else if (key == KeyEvent.VK_UP) {
					req_dx = 0;
					req_dy = -1;

				} else if (key == KeyEvent.VK_DOWN) {
					req_dx = 0;
					req_dy = 1;

				} else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
					EnJuego = false;

				}

			} else {

				if (key == KeyEvent.VK_SPACE) {
					EnJuego = true;
					InicioJuego();

				}

			}

		}

	}



	/**
	 * Metodo sobrescrito de ActionListener que se invoca cada vez que ocurre una accion, como un evento del temporizador.
	 * Es responsable de redibujar el juego y actualizar el estado del juego.
	 *

	 * @param e El evento que desencadeno la llamada a este metodo.
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();

	}



}