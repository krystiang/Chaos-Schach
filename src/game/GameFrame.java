package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 * Eine Klasse die das Hauptgebilde des Spiels darstellt und auf dem alle
 * wichtigen GUI Schaltflächen enthalten sind.
 * 
 * @author Bernhof, Diedrich, Graczyk
 * 
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame {

	private JButton aufgeben, help, turn;
	private JPanel westPanel;
	private JLabel conn;
	private Board board;
	private JTextArea textArea;
	private JLabel yourTurn;

	/**
	 * Konstruktor erzeugt die Knoepfe fuer das Gamefenster und setzt die
	 * jeweiligen ActionListener
	 * 
	 * @param opt
	 *            Gibt an ob es sich um einen Server oder Client handelt.
	 */
	GameFrame(String opt) {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setSize(1280, 720);
		this.setBackground(Color.DARK_GRAY);
		this.setLayout(new BorderLayout());
		this.setResizable(false);

		westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.setPreferredSize(new Dimension(200, 720));
		aufgeben = new JButton("Aufgeben");
		aufgeben.setPreferredSize(new Dimension(200, 50));
		aufgeben.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (board != null) {
					JOptionPane.showMessageDialog(null,
							"Sie haben das Spiel aufgegeben", "Verloren",
							JOptionPane.WARNING_MESSAGE);
					board.end();
				} else
					System.exit(0);
			}
		});
		turn = new JButton("Turn");
		turn.setPreferredSize(new Dimension(200, 100));
		turn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (board != null) {
					if (board.getTurn())
						board.turn();
				}
			}
		});
		help = new JButton("Help");
		help.setPreferredSize(new Dimension(200, 100));
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop()
							.browse(new URI(
									"https://www.youtube.com/watch?v=QiVT7Rb5N7s"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		textArea = new JTextArea(20, 5);
		textArea.setEditable(false);
		JPanel southButtons = new JPanel();
		southButtons.setLayout(new BorderLayout());
		southButtons.add(turn, BorderLayout.NORTH);
		southButtons.add(help, BorderLayout.CENTER);
		southButtons.add(aufgeben, BorderLayout.SOUTH);
		yourTurn = new JLabel();
		yourTurn.setPreferredSize(new Dimension(200, 200));
		yourTurn.setOpaque(true);
		yourTurn.setHorizontalAlignment(JLabel.CENTER);
		westPanel.add(textArea, BorderLayout.NORTH);
		westPanel.add(yourTurn, BorderLayout.CENTER);
		westPanel.add(southButtons, BorderLayout.SOUTH);
		westPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(westPanel, BorderLayout.WEST);
		conn = new JLabel("waiting for connection");
		this.add(conn, BorderLayout.CENTER);
		this.setVisible(true);
		try {
			if (opt.equals("s")) {
				this.setTitle("Server");
				Thread trd = new Thread(new Server(this));
				trd.start();
			} else if (opt.equals("c")) {
				this.setTitle("Client");
				new Client(this);
				changeTurnStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fuegt dem GameFrame das Board hinzu und dem Board das GameFrame
	 * 
	 * @param board
	 * 
	 */
	public void addBoard(Board board) {
		this.board = board;
		this.remove(conn);
		this.add(this.board, BorderLayout.CENTER);
		this.setVisible(true);
		this.board.addGameFrame(this);
		this.board.setBorder(BorderFactory.createLineBorder(Color.black));
		setCursor(board.getHandCur());
	}

	/**
	 * Schreibt in die TextArea ueber welchen Spielstein die Maus gerade hovered
	 * 
	 * @param str
	 *            Alle wichtigen Eigenschaften eines Spielsteins
	 * @param bl
	 *            Angabe ob es sich um einen eigenen Spielstein handelt
	 * 
	 */
	public void setTextArea(String str, Boolean bl) {
		textArea.setText("");
		textArea.setText(str);
		if (bl) {
			textArea.append("your figure");
			textArea.setForeground(new Color(0x00, 0xC0, 0x00));
		} else {
			textArea.append("enemys figure");
			textArea.setForeground(Color.red);
		}
	}

	/**
	 * Wechselt und zeigt an wer an der Reihe ist
	 */
	public void changeTurnStatus() {
		if (board.getTurn()) {
			yourTurn.setText("Sie sind am Zug!");
			yourTurn.setBackground(Color.green);
		} else {
			yourTurn.setText("Ihr Gegner ist am Zug!");
			yourTurn.setBackground(Color.gray);
		}
	}

}
