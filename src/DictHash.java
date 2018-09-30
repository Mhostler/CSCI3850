import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Formatter;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DictHash { // convert to Concurrent Hash Map
	static final int [] primes = { 104743, 223469, 478637 };
	static int curPrime = 0;
	static float fill = 0;
	static Node[] table = new Node[primes[curPrime]];
	
	public synchronized static void DisplayTable() {
		Formatter formOut;
		try {
			String name;
			name = "Output.txt";
			formOut = new Formatter(new FileOutputStream(name));
			
			for( Node n : table ) {
				
				if( n == null )
					continue;
				
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
	
	public synchronized static Node find(String keyword) {
		int hKey = findLoc(keyword);
		
		if( hKey == -1 )
			return null;
		else
			return table[hKey];
	}
	
	public synchronized static boolean remove(String keyword) {
		int hKey = findLoc(keyword);
		
		if( hKey == -1 )
			return false;
		else
			return true;
	}
	
	public synchronized static int findLoc(String keyword) {
		int hKey = Math.abs(keyword.hashCode()) % table.length;
		int i = hKey;
		
		while( true ) {
			//wrap if at end of table
			if( i == table.length ) { i = 0; }
			
			//compare to current value
			if( table[i] == null ) {
				i++;
				continue;
			}
			else if( keyword.compareTo(table[i].getKeyword()) == 0 ) {
				return i;
			}
			
			//stop if we've gone a full loop
			//if not advance i
			if( i++ == hKey-1 ) {
				return -1;
			}
		}
	}
	
	public synchronized static void add(ConcurrentLinkedQueue<Node> cn) {
		
		for( Node n : cn ) {
			//System.out.println(n.getKeyword());
			add(n);
		}
	}
	
	public synchronized static void add(Node a) {
		int hKey = Math.abs(a.getKeyword().hashCode()) % table.length;
		
		if( table[hKey] == null ) {
			table[hKey] = new Node();
			table[hKey].setKeyword(a.getKeyword());
			table[hKey].setOccurrence(a.getOccurrence());
			table[hKey].setQueue(a.getQueue());
			fill++;
		}
		else if( table[hKey].compareTo(a) == 0 ) {
			join(a, hKey);
		}
		else {
			int i = hKey + 1;
			
			while( true ) {
				//wrap if at end of table
				if( i == table.length ) { i = 0; }
				
				//compare to current value
				if( table[i] == null ) {
					table[i] = new Node();
					table[i].setKeyword(a.getKeyword());
					table[i].setOccurrence(a.getOccurrence());
					table[i].setQueue(a.getQueue());
					fill++;
					break;
				}
				else if( table[i].compareTo(a) == 0 ) {
					join(a, i);
					break;
				}

				//stop if we've gone a full loop
				//if not advance i
				if( i++ == hKey ) {
					System.out.println("Error adding: " + a.getKeyword());
					break;
				}
			}
		}
		
		if( fill / (float)table.length >= 0.9 ) { rehash(); }
	}
	
	private synchronized static void rehash() {
		if( ++curPrime >= primes.length ) {
			System.out.println("Ran out of Primes!");
			System.exit(-1);
		}
		
		Node[] newHash = new Node[primes[curPrime]];
		
	    for( int i = 0; i < table.length; i++ ) {
	    	if( table[i] == null ) { continue; }
	    	else {
	    		int key = table[i].getKeyword().hashCode() % primes[curPrime];
	    		key = Math.abs(key);
	    		
	    		//is our new key unique
	    		if( newHash[key] == null ) {
	    			newHash[key] = table[i];
	    		}
	    		else {
	    			int j = key + 1;
	    			
	    			while( true ) {
	    				//wrap if at end of table
	    				if( j == newHash.length ) { j = 0; }
	    				
	    				//check current
	    				if( newHash[j] == null ) { 
	    					newHash[j] = table[i];
	    					break;
	    				}

	    				//stop if we've gone a full loop
	    				//if not advance i
	    				if( j++ == key-1 ) {
	    					System.out.println("Error rehashing: " + table[i].getKeyword());
	    					break;
	    				}
	    			}
	    		}
	    	}
	    }
	    
	    table = newHash;
	}
	
	private synchronized static void join(Node n, int hKey) {
		Node temp = new Node();
		temp.setKeyword(n.getKeyword());
		temp.setOccurrence(table[hKey].getOccurrence() + n.getOccurrence());
		temp.setQueue( fileMerge(table[hKey].getQueue(),n.getQueue()) );
		
		table[hKey] = temp;
	}
	
	private static synchronized ConcurrentLinkedQueue<FileNode> fileMerge(ConcurrentLinkedQueue<FileNode> q1, ConcurrentLinkedQueue<FileNode> q2 ) {
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
