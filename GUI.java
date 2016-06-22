package eval;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;



public class GUI {

	/**
	 * @param args
	 */
	
	static String x;
	static JLabel selected1 = new JLabel("No file selected.");
    static JLabel selected2 = new JLabel("No file selected.");
        static int ansCount = 0;
        
	public static void listFilesForFolder(final File folder, boolean ans) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	           //  listFilesForFolder(fileEntry);
	        } else {
	        	String read = fileEntry.getName();
	        	if ( read.endsWith(".txt") ){
	        		System.out.println(fileEntry.getName());
                                if(ans) ansCount++;
                        }
                        
	        }
	    }
	}
	
	private static void handleFile(File file, Charset encoding)
            throws IOException {
        try {
            InputStream in = new FileInputStream(file);
             Reader reader = new InputStreamReader(in, encoding);
             // buffer for efficiency
             Reader buffer = new BufferedReader(reader);
            handleCharacters(buffer);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    private static void handleCharacters(Reader reader)
            throws IOException {
        System.out.println("Hello!");
        int r;
        StringBuilder builder = new StringBuilder();
        while ((r = reader.read()) != -1) {
            char ch = (char) r;
            if ( ch != '\n' && ch != '\r' ) {
            	builder.append(ch);
            	System.out.println("Do something with " + ch);
            } else {
            	System.out.println("Do something with <newline>");
//            	if ( ch == '\n' ) builder.append(' ');
            }
        }
        allAnswers.add(builder.toString());
    }

	static Vector< String > allAnswers = new Vector< String >();;
	static File answerfolder = new File("C:\\Users\\Divanshu\\Desktop\\try");
	static File studentfolder = new File("C:\\Users\\Divanshu\\Desktop\\try");
	
	public static void appendCompleteDirectory( File folder , boolean ans) throws IOException {
                File [] a = folder.listFiles();
                Arrays.sort(a);
		for (final File fileEntry : a) {
	        if (!fileEntry.isDirectory()) {
	        	String read = fileEntry.getName();
	        	if ( read.endsWith(".txt") ) {
                                if(ans) ansCount++;
	        		System.out.println("Reading .. " + fileEntry.getName());
	        		handleFile(new File(folder.toString()+"/"+read ),Charset.defaultCharset());	        		
	        	}
	        }
	    }
	}
	
	public static void printStringToFile() {
		try {
			System.out.println("Writing output..");
			OutputStream os = new FileOutputStream("inputGenerated.txt");
			for ( int i = 0; i < allAnswers.size(); ++i ) {
				System.out.println(allAnswers.get(i));
				for ( int j = 0; j < allAnswers.get(i).length(); ++j ) {
					os.write(allAnswers.get(i).charAt(j));
				} os.write('\n');
			}
		} catch ( Exception e ) {
			System.out.println("cannot openfile!");
		}
		System.out.println("Generation done!!" + ansCount);
		
	}
	
	
	
	public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Automated Evaluator");
        Container container1 = frame.getContentPane();
        JPanel container = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("<html><h1>Automated Subjective Answers</h1></html>");
        JLabel enterTraining = new JLabel("Enter the directory which contains the model answers:");
        JLabel enterTest = new JLabel("Enter the directory which contains the students answers:");
        container.setLayout(new BoxLayout(container,BoxLayout.PAGE_AXIS));
        container.add(title);
        container.add(enterTraining);
        JButton answerOpen = new JButton("Upload Model Answers..");
        answerOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
            	JFileChooser chooser = new JFileChooser();
            	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int returnValue = chooser.showOpenDialog( null ) ;
		
		        File file = null;
		        if( returnValue == JFileChooser.APPROVE_OPTION ) {
		               file = chooser.getSelectedFile() ;
		        }
		        if(file != null)
		        {
		        	 answerfolder = file;
		             String filePath = file.getPath();
		             System.out.println(filePath);
                             
		             selected1.setText(filePath+" selected.");
		        } 
            }
        });
        container.add(answerOpen);
        container.add(selected1);
        container.add(enterTest);
        JButton studentOpen = new JButton("Upload Student Answers..");
        studentOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
            	JFileChooser chooser = new JFileChooser();
            	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = chooser.showOpenDialog( null ) ;
		
		        File file = null;
		        if( returnValue == JFileChooser.APPROVE_OPTION ) {
		               file = chooser.getSelectedFile() ;
		        }
		        if(file != null)
		        {
		        	 studentfolder = file;
		             String filePath = file.getPath();
		             System.out.println(filePath);
		             selected2.setText(filePath+" selected.");
		        } 
            }
        });
        
        
        container.add(studentOpen);
        container.add(selected2);
        JButton submit = new JButton("<html><h1>Evaluate!!</h1></html>");
        submit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
            	 if ( answerfolder != null && studentfolder != null ) {
            		// order matters here!
            		 try {
        			appendCompleteDirectory(studentfolder,  false);
        			appendCompleteDirectory(answerfolder, true);
        			// print to a file!
        			printStringToFile();
                                main m = new main();
                                int [] rat = m.prediction(ansCount);
                                for(int i = 0;i < rat.length; i++) {
                                    System.out.println("Ratings "+i+" "+rat[i]);
                                }
                                Results r = new Results(rat, false);
                                r.setVisible(true);
            		 } catch ( Exception e ) {
            			 // Error!
            		 }
                         
            	 } else {
            		 System.out.println("Select both the files..");
            	 }
            }
        });
                 Border padding = BorderFactory.createEmptyBorder(25, 50, 25, 50);
                 container.setBorder(padding);
        container.add(submit);
//        panel.setVisible(true);
//        frame.add(panel);
        container1.add(container);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub 
//		listFilesForFolder(answerfolder);
		// order matters here!
//		appendCompleteDirectory(studentfolder);
//		appendCompleteDirectory(answerfolder);
//		// print to a file!
//		printStringToFile();
		answerfolder = null;
		studentfolder = null;
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}

}
