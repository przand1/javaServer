package server;

import java.util.Random;

public class Game {
  private int[][] board;
  private Player[] playerList;

  public Game(Player[] playerList) {
    board = new int[50][50];        //PUSTA PLANSZA
    for (int i=0; i < 50; ++i) {
      for (int j=0; j < 50; ++j) {
        board[i][j] = 0;
      }
    }
    this.playerList = playerList;
    for (Player p : playerList) {   //DODAJ GRACZY
      board[p.getX()][p.getY()] = p.getID();
    }
  }

  public void displayGame() {   //DO TESTÓW
    for (int j=0;j<50 ;j++ ) {
      for (int i=0;i<50 ;i++) {
        System.out.print(board[i][j]);
      }
      System.out.print('\n');
    }
  }

  public String toString() {   //DO WYSŁANIA
    String boardString = "";
    for (int j=0;j<50 ;j++ ) {
      for (int i=0;i<50 ;i++) {
        boardString += String.valueOf(board[i][j]);
      }
      boardString += '\n';
    }
    return boardString;
  }

  public void chngPlayerDir(int playerId,char dir) { //WYWOŁYWANE PRZEZ SERWER
    playerList[playerId-1].setDir(dir);
  }

  public void runOneTurn() {                    //DODAĆ OBSŁUGĘ KOLIZJII
    for (Player p : playerList) {
      if (!p.getLost()){
        p.move();//MOVE ZMIENIA PLAYER.X/Y    SPRAWDZIĆ CZY NIE WYLAZŁ <0 >49
        if (p.getX()<0 || p.getX()>49 || p.getY()<0 || p.getY()>49 || board[p.getX()][p.getY()] != 0) {
          p.setLost(true);
          p.setLostNotify(true);
        }
        else board[p.getX()][p.getY()] = p.getID();
      }
    }
  }

}
