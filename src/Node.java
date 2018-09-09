import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * keyword: Dictionary item
 * concurrence: how many times it appears
 * specific: filenames for document appearances 
 */
public class Node {
    private String keyword;
    private int occurrence;
    private ConcurrentLinkedQueue<FileNode> specific;
    
    public Node() {
    	keyword = "";
    	occurrence = 0;
    	specific = new ConcurrentLinkedQueue<FileNode>();
    }
    
    public String getKeyword() { return keyword; }    	
    public int getOccurance() { return occurrence; }
    
    public void setKeyword(String key) { keyword = key; }
    public void setOccurance(int oc) { occurrence = oc; }
    
    public FileNode deQueue() { return specific.remove(); }
    public void enQueue(FileNode node) { specific.add(node); }
}
