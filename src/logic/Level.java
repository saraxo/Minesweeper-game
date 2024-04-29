/**
 * 
 */
package logic;

/**
 * @author sara5
 *
 */

//dodatna specifikacija koja nam omogucava da izaberemo level
//koristim enum jer su ovo nepromjenjive varijable

public enum Level {
	BEGINNER('b', "beginner",9, 9, 10),
	INTERMEDIATE('i', "intermediate",16, 16, 40),
	EXPERT('e', "expert", 24, 24, 99);
	
	private final char izbor; // znak koji predstavlja izbor levela od strane korisnika
	private final String ime; // naziv levela
	private final int rows; // broj redova na ploci
	private final int cols; // broj kolona na ploci
	private final int numMines; // broj mina na ploci
	
	//konstruktor za Level
	/**
	 * @param izbor - znak koji predstavlja izbor levela od strane korisnika, koristi se u konzoli
	 * @param ime - naziv levela, koristimo ga za GUI
	 * @param rows - broj redova na ploci
	 * @param cols - broj kolona na ploci
	 * @param numMines - broj mina na ploci
	 */
	Level(char izbor, String ime, int rows, int cols, int numMines){
		this.izbor = izbor;
		this.ime = ime;
		this.rows = rows;
		this.cols = cols;
		this.numMines = numMines;
	}
	
	//funkcija koja vraca koji je level u pitanju, preko naziva levela
	//koristicemo je za gui
	/** Funkcija koja sluzi da nam da ime levela koristeci ime, dakle za GUI.
	 * @param ime - naziv levela
	 * @return - vraca level ako ga imamo
	 */
	public static Level getLevelByName(String ime) {
		for(Level level: values()) {
			if(level.getIme().equalsIgnoreCase(ime)) {
				//dakle ako imamo level, vrati ga
				return level;
			}
		}
		return null; //nema odgovarajuceg levela
	}
	//funkcija koja vraca koji je level u pitanju
	//koristi se u konzoli
	/** Funkcija analogna funkciji getLevelByName, samo se koristi za konzolu.
	 * @param izbor 
	 * @return
	 */
	public static Level getLevelByIzbor(String izbor) {
		for(Level level: values()) {
			if(level.getIzbor() == izbor.charAt(0)) {
				//ukoliko pronadje level, vrati ga
				return level;
			}
		}
		return null; // nije pronadjen odgovarajuci level
	}
	
	//getteri
	public char getIzbor() {
		return izbor;
	}
	public String getIme() {
		return ime;
	}
	public int getRows() {
		return rows;
	}
	public int getCols() {
		return cols;
	}
	public int getNumMines() {
		return numMines;
	}

	
}
