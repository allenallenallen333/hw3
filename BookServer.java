import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class BookServer {
  public static void main (String[] args) {
    int tcpPort;
    int udpPort;
    if (args.length != 1) {
      System.out.println("ERROR: Provide 1 argument: input file containing initial inventory");
      System.exit(-1);
    }
    String fileName = args[0];
    tcpPort = 7000;
    udpPort = 8000;
    
    // parse the inventory file
    try {
        Scanner sc = new Scanner(new FileReader(fileName));

        while(sc.hasNextLine()) {
          String cmd = sc.nextLine();
          String[] tokens = cmd.split(" ");

          String bookName = tokens[0];
          int bookQuantity = Integer.parseInt(tokens[1]);
          
          inventory.add(new Entry(bookName, bookQuantity));
        }
    } catch (FileNotFoundException e) {
    	e.printStackTrace();
    }
    
    // TODO: handle request from clients
  }
  
  // ArrayList to keep track of inventory. The reason an arraylist is used is so the inventory can be printed in the same order.
  static ArrayList<Entry> inventory = new ArrayList<Entry>();
  
  // Entry class for unique book name and quantity
  static class Entry{
  	String bookName;
  	int bookQuantity;
  	
  	public Entry(String bookName, int bookQuantity) {
  		this.bookName = bookName;
  		this.bookQuantity = bookQuantity;
  	}
  }
}
