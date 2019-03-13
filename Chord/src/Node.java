import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Scanner;

public class Node {
	// DECLARATION
	private InetSocketAddress nodeAddr;
	private HashMap<Integer, InetSocketAddress> fingerTable;
	private Scanner in;
	
	public Node () {
		nodeAddr = new InetSocketAddress(1);
		fingerTable = new HashMap<Integer, InetSocketAddress>();
	}
	
	public void newChord(InetSocketAddress thisNode) {
		// port choosen by user and ip of the device automatically catched
		nodeAddr = thisNode;
		this.initializeNode();
		this.choose();
	}
	
	public void joinNetwork(InetSocketAddress thisNode,InetSocketAddress connectionNode) {
		nodeAddr = thisNode;
		//request of the ID from by socket request
		this.initializeNode();
		this.choose();
	}
	
	private void initializeNode() {
		// Stabilize process
		// Listener socket
		
	}

	private int assignID(InetSocketAddress newNodeAddr) {
		return -1;
	}
	
	private void choose() {
		while(true){
			System.out.println("Select the operation:\n"
				+ "INFO\n"
				+ "DELETENODE\n");
			in = new Scanner(System.in);
			String operation = in.next().toUpperCase();
			switch(operation) {
				case "INFO" : 
					this.displayInfo(); 
					break; // shows finger table
				case "DELETENODE" : 
					System.exit(1); // 1 represent the exit in a correct way
					break; // delete the node
				default : 
					System.out.println("\nWrong Node Command!\n"); // ask again
					break;
			}
		}
	}

	private void displayInfo() {
		System.out.println("\nNode finger table:\n");
		System.out.println(fingerTable);
	}

	public InetSocketAddress getNodeAddress() {
		return nodeAddr;
	}


}
