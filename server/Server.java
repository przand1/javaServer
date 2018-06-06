package server;

import java.net.*;
import java.io.*;

public class Server {

  private static ServerSocket serverSocket;
  private static Socket clientSocket;
  private static int numOfPlayers = 0;
  private static ClientHandler[] playerHandlers = new ClientHandler[5];
  private static Player[] playerList = new Player[5];
  private static String playerCords = "";

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
        System.out.println("SERVER: Waiting for players...");//Bez tego nie działa
        if (playerHandlers[0].isLogged() && playerHandlers[1].isLogged() && playerHandlers[2].isLogged() && playerHandlers[3].isLogged() && playerHandlers[4].isLogged()) {
          for (int i =0;i<5 ;++i ) {
            playerList[i] = new Player(playerHandlers[i].getID());
            playerCords += (playerList[i].getID() + ":("+playerList[i].getX()+","+playerList[i].getY()+") ");
            playerHandlers[i].setStart();
          }
        break;
        }
      }
      for (int i =0;i<5 ;++i ) {
        playerHandlers[i].sendPlayers(playerCords);
      }
      while(true) {
        System.out.println("SERVER: Waiting for directions...");
        if (playerHandlers[0].directionIsSet() && playerHandlers[1].directionIsSet() && playerHandlers[2].directionIsSet() && playerHandlers[3].directionIsSet() && playerHandlers[4].directionIsSet()) {
          for (int i =0;i<5 ;++i ) {
            playerHandlers[i].setStartGame();
          }
          break;
        }
      }

      Game game = new Game(playerList);
      try {
        for(int j=0;j<10;j++) {
          Thread.sleep(100);
            for (int i =0;i<5 ;++i ) {
              System.out.println("TURN "+j+" "+i);
              playerHandlers[i].sendBoard(game.toString());
            }
        }
      } catch(InterruptedException e) {e.printStackTrace();}

      for (int i =0;i<5 ;++i ) {
        playerHandlers[i].setEndGame();
      }


      serverSocket.close();
  }
}


////////////////////////////////////////////////////////////////////////////////////////////////////

class ClientHandler extends Thread {
  final DataInputStream inputStream;
  final DataOutputStream outputStream;
  final Socket socket;
  private int id;
  private String playerLogin;
  private char direction;
  private String playerCords;
  private String board;
  //FLAGI
  private Boolean waitForStart = true;
  private Boolean logged = false;
  private Boolean canSendPlayers = false;
  private Boolean directionSet = false;
  private Boolean startGame = false;
  private Boolean canSendBoard = false;
  private Boolean endGame = false;

  public ClientHandler(Socket s,DataOutputStream out,DataInputStream in, int id) {
    socket = s;
    outputStream = out;
    inputStream = in;
    this.id=id+1;
  }

  public void setStart() {
    waitForStart = false;
  }
  public Boolean isLogged() {
    return logged;
  }
  public int getID () {
    return id;
  }
  public void sendPlayers(String p) {
    canSendPlayers = true;
    playerCords = p;
  }
  public Boolean directionIsSet() {
    return directionSet;
  }
  public void setStartGame() {
    startGame = true;
  }
  public char getDirection() {
    return direction;
  }
  public void sendBoard(String board) {
    this.board = board;
    canSendBoard = true;
  }
  public void setEndGame() {
    endGame = true;
  }

  @Override
  public void run() {
    try {
      int counter = 0;

      outputStream.writeUTF("CONNECT");
      playerLogin = inputStream.readUTF();
      System.out.println("Player "+id+" login: "+playerLogin);
      logged = true;
      while(true) {
        System.out.println("HANDLER:Fill the loop");//Bez tego nie działa
        if (!waitForStart) {
          outputStream.writeUTF("START "+id);
          break;
        }
      }
      while(true) {
        System.out.println("HANDLER:Fill the loop");//Bez tego nie działa
        if(canSendPlayers) {
          outputStream.writeUTF("PLAYERS "+playerCords);
          break;
        }
      }
      direction = inputStream.readUTF().charAt(6);
      directionSet = true;
      while (true) {
        System.out.println("HANDLER:Direction Set");
        if (startGame) {
          outputStream.writeUTF("GAME");
          break;
        }
      }
//utworzyć listener
      while(true) {
        System.out.println("HANDLER:Fill the loop");
        if (canSendBoard) {
          outputStream.writeUTF(board);
          canSendBoard = false;
        }
        if(inputStream.available() > 0) {
          System.out.println(inputStream.readUTF());
        }
        if (endGame) {
          break;
        }
      }

      System.out.println("HANDLER: SENDING END");
      outputStream.writeUTF("END");




      outputStream.close();
      inputStream.close();
      socket.close();
    } catch(IOException e) {e.printStackTrace();}
  }
}
