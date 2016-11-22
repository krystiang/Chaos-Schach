package game;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;

import javax.swing.JOptionPane;

/**
 * Eine Klasse um die Verbindung zwischen zwei Spielern herzustellen.
 * 
 * @author Bernhof, Diedrich, Graczyk
 * 
 */
public class Server implements Runnable {

	private ServerSocket serverSocket;
	private Socket socket;
	private DataOutputStream dout;
	private DataInputStream din;
	private GameFrame frame;
	private int port;

	/**
	 * Erstellt einen Server mit einem GameFrame
	 * 
	 * @param frame
	 * 
	 */
	Server(GameFrame frame) {
		this.frame = frame;
	}

	@Override
	public void run() {
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));
			String ip = in.readLine();
			String str = JOptionPane.showInputDialog("Ihre IP = " + ip
					+ "\nGeben sie den port ein:", "7777");
			port = Integer.parseInt(str);
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
			dout = new DataOutputStream(socket.getOutputStream());
			din = new DataInputStream(socket.getInputStream());
			this.frame.addBoard(new Board(this));
			frame.changeTurnStatus();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
					"Ungueltiger Port!\nHosten vom Spiel abgebrochen!",
					"Abbruch", JOptionPane.WARNING_MESSAGE);
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
