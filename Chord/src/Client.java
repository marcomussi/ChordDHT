import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Node x = new Node();
		System.out.println("Select the operation:\n"
				+ "CREATE\n"
				+ "JOIN\n"
				+ "SEARCH\n");
		Scanner in = new Scanner(System.in);
		String operation = in.next().toUpperCase();
		switch(operation){
		case "CREATE" : x.newChord(); break; //crea rete
		case "JOIN" : x.joinNetwork(); break; //con il nodo attuale		
		case "SEARCH" : Node.searchItem(); break; //con il nodo attuale
		default : System.exit(1); break;
		}
	}

}
