import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import Request.CheckStatusRequest;
import Request.GetPredecessorRequest;
import Request.GetSuccListRequest;
import Request.GetSuccessorRequest;
import Request.NotifyRequest;

public class ManageRequest extends Thread{

	private Socket socket;
	private Node node;
	private InputStream input;
	private OutputStream output;

	public ManageRequest(Node n,Socket s) {
		this.node = n;
		this.socket = s;
	}
	
	@Override
	public void run() {
		try {
			input = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(input);
			process(objectInputStream.readObject());
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String process(Object request) {
		ObjectOutputStream objectOutputStream;
		InetSocketAddress response = null;
		ArrayList<InetSocketAddress> responseList = null;
		if(request instanceof GetSuccListRequest) {
			try {
				responseList = node.getSuccList();
				output = socket.getOutputStream();
				objectOutputStream = new ObjectOutputStream(output);
				objectOutputStream.writeObject(responseList);
			} catch (IOException e) {	
				e.printStackTrace();
			}
			return null;
		}
		if(request instanceof GetSuccessorRequest) {
			response = node.findSuccessor(((GetSuccessorRequest) request).getId());
		}
		if(request instanceof GetPredecessorRequest) {
			response = node.getPredecessorAddr();
		}
		if(request instanceof NotifyRequest) {
			System.out.println("Sono il nodo " + node.getNodeAddress() + " e ho ricevuto una richiesta di notify");
			InetSocketAddress sourceAddress = ((NotifyRequest) request).getAddress();
			Long sourceAddressIntervalUpperBound = Utilities.encryptString(sourceAddress.toString());
			if (node.getPredecessorAddr() == null)
				node.setPredecessorAddr(sourceAddress);
			else {
				if (Utilities.encryptString(node.getPredecessorAddr().toString()) < node.getNodeUpperBound()) {
					if ((sourceAddressIntervalUpperBound > Utilities.encryptString(node.getPredecessorAddr().toString())
								&& sourceAddressIntervalUpperBound < node.getNodeUpperBound()))
						node.setPredecessorAddr(sourceAddress);
				}
				else {
					if ((sourceAddressIntervalUpperBound > Utilities.encryptString(node.getPredecessorAddr().toString())
							|| sourceAddressIntervalUpperBound < node.getNodeUpperBound()))
					node.setPredecessorAddr(sourceAddress);
				}
			}
		}
		if(request instanceof CheckStatusRequest) {
			response = node.getNodeAddress();
		}
		try {
			output = socket.getOutputStream();
			objectOutputStream = new ObjectOutputStream(output);
			objectOutputStream.writeObject(response);
		} catch (IOException e) {	
			e.printStackTrace();
		}
		return null;
	}
	
}
