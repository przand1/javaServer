package testarea;
import java.util.Scanner;
public class GameTest {
  public static void main(String[] args) {
    Player[] players = new Player[5];
    Scanner scn = new Scanner(System.in);
    char c;
    for (int i=1;i<=5 ;i++ ) {
      players[i-1] = new Player(i);
    }
    Game game = new Game(players);
    for (Player p : players) {
      p.initDir('w');
    }
    do {
      c=scn.nextLine().charAt(0);
      players[0].setDir(c);
      game.runOneTurn();
      game.displayGame();
    } while (players[0].getLost()==false);
  }
}
