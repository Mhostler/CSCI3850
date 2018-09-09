import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CSCI3850p0 {
	private static ConcurrentLinkedQueue<String> fileQueue = new ConcurrentLinkedQueue<String>();
	private static ConcurrentLinkedQueue<Node> tokenQueue = new ConcurrentLinkedQueue<Node>();
	
	private static int run;
	
	public static void main(String[] args) {
		
		Dictionary dict = new Dictionary( tokenQueue );
		File directory = new File("./documentset");
		String fileList[] = directory.list();
		int threadNo = 20;
		ElimStopWords.init();
		if(args.length != 0) {
			run = Integer.parseInt(args[0]);
		}
		else {
			run = 0;
		}
		
		for( String str : fileList ) {
			fileQueue.add(str);
		}
		
		ExecutorService executor = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );
		
		for( int i = 0; i < threadNo; i++) {
			executor.execute( new FileProcessor( fileQueue, tokenQueue ) );
		}
		
//		Thread t = new Thread(new FileProcessor());
//		t.start();
//		try {
//			t.join();
//			
//			Dictionary.sort();
//			
//			Dictionary.display();
//		} catch (InterruptedException e) {
//			System.out.println("thread interrupted");
//			e.printStackTrace();
//		}	
		
		try {
			executor.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dict.sort();

		dict.display();	
	}
	
	public static int getRunType() {
		return run;
	}
}
