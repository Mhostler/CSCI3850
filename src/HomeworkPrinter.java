import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HomeworkPrinter {

	private static long processTime = 0;
	
	private static Node [] highest = new Node[10];
	private static Node [] lowest = new Node[10];
	private static ConcurrentLinkedQueue<Node> dictionary;
	
	public static void setQueue( ConcurrentLinkedQueue<Node> clq ) { dictionary = clq; }
	public static void setTime( long pT ) { processTime = pT; }
	
	public static void printHomework() {
		int tokens = dictionary.size();
		
		String file;
		BufferedWriter writer;
		int runType = CSCI3850p0.getRunType();
		
		if( runType == 0 ) { file = "hw_nocull.txt"; }
		else if( runType == 1 ) { file = "hw_Stop.txt"; }
		else if( runType == 2 ) { file = "hw_StopStem.txt"; }
		else {
			file = "err";
			System.out.println("Error with runType in HomeworkPrinter");
		}
		
		FindHighest.setDictionary( dictionary );
		highest = FindHighest.findHighest();
		lowest = FindHighest.findLowest();
		
		try {
			writer = new BufferedWriter( new FileWriter( file ) );
			
			writer.write(" Unique Terms: " + Integer.toString(tokens) + "\n" );
			writer.write(" Program Time: " + Long.toString(processTime) + "\n\n" );
			writer.write( "Most Frequent Terms: \n" );
			
			for( Node n : highest ) {
				writer.write("  " + n.getKeyword() + " : " + Integer.toString( n.getOccurrence() ) + "\n" );
			}
			
			writer.newLine();
			writer.write( "Least Frequent Terms: \n" );
			
			for( Node n : lowest ) {
				writer.write("  " + n.getKeyword() + " : " + Integer.toString( n.getOccurrence() ) + "\n" );
			}
			
			writer.close();
		} catch (IOException e) {
			System.out.println( "Failed to open " + file );
			e.printStackTrace();
		}
	}
	
}
