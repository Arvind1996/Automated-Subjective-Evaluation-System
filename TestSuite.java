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
public class TestSuite {
	public Vector<Vector<String> > train, test;
	
	public TestSuite(Vector<Vector<String> > data, int trainNo) {

		train = new Vector<Vector<String> >();
		test = new Vector<Vector<String> >();
		
		for(int i = 0; i < trainNo; i++) {
			train.add(data.get(i));
		}
		for(int i = trainNo; i < data.size(); i++) {
			test.add(data.get(i));
		}
		if(test.size() < 1) {
			System.out.println("No Testing Data");
		}
		
	}
	public void test() {
		//pass;
	}
}
