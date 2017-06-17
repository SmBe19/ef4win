package ch.ksobwalden.ef4win;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConnectFourCmd {

	public static void main(String[] args){
		Scanner cin = new Scanner(System.in);
		System.out.println("Vier gewinnt");
		System.out.println("------------");
		System.out.print("Wer beginnt? (1 Mensch, 2 Computer): ");
		int spieler = cin.nextInt();
		if (spieler != 1 && spieler != 2){
			System.out.println("Ungültiger Spieler");
			return;
		}
		int[][] spielfeld = new int[ConnectFourLib.SPIELFELD_HOEHE][ConnectFourLib.SPIELFELD_BREITE];

		/*
		spielfeld[0][0] = 1;
		spielfeld[0][1] = 1;
		spielfeld[0][3] = 1;
		spielfeld[0][5] = 2;
		spielfeld[1][5] = 2;
		spielfeld[2][5] = 2;
		*/

		while (true){
			if (spieler == 1) {
				ConnectFourLib.printSpielfeld(System.out, spielfeld);
				do {
					try {
						System.out.printf("Nächster Zug? (0 - %d): ", ConnectFourLib.SPIELFELD_BREITE - 1);
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

		ConnectFourLib.printSpielfeld(System.out, spielfeld);

		System.out.printf("Spieler %d hat gewonnen.%n", ConnectFourLib.gewonnen(1, spielfeld) ? 1 : 2);
	}
}
