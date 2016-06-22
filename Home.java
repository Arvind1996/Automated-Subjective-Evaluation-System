package eval;


import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabber
 */
public class Home extends JFrame{
    public Home() {
        initComponents();
    }
    public void initComponents() {
        setTitle("Main Window!");
        
	setLocation(10,200); // default is 0,0 (top left corner)
	
		// Window Listeners
        addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
                System.exit(0);
          } //windowClosing
        }); //addWindowLister
	
		// Add Panels
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        JPanel pan = new JPanel();
        
        JLabel title = new JLabel("<html><h1>Automated Subjective Answers</h1></html>");

        JLabel mode = new JLabel("<html><h3>Function Mode</h3></html>");
        JButton testing = new JButton("<html><h2>Testing Mode</h2></html>");
        JButton pred = new JButton("<html><h2>Prediction Mode</h2></html>");
        pan.setLayout(new BoxLayout(pan,BoxLayout.PAGE_AXIS));
                Border padding = BorderFactory.createEmptyBorder(25, 50, 25, 50);

        pan.add(title);
        pan.add(mode);
        pan.add(testing);
        pan.add(pred);
//        pred.setBorder(padding);
        pan.setBorder(padding);
        
        
        contentPane.add(pan);
        this.pack();
        testing.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
            	main eval = new main();
                int [] err = eval.testing();
                System.out.println("Error = "+ (err[0]/1000.00)+"%");
            }
        });
        pred.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
            	GUI g = new GUI();
                g.createAndShowGUI();
            }
        });
    }
    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        Home h = new Home();
        h.setVisible(true);
    }
}
