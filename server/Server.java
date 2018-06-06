package server;

import java.net.*;
import java.io.*;

public class Server {

  private static ServerSocket serverSocket;
  private static Socket clientSocket;
  private static int numOfPlayers;
  private static ClientHandler[] playerHandlers;
  private static Game game;
  private static Player[] playerList;
  private static String playerCords;

  public Server() {
    numOfPlayers = 0;
    playerCords = new String("");
    playerHandlers = new ClientHandler[5];
    playerList = new Player[5];
  }

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
        if (playerHandlers[0].isLogged()&&playerHandlers[1].isLogged()&&playerHandlers[2].isLogged()&&playerHandlers[3].isLogged()&&playerHandlers[4].isLogged()) {
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

      //game = new Game();
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
  private Boolean waitForStart = true;
  private Boolean logged = false;
  private char direction;
  private Boolean canSendPlayers = false;
  private String playerCords;

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

  @Override
  public void run() {
    try {
      int counter = 0;

      System.out.println("Sending: CONNECT....");
      outputStream.writeUTF("CONNECT");
      playerLogin = inputStream.readUTF();
      System.out.println("Player "+id+" login: "+playerLogin);
      logged = true;
      while(true) {
        System.out.println("HANDLER:Fill the loop");//Bez tego nie działa
        if (!waitForStart) {
          System.out.println("HANDLER:WYSYŁAM START");
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
      direction = inputStream.readUTF().charAt(0);



      outputStream.close();
      inputStream.close();
      socket.close();
    } catch(IOException e) {e.printStackTrace();}
  }
}
