package server;

import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class Server {

  private static ServerSocket serverSocket;
  private static Socket clientSocket;
  private static int numOfPlayers = 0;
  private static ClientHandler[] playerHandlers = new ClientHandler[5];
  private static Player[] playerList = new Player[5];
  private static String playerCords = "";
  private static int playersLeft =  5;

  public static void main(String[] args) throws IOException {

      serverSocket = new ServerSocket(9999);

      while(numOfPlayers < 5) {
        clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: "+clientSocket);
            DataInputStream clientInput = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream clientOutput = new DataOutputStream(clientSocket.getOutputStream());

            playerHandlers[numOfPlayers] = new ClientHandler(clientSocket,clientOutput,clientInput,numOfPlayers);
            playerHandlers[numOfPlayers].start();
            numOfPlayers++;

        } catch(Exception e) {serverSocket.close();e.printStackTrace();}
      }
        while(true) {
            try {
            TimeUnit.SECONDS.sleep(1);
            if (playerHandlers[0].isLogged() && playerHandlers[1].isLogged() && playerHandlers[2].isLogged() && playerHandlers[3].isLogged() && playerHandlers[4].isLogged()) {
              for (int i =0;i<5 ;++i ) {
                playerList[i] = new Player(playerHandlers[i].getID());                    //TWORZENIE GRACZY
                                                                                          //Player LOSUJE SOBIE CORDS
                playerCords += (playerList[i].getID() + ":("+playerList[i].getX()+","+playerList[i].getY()+") ");//CORDS
                playerHandlers[i].setStart();
              }
            break;
            }
          }catch(InterruptedException e){System.err.println("INTERRUPTED");}
        }
        for (int i =0;i<5 ;++i ) {
          playerHandlers[i].sendPlayers(playerCords);
        }
        while(true) {
          try {
            TimeUnit.SECONDS.sleep(1);
            if (playerHandlers[0].directionIsSet() && playerHandlers[1].directionIsSet() && playerHandlers[2].directionIsSet() && playerHandlers[3].directionIsSet() && playerHandlers[4].directionIsSet()) {
              for (int i =0;i<5 ;++i ) {
                playerList[i].initDir(playerHandlers[i].getDirection());  //GRACZ DOSTAJE KIERUNEK Z HANDLERA
                playerHandlers[i].setStartGame();
              }
              break;
            }
          }catch(InterruptedException e){System.err.println("INTERRUPTED");}
        }

        Game game = new Game(playerList); //PRZKAZANIE GRACZY GRZE
                                          //GRA DOSTAJE GRACZY Z CORDS I DIRECTION

        while (playersLeft > 1) {       //TU SIĘ DZIEJE WŁAŚCIWA GRA
          try {
            TimeUnit.MILLISECONDS.sleep(100);//co 100ms

              for (int i =0;i<5 ;++i ) { //SPRAWDŹ, CZY KTOŚ NIE PRZEGRAŁ
                if (playerList[i].getLostNotify()) {
                  playerList[i].setLostNotify(false);
                  playerHandlers[i].setLost();//TODO: zmienić na LOST + miejsce
                  playersLeft--;
                }
              }


              for (int i =0;i<5 ;++i ) {  //SPRAWDŹ, CZY KTOŚ NIE ŻĄDA ZMIANY KIERUNKU
                if (playerHandlers[i].getDirChngReq() && !playerList[i].getLost()) {
                  game.chngPlayerDir(playerHandlers[i].getID(),playerHandlers[i].getDirection());
                  System.out.println("SERVER: Dostałem dirChngReq od gracza "+playerHandlers[i].getID());
                  playerHandlers[i].setDirChngReq();
                }
              }

              //MOVE
              game.runOneTurn();

              for (int i =0;i<5 ;++i ) {
                if (!playerList[i].getLost()){  //JEŚLI NIE PRZEGRAŁ, WYŚLIJ PLANSZĘ
                  playerHandlers[i].sendBoard(game.toString());
                }
              }
            } catch(InterruptedException e) {e.printStackTrace();}
          }
          //TODO wysłać WIN do ostatniego gracza
          for (Player p : playerList) {
            if (!p.getLost()) {
              playerHandlers[p.getID()-1].setWin(); //TODO wysłać WIN do ostatniego gracza
            }
          }
      
      serverSocket.close();
  }
}
