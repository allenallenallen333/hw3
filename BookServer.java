import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
          
        	library.put(bookName, bookQuantity);
      }
    } catch (FileNotFoundException e) {
    	  e.printStackTrace();
    }
    
    // TODO: handle request from clients
 
  }
  
  static class Record {
	int recordID;
	String username, bookName;
	
	public Record (int id, String user, String book) {
		this.recordID = id;
		this.username = user;
		this.bookName = book;
	}
  }
  //library is all books available and number of book
  private static HashMap<String, Integer> library;
  //records is for records of orders
  private static HashMap<Integer, Record> records;
  private static int numRecord = 1;
  
  public static String parseCommand (String command) {
    String[] tokens = command.split(" ");
    switch (tokens[0]) {
      case "borrow": return borrowBook(tokens[1], tokens[2]);
      case "return": return returnBook(tokens[1]);
      case "list": return list(tokens[1]);
      case "inventory": return inventory();
    }
	return "error";
  }
  
  public synchronized static String borrowBook(String student, String book) {
	Integer i = library.get(book);
	if(i == null) {
	  return "Request Failed - Book not available";
	} else {
	  library.put(book, i - 1);
	  Record record = new Record(numRecord, student, book);
	  numRecord++;
	  records.put(record.recordID, record);
	  return "You request hasbeen approved, " + record.recordID + " " + record.username + " " + record.bookName;
	}  
  }
  
  public synchronized static String returnBook(String recordId) {
	Record record = records.remove(Integer.parseInt(recordId));
	if(record == null) {
		return recordId + " not found, no such borrow record";
	}
	library.put(record.bookName, library.get(record.bookName) + 1);
	return recordId + " is returned";
  }
  
  public synchronized static String list(String student) {
	String list = "";
	String nextLine = "";
	for(Record record: records.values()) {
	  if(record.username.equals(student)) {
		list += nextLine + record.recordID + record.bookName;
		nextLine = "\n";
	  }
	}
	if(list.equals("")) {
		return "No record found for " + student;
	}
	return list;
  }
  
  public synchronized static String inventory() {
	String list = "";
	String nextLine = "";
	for(Map.Entry<String, Integer> book : library.entrySet()) {
		list += nextLine + book.getKey() + " " + book.getValue();
		nextLine = "\n";
	}
	return list;
  }
}


