import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ElimStopWords {

	public static String[] stahp;
	static String add;
	static int x;
	//HowTo: call this method as part of startup process to set up the arrays or add this method to another file
	public ElimStopWords() {
		
		stahp = new String[174];
		x = 0;
		
		//Use file IO to import words from stopWords.txt into array stahp
		BufferedReader stopReader;
		try {
			stopReader = new BufferedReader(new FileReader("stopWords.txt"));
			while( (add = stopReader.readLine()) != null ){
				stahp[x] = add;
				x++;
			}
			stopReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open file: stopWords.txt");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File I/O failed.");
			e.printStackTrace();
		}
		
	}
	//uses binary search to find value in sorted list of stopwords, returns true if found else false
	public static boolean isStop(String x) {
		int a =  Arrays.binarySearch(stahp, x);
		if(a > 0) {
			return true;
		}
		else {
			return false;
		}
	}

}
