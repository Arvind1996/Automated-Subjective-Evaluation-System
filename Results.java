/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eval;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author gabber
 */
public class Results extends JFrame{
    public int[] rat;
    public Results(int [] a, boolean test) {
        rat = a;
        initComponents(test);
    }
    public void initComponents(boolean test) {
        
        setTitle("Main Window!");
        
	setLocation(10,200); // default is 0,0 (top left corner)
	
		// Window Listeners
        addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
                System.exit(0);
          } //windowClosing
        }); //addWindowLister
	
		// Add Panels
        
        String ans = new String("<html>");
        String t = new String("<html><h3>Test Results</h3></html>");
        if(test) {
            //ans = ans + new String("<h2>Error "+(rat[0]/1000.0)+"%</h2>");
            ans = ans + new String("<h2>Accuracy "+(100.0-rat[0]/1000.0)+"%</h2>");        } else{
            t = new String("<html><h3>Predicted Ratings</h3></html>");
            for(int i = 0; i < rat.length; i++) {
                ans = ans + new String("<h2>Rating #"+(i+1)+" .............. ");
                ans = ans + rat[i] ;
                ans = ans + new String("</h2>");
            }
        }
        ans = ans + new String("</html>");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        JLabel title = new JLabel("<html><h1>Automated Subjective Answers</h1></html>");
        JPanel pan = new JPanel();
        JLabel mode = new JLabel(t);
        System.out.println(ans);
        JLabel results = new JLabel(ans);
        pan.setLayout(new BoxLayout(pan,BoxLayout.PAGE_AXIS));
        
        pan.add(title);
        pan.add(mode);
        pan.add(results);
        
         Border padding = BorderFactory.createEmptyBorder(25, 50, 25, 50);
         pan.setBorder(padding);
         contentPane.add(pan);
         pack();
    }
}
