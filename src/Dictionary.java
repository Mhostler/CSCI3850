import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Formatter;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Dictionary {
	
	private ConcurrentLinkedQueue<Node> dictionary;
	
	public Dictionary( ConcurrentLinkedQueue<Node> d ) {
		this.dictionary = d;
	}
	
	public ConcurrentLinkedQueue<Node> getQueue() { return dictionary; }
	
	public void add(Node n) { dictionary.add(n); }
	public Node remove() { return dictionary.remove(); }
	
	public synchronized void input(ConcurrentLinkedQueue<Node> input) {
		dictionary = merge( input, dictionary );
	}
	
	public void sort() { dictionary = mergeSortQueue( dictionary ); }
	
	public void display() {
		Formatter formOut;
		try {
			String name;
			name = "Output.txt";
			formOut = new Formatter(new FileOutputStream(name));
			
			for( Node n : dictionary ) {
				
				Iterator<FileNode> iter = n.getQueue().iterator();
				String files[] = new String[5];
				long counts[] = new long[5];
				
				formOut.format( "%15s:\t%d\n", n.getKeyword(), n.getOccurrence() );	
				System.out.printf( "%15s:\t%d\n", n.getKeyword(), n.getOccurrence() );
				
				while( iter.hasNext() ) {
					for( int i = 0; i < 5; i++ ) {
						if( iter.hasNext() ) {
							FileNode temp = iter.next();
							files[i] = temp.getFileID();
							counts[i] = temp.getWordCount();
							iter.remove();
						}
						else {
							files[i] = "";
						}
					}
					
					formOut.format("  %8s:%d  %8s:%d  %8s:%d   %8s:%d   %8s:%d\n", files[0], counts[0], files[1], counts[1], files[2], counts[2], files[3], counts[3], files[4], counts[4]);
					System.out.printf("  %8s:%d  %8s:%d  %8s:%d   %8s:%d   %8s:%d\n", files[0], counts[0], files[1], counts[1], files[2], counts[2], files[3], counts[3], files[4], counts[4]);
				}
			}
			
			formOut.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
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
	
	private synchronized static ConcurrentLinkedQueue<Node> merge( ConcurrentLinkedQueue<Node> q1, ConcurrentLinkedQueue<Node> q2 ) {
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
				
				ConcurrentLinkedQueue<FileNode> fn = fileMerge( n.getQueue(), n2.getQueue() );
				n.setQueue( fn );
				n.setOccurrence(n.getOccurrence() + n2.getOccurrence());
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
				FileNode n2 = q2.poll();
				n.setOccurrence( n.getOccurrence() + n2.getOccurrence() );
				
				if( n.getWordCount() != n2.getWordCount() ) {
					System.out.println("Word count mismatch.");
				}
				
				merged.add( n );
			}
		}
		
		
		return merged;
	}
}
