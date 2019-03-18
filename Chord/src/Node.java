import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Scanner;
import Request.GetSuccessorRequest;

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
		setPredecessorAddr(null);
	}
	
	public void create(InetSocketAddress thisNode) {
		// port choosen by user and ip of the device automatically catched
		nodeAddr = thisNode;
		initFingerHashMap();
		predecessorAddr = null;
		this.lauchThreads();
		this.choose();
	}
	
	public void join(InetSocketAddress thisNode,InetSocketAddress connectionNode) {
		nodeAddr = thisNode;
		initFingerHashMap();
		predecessorAddr = null;
		InetSocketAddress successorAddress = Utilities.requestToNode(connectionNode, new GetSuccessorRequest(currentIntervalUpperBound));
		fingerTable.get(0).setAddress(successorAddress);
		this.lauchThreads();
		this.choose();
	}

	private void lauchThreads() {
		try {
			listenerThread = new Listener(this);
			listenerThread.start();

			stabilizerThread = new Stabilize(this);
			stabilizerThread.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		//PRED
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
		Utilities.displayFingerTable(this);
	}

	public InetSocketAddress getNodeAddress() {
		return nodeAddr;
	}

	public Long getNodeUpperBound() {
		return this.currentIntervalUpperBound;
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
	
	public InetSocketAddress getPredecessorAddr() {
		return predecessorAddr;
	}

	public void setPredecessorAddr(InetSocketAddress predecessorAddr) {
		this.predecessorAddr = predecessorAddr;
	}
	public void initFingerHashMap() {
		/* fingerTable must be init because in this way we can use 
		 * always "get" and "set" commands in the stabilize instead
		 * of the "put", this allows to avoid the distiction between
		 * the first and the others stabilize calls */
		Long auxHashValue;
		currentIntervalUpperBound = Utilities.encryptString(nodeAddr.toString());
		for(int i=0;i<32;i++) {
			auxHashValue = (long) Math.pow(2, i) + currentIntervalUpperBound;
			this.getFingerTable().put(i, new FingerObject(null, auxHashValue));
		}
	}
	public InetSocketAddress findSuccessor(Long id) {
		if (id>currentIntervalUpperBound
				&& id<=this.getSuccessorIntervalUpperBound()) {
			return this.getSuccessorAddress();
		}
		InetSocketAddress auxNode = this.closestPrecedingNode(id);
		if (auxNode==this.getNodeAddress()) // Base case, in order to block deadlock
			return this.getNodeAddress();
		// If another node is found, then send the request to it
		GetSuccessorRequest getSuccessorRequest = new GetSuccessorRequest(id);
		return Utilities.requestToNode(auxNode, getSuccessorRequest);
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
