
import java.util.concurrent.ConcurrentLinkedQueue;


public class findHighest {

	public static void find(ConcurrentLinkedQueue<Node> z, Node[] bottom, Node[] top) {
		int counter = 0;
		for (Node a : z) {
			//run initial pass and then sort that array
			if (counter < 10) {
				bottom[counter] = a;
				top[counter] = a;
				counter++;
			} else if (bottom[0].getOccurrence() > a.getOccurrence()) {
				bottom[0] = a;
				sortBot(bottom);
			} else if (top[0].getOccurrence() < a.getOccurrence()) {
				top[0] = a;
				sortBot(top);
			} else {
				continue;
			}
			if (counter == 9) {
				sortBot(bottom);
				sortHigh(top);
			}
			
		}
		
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
