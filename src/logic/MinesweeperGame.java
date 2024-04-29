/**
 * 
 */
package logic;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author sara5
 *
 */
public class MinesweeperGame {
	  public static final char NEOTKRIVENO_POLJE = '.'; // neotkriveno polje
	  public static final char PRAZNO_POLJE = '0'; // prazno polje
	  public static final char MINE_POLJE = '*'; // polje sa minom
	  public static final char OZNACENO_POLJE = 'f'; // oznaceno polje

	  private int rows; // broj redova na ploci
	  private int cols; // broj kolona na ploci
	  private int numMines; // broj mina na ploci

	  private char[][] ploca; // predstavlja igracu plocu sa simbolima
	  private boolean[][] otkriveno; // indikator da li je polje otkriveno ili nije
	  private boolean[][] oznaceno; // indikator da li je polje oznaceno kao mina ili nije

	  private boolean gameOver; // varijabla koja prati da li je igra zavrsena
	  private boolean gameWon; // varijabla koja prati da li je korisnik pobijedio

	  private int brojacMina; // broji mine koje su ostale na ploci
	  private Timer timer; // brojac koji sluzi da mjeri vrijeme
	  private boolean timerStarted; // varijabla koja oznacava da li je timer poceo brojati ili ne
	  private long pocetnoVrijeme; // pocetno vrijeme u milisekundama
	  private int protekloVrijeme; // proteklo vrijeme u sekundama

	  // Konstruktor
	  /** Konstruktor klase, inicijaliziramo plocu.
	 * @param rows - broj redova na ploci
	 * @param cols - broj kolona na ploci
	 * @param numMines - broj mina na ploci
	 */
	public MinesweeperGame(int rows, int cols, int numMines) {
	    this.rows = rows;
	    this.cols = cols;
	    this.numMines = numMines;
	    initializeBoard();
	  }

	  //Konstruktor za predefinisane levele, odnosno koji ce level korisnik izabrat
	  // (beginner, intermediate, expert)
	  /** Konstruktor ukoliko imamo level, inicijalizira redove i kolone i mine u skladu sa izabranim levelom.
	 * @param level - koji je izabrani level
	 */
	public MinesweeperGame(String level) {
	    Level izabraniLevel = Level.getLevelByName(level);
	    if (izabraniLevel == null) {
	    	throw new IllegalArgumentException("Nevazeci level!");
	    }
	    //ako je vazeci level, inicijaliziraj plocu
	    this.rows = izabraniLevel.getRows();
	    this.cols = izabraniLevel.getCols();
	    this.numMines = izabraniLevel.getNumMines();
	    initializeBoard();
	  }

	 
	  //incijaliziraj plocu sa minama, brojevima i zastavicama
	  public void initializeBoard() {
	    inicijalizirajPraznuPlocu();
	    postaviMine();
	    calculateAdjacentMineCounts();
	    resetGameFlags();
	  }

	  // inicijaliziraj praznu plocu na pocetku
	  /** Funkcija koja na pocetku inicijalizira praznu plocu. Postavlja brojac mina, a takodjer i incijalizira vrijeme na 0.
	 * 
	 */
	private void inicijalizirajPraznuPlocu() {
	    ploca = new char[rows][cols];
	    otkriveno = new boolean[rows][cols];
	    oznaceno = new boolean[rows][cols];
	    gameOver = false;
	    gameWon = false;

	    //incijaliziraj prazna polja sa '0' - konzola
	    for (int row = 0; row < rows; row++) {
	      for (int col = 0; col < cols; col++) {
	        ploca[row][col] = '0';
	      }
	    }

	    this.brojacMina = numMines; // inicijaliziraj brojac mina
	    
	   
	    //inicijaliziraj polja vezana za tajmer
	    //resetuj tajmer polja
	    if (timer != null) {
	      timer.cancel();
	    }
	    timer = new Timer();
	    timerStarted = false;
	    pocetnoVrijeme = 0;
	    protekloVrijeme = 0; //inicijaliziraj proteklo vrijeme na 0
	  }

	  //resetuj flags u igrici na pocetku svake igrice
	  private void resetGameFlags() {
	    gameOver = false;
	    gameWon = false;
	  }

	  //postavi mine na plocu na random pozicijama
	  /** Postavlja mine na plocu na random pozicijama. 
	   * Mine se postavljaju sve dok ne dodje do numMines odnosno do broja mina koje jedan level mora imati.
	   * Za svako izracunato random polje provjeri da li to polje vec sadrzi minu ili ne, ako ne sadrzi postavlja je.
	 * 
	 */
	private void postaviMine() {
	    int postavljeneMine = 0;
	    //postavljaj mine na plocu sve dok ne dodjes do numMines
	    //odnosno do broja mina koje jedan level mora imati
	    while (postavljeneMine < numMines) {
	      int randomRed = (int) (Math.random() * rows);
	      int randomKolona = (int) (Math.random() * cols);

	      //provjera da li je polje vec mina ili nije
	      if (ploca[randomRed][randomKolona] != MINE_POLJE) {
	        ploca[randomRed][randomKolona] = MINE_POLJE;
	        postavljeneMine++;
	      }
	    }
	  }

	  //izracunaj broj susjednih mina za svako polje koje nije mina
	  /**Funkcija koja racuna broj susjednih mina za svako polje koje nije mina.
	   * Ukoliko je polje mina, preskace racuanje.
	   * Azurira svako polje sa brojem susjednih mina.
	 * 
	 */
	private void calculateAdjacentMineCounts() {
	    for (int row = 0; row < rows; row++) {
	      for (int col = 0; col < cols; col++) {
	    	//preskoci racunanje ako je polje mina
	        if (ploca[row][col] != MINE_POLJE) {
	          int blizuMina = countAdjacentMines(row, col);
	          if (blizuMina > 0) {
	            //update polje sa brojem susjednih mina
	            ploca[row][col] = (char) ('0' + blizuMina);
	          }
	        }
	      }
	    }
	  }

	  //izracunaj broj susjednih mina za dato polje
	  /**
	 * @param row koji je red polja
	 * @param col koja je kolona polja
	 * @return koliko mina ima blizu zadatog polja
	 * 
	 * Provjerava osam susjednih polja za mine i koliko ih je. Takodjer osigurava da je susjedno polje u granicama i da sadrzi minu.
	 */
	private int countAdjacentMines(int row, int col) {
	    int blizuMina = 0;

	    //provjeri osam susjednih polja za mine
	    for (int i = -1; i <= 1; i++) {
	      for (int j = -1; j <= 1; j++) {
	        int noviRed = row + i;
	        int novaKolona = col + j;

	        // osiguranje da je susjedno polje u granicama i da sadrzi minu
	        if (jelValidnoPolje(noviRed, novaKolona) && ploca[noviRed][novaKolona] == MINE_POLJE) {
	          blizuMina++;
	        }
	      }
	    }
	    return blizuMina;
	  }

	  //provjera da li je polje validno
	  public boolean jelValidnoPolje(int row, int col) {
	    return row >= 0 && row < rows && col >= 0 && col < cols;
	  }

	  //provjera da li je polje na ploci otkriveno ili ne
	  public boolean jelOtkriveno(int row, int col) {
	    return otkriveno[row][col];
	  }

	 
	  //provjera da li je polje na ploci oznaceno kao mina
	  public boolean jelOznaceno(int row, int col) {
	    return oznaceno[row][col];
	  }

	 

	  /**
	 * @param row - u kom je redu polje
	 * @param col - u kojoj je koloni polje
	 * Funkcija prvo provjerava da li su unesene vazece koordinate polja, a nakon toga da li je igrica zavrsena, da li je polje otkriveno ili oznaceno.
	 * Ukoliko zadovoljava validnosti i da nije otvoreno, oznaceno ili da igrica nije zavrsena, otkriva se polje.
	 * Nakon toga u funkciji se pokrece tajmer kada je prvo polje otkriveno.
	 * Funkcija nakon toga provjerava da li otkriveno polje sadrzi minu (ako sadrzi, igrica je gotova), a ako je otkriveno prazno polje rekurzivno otkriva susjedna polja.
	 * Na kraju imamo provjeru da li su sva polja bez mina otkrivena, a ako jesu korisnik je pobijedio.
	 * Tajmer se zaustavlja kada se igrica zavrsi.
	 * 
	 */
	public void reveal(int row, int col) {
	    //provjera je li polje validno ili nije
	    if (!jelValidnoPolje(row, col)) {
	      throw new IllegalArgumentException("Nevazece koordinate polja");
	    }

	
	    //provjeri da li je igrica zavrsena ili da li je polje vec otkriveno/oznaceno kao mina
	    if (gameOver || otkriveno[row][col] || oznaceno[row][col]) {
	      return;
	    }

	   
	    //oznaci polje kao otkriveno
	    otkriveno[row][col] = true;

	    //pokreni tajmer kada je prvo polje otkriveno
	    if (!timerStarted) {
	      startTimer();
	    }

	    //ako otkriveno polje sadrzi minu, igrica je gotova
	    if (ploca[row][col] == MINE_POLJE) {
	      gameOver = true;
	      //zaustavi tajmer kada je igrica gotova
	      stopTimer();
	      return;
	    }
	    //ako je otkriveno prazno polje, rekurzivno otkrij susjedna polja
	    if (ploca[row][col] == PRAZNO_POLJE) {
	      // istrazi susjedna polja u svim smjerovima
	      for (int i = -1; i <= 1; i++) {
	        for (int j = -1; j <= 1; j++) {
	          int noviRed = row + i;
	          int novaKolona = col + j;

	          //provjeri da li su susjedna polja validna
	          if (jelValidnoPolje(noviRed, novaKolona)) {
	            reveal(noviRed, novaKolona);
	          }
	        }
	      }
	    }
	    //provjeri da li su sva polja bez mina otkrivena (pobjedio je korisnik)
	    int brojacOtkrivenih = 0;
	    for (int i = 0; i < rows; i++) {
	      for (int j = 0; j < cols; j++) {
	        if (otkriveno[i][j]) {
	          brojacOtkrivenih++;
	        }
	      }
	    }

	    if (brojacOtkrivenih == rows * cols - numMines) {
	      gameWon = true;
	      //zaustavi tajmer kada igrac pobijedi
	      stopTimer();
	    }
	  }

	  /**
	 * @param row - red u kojem se polje nalazi
	 * @param col - kolona u kojem se polje nalazi
	 * @return
	 * Funkcija koja postavlja zastavicu na polje. Provjerava da li je polje oznaceno ili otkriveno. 
	 * Provjerava da li zastavica ima manje ili jednako broju mina, posto ih ne smije biti vise od broja mina.
	 */
	public boolean toggleFlagCell(int row, int col) {
	    if (jelOtkriveno(row, col)) {
	      return false;
	    }
	    if (jelOznaceno(row, col)) {
	      // Unflag polje
	      oznaceno[row][col] = false;
	      brojacMina++;
	    } else {
	      // pokreni tamjer kada je prvo polje oznaceno
	      if (!timerStarted) {
	        startTimer();
	      }

	      if (brojacMina <= 0) {
	        return false; //ne mozes oznaciti vise polja nego sto ima mina
	      }

	      // oznaci polje
	      oznaceno[row][col] = true;
	      brojacMina--;
	    }
	    return true; // vrati true ako je uspjesno postavljanje zastavice
	  }
 
	  //ovo se odnosni na konzolni prikaz ploce
	  /** Funkcija koja se odnosi na ispisivanje ploce u konzoli. Sa pratecim porukama.
	 * 
	 */
	public void prikaziPlocu() {
		  
	    System.out.println("MINE: " + brojacMina + " " + "VRIJEME: " + protekloVrijeme + " s");
	    System.out.println("Trenutna ploca:\n");

	    // Izracunaj ukupnu sirinu koja je potrebna za igranje, a na osnovu broja kolona i redova
	    int sirinaKolone = 3; // fiksna sirina za kolonu
	    int sirinaPolja = 3; // fiksna sirina za polje
	    int leadingSpacesSirina = 3; // fiksna sirina za leading spaces

	   
	    //ispisi top red sa indeksima kolona
	    System.out.print(String.format("%" + leadingSpacesSirina + "s", ""));
	    for (int col = 0; col < cols; col++) {
	      if (col == 10) {
	        System.out.print(" ");
	      }
	      System.out.print(String.format("%" + sirinaKolone + "s", col));
	    }
	    System.out.println();

	  //ispisi horizontalnu liniju koja odvaja indekse redova i sadrzaj na ploci
	    System.out.print(String.format("%" + leadingSpacesSirina + "s", ""));
	    for (int col = 0; col < cols; col++) {
	      for (int i = 0; i < sirinaPolja; i++) {
	        System.out.print("-");
	      }
	    }
	    System.out.println();

	    //ispisi redove sa sadrzajem ploce i indekse redova na desnoj strani
	    for (int row = 0; row < rows; row++) {
	      System.out
	          .print(String.format("%" + leadingSpacesSirina + "s", (row < 10 ? " " : "") + row + " |"));

	      for (int col = 0; col < cols; col++) {
	        char polje;
	        if (oznaceno[row][col]) {
	          polje = OZNACENO_POLJE;
	        } else if (otkriveno[row][col]) {
	          polje = ploca[row][col];
	        } else {
	          polje = NEOTKRIVENO_POLJE;
	        }
	        System.out.print(" " + polje + " ");
	      }

	      System.out.println("|");
	    }

	    //ispisi ponovo horizontalnu liniju nakon ploce
	    System.out.print(String.format("%" + leadingSpacesSirina + "s", ""));
	    for (int col = 0; col < cols; col++) {
	      for (int i = 0; i < sirinaPolja; i++) {
	        System.out.print("-");
	      }
	    }
	    System.out.println();

	    //ispisi broj kolona ponovo na dnu
	    System.out.print(String.format("%" + leadingSpacesSirina + "s", ""));
	    for (int col = 0; col < cols; col++) {
	      if (col == 10) {
	        System.out.print(" ");
	      }
	      System.out.print(String.format("%" + sirinaKolone + "s", col));
	    }
	    System.out.println();
	  }

	  //metoda za prikaz ploce u GUI
	  /**
	 * @return - ploca
	 * Metoda koja sluzi za prikaz ploce u GUI.
	 */
	public char[][] getGameBoard() {
	    char[][] kopijaPloce = new char[rows][cols];

	    for (int row = 0; row < rows; row++) {
	      for (int col = 0; col < cols; col++) {
	        if (otkriveno[row][col]) {
	        //ako je polje otkriveno, kopiraj sadrzaj iz trenutne ploce
	          kopijaPloce[row][col] = ploca[row][col];
	        } else if (oznaceno[row][col]) {
	        //ako je polje oznaceno, setuj ga kao oznaceno
	          kopijaPloce[row][col] = OZNACENO_POLJE;
	        } else if (isGameOver() || isGameWon()) {
	          //ako je igra zavrsena ili je korisnik pobijedio, prikazi mine
	          if (ploca[row][col] == MINE_POLJE) {
	            kopijaPloce[row][col] = MINE_POLJE;
	          } else {
	            kopijaPloce[row][col] = NEOTKRIVENO_POLJE;
	          }
	        } else {
	        	//ako polje nije ni otkriveno ni oznaceno, setuj ga kao neotkriveno
	          kopijaPloce[row][col] = NEOTKRIVENO_POLJE;
	        }
	      }
	    }

	    return kopijaPloce;
	  }

	  //daj trenutnu vrijednost brojaca mina
	  public int getMinesCounter() {
	    return brojacMina;
	  }

	  // daj proteklo vrijeme u sekundama
	  public int getElapsedTime() {
	    return protekloVrijeme;
	  }

	  // pokreni tajmer kada je prvo polje otkriveno
	  /** Funkcija koja pokrece tajmer kada je prvo polje otkriveno tako sto uzima trenutno vrijeme. 
	   * Nakon toga azurira tajmer svaku sekundu i mjeri koliko je vremena proteklo dok se igrica ne zavrsi.
	 * 
	 */
	private void startTimer() {
	    if (!timerStarted) {
	      timerStarted = true;
	      pocetnoVrijeme = System.currentTimeMillis(); // uzmi trenutno vrijeme
	      timer.scheduleAtFixedRate(new TimerTask() {
	        @Override
	        public void run() {
	         //izracunaj proteklo vrijeme u sekundama
	          long trenutnoVrijeme = System.currentTimeMillis();
	          protekloVrijeme = (int) ((trenutnoVrijeme - pocetnoVrijeme) / 1000);
	        }
	      }, 1000, 1000); // postavi tajmer da azurira proteklo vrijeme svake sekunde
	    }
	  }

	  // Zaustavi tajmer kada je igra zavrsena ili kada je korisnik pobijedio
	  private void stopTimer() {
	    if (timerStarted) {
	      timer.cancel();
	      timerStarted = false;
	    }
	  }


	  public boolean isGameWon() {
	    return gameWon;
	  }

	  public boolean isGameOver() {
	    return gameOver;
	  }
}
