import java.util.Scanner;

class InteractiveRunnableDemo implements Runnable {
   private Thread t;
   private String threadName;
   private static char s;
   private static Boolean sChanged;
   private static Boolean toggleExit;

   InteractiveRunnableDemo( String name) {
      threadName = name;
      System.out.println("Creating " +  threadName );
      sChanged = false;
      toggleExit = false;
      s = 'w';
   }

   public void setSChanged(Boolean b) {sChanged = b;}
   public void setToggleExit(Boolean b) {toggleExit = b;}
   public void setS(char s) {this.s = s;}
   public Boolean getSChanged() {return sChanged;}
   public Boolean getToggleExit() {return toggleExit;}
   public char getS() {return s;}

   public void run() {
      System.out.println("Running " +  threadName );
      try {
           Scanner scanner = new Scanner(System.in);
          while(!toggleExit){
           s = scanner.next().charAt(0);
           setSChanged(true);
         }
      } catch (Exception e) {
         System.out.println("Thread " +  threadName + " interrupted.");
      }
      System.out.println("Thread " +  threadName + " exiting.");
   }

   public void start () {
      System.out.println("Starting " +  threadName );
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   }
}

public class TestThread {

  public static void displayMap(String[] tabl,int sz) {
     for (byte bb=0; bb<sz; bb++) {
       System.out.println(tabl[bb]);
     }
   }
   public static void main(String args[]) {
     String[] tabl = new String[50];
     int X=25,Y=25;
     tabl[0]=tabl[49]="++++++++++++++++++++++++++++++++++++++++++++++++++";
     for (byte b = 1; b<49; b++) {
       tabl[b] = "+------------------------------------------------+";
     }

     InteractiveRunnableDemo R2 = new InteractiveRunnableDemo( "Thread-2");
     R2.start();

     try {
      while(true) {

        switch(R2.getS()) {
          case 'w': Y--;break;
          case 's': Y++;break;
          case 'a': X--;break;
          case 'd': X++;break;
        }
        if ((tabl[Y].charAt(X) == 'X')||X==0||Y==0||X==49||Y==49) {
          R2.setToggleExit(true);
          System.out.println("ITS OVER YO!");
          break;
        }
        tabl[Y]=tabl[Y].substring(0,X)+'X'+tabl[Y].substring(X+1);
        Thread.sleep(200);
        if (R2.getSChanged()) {
          R2.setSChanged(false);
          //tabl[Y]=tabl[Y].substring(0,X)+'-'+tabl[Y].substring(X+1);
        }
        System.out.println("");
        displayMap(tabl,50);
      }
    } catch(InterruptedException e) {e.printStackTrace();}
        //catch(Exception e) {e.printStackTrace();}
   }
}
