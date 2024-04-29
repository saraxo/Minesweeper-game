# Minesweeper-game
Implementation of popular Minesweeper game in Java. 

<b>Requirements<b>
<br>
<p>The project should have three packages - logic, console and gui.</p>
<br>

<b>Logic package requirements</b>
<br>
<p>
  1.Create class MinesweeperGame with constructor and his parameters:
  <br>
  -<b>int rows</b> - number of board rows
  <br>
  -<b>int cols</b> - number of board columns
  <br>
  -<b>int numMines</b> - the number of mines on the board
  <br>
  <br>
  2.Implement game manipulation methods:
  <br>
  -<b>void intializeBoard()</b> - place fields and mines on the board
  <br>
  -<b>void reveal(int row,int col)</b> - reveals the field at given position
  <br>
  -<b>boolean isGameWon()</b> - checks if the game is won
  <br>
  -<b>boolean isGameOver()</b> - checks if the game is over (mine detected)
  <br>
  <br>
  3.Implement additional constructors that make it easier to intialize the game for different levels:
  <br>
  <b>MinesweeperGame("beginner")</b>
  <br>
  <b>MinesweeperGame("intermediate")</b>
  <br>
  <b>MinesweeperGame("expert")</b>
  <br>
</p>
<br>
<b>GUI package requirements</b>
<p>
  In this package implement GUI interface for playing Minesweeper game.
  <br>
 1. Create class <b>GUIMinesweeper</b> that will use logic from the logic package.
  <br>
 2. Implement a graphical representation of the board, allow the player to interact by clicking on the  fiels. Use Java Swing.
  <br>
 3. Display feedback and endgame messages through a GUI.
</p>
<br>
<b>Console package requirements</b>
<p>
  In this package implement console interface for playing Minesweeper game.
  1. Create class ConsoleMinesweeper that will use the logic from the logic package.
  <br>
  2. Implement a user interface for entering and displaying the board.
  <br>
  3. Allow the user to enter row and column to reveal the field.
  <br>
  4. Continuosly display the current state of the board, feedback and endgame messages.
</p>
<br>
<p>Flow diagram is presented below.</p>



