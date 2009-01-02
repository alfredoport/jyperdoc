package graph.view2D;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.geom.Line2D;

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
 *
 */

public class Control2D extends JFrame {

	private JLabel translate;
	private JLabel graphs;
	private JLabel scale;
	private JPanel panel;
	
	public Control2D() {
		
		//this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public void drawControlPanel() {
		
		this.setTitle("2D Control Panel");
		
		this.translate = new JLabel("Translate");
		this.graphs = new JLabel("Graphs");
		this.scale = new JLabel("Scale");
		
		//this.add(this.scale);
		//this.add(this.graphs);
		//this.add(this.translate);
		this.panel = new JPanel();
		this.panel.setSize(200,70);
		this.panel.setVisible(true);

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) { }
		
		Control2D c = new Control2D();
		c.setSize(350,80);
		c.setVisible(true);
		
		c.drawControlPanel();
		
		

	}

}
