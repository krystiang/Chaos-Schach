package game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 
 * Ein Spielstein die auf dem Board auf den Feldern vorhanden sein kann. Dieser
 * Spielstein hat verschiedene Eigenschaften die für das spielen des Spiel von
 * noeten sind.
 * 
 * 
 * @author Bernhof, Diedrich, Graczyk
 * 
 */
public class GamePiece {

	private final static String newline = "\n";

	private String owner;
	private int type;
	private String name;
	private int steps;
	private int stepsLeft;
	private int hp;
	private int maxHp;
	private int attack;
	private int reach;
	private Boolean atk = true;
	private BufferedImage img;

	/**
	 * Konstruktor ruft die Hilfsmethode initialize mit den jeweils fuer den
	 * Spielsteintypen benoetigten Parametern auf
	 * 
	 * @param owner
	 *            Der Besitzer dieses Spielsteins
	 * @param type
	 *            Die Art des Spielsteins
	 * @param owned
	 *            eine Variable die anzeigt ob der Spielstein zu einem selber
	 *            gehoert oder zum Gegner
	 * 
	 */
	public GamePiece(String owner, int type, boolean owned) {
		this.owner = owner;
		this.type = type;
		if (owned) {
			switch (type) {
			case 0:
				initialize(0, 20, 1, 3, "Base", "images/CastleBlue.png");
				break;
			case 1:
				initialize(4, 10, 3, 1, "Soldier", "images/KnightBlue.png");
				break;
			case 2:
				initialize(5, 5, 3, 3, "Archer", "images/ArcherBlue.png");
				break;
			case 3:
				initialize(10, 8, 3, 1, "Horse", "images/HorseBlue.png");
				break;
			default:
				break;
			}
		}
		if (!owned) {
			switch (type) {
			case 0:
				initialize(0, 20, 1, 3, "Base", "images/CastleRed.png");
				break;
			case 1:
				initialize(4, 10, 3, 1, "Soldier", "images/KnightRed.png");
				break;
			case 2:
				initialize(5, 5, 3, 3, "Archer", "images/ArcherRed.png");
				break;
			case 3:
				initialize(10, 8, 3, 1, "Horse", "images/HorseRed.png");
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 
	 * Wird vom Konstruktor aufgerufen und setzt die Paramter fuer einen
	 * bestimmten Spielsteintypen
	 * 
	 * @param steps
	 *            Die Reichweite wie weit der Spielstein ziehen kann
	 * @param hp
	 *            die maximalen Lebenspunkte des Spielsteins
	 * @param attack
	 *            Die Angriffskraft des Spielsteins
	 * @param reach
	 *            Die Reichweite des Angriffs eines Spielsteins
	 * @param name
	 *            Bezeichnung des Spielsteins
	 * @param url
	 *            die URL des Bildes für den Spielstein
	 */
	private void initialize(int steps, int hp, int attack, int reach,
			String name, String url) {
		this.steps = steps;
		this.stepsLeft = steps;
		this.hp = hp;
		this.maxHp = hp;
		this.attack = attack;
		this.reach = reach;
		this.name = name;
		ClassLoader cldr = this.getClass().getClassLoader();
		try {
			img = ImageIO.read(cldr.getResource(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Gibt die Anzahl der Schritte zurueck die der Spielstein
	 *         zuruecklegen kann
	 */
	public int getSteps() {
		return steps;
	}

	/**
	 * @return Gibt den Besitzer des Spielsteins zurueck
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Setzt den Besitzer des Spielsteins
	 * 
	 * @param owner
	 * 
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return Gibt die Angriffsstaerke des Spielsteisn zurueck
	 */
	public int getAttack() {
		return attack;
	}

	/**
	 * Setzt die Angriffstaerke des Spielsteins
	 * 
	 * @param attack
	 * 
	 */
	public void setAttack(int attack) {
		this.attack = attack;
	}

	/**
	 * @return Gibt die Anzahl der Lebenspunkte des Spielsteins zurueck
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * Setzt die Anzahl der Lebenspunkte des Spielsteins
	 * 
	 * @param hp
	 * 
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}

	/**
	 * @return Gibt den Typen des Spielsteins zurueck
	 */
	public int getType() {
		return type;
	}

	/**
	 * Setzt den Typen des Spielsteins
	 * 
	 * @param type2
	 * 
	 */
	public void setType(int type2) {
		this.type = type2;
	}

	/**
	 * @return Gibt die Anzahl der noch uebrigen Schritte des Spielsteins
	 *         zurueck
	 */
	public int getStepsLeft() {
		return stepsLeft;
	}

	/**
	 * Setzt die uebrigen Schritte des Spielsteins
	 * 
	 * @param stepsLeft
	 * 
	 */
	public void setStepsLeft(int stepsLeft) {
		this.stepsLeft = stepsLeft;
	}

	/**
	 * @return Gibt die Angriffsreichweite des Spielsteins zurueck
	 */
	public int getReach() {
		return reach;
	}

	/**
	 * Setzt die Angriffsreichweite des Spielsteins
	 * 
	 * @param reach
	 * 
	 */
	public void setReach(int reach) {
		this.reach = reach;
	}

	/**
	 * @return Gibt zurueck ob der Spielstein angreifen kann oder nicht
	 */
	public Boolean getAtk() {
		return atk;
	}

	/**
	 * Setzt per boolean ob der Spielstein angriffsberechtigt ist oder nicht
	 * 
	 * @param atk
	 * 
	 */
	public void setAtk(Boolean atk) {
		this.atk = atk;
	}

	/**
	 * @return Gibt die maximale Hp des Spielsteins zurueck
	 */
	public int getMaxHp() {
		return maxHp;
	}

	/**
	 * @return Gibt das Bild zurueck was fuer den Spielstein benutzt wird
	 */
	public BufferedImage getImg() {
		return img;
	}

	public String toString() {
		return "Type: " + name + newline + "Attack: " + getAttack() + newline
				+ "Reach: " + reach + newline + "HP: " + getHp() + newline
				+ "StepsLeft: " + getStepsLeft() + newline + "Steps: "
				+ getSteps() + newline;

	}
}
