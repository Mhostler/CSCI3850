import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileProcessor implements Runnable {

	ConcurrentLinkedQueue<Node> tokenQueue;
	ConcurrentLinkedQueue<String> files;
	
	public FileProcessor(ConcurrentLinkedQueue<String> fileNames, ConcurrentLinkedQueue<Node> dict ) {
		files = fileNames;
		tokenQueue = dict;
	}
	
	public void run() {
		BufferedReader fileReader;
		String fileName = "";
		ElimStopWords esw = new ElimStopWords();

		int runType = CSCI3850p0.getRunType();

		try {
			do{
				fileName = files.poll();

				if( fileName == null )
					break;
				
				fileReader = new BufferedReader(new FileReader("./documentset/" + fileName));	
				String str = "";
				
				while( (str = fileReader.readLine()) != null ) {
					//System.out.println("Working: " + fileName);
					process(str, fileName, esw, runType);
				}
				
				fileReader.close();
				
			}while( !files.isEmpty() );
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open file: " + fileName);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File I/O failed.");
			e.printStackTrace();
		}
		
	

	}
	
	public void process(String str, String fileName, ElimStopWords esw, int runType) {
		str = str.replaceAll("<.*?>", "");
		str = str.replaceAll("[^a-zA-Z0-9 ]", "");
		str = str.replaceAll("\\s+", " ");
		str = str.toLowerCase();
		
		String tokens[] = str.split("\\s");

		Stemmer s = new Stemmer();
		char[] ack;
		
		for( String token : tokens ) {
			
			if(!token.isEmpty()) {
				Node n = new Node();
				n.setKeyword(token);
				n.setOccurrence(1);
				
				FileNode fn = new FileNode();
				fn.setFileID(fileName);
				fn.setOccurrence(1);
				n.enQueue(fn);
				
				//Stopwords
				if(runType == 1) {
					if(esw.isStop(token)) {
						continue;
					}
					else {
						tokenQueue.add(n);
					}
				}
				//Stemming
				else if(runType == 2) {
					if(esw.isStop(token)) {
						continue;
					}
					else {
						//stemming stuff
						ack = token.toCharArray();
						s.add(ack, token.length());
						s.stem();
						token = s.toString();
						n.setKeyword(token);
						tokenQueue.add(n);
					}
				}
				else {
						tokenQueue.add(n);
				}
			}
		}
	}
}
