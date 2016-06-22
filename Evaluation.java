package eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.factory.SingularValueDecomposition;
import org.ejml.ops.CommonOps;



/*
 *  Main class where all the where the predictions are done, 
 *  and error is calculated.
 *  
 *  docs - vector consisting of space separted words in each essay.
 *  realRatings - Actual ratings of the corresponding essay(only in testing mode)
 *  termDocument - The term-document matrix calculated with tf-idf.
 *  stopWords - list of stopWords in hashMap for fast Searching.
 *  test - Boolean variable for testing mode or predication phase.
 *  
 */

class Evaluation {
    
    public Vector<String> docs;
    public int[] realRatings;
    public DenseMatrix64F termDocument;
    
    public HashMap<String,Integer> stopWords;
    public int numDocs ;
    public int numFeat ;
    public int numCorrect;
    public boolean test;
    public Evaluation() {
    }
    
    /*
     * Main Constructer used for taking input for docs,
     * and realRatings and functioning mode.
     */
    
    public Evaluation(Vector<String> s, int[] realRatings, int n, boolean t) {
        this.realRatings = realRatings;
        this.test = t;
//        System.out.println(docs.get(0));
        setVector(s);
        numCorrect = n;
        numDocs = s.size();
        InputReader inr = new InputReader("./stopwords.txt");
        Vector<String> v = inr.getData();
        stopWords = new HashMap<String,Integer>();
        for(int i = 0;i < v.size(); i++) {
            stopWords.put(v.get(i), 1);
        }
        
    }
    
    public void setVector(Vector<String> s){
        docs = s;
    }
    
    /* 
     * Fuction for removing stopWords from the essay,
     * takes the essays as input.
     */
    public List<String> removeStopWords(List<String> l) {
        List<String> newl = new ArrayList<String>();
        for(int i = 0; i < l.size(); i++) {
            if(!stopWords.containsKey(l.get(i))){
                newl.add(l.get(i));
            }
        }
        return newl;
    }
    /*
     * Preprocessing function for splitting essay into words, 
     * and stemming all the words.
     */
    public void preprocess() {
	Stemmer s = new Stemmer();
	//Stemming Vector of string
        
        for(int i = 0; i < docs.size(); i++) {
            String temp = docs.get(i);
             List<String> temp2 = Arrays.asList(temp.split(" "));
             temp2 = removeStopWords(temp2);
             temp2 = s.stemList(temp2);
             String str = new String();
             
             for(int j = 0;j < temp2.size(); j++) {
                 
//                     System.out.println(temp2.get(j));
                 
                   str = str+" " +temp2.get(j);
             }

        }
	

    }
    /*
     * Method call which calculates the term-document matrix, 
     * by calculating the frequency(tf) and
     * the inverse term frequency(idf) of the words.
     */
    public void TDMatrix() {
        String [] str = docs.toArray(new String[0]);
        
        final int TOTAL = docs.size();
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
        HashMap<String,Integer> wordCountCorrect = new HashMap<String,Integer>();
        Vector<String> wordIdx = new Vector<String>();
        int distinctWords = 0;

        for ( int i = 0; i < TOTAL; ++i ) {
                for ( String read:v.elementAt(i) ) {
                        if ( !wordCount.containsKey(read) ) {
                                wordCount.put(read,1);
                                wordIdx.add(read);
                                if ( i >= TOTAL-numCorrect ) wordCountCorrect.put(read,1);
                                distinctWords++;
                        } else {
                                int cnt = wordCount.get(read);
                                wordCount.put(read,cnt+1);
                                if ( i >= TOTAL-numCorrect ) {
                                    if ( !wordCountCorrect.containsKey(read) ) {
                                        wordCountCorrect.put(read,1);
                                    } else {
                                        int c = wordCountCorrect.get(read);
                                        wordCountCorrect.put(read,c+1);
                                    }
                                }
                        }
                }
        }


        double DTMatrix[][] = new double[TOTAL][];
        for ( int i = 0; i < TOTAL; ++i ) {
                DTMatrix[i] = new double[distinctWords];
                for ( int j = 0; j < distinctWords; ++j ) {
                        double tf = 0.0;
                        if ( frequency.elementAt(i).containsKey(wordIdx.elementAt(j)) )
                                tf = 0.5*(double)frequency.elementAt(i).get(wordIdx.elementAt(j));
                        tf /= (double)(maxFrequency.elementAt(i));
                        tf += 0.5;
                        double idf = Math.log((double)(1+numCorrect));
                        if ( wordCountCorrect.containsKey(wordIdx.elementAt(j)) )
                            idf = Math.log((double)(1+numCorrect)/(double)(1+wordCountCorrect.get(wordIdx.elementAt(j))));
                        DTMatrix[i][j] = tf*idf;

                } 
        }
        termDocument = new DenseMatrix64F(DTMatrix);

    }
    /*
     * Wrapper function for applying svd,
     * and then retrieving the three matrices.
     */
    public DenseMatrix64F [] decompose( DenseMatrix64F A ) {
        SingularValueDecomposition<DenseMatrix64F> svd = DecompositionFactory.svd(A.numRows,A.numCols, true, true, true);

        if( !svd.decompose(A) )
            throw new RuntimeException("Decomposition failed");
        
        DenseMatrix64F U = svd.getU(null,false);
        DenseMatrix64F W = svd.getW(null);
        DenseMatrix64F V = svd.getV(null,false);
        DenseMatrix64F [] mats = {U, W, V};
        return mats;
    }
    
    /*
     * Method used to calculate ratings from the cosine similarity score.
     * Function calculates ratings in range 2-12.
     */
    public double [] getRating(double[][] sim) {
        double[] finalScore = new double[numDocs];
        double[] ratings = new double[numDocs];
        for(int j = 0; j < numDocs-numCorrect; j++) {
            finalScore[j] = 0;
        }
        for (int i = 0;i < numCorrect; i++) {
            for(int j = 0; j < numDocs-numCorrect; j++) {
                finalScore[j]+= sim[i][j];
            }
        }
        double mi = 10000, mx = -1;
        for(int j = 0; j < numDocs-numCorrect; j++) {
            finalScore[j] = (finalScore[j]/numCorrect)*10000;
            mi = Math.min(mi, finalScore[j]);
            mx = Math.max(finalScore[j], mx);

        }
        
        double range = (mx - mi)/11.0;
        for(int i = 0 ;i < numDocs; i++) {
            ratings[i] = -1;
        }

        int rat = 2;
        for (double ii = mi+range; ; ii+=range, rat++) {

            for(int i = 0; i < numDocs - numCorrect; i++) {
                if(ratings[i] != -1 || finalScore[i] > ii) continue;

                ratings[i] = rat;
            }
            if (ii >= mx) break;
        }
        for(int i = 0; i < numDocs-numCorrect; i++) {
            if(ratings[i] > 10)
                ratings[i] = 10;
        }
        
        
        return ratings;
        
    }
    /*
     * Main function calling all the other steps and returning an array.
     * Testing phase - gives error
     * Prediction Phase - gives ratings of coresponding essays.
     */
    public int[] run() {
        System.out.println("hello");
        preprocess();
        TDMatrix();
        DenseMatrix64F [] mats = decompose(termDocument);

        
        DenseMatrix64F semantic = new DenseMatrix64F(mats[0].numRows,mats[1].numCols );
        CommonOps.mult(mats[0], mats[1], semantic);
        CommonOps.transpose(mats[2]);
        DenseMatrix64F temp = new DenseMatrix64F(semantic.numRows, mats[2].numCols );
        CommonOps.mult(semantic, mats[2], temp);
        
        semantic = new DenseMatrix64F(temp );
        numDocs = semantic.numRows;
        numFeat = semantic.numCols;
        
        double [] mean = new double[numDocs];
        double [] denom = new double[numDocs];
        double [] sq = new double[numDocs];
        for(int i = 0; i < numDocs; i++) {
            double sum = 0;
            for(int j = 0; j < numFeat; j++) {
                sum += semantic.get(i, j);
            }
            mean[i] = sum/numFeat;
            sum = 0;
            for(int j = 0; j < numFeat; j++) {
                sum += (semantic.get(i, j) - mean[i])*(semantic.get(i, j) - mean[i]);
            }
            
            denom[i] = Math.sqrt(sum);
            

            
            sum = 0;
            for(int j = 0; j < numFeat; j++) {
                sum += semantic.get(i, j)*semantic.get(i, j);
            }
            sq[i] = Math.sqrt(sum);
       	     
            
        }
        

        
        double [][] corMat= new double[numCorrect][];
        for(int a1 = 0; a1 < numCorrect ; a1++) {
            corMat[a1] = new double[numDocs];
            
            for (int a2 = 0; a2 <numDocs - numCorrect; a2++) {
                double numer = 0;
                for(int i = 0; i < numFeat; i++) {
                    numer = numer + (semantic.get(numDocs-a1-1, i)-(double)mean[numDocs- a1-1])*(semantic.get(a2, i)-(double)mean[a2]);
                }
                numer = numer/(denom[numDocs-a1-1]*denom[a2]);
                corMat[a1][a2] = numer;

            }
        }
        
        
        double[] ratings = getRating(corMat);
        

        
        double [][] cosMat= new double[numCorrect][];
        for(int a1 = 0; a1 < numCorrect ; a1++) {
            cosMat[a1] = new double[numDocs];
            for (int a2 = 0; a2 <numDocs - numCorrect; a2++) {
                double numer = 0;
                for(int i = 0; i < numFeat; i++) {
                    numer = numer + (semantic.get(numDocs-a1-1, i))*(semantic.get(a2, i));
                }
                numer = numer/(sq[numDocs-a1-1]*sq[a2]);
                cosMat[a1][a2] = numer;

            }
        }
        
        double [] ratings1 = getRating(cosMat);
        int [] finRatings = new int[numDocs - numCorrect];
        for(int i = 0; i < numDocs - numCorrect; i++) {
            finRatings[i] = (int)ratings[i];
        }
        
        if(test) {
            double error = 0;
            for(int i = 0; i < numDocs - numCorrect; i++) {
               error += Math.abs((ratings[i] + ratings1[i])/2.0 - realRatings[i]);
            }
            error = (error*10)/((numDocs - numCorrect));
            System.out.println("Error "+error+"%");
            int [] arr = new int[1];
            arr[0] = (int)(error*1000);
            return arr;
        }
        
        return finRatings;
    }    	
}












