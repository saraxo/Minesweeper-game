/**
 * 
 */
package console;

import java.util.Scanner;

import logic.Level;
import logic.MinesweeperGame;

/**
 * @author sara5
 *
 */
public class ConsoleMinesweeper {
	private MinesweeperGame igra;
	private Scanner scanner;

	  public ConsoleMinesweeper(MinesweeperGame igra) {
	    this.igra = igra;
	    this.scanner = new Scanner(System.in);
	  }
	  
	  /**
	 * Zapocni igru i omoguci korisniku da izabere opcije sa kojima zeli igrat. 
	 * Dozvoljava se igracu da izabere level na kojem zeli igrati.
	 * Poziva funkciju za prikaz instrukcija kako se igra igrica.
	 */
	public void zapocniIgru() {
	    prikaziPocetnuPoruku();
	    izaberiLevel();
	    prikaziInstrukcije();
	    igrajIgru();
	}

	  /**Prikazuje pocetnu poruku igracu
	 * 
	 */
	private void prikaziPocetnuPoruku() {
	    System.out.println("Dobrodosli u Minesweeper!");
	    printSeparatorLine();
	  }

	  //Dozvoli igracu da izabere level na kojem zeli igrati
	  /** Funkcija koja dozvoljava igracu da izabere level na kojem zeli igrati. Igrac moze izabrati samo tri validna levela.
	   * To su Beginner,intermediate i expert. Prikazuje mu levele, zatim trazi da unese koji level zeli. 
	   * Provjerava da li je unio ispravan level, ako nije, trazi ponovni unos levela.
	   * Na kraju pokrece igricu sa odgovarajucim levelom.
	 * 
	 */
	private void izaberiLevel() {
	    Level izabraniLevel = null;
	    
	    //igrac moze izabrati samo validan level, dakle samo tri ponudjena
	    while (izabraniLevel == null) {
	      System.out.println("Izaberite level:");

	    
	      //prikazi dostupne levele i njihove odgovarajuce izbore
	      for (Level level : Level.values()) {
	        System.out.println("(" + level.getIzbor() + ") " + level.getIme());
	      }

	      //uzmi i procesiraj igracev izbor
	      System.out.print("Unesite vas izbor > ");
	      String izbor = scanner.nextLine().trim().toLowerCase();
	      izabraniLevel = Level.getLevelByIzbor(izbor);

	      //ako korisnik izabere nevazeci level
	      if (izabraniLevel == null) {
	        System.out.println("[Nevazeci level. Molimo Vas unesite ispravan level.]");
	        printSeparatorLine();
	      }
	    }

	    //prikazi poruku izabranog levela
	    System.out.println("Level: " + izabraniLevel.getIme());

	 
	    //incijaliziraj igru sa izabranim levelom
	    igra = new MinesweeperGame(izabraniLevel.getIme());
	  }
	

	  // prikazi instrukcije kako igrati igricu
	  /** Funkcija koja prikazuje instrukcije kako igrati igricu u konzoli. Korisnik unosom odgovarajuceg slova sa tastature bira opciju.
	   * 
	 * 
	 */
	private void prikaziInstrukcije() {
	    printSeparatorLine();

	    //prikaz instrukcija
	    System.out.println("Kako da igrate:");
	    System.out.println("'.' predstavlja neotkriveno polje");
	    System.out.println("'0' predstavlja prazno polje");
	    System.out.println("'*' predstavlja minu");
	    System.out.println("'f' predstavlja oznaceno polje");

	    printSeparatorLine();
	  }

	  // zapocni igranje igrice
	  /** Funkcija koja zapocinje igranje igrice. Prikazuje se prva ploca, gdje su sva polja pokrivena. 
	   * Korisnik bira opciju koju zeli. Kada izabere zeljenu opciju, funkcija poziva odgovarajucu funkciju koja pokrece adekvatnu radnju.
	 * 
	 */
	private void igrajIgru() {
	    // prikazi inicijaliziranu plocu
	    System.out.println("Igra je pocela !");
	    prikaziPlocu();

	    while (true) {
	      //prikazi dostupne opcije igracu
	      prikaziOpcije();
	      String opcija = scanner.nextLine().trim().toLowerCase();

	      //uzmi koju je opciju igrac izabrao i poduzmi odgovarajuce akcije
	      switch (opcija) {
	        case "r" -> otkrijPolje();
	        case "f" -> oznaciPolje();
	        case "u" -> unflagField();
	        case "x" -> resetIgru();
	        case "q" -> odustaniIgra();
	        default -> System.out.println("[Nevazeca opcija. Molimo Vas izaberite jednu od vazecih opcija.]");
	      }

	    
	      prikaziPlocu();
	    }
	  }

	  //prikazi trenutnu plocu
	  private void prikaziPlocu() {
	    printSeparatorLine();
	    igra.prikaziPlocu();
	    printSeparatorLine();
	  }

	  //prikazi dostupne opcije igracu
	  private void prikaziOpcije() {
	    System.out.println("Opcije:");
	    System.out.println("(r) Otkrij polje");
	    System.out.println("(f) Oznaci polje");
	    System.out.println("(u) Unflag polje");
	    System.out.println("(x) Resetuj igricu");
	    System.out.println("(q) Odustani od igrice");
	    System.out.print("Unesite vasu opciju > ");
	  }

	  //otkrij polje
	  
	  /** Funkcija koja trazi input od korisnika koje polje zeli otkriti. Provjerava prvo da li je validan unos, da li su validne koordinate polja.
	   * Nakon toga provjerava da li je polje otkriveno, oznaceno ili validno. Ako nije otkriveno, oznaceno, a jeste validno - otkriva polje.
	   * Na kraju provjerava da li je igra zavrsena ili da li je korisnik pobijedio i poduzima odgovarajuce akcije.
	 * 
	 */
	private void otkrijPolje() {
	    System.out.print("Unesite red i kolonu polja koje zelite otkriti (npr., 1 2) > ");
	    String input = scanner.nextLine().trim();

	 
	    //uzmi red i kolonu
	    String[] dijelovi = input.split(" ");
	    if (dijelovi.length != 2) {
	      System.out.println("[Nevazeci unos. Molimo Vas unesite kao 'red kolona' (npr., '2 3').]");
	      return;
	    }

	    int row, col;
	    try {
	      row = Integer.parseInt(dijelovi[0]);
	      col = Integer.parseInt(dijelovi[1]);
	    } catch (NumberFormatException e) {
	      System.out.println("[Nevazeci unos. Molimo Vas unesite validan red i kolonu.]");
	      return;
	    }

	    //provjeri da li je polje validno
	    if (!igra.jelValidnoPolje(row, col)) {
	      System.out.println("[Nevazece koordinate polja. Molimo Vas unesite validan red i kolonu.]");
	      return;
	    }

	    //provjeri da li je polje vec otkriveno
	    if (igra.jelOtkriveno(row, col)) {
	      System.out.println("[Ovo polje je vec otkriveno!]");
	      return;
	    }

	    //provjeri da li je polje vec oznaceno
	    if (igra.jelOznaceno(row, col)) {
	      System.out.println("[Ovo polje je oznaceno. Ne mozete otkriti oznaceno polje.]");
	      return;
	    }

	   //otkrij polje
	    igra.reveal(row, col);

	    //provjeri jel igra zavrsena ili da li je korisnik pobijedio
	    //poduzmi odgovarajuce akcije
	    if (igra.isGameOver()) {
	      prikaziPlocu();
	      System.out.println("[KRAJ IGRE!] Mina je na poziciji: " + row + " - " + col);
	      endGame();
	    } else if (igra.isGameWon()) {
	      prikaziPlocu();
	      System.out.println("[Cestitamo! Pobijedili ste!]");
	      endGame();
	    }
	  }


	  //oznaci polje
	  private void oznaciPolje() {
	    toggleFlag("flag");
	  }

	  // Unflag polje
	  private void unflagField() {
	    toggleFlag("unflag");
	  }
	  
	 
	private void toggleFlag(String action) {
	    System.out.print("Unesi red i kolonu na" + action + " (npr., 1 2) > ");
	    String input = scanner.nextLine().trim();

	   
	    String[] dijelovi = input.split(" ");
	    if (dijelovi.length != 2) {
	    	System.out.println("[Nevazeci unos. Molimo Vas unesite kao 'red kolona' (npr., '2 3').]");
	      return;
	    }

	    int row, col;
	    try {
	      row = Integer.parseInt(dijelovi[0]);
	      col = Integer.parseInt(dijelovi[1]);
	    } catch (NumberFormatException e) {
	    	System.out.println("[Nevazeci unos. Molimo Vas unesite validan red i kolonu.]");
	      return;
	    }
      
	    //provjeri da li je polje validno
	    if (!igra.jelValidnoPolje(row, col)) {
	    	System.out.println("[Nevazece koordinate polja. Molimo Vas unesite validan red i kolonu.]");
	      return;
	    }
	    
	    //provjeri jel polje vec otkriveno ili ne
	    if (igra.jelOtkriveno(row, col)) {
	      System.out
	          .println("[Ovo polje je vec otkriveno. Ne mozete " + action + " otkriveno polje.]");
	      return;
	    }

	    
	    boolean uspjeh;
	    if (action.equals("flag")) {
	      uspjeh = igra.toggleFlagCell(row, col);
	    } else if (action.equals("unflag")) {
	      if (igra.jelOznaceno(row, col)) {
	        uspjeh = igra.toggleFlagCell(row, col);
	      } else {
	        System.out.println("[Ovo polje nije oznaceno. Mozete samo unflag a oznacena polja.]");
	        return;
	      }
	    } else {
	      System.out.println("[Nevazeca akcija. Molim Vas unesite 'flag' ili 'unflag'.]");
	      return;
	    }

	    if (!uspjeh) {
	      System.out.println("[Ne mozete oznaciti vise polja nego mina.]");
	    }
	  }


	  //resetuj igricu i opcionalno promijeni level
	  /**Funkcija koja resetuje igricu. 
	   * Pita korisnika da li zeli promijeniti level ili ne. Ukoliko napise da, ponudi mu da izabere drugi level, a inace samo resetuje.
	 * 
	 */
	private void resetIgru() {
	    printSeparatorLine();
	    System.out.println("[Resetuj igricu.]");

	    //Pitaj igraca da li zeli da promijeni level
	    System.out.print("Da li zelite promijeniti level? (da/ne): ");
	    String promjenaLevela = scanner.nextLine().trim().toLowerCase();

	    switch (promjenaLevela) {
	      case "da" -> izaberiLevel();
	      case "ne" -> System.out.println("[Ostajete na istom levelu.]");
	      default -> {
	        System.out.println("[Nevazeci unos. Molim Vas unesite 'da' ili 'ne'.]");
	        resetIgru();
	      }
	    }
	    //incijaliziraj ili resetuj plocu
	    igra.initializeBoard();
	  }

	  // odustani od igre
	  /**U konzoli postoji opcija da korisnik sam odustane od igrice. Funkcija ispisuje kraj i poziva metodu za kraj igrice.
	 * 
	 */
	private void odustaniIgra() {
	   //odustani od igrice i pozovi metodu za kraj igrice
	    System.out.println("[KRAJ IGRE.]");
	    printSeparatorLine();
	    endGame();
	  }
	

	  //zavrsi igricu i daj opcije za ponovno igranje ili odustajanje
	  /**Funkcija zavrsava igricu i daje opcije za ponovno igranje ili odustajanje od igrice.
	   * Ako je korisnik zeli ponovo igrati, resetuje se ploca.
	 * 
	 */
	private void endGame() {
	    System.out.print("Da li zelite da igrate ponovo? (da/ne): ");
	    String ponovniIzbor = scanner.nextLine().trim().toLowerCase();

	    switch (ponovniIzbor) {
	      case "da" -> {
	        System.out.println("[Nova igra je pocela.]");
	        //resetuj plocu
	        igra.initializeBoard();
	      }
	      case "ne" -> {
	        System.out.println("[Hvala Vam na igranju!]");
	        System.exit(0); // zatvori program
	      }
	      default -> {
	        System.out.println("[Nevazeci unos. Molim Vas unesite 'da' ili 'ne'.]");
	        endGame();
	      }
	    }
	  }
	 
	 
	  /**Razdvajajuca linija, sluzi da igrica u konzoli bude vizuelno ljepsa.
	 * 
	 */
	private void printSeparatorLine() {
	    System.out.println("________________________________\n");
	  }
	

	  /**Kreiraj i zapocni igricu u konzoli.
	 * 
	 */
	public static void main(String[] args) {
	    //kreiraj i zapocni igru
	    MinesweeperGame igra = null; // inicijaliziraj igru na null, za pocetak
	    ConsoleMinesweeper minesweeper = new ConsoleMinesweeper(igra);
	    minesweeper.zapocniIgru();
	  }

}
