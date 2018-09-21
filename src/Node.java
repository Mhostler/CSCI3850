import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * keyword: Dictionary item
 * concurrence: how many times it appears
 * specific: filenames for document appearances 
 */
public class Node {
    private String keyword;
    private ConcurrentLinkedQueue<FileNode> specific = new ConcurrentLinkedQueue<FileNode>();
    private int occurrence;
    
    public String getKeyword() { return keyword; }    	
    
    public void setKeyword(String key) { keyword = key; }
    
    public int getOccurrence() { return occurrence; }
    public void setOccurrence(int val) { occurrence = val; }
    
    public ConcurrentLinkedQueue<FileNode> getQueue() { return specific; }
    public void setQueue( ConcurrentLinkedQueue<FileNode> queue ) { specific = queue; }
    
    public FileNode deQueue() { return specific.remove(); }
    public void enQueue(FileNode node) { specific.add(node); }

    
    public boolean equals(Object e) {
    	if (e == this)
    		return true;
    	
    	if( !(e instanceof Node)) {
    		return false;
    	}
    	
    	Node n = (Node)e;
    	
    	return this.getKeyword().equals(n.getKeyword());
    }
    
    public int compareTo( Node n ) {
    	return this.getKeyword().compareTo( n.getKeyword() );
    }
}
