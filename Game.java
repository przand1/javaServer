import java.util.ArrayList;
import java.util.Random;

public class Game {
  private int[][] board;
  private Player[] playerList;

  public Game(char[] playersDirs) {
    board = new int[50][50];
    for (int i=0; i < 50; ++i) {
      for (int j=0; j < 50; ++j) {
        board[i][j] = 0;
      }
    }
    playerList = new Player[5];
    for (int i=0; i<5; i++) {
      playerList[i] = new Player(i,playersDirs[i]);
    }
  }

  public void runOneTurn() {

  }
}

class Player {
  private int ID;
  private int X;
  private int Y;
  private char dir;

  public Player(int ID,char dir) {
    Random r = new Random();
    X = (int)(1+r(50));
    Y = (int)(1+r(50));
    this.ID = ID;
    this.dir = dir;
  }
  public void setID( int ID ) { this.ID = ID; }
  public void setX( int X ) { this.X = X; }
  public void setY( int Y ) { this.Y = Y; }
  public void setDir( char dir ) { this.dir = dir; }
  public int getID() { return ID; }
  public int getX() { return X; }
  public int getY() { return Y; }
  public char getDir() { return dir; }

}
