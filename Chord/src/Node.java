import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Scanner;

public class Node {
	
	private InetSocketAddress nodeAddr;
	private InetSocketAddress predecessorAddr;
	
	private HashMap<Integer, InetSocketAddress> fingerTable;
	private Scanner in;
	private Listener listenerThread;
	private Stabilize stabilizerThread;
	
	public Node () {
		fingerTable = new HashMap<Integer, InetSocketAddress>();
		predecessorAddr = null;
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
		
		predecessorAddr = this.askTo(connectionNode);
		this.initializeNode();
		this.choose();
	}
	
	private InetSocketAddress askTo(InetSocketAddress connectionNode) {
		// TODO Auto-generated method stub
		//send request and see response
		//id is given by the hash of my address
		// i must receive 
		return null;
	}

	private void initializeNode() {
		listenerThread = new Listener(this);
		stabilizerThread = new Stabilize(this);
		//PRED
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

	public HashMap<Integer, InetSocketAddress> getFingerTable() {
		return fingerTable;
	}

	public void setFingerTable(HashMap<Integer, InetSocketAddress> fingerTable) {
		this.fingerTable = fingerTable;
	}


}
