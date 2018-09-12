import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;

public class CSCI3850p0 {
	private static ConcurrentLinkedQueue<String> fileQueue = new ConcurrentLinkedQueue<String>();
	private static ConcurrentLinkedQueue<Node> tokenQueue = new ConcurrentLinkedQueue<Node>();
	private static ConcurrentLinkedQueue<String> stopWords = new ConcurrentLinkedQueue<String>();
	
	private static int run;
	private static long timeStop;

	public static void main(String[] args) {
		
		int threadNo = 20;		
		Thread t[] = new Thread[threadNo];
		
		long timeStart = System.currentTimeMillis();
		
		BufferedReader stopReader;
		String word;
		
		Dictionary dict = new Dictionary( tokenQueue );
		File directory = new File("./documentset");
		String fileList[] = directory.list();

		if(args.length != 0) {
			run = Integer.parseInt(args[0]);
		}
		else {
			run = 0;
		}
		
		for( String str : fileList ) {
			fileQueue.add(str);
		}
		
		try {
			stopReader = new BufferedReader(new FileReader("stopWords.txt"));
			while( (word = stopReader.readLine()) != null) {
					stopWords.add(word);
				}
			stopReader.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Failed to find stopWords.txt");
			e1.printStackTrace();
		} catch (IOException e) {
			System.out.println("stopwords io exception");
			e.printStackTrace();
		}		
		
		System.out.println( "Beginning File Parsing." );
		for( int i = 0; i < threadNo; i++ ) {
			t[i] = new Thread( new FileProcessor( fileQueue, tokenQueue ) );
			t[i].start();
		}
		
		for( Thread th : t ) {
			try {
				th.join();
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted");
				e.printStackTrace();
			}
		}
		System.out.println( "Finished Parsing" );
		
		System.out.println( "Beginning Term Sorting, this may take some time" );
		dict.sort();
		System.out.println( "Sorting Finished" );
		
		timeStop = System.currentTimeMillis() - timeStart;
		
		//dict.display();	
		
		System.out.println( "Printing to output file." );
		HomeworkPrinter.setQueue( dict.getQueue() );
		HomeworkPrinter.setTime( timeStop );
		HomeworkPrinter.printHomework();
		
		System.out.println( "Program Finished. Time: " + Long.toString(timeStop) + " miliseconds" );
	}
	
	public static int getRunType() {
		return run;
	}
	public static long getTime() {
		return timeStop;
	}
	
	public static Object[] getStopWords() {
		return stopWords.toArray();
	}
}
