import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * keyword: Dictionary item
 * concurrence: how many times it appears
 * specific: filenames for document appearances 
 */
public class Node {
    private String keyword;
    private int occurrence = 0;
    private ConcurrentLinkedQueue<FileNode> specific = new ConcurrentLinkedQueue<FileNode>();
    
    public String getKeyword() { return keyword; }    	
    public int getOccurance() { return occurrence; }
    
    public void setKeyword(String key) { keyword = key; }
    public void setOccurance(int oc) { occurrence = oc; }
    
    public ConcurrentLinkedQueue<FileNode> getQueue() { return specific; }
    public void setQueue( ConcurrentLinkedQueue<FileNode> queue ) { specific = queue; }
    
    public FileNode deQueue() { return specific.remove(); }
    public void enQueue(FileNode node) { specific.add(node); }
    
    public void increment() {
    	occurrence++;
    }
    
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
