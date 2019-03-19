import java.io.IOException;
import java.net.InetSocketAddress;

import Request.GetPredecessorRequest;
import Request.NotifyRequest;

public class Stabilize extends Thread {
	
	private Node node;

	public Stabilize(Node n) throws IOException {
		this.node = n;
	}
	
	@Override
	public void run() {
		System.out.println("Started stabilize process\n" 
				+ node.getNodeAddress() + "\n");
		int sleepTimeMillis = 5000;
		while(true) {
			try {
				System.out.println("My predecessor is " + node.getPredecessorAddr());
				Thread.sleep(sleepTimeMillis);
				// Get the node successor
				InetSocketAddress nodeSucc = node.getSuccessorAddress();
				// If the successor is still null, then continue
				if (nodeSucc==null) 
					continue;
				// Get the node successor's predecessor (=x)
				InetSocketAddress nodeSuccPredecessor = Utilities.requestToNode(nodeSucc, new GetPredecessorRequest());
				// If it's null, hash it in order to obtain its interval upper bound
				Long nodeSuccPredecessorIntervalUpperBound;
				if (nodeSuccPredecessor!=null)
					// Se sono il successore di me stesso e ricevo una notify, aggiorno il mio successore con quello ricevuto
					if (node.getSuccessorAddress()==node.getNodeAddress()) 
						node.getFingerTable().get(0).setAddress(nodeSuccPredecessor);
					// Se ho ottenuto una notify con x � (n , successor) 
					else {
						nodeSuccPredecessorIntervalUpperBound = Utilities.encryptString(nodeSuccPredecessor.toString());
						if ((nodeSuccPredecessorIntervalUpperBound > node.getNodeUpperBound() 
						&& nodeSuccPredecessorIntervalUpperBound < node.getSuccessorIntervalUpperBound()))
							node.getFingerTable().get(0).setAddress(nodeSuccPredecessor);
					}
				//TODO: Verificare se notify fatta così potrebbe generare risultati 
				// strani in quanto non è sincronizzata con niente
				if (node.getSuccessorAddress() != node.getNodeAddress())
					Utilities.requestToNode(node.getSuccessorAddress(), new NotifyRequest(node.getNodeAddress()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			};
		}
	}
}