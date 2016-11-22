package game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputListener;

/**
 * Eine Klasse die das gesamte Spiel regelt. In Ihr befinden sich alle noetigen
 * Eigenschaften und Methoden zum spielen vom Spiel.
 * 
 * 
 * @author Bernhof, Diedrich, Graczyk
 * 
 */
@SuppressWarnings("serial")
public class Board extends JPanel implements MouseInputListener, ActionListener {

	private final int FIELDHEIGHT = 30;
	private final int FIELDWIDTH = 30;
	private final int DISTANCE = 3;
	private final int FIELDCOUNTX = 23;
	private final int FIELDCOUNTY = 11;

	private int basePos;
	private int enemyBasePos;
	private String player;
	private Input input;
	private Output output;
	private boolean turn;
	private BufferedImage handImg, swordImg, feetImg, noImg;
	private Cursor handCur, swordCur, feetCur, noCur;
	private BufferedImage path, grass, forest, mountain, tmp;

	private ArrayList<GameField> gameFields = new ArrayList<GameField>();
	private HashMap<GameField, Integer> reachableGameFields = new HashMap<GameField, Integer>(),
			attackableGameFields = new HashMap<GameField, Integer>();
	private GameField clickedField;
	private Random rand = new Random(20071969);
	private GameFrame gameFrame;
	private GamePiece actGamePiece;

	/**
	 * Konstruktor des Boards fuer den Server
	 * 
	 * @param server
	 * 
	 */
	Board(Server server) {
		player = "Server";
		input = new Input(server, this);
		output = new Output(server);
		basePos = FIELDCOUNTX * FIELDCOUNTY - 2;
		enemyBasePos = 1;
		turn = true;
		init();
	}

	/**
	 * Konstruktor des Boards fuer den Client
	 * 
	 * @param client
	 * 
	 */
	Board(Client client) {
		player = "Client";
		input = new Input(client, this);
		output = new Output(client);
		basePos = 1;
		enemyBasePos = FIELDCOUNTX * FIELDCOUNTY - 2;
		turn = false;
		init();
	}

	/**
	 * Hilfsmethode welche vom Konstruktor aufgerufen wird welche den Input
	 * Thread startet, Listener added, Spielfelder und Spielfiguren erzeugt und
	 * die "Gameloop" startet
	 */
	private void init() {
		(new Thread(input)).start();
		addMouseListener(this);
		addMouseMotionListener(this);
		this.requestFocus();
		setGameFields();
		setGamePieces();
		defCursor();
	}

	/**
	 * Eine Methode welche die unterschiedlichen Cursor mit den Bildern erstellt
	 * die das Spiel benutzt
	 */
	private void defCursor() {
		ClassLoader cldr = this.getClass().getClassLoader();
		try {
			handImg = ImageIO.read(cldr.getResource("images/HandCursor.png"));
			swordImg = ImageIO.read(cldr.getResource("images/Sword.png"));
			feetImg = ImageIO.read(cldr.getResource("images/Feet.png"));
			noImg = ImageIO.read(cldr.getResource("images/NoCur.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		handCur = Toolkit.getDefaultToolkit().createCustomCursor(handImg,
				new Point(10, 0), "handCur");
		swordCur = Toolkit.getDefaultToolkit().createCustomCursor(swordImg,
				new Point(0, 0), "swordCur");
		feetCur = Toolkit.getDefaultToolkit().createCustomCursor(feetImg,
				new Point(10, 10), "feetCur");
		noCur = Toolkit.getDefaultToolkit().createCustomCursor(noImg,
				new Point(10, 10), "noCur");
	}

	/**
	 * Erzeugt die einzelnen Spielfelder mit einem zufaelligen Typen. Schafft
	 * auﬂerdem direkt die Nachbarverbindungen der Spielfelder
	 */
	private void setGameFields() {
		int height = FIELDHEIGHT;
		int width = FIELDWIDTH;
		int mx = width;
		int my = height;
		int flag = 0;
		ClassLoader cldr = this.getClass().getClassLoader();
		try {
			path = ImageIO.read(cldr.getResource("images/Path.png"));
			grass = ImageIO.read(cldr.getResource("images/Grass.png"));
			forest = ImageIO.read(cldr.getResource("images/Forest.png"));
			mountain = ImageIO.read(cldr.getResource("images/Mountain.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int j = 0; j < FIELDCOUNTX; j++) {
			for (int i = 0; i < FIELDCOUNTY; i++) {
				int chance = rand.nextInt(100);
				int fieldtype = 0;
				if (chance >= 1 && chance < 34) {
					fieldtype = 0;
					tmp = path;
				} else if (chance >= 34 && chance < 67) {
					fieldtype = 1;
					tmp = grass;
				} else if (chance >= 67 && chance < 97) {
					fieldtype = 2;
					tmp = forest;
				} else if (chance >= 97 && chance <= 100) {
					fieldtype = 3;
					tmp = mountain;
				}
				gameFields.add(new GameField(tmp, mx, my, fieldtype));
				my += height * 2;
			}
			if (flag == 0) {
				my = 2 * height;
				flag = 1;
			} else {
				my = height;
				flag = 0;
			}
			mx += (int) (width * 1.5);
		}

		for (GameField gf : gameFields) {
			if (getGameField(gf.getX(), gf.getY() + 2 * height) != null)
				gf.addNeighbor(getGameField(gf.getX(), gf.getY() + 2 * height));
			if ((getGameField((int) (gf.getX() - 1.5 * width), gf.getY()
					+ height)) != null)
				gf.addNeighbor(getGameField((int) (gf.getX() - 1.5 * width),
						gf.getY() + height));
			if (getGameField((int) (gf.getX() + 1.5 * width), gf.getY()
					+ height) != null)
				gf.addNeighbor(getGameField((int) (gf.getX() + 1.5 * width),
						gf.getY() + height));
			if (getGameField(gf.getX(), gf.getY() - 2 * height) != null)
				gf.addNeighbor(getGameField(gf.getX(), gf.getY() - 2 * height));
			if (getGameField((int) (gf.getX() - 1.5 * width), gf.getY()
					- height) != null)
				gf.addNeighbor(getGameField((int) (gf.getX() - 1.5 * width),
						gf.getY() - height));
			if (getGameField((int) (gf.getX() + 1.5 * width), gf.getY()
					- height) != null)
				gf.addNeighbor(getGameField((int) (gf.getX() + 1.5 * width),
						gf.getY() - height));
		}
	}

	/**
	 * Setzt in das obere linke und das untere rechte Eck des Spielfeldes die
	 * Basis Steine und bereits einen Soldaten
	 */
	private void setGamePieces() {
		GamePiece base1 = new GamePiece("Client", 0, "Client" == player);
		gameFields.get(1).setPiece(base1);
		GamePiece soldier1 = new GamePiece("Client", 1, "Client" == player);
		gameFields.get(2).setPiece(soldier1);

		GamePiece base2 = new GamePiece("Server", 0, "Server" == player);
		gameFields.get(FIELDCOUNTX * FIELDCOUNTY - 2).setPiece(base2);
		GamePiece soldier2 = new GamePiece("Server", 1, "Server" == player);
		gameFields.get(FIELDCOUNTX * FIELDCOUNTY - 3).setPiece(soldier2);

	}

	/**
	 * Setzt an die erste Position neben der Basis welche er findet zufaellig
	 * einen neuen Spielstein
	 */
	public void spawn() {
		resetStatus();
		GameField tmp = gameFields.get(basePos);

		for (GameField gf : tmp.getNeighbors()) {
			if (gf.getPiece() == null) {
				GamePiece temp = randomNewPiece();
				gf.setPiece(temp);
				output.create("c", gf.getX(), gf.getY(), temp.getOwner(),
						temp.getType());
				break;
			}
		}
		this.repaint();
	}

	/**
	 * Erzeugt einen neuen Spielstein an der Stelle x,y vom Typ type und der
	 * Besitzer ist player
	 * 
	 * @param x
	 *            x-Koordinate des Spielsteins
	 * @param y
	 *            y-Koordinate des Spielsteins
	 * @param player
	 *            Besitzer des Spielsteins
	 * @param type
	 *            Typ des Spielsteins
	 * 
	 */
	public void create(int x, int y, String player, int type) {
		getGameField(x, y).setPiece(
				new GamePiece(player, type, player == this.player));
		this.repaint();
	}

	/**
	 * @return Gibt einen neuen Spielstein von einem zufaelligen Typen zurueck
	 */
	private GamePiece randomNewPiece() {
		return new GamePiece(player, rand.nextInt(3) + 1, true);
	}

	/**
	 * @param x
	 *            x-Koordinate des zu findenden Spielfelds
	 * @param y
	 *            y-Koordinate des zu findenden Spielfelds
	 * @return Gibt das Spielfeld an der Position x,y zurueck
	 */
	private GameField getGameField(int x, int y) {
		for (GameField gf : gameFields) {

			if (gf.getX() == x && gf.getY() == y) {
				return gf;
			}
		}
		return null;
	}

	/**
	 * @param event
	 * @return Gibt das vom Mausevent am wenigsten entfernte Spielfeld zurueck
	 */
	private GameField nearestField(MouseEvent event) {
		GameField nearestGameField = gameFields.get(0);
		for (GameField gf : gameFields) {
			if ((Math.sqrt(Math.pow((gf.getX() - event.getX()), 2)
					+ Math.pow((gf.getY() - event.getY()), 2))) < (Math
					.sqrt(Math.pow((nearestGameField.getX() - event.getX()), 2)
							+ Math.pow(
									(nearestGameField.getY() - event.getY()), 2)))) {
				nearestGameField = gf;
			}
		}
		return nearestGameField;
	}

	/**
	 * Erstellt rekursiv die Menge von erreichbaren Spielfeldern mit Hilfe eines
	 * Startfeldes und der Bewegungsreichweite
	 * 
	 * @param gf
	 *            momentan zu ueberpruefende Gamefield
	 * @param steps
	 *            Anzahl der restlichen steps
	 */
	private void reachableGameFields(GameField gf, int steps) {
		reachableGameFields.put(gf, steps);
		for (GameField tmp : gf.getNeighbors()) {
			if (!(reachableGameFields.containsKey(tmp))
					|| reachableGameFields.get(tmp) < steps) {
				if (tmp.getPiece() == null) {
					if (steps >= tmp.getDifficulty())
						reachableGameFields(tmp, steps - tmp.getDifficulty());
				}
			}
		}
	}

	/**
	 * Erstellt rekursiv die Menge von angreifbaren Spielfeldern mit Hilfe eines
	 * Startfeldes und der Angriffsreichweite
	 * 
	 * @param gf
	 *            momentan zu ueberpruefende Gamefield
	 * @param steps
	 *            Anzahl der restlichen steps
	 */
	private void attackableGameFields(GameField gf, int reach) {
		for (GameField tmp : gf.getNeighbors()) {
			if (!(attackableGameFields.containsKey(tmp))
					|| attackableGameFields.get(tmp) <= reach) {
				if (tmp.getPiece() == null
						|| (tmp.getPiece() != null && tmp.getPiece().getOwner() == player)) {
					if (reach > 0)
						attackableGameFields(tmp, reach - 1);
				} else if (tmp.getPiece() != null
						&& tmp.getPiece().getOwner() != player) {
					if (reach > 0) {
						attackableGameFields.put(tmp, reach);
						attackableGameFields(tmp, reach - 1);
					}
				}
			}
		}
	}

	/**
	 * Spielstein an der Position xAlt, yAlt greift Spielstein an der Position
	 * xNeu, yNeu an
	 * 
	 * @param xAlt
	 *            x-Koordinate der angreifenden Figur
	 * @param yAlt
	 *            y-Koordinate der angreifenden Figur
	 * @param xNeu
	 *            x-Koordinate der anzugreifenden Figur
	 * @param yNeu
	 *            y-Koordinate der anzugreifenden Figur
	 * 
	 */
	public void attack(int xAlt, int yAlt, int xNeu, int yNeu) {
		if (getGameField(xNeu, yNeu).getPiece().getHp() <= getGameField(xAlt,
				yAlt).getPiece().getAttack()) {
			getGameField(xNeu, yNeu).setPiece(null);
		} else {
			getGameField(xNeu, yNeu).getPiece().setHp(
					getGameField(xNeu, yNeu).getPiece().getHp()
							- getGameField(xAlt, yAlt).getPiece().getAttack());
		}
		getGameField(xAlt, yAlt).getPiece().setAtk(false);
		if (gameFields.get(enemyBasePos).getPiece() == null)
			win();
		this.repaint();
	}

	/**
	 * Verschiebt einen Spielstein auf dem Spielfeld
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
	public void moveGamePiece(int xAlt, int yAlt, int xNeu, int yNeu) {
		getGameField(xNeu, yNeu).setPiece(getGameField(xAlt, yAlt).getPiece());
		getGameField(xAlt, yAlt).setPiece(null);
		getGameField(xNeu, yNeu).getPiece().setStepsLeft(0);
		this.repaint();
	}

	/**
	 * Spiel wird beendet
	 */
	public void end() {
		output.close();
		System.exit(0);
	}

	/**
	 * Gibt den Zug an den Gegner ab
	 */
	public void turn() {
		output.turn();
		turn = false;
		gameFrame.changeTurnStatus();
		resetStatus();
		this.repaint();
	}

	/**
	 * Spiel gewonnen
	 */
	public void win() {
		output.win();
		JOptionPane.showMessageDialog(null,
				"Gegnerische Basis zerstoert \nSie haben das Spiel gewonnen!",
				"Gewonnen", JOptionPane.WARNING_MESSAGE);
		System.exit(0);
	}

	/**
	 * Resettet bei allen Spielsteinen die Werte fuer die Reichweite und ob
	 * diese angriffsberechtigt sind
	 */
	private void resetStatus() {
		for (GameField gf : gameFields) {
			if (gf.getPiece() != null) {
				gf.getPiece().setStepsLeft(gf.getPiece().getSteps());
				gf.getPiece().setAtk(true);
			}
		}
	}

	/**
	 * Ruft die Methode im GameFrame auf welche anzeigt wer an der Reihe ist
	 */
	public void changeTurnStatus() {
		gameFrame.changeTurnStatus();
	}

	/**
	 * @return Gibt den HandCursor zurueck
	 */
	public Cursor getHandCur() {
		return handCur;
	}

	/**
	 * @return Gibt zurueck ob man an der Reihe ist
	 */
	public boolean getTurn() {
		return turn;
	}

	/**
	 * Setzt ob man an der Reihe ist
	 * 
	 * @param turn
	 */
	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	/**
	 * Fuegt GameFrame zum Board hinzu
	 * 
	 * @param gameFrame
	 */
	public void addGameFrame(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
	}

	/**
	 * Veraendert den Cursor je nachdem bei welchem Mausevent das Spiel gerade ist
	 * 
	 * @param event
	 */
	private void actCur(MouseEvent event) {
		GameField ngf = nearestField(event);
		if (reachableGameFields.containsKey(ngf)) {
			setCursor(feetCur);
		} else if (attackableGameFields.containsKey(ngf)) {
			setCursor(swordCur);
		} else if (ngf.getPiece() != null) {
			if (ngf.getPiece().getOwner() != player) {
				setCursor(noCur);
			} else {
				setCursor(handCur);
			}
		} else {
			setCursor(handCur);
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		GameField ngf = nearestField(event);
		if (ngf.getPiece() != null) {
			gameFrame.setTextArea(ngf.getPiece().toString(), ngf.getPiece()
					.getOwner().equals(player));
		}
		actCur(event);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (turn) {
			if (actGamePiece == null) {
				firstclick(event);
				this.repaint();
			} else {
				secondclick(event);
				clickedField = null;
				actGamePiece = null;
				this.repaint();
			}
		}
		actCur(event);
	}

	/**
	 * Bearbeitet den ersten Klick der Maus auf dem Spielfeld
	 * 
	 * @param event
	 */
	private void firstclick(MouseEvent event) {
		clickedField = nearestField(event);
		if (clickedField.getPiece() != null
				&& clickedField.getPiece().getOwner().equals(player)) {
			reachableGameFields(clickedField, clickedField.getPiece()
					.getStepsLeft());
			if (clickedField.getPiece().getAtk()) {
				attackableGameFields(clickedField, clickedField.getPiece()
						.getReach());
			}
			actGamePiece = clickedField.getPiece();
		}
	}

	/**
	 * Bearbeitet den zweiten Klick der Maus auf dem Spielfeld
	 * 
	 * @param event
	 */
	private void secondclick(MouseEvent event) {
		GameField releasedField = nearestField(event);
		if (!(clickedField.equals(releasedField))
				&& releasedField.getPiece() == null
				&& clickedField.getPiece() != null) {

			for (GameField gf : reachableGameFields.keySet()) {
				if (gf == releasedField) {
					output.move(clickedField.getX(), clickedField.getY(),
							releasedField.getX(), releasedField.getY());
					moveGamePiece(clickedField.getX(), clickedField.getY(),
							releasedField.getX(), releasedField.getY());
					// gf.getPiece().setStepsLeft(reachableGameFields.get(releasedField));
					// // wont work yet
					gf.getPiece().setStepsLeft(0);
				}
			}
		} else if (!(clickedField.equals(releasedField))
				&& releasedField.getPiece() != null
				&& clickedField.getPiece() != null) {
			for (GameField gf : attackableGameFields.keySet()) {
				if (gf == releasedField) {
					output.attack(clickedField.getX(), clickedField.getY(),
							releasedField.getX(), releasedField.getY());
					attack(clickedField.getX(), clickedField.getY(),
							releasedField.getX(), releasedField.getY());
				}
			}

		}
		attackableGameFields.clear();
		reachableGameFields.clear();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		actCur(event);
	}

	@Override
	public void mouseExited(MouseEvent event) {
		actCur(event);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int height = FIELDHEIGHT - DISTANCE + 1;
		int width = FIELDWIDTH - DISTANCE + 1;
		for (GameField gf : gameFields) {
			int ax = (gf.getX() + width); // rechter Punkt
			int ay = (gf.getY());
			int bx = (int) (gf.getX() + (width * 0.5)); // unterer rechter Punkt
			int by = (gf.getY() + height);
			int cx = (int) (gf.getX() - (width * 0.5)); // unterer linker Punkt
			int cy = (gf.getY() + height);
			int dx = (gf.getX() - width); // linker Punkt
			int dy = (gf.getY());
			int ex = (int) (gf.getX() - (width * 0.5)); // oberer linker Punkt
			int ey = (gf.getY() - height);
			int fx = (int) (gf.getX() + (width * 0.5)); // oberer rechter Punkt
			int fy = (gf.getY() - height);

			Polygon polygon = new Polygon();

			polygon.addPoint(ax, ay);
			polygon.addPoint(bx, by);
			polygon.addPoint(cx, cy);
			polygon.addPoint(dx, dy);
			polygon.addPoint(ex, ey);
			polygon.addPoint(fx, fy);

			if (this.reachableGameFields.containsKey(gf)) {
				g2.setColor(Color.magenta);
				g2.fillPolygon(polygon);
				g2.drawPolygon(polygon);
			}
			if (this.attackableGameFields.containsKey(gf)) {
				g2.setColor(Color.red);
				g2.fillPolygon(polygon);
				g2.drawPolygon(polygon);
			}

			g2.drawImage(gf.getImg(), gf.getX() - gf.getImg().getWidth() / 2,
					gf.getY() - gf.getImg().getHeight() / 2, null);

		}

		for (GameField gf : gameFields) {
			if (gf.getPiece() != null) {

				g2.drawImage(gf.getPiece().getImg(), gf.getX()
						- gf.getPiece().getImg().getWidth() / 2, gf.getY()
						- gf.getPiece().getImg().getHeight() / 2, null);

				int length = gf.getPiece().getImg().getWidth() - 4;
				int life = gf.getPiece().getHp() * length
						/ gf.getPiece().getMaxHp();
				int nolife = length - life;

				g2.setColor(Color.red);
				g2.fillRect(life + 2 + gf.getX()
						- gf.getPiece().getImg().getWidth() / 2, gf.getY() - 2
						- gf.getPiece().getImg().getHeight() / 2, nolife, 4);

				g2.setColor(Color.green);
				g2.fillRect(gf.getX() + 2 - gf.getPiece().getImg().getWidth()
						/ 2, gf.getY() - 2 - gf.getPiece().getImg().getHeight()
						/ 2, life, 4);
			}
		}
	}

}
