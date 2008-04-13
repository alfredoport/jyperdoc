package jyperdoc;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.border.*;

/**
 * 
 * @author Alfredo Portes
 *
 */
public class Body extends JPanel implements HyperlinkListener {
	
	/**
	 * 
	 */
	private JEditorPane content;
	private JScrollPane pictureScrollPane;
	private Pages pages;
	
	/**
	 * 
	 * @param content
	 * @param dim
	 */
	public Body (String content, Dimension dim, Pages pages) {

		this.pages = pages;		
		this.content = new JEditorPane("text/html", content);
		this.content.setBackground(Color.WHITE);
		this.content.setEditable(false);
		this.content.addHyperlinkListener(this);
		this.content.setFont(new Font("Serif", Font.PLAIN, 16));
		
        // Put it in a scroll pane
		//this.pictureScrollPane = new JScrollPane(this.content,
		//			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		//			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.pictureScrollPane = new JScrollPane(this.content,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);	
		
		this.pictureScrollPane.setPreferredSize(new Dimension(dim.width - 15, dim.height - 80));
		this.pictureScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));		
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		this.setBorder(border);
		
		// The HyperDoc white background.
		this.setBackground(Color.WHITE);

		this.add(this.pictureScrollPane);
	}
	
	/**
	 * 
	 */
	public void hyperlinkUpdate(HyperlinkEvent event) {
		
	    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	    	
	    	// Get the link description.
	    	String url = event.getDescription().toString();
	    	
	    	//Split by :// to get the type of link and the link page.
	    	String[] urls = url.split("://");
	    	
	    	// Save the name of the link and type of link.
	        String typeofurl = urls[0];
	    	url = urls[1];
	    	
	    	// Get the information for the page in the link.
	    	String[] pageinfo = Database.searchDatabase(url);
	    	
	    	// If the page cannot be found.
	    	if (pageinfo == null) {
	    		
	    		this.content.setText("Page not implemented yet.");
	    		this.pages.setPreviousPage(this.pages.getPageName());
	    		this.pages.setPageName("EMPTY");
		    	this.pages.getTitleWindow().setTitlePageName("Empty Page");
		    	String[] names = { "EXIT", "HELP", "HOME", "UP"};
		    	this.pages.getTitleWindow().setButtons(names);
		    	return;
	    		
	    	}
	    	
	    	// Get content of the page to be displayed.
	    	String positions[] = this.pages.getMainWindow().getParser().processContent(pageinfo);
	    	
	    	// The link creates a new HyperDoc page.
	    	if (typeofurl.equals("mwl"))
	    		this.pages.getMainWindow().newPage(url, this.pages.getPageName());
	    	// or display the links in the current page.
	    	else if (typeofurl.equals("mml") || typeofurl.equals("ml") || typeofurl.equals("mdl") || typeofurl.equals("dl")) {
	    		
	    		this.pages.setPreviousPage(this.pages.getPageName());
	    		this.pages.setPageName(positions[0]);
		    	this.pages.getTitleWindow().setTitlePageName(positions[1]);
		    	String[] names = { "EXIT", "HELP", "HOME", "UP"};
		    	this.pages.getTitleWindow().setButtons(names);
		    	this.pages.getBody().setContent(positions[2]);
		    	
	    	}
	    }   
	}
	
	/**
	 * 
	 * @param content sets the content to
	 * be displayed in the body of the page.
	 */
	public void setContent(String content) {
		
		this.content.setText(content);
	}
	
	/**
	 * 
	 * @return a String of the current 
	 * content of the body.
	 */
	public String getContent() {
		
		return this.content.getText();
	}
}
