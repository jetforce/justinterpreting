package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Contains methods for reading from txt file
 * 
 * @author amcan
 */
public class TXTIO {

	/**
	 * Reads a TXT file
	 * 
	 * @param filename
	 *            filename of the TXT file to be read
	 * @return ArrayList of TXT contents per line
	 */
	public static ArrayList<String> read(String filename) {

		ArrayList<String> input = new ArrayList<String>();

		try {

			Scanner sc = new Scanner(new File(filename));

			while (sc.hasNext()) {
				String s;
				if(!(s = sc.nextLine()).isEmpty())
					input.add(s);
			}

			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return input;

	}
	
}
