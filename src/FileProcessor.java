import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileProcessor implements Runnable {

	ConcurrentLinkedQueue<Node> tokenQueue = new ConcurrentLinkedQueue<Node>();
	
	public void run() {
		BufferedReader fileReader;
		String fileName = "";
		
		try {
			//do{
				fileName = CSCI3850p0.getNextFile();
				fileReader = new BufferedReader(new FileReader("./documentset/" + fileName));	
				String str = "";
				
				while( (str = fileReader.readLine()) != null ) {
					process(str, fileName);
				}
				
//				for( String key : tokenQueue )
//				{
//					System.out.println(key);
//				}
				
				output();
				fileReader.close();
				
			//}while( !CSCI3850p0.isEmpty() );
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open file: " + fileName);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File I/O failed.");
			e.printStackTrace();
		}
		
	

	}
	
	public void process(String str, String fileName) {
		str = str.replaceAll("<.*?>", "");
		str = str.replaceAll("[^a-zA-Z0-9]", " ");
		str = str.replaceAll("\\s+", " ");
		str = str.toLowerCase();
		
		String tokens[] = str.split("\\s");
		
		for( String token : tokens ) {
			
			if(!token.isEmpty()) {
				Node n = new Node();
				n.setKeyword(token);
				n.setOccurance(1);
				
				FileNode fn = new FileNode();
				fn.setFileID(fileName);
				fn.setOccurrence(1);
				n.enQueue(fn);
				
				tokenQueue.add(n);
				System.out.println(n.getKeyword());
				//tokenQueue.add(token);
			}
		}
	}
	
	public  void output() {
		Dictionary.input( tokenQueue );
	}
	
//	public void lineLinker(String toBreak, String docName){
//        	String[] arrs = toBreak.split(" ");
//        	for(int x = 0; x < arrs.length; x++){
//        		Node a = new Node();
//        		FileNode b = new FileNode();
//        		a.setKeyword(arrs[x]);
//        		a.setOccurance(1);
//        		b.setFileID(docName);
//        		b.setOccurrence(1);
//        		a.enQueue(b);
//        		tokenQueue.add(a);
//        	}
//    	}
}
