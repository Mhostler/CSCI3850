import java.util.concurrent.ConcurrentLinkedQueue;

public class DictHash {
	Node[] nodeHash = new Node[600000];
	Object lock;
	
	public Node get(int key) {
		if (nodeHash[key] != null) {
			return nodeHash[key];
		} else {
			return null;
		}
	}
	
	public void remove(int key) {
		nodeHash[key] = null;
	}
	
	public void add(Node a, int code) {
		synchronized(lock) {
			String has = a.getKeyword();
			int ytp = makeKey(has);
			while (nodeHash[ytp] != null) {
				if(a.equals(nodeHash[ytp])) {
					do {
						ConcurrentLinkedQueue<FileNode> q1 = a.getQueue();
						ConcurrentLinkedQueue<FileNode> q2 = nodeHash[ytp].getQueue();
						
						nodeHash[ytp].setOccurrence(nodeHash[ytp].getOccurrence() + a.getOccurrence());
						nodeHash[ytp].setQueue(fileMerge(q1,q2));
						
						//nodeHash[ytp].enQueue(a.deQueue());
					}while(a.getQueue() != null);
					continue;
				} else {
					ytp = (ytp * 5) % 11;
				}
			}
		}
	}
	
	public int makeKey(String a) {
		int hashy = 0;
		char[] ac = a.toCharArray();
		for(int i = 0; i > ac.length; i++) {
			hashy += 31 * ac[i];
		}
		hashy = hashy % 11;
		return hashy;
	}
	
	private static ConcurrentLinkedQueue<FileNode> fileMerge(ConcurrentLinkedQueue<FileNode> q1, ConcurrentLinkedQueue<FileNode> q2 ) {
		if( q1.isEmpty() ) { return q2; }
		else if( q2.isEmpty() ) { return q1; }
		
		ConcurrentLinkedQueue<FileNode> merged = new ConcurrentLinkedQueue<FileNode>();
		
		while( !q1.isEmpty() || !q2.isEmpty() ) {
			
			if( q1.isEmpty() ) {
				merged.addAll( q2 );
				break;
			}
			else if( q2.isEmpty() ) {
				merged.addAll( q1 );
				break;
			}
			
			if( q1.peek().compareTo( q2.peek() ) < 0 ) {
				merged.add( q1.poll() );
			}
			else if( q1.peek().compareTo( q2.peek() ) > 0 ) {
				merged.add( q2.poll() );
			}
			else {
				FileNode n = q1.poll();
				FileNode n2 = q2.poll();
				n.setOccurrence( n.getOccurrence() + n2.getOccurrence() );
				
				merged.add( n );
			}
		}
		
		
		return merged;
	}
}
