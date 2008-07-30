package jyperdoc;

import javax.swing.*;

/**
 * 
 * @author Alfredo Portes
 * 
 */
public class MainWindow {

	private Pages rootpage;
	private Parser parser;
	
	/**
	 *  HyperDoc main constructor.
	 */
	public MainWindow() {

		this.parser = new Parser();
		this.rootpage = new Pages(this);
		
		// Initial values for the top buttons and main page
		// when starting the application.
		String[] buttonsnames = { "EXIT", "HELP", null, null};
		this.rootpage.setPageContent("RootPage", buttonsnames);
		//this.rootpage.setPageContent("ugHyperPage", buttonsnames);
		
		this.rootpage.setVisible(true);
	}
	
	/**
	 * @return the main page class object.
	 */
	public Pages getPage() {
		
		return this.rootpage;
	}
	
	/**
	 * @return the main parser class object.
	 */
	public Parser getParser() {
		
		return this.parser;
	}
	
	/**
	 * 
	 *  Exit() function allows for a clean
	 *  termination of the application when
	 *  clicking the exit button.
	 */
	public void exit() {
		
		System.exit(0);
	}
	
	/**
	 * 
	 * Creates a new page in a separate window.
	 * 
	 * @param url is the name of the page that 
	 * will be created.
	 * 
	 * @param parent is the name of the page 
	 * that is creating the new page.
	 */
	public void newPage(String url, String parentpage) {
    		
    	String[] buttonsnames = { "EXIT", "HELP", null, null};
    		
    	Pages newpage = new Pages(this);
		newpage.setHomePage(url);
	    newpage.setPageContent(url, buttonsnames);
	    newpage.setVisible(true);
	    newpage.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
	}
	
	/**
	 * Main function.
	 * 
	 * @param args is the arguments passed 
	 * to the application.
	 */
	public static void main(String[] args) {
				
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) { }
        new MainWindow();
	}

}
