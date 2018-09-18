import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CSCI3850p0 {
	private static ConcurrentLinkedQueue<String> fileQueue = new ConcurrentLinkedQueue<String>();
	private static ConcurrentLinkedQueue<Node> tokenQueue = new ConcurrentLinkedQueue<Node>();
	public static Node[] bottom = new Node[10];
	public static Node[] top = new Node[10];

	private static long timeStop;
	
	public static void main(String[] args) {
		
		long timeStart = System.currentTimeMillis();
		
		Dictionary dict = new Dictionary( tokenQueue );
		File directory = new File("./documentset");
		String fileList[] = directory.list();
		int threadNo = 20;
		
		for( String str : fileList ) {
			fileQueue.add(str);
		}
		
		ExecutorService executor = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );
		
		for( int i = 0; i < threadNo; i++) {
			executor.execute( new FileProcessor( fileQueue, tokenQueue ) );
		}
		
		try {
			executor.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dict.sort();
		
		//findHighest.find(tokenQueue, bottom, top);
		
		timeStop = System.currentTimeMillis() - timeStart;
		
		dict.display();	
		
		
	}

	public static long getTime() {
		return timeStop;
	}
}
