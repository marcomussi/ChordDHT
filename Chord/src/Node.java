import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Scanner;

public class Node {
	
	private InetSocketAddress nodeAddr;
	private InetSocketAddress predecessorAddr;
	private Long currentIntervalUpperBound;
	
	private HashMap<Integer, FingerObject> fingerTable;
	private Scanner in;
	private Listener listenerThread;
	private Stabilize stabilizerThread;
	
	public Node () {
		fingerTable = new HashMap<Integer, FingerObject>();
		Utilities.initFingerHashMap(fingerTable,32,null);
		predecessorAddr = null;
	}
	
	public void create(InetSocketAddress thisNode) {
		// port choosen by user and ip of the device automatically catched
		nodeAddr = thisNode;
		currentIntervalUpperBound = Utilities.encryptString(this.nodeAddr.toString());
		this.lauchThreads();
		this.choose();
	}
	
	public void join(InetSocketAddress thisNode,InetSocketAddress connectionNode) {
		nodeAddr = thisNode;
		currentIntervalUpperBound = Utilities.encryptString(this.nodeAddr.toString());
		//request of the ID from by socket request
		//fingerTable.put(0, connectionNode);
		//SISTEMARE
		predecessorAddr = this.askTo(connectionNode);
		this.lauchThreads();
		this.choose();
	}
	
	private InetSocketAddress askTo(InetSocketAddress connectionNode) {
		//send request and see response
		//id is given by the hash of my address
		// i must receive 
		return null;
	}

	private void lauchThreads() {
		try {
			listenerThread = new Listener(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public HashMap<Integer, FingerObject> getFingerTable() {
		return fingerTable;
	}

	public void setFingerTable(HashMap<Integer, FingerObject> fingerTable) {
		this.fingerTable = fingerTable;
	}

	public InetSocketAddress getSuccessorAddress() {
		return fingerTable.get(0).getAddress();
	}
	
	public Long getSuccessorIntervalUpperBound() {
		return fingerTable.get(0).getIntervalUpperbound();
	}
	
	public InetSocketAddress findSuccessor(Long id) {
		if (id<currentIntervalUpperBound
				&& id>=this.getSuccessorIntervalUpperBound()) {
			return this.getSuccessorAddress();
		}
		InetSocketAddress auxNode = this.closestPrecedingNode(id);
		//CONNESSIONE AD AUXNODE
		return this.findSuccessor(id);
	}

	private InetSocketAddress closestPrecedingNode(Long id) {
		for(int i=31;i>=0;i--) {
			if(this.fingerTable.get(i).getIntervalUpperbound()>this.currentIntervalUpperBound 
					&& this.fingerTable.get(i).getIntervalUpperbound()<id) {
				fingerTable.get(i).getAddress();
			}
		}
		return this.getNodeAddress();
	}
	

}
