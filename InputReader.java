package eval;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.Scanner;
public class InputReader {
	public String fileName;
	public InputReader(String f) {
		this.fileName = f;
	}
	public Vector<String> getData() {
		
	
		try {
			System.setIn(new FileInputStream(new File(this.fileName)));
		} catch (FileNotFoundException e1) {
			System.out.println("Input file not found");
			System.exit(1);
		}

		Vector<String> v = new Vector<String>();	

		try {
			Scanner s = new Scanner(System.in);
		
			while (s.hasNext()) {
			    v.add(s.nextLine());

			}
		
		} catch(Exception ie) {

		}
		
		return v;
	}
	
}
