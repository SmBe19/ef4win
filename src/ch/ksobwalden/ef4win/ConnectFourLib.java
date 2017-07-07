package ch.ksobwalden.ef4win;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility functions for connect four
 */
public class ConnectFourLib {

	public static final int SPIELFELD_HOEHE = 6;
	public static final int SPIELFELD_BREITE = 7;
	public static final int X_GEWINNT = 4;
	public static int MINMAX_TIEFE = 4;
	public static boolean MINMAX_ALPHA_BETA = true;

	private static final boolean DEBUG1 = false;
	private static final boolean DEBUG2 = false;

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

	private static void arraycopy(int[][] a1, int[][] a2){
		for (int y = 0; y < a1.length; y++) {
			for (int x = 0; x < a1[0].length; x++) {
				a2[y][x] = a1[y][x];
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Gibt das Spielfeld aus.
	 * @param out Der Stream, auf dem das Spielfeld ausgegeben werden soll
	 * @param spielfeld Das Spielfeld
	 */
	public static void printSpielfeld(PrintStream out, int[][] spielfeld){
		String[] symb = new String[]{ "   ", " X ", " O " };
		out.print(" + ");
		for (int x = 0; x < ConnectFourLib.SPIELFELD_BREITE; x++) {
			out.print(" " + x + " ");
		}
		out.println(" + ");
		for (int y = ConnectFourLib.SPIELFELD_HOEHE-1; y >= 0; y--) {
			out.print(" " + y + " ");
			for (int x = 0; x < ConnectFourLib.SPIELFELD_BREITE; x++) {
				out.print(symb[spielfeld[y][x]]);
			}
			out.print(" " + y + " ");
			out.println();
		}
		out.print(" + ");
		for (int x = 0; x < ConnectFourLib.SPIELFELD_BREITE; x++) {
			out.print(" " + x + " ");
		}
		out.println(" + ");
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static boolean gewonnenUtil(int spieler, int[][] spielfeld, int xdir, int ydir){
		int xdirabs = Math.abs(xdir);
		int ydirabs = Math.abs(ydir);
		for (int y = 0; y < SPIELFELD_HOEHE - (X_GEWINNT-1) * ydirabs; y++) {
			for (int x = 0; x < SPIELFELD_BREITE - (X_GEWINNT-1) * xdirabs; x++) {
				boolean pos = true;
				for (int i = 0; i < X_GEWINNT; i++) {
					int cx = x + xdirabs * i;
					int cy = y + ydirabs * i;
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
	 * Überprüft, ob in der gegebenen Spalte gespielt werden kann.
	 * @param spielfeld das aktuelle Spielfeld
	 * @param x die Spalte
	 * @return true, wenn gespielt werden kann
	 */
	public static boolean spielMoeglich(int[][] spielfeld, int x) {
		checkValidSpielfeldOrThrow(spielfeld);
		checkValidXYOrThrow(x, 0);
		return spielfeld[SPIELFELD_HOEHE-1][x] == 0;
	}

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

	/**
	 * Macht einen Zug in der gegebenen Spalte rückgängig.
	 * @param spielfeld das aktuelle Spielfeld
	 * @param x die Spalte, die gespielt wird
	 */
	public static void spielRueckgaengig(int[][] spielfeld, int x){
		checkValidSpielfeldOrThrow(spielfeld);
		checkValidXYOrThrow(x, 0);

		for (int y = SPIELFELD_HOEHE - 1; y >= 0; y--) {
			if (spielfeld[y][x] != 0) {
				spielfeld[y][x] = 0;
				return;
			}
		}
		throw new IllegalArgumentException("Die gegebene Spalte ist leer");
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static int spielfeldScoreUtilOnePlayer(int spieler, int[][] spielfeld, int xdir, int ydir){
		int xdirabs = Math.abs(xdir);
		int ydirabs = Math.abs(ydir);
		int score = 0;
		for (int y = 0; y < SPIELFELD_HOEHE - (X_GEWINNT-1) * ydirabs; y++) {
			for (int x = 0; x < SPIELFELD_BREITE - (X_GEWINNT-1) * xdirabs; x++) {
				int count = 0;
				boolean enemy = false;
				for (int i = 0; i < X_GEWINNT; i++) {
					int cx = x + xdirabs * i;
					int cy = y + ydirabs * i;
					if (xdir < 0) {
						cx = SPIELFELD_BREITE-1-cx;
					}
					if (ydir < 0) {
						cy = SPIELFELD_HOEHE-1-cy;
					}
					if (spielfeld[cy][cx] == spieler){
						count++;
					}
					if (spielfeld[cy][cx] == 3 - spieler){
						enemy = true;
					}
				}
				if (!enemy && count > 0){
					score += Math.pow(10, Math.pow(2, count-1));
				}
			}
		}
		return score;
	}

	private static int spielfeldScoreUtil(int spieler, int[][] spielfeld, int xdir, int ydir) {
		return spielfeldScoreUtilOnePlayer(spieler, spielfeld, xdir, ydir) - spielfeldScoreUtilOnePlayer(3 - spieler, spielfeld, xdir, ydir);
	}

	private static int spielfeldScore(int spieler, int[][] spielfeld){
		int score = spielfeldScoreUtil(spieler, spielfeld, 1, 0) + spielfeldScoreUtil(spieler, spielfeld, 0, 1)
				+ spielfeldScoreUtil(spieler, spielfeld, 1, 1) + spielfeldScoreUtil(spieler, spielfeld, -1, 1);
		return score;
	}

	private static List<Integer> history = new ArrayList<>();

	private static int minmax(int spieler, int[][] spielfeld, int spielX, int depth, int alpha, int beta){
		int val;
		spiel(spieler, spielfeld, spielX);
		if (DEBUG2) {
			history.add(spielX);
		}
		if (depth <= 0 || gewonnen(spielfeld)) {
			val = spielfeldScore(spieler, spielfeld);
		} else {
			val = MINMAX_ALPHA_BETA ? beta : Integer.MAX_VALUE;
			for (int x = 0; x < SPIELFELD_BREITE; x++) {
				if (spielfeld[SPIELFELD_HOEHE-1][x] == 0) {
					int newval = -minmax(3 - spieler, spielfeld, x, depth - 1, -val, -alpha);
					val = Math.min(val, newval);
					if (MINMAX_ALPHA_BETA && val <= alpha){
						break;
					}
				}
			}
		}
		if (DEBUG2) {
			System.err.printf("Score (Spieler %d): %s %d%n", spieler, history, val);
		}
		if (DEBUG2) {
			history.remove(history.size() - 1);
		}
		spielRueckgaengig(spielfeld, spielX);
		return val;
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
		int maxval = Integer.MIN_VALUE;
		List<Integer> maxcols = new ArrayList<>();
		int[][] spielfeldCopy = new int[SPIELFELD_HOEHE][SPIELFELD_BREITE];
		arraycopy(spielfeld, spielfeldCopy);
		for (int x = 0; x < SPIELFELD_BREITE; x++) {
			if (spielfeld[SPIELFELD_HOEHE-1][x] != 0) {
				continue;
			}
			int newval = minmax(spieler, spielfeldCopy, x, MINMAX_TIEFE, -Integer.MAX_VALUE, Integer.MAX_VALUE);
			if (newval > maxval){
				maxval = newval;
				maxcols.clear();
			}
			if (newval == maxval) {
				maxcols.add(x);
			}
		}
		int idx = (int) (Math.random() * maxcols.size());
		if(DEBUG1) {
			System.err.println("--------------------------------------------------------------");
			System.err.printf("Possible choices with value %d: %s%n", maxval, maxcols);
		}
		return maxcols.get(idx);
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
