import java.util.Scanner;

public class Node {
	// DECLARATION
	public void newChord(/*1 PARAM*/) {
		//TO DO
		this.choose();
	}
	
	public void joinNetwork(/*3 PARAMS*/) {
		//TO DO
		this.choose();
	}
	
	private void choose() {
		while(true){
			System.out.println("Select the operation:\n"
				+ "INFO\n"
				+ "DELETENODE\n");
			Scanner in = new Scanner(System.in);
			String operation = in.next().toUpperCase();
			switch(operation){
			case "INFO" : this.displayInfo(); break; //crea rete
			case "DELETENODE" : System.exit(1); break; //cancella
			default : System.exit(-1); break;
			}
			}
	}

	public static void searchItem() {
		// TODO Auto-generated method stub
		//WE CANNOT USE this KEYWORD
	}

	private void displayInfo() {
		// TODO Auto-generated method stub
		//PRINT FINGERTABLE
	}
}
