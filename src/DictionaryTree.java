import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DictionaryTree {

	TreeNode root;
	int t;
	BufferedWriter bw;
	
	private class TreeNode {
		Node [] keys;
		int t;
		TreeNode [] children;
		int n;
		boolean leaf;
		
		TreeNode(int _t, boolean _leaf) {
			keys = new Node[2*_t - 1];
			t = _t;
			children = new TreeNode[2*t];
			n = 0;
			leaf = _leaf;
		}
		
		synchronized void splitChild(int i, TreeNode tn) {
			TreeNode nc = new TreeNode(tn.t, tn.leaf);
			nc.n = t - 1;
			
			for( int j = 0; j < t-1; j++ ) {
				nc.keys[j] = tn.keys[j+t];
			}
			
			if( tn.leaf == false ) {
				for( int j = 0; j < t; j++ ) {
					nc.children[j] = tn.children[j+t];
				}
			}
			
			tn.n = t - 1;
			
			for( int j = n; j >= i+1; j-- ) {
				children[j+1] = children[j];
			}
			
			children[i+1] = nc;
			
			for( int j = n-1; j >= i; j-- ) {
				keys[j+1] = keys[j];
			}
			
			keys[i] = tn.keys[t-1];
			
			n = n + 1;
		}
		
		synchronized void insertNonFull(Node node) {
			
			int i = n - 1;
			boolean unique = true;
			
			for( Node tmp : keys ) {
				if( tmp != null && tmp.compareTo(node) == 0 ) {
					unique = false;
					tmp.addSimilar(node);
				}
			}
			
			if( leaf && unique ) {
				
				while( i >= 0 && keys[i].compareTo(node) >  0 ) {
					keys[i+1] = keys[i];
					i--;
				}
				keys[i+1] = node;
				n = n+1;
			}
			else if( unique ) {
				while( i >= 0 && keys[i].compareTo(node) > 0 ) {
					if( keys[i].compareTo(node) == 0 )
						break;
					
					i--;
				}
			
				if( children[i+1].n == 2*t-1 ) {
					splitChild(i+1, children[i+1]);
					
					if( keys[i+1].compareTo(node) < 0 ) {
						i++;
					}
				}
				children[i+1].insertNonFull(node);
			}
		}
		
		synchronized Node search(String s) {
			int i;
			for( i = 0; i < n; i++ ) {
				if( keys[i] == null )
					break;
				
				if( s.compareTo(keys[i].getKeyword()) > 0 ) {
					continue;
				}
			}
			
			if( keys[i] != null && s.compareTo(keys[i].getKeyword()) == 0 ) {
				return keys[i];
			}
			
			if(leaf == true)
				return null;
			
			return children[i].search(s);
		}
		
		synchronized void traverse() {
			int i;
			for ( i = 0; i < n; i++ ) {
				if( leaf == false ) {
					children[i].traverse();
				}
				
				try {
					bw.write(keys[i].getKeyword() + "\n");
					bw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}//end TreeNode
	
	DictionaryTree(int _t) {
		root = null;
		t = _t;
	}
	
	synchronized void traverse() {
		try {
			bw = new BufferedWriter(new FileWriter("TreeOut.txt"));
			
			if( root != null ) {
				root.traverse();
			}
			else {
				System.out.println("Traverse failed, root = null");
			}
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	synchronized Node search(String s) {
		return (root == null) ? null : root.search(s);
	}
	
	synchronized void insert(Node node) {
		if( root == null ) {
			root = new TreeNode(t, true);
			root.keys[0] = node;
			root.n = 1;
		}
		else {
			if( root.n == 2*t-1 ) {
				TreeNode tn = new TreeNode( t, false );
				
				tn.children[0] = root;
				
				tn.splitChild(0, root);
				
				int i = 0;
				
				if( tn.keys[0].compareTo(node) < 0 ) {
					i++;
				}
				tn.children[i].insertNonFull(node);
				
				root = tn;
			}
			else {
				root.insertNonFull(node);
			}
		}
	}
}//end DictionaryTree
