package eval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class Evaluator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		final int TOTAL = 3; 
		
		String str[] = new String[TOTAL];
		str[0] = "abc def ghi jkl";
		str[1] = "saf asf asf asf asffafaf asf";
		str[2] = "fgtrwgsadf asf asf f fdgf d sd ss dsd";
		
		Vector< List<String> > v = new Vector< List<String> >();
		Vector< HashMap<String,Integer> > frequency = new Vector< HashMap<String,Integer> >();
		Vector<Integer> maxFrequency = new Vector<Integer>();
		
		for (int i = 0; i < TOTAL; ++i ) {
			int maxF = 0;
			frequency.add(new HashMap<String,Integer>());
			List<String> ss = new ArrayList<String>();
			String read = "";
			for ( int j = 0; j <= str[i].length(); ++j ) {
				if ( j == str[i].length() || str[i].charAt(j) == ' ' ) {
					if ( !frequency.elementAt(i).containsKey(read) ) {
						frequency.elementAt(i).put(read,1);
						if ( maxF < 1 ) maxF = 1;
						ss.add(read);
					} else {
						int cnt = frequency.elementAt(i).get(read);
						frequency.elementAt(i).put(read,cnt+1);
						if ( maxF < cnt+1 ) maxF = cnt+1;
					}
					read = "";
					continue;
				}
				read += str[i].charAt(j);
			}
			maxFrequency.add(maxF);
			Collections.sort(ss);
			v.add(ss);
		}
		
		HashMap<String,Integer> wordCount = new HashMap<String,Integer>();
		Vector<String> wordIdx = new Vector<String>();
		int distinctWords = 0;
		
		for ( int i = 0; i < TOTAL; ++i ) {
			for ( String read:v.elementAt(i) ) {
				if ( !wordCount.containsKey(read) ) {
					wordCount.put(read,1);
					wordIdx.add(read);
					distinctWords++;
				} else {
					int cnt = wordCount.get(read);
					wordCount.put(read,cnt+1);
				}
			}
		}
		
		System.out.println(TOTAL+" "+distinctWords);
		
		double DTMatrix[][] = new double[TOTAL][];
		for ( int i = 0; i < TOTAL; ++i ) {
			DTMatrix[i] = new double[distinctWords];
			for ( int j = 0; j < distinctWords; ++j ) {
				double tf = 0.0;
				if ( frequency.elementAt(i).containsKey(wordIdx.elementAt(j)) )
					tf = 0.5*(double)frequency.elementAt(i).get(wordIdx.elementAt(j));
				tf /= (double)(maxFrequency.elementAt(i));
				tf += 0.5;
				double idf = Math.log((double)(1+TOTAL)/(double)(1+wordCount.get(wordIdx.elementAt(j))));
				DTMatrix[i][j] = tf*idf;
				System.out.print(DTMatrix[i][j]+" ");
			} System.out.println("");
		}

	}

}
