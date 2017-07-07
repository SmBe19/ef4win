package ch.ksobwalden.ef4win;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Random;

import static org.junit.Assert.*;


public class ConnectFourLibTest{

	protected int[][] createSpielfeld(){
		return new int[ConnectFourLib.SPIELFELD_HOEHE][ConnectFourLib.SPIELFELD_BREITE];
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	public void test_spiel_default(){
		int[][] ts = createSpielfeld();
		int[][] ss = createSpielfeld();

		assertArrayEquals(ss, ts);

		ss[0][3] = 1;
		ConnectFourLib.spiel(1, ts, 3);
		assertArrayEquals(ss, ts);

		ss[0][2] = 2;
		ConnectFourLib.spiel(2, ts, 2);
		assertArrayEquals(ss, ts);

		ss[1][3] = 1;
		ConnectFourLib.spiel(1, ts, 3);
		assertArrayEquals(ss, ts);

		ss[0][6] = 2;
		ConnectFourLib.spiel(2, ts, 6);
		assertArrayEquals(ss, ts);

		ss[0][0] = 1;
		ConnectFourLib.spiel(1, ts, 0);
		assertArrayEquals(ss, ts);

		ss[2][3] = 2;
		ConnectFourLib.spiel(2, ts, 3);
		assertArrayEquals(ss, ts);

		ss[3][3] = 1;
		ConnectFourLib.spiel(1, ts, 3);
		assertArrayEquals(ss, ts);
	}

	@Test (expected = IllegalArgumentException.class)
	public void test_spiel_exception_left(){
		int[][] ts = createSpielfeld();
		ConnectFourLib.spiel(1, ts, -1);
	}

	@Test (expected = IllegalArgumentException.class)
	public void test_spiel_exception_right(){
		int[][] ts = createSpielfeld();
		ConnectFourLib.spiel(1, ts, 7);
	}

	@Test (expected = IllegalArgumentException.class)
	public void test_spiel_exception_top(){
		int[][] ts = createSpielfeld();
		for(int i = 0; i < 7; i++) {
			ConnectFourLib.spiel(1, ts, 3);
		}
	}

	@Test (expected = IllegalArgumentException.class)
	public void test_spiel_exception_spieler0(){
		int[][] ts = createSpielfeld();
		ConnectFourLib.spiel(0, ts, 3);
	}

	@Test (expected = IllegalArgumentException.class)
	public void test_spiel_exception_spieler3(){
		int[][] ts = createSpielfeld();
		ConnectFourLib.spiel(3, ts, 3);
	}

	@Test (expected = IllegalArgumentException.class)
	public void test_spiel_exception_spielfeld_breite1(){
		int[][] ts = new int[ConnectFourLib.SPIELFELD_HOEHE][ConnectFourLib.SPIELFELD_BREITE-1];
		ConnectFourLib.spiel(2, ts, 3);
	}

	@Test (expected = IllegalArgumentException.class)
	public void test_spiel_exception_spielfeld_breite2(){
		int[][] ts = new int[ConnectFourLib.SPIELFELD_HOEHE][ConnectFourLib.SPIELFELD_BREITE+1];
		ConnectFourLib.spiel(2, ts, 3);
	}

	@Test (expected = IllegalArgumentException.class)
	public void test_spiel_exception_spielfeld_hoehe1(){
		int[][] ts = new int[ConnectFourLib.SPIELFELD_HOEHE-1][ConnectFourLib.SPIELFELD_BREITE];
		ConnectFourLib.spiel(2, ts, 3);
	}

	@Test (expected = IllegalArgumentException.class)
	public void test_spiel_exception_spielfeld_hoehe2(){
		int[][] ts = new int[ConnectFourLib.SPIELFELD_HOEHE+1][ConnectFourLib.SPIELFELD_BREITE];
		ConnectFourLib.spiel(2, ts, 3);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	public void test_gewonnen_leer(){
		int[][] ts = createSpielfeld();
		assertFalse(ConnectFourLib.gewonnen(ts));
		assertFalse(ConnectFourLib.gewonnen(1, ts));
		assertFalse(ConnectFourLib.gewonnen(2, ts));
	}

	@Test
	public void test_gewonnen_horizontal_oneplayer(){
		int[][] ts = createSpielfeld();
		for(int i = 2; i < 5; i++){
			ConnectFourLib.spiel(1, ts, i);
			assertFalse(ConnectFourLib.gewonnen(ts));
			assertFalse(ConnectFourLib.gewonnen(1, ts));
			assertFalse(ConnectFourLib.gewonnen(2, ts));
		}
		ConnectFourLib.spiel(1, ts, 5);
		assertTrue(ConnectFourLib.gewonnen(ts));
		assertTrue(ConnectFourLib.gewonnen(1, ts));
		assertFalse(ConnectFourLib.gewonnen(2, ts));
	}

	@Test
	public void test_gewonnen_horizontal_twoplayer1(){
		int[][] ts = createSpielfeld();
		for(int i = 2; i < 5; i++){
			ConnectFourLib.spiel(1, ts, i);
			ConnectFourLib.spiel(2, ts, i);
			assertFalse(ConnectFourLib.gewonnen(ts));
			assertFalse(ConnectFourLib.gewonnen(1, ts));
			assertFalse(ConnectFourLib.gewonnen(2, ts));
		}
		ConnectFourLib.spiel(1, ts, 5);
		assertTrue(ConnectFourLib.gewonnen(ts));
		assertTrue(ConnectFourLib.gewonnen(1, ts));
		assertFalse(ConnectFourLib.gewonnen(2, ts));

		ConnectFourLib.spiel(2, ts, 5);
		assertTrue(ConnectFourLib.gewonnen(ts));
		assertTrue(ConnectFourLib.gewonnen(1, ts));
		assertTrue(ConnectFourLib.gewonnen(2, ts));
	}

	@Test
	public void test_gewonnen_horizontal_twoplayer2(){
		int[][] ts = createSpielfeld();
		for(int i = 2; i < 5; i++){
			ConnectFourLib.spiel(1, ts, i);
			ConnectFourLib.spiel(2, ts, i);
			assertFalse(ConnectFourLib.gewonnen(ts));
			assertFalse(ConnectFourLib.gewonnen(1, ts));
			assertFalse(ConnectFourLib.gewonnen(2, ts));
		}

		ConnectFourLib.spiel(2, ts, 5);
		assertFalse(ConnectFourLib.gewonnen(ts));
		assertFalse(ConnectFourLib.gewonnen(1, ts));
		assertFalse(ConnectFourLib.gewonnen(2, ts));

		ConnectFourLib.spiel(1, ts, 5);
		assertFalse(ConnectFourLib.gewonnen(ts));
		assertFalse(ConnectFourLib.gewonnen(1, ts));
		assertFalse(ConnectFourLib.gewonnen(2, ts));
	}

	@Test
	public void test_gewonnen_vertikal_oneplayer(){
		int[][] ts = createSpielfeld();
		for(int i = 0; i < 3; i++){
			ConnectFourLib.spiel(1, ts, 2);
			assertFalse(ConnectFourLib.gewonnen(ts));
			assertFalse(ConnectFourLib.gewonnen(1, ts));
			assertFalse(ConnectFourLib.gewonnen(2, ts));
		}

		ConnectFourLib.spiel(1, ts, 2);
		assertTrue(ConnectFourLib.gewonnen(ts));
		assertTrue(ConnectFourLib.gewonnen(1, ts));
		assertFalse(ConnectFourLib.gewonnen(2, ts));
	}

	@Test
	public void test_gewonnen_vertikal_twoplayer1(){
		int[][] ts = createSpielfeld();
		for(int i = 0; i < 3; i++){
			ConnectFourLib.spiel(1, ts, 2);
			ConnectFourLib.spiel(2, ts, 3);
			assertFalse(ConnectFourLib.gewonnen(ts));
			assertFalse(ConnectFourLib.gewonnen(1, ts));
			assertFalse(ConnectFourLib.gewonnen(2, ts));
		}

		ConnectFourLib.spiel(1, ts, 2);
		assertTrue(ConnectFourLib.gewonnen(ts));
		assertTrue(ConnectFourLib.gewonnen(1, ts));
		assertFalse(ConnectFourLib.gewonnen(2, ts));

		ConnectFourLib.spiel(2, ts, 3);
		assertTrue(ConnectFourLib.gewonnen(ts));
		assertTrue(ConnectFourLib.gewonnen(1, ts));
		assertTrue(ConnectFourLib.gewonnen(2, ts));
	}

	@Test
	public void test_gewonnen_vertikal_twoplayer2(){
		int[][] ts = createSpielfeld();
		for(int i = 0; i < 3; i++){
			ConnectFourLib.spiel(1, ts, i % 2 == 0 ? 2 : 3);
			ConnectFourLib.spiel(2, ts, i % 2 == 0 ? 3 : 2);
			assertFalse(ConnectFourLib.gewonnen(ts));
			assertFalse(ConnectFourLib.gewonnen(1, ts));
			assertFalse(ConnectFourLib.gewonnen(2, ts));
		}

		ConnectFourLib.spiel(1, ts, 2);
		assertFalse(ConnectFourLib.gewonnen(ts));
		assertFalse(ConnectFourLib.gewonnen(1, ts));
		assertFalse(ConnectFourLib.gewonnen(2, ts));

		ConnectFourLib.spiel(2, ts, 3);
		assertFalse(ConnectFourLib.gewonnen(ts));
		assertFalse(ConnectFourLib.gewonnen(1, ts));
		assertFalse(ConnectFourLib.gewonnen(2, ts));
	}

	@Test
	public void test_gewonnen_diagonal_up(){
		int[][] ts = createSpielfeld();
		for(int i = 1; i < 4; i++){
			for(int j = 0; j < i; j++) {
				ConnectFourLib.spiel(2, ts, i);
				assertFalse(ConnectFourLib.gewonnen(ts));
				assertFalse(ConnectFourLib.gewonnen(1, ts));
				assertFalse(ConnectFourLib.gewonnen(2, ts));
			}
		}
		for(int i = 0; i < 3; i++){
			ConnectFourLib.spiel(1, ts, i);
			assertFalse(ConnectFourLib.gewonnen(ts));
			assertFalse(ConnectFourLib.gewonnen(1, ts));
			assertFalse(ConnectFourLib.gewonnen(2, ts));
		}

		ConnectFourLib.spiel(1, ts, 3);
		assertTrue(ConnectFourLib.gewonnen(ts));
		assertTrue(ConnectFourLib.gewonnen(1, ts));
		assertFalse(ConnectFourLib.gewonnen(2, ts));
	}

	@Test
	public void test_gewonnen_diagonal_down(){
		int[][] ts = createSpielfeld();
		for(int i = 1; i < 4; i++){
			for(int j = 0; j < i; j++) {
				ConnectFourLib.spiel(2, ts, 5-i);
				assertFalse(ConnectFourLib.gewonnen(ts));
				assertFalse(ConnectFourLib.gewonnen(1, ts));
				assertFalse(ConnectFourLib.gewonnen(2, ts));
			}
		}
		for(int i = 0; i < 3; i++){
			ConnectFourLib.spiel(1, ts, 5-i);
			assertFalse(ConnectFourLib.gewonnen(ts));
			assertFalse(ConnectFourLib.gewonnen(1, ts));
			assertFalse(ConnectFourLib.gewonnen(2, ts));
		}

		ConnectFourLib.spiel(1, ts, 5);
		assertFalse(ConnectFourLib.gewonnen(ts));
		assertFalse(ConnectFourLib.gewonnen(1, ts));
		assertFalse(ConnectFourLib.gewonnen(2, ts));

		ConnectFourLib.spiel(1, ts, 2);
		assertTrue(ConnectFourLib.gewonnen(ts));
		assertTrue(ConnectFourLib.gewonnen(1, ts));
		assertFalse(ConnectFourLib.gewonnen(2, ts));
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	public void test_minmax_alphabeta() throws Exception{
		// We don't want minmax to be public because it is an internal implementation detail
		// and is of no use to the user. Nonetheless it is important to test that
		// alpha-beta prunning does not influence the result. Sorry for the reflection...
		Method minmax = null;
		for(Method method : ConnectFourLib.class.getDeclaredMethods()){
			if ("minmax".equals(method.getName())){
				minmax = method;
			}
		}
		assertNotNull(minmax);
		minmax.setAccessible(true);

		for(int seed = 42; seed < 60; seed++) {
			System.out.println("Current seed (42 - 60): " + seed);
			int[][] ts = createSpielfeld();
			Random rnd = new Random(seed);
			for (int zugnr = 0; zugnr < 40 && !ConnectFourLib.gewonnen(ts); zugnr++) {
				int zug = 0;
				do {
					zug = rnd.nextInt(7);
				} while (!ConnectFourLib.spielMoeglich(ts, zug));
				ConnectFourLib.spiel(zugnr%2 == 0 ? 1 : 2, ts, zug);

				for(int j = 0; j < 7; j++){
					if (!ConnectFourLib.spielMoeglich(ts, j)){
						continue;
					}
					ConnectFourLib.MINMAX_ALPHA_BETA = true;
					int mit = (int) minmax.invoke(null, zugnr%2 == 0 ? 2 : 1, ts, j, ConnectFourLib.MINMAX_TIEFE, -Integer.MAX_VALUE, Integer.MAX_VALUE);
					ConnectFourLib.MINMAX_ALPHA_BETA = false;
					int ohne = (int) minmax.invoke(null, zugnr%2 == 0 ? 2 : 1, ts, j, ConnectFourLib.MINMAX_TIEFE, -Integer.MAX_VALUE, Integer.MAX_VALUE);
					assertEquals(ohne, mit);
				}
			}
		}
	}
}
