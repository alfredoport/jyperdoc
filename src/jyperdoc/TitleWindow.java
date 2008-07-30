package jyperdoc;

import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * @author Alfredo Portes
 *
 */
public class TitleWindow extends JPanel implements ActionListener {
	
	private JLabel title;
	private Border border;
	private JButton[] buttons;
	private Dimension dim;
	private Pages page;
	static private int numberbuttons = 4;
	
	/**
	 * 
	 * @param titlename
	 * @param names
	 */
	public TitleWindow(String titlename, Pages page) {
		
		this.page = page;
		this.dim = Pages.getDimension();
		
		this.setLayout(new FlowLayout());
		this.setBackground(Color.WHITE);
		
		this.border = BorderFactory.createLineBorder(Color.BLACK, 1);
		this.setBorder(this.border);
		
		this.buttons = new JButton[numberbuttons];
		for (int i = 0; i < numberbuttons; i++) {
			this.buttons[i] = new JButton("");
			this.buttons[i].setBorder(null);
			this.buttons[i].addActionListener(this);
		}
		
		this.title = new JLabel(titlename);
		this.title.setFont(new Font("Serif", Font.PLAIN, 16));
		this.title.setHorizontalAlignment(SwingConstants.CENTER);
		this.title.setPreferredSize(new Dimension(this.dim.width - 280, 25 ));
		this.title.setBorder(null);
		
		this.setPreferredSize(new Dimension(1500, 40));
		
		this.setElements();
		
		this.setVisible(true);
		this.repaint();
		
	}
	
	/**
	 * 
	 *
	 */
	public void setTitlePageName(String titlename) {
		
		this.title.setText(titlename);
		this.repaint();
	}
	
	/**
	 * 
	 * @return the title of the current Page.
	 */
	public String getTitlePagename() {
		
		return this.title.getText();
	}
	
	/**
	 * 
	 * Place the buttons and title string in
	 * the current container and layout.
	 *
	 */
	private void setElements() {
		
		for (int i = 0; i < numberbuttons + 1; i++) {
			
			// Set the first two buttons.
			if (i < 2) 
				this.add(this.buttons[i]);
			// Set the page name in the title.
			else if (i == 2) 
				this.add(this.title);
			// set the last two buttons.
			else
				this.add(this.buttons[i-1]);
		}
	}
	
	/**
	 * @param index of the button to be set.
	 * @param name to be set the button.
	 */
	public void setButton(int index, String name) {
			
		this.buttons[index].setText(name);
		this.buttons[index].repaint();
	}
	
	/**
	 * 
	 * @param names is the String array
	 * containing the names of the buttons
	 * to be displayed on the title window.
	 */
	public void setButtons(String[] names) {
		
		for (int i = 0; i < numberbuttons; i++) {
			
			if (names[i] == null)
				this.buttons[i].setIcon(new ImageIcon(Parser.currentdir + Parser.IMAGEDIR + "noop3d.bitmap"));
			
			else if (names[i].equals("EXIT")) {
					
				this.buttons[i].setIcon(new ImageIcon(Parser.currentdir + Parser.IMAGEDIR + "exit3d.bitmap"));
				this.buttons[i].setName("EXIT");
			}
			else if(names[i].equals("HELP")) {
					
				this.buttons[i].setIcon(new ImageIcon(Parser.currentdir + Parser.IMAGEDIR + "help3d.bitmap"));
				this.buttons[i].setName("HELP");
			}
			else if(names[i].equals("HOME")) {
					
				this.buttons[i].setIcon(new ImageIcon(Parser.currentdir + Parser.IMAGEDIR + "home3d.bitmap"));
				this.buttons[i].setName("HOME");
			}
			else if(names[i].equals("UP")) {
					
				this.buttons[i].setIcon(new ImageIcon(Parser.currentdir + Parser.IMAGEDIR + "up3d.bitmap"));
				this.buttons[i].setName("UP");
			}
			this.buttons[i].repaint();
		}
	}

	/**
	 * 
	 * The actionPerfromed method takes care of handling
	 * the events produced by the 4 buttons in the TitleWindow.
	 * 
	 */
	public void actionPerformed (ActionEvent e)
	{
		String source = ((JButton)e.getSource()).getName();
		
		if (source == null)
			return;
		
		else if (source.equals("HELP")) {
			
			this.page.setPreviousPage(this.page.getPageName());
			
		    if  (this.page.getPreviousPageObject().empty()) {
				String[] names = {"EXIT", "HELP", null, null};
	    	    this.setButtons(names);
		    }
		    else { 
		    	String[] names = { "EXIT", "HELP", "HOME", "UP"};
		    	this.setButtons(names);
		    }
			
	    	this.page.setHomePage(this.page.getPageName());
	    	
		    String[] pageinfo = Database.searchDatabase("ugHyperPage");
	    	String locations[] = this.page.getMainWindow().getParser().processContent(pageinfo);
			
	    	this.page.setPageName(locations[0]);
	    	this.page.getBody().setContent(locations[2]);
			this.setTitlePageName(locations[1]);
		}
		else if (source.equals("EXIT"))	
			this.page.dispose();
		
		else if (source.equals("UP")) {
			
			// Find out which is the previous Page for the
			// current Page.
			String previouspage = this.page.getPreviousPage();
			
			// If it is null it means this is a top level Page.
			// This actually should never happen, because the UP
			// button should not be visible to the user if this
			// is already a top level page.
			if (previouspage == null)
				return;
			
			// Get the information and content of the previous Page.
			String pageinfo[] = Database.searchDatabase(previouspage);
	    	String locations[] = this.page.getMainWindow().getParser().processContent(pageinfo);
	    	
	    	// If the stack of previous pages becomes empty,
	    	// then set the TitleWindow buttons as a top
	    	// level Page (no homepage and no up).
		    if  (this.page.getPreviousPageObject().empty()) {
				String[] names = {"EXIT", "HELP", null, null};
		    	this.setButtons(names);
		    }
		    
			if (!(this.page.getHomePage().equals(this.page.getPageName()))) {
		    	
		    	this.page.setPageName(previouspage);
			    this.setTitlePageName(locations[1]);
			    this.page.getBody().setContent(locations[2]);
			    
			}
		}
		else if (source.equals("HOME")) {
			
			// Find out which one is the homepage
			// of the current page.
			String homepage = this.page.getHomePage();
			
			// Clear the stack of the previouspages.
			this.page.getPreviousPageObject().removeAllElements();
			
			// Set the TitleWindow buttons.
			String[] names = { "EXIT", "HELP", null, null};
	    	this.setButtons(names);
	    
	    	// Get the homepage content.
	    	String pageinfo[] = Database.searchDatabase(homepage);
	    	String processedcontent[] = this.page.getMainWindow().getParser().processContent(pageinfo);
	    	
	    	// Display the Homepage
	    	this.page.setPageName(homepage);
		    this.page.getTitleWindow().setTitlePageName(processedcontent[1]);
		    this.page.getBody().setContent(processedcontent[2]);
		}
	}
}
