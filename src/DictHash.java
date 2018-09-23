
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
			while(true) {
				if(nodeHash[ytp] == null) {
					nodeHash[ytp] = a;
					break;
				} else if (a.equals(nodeHash[ytp])){
					do {
						nodeHash[ytp].enQueue(a.deQueue());
					}while(a.getQueue() != null);
					break;
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
}
