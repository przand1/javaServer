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
  private static Game game;
  private static int[] score = new int[5];
  private static String scoreString = "KONIEC ";

  public static void main(String[] args) throws IOException {

      serverSocket = new ServerSocket(9999);
      for (int i =0;i<5;i++ ) {
        score[i]=0;
      }

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
              }
            break;
            }
          }catch(InterruptedException e){System.err.println("INTERRUPTED");}
        }
        for (int i =0;i<5 ;++i) {
          playerHandlers[i].setStart();
          System.out.println("SERVER: setStart "+(i+1));
        }
//----------------------------------------------  POCZĄTEK PĘTLI ----------------------------------------------------------------------
for (int round =0;round<5 ;++round) {
        for (int i =0;i<5 ;++i) {
          playerList[i].setRandomXY();                             //Player LOSUJE SOBIE CORDS
          playerCords += (playerList[i].getX()+" "+playerList[i].getY()+" ");//CORDS
        }
        for (int i =0;i<5 ;++i ) {
          playerHandlers[i].sendPlayers(playerCords);
          System.out.println("SERVER: sendPlayers "+(i+1));
        }
        while(true) {
          try {
            TimeUnit.SECONDS.sleep(1);
            if (playerHandlers[0].directionIsSet() && playerHandlers[1].directionIsSet() && playerHandlers[2].directionIsSet() && playerHandlers[3].directionIsSet() && playerHandlers[4].directionIsSet()) {
              for (int i =0;i<5 ;++i ) {
                playerList[i].initDir(playerHandlers[i].getDirection());  //GRACZ DOSTAJE KIERUNEK Z HANDLERA
                playerHandlers[i].setStartGame();
                System.out.println("SERVER: setStartGame "+(i+1));
              }
              break;
            }
          }catch(InterruptedException e){System.err.println("INTERRUPTED");}
        }

        game = new Game(playerList); //PRZKAZANIE GRACZY GRZE
                                          //GRA DOSTAJE GRACZY Z CORDS I DIRECTION

        while (playersLeft > 1) {       //TU SIĘ DZIEJE WŁAŚCIWA GRA
          try {
            TimeUnit.MILLISECONDS.sleep(100);//co 100ms

              for (int i =0;i<5 ;++i ) { //SPRAWDŹ, CZY KTOŚ NIE PRZEGRAŁ
                if (playerList[i].getLostNotify()) {
                  playerList[i].setLostNotify(false);
                  playerHandlers[i].setLost(playersLeft);//TODO: zmienić na LOST + miejsce
                  score[i]+=playersLeft;
                  playersLeft--;
                  System.out.println("SERVER: "+playerHandlers[i].getPlayerLogin()+" LOST.");
                }
              }


              for (int i =0;i<5 ;++i ) {  //SPRAWDŹ, CZY KTOŚ NIE ŻĄDA ZMIANY KIERUNKU
                if (playerHandlers[i].getDirChngReq() && !playerList[i].getLost()) {
                  game.chngPlayerDir(playerHandlers[i].getID(),playerHandlers[i].getDirection());
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
          } //KONIEC RUNDY

          for (Player p : playerList) {
            if (!p.getLost()) {
              playerHandlers[p.getID()-1].setWin(); // WIN do ostatniego gracza
              System.out.println("SERVER: "+playerHandlers[p.getID()-1].getPlayerLogin()+" WON.");
              score[p.getID() - 1]++;
            }
          }
          while(true) {
            try {
              System.out.println("SERVER: Waiting for players to finish round "+(round+1));
              TimeUnit.SECONDS.sleep(1);
              if (playerHandlers[0].getRoundFinished() && playerHandlers[1].getRoundFinished() && playerHandlers[2].getRoundFinished() && playerHandlers[3].getRoundFinished() && playerHandlers[4].getRoundFinished()) {
                for (int i =0;i<5 ;++i ) {
                  playerHandlers[i].setRoundFinished();
                }
                System.out.println("SERVER: ALL ABOARD!");
                break;
              }
            }catch(InterruptedException e){System.err.println("INTERRUPTED");}
          }
          // PRZYGOTOWANIE DO NASTĘPNEJ ITERACJI
          playerCords = "";
          playersLeft = 5;
          for (Player p : playerList) {
            p.setLost(false);
          }

}
//------------------------------------------- KONIEC PĘTLI ----------------------------------------------
//    [7,2,8,12,5] punkty
  //   0 1 2 3  4  gracze
      int[] indexed = new int[5];
      for(int j=1;j<5;j++) {
        indexed[j]=j;
      }
      int temp;
      for(int i=0;i<4;i++) {
      	for(int j=i+1;j<5;j++) {
      		if(score[i] > score[j]) {

      			temp = score[i];
      			score[i]=score[j];
      			score[j]=temp;

      			temp = indexed[i];
      			indexed[i]=indexed[j];
      			indexed[j]=temp;
      		}
      	}
      }
      for(int i=0;i<5;i++) {
      	scoreString += ( playerHandlers[indexed[i]].getPlayerLogin() + ": " + score[i] + " ");
      }

      for (int i =0;i<5 ;++i ) {
        playerHandlers[i].setScore(scoreString);
      }
      serverSocket.close();
  }
}
