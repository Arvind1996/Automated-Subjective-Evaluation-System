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
public class main{
	public int[] testing(){
            final File file = new File(".");
        System.out.println("file = " + file.getAbsoluteFile().getParent());
            String[] arr = {"./Test Data/test3.in", "./Test Data/test12.in"};
            String[] brr = {"./Test Data/rat3.in", "./Test Data/rat12.in"};
                int [] ar = new int[100];
		Vector<Vector<String> > data = new Vector<Vector<String> > ();
		for(int i = 0; i < arr.length-1; i++) {
                    
		    String fileName = arr[i];
                    Vector<String> v = new Vector<String>();
                    InputReader inr = new InputReader(fileName);
                    InputReader inrat = new InputReader(brr[i]);
                    v = inrat.getData();
                    int[] ratdoub = new int[v.size()];
                    for(int j = 0; j <v.size(); j++) {
                        ratdoub[j] = Integer.parseInt(v.get(j));
                    }
                    Evaluation eval = new Evaluation(inr.getData(),ratdoub, 50, true);
                    ar = eval.run();
                    Results r = new Results(ar, true);
                    r.setVisible(true);
			
		}
                
             
                return ar;
        }
        public int[] prediction(int test) {
            String[] arr = {"inputGenerated.txt"};
                 int []ratings = new int[100];
		Vector<Vector<String> > data = new Vector<Vector<String> > ();
		for(int i = 0; i < arr.length; i++) {
		    String fileName = arr[i];
                    System.out.println(fileName);
                    Vector<String> v = new Vector<String>();
                    InputReader inr = new InputReader(fileName);

                    int[] ratdoub = new int[v.size()];
                    for(int j = 0; j <v.size(); j++) {
                        ratdoub[j] = Integer.parseInt(v.get(j));
                    }
                    Evaluation eval = new Evaluation(inr.getData(),ratdoub, test, false);
                    ratings= eval.run();
			
		}
                return ratings;
                
        }
	public static void main(String[] args) {
                main m = new main();
                m.testing();
		
	}
	
}
