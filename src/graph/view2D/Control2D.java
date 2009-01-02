package graph.view2D;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * 
 * Control2D is the menu to control the different
 * visual attributes of a graph.
 * 
 * @author Alfredo Portes
 * @author Jose Mestizo
 *
 */

public class Control2D extends JFrame {

	private JLabel translate;
	private JLabel scale;

	private int width;
	private int height;
	
	/**
	 * 
	 */
	public void drawControlPanel() {
		
		this.setTitle("2D Control Panel");
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		this.translate = new JLabel("Translate");
		this.scale = new JLabel("Scale");
		
		this.width = 245;
		this.height = 430;
		
		this.setSize(new Dimension(this.width, this.height));
		
		this.add(this.scale);
		this.add(this.translate);
		
		this.setVisible(true);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) { }
		
		Control2D c = new Control2D();
		
		c.drawControlPanel();
	}

}
