import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import Request.Request; 

public class Utilities {
	// Method to calculate the SHA-1 of a given input string
	public static Long encryptString(String input) {
        try { 
            // getInstance() method is called with algorithm SHA-1 
            MessageDigest md = MessageDigest.getInstance("SHA-1"); 
            // digest() method is called to calc message digest of the input string 
            // returned as array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
            // Add preceding 0s to make it 32 bit 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            // return the last 8 characters of the HashText 
            return Long.parseLong(hashtext.substring(hashtext.length() - 8),16); 
        }
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        }
	}
	
	public static String longToHexString(long l) {
		return Long.toHexString(l);
	}
	
	public static void searchItem(InetSocketAddress socketAddr, Long key) {
		
	}
	
	public static void displayFingerTable(Node inputNode){
		HashMap<Integer, FingerObject> fingerTable = inputNode.getFingerTable();
		int size = fingerTable.size();
		System.out.println("Displaying finger table for node: " + longToHexString(inputNode.getNodeUpperBound()));
		System.out.println("i\tHashCurrentNode+2^i\tsuccessor");
		for(int i=0 ; i < size; i++) {
			System.out.println(i + "\t" 
					+ longToHexString(fingerTable.get(i).getIntervalUpperbound()) 
					+ "\t\t\t" +  fingerTable.get(i).getAddress());
		}
	}
	
	public static InetSocketAddress requestToNode(InetSocketAddress destination, Request request){
		//System.out.println("RequestToNode invocata");
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
		} catch (ConnectException e) {
			System.out.println("A problem occur while contacting " + destination);
		} catch (IOException e) {	
			e.printStackTrace();
		} catch (ClassNotFoundException e) {	
			e.printStackTrace();
		}
		return null;		
	}
	
	public static ArrayList<InetSocketAddress> requestListToNode(InetSocketAddress destination, Request request){
		//System.out.println("RequestToNode invocata");
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
			ArrayList<InetSocketAddress> result = (ArrayList<InetSocketAddress>) objectInputStream.readObject();
			return result;
		} catch (IOException e) {	
			e.printStackTrace();
		} catch (ClassNotFoundException e) {	
			e.printStackTrace();
		}
		return null;		
	}


}
