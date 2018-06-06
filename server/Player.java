package server;

import java.util.Random;

public class Player {
  private int ID;
  private int X;
  private int Y;
  private char dir;

  public Player(int ID) {
    Random r = new Random();
    X = (int)(r.nextInt(50));
    Y = (int)(r.nextInt(50));
    this.ID = ID;
  }
  public void setID( int ID ) { this.ID = ID; }
  public void setX( int X ) { this.X = X; }
  public void setY( int Y ) { this.Y = Y; }
  public void setDir( char dir ) { this.dir = dir; }
  public int getID() { return ID; }
  public int getX() { return X; }
  public int getY() { return Y; }
  public char getDir() { return dir; }

}
