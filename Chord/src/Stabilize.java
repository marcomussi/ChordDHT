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
		int sleepTimeMillis = 500;
		while(true) {
			try {
				//System.out.println("My predecessor is " + node.getPredecessorAddr());
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
				// Se sono il successore di me stesso e ricevo una notify, aggiorno il mio successore con quello ricevuto
				if (node.getSuccessorAddress()==node.getNodeAddress()) {
					node.getFingerTable().get(0).setAddress(nodeSuccPredecessor);
					node.updateSuccList(nodeSuccPredecessor, true);
				}
					// Se ho ottenuto una notify con x € (n , successor) 
				else {
					nodeSuccPredecessorIntervalUpperBound = Utilities.encryptString(nodeSuccPredecessor.toString());
					if (node.getNodeUpperBound() < Utilities.encryptString(node.getSuccessorAddress().toString())) {
						if ((nodeSuccPredecessorIntervalUpperBound > node.getNodeUpperBound() 
						&& nodeSuccPredecessorIntervalUpperBound < Utilities.encryptString(node.getSuccessorAddress().toString()))) {
							node.getFingerTable().get(0).setAddress(nodeSuccPredecessor);
							node.updateSuccList(nodeSuccPredecessor, true);
						}
					}
					else
						if (nodeSuccPredecessorIntervalUpperBound > node.getNodeUpperBound()
								|| nodeSuccPredecessorIntervalUpperBound < node.getNodeUpperBound()) {
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