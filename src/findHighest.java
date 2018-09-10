
import java.util.concurrent.ConcurrentLinkedQueue;


public class findHighest {

	public static void find(ConcurrentLinkedQueue<Node> z, Node[] bottom, Node[] top) {
		int counter = 0;
		for (Node a : z) {
			//run initial pass and then sort that array
			//for a >= 11 compare with bottom[0] if a is lower do sortbot vice versa
			sortBot(bottom);
			sortHigh(top);
		}
		
		
		
	}
	
	public static boolean greater(int val, int[] check) {
		for(int i = 0; i < check.length; i++) {
			if(check[i] >= val) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean lesser(int val, int[] check) {
		for(int j = 0; j < check.length; j++) {
			if(check[j] <= val) {
				return false;
			}
		}
		return true;
	}
	// greatest value first
	public static void sortBot(Node[] arr) {
		for (int i = 1; i < 10; ++i)
        {
			Node key = arr[i];

			int j = i-1;

			while (j>=0 && arr[j].getOccurrence() < key.getOccurrence())
			{
				arr[j+1] = arr[j];
				j = j-1;
			}
			arr[j+1] = key;
        }
	}
	//lowest value first
	public static void sortHigh(Node[] arr) {
		for (int i = 1; i < 10; ++i)
        {
			Node key = arr[i];

			int j = i-1;

			while (j>=0 && arr[j].getOccurrence() > key.getOccurrence())
			{
				arr[j+1] = arr[j];
				j = j-1;
			}
			arr[j+1] = key;
        }
	}

}
