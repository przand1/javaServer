package server;

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
  private Boolean lost = false;
  private Boolean win = false;
  private Boolean dirChngReq = false;

  public void setStart() {  waitForStart = false;}
  public void setStartGame() {startGame = true;}
  public void setDirChngReq () {dirChngReq = false;}
  public void sendPlayers(String p) {
    canSendPlayers = true;
    playerCords = p;
  }
  public void sendBoard(String board) {
    this.board = board;
    canSendBoard = true;
  }
  public void setLost() {
    lost = true;
  }
  public void setWin() {
    win = true;
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

      outputStream.writeUTF("CONNECT");
      playerLogin = inputStream.readUTF();
      System.out.println("Player "+id+" login: "+playerLogin);
      logged = true;
//----------------------- TODO POCZĄTEK PĘTLI, SERVER LOSUJE POŁOŻENIA ITP.
for (int round =0;round<5 ;++round) {
  System.out.println("DEBUG: HANDLER "+id+" entered round "+round);
        while(true) {
          TimeUnit.SECONDS.sleep(1);
          if (!waitForStart) {
            outputStream.writeUTF("START "+id);
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
        while(true) { //--JEDNA RUNDA
          if (canSendBoard) {
            outputStream.writeUTF(board);
            canSendBoard = false;
          }
          if(inputStream.available() > 0) {
            direction = inputStream.readUTF().charAt(5);
            System.out.println("Direction from client "+id+": "+direction);
            dirChngReq = true;
          }
          if (lost) {
            outputStream.writeUTF("LOST");
            break;
          }
          if (win) {
            outputStream.writeUTF("WIN");
            break;
          }
          TimeUnit.MILLISECONDS.sleep(10);
        }//--KONIC RUNDY

        // PRZYGOTOWANIE DO NASTĘPNEJ ITERACJI
        waitForStart = true;
        canSendPlayers = false;
        directionSet = false;
        startGame = false;
        canSendBoard = false;
        lost = false;
        win = false;
        dirChngReq = false;
}
//----------------------------- TODO KONIEC PĘTLI


      outputStream.close();
      inputStream.close();
      socket.close();
    } catch(IOException e) {e.printStackTrace();}
      catch(InterruptedException e){System.err.println("INTERRUPTED");}
  }
}