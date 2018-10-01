import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CSCI3850p0 {

	private static File directory;
	
	private static ConcurrentLinkedQueue<String> fileQueue = new ConcurrentLinkedQueue<String>();
	private static ConcurrentLinkedQueue<String> stopWords = new ConcurrentLinkedQueue<String>();
	
	public static ConcurrentHashMap<String, Node> mapping = new ConcurrentHashMap<String, Node>();
	public static Node[] bottom = new Node[10];
	public static Node[] top = new Node[10];
	public static String[] queryList;
	public static String queries;
	
	private static long timeStop;

	public static String getFileName() { return directory.getName(); }
	
	public static void main(String[] args) {
		
		int threadNo = 20;		
		Thread t[] = new Thread[threadNo];
		
		BufferedReader stopReader;
		String word;
		
		long timeStart = System.currentTimeMillis();
		
		if(args == null) {
			System.out.println("ERROR: to execute type 'java CSCI3850p0 DATA query.txt'");
			System.exit(0);
		}
		
		//TODO get query.txt placed line by line into an array (stem and remove stopwords as well)
		setupQuery(args[1], queryList);

		directory = new File(args[0]);
		String fileList[] = directory.list();

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
			t[i] = new Thread( new FileProcessor( fileQueue ) );
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
		
		timeStop = System.currentTimeMillis() - timeStart;

		displayMap();
		
		System.out.println( "Printing to output file." );
		HomeworkPrinter.setTime( timeStop );
		HomeworkPrinter.printHomework();
		
		Thread tester = new Thread(new Query(queryList));
		tester.start();
		try {
			tester.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println( "Program Finished. Time: " + Long.toString(timeStop) + " miliseconds" );
	}

	public static long getTime() {
		return timeStop;
	}
	
	public static ConcurrentLinkedQueue<String> getStopWords() { return stopWords; }
	
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
        
				line = in.nextLine();
				agh = line.split(" ");
				//add in stemming and etc
				for(String item : agh) {
					if(y.isStop(item)) {
						item = "";
					}
					else {
						item = item.toLowerCase();
						meh = item.toCharArray();
						z.add(meh, meh.length);
						z.stem();
						item = z.toString();
					}
				}
				token = String.join(" ", agh);
				token.replaceAll("\\s+", " ");

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
	
	public static void displayMap() {
		try {
			BufferedWriter bw = new BufferedWriter( new FileWriter( "MapOut.txt." ));
			for( Map.Entry<String, Node> e : mapping.entrySet() ) {
				bw.write(e.getValue().getKeyword() + ":" + e.getValue().getOccurrence() + "\n     ");
				
				int count = 0;
				for( FileNode fn : e.getValue().getQueue() ) {
					bw.write(fn.getFileID() + ": " + fn.getWordCount() + "  " );
					
					if( count++ >= 5 ) {
						bw.write("\n     ");
						count = 0;
					}
				}
				
				bw.write("\n\n");
			}
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}