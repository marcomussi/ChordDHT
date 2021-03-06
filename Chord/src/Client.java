import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {

	private static Scanner in;

	public static void main(String[] args) throws UnknownHostException {
	
		int localPort, connectPort;
		String connectIP, searchStr;
		InetSocketAddress desiredDataNodeAddr;
		Node x;
		in = new Scanner(System.in);
		
		/* While true loop is only to avoid the termination of the program
		 * in case of wrong command, in case of a correct one the execution
		 * is moved to the Node class code */
		while(true) {
		
			System.out.println("Select the operation:\n"
				+ "CREATE\n"
				+ "JOIN\n"
				+ "SEARCH\n");
			String operation = in.next().toUpperCase();
			
			switch(operation) {
				case "CREATE" : { // Create network
					System.out.println("Local Destination Port (IP is taken from the device): ");
					localPort = Integer.parseInt(in.next());
					x = new Node();
					x.create(new InetSocketAddress(InetAddress.getLocalHost(),localPort)); } 
					break;
				case "JOIN" : { // Add node to the network of the connect node
					System.out.println("Local Destination Port (IP is taken from the device): ");
					localPort = Integer.parseInt(in.next());
					System.out.println("IP of the node you want to connect: ");
					connectIP = in.next();
					System.out.println("Port of the connection node: ");
					connectPort = Integer.parseInt(in.next());
					x = new Node();
					x.join(new InetSocketAddress(InetAddress.getLocalHost(),localPort),
												new InetSocketAddress(connectIP,connectPort)); } 
					break;	
				case "SEARCH" : { // Search in a node specified
					System.out.println("IP of the node to start search: ");
					connectIP = in.next();
					System.out.println("Port of the search node: ");
					connectPort = Integer.parseInt(in.next());
					System.out.println("Search string: ");
					searchStr = in.next();
					desiredDataNodeAddr =  Utilities.searchItem(
							new InetSocketAddress(connectIP,connectPort),
							Utilities.encryptString(searchStr));
					System.out.println("You can find your data with key " + searchStr 
							+ " (Encrypted= " + Utilities.longToHexString(Utilities.encryptString(searchStr)) + ") in node " + desiredDataNodeAddr.toString() + "\n"); }
					break;
				case "EXIT" : // Terminate the run
					System.exit(1); 
					break;
				default : // If the command is different from the above the program ends
					System.out.println("Wrong Input!\n"); 
					break;
			}
		}
	}
	
}
