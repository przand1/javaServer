package testarea;

import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class SampleServer {
  private static ServerSocket serverSocket;
  private static Socket clientSocket;
  private static ClientHandler handler;
  private static Player player;
  private static String playerCords = "";

  public static void main(String[] args) throws IOException {

    serverSocket = new ServerSocket(9999);
    try {
      clientSocket = serverSocket.accept();
      System.out.println("Client connected: "+clientSocket);
      DataInputStream clientInput = new DataInputStream(clientSocket.getInputStream());
      DataOutputStream clientOutput = new DataOutputStream(clientSocket.getOutputStream());

      handler = new ClientHandler(clientSocket,clientOutput,clientInput,0);
      handler.start();

    } catch(Exception e) {serverSocket.close();e.printStackTrace();}


          player = new Player(handler.getID());                    //TWORZENIE GRACZY
                                                                                    //Player LOSUJE SOBIE CORDS
          playerCords += (player.getID() + ":("+player.getX()+","+player.getY()+") ");//CORDS
          System.out.println("sending Start to handler");
          handler.setStart();

    handler.sendPlayers(playerCords);

      while(true) {
        try {
          TimeUnit.SECONDS.sleep(1);
          if (handler.directionIsSet()) {
            for (int i =0;i<5 ;++i ) {
              player.initDir(handler.getDirection());  //GRACZ DOSTAJE KIERUNEK Z HANDLERA
              handler.setStartGame();
            }
            break;
          }
        }catch(InterruptedException e){System.err.println("INTERRUPTED");}
      }

      Game game = new Game(player); //PRZKAZANIE GRACZY GRZE
                                        //GRA DOSTAJE GRACZY Z CORDS I DIRECTION

      while (player.getLost()==false) {       //TU SIĘ DZIEJE WŁAŚCIWA GRA
        try {
          TimeUnit.MILLISECONDS.sleep(100);//co 100ms

          if (player.getLostNotify()) {
            player.setLostNotify(false);
            handler.setEndGame();
          }
          if (handler.getDirChngReq() && !player.getLost()) {
                game.chngPlayerDir(handler.getID(),handler.getDirection());
          }

            //MOVE
          game.runOneTurn();

          if (!player.getLost()){  //JEŚLI NIE PRZEGRAŁ, WYŚLIJ PLANSZĘ
            handler.sendBoard(game.toString());
          }
          } catch(InterruptedException e) {e.printStackTrace();}
        }

      serverSocket.close();
  }
}
class Game {
  private int[][] board;
  private Player player;

  public Game(Player player) {
    board = new int[50][50];        //PUSTA PLANSZA
    for (int i=0; i < 50; ++i) {
      for (int j=0; j < 50; ++j) {
        board[i][j] = 0;
      }
    }
    this.player = player;  //DODAJ GRACZY
      board[player.getX()][player.getY()] = player.getID();
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
    player.setDir(dir);
  }

  public void runOneTurn() {
      if (!player.getLost()){
        player.move();//MOVE ZMIENIA PLAYER.X/Y    SPRAWDZIĆ CZY NIE WYLAZŁ <0 >49
        if (player.getX()<0 || player.getX()>49 || player.getY()<0 || player.getY()>49 || board[player.getX()][player.getY()] != 0) {
          player.setLost(true);
          player.setLostNotify(true);
        }
        else board[player.getX()][player.getY()] = player.getID();
      }
  }

}
