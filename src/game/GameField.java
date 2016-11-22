package game;

import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Ein Spielfeld mit verschiedenen Eigenschaften um eine unterschiedliche
 * Struktur des gesamten Spieles erzeugen zu können. Das Spielfeld hat einen
 * eigenen Typ und auf dem Spielfeld kann ein Spielstein vorhanden sein.
 * 
 * @author Bernhof, Diedrich, Graczyk
 * 
 */
public class GameField {

	private Set<GameField> neighbors = new HashSet<GameField>();
	private int difficulty = 1;
	private int x;
	private int y;
	private GamePiece gamePiece;
	private BufferedImage img;

	/**
	 * Konstruktor ruft die initialize Methode mit den zum Typ passenden
	 * Parametern auf.
	 * 
	 * @param img
	 *            Das Bild fuer das Spielfeld
	 * @param x
	 *            x-Koordinate des Spielfeld
	 * @param y
	 *            y-Koordinate des Spielfeld
	 * @param type
	 *            Gibt an um welchen Feldtyp es sich handelt
	 * 
	 */
	public GameField(BufferedImage img, int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.img = img;
		if (type == 0) {
			this.difficulty = 1;
		} else if (type == 1) {
			this.difficulty = 2;
		} else if (type == 2) {
			this.difficulty = 3;
		} else if (type == 3) {
			this.difficulty = 999;
		}

	}

	/**
	 * @return Gibt die x-Koordinate des GameFields zurueck
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return Gibt die y-Koordinate des GameFields zurueck
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return Gibt die Schwierigkeit zurueck mit der eine Spielfigur sich ueber
	 *         das Spielfeld bewegen kann (wieviel reach ihr fuer diese Bewegung
	 *         abgezogen wird)
	 */
	public int getDifficulty() {
		return difficulty;
	}

	/**
	 * Setzt ein GamePiece auf das GameField
	 * 
	 * @param gp Spielstein der auf dieses Spielfeld gesetzt werden soll.
	 *            
	 */
	public void setPiece(GamePiece gp) {
		gamePiece = gp;
	}

	/**
	 * @return Gibt das GamePiece welches sich auf dem GameField befindet
	 *         zurueck
	 */
	public GamePiece getPiece() {
		return gamePiece;
	}

	/**
	 * Fuegt ein Nachbarfeld hinzu
	 * 
	 * @param gf Das spezifische Nachbarfeld
	 *            
	 */
	public void addNeighbor(GameField gf) {
		neighbors.add(gf);
	}

	/**
	 * @return gibt das Set aller Nachbarn zurück
	 */
	public Set<GameField> getNeighbors() {
		return neighbors;
	}
	
	/**
	 * @return Gibt das Bild des Spielfelds zurueck
	 */
	public BufferedImage getImg() {
		return img;
	}

	/**
	 * @param gf das zu vergleichende Spielfeld
	 * @return boolean Prueft ob die GameFields inhaltsgleich sind
	 */
	public boolean equals(GameField gf) {
		if ((gf.getX() == x) && gf.getY() == y) {
			return true;
		}
		return false;
	}

}
