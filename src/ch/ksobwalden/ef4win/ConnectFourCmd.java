package ch.ksobwalden.ef4win;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConnectFourCmd {

	private static void printSpielfeld(int[][] spielfeld){
		String[] symb = new String[]{ "   ", " X ", " O " };
		System.out.print("   ");
		for (int x = 0; x < ConnectFourLib.SPIELFELD_BREITE; x++) {
			System.out.print(" " + x + " ");
		}
		System.out.println();
		for (int y = ConnectFourLib.SPIELFELD_HOEHE-1; y >= 0; y--) {
			System.out.print(" " + y + " ");
			for (int x = 0; x < ConnectFourLib.SPIELFELD_BREITE; x++) {
				System.out.print(symb[spielfeld[y][x]]);
			}
			System.out.println();
		}
	}

	public static void main(String[] args){
		Scanner cin = new Scanner(System.in);
		System.out.println("Vier gewinnt");
		System.out.println("------------");
		System.out.print("Wer beginnt? (1 Mensch, 2 Computer): ");
		int spieler = cin.nextInt();
		int[][] spielfeld = new int[ConnectFourLib.SPIELFELD_HOEHE][ConnectFourLib.SPIELFELD_BREITE];

		while (true){
			if (spieler == 1) {
				printSpielfeld(spielfeld);
				do {
					try {
						System.out.print("Nächster Zug? (0 - " + (ConnectFourLib.SPIELFELD_BREITE - 1) + "): ");
						int x;
						x = cin.nextInt();
						ConnectFourLib.spiel(spieler, spielfeld, x);
						spieler = 3 - spieler;
					} catch (InputMismatchException e) {
						cin.next();
						continue;
					} catch (IllegalArgumentException e){
						continue;
					}
					break;
				} while (true);
			} else {
				ConnectFourLib.spiel(spieler, spielfeld, ConnectFourLib.computerProfiSpielzug(spielfeld));
				spieler = 3 - spieler;
			}

			if (ConnectFourLib.gewonnen(spielfeld)){
				break;
			}
		}

		printSpielfeld(spielfeld);

		System.out.println("Spieler " + (ConnectFourLib.gewonnen(1, spielfeld) ? 1 : 2) + " hat gewonnen");
	}
}
