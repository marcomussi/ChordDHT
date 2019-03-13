import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Scanner;

public class Node {
	// DECLARATION
	private InetSocketAddress nodeAddr;
	// private HashMap<Integer, InetSocketAddress> fingerTable;
	private Scanner in;
	
	public Node () {
		nodeAddr = new InetSocketAddress(-1);
		// fingerTable = new HashMap<Integer, InetSocketAddress>();
	}
	
	public void newChord(InetSocketAddress thisNode) {
		// port choosen by user and ip of the device automatically catched
		this.initializeNode();
		this.choose();
	}
	
	public void joinNetwork(InetSocketAddress thisNode,InetSocketAddress connectionNode) {
		//TO DO
		//otteniamo le info
		//gen
		this.initializeNode();
		this.choose();
	}
	
	private void initializeNode() {
		// TODO Auto-generated method stub
		
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

	public static void searchItem() {
		// TODO Auto-generated method stub
		// method in the main menu part will be developed here here
	}

	private void displayInfo() {
		// TODO Auto-generated method stub
		System.out.println("\nNode finger table:\n");
		// System.out.println(fingerTable);
	}
}
