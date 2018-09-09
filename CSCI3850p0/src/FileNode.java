
public class FileNode {
    String fileID;
    int occurrence = 0;
    
    public String getFileID() { return fileID; }
    public int getOccurrence() { return occurrence; }
    
    public void setFileID(String id) { fileID = id; }
    public void setOccurrence(int oc) { occurrence = oc; }
    
    public synchronized void increment() { occurrence++; }
    
    public int compareTo( FileNode fn ) {
    	return this.getFileID().compareTo( fn.getFileID() );
    }
}
