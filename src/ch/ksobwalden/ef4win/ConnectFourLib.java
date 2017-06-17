package ch.ksobwalden.ef4win;

/**
 * Utility functions for connect four
 */
public class ConnectFourLib {

	public static final int SPIELFELD_HOEHE = 6;
	public static final int SPIELFELD_BREITE = 7;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static void checkValidSpielfeldOrThrow(int[][] spielfeld){
		if (spielfeld == null){
			throw new IllegalArgumentException("Spielfeld darf nicht null sein");
		}
		if (spielfeld.length != SPIELFELD_HOEHE) {
			throw new IllegalArgumentException("Ungültige Spielfeld Höhe " + spielfeld.length + " (sollte " + SPIELFELD_HOEHE + " hoch sein)");
		}
		if (spielfeld[0].length != SPIELFELD_BREITE) {
			throw new IllegalArgumentException("Ungültige Spielfeld Breite " + spielfeld.length + " (sollte " + SPIELFELD_BREITE + " breit sein)");
		}
		for (int y = 0; y < SPIELFELD_HOEHE; y++) {
			for (int x = 0; x < SPIELFELD_BREITE; x++) {
				if (spielfeld[y][x] < 0 || spielfeld[y][x] > 2) {
					throw new IllegalArgumentException("Ungültiger Spielfeld inhalt bei spielfeld[" + y + "][" + x + "]: " + spielfeld[y][x]);
				}
			}
		}
	}

	private static void checkValidSpielerOrThrow(int spieler){
		if (spieler != 1 && spieler != 2){
			throw new IllegalArgumentException("Ungültiger Spieler: " + spieler);
		}
	}

	private static void checkValidXYOrThrow(int x, int y){
		if (x < 0 || x >= SPIELFELD_BREITE) {
			throw new IllegalArgumentException("Ungültige X Position: " + x);
		}
		if (y < 0 || y >= SPIELFELD_HOEHE) {
			throw new IllegalArgumentException("Ungültige Y Position: " + y);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static boolean gewonnenUtil(int spieler, int[][] spielfeld, int xdir, int ydir){
		for (int y = 0; y < SPIELFELD_HOEHE - 4 * Math.abs(ydir); y++) {
			for (int x = 0; x < SPIELFELD_BREITE - 4 * Math.abs(xdir); x++) {
				boolean pos = true;
				for (int i = 0; i < 4; i++) {
					int cx = x + Math.abs(xdir) * i;
					int cy = y + Math.abs(ydir) * i;
					if (xdir < 0) {
						cx = SPIELFELD_BREITE-1-cx;
					}
					if (ydir < 0) {
						cy = SPIELFELD_HOEHE-1-cy;
					}
					if(spielfeld[cy][cx] != spieler){
						pos = false;
					}
				}
				if (pos){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Überprüft, ob im gegebenen Spielfeld der gegebene Spieler gewonnen hat.
	 * @param spieler der Spieler, der überprüft werden soll (1 oder 2)
	 * @param spielfeld das Spielfeld, das überprüft werden soll
	 * @return true genau dann, wenn der Spieler gewonnen hat
	 */
	public static boolean gewonnen(int spieler, int[][] spielfeld){
		checkValidSpielfeldOrThrow(spielfeld);
		checkValidSpielerOrThrow(spieler);
		return gewonnenUtil(spieler, spielfeld, 1, 0) || gewonnenUtil(spieler, spielfeld, 0, 1)
				|| gewonnenUtil(spieler, spielfeld, 1, 1) || gewonnenUtil(spieler, spielfeld, -1, 1);
	}

	/**
	 * Überprüft, ob einer der Spieler gewonnen hat.
	 * @param spielfeld das aktuelle Spielfeld
	 * @return true genau dann, wenn einee der beiden Spieler gewonnen hat
	 */
	public static boolean gewonnen(int[][] spielfeld){
		return gewonnen(1, spielfeld) || gewonnen(2, spielfeld);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Spielt die gegebene Spalte.
	 * @param spieler der Spieler, der spielt
	 * @param spielfeld das aktuelle Spielfeld
	 * @param x die Spalte, die gespielt wird
	 */
	public static void spiel(int spieler, int[][] spielfeld, int x){
		checkValidSpielfeldOrThrow(spielfeld);
		checkValidSpielerOrThrow(spieler);
		checkValidXYOrThrow(x, 0);

		for (int y = 0; y < SPIELFELD_HOEHE; y++) {
			if(spielfeld[y][x] == 0) {
				spielfeld[y][x] = spieler;
				return;
			}
		}
		throw new IllegalArgumentException("Die gegebene Spalte ist bereits voll");
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected static int spielfeldScore(int spieler, int[][] spielfeld){
		// TODO: implement
		return 0;
	}

	/**
	 * Berechnet den besten Zug für den gegebenen Spieler und das gegebene Spielfeld.
	 * @param spieler der zu spielende Spieler
	 * @param spielfeld aktuelles Spielfeld
	 * @return die Spalte, die gespielt werden soll
	 */
	// http://www.mathematik.uni-muenchen.de/~spielth/artikel/VierGewinnt.pdf
	public static int computerProfiSpielzug(int spieler, int[][] spielfeld){
		checkValidSpielfeldOrThrow(spielfeld);
		checkValidSpielerOrThrow(spieler);
		// TODO: implement
		return (int) (Math.random() * SPIELFELD_BREITE);
	}

	/**
	 * Berechnet den besten Zug für den Computer Spieler (2) für das gegebene Spielfeld.
	 * @param spielfeld aktuelles Spielfeld
	 * @return die Spalte, die gespielt werden soll
	 */
	public static int computerProfiSpielzug(int[][] spielfeld){
		return computerProfiSpielzug(2, spielfeld);
	}

}
