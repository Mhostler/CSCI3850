import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CSCI3850p0 {
	private static ConcurrentLinkedQueue<String> fileQueue = new ConcurrentLinkedQueue<String>();
	private static ConcurrentLinkedQueue<Node> tokenQueue = new ConcurrentLinkedQueue<Node>();
	public static Node[] bottom = new Node[10];
	public static Node[] top = new Node[10];
	public static String[] queryList;
	public static String queries;
	
	private static long timeStop;
	
	public static void main(String[] args) {
		
		long timeStart = System.currentTimeMillis();
		
		if(args == null) {
			System.out.println("ERROR: to execute type 'java CSCI3850p0 DATA query.txt'");
			System.exit(0);
		}
		
		//TODO get query.txt placed line by line into an array (stem and remove stopwords as well)
		setupQuery(args[1], queryList);
		
		Dictionary dict = new Dictionary( tokenQueue );
		File directory = new File(args[0]);
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
	
	public static void setupQuery(String fname, String[] querylist) {
		String token = "";
		ArrayList<String> tempList = new ArrayList<String>();
		String[] agh;
		char[] meh;
		ElimStopWords y = new ElimStopWords();
		Stemmer z = new Stemmer();
		
		try {
			Scanner in = new Scanner(new File(fname));
			while(in.hasNextLine()) {
				token = in.nextLine();
				tempList.add(token);
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error 404: File not Found");
			e.printStackTrace();
			System.exit(0);
		}
		
		queryList = tempList.toArray(new String[0]);
		
		for(int x = 0; x < queryList.length; x++) {
			//take qL[0] split into temp string array by " " check for stopwords and then convert each word to char array and stem
			agh = queryList[x].split(" ");
			for(int h = 0; h < agh.length; h++) {
				if (y.isStop(agh[h])) {agh[h] = " ";}
				if (agh[h] != " ") {
					meh = agh[h].toCharArray();
					z.add(meh, agh[h].length());
					z.stem();
					agh[h] = z.toString();
				}
			}
			queryList[x] = String.join(" ", agh);
		}
		
	}
}
