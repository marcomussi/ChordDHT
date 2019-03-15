import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import java.util.HashMap; 

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
	
	public static void searchItem(InetSocketAddress socketAddr, Long key) {
		
		// method in the main menu part will be developed here here
	}
	
	public static void displayFingerTable(Node inputNode){
		HashMap<Integer, FingerObject> fingerTable = inputNode.getFingerTable();
		int size = fingerTable.size();
		System.out.println("i   hash(current node) + 2^i   successor");
		for(int i=0 ; i < size; i++) {
			System.out.println(i + "   " + fingerTable.get(i).getIntervalUpperbound() 
							+ "                  " +  fingerTable.get(i).getAddress());
		}
	}
	
}
