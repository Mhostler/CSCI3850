import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Formatter;

public class Query {
	private static ConcurrentLinkedQueue<Node> results;
	private static ConcurrentLinkedQueue<queryNode> found;
	private static ConcurrentLinkedQueue<FileNode> aaaa;
	private static float weight;
	private static Iterator<FileNode> it;
	
	public static ConcurrentLinkedQueue<Node> commence(String list, ConcurrentLinkedQueue<Node> archive){
		Node[] abc = (Node[]) archive.toArray();
		String[] xyz = list.split(" ");
		for (int m = 0; m < abc.length; m++) { //for every element of abc archive
			for (int n = 0; n < xyz.length; n++) { //for every element of xyz which is split of list
				if(abc[m].getKeyword() == xyz[n]) { //if it matches
					aaaa = abc[m].getQueue();
					it = abc[m].getQueue().iterator();
					while(it.hasNext()) {
						queryNode a = new queryNode(); // gather all files and calculate weight for each file
						a.setDocID(it.next().getFileID());
						
						// add them to queue in order of highest weight
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
}
