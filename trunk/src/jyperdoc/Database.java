package jyperdoc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 
 * @author Alfredo Portes
 * 
 */
public class Database {
	
	// Directory containing the Hyperdoc pages.
	static public String dbdir = "/jyperdoc/pages/";
	
	// Index file of the Hyperdoc pages.
	static public String pagesindex = "ht.db";	
	
	/**
	 * 
	 * @param filename of the page to processed.
	 * @return contents of the file {@filename}
	 */
	static public String getFileContent(String filename) {
		
		StringBuffer contents = new StringBuffer();

		// Declared here only to make visible to finally clause
	    BufferedReader input = null;
		try {
			
	         input = new BufferedReader(new InputStreamReader(
	        		 Object.class.getResourceAsStream(Database.dbdir + filename)));
			 String line = null; //not declared within while loop
	         if (input != null) {

	        	 while ((line = input.readLine()) != null) {
	   		      contents.append(line);
			      contents.append(System.getProperty("line.separator"));

	        	 }
	         } 
		 }
		 catch (FileNotFoundException ex) {
			 ex.printStackTrace();
		 }
		 catch (IOException ex){
			 ex.printStackTrace();
		 }
		 finally {
		   try {
		     if (input!= null) {
		         // flush and close both "input" and its underlying FileReader
		         input.close();
		      }
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		    }
		  }
		  return contents.toString();
	}
	
	
	/**
	 * The method searchDatabase takes a name of the 
	 * link that will be searched for in the database.
	 * and returns basic information about the page.
	 * To achieve this, the ht.db index file is used.
	 * 
	 * @param pagename of the page to be found.<br>
	 * @return String[] pageinfo:<br>
	 * Index 0: Page Name<br>
	 * Index 1: File Name in <strong>pages</strong> directory.<br>
	 * Index 2: Line number in the file.<br>
	 * Index 3: \begin{page} location.<br>
	 * 
	 * <strong>null: </strong> if the page could not be found in the
	 * index file. 
	 */
	static public String[] searchDatabase(String pagename) {
		
		String content = Database.getFileContent(Database.pagesindex);
	
		String[] pageinfo = new String[4];

		// Find the location of name we are looking for.
		int namestart = content.indexOf(" " + pagename + " ");

		// If nothing found return null
		if (namestart == -1)
			return null;
		
		// Now look for the next space.
		int nameend = content.indexOf(" ", namestart + pagename.length()) + 1;
		int charend = content.indexOf(" ", nameend);
		int lineend = content.indexOf("\n", charend + 1);
		
		// Store name.
		pageinfo[0] = pagename;
		
		// Store char number.
		pageinfo[3] = content.substring(nameend, charend);

		// Store line number.
		pageinfo[2] = content.substring(charend + 1, lineend);
		
		// Get substring from the start of the file
		// to the position of the name found.
		String filesub = content.substring(0, namestart);
		
		// Search this new substring for the last tab
		// occurrence. This will give you the file name
		// of the page we are looking for.
		int filestart = filesub.lastIndexOf("\t") + 1;
		
		// Now look for the space after the tab found.
		int fileend = content.indexOf(" ", filestart);
		
		// Now just extract the name of the file.
		pageinfo[1] = content.substring(filestart, fileend);
		
		return pageinfo;
	}
}
