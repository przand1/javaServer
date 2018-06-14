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
  private Boolean roundFinished = false;
  private Boolean canSendScore = false;


  private int position;
  private String score;

  public void setStart() {  waitForStart = false;}
  public void setStartGame() {startGame = true;}
  public void setDirChngReq () {dirChngReq = false;}
  public void setRoundFinished () {roundFinished = false;}
  public void sendPlayers(String p) {
    canSendPlayers = true;
    playerCords = p;
  }
  public void sendBoard(String board) {
    this.board = board;
    canSendBoard = true;
  }
  public void setLost(int position) {
    lost = true;
    this.position = position;
  }
  public void setWin() {
    win = true;
  }
  public void setScore(String score) {
    this.score = score;
    canSendScore = true;
  }

  public Boolean isLogged() {return logged;}
  public Boolean directionIsSet() {return directionSet;}
  public Boolean getDirChngReq () {return dirChngReq;}
  public Boolean getRoundFinished () {return roundFinished;}
  public int getID () {return id;}
  public char getDirection() {return direction;}
  public String getPlayerLogin() {return playerLogin;}

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
      if ( (!(playerLogin.split(" ")[0].equals("LOGIN"))) || playerLogin.split(" ")[1].equals("")) {
        outputStream.writeUTF("ERROR");//TODO wywal go
      } else {
        playerLogin = playerLogin.split(" ",2)[1];
        outputStream.writeUTF("OK");
      }

      System.out.println("Player "+id+" login: "+playerLogin);
      logged = true;
      while(true) {
        TimeUnit.SECONDS.sleep(1);
        if (!waitForStart) {
          outputStream.writeUTF("START "+id);
          break;
        }
      }
      String directionBuffer;
//----------------------- POCZĄTEK PĘTLI, SERVER LOSUJE POŁOŻENIA ITP.
for (int round =0;round<5 ;++round) {
  System.out.println("DEBUG: HANDLER "+id+" entered round "+(round+1));
  if (round>0) {
    outputStream.writeUTF("ROUND "+(round+1));
  }

        while(true) {
          TimeUnit.SECONDS.sleep(1);
          if(canSendPlayers) {
            outputStream.writeUTF("PLAYERS "+playerCords);
            break;
          }
        }
        directionBuffer = inputStream.readUTF();
        if( (!(directionBuffer.split(" ")[0].equals("BEGIN"))) || directionBuffer.split(" ")[1].equals("")) {
          outputStream.writeUTF("ERROR");
          //TODO wywal go
        } else {
          outputStream.writeUTF("OK");
          System.out.println("HANDLER "+id+" direction: "+directionBuffer);
          direction = directionBuffer.charAt(6);
          directionSet = true;
        }
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
            directionBuffer = inputStream.readUTF();
            if( (!(directionBuffer.split(" ")[0].equals("MOVE"))) || directionBuffer.split(" ")[1].equals("")) {
              outputStream.writeUTF("ERROR");
              //TODO wywal go
            } else {
              direction = directionBuffer.charAt(5);
              outputStream.writeUTF("OK");
              System.out.println("Direction from client "+id+": "+direction);
              dirChngReq = true;
            }
          }
          if (lost) {
            outputStream.writeUTF("LOST "+position);
            break;
          }
          if (win) {
            outputStream.writeUTF("WIN");
            break;
          }
          TimeUnit.MILLISECONDS.sleep(10);
        }//--KONIC RUNDY

        roundFinished = true;

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
//----------------------------- KONIEC PĘTLI

      while (!canSendScore) {
        TimeUnit.MILLISECONDS.sleep(500);
      }
      outputStream.writeUTF(score);

      outputStream.close();
      inputStream.close();
      socket.close();
    } catch(IOException e) {e.printStackTrace();}
      catch(InterruptedException e){System.err.println("INTERRUPTED");}
  }
}
