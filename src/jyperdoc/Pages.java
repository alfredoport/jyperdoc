package jyperdoc;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * 
 * The pages class is the object containing all the different
 * parts to display a HyperDoc page.
 * 
 * @author Alfredo Portes
 *
 */
public class Pages extends JFrame {

	private int width;
	private int height;
	private TitleWindow titlewindow;
	private Body body;
	private MainWindow main;
	private Stack<String> previouspage;
	private String homepage;
	private String pagename;

	/**
	 * 
	 * @param main is the MainWindow object.
	 */
	public Pages(MainWindow main) {
		
		super("JyperDoc");
		this.computeWH();
		this.main = main;

		this.homepage = null;
		this.previouspage = new Stack<String>();
		
		this.setSize(new Dimension(this.width, this.height));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		
		// Set the status bar image icon.
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("bitmaps/ht_icon.bitmap"));

		// Put the windows in the center of the screen.
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}
	
	/**
	 * 
	 * @param pagename is the name of the page to be parsed and displayed.
	 * @param names is an array in the name of the buttons to be displayed
	 * in the TitleWindow.
	 */
	public void setPageContent(String pagename, String[] names) {
		
		// Get the information of the page.
		String pageinfo[] = Database.searchDatabase(pagename);
		
		// Parse the page from tex to html.
		String processedcontent[] = this.main.getParser().processContent(pageinfo);
		
		// Place the proper information in the TitleWindow.
		this.titlewindow = new TitleWindow(processedcontent[1], this);
		this.titlewindow.setButtons(names);
		this.setTitleWindow(titlewindow);
		
		// Set and display the content of the page. 
		this.body = new Body(processedcontent[2], Pages.getDimension(), this);
		this.setBody(this.body);
		
		this.pagename = pageinfo[0];
		this.setHomePage(this.pagename);
		this.repaint();
	}
	
	/**
	 * 
	 * @return the TitleWindow object for the
	 * current Page.
	 */
	public TitleWindow getTitleWindow() {
		
		return this.titlewindow;
	}
	
	/**
	 * 
	 * @return the Body object for the current
	 * Page.
	 */
	public Body getBody() {
		
		return this.body;
	}
	
	/**
	 * 
	 * @return a string of the name of the 
	 * current Page.
	 */
	public String getPageName() {
		
		return this.pagename;
	}
	
	/**
	 * 
	 * @param pagename sets the name for the
	 * current Page.
	 */
	public void setPageName(String pagename) {
		
		this.pagename = pagename;
	}
	
	/**
	 * 
	 * @return the MainWindow object.
	 */
	public MainWindow getMainWindow() {
		
		return this.main;
	}
	
	/**
	 * 
	 * @param pagename is the value of the parent
	 * Page for the the current Page. If the stack
	 * is empty it means that the current Page is
	 * a top level Page.
	 */
	public void setPreviousPage(String parentpage) {
		
		this.previouspage.push(parentpage);
	}
	
	/**
	 * 
	 * @param pagename is the name of the homepage
	 * for the current Page.
	 */
	public void setHomePage(String parentpage) {
		
		this.homepage = parentpage;
	}
	
	/**
	 * 
	 * @return the top string value in the stack, 
	 * which is the name of the previous Page. <br>
	 * If the stack is empty, return null.
	 */
	public String getPreviousPage() {
		
		if (this.previouspage.empty())
			return null;
		else
			return this.previouspage.pop();
	}
	
	/**
	 * 
	 * @return the previous Page data member.
	 */
	public Stack<String> getPreviousPageObject() {
		
		return this.previouspage;
	}
	
	/**
	 * 
	 * @return a string of the homepage
	 * for the current Page.
	 */
	public String getHomePage() {
		
		return this.homepage;
	}
	
	/**
	 * @param titlewindow is the TitleWindow object to 
	 * be set in the current Page. 
	 */
	public void setTitleWindow(TitleWindow titlewindow) {
		
		this.titlewindow = titlewindow;
		this.getContentPane().add("North", this.titlewindow);
		this.repaint();
	}
	
	/**
	 * 
	 * @param body is the Body object to be set
	 * for the current Page.
	 */
	public void setBody(Body body) {
		
		this.body = body;
		this.getContentPane().add("Center", this.body);
		this.repaint();
	}
	
	/**
	 *  computeHW gets the size of the screen and calculate a default
	 *  size for all the windows to be displayed using the width
	 *  and height data members.
	 */
	private void computeWH() {
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.width = (dim.width / 2) + 80;
		this.height = (dim.height / 2) + 85;
		
	}
	
	/**
	 * @return a default dimension based on the size of the screen.
	 */
	static public Dimension getDimension() {
	
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		return new Dimension((dim.width / 2) + 80, (dim.height / 2) + 85);
	}
	
}
