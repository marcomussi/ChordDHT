import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import Request.CheckStatusRequest;
import Request.GetPredecessorRequest;
import Request.NotifyRequest;

public class Stabilize extends Thread {
	
	private Node node;
	private Socket socket;
	private OutputStream output;
	private InputStream input;
	private ObjectOutputStream objectOutputStream;
	private CheckStatusRequest checkPredReq;
	
	public Stabilize(Node n) throws IOException {
		this.node = n;
	}
	
	@Override
	public void run() {
		System.out.println("Started stabilize process\n" 
				+ node.getNodeAddress() + "\n");
		int sleepTimeMillis = 300;
		while(true) {
			try {
				Thread.sleep(sleepTimeMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			};
			if (node.getPredecessorAddr() != null) {
				try {
					socket = new Socket(node.getPredecessorAddr().getAddress(),node.getPredecessorAddr().getPort());
					socket.setSoTimeout(1000);
					output = socket.getOutputStream();
					objectOutputStream = new ObjectOutputStream(output);
					checkPredReq = new CheckStatusRequest();
					objectOutputStream.writeObject(checkPredReq);
					input = socket.getInputStream();
					ObjectInputStream objectInputStream = new ObjectInputStream(input);
					objectInputStream.readObject();
				} catch (SocketTimeoutException|ConnectException e) {
					System.out.println("A problem occurs while contacting the predecessor. A new predecessor will be assigned soon");
					node.setPredecessorAddr(null);
				} catch (IOException e) {	
					e.printStackTrace();
				} catch (ClassNotFoundException e) {	
					e.printStackTrace();
				}
			}
			// Get the node successor
			InetSocketAddress nodeSucc = node.getSuccessorAddress();
			// Get the node successor's predecessor (=x)
			InetSocketAddress nodeSuccPredecessor = Utilities.requestToNode(nodeSucc, new GetPredecessorRequest());
			Long nodeSuccPredecessorIntervalUpperBound;
			if (nodeSuccPredecessor!=null) {
				// If I'm the successor of myself and I receive a notify, I update my successor with the one received
				if (node.getSuccessorAddress()==node.getNodeAddress()) {
					node.getFingerTable().get(0).setAddress(nodeSuccPredecessor);
					node.updateSuccList(nodeSuccPredecessor, true);
				}
					// If I've obtained a notify with x € (n , successor) 
				else {
					nodeSuccPredecessorIntervalUpperBound = Utilities.encryptString(nodeSuccPredecessor.toString());
					if (node.getNodeId() < Utilities.encryptString(node.getSuccessorAddress().toString())) {
						if ((nodeSuccPredecessorIntervalUpperBound > node.getNodeId() 
						&& nodeSuccPredecessorIntervalUpperBound < Utilities.encryptString(node.getSuccessorAddress().toString()))) {
							node.getFingerTable().get(0).setAddress(nodeSuccPredecessor);
							node.updateSuccList(nodeSuccPredecessor, true);
						}
					}
					else
						if (nodeSuccPredecessorIntervalUpperBound > node.getNodeId()
								|| nodeSuccPredecessorIntervalUpperBound < node.getNodeId()) {
							node.getFingerTable().get(0).setAddress(nodeSuccPredecessor);
							node.updateSuccList(nodeSuccPredecessor, true);
						}
					}
				}
				if (!node.getSuccessorAddress().equals(node.getNodeAddress())) {
					Utilities.requestToNode(node.getSuccessorAddress(), new NotifyRequest(node.getNodeAddress()));
				}
		}
	}
}