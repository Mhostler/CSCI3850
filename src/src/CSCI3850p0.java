import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CSCI3850p0 {
	private static ConcurrentLinkedQueue<String> fileQueue = new ConcurrentLinkedQueue<String>();	
		
	public static void main(String[] args) {
		
		File directory = new File("./documentset");
		String fileList[] = directory.list();
		
		for( String str : fileList ) {
			fileQueue.add(str);
		}
		
		Thread t = new Thread(new FileProcessor());
		t.start();
		try {
			t.join();
			
			Dictionary.sort();
			
			Dictionary.display();
		} catch (InterruptedException e) {
			System.out.println("thread interrupted");
			e.printStackTrace();
		}

//		ConcurrentLinkedQueue<Thread> threads = new ConcurrentLinkedQueue<Thread>();
//		while( !fileQueue.isEmpty() ) {
//			threads.add(new Thread(new FileProcessor()));
//		}
//		
//		for (Thread thread : threads ) {
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	
	public static String getNextFile() {
		return fileQueue.remove();
	}
	
	public static boolean isEmpty() {
		if( fileQueue.isEmpty() ) {
			return true;
		}
		else
			return false;
	}
}
