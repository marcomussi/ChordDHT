import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Request.GetSuccListRequest;
import Request.GetSuccessorRequest;
import Request.UpdateSuccessorListRequest;

public class Node {
	
	private InetSocketAddress nodeAddr;
	private InetSocketAddress predecessorAddr;
	private Long nodeId;
	private ArrayList<InetSocketAddress> succList;
	private HashMap<Integer, FingerObject> fingerTable;
	private Scanner in;
	private Listener listenerThread;
	private Stabilize stabilizerThread;
	private FixFingers fixFingersThread;
	private CheckSuccessors checkSuccessorsThread;
	
	
	public Node () {
		fingerTable = new HashMap<Integer, FingerObject>();
		succList = new ArrayList<InetSocketAddress>(32);
		setPredecessorAddr(null);
	}
	
	public void create(InetSocketAddress thisNode) {
		// Port chosen by user and ip of the device automatically catched
		nodeAddr = thisNode;
		initFingerHashMap();
		predecessorAddr = null;
		fingerTable.get(0).setAddress(this.getNodeAddress());
		this.lauchThreads();
		this.choose();
	}
	
	public void join(InetSocketAddress thisNode,InetSocketAddress connectionNode) {
		nodeAddr = thisNode;
		initFingerHashMap();
		predecessorAddr = null;
		InetSocketAddress successorAddress = 
				Utilities.requestToNode(connectionNode, 
								new GetSuccessorRequest(nodeId));
		fingerTable.get(0).setAddress(successorAddress);
		updateSuccList(successorAddress, false);
		this.lauchThreads();
		this.choose();
	}

	private void lauchThreads() {
		try {
			listenerThread = new Listener(this);
			listenerThread.start();

			stabilizerThread = new Stabilize(this);
			stabilizerThread.start();
			
			fixFingersThread = new FixFingers(this);
			fixFingersThread.start();
			
			checkSuccessorsThread = new CheckSuccessors(this);
			checkSuccessorsThread.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void choose() {
		while(true){
			System.out.println("\nSelect the operation:\n"
				+ "INFO\n"
				+ "DELETENODE\n");
			in = new Scanner(System.in);
			String operation = in.next().toUpperCase();
			switch(operation) {
				case "INFO" : 
					this.displayInfo(); 
					break; // Shows finger table
				case "DELETENODE" : 
					System.exit(1); // 1 represent the exit in a correct way
					break; // Delete the node
				default : 
					System.out.println("\nWrong Node Command!\n"); // Ask again
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
	
	public ArrayList<InetSocketAddress> getSuccList() {
		return succList;
	}
	
	public void updateSuccList(InetSocketAddress succAddress, boolean removeLastEntry) {
		ArrayList<InetSocketAddress> succList = 
					Utilities.requestListToNode(succAddress, new GetSuccListRequest());
		@SuppressWarnings("unchecked")
		ArrayList<InetSocketAddress> oldList = 
							(ArrayList<InetSocketAddress>) this.succList.clone();
		succList.add(0, succAddress);
		if (succList.size() > 31 || removeLastEntry)
			succList.remove(succList.size()-1);
		// This is to handle the case in which a node crashed and 
		// I'm the last entry of the successor list received 
		if (succList.get(succList.size()-1).equals(nodeAddr))
			succList.remove(succList.size()-1);
		for (int i=0; i<succList.size(); i++) {
			System.out.println("Succ list entry " + i + " = " + succList.get(i));
		}
		this.succList = succList;
		if (!oldList.equals(succList) && predecessorAddr!=null && !oldList.isEmpty()) {
			Utilities.requestToNode(predecessorAddr, new UpdateSuccessorListRequest());
		}
	}

	public Long getNodeId() {
		return this.nodeId;
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
		Long auxHashValue;
		nodeId = Utilities.encryptString(nodeAddr.toString());
		for(int i=0;i<32;i++) {
			auxHashValue = ((long) Math.pow(2, i) + nodeId) 
													% (long) Math.pow(2, 32);
			this.getFingerTable().put(i, new FingerObject(null, auxHashValue));
		}
	}
	public InetSocketAddress findSuccessor(Long id) {	
		if (this.getNodeId() < Utilities.encryptString(
												getSuccessorAddress().toString())) {
			if (id>nodeId
					&& id<=Utilities.encryptString(getSuccessorAddress().toString()))
				return this.getSuccessorAddress();
			else {
					InetSocketAddress auxNode = this.closestPrecedingNode(id);
					if (auxNode.equals(this.nodeAddr)) // Base case, block deadlock
						return this.nodeAddr;
					// If another node is found, then send the request to it
					GetSuccessorRequest getSuccessorRequest = new GetSuccessorRequest(id);
					return Utilities.requestToNode(auxNode, getSuccessorRequest);
				}
		}
		else {
				if (id<=Utilities.encryptString(getSuccessorAddress().toString())
						|| (id % Math.pow(2, 32)) > getNodeId())
					return this.getSuccessorAddress();
				else {
					InetSocketAddress auxNode = this.closestPrecedingNode(id);
					if (auxNode.equals(this.nodeAddr)) // Base case, block deadlock
						return this.nodeAddr;
					// If another node is found, then send the request to it
					GetSuccessorRequest getSuccessorRequest = new GetSuccessorRequest(id);
					return Utilities.requestToNode(auxNode, getSuccessorRequest);
				}
		}
	}

	private InetSocketAddress closestPrecedingNode(Long id) {
		Long fingerEntry = 0L;
		for(int i=31;i>=0;i--) {
			if(this.getFingerTable().get(i).getAddress() != null) {
				fingerEntry = Utilities.encryptString(
								this.fingerTable.get(i).getAddress().toString());
				if (nodeId<id) {	
					if(fingerEntry>nodeId && fingerEntry<id) 
						return fingerTable.get(i).getAddress();
				}
				else
					if (fingerEntry>nodeId || fingerEntry < id)
						return fingerTable.get(i).getAddress();
			}
		}
		return this.getNodeAddress();
	}
	
}
