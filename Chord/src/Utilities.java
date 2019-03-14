import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import java.util.HashMap; 

public class Utilities {
	// Method to calculate the SHA-1 of a given input string
	public static String encryptString(String input) {
        try { 
            // getInstance() method is called with algorithm SHA-1 
            MessageDigest md = MessageDigest.getInstance("SHA-1"); 
            // digest() method is called to calculate message digest of the input string 
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
            return hashtext.substring(hashtext.length() - 8); 
        }
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        }
	}
	
	public static void searchItem() {
		// TODO Auto-generated method stub
		// method in the main menu part will be developed here here
	}
	
	public static void displayFingerTable(Node inputNode){
		String encryptedAddress = Utilities.encryptString(inputNode.getNodeAddress().toString());
		Long encryptedAddressLong = Long.parseLong(encryptedAddress, 16);
		HashMap<Integer, InetSocketAddress> fingerTable = inputNode.getFingerTable();
		int size = fingerTable.size();
		System.out.println("i   hash(current node) + 2^i   successor");
		Long currentHash;
		for(int i=0 ; i < size; i++) {
			currentHash = (long) Math.pow(2, i) + encryptedAddressLong;
			System.out.println(i + "   " + Long.toHexString(currentHash) 
							+ "                  " +  fingerTable.get(i));
		}
	}
	
	public static void initFingerHashMap(HashMap<Integer,InetSocketAddress> hmap, int upperbound,InetSocketAddress socketAddr) {
		// upperbound not included
		// upperbound 32: 0 to 31
		for(int i=0;i<upperbound;i++)
			hmap.put(i, socketAddr);
	}
	
	
	
	
	
	/*
	public static void main(String[] args){
		Node prova = new Node();
		prova.setNodeAddr(new InetSocketAddress("127.0.0.1", 54));
		HashMap<Integer, InetSocketAddress> finger = new HashMap<>();
		prova.setFingerTable(finger);
		finger.put(0, new InetSocketAddress("127.0.0.1", 54));
		finger.put(1, new InetSocketAddress("127.0.0.1", 54));
		finger.put(2, new InetSocketAddress("127.0.0.1", 54));
		Utilities.displayFingerTable(prova);
	}
	*/
	
}
