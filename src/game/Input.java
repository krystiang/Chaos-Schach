package game;

import java.io.DataInputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * 
 * Eine Klasse welche die einkommenden Befehle des anderen Spielers über den
 * Output verarbeiten und auf dem Board die passenden Methoden aufruft
 * 
 * @author Bernhof, Diedrich, Graczyk
 * 
 */
public class Input implements Runnable {
	private DataInputStream din;
	private Board board;
	private Boolean end = false;

	/**
	 * Konstruktor fuer Server-Input
	 * 
	 * @param server
	 * @param board
	 * 
	 */
	Input(Server server, Board board) {
		din = server.getDis();
		this.board = board;

	}

	/**
	 * Konstruktor fuer Client-Input
	 * 
	 * @param client
	 * @param board
	 * 
	 */
	Input(Client client, Board board) {
		din = client.getDis();
		this.board = board;
	}

	/**
	 * Laeuft so lange bis das Spiel beendet ist und liest den Input Stream aus
	 * und ruft auf diesen interprete() auf
	 */
	@Override
	public void run() {
		while (!end) {
			try {
				interprete(din.readUTF());

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Verbindung zum Gegner unterbrochen", "",
						JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
		}
	}

	/**
	 * Interpretiert den uebergeben String je nachdem ob es sich um einen
	 * Rundenwechsel eine Spielfigurbewegung einen Angriff oder sonst eine
	 * Handlung handelt, welche Client und Server betrifft, und ruft die
	 * entsprechenden Methoden auf
	 * 
	 * @param str der Befehlsstring der verarbeitet und interpretiert werden soll.
	 * 
	 */
	private void interprete(String str) {
		if (str.matches("t")) {
			board.setTurn(true);
			board.changeTurnStatus();
			board.spawn();
		}
		if (str.matches("m.*")) {
			board.moveGamePiece(Integer.parseInt(str.split(",")[1]),
					Integer.parseInt(str.split(",")[2]),
					Integer.parseInt(str.split(",")[3]),
					Integer.parseInt(str.split(",")[4]));
		}
		if (str.matches("b.*")) {
			JOptionPane.showMessageDialog(null,
					"Ihr Gegner hat das Spiel aufgegeben", "Gewonnen",
					JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		if (str.matches("c.*")) {
			board.create(Integer.parseInt(str.split(",")[1]),
					Integer.parseInt(str.split(",")[2]), str.split(",")[3],
					Integer.parseInt(str.split(",")[4]));
		}
		if (str.matches("a.*")) {
			board.attack(Integer.parseInt(str.split(",")[1]),
					Integer.parseInt(str.split(",")[2]),
					Integer.parseInt(str.split(",")[3]),
					Integer.parseInt(str.split(",")[4]));
		}
		if (str.matches("w.*")) {
			JOptionPane.showMessageDialog(null,
					"Ihre Basis wurde zerstört \nSie haben verloren",
					"Verloren", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	}

}
