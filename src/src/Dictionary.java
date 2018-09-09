import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Dictionary {
	
	private static ConcurrentLinkedQueue<Node> dictionary = new ConcurrentLinkedQueue<Node>();
	
	public void add(Node n) { dictionary.add(n); }
	public Node remove() { return dictionary.remove(); }
	
	public static synchronized void input(ConcurrentLinkedQueue<Node> input) {
		dictionary = merge( input, dictionary );
	}
	
	public static void sort() { dictionary = mergeSortQueue( dictionary ); }
	
	public static void display() {
		for( Node n : dictionary ) {
			
			Iterator<FileNode> iter = n.getQueue().iterator();
			String files[] = new String[5];
			
			while( iter.hasNext() ) {
				for( int i = 0; i < 5; i++ ) {
					if( iter.hasNext() ) {
						files[i] = iter.next().getFileID();
						iter.remove();
					}
					else {
						files[i] = "";
					}
				}
				//System.out.println("We made it!");
				System.out.printf("%s:\t\t%d\t%s\t%s\t%s\t%s\t%s\n", n.getKeyword(), n.getOccurance(), files[0], files[1], files[2], files[3], files[4]);
			}
		}
	}
	
	public static ConcurrentLinkedQueue<Node> mergeSortQueue( ConcurrentLinkedQueue<Node> q ) {
		Node n;
		int size = q.size();
		
		if( size > 1 ) {
			
			ConcurrentLinkedQueue<Node> q2 = new ConcurrentLinkedQueue<Node>();
			
			for( int i = 0; i < size/2; i++ ) {
				n = q.remove();
				
				q2.add(n);
			}

			ConcurrentLinkedQueue<Node> sort1 = mergeSortQueue(q);
			ConcurrentLinkedQueue<Node> sort2 = mergeSortQueue(q2);
			
			return merge( sort1, sort2 );
		}
		
		return q;
	}
	
	private static ConcurrentLinkedQueue<Node> merge( ConcurrentLinkedQueue<Node> q1, ConcurrentLinkedQueue<Node> q2 ) {
		if(q1.isEmpty()) { return q2; }
		if(q2.isEmpty()) { return q1; }
		
		ConcurrentLinkedQueue<Node> merged = new ConcurrentLinkedQueue<Node>();
		
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
				Node n = q1.poll();
				Node n2 = q2.poll();
				n.increment();
				
				ConcurrentLinkedQueue<FileNode> fn = fileMerge( n.getQueue(), n2.getQueue() );
				n.setQueue( fn );
				
				merged.add( n );
			}
		}
		
		return merged;
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
				q2.poll();
				n.increment();
				
				merged.add( n );
			}
		}
		
		
		return merged;
	}
}
