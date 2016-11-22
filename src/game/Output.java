package game;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 
 * Eine Klasse welche die Schnittstelle zwischen den Spielern darstellt. Hier
 * werden die Outputstreams angesteuert und verschiedene Moeglichkeiten
 * uebersendet
 * 
 * @author Bernhof, Diedrich, Graczyk
 * 
 */
public class Output {
	private DataOutputStream dout;

	/**
	 * Konstruktor fuer den Server
	 * 
	 * @param server
	 * 
	 */
	Output(Server server) {
		dout = server.getDos();

	}

	/**
	 * Konstruktor fuer den Client
	 * 
	 * @param client
	 * 
	 */
	Output(Client client) {
		dout = client.getDos();
	}

	/**
	 * Schreibt das Symbol fuer den Rundenwechsel in den Output Stream
	 */
	public void turn() {
		try {
			dout.writeUTF("t");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Schreibt den String fuer das Bewegen einer Spielfigur in den Output
	 * Stream
	 * 
	 * @param xAlt
	 *            x-Koordinate vom alten Standpunkt der zu ziehenden Figur
	 * @param yAlt
	 *            y-Koordinate vom alten Standpunkt der zu ziehenden Figur
	 * @param xNeu
	 *            x-Koordinate vom neuen Standpunkt der zu ziehenden Figur
	 * @param yNeu
	 *            y-Koordinate vom neuen Standpunkt der zu ziehenden Figur
	 * 
	 */
	public void move(int xAlt, int yAlt, int xNeu, int yNeu) {
		try {
			dout.writeUTF("m," + xAlt + "," + yAlt + "," + xNeu + "," + yNeu);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Schreibt das Symbol zum Schliessen des Spiels in den Output Stream
	 */
	public void close() {
		try {
			dout.writeUTF("b");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Schreibt den String fuer das Erstellen einer neuen Spielfigur durch die
	 * Basis in den Output Stream
	 * 
	 * @param string
	 * @param x
	 *            x-Koordinate der zu erstellenden Figur
	 * @param y
	 *            y-Koordinate der zu erstellenden Figur
	 * @param name
	 *            Name vom Owner der Figur
	 * @param type
	 *            Typ der Figur
	 * 
	 */
	public void create(String string, int x, int y, String name, int type) {
		try {
			dout.writeUTF(string + "," + x + "," + y + "," + name + "," + type);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Schreibt den String fuer den Angriff einer Spielfigur auf eine Andere in
	 * den Output Stream
	 * 
	 * @param xAlt x-Koordinate der angreifenden Figur
	 * @param yAlt y-Koordinate der angreifenden Figur
	 * @param xNeu x-Koordinate der anzugreifenden Figur
	 * @param yNeu y-Koordinate der anzugreifenden Figur
	 * 
	 */
	public void attack(int xAlt, int yAlt, int xNeu, int yNeu) {
		try {
			dout.writeUTF("a," + xAlt + "," + yAlt + "," + xNeu + "," + yNeu);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Schreibt das Symbol fuer ein gewonnenes Spiel in den Output Stream
	 */
	public void win() {
		try {
			dout.writeUTF("w");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
