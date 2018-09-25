
public class queryNode {
	String docID;
	float weight;
	
	public String getDocID() {return docID;}
	public float getWeight() {return weight;}
	
	public void setDocID(String a) {docID = a;}
	public void setWeight(float b) {weight = b;}
	public void addWeight(float c) {weight += c;}
}
