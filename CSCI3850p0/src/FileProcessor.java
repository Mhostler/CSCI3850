import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileProcessor implements Runnable {

	ConcurrentLinkedQueue<String> tokenQueue = new ConcurrentLinkedQueue<String>();
	
	public void run() {
		BufferedReader fileReader;
		String fileName = CSCI3850p0.getNextFile();
		
		try {
			fileReader = new BufferedReader(new FileReader("./documentset/" + fileName));	
			String str = "";
			
			while( (str = fileReader.readLine()) != null ) {
				
				str = str.replaceAll("<.*?>", "");
				str = str.replaceAll("[^a-zA-Z0-9]", " ");
				str = str.replaceAll("\\s+", " ");
				str = str.toLowerCase();
				
				String tokens[] = str.split("\\s");
				
				for( String token : tokens ) {
					
					if(!token.isEmpty()) {
						tokenQueue.add(token);
					}
				}
			}
			

			for( String token : tokenQueue ) {
				System.out.println(token);
			}
			
			fileReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open file: " + fileName);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File I/O failed.");
			e.printStackTrace();
		}
		
		

	}
}
