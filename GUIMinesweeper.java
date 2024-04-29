/**
 * 
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import logic.Level;
import logic.MinesweeperGame;

/**
 * @author sara5
 *
 */
public class GUIMinesweeper {
	  // Konstante za velicinu prozora
	  private static final int FRAME_WIDTH = 800;
	  private static final int FRAME_HEIGHT = 600;

	  //Konstante za font i velicinu fonta
	  private static final String FONT_NAME = "Arial";
	  private static final int WELCOME_FONT_SIZE = 24;
	  private static final int DIFFICULTY_FONT_SIZE = 20;
	  private static final int LABEL_FONT_SIZE = 20;

	
	  private static final int BUTTON_FONT_SIZE = 16;
	  private static final int BEGINNER_BUTTON_FONT_SIZE = 18;
	  private static final int INTERMEDIATE_BUTTON_FONT_SIZE = 14;
	  private static final int EXPERT_BUTTON_FONT_SIZE = 8;

	  // Gdje su smjestene ikonice, koji je glavni folder
	  private static final String RESOURCE_PATH = "images/";

	  // Konstante za ikone
	  private static final String ICON_FILE = "tab-icon-96.png";
	  private static final String FLAG_ICON_FILE = "flag.png";
	  private static final String MINE_ICON_FILE = "mine.png";
	  private static final String SMILEY_ICON_FILE = "smiley.png";
	  private static final String COOL_ICON_FILE = "cool.png";
	  private static final String DEAD_ICON_FILE = "dead.png";

	
	  //konstante za timer delay i inicijalna vrijednost tajmera
	  private static final int TIMER_DELAY = 1000;
	  private static final int INITIAL_TIMER_VALUE = 0;

	
	  //konstants za dugmad i label dimenzije (odnosno za meni)
	  private static final int BUTTON_WIDTH = 200;
	  private static final int BUTTON_HEIGHT = 40;
	  private static final int LABEL_WIDTH = 100;
	  private static final int LABEL_HEIGHT = 40;

	  private JFrame frame;
	  private JPanel pocetniPanel;
	  private JPanel levelPanel;
	  private JPanel igraPanel;
	  private MinesweeperGame igra;
	  private JLabel minesCounterLabel;
	  private JLabel timerLabel;
	  private JButton smileyButton;
	  private JButton[][] polja_dugmad; // varijabla gdje cuvamo polja u vidu dugmadi
	  private Timer timer;
	  private Level trenutniLevel; // varijabla koja cuva trenutni level/izabrani level

	  private boolean igraUToku = false;
	  
	  //konstruktor
	  /** Konstruktor. Incijalizira okvir i kreira pocetni ekran.
	 * 
	 */
	public GUIMinesweeper() {
	    initFrame();
	    kreirajPocetniEkran();
	  }

	  private void initFrame() {
	    frame = new JFrame("Minesweeper");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
	    frame.setResizable(false);
	    centerFrame();
	    setIconImage();
	    frame.setVisible(true);
	  }

	  private void setIconImage() {
	    try {
	      Image iconImage =
	          new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_PATH + ICON_FILE))
	              .getImage();
	      frame.setIconImage(iconImage);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

	  // Pocetni ekran
	  /** Funkcija koja kreira pocetni ekran. Odnosno pocetni panel koristeci JPanel.
	   * Postavlja boju panela. Kreira JLabel i dodaje mu da ispise poruku dobrodoslice, a nakon toga ubacujemo i dugme za pocetak igre.
	 * 
	 */
	private void kreirajPocetniEkran() {
	    pocetniPanel = createPanel(Color.LIGHT_GRAY);

	    JLabel welcomeLabel = createLabel("Dobrodosli u Minesweeper!", WELCOME_FONT_SIZE);
	    addComponent(pocetniPanel, welcomeLabel, 0, 0);

	    JButton playButton = createStyledButton("Igraj", e -> kreirajEkranIzboraLevela());
	    addComponent(pocetniPanel, playButton, 0, 1, new Insets(20, 0, 0, 0));

	    updateFrame(pocetniPanel);
	  }

	 
	  //Ekran koji omogucava korisniku izbor levela
	  /** Ekran koji dolazi nakon pocetnog ekrana, a na ovom ekranu korisnik bira koji ce level da igra.
	   * Sastoji se od tri dugmeta i jednog Labela koji su dodati u panel, a potom smo azurirali i okvir.
	 * 
	 */
	private void kreirajEkranIzboraLevela() {
	    levelPanel = createPanel(Color.LIGHT_GRAY);

	    JLabel difficultyLabel = createLabel("Izaberite level:", DIFFICULTY_FONT_SIZE);
	    addComponent(levelPanel, difficultyLabel, 0, 0);

	    String[] levels = {"Beginner", "Intermediate", "Expert"};
	    for (int i = 0; i < levels.length; i++) {
	      final int levelIndex = i;
	      JButton levelButton =
	          createStyledButton(levels[i], e -> startGame(Level.values()[levelIndex]));
	      addComponent(levelPanel, levelButton, 0, i + 1, new Insets(20, 0, 0, 0));
	    }

	    updateFrame(levelPanel);
	  }


	  //Pocni minesweeper igricu sa izabranim levelom
	  /**
	 * @param izabraniLevel - prosljedujemo joj level koji je korisnik izabrao
	 * Funkcija koja pokrece igricu sa izabranim levelom i kreira GamePanel.
	 */
	private void startGame(Level izabraniLevel) {
	    frame.getContentPane().removeAll();
	    trenutniLevel = izabraniLevel;
	    igra = new MinesweeperGame(izabraniLevel.getIme());
	    createGamePanel(izabraniLevel);
	    frame.revalidate();
	    frame.repaint();
	  }

	  //kreiraj panel za igru sa izabranim levelom
	  /**
	 * @param izabraniLevel - izabrani level igrice
	 * Funkcija koja kreira izgled za odgovarajuci level. Postavljamo flag da je igrica u toku.
	 * Kreiramo top bar.
	 * Izracunavamo velicinu polja i dodajemo odgovarajuci broj polja u okvir.
	 * Dodajemo ikonicu za reset. Pokrecemo tajmer.
	 */
	private void createGamePanel(Level izabraniLevel) {
	    igraUToku = true;
	    igraPanel = new JPanel(new BorderLayout());
	    createTopBar();
	    int velicinaPolja = calculateCellSize(izabraniLevel); // izracunaj velicinu polja

	    JPanel boardPanel =
	        new JPanel(new GridLayout(izabraniLevel.getRows(), izabraniLevel.getCols()));
	    polja_dugmad = new JButton[izabraniLevel.getRows()][izabraniLevel.getCols()]; //ovdje uzimamo koliko nam treba redova i kolona, u ovisnosti od levela

	    for (int row = 0; row < izabraniLevel.getRows(); row++) {
	      for (int col = 0; col < izabraniLevel.getCols(); col++) {
	        JButton poljeDugme = kreirajPoljeDugme(velicinaPolja, izabraniLevel); // proslijedi velicinu polja
	        final int buttonRow = row;
	        final int buttonCol = col;
	        configureCellButton(poljeDugme, buttonRow, buttonCol);
	        polja_dugmad[row][col] = poljeDugme;
	        boardPanel.add(poljeDugme);
	      }
	    }

	    setSmileyButtonIcon(RESOURCE_PATH + SMILEY_ICON_FILE);

	    igraPanel.add(boardPanel, BorderLayout.CENTER);
	    timer = new Timer(TIMER_DELAY, e -> updateTimerLabel());
	    timer.start();
	    updateFrame(igraPanel);
	  }

	
	  //kreiraj polje dugme za igricu sa centriranim i popunjenim tekstom
	  /**
	 * @param velicinaPolja - kolika je velicina polja
	 * @param izabraniLevel - koji smo level izabrali
	 * @return - dugme
	 * Funkcija koja kreira dugme, izracunava poziciju svakog dugmeta i postavlja tekst koji je centriran i vertikalno i horizontalno.
	 * Crta tekst na izracunatoj poziciji te bira odgovarajuci font.
	 */
	private JButton kreirajPoljeDugme(int velicinaPolja, Level izabraniLevel) {
	    JButton cellButton = new JButton() {
	      private static final long serialVersionUID = 5668115853233960843L;

	      @Override
	      protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        String tekst = getText();
	        if (tekst != null && !tekst.isEmpty()) {
	          //izracunaj poziciju da centriras tekst i horizontalno i vertikalno
	          FontMetrics fm = g.getFontMetrics();
	          int x = (getWidth() - fm.stringWidth(tekst)) / 2; //horizontalno centriranje
	          int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent(); //vertikalno centriranje

	          //nacrtaj tekst na izracunatoj poziciji
	          g.drawString(tekst, x, y);
	        }
	      }
	    };

	    cellButton.setPreferredSize(new Dimension(velicinaPolja, velicinaPolja)); // setuj velicinu polja
	    cellButton.setBackground(Color.LIGHT_GRAY);
	    cellButton.setFocusable(false);

	    //izaberi font size na osnovu izabranog levela
	    int fontSize;
	    switch (izabraniLevel) {
	      case BEGINNER:
	        fontSize = BEGINNER_BUTTON_FONT_SIZE;
	        break;
	      case INTERMEDIATE:
	        fontSize = INTERMEDIATE_BUTTON_FONT_SIZE;
	        break;
	      case EXPERT:
	        fontSize = EXPERT_BUTTON_FONT_SIZE;
	        break;
	      default:
	        fontSize = BUTTON_FONT_SIZE; //koristi ga kao defaultnu vrijednost ako level nije prepoznat
	    }

	    Font buttonFont = new Font(FONT_NAME, Font.PLAIN, fontSize);
	    cellButton.setFont(buttonFont);

	    return cellButton;
	  }


	  //konfiguriši polje dugmeta sa akcijama za mis i sa akcijama za specificni red i kolonu. Odnosno sta se desava kada kliknemo na polje.
	  //takodjer omogucava da kliknemo na polje koje zelimo otvoriti
	  /**
	 * @param poljeDugme - koje dugme konfigurišemo
	 * @param row - u kojem redu je polje
	 * @param col - u kojoj je koloni polje
	 * Konfigurise polje dugmeta sa akcijama za mis i akcijama za specificni red i kolonu.
	 * Definise sta se desava kada kliknemo na polje, omogucava nam da kliknemo na polje koje zelimo otvoriti ili oznaciti.
	 */
	private void configureCellButton(JButton poljeDugme, int row, int col) {
	    poljeDugme.addMouseListener(new MouseAdapter() {
	      @Override
	      public void mouseClicked(MouseEvent e) {
	        if (!igraUToku) {
	          return;
	        }
	        if (SwingUtilities.isRightMouseButton(e)) {
	        	//ako je korisnik pritisnuo desni taster miša
	          boolean isFlagged = igra.jelOznaceno(row, col);
	          boolean toggleSuccess = igra.toggleFlagCell(row, col);
	          if (toggleSuccess) {
	            setFlagIcon(poljeDugme, isFlagged);
	            osvjeziPlocu();
	          }
	        }
	      }
	    });

	    poljeDugme.addActionListener(e -> {
	      if (!igraUToku) {
	        return;
	      }
	      igra.reveal(row, col);
	      handleCellButtonClick(row, col);
	    });
	  }

	  //postavi flag ikonu na polje dugmeta i toggle njegovo stanje
	  /**
	 * @param poljeDugme - koje je polje u pitanju
	 * @param isFlagged - da li je oznaceno ili nije
	 * Funkcija koja postavlja flag na polje dugmeta i prati njegovo stanje.
	 */
	private void setFlagIcon(JButton poljeDugme, boolean isFlagged) {
	    if (isFlagged) {
	      poljeDugme.setIcon(null);
	    } else {
	      ImageIcon flagIcon = createImageIcon(RESOURCE_PATH + FLAG_ICON_FILE, poljeDugme.getWidth(),
	          poljeDugme.getHeight());
	      poljeDugme.setIcon(flagIcon);
	    }
	  }

	 
	  //sta se desava kada kliknemo na dugme,hendluj tu akciju
	  /**
	 * @param row - u kojem je redu polje
	 * @param col - u kojoj je koloni polje
	 * Funkcija koja nam daje odgovor na pitanje sta se desava kada se klikne na polje. Osvjezava plocu i postavlja izgled mine.
	 */
	private void handleCellButtonClick(int row, int col) {
	    osvjeziPlocu();
	    if (igra.isGameWon()) {
	      handleGameWon();
	    } else if (igra.isGameOver() && igra.getGameBoard()[row][col] == MinesweeperGame.MINE_POLJE) {
	      postaviIzgledMine(row, col, true);
	      handleGameOver();
	    }
	  }
	  
	  //postavi izgled mina
	  private void postaviIzgledMine(int row, int col, boolean isHittedMine) {
	    int cellSize = polja_dugmad[row][col].getWidth(); // uzmi velicinu polja
	    ImageIcon mineIcon = createImageIcon(RESOURCE_PATH + MINE_ICON_FILE, (int) (cellSize * 0.75),
	        (int) (cellSize * 0.75));
	    polja_dugmad[row][col].setIcon(mineIcon);
	    polja_dugmad[row][col].setText("");

	    // postavi pozadinu na crveno samo kada je igra zavrsena odnosno game over event
	    if (isHittedMine) {
	      polja_dugmad[row][col].setBackground(Color.RED);
	    }
	  }

	  //sta se desava kada pobijedi igrac
	  private void handleGameWon() {
	    setSmileyButtonIcon(RESOURCE_PATH + COOL_ICON_FILE);
	    showMessageDialog("Cestitamo! Pobijedili ste!");
	    igraUToku = false;
	    flagUnflaggedMines();
	  }

	  //sta se desava kada igrac izgubi
	  private void handleGameOver() {
	    setSmileyButtonIcon(RESOURCE_PATH + DEAD_ICON_FILE);
	    showMessageDialog("KRAJ IGRE! Vise srece drugi put.");
	    igraUToku = false;
	    otkrijMine();
	  }


	  // oznaci neoznacene mine
	  private void flagUnflaggedMines() {
	    char[][] plocaIgraca = igra.getGameBoard();
	    for (int row = 0; row < trenutniLevel.getRows(); row++) {
	      for (int col = 0; col < trenutniLevel.getCols(); col++) {
	        if (plocaIgraca[row][col] == MinesweeperGame.MINE_POLJE && !igra.jelOznaceno(row, col)) {
	          int cellWidth = polja_dugmad[row][col].getWidth();
	          int cellHeight = polja_dugmad[row][col].getHeight();
	          ImageIcon flagIcon =
	              createImageIcon(RESOURCE_PATH + FLAG_ICON_FILE, cellWidth, cellHeight);
	          polja_dugmad[row][col].setIcon(flagIcon);
	        }
	      }
	    }
	  }

	  //postavi ikone za mine
	  private void otkrijMine() {
	    char[][] plocaIgraca = igra.getGameBoard();
	    for (int row = 0; row < trenutniLevel.getRows(); row++) {
	      for (int col = 0; col < trenutniLevel.getCols(); col++) {
	        if (plocaIgraca[row][col] == MinesweeperGame.MINE_POLJE) {
	          postaviIzgledMine(row, col, false);
	        }
	      }
	    }
	  }

	  //osvjezi plocu
	  private void osvjeziPlocu() {
	    if (polja_dugmad != null) {
	      char[][] gameBoard = igra.getGameBoard();
	      for (int row = 0; row < polja_dugmad.length; row++) {
	        for (int col = 0; col < polja_dugmad[0].length; col++) {
	          JButton poljeDugme = polja_dugmad[row][col];
	          char vrijednostPolja = gameBoard[row][col];

	          if (igra.jelOtkriveno(row, col)) {
	            if (vrijednostPolja == MinesweeperGame.PRAZNO_POLJE) {
	              poljeDugme.setText("");
	              poljeDugme.setBackground(Color.DARK_GRAY);
	            } else {
	              poljeDugme.setText(String.valueOf(vrijednostPolja));
	              poljeDugme.setBackground(Color.WHITE);
	            }
	          } else {
	            poljeDugme.setText("");
	            poljeDugme.setBackground(Color.LIGHT_GRAY);
	          }
	        }
	      }

	      minesCounterLabel.setText(formatMinesCounter(igra.getMinesCounter()));
	      updateTimerLabel();
	    }
	  }
      
	  //kreiraj ImageIcon. Imamo putanju slike i zadatu sirinu, visinu.
	  /**
	 * @param imagePath - putanja fajla
	 * @param width - sirina
	 * @param height - visina 
	 * @return
	 * Funkcija koja pronalazi sliku na osnovu putanje i kreira objekat.
	 * Nakon sto smo ucitali sliku, skalira ga na odgovarajucu velicinu, a potom dodaje novi objekat tipa ImageIcon.
	 * Analogno vrijedi za sve ostale funkcije koje kao parametar imaju imagePath.
	 */
	private ImageIcon createImageIcon(String imagePath, int width, int height) {
	    ClassLoader classLoader = getClass().getClassLoader();
	    ImageIcon icon = new ImageIcon(classLoader.getResource(imagePath)); //pronalazi sliku na osnovu putanje i kreira objekat
	    Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); //nakon sto smo ucitali sliku, skalira ga na odgovarajucu velicinu
	    return new ImageIcon(scaledImage); //dodajemo novi objekat tipa ImageIcon
	  }

	  //kreiraj top bar
	  /** Funkcija koja kreira top bar. Postavlja novi JPanel. JPanel se dijeli na lijevi i desni panel.
	   * Na lijevom panelu se nalazi brojac mina, a na desnom panelu se nalazi tajmer.
	   * U sredini se nalazi simleyButton koji smo ucitali iz fajlova, a koji ima ActionListener na sebi i sa njim resetujemo igricu.
	 * 
	 */
	private void createTopBar() {
	    JPanel topBarPanel = new JPanel(new BorderLayout());
	    topBarPanel.setBackground(Color.LIGHT_GRAY);

	    JPanel leftLabelPanel = new JPanel(new BorderLayout());
	    leftLabelPanel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
	    leftLabelPanel.setBackground(Color.BLACK);

	    minesCounterLabel = new JLabel(formatMinesCounter(igra.getMinesCounter()));
	    minesCounterLabel.setFont(new Font("Digital-7", Font.PLAIN, LABEL_FONT_SIZE));
	    minesCounterLabel.setForeground(Color.RED);
	    minesCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    leftLabelPanel.add(minesCounterLabel, BorderLayout.CENTER);

	    JPanel rightLabelPanel = new JPanel(new BorderLayout());
	    rightLabelPanel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
	    rightLabelPanel.setBackground(Color.BLACK);

	    timerLabel = new JLabel(formatProtekloVrijeme(igra.getElapsedTime()) + " s");
	    timerLabel.setFont(new Font("Digital-7", Font.PLAIN, 20));
	    timerLabel.setForeground(Color.RED);
	    timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    rightLabelPanel.add(timerLabel, BorderLayout.CENTER);

	    smileyButton = new JButton();
	    smileyButton.setBackground(Color.LIGHT_GRAY);
	    ClassLoader classLoader = getClass().getClassLoader();
	    ImageIcon smileyIcon = new ImageIcon(classLoader.getResource(RESOURCE_PATH + SMILEY_ICON_FILE));
	    Image scaledIcon =
	        smileyIcon.getImage().getScaledInstance(LABEL_HEIGHT, LABEL_HEIGHT, Image.SCALE_SMOOTH);
	    ImageIcon scaledSmileyIcon = new ImageIcon(scaledIcon);
	    smileyButton.setIcon(scaledSmileyIcon);
	    smileyButton.setBorderPainted(false);
	    smileyButton.setFocusPainted(false);
	    smileyButton.setOpaque(false);
	    topBarPanel.add(smileyButton, BorderLayout.CENTER);

	   
	    //dodaj ActionListener kako bi korisnik mogao promijeniti level
	    smileyButton.addActionListener(e -> promjenaLevela());
	    
	    topBarPanel.add(leftLabelPanel, BorderLayout.WEST);
	    topBarPanel.add(rightLabelPanel, BorderLayout.EAST);

	    igraPanel.add(topBarPanel, BorderLayout.NORTH);

	    smileyButton.addActionListener(new ActionListener() {
	      @Override
	      public void actionPerformed(ActionEvent e) {
	        resetGame(trenutniLevel); //resetuj igru sa trenutno izabranim levelom
	        //tajmer podesi na 0
	        if (timer != null) {
	          timer.stop();
	          timerLabel.setText(formatProtekloVrijeme(INITIAL_TIMER_VALUE) + " s");
	          timer.start();
	        }
	      }
	    });
	  }

	
	  //resetuj igricu
	  private void resetGame(Level izabraniLevel) {
	    frame.getContentPane().removeAll();
	    igraUToku = true;
	    igra = new MinesweeperGame(izabraniLevel.getIme());
	    createGamePanel(izabraniLevel);

	    frame.revalidate();
	    frame.repaint();
	  }

	  //formatiraj brojac mina
	  private String formatMinesCounter(int mine) {
	    DecimalFormat df = new DecimalFormat("000");
	    return df.format(mine);
	  }

	 
	  //formatiraj proteklo vrijeme
	  private String formatProtekloVrijeme(int protekloVrijemeUSekundama) {
	    DecimalFormat df = new DecimalFormat("0.0");
	    return df.format(protekloVrijemeUSekundama);
	  }

	  // Update Timer Label
	  private void updateTimerLabel() {
	    timerLabel.setText(formatProtekloVrijeme(igra.getElapsedTime()) + " s");
	  }

	  //pomocna metoda za kreiranje JPanela sa specificiranom bojom za pozadinu
	  private JPanel createPanel(Color bgColor) {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBackground(bgColor);
	    return panel;
	  }

	  //pomocna metoda za kreiranje JLabel sa specificiranim tekstom i font size
	  private JLabel createLabel(String text, int fontSize) {
	    JLabel label = new JLabel(text);
	    label.setFont(new Font(FONT_NAME, Font.PLAIN, fontSize));
	    return label;
	  }

	  //pomocna metoda da doda komponente na panel sa specificnim ogranicenjima
	  private void addComponent(JPanel panel, Component component, int gridX, int gridY,
	      Insets insets) {
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.gridx = gridX;
	    constraints.gridy = gridY;
	    constraints.insets = insets;
	    panel.add(component, constraints);
	  }

	
	  //pomocna metoda da doda komponente u panel bez insetsa
	  private void addComponent(JPanel panel, Component component, int gridX, int gridY) {
	    addComponent(panel, component, gridX, gridY, new Insets(0, 0, 0, 0));
	  }

	  //metoda koja centrira okvir 
	  /** Metoda koja centrira okvir.
	   * Uzima velicinu ekrana kod korisnika kako bi mogla centrirati okvir. Sljedece je da pronadje horizontalnu i vertikalnu poziciju.
	   * Nakon toga postavlja okvir na sredinu, na izracunatu poziciju.
	 * 
	 */
	private void centerFrame() {
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //uzima velicinu ekrana kod korisnika kako bi mogla centrirati
	    int centerX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2); //horizontalna pozicija i dijeli sa dva kako bi bilo na sredini
	    int centerY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2); //vertikalna pozicija i dijeli sa dva kako bi bilo na sredini
	    frame.setLocation(centerX, centerY); //postavlja okvir na sredinu
	  }
	  
	  
	  //pomocna metoda koja azurira okvir sa novim JPanel
	  /**
	 * @param panel 
	 * Metoda koja azurira okvir sa novim JPanelom
	 */
	private void updateFrame(JPanel panel) {
	    frame.getContentPane().removeAll();
	    frame.add(panel);
	    frame.revalidate();
	    frame.repaint();
	  }

	  //Pomocna metoda koja kreira i dodaje JButton sa specificnim teksom i akcijama koje smo vec definisali i samo joj proslijedili
	  private JButton createStyledButton(String text, ActionListener action) {
	    JButton button = new JButton(text);
	    button.setFont(new Font(FONT_NAME, Font.PLAIN, BUTTON_FONT_SIZE));
	    button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
	    button.setBackground(Color.WHITE);
	    button.setForeground(Color.BLACK);
	    button.setFocusable(false);
	    button.addActionListener(action);
	    return button;
	  }

	  // Set Smiley Button Icon - ista funkcija kao i za createImageIcon
	  private void setSmileyButtonIcon(String imagePath) {
	    ClassLoader classLoader = getClass().getClassLoader();
	    ImageIcon icon = new ImageIcon(classLoader.getResource(imagePath));
	    Image scaledIcon =
	        icon.getImage().getScaledInstance(LABEL_HEIGHT, LABEL_HEIGHT, Image.SCALE_SMOOTH);
	    ImageIcon scaledIconImage = new ImageIcon(scaledIcon);
	    smileyButton.setIcon(scaledIconImage);
	  }

	  // izracunaj velicinu polja na osnovu izabranog levela
	  private int calculateCellSize(Level izabraniLevel) {
	    int maxCells = Math.max(izabraniLevel.getRows(), izabraniLevel.getCols());
	    int cellSize = FRAME_WIDTH / maxCells; // prilagodjava okviru ukoliko je potrebno
	    return cellSize;
	  }

	  private void promjenaLevela() {
	    int izbor = JOptionPane.showConfirmDialog(frame, "Da li zelite da promijenite level?",
	        "Promijeni level", JOptionPane.YES_NO_OPTION);
	    if (izbor == JOptionPane.YES_OPTION) {
	      frame.dispose(); //zatvori trenutni okvir
	      new GUIMinesweeper();  //kreiraj prozor za novu igricu
	    } else {
	      resetGame(trenutniLevel);
	    }
	  }

	  private void showMessageDialog(String message) {
	    JOptionPane.showMessageDialog(frame, message);
	  }

	  public static void main(String[] args) {
	    SwingUtilities.invokeLater(() -> new GUIMinesweeper());
	  }

}
