package testarea;


import java.net.*;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class ClientHandler extends Thread {
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
  private Boolean dirChngReq = false;

  public void setStart() {  waitForStart = false;}
  public void setStartGame() {startGame = true;}
  public void setEndGame() {endGame = true;}
  public void setDirChngReq () {dirChngReq = false;}
  public void sendPlayers(String p) {
    canSendPlayers = true;
    playerCords = p;
  }
  public void sendBoard(String board) {
    this.board = board;
    canSendBoard = true;
  }

  public Boolean isLogged() {return logged;}
  public Boolean directionIsSet() {return directionSet;}
  public Boolean getDirChngReq () {return dirChngReq;}
  public int getID () {return id;}
  public char getDirection() {return direction;}

  public ClientHandler(Socket s,DataOutputStream out,DataInputStream in, int id) {
    socket = s;
    outputStream = out;
    inputStream = in;
    this.id=id+1;
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
        TimeUnit.SECONDS.sleep(1);
        if (!waitForStart) {
          outputStream.writeUTF("START "+id);
          System.out.println("sending START");
          break;
        }
      }
      while(true) {
        TimeUnit.SECONDS.sleep(1);
        if(canSendPlayers) {
          outputStream.writeUTF("PLAYERS "+playerCords);
          break;
        }
      }
      direction = inputStream.readUTF().charAt(6);
      directionSet = true;
      while (true) {
        TimeUnit.SECONDS.sleep(1);
        if (startGame) {
          outputStream.writeUTF("GAME");
          break;
        }
      }
      while(true) {
        if (canSendBoard) {
          outputStream.writeUTF(board);
          canSendBoard = false;
        }
        if(inputStream.available() > 0) {
          direction = inputStream.readUTF().charAt(5);
          dirChngReq = true;
        }
        if (endGame) {
          break;
        }
        TimeUnit.MILLISECONDS.sleep(10);
      }

      System.out.println("HANDLER: SENDING END");
      outputStream.writeUTF("END");




      outputStream.close();
      inputStream.close();
      socket.close();
    } catch(IOException e) {e.printStackTrace();}
      catch(InterruptedException e){System.err.println("INTERRUPTED");}
  }
}
