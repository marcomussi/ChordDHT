import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {

	private static Scanner in;

	public static void main(String[] args) throws UnknownHostException {
	
		/* while true loop is only to avoid the termination of the program
		 * in case of wrong command, in case of a correct one the execution
		 * is moved to the Node class code */
		Node x;
		int localPort, connectPort;
		String connectIP;
		
		while(true) {
		
			System.out.println("Select the operation:\n"
				+ "CREATE\n"
				+ "JOIN\n"
				+ "SEARCH\n");
			in = new Scanner(System.in);
			String operation = in.next().toUpperCase();
			
			switch(operation) {
				case "CREATE" : { // create network
					System.out.println("Local Destination Port (IP is taken from the device): ");
					localPort = Integer.parseInt(in.next());
					x = new Node();
					x.newChord(new InetSocketAddress(InetAddress.getLocalHost(),localPort)); } 
					break;
				case "JOIN" : { // add node to the network of the connect node
					System.out.println("Local Destination Port (IP is taken from the device): ");
					localPort = Integer.parseInt(in.next());
					System.out.println("IP of the node you want to connect: ");
					connectIP = in.next();
					System.out.println("Port of the connection node: ");
					connectPort = Integer.parseInt(in.next());
					x = new Node();
					x.joinNetwork(new InetSocketAddress(InetAddress.getLocalHost(),localPort),
												new InetSocketAddress(connectIP,connectPort)); } 
					break;	
				case "SEARCH" : // search in a node specified
					Node.searchItem(); 
					break;
				default : // if the command is different from the above the program ends
					System.out.println("\nWrong Input!\n"); 
					break;
			}
		}
	}
	
}
