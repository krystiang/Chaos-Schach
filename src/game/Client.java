package game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

/**
 * Eine Klasse um die Verbindung zwischen zwei Spielern herzustellen.
 * 
 * @author Bernhof, Diedrich, Graczyk
 * 
 */
public class Client {

	private Socket socket;
	private DataOutputStream dout;
	private DataInputStream din;
	private String ip;
	private int port;

	/**
	 * Konstruktor, der versucht eine Verbindung zu einzugebener IP herzustellen
	 * um dann In- und Output Streams zu initializieren und dann das Board dem
	 * Frame hinzufuegt.
	 * 
	 * @param frame
	 * 
	 */
	Client(GameFrame frame) {
		ip = JOptionPane.showInputDialog("Geben sie die ip ein:", "127.0.0.1");
		String str = JOptionPane.showInputDialog("Geben sie den port ein:",
				"7777");
		try {
			port = Integer.parseInt(str);
			socket = new Socket(ip, port);
			dout = new DataOutputStream(socket.getOutputStream());
			din = new DataInputStream(socket.getInputStream());
			frame.addBoard(new Board(this));
		} catch (ConnectException e) {
			JOptionPane.showMessageDialog(null,
					"Keinen möglichen Server gefunden", "Verbindungsfehler",
					JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Ungueltige Eingabe!\nConnect zum Spiel abgebrochen!",
					"Verbindungsfehler", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
					"Ungueltige Eingabe!\nConnect zum Spiel abgebrochen!",
					"Verbindungsfehler", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}

	}

	/**
	 * @return Gibt den Output Stream zurueck
	 */
	public DataOutputStream getDos() {
		return dout;
	}

	/**
	 * @return Gibt den Input Stream zurueck
	 */
	public DataInputStream getDis() {
		return din;
	}

}
