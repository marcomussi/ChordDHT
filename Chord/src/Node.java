import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import Request.GetSuccessorRequest;
import Request.Request;

public class Node {
	
	private InetSocketAddress nodeAddr;
	private InetSocketAddress predecessorAddr;
	public Long currentIntervalUpperBound;
	
	private HashMap<Integer, FingerObject> fingerTable;
	private Scanner in;
	private Listener listenerThread;
	private Stabilize stabilizerThread;
	
	public Node () {
		fingerTable = new HashMap<Integer, FingerObject>();
		predecessorAddr = null;
	}
	
	public void create(InetSocketAddress thisNode) {
		// port choosen by user and ip of the device automatically catched
		nodeAddr = thisNode;
		currentIntervalUpperBound = Utilities.encryptString(this.nodeAddr.toString());
		Utilities.initFingerHashMap(fingerTable,32,currentIntervalUpperBound);
		this.lauchThreads();
		this.choose();
	}
	
	public void join(InetSocketAddress thisNode,InetSocketAddress connectionNode) {
		nodeAddr = thisNode;
		currentIntervalUpperBound = Utilities.encryptString(this.nodeAddr.toString());
		//request of the ID from by socket request
		//fingerTable.put(0, connectionNode);
		//SISTEMARE
		Utilities.initFingerHashMap(fingerTable,32,currentIntervalUpperBound);
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
		GetSuccessorRequest getSuccessorRequest = new GetSuccessorRequest(id);
		return requestToNode(auxNode, getSuccessorRequest);
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

	private InetSocketAddress requestToNode(InetSocketAddress destination, Request request){
		Socket socket;
		OutputStream output;
		InputStream input;
		ObjectOutputStream objectOutputStream;
		try {
			socket = new Socket(destination.getAddress(), destination.getPort());
			output = socket.getOutputStream();
			objectOutputStream = new ObjectOutputStream(output);
			objectOutputStream.writeObject(request);
			input = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(input);
			InetSocketAddress result = (InetSocketAddress) objectInputStream.readObject();
			return result;
		} catch (IOException e) {	
			e.printStackTrace();
		} catch (ClassNotFoundException e) {	
			e.printStackTrace();
		}
		return null;		
	}
}
