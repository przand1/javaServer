import java.net.*;
import java.io.*;

public class Server {

  private static ServerSocket serverSocket;
  private static Socket clientSocket;
  private static int numOfPlayers = 0;
  private static ClientHandler[] playerList = new ClientHandler[5];

  public static void main(String[] args) throws IOException {

      serverSocket = new ServerSocket(9999);

      while(numOfPlayers < 5) {
        clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: "+clientSocket);
            DataInputStream clientInput = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream clientOutput = new DataOutputStream(clientSocket.getOutputStream());

            playerList[numOfPlayers] = new ClientHandler(clientSocket,clientOutput,clientInput,numOfPlayers);
            playerList[numOfPlayers].start();
            numOfPlayers++;

        } catch(Exception e) {serverSocket.close();e.printStackTrace();}
      }
      //int counter = 0;
      //while(!(playerList[0].isLogged()&&playerList[1].isLogged()&&playerList[2].isLogged()&&playerList[3].isLogged()&&playerList[4].isLogged())){
      //System.out.print(counter++);}
      try {
        synchronized(playerList[0]) {
          synchronized(playerList[1]) {
            synchronized(playerList[2]) {
              synchronized(playerList[3]) {
                synchronized(playerList[4]) {
                  for (int i=0;i<5 ;i++ ) {
                    playerList[i].setStart();
                    System.out.println("WYWOŁAŁEM SetStart"+i);
                  }
                }
              }
            }
          }
        }
      } catch (Exception e) {e.printStackTrace();}



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

  @Override
  public void run() {
    try {
      int counter = 0;

      System.out.println("Sending: CONNECT....");
      outputStream.writeUTF("CONNECT");
      synchronized(this) {
        playerLogin = inputStream.readUTF();
        System.out.println("Player "+id+" login: "+playerLogin);
        logged = true;
      }
      while(true) {
        if (!waitForStart) {
          System.out.println("WYSYŁAM START");
          outputStream.writeUTF("START");
          break;
        }
      }

      outputStream.close();
      inputStream.close();
      socket.close();
    } catch(IOException e) {e.printStackTrace();}
  }
}
