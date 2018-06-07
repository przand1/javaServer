package testarea;

import java.util.Random;

public class Player {
  private int ID;
  private int X;
  private int Y;
  private char dir;
  private String possibleDirs = "AWDS";
  private Boolean lost = false;
  private Boolean lostNotify = false;

  public Player(int ID) {
    Random r = new Random();
    X = (int)(r.nextInt(50));
    Y = (int)(r.nextInt(50));
    this.ID = ID;
  }
  public void setID( int ID ) { this.ID = ID; }
  public void setX( int X ) { this.X = X; }
  public void setY( int Y ) { this.Y = Y; }
  public void setLost( Boolean lost ) { this.lost = lost; }
  public void setLostNotify( Boolean lostNotify ) { this.lostNotify = lostNotify; }
  public void setDir( char dir ) {
    switch(dir) {
      case 'r':
      case 'R':
        char c = this.dir;
        this.dir = possibleDirs.charAt((possibleDirs.indexOf(c)+1)%4);
        break;
      case 'l':
      case 'L':
        char c1 = this.dir;
        this.dir = possibleDirs.charAt((possibleDirs.indexOf(c1)+3)%4);
        break;
      case 's':
      case 'S':
        break;
    }
  }
  public void initDir(char dir) {
    switch(dir) {
      case 'w':
      case 'W':
        this.dir = 'W';
        break;
      case 's':
      case 'S':
        this.dir = 'S';
        break;
      case 'a':
      case 'A':
        this.dir = 'A';
        break;
      case 'd':
      case 'D':
        this.dir = 'D';
        break;
    }
  }
  public int getID() { return ID; }
  public int getX() { return X; }
  public int getY() { return Y; }
  public char getDir() { return dir; }
  public Boolean getLost() { return lost; }
  public Boolean getLostNotify() { return lostNotify; }

  public void move() {
    switch (dir) {
      case 'W':
        Y--;
        break;
      case 'A':
        X--;
        break;
      case 'S':
        Y++;
        break;
      case 'D':
        X++;
        break;
    }
  }

}
