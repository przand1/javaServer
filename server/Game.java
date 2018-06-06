package server;

import java.util.ArrayList;
import java.util.Random;

public class Game {
  private int[][] board;
  private Player[] playerList;

  public Game() {
    board = new int[50][50];
    for (int i=0; i < 50; ++i) {
      for (int j=0; j < 50; ++j) {
        board[i][j] = 0;
      }
    }
  }

  public void runOneTurn() {

  }
}
