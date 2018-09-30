import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Query {
	private static ConcurrentLinkedQueue<queryNode> found;
	private static ConcurrentLinkedQueue<FileNode> hit;
	private static float weight;
	private static Iterator<FileNode> it;
	private static Iterator<Node> bigIt;
	private static Iterator<queryNode> littleIt;
	
	public static ConcurrentLinkedQueue<queryNode> commence(String list, ConcurrentLinkedQueue<Node> archive){
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
		found = processQueue(found);
		return found;
	}
	
	//may need to be modified later
	public static void printOut(ConcurrentLinkedQueue<queryNode> toPrint, int time, String query) {
		Iterator<queryNode> printIt = toPrint.iterator();
		int counter = 0;
		System.out.printf("Query '%s', time to process: %d\n", query, time);
		while (printIt.hasNext() && counter < 10) {
			System.out.printf("DocID: %s\n", printIt.next().getDocID());
			counter++;
		}
	}
	
	public static ConcurrentLinkedQueue<queryNode> processQueue(ConcurrentLinkedQueue<queryNode> a) {
		queryNode[] arrs = new queryNode[10];
		ConcurrentLinkedQueue<queryNode> sorted = null;
		Iterator<queryNode> it = a.iterator();
		int count = 0;
		
		while(it.hasNext()) {
			if(count < 9) {
				arrs[count] = it.next();
				count++;
			} 
			if(count == 9) {
				arrs[count] = it.next();
				arrs = sortHelp(arrs);
				count++;
			} else if(it.next().getWeight() > arrs[0].getWeight()) {
				arrs[0] = it.next();
				arrs = sortHelp(arrs);
			} 
		}
		
		return sorted;
	}
	
	//helper method
	public static queryNode[] sortHelp(queryNode[] toSort) {
		//insertion sort implementation
		for(int l = 1; l<10 ; ++l) {
			float key = toSort[l].getWeight();
			int j = l - 1;
			
			while (j>=0 && toSort[j].getWeight() > key) {
				toSort[j+1] = toSort[j];
				j = j-1;
			}
			toSort[j+1] = toSort[l];
		}
		return toSort;
	}
	
}

