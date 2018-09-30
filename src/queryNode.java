
public class queryNode {
	String token;
	String docID;
	float weight = 0;
	
	public String getDocID() {return docID;}
	public float getWeight() {return weight;}
	
	public void setDocID(String a) {docID = a;}
	public void setWeight(float b) {weight = b;}
	public void addWeight(float c) {weight += c;}
	
	public boolean equals(Object e) {
		if (e == this)
    		return true;
    	
    	if( !(e instanceof Node)) {
    		return false;
    	}
    	
    	queryNode n = (queryNode)e;
    	
    	return this.getDocID().equals(n.getDocID());
	}
}
