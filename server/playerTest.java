package server;

import java.util.Random;
import java.util.Scanner;

public class playerTest {
  public static void main(String[] args) {

    char[] A = {'a','b','c','d'};
    test T = new test(A);
    System.out.println("main class: "+A[0]);
    System.out.println("test class: "+T.getD());
    T.modify();
    System.out.println("test after mod: "+T.getD());
    System.out.println("main after mod: "+A[0]);
    A[0]='c';
    System.out.println("test after mod: "+T.getD());
    System.out.println("main after mod: "+A[0]);


    //TEST ZMIANY KIERUNKU
  //   Player p = new Player(0);
  //   char[] t1={'w','a','s','d'};
  //   char[] t2 ={'l','l','l','l','r','r','r','r','r','r'};
  //   for (char c : t1) {
  //    p.dirInit(c);
  //    System.out.println("INIT: "+p.getDir());
  //  }
  //   for (char c : t2) {
  //    p.setDir(c);
  //    System.out.println("GOING "+c+": "+p.getDir());
  //   }

    //TEST RUCHU
    // Player p = new Player(0);
    // p.initDir('W');
    // char c;
    // Scanner s = new Scanner(System.in);
    // System.out.println("Initial: "+p.getX()+","+p.getY());
    //
    //   do {
    //   c = s.nextLine().charAt(0);
    //   p.setDir(c);
    //   p.move();
    //   System.out.println(p.getX()+" "+p.getY());
    // } while(c!='q');

  }
}

class test {
  private char[] tab;
  public test(char[] a){tab=a;}
  public void modify() {tab[0]='d';}
  public char getD() {return tab[0];}
}
