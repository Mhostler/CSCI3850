import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Query implements Runnable {
	private ConcurrentLinkedQueue<queryNode> found = new ConcurrentLinkedQueue<queryNode>();
	private ConcurrentLinkedQueue<FileNode> hit;
	private ConcurrentLinkedQueue<String> queries;
	private float weight;
	private Iterator<FileNode> hitIt;
	
	public Query(ConcurrentLinkedQueue<String> item) {
		queries = item;
	}
	
	public void commence(String list){
		ConcurrentHashMap<String, Node> archive = CSCI3850p0.mapping;
		ConcurrentLinkedQueue<queryNode> mergeFound = new ConcurrentLinkedQueue<queryNode>();
		String[] xyz = list.split(" ");
		for(String term : xyz) {
			System.out.println(term);
			if(archive.containsKey(term)) {
				Node alpha = archive.get(term);
				hit = alpha.getQueue();
				hitIt = hit.iterator();
				while(hitIt.hasNext()) {
					queryNode a = new queryNode();
					FileNode b = hitIt.next();
					a.setDocID(b.getFileID());
					weight = (float)b.getOccurrence() / (float)b.getWordCount();
					a.addWeight(weight);
					mergeFound.add(a);
					System.out.println(a.getDocID());
				}
				
				merger(mergeFound);
				mergeFound.clear();
			}
		}
	}
	
	//may need to be modified later
	public void printOut(ConcurrentLinkedQueue<queryNode> toPrint, long time, String query) {
		Iterator<queryNode> printIt = toPrint.iterator();
		int counter = 0;
		System.out.printf("Query '%s', time to process: %d\n", query, time);
		while (printIt.hasNext() && counter < 10) {
			System.out.printf("DocID: %s\n", printIt.next().getDocID());
			counter++;
		}
	}
	
	public void merger(ConcurrentLinkedQueue<queryNode> toMerge) {
		//for every item in tomerge if found in found add the weights together if not add the node to found
		queryNode toJudge = toMerge.poll();
		ConcurrentLinkedQueue<queryNode> ttt = new ConcurrentLinkedQueue<queryNode>();
		
		while(!toMerge.isEmpty() || !found.isEmpty()) {
			if(toMerge.isEmpty()) {
				ttt.addAll(found);
				break;
			} else if(found.isEmpty()) {
				ttt.addAll(toMerge);
				break;
			}
			
			if(toMerge.peek().getDocID().compareTo(found.peek().getDocID()) < 0) {
				ttt.add(toMerge.poll());
			} else if(toMerge.peek().getDocID().compareTo(found.peek().getDocID()) > 0) {
				ttt.add(found.poll());
			} else {
				queryNode j = found.poll();
				j.addWeight(toMerge.poll().getWeight());
				ttt.add(j);
			}
		}
		found = ttt;
	}
	
	public ConcurrentLinkedQueue<queryNode> processQueue(ConcurrentLinkedQueue<queryNode> a) {
		queryNode[] arrs = new queryNode[11];
		ConcurrentLinkedQueue<queryNode> sorted = new ConcurrentLinkedQueue<queryNode>(); //change this so it returns something
		int count = 0;
		
		for(int i = 0; i < arrs.length; i++) {
			arrs[i] = new queryNode();
			arrs[i].setWeight(0f);
		}
		for(queryNode v : a) {
			if(v == null) {
				continue;
			}
			arrs[10] = v;
			arrs = sortHelp(arrs);
			
		}
		for(queryNode k : arrs) {
			sorted.add(k);
		}
		
		return sorted;
	}
	
	//helper method
	public queryNode[] sortHelp(queryNode[] toSort) {
		//insertion sort implementation
		for(int l = (toSort.length - 1); l > 0 ; l--) {
			if(toSort[l].getWeight() > toSort[l - 1].getWeight()) {
				queryNode temp = toSort[l];
				toSort[l] = toSort[l - 1];
				toSort[l - 1] = temp;
			}
		}
		return toSort;
	}
	
	public void run() {
		String nextUp;
		while(!queries.isEmpty()) {
			long timeStart = System.currentTimeMillis();
			nextUp = queries.poll();
			commence(nextUp);
			found = processQueue(found);
			long timestop = System.currentTimeMillis() - timeStart;
			printOut(found, timestop, nextUp);
		}
		
	}
	
}

