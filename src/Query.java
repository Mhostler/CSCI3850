import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Formatter;

public class Query {
	private static ConcurrentLinkedQueue<Node> results;
	private static ConcurrentLinkedQueue<queryNode> found;
	private static ConcurrentLinkedQueue<FileNode> hit;
	private static float weight;
	private static Iterator<FileNode> it;
	private static Iterator<Node> bigIt;
	private static Iterator<queryNode> littleIt;
	
	public static ConcurrentLinkedQueue<Node> commence(String list, ConcurrentLinkedQueue<Node> archive){
		bigIt = archive.iterator();
		String[] xyz = list.split(" ");
		for (int m = 0; m < xyz.length; m++) { //for every element of abc archive
			while (bigIt.hasNext()) { //for every element of xyz which is split of list
				if(bigIt.next().getKeyword() == xyz[m]) { //if it matches
					hit = bigIt.next().getQueue();
					it = hit.iterator();
					while(it.hasNext()) {
						queryNode a = new queryNode(); // gather all files and calculate weight for each file
						a.setDocID(it.next().getFileID());
						weight = (float)it.next().getOccurrence() / (float)it.next().getWordCount();
						a.addWeight(weight);
						// add them to queue in order of highest weight
						if(found.contains(a)) {
							littleIt = found.iterator();
							while(littleIt.hasNext()) {
								if(littleIt.next().equals(a)) {
									littleIt.next().addWeight(weight);
									break;
								}
							}
						}
						else {
							found.add(a);
						//TODO: process the results after the loop is finally complete
						}
					}
				}
			}
			
		}
		return results;
	}
	
	public static float findWeight() {
		float result = 0;
		return result;
	}
	
	//may need to be modified later
	public static void printOut(ConcurrentLinkedQueue<queryNode> toPrint, int time, String query) {
		Iterator<queryNode> printIt = toPrint.iterator();
		System.out.printf("Query '%s', time to process: %d\n", query, time);
		while (printIt.hasNext()) {
			System.out.printf("DocID: %s\n", printIt.next().getDocID());
		}
	}
	
	public static void processQueue() {
		//processes the queue so that DocIDs are matched and total weight is calculated
		//also calls printout
	}
	
}

