package client;
import java.io.*;

public class Inputer extends Thread {

  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private char dir;
  private Boolean dirChanged = false;
  private Boolean stop = false;

  public char getDir() {return dir;}
  public Boolean getDirChanged() {return dirChanged;}
  public void setDirChanged() {dirChanged = false;}
  public void setStop() {stop = true;}


  public void run () {
    while(!stop) {
      try {
        dir = reader.readLine().charAt(0);
        dirChanged = true;
      }
      catch(IOException e) {
        e.printStackTrace();
      }
    }
  }
}
