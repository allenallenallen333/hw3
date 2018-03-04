import java.util.Scanner;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class BookClient {
	public static void main(String[] args) {
		String hostAddress;
		int tcpPort;
		int udpPort;
		int clientId;

		if (args.length != 2) {
			System.out.println("ERROR: Provide 2 arguments: commandFile, clientId");
			System.out.println("\t(1) <command-file>: file with commands to the server");
			System.out.println("\t(2) client id: an integer between 1..9");
			System.exit(-1);
		}

		String commandFile = args[0];
		clientId = Integer.parseInt(args[1]);
		hostAddress = "localhost";
		tcpPort = 7000;// hardcoded -- must match the server's tcp port
		udpPort = 8000;// hardcoded -- must match the server's udp port

		// Default is UDP
		String protocol = "U";

		
		try {
			PrintWriter out = new PrintWriter("out_" + clientId + ".txt");
			
			// UDP
			InetAddress ia = InetAddress.getByName(hostAddress);
			DatagramSocket datasocket = new DatagramSocket();
			int len = 1024;
			byte[] rbuffer = new byte[len];
			DatagramPacket sPacket, rPacket;

			Scanner sc = new Scanner(new FileReader(commandFile));

			while (sc.hasNextLine()) {
				String cmd = sc.nextLine();
				String[] tokens = cmd.split(" ");

				if (tokens[0].equals("setmode")) {
					// TODO: set the mode of communication for sending commands to the server
					protocol = tokens[1];
				} else if (tokens[0].equals("borrow")) {
					// TODO: send appropriate command to the server and display the
					// appropriate responses form the server
					
					if (protocol.equals("T")) {
						Socket socket = new Socket(hostAddress, tcpPort);
			    		DataOutputStream tOut = new DataOutputStream(socket.getOutputStream());
			    		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			    		tOut.writeBytes(cmd + '\n');
			    		String line;
			    		while ((line = in.readLine()) != null) {
			    			out.println(line);
			    		}
			    		socket.close();
					}
					
					if (protocol.equals("U")) {
						byte[] buffer = new byte[cmd.length()];
						buffer = cmd.getBytes();
						sPacket = new DatagramPacket(buffer, buffer.length, ia, udpPort);
						datasocket.send(sPacket);
						rPacket = new DatagramPacket(rbuffer, rbuffer.length);
						datasocket.receive(rPacket);
						String retstring = new String(rPacket.getData(), 0, rPacket.getLength());
						
						out.println(retstring);
					}
				} else if (tokens[0].equals("return")) {
					// TODO: send appropriate command to the server and display the
					// appropriate responses form the server
				} else if (tokens[0].equals("inventory")) {
					// TODO: send appropriate command to the server and display the
					// appropriate responses form the server
				} else if (tokens[0].equals("list")) {
					// TODO: send appropriate command to the server and display the
					// appropriate responses form the server
				} else if (tokens[0].equals("exit")) {
					// TODO: send appropriate command to the server
					out.close();
				} else {
					System.out.println("ERROR: No such command");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (SocketException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
