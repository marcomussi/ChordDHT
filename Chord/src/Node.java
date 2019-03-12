import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Scanner;

public class Node {
	// DECLARATION
	private InetSocketAddress nodeAddr;
	private HashMap<Integer, InetSocketAddress> fingerTable;
	
	public Node () {
	}
	
	public void newChord(int destPort) {
		//TO DO
		this.choose();
	}
	
	public void joinNetwork(int destPort, int connectIP, int connectPort) {
		//TO DO
		this.choose();
	}
	
	public int assignID(InetSocketAddress newNodeAddr) {
		return -1;
	}
	
	private void choose() {
		while(true){
			System.out.println("Select the operation:\n"
				+ "INFO\n"
				+ "DELETENODE\n");
			Scanner in = new Scanner(System.in);
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
		System.out.println(fingerTable);
	}
}
