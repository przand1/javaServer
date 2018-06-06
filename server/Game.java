package server;

import java.util.ArrayList;
import java.util.Random;

public class Game {
  private int[][] board;
  private Player[] playerList;

  public Game(Player[] playerList) {
    board = new int[50][50];
    for (int i=0; i < 50; ++i) {
      for (int j=0; j < 50; ++j) {
        board[i][j] = 0;
      }
    }
    this.playerList = playerList;
    for (Player p : playerList) {
      board[p.getX()][p.getY()] = p.getID();
    }
  }

  public void displayGame() {
    for (int j=0;j<50 ;j++ ) {
      for (int i=0;i<50 ;i++) {
        System.out.print(board[i][j]);
      }
      System.out.print('\n');
    }
  }

  public String toString() {
    String boardString = "";
    for (int j=0;j<50 ;j++ ) {
      for (int i=0;i<50 ;i++) {
        boardString += String.valueOf(board[i][j]);
      }
      boardString += '\n';
    }
    return boardString;
  }

  public void runOneTurn() {

  }
}
