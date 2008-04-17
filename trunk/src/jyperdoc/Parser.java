package jyperdoc;

import java.io.*;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Parser class contains the methods needed
 * to process the HyperDoc ht pages, which are 
 * written in a tex like syntax, and convert
 * them into html that Java can display. 
 * 
 * @author Alfredo Portes
 *
 */
public class Parser {

	/**
	 * 
	 * Static variables to be used in the processing 
	 * of the HyperDoc pages.
	 * 
	 */
	static private String LANGUAGE = "AXIOM";
	static private String HYPERNAME = "JyperDoc";
	static public String IMAGEDIR = "/jyperdoc/bitmaps/";
	static private String LOGO = "/bitmaps/axiom1.bitmap";
	static private String ASTYLE = "style='color: black; font-weight: bold; text-decoration: none'";
	static private Pattern BACKWORDB = Pattern.compile("^\\\\([a-zA-Z]+\\s*\\{)");
	static private Pattern BACKSYMBOL = Pattern.compile("^\\{\\\\([a-zA-Z]+\\s+)");
	static private Pattern BACKWORD = Pattern.compile("^\\\\([a-zA-Z]+)");
	static private Pattern COMMENT = Pattern.compile("^(%.*)");
	static private Pattern CLOSEBRACKET = Pattern.compile("^\\}");
	static private Pattern OPENBRACKET = Pattern.compile("^\\{");
	static private Pattern BREAKLINE = Pattern.compile("^\\n");
	static private Pattern WORD = Pattern.compile("^(\\p{Alnum}+)");
	static private Pattern NUMBER = Pattern.compile("(^[0-9]+)");
	static private Pattern PUNCTUATION = Pattern.compile("^\\p{Punct}");
	//static private Pattern SPACE = Pattern.compile("^\\s+");
	//static private Pattern BRACKET = Pattern.compile("^(\\}\\{)");
	static public String currentdir;
	static private String DOTIMAGE;

	
	/**
	 * 
	 * @param main HyperDoc class object.
	 */
	public Parser() {

		// Get the current working directory.
		currentdir = (new File("")).getAbsolutePath();
		
		// Check if we are in Windows.
		if (currentdir.indexOf(":") != -1) {
			
			currentdir = currentdir.replaceFirst(".:", "");
			currentdir = currentdir.replaceAll("\\\\", "/");
		}
		
		DOTIMAGE = new String("<img src='file:" + currentdir.trim()  + Parser.IMAGEDIR + "menudot.bitmap" + "'/>");

	}
	
	/**
	 * 
	 * @param pageinfo
	 * @return 
	 * @throws IOException 
	 */
	public String[] processContent(String[] pageinfo) {
				
	    // Get the contents for the ht file specified in pageinfo[1].
		String filecontent = Database.getFileContent(pageinfo[1]);
		
		// Get the information of the page the the processed.
		String pagecontents[] = Parser.getPageContents(pageinfo, filecontent);
		
		if (pagecontents == null)
			return Parser.processingError();
		
		// Content will be used to convert the tex syntax to html.
		String content = pagecontents[2];
		//System.out.println(content);
		
		Vector<String> tokens = Parser.createTokens(content);
		//System.out.println("tokens: " + tokens);
		Vector<String> processedtokens = Parser.processTokens(tokens);
		//System.out.println("ptokens: " + processedtokens);
		
		String title = pagecontents[1];
		Vector<String> tokens2 = Parser.createTokens(title);
		Vector<String> processedtokens2 = Parser.processTokens(tokens2);
		
		Vector<String> reversedlinks = Parser.reverseLinks(processedtokens);
		//System.out.println("rlinks: " + reversedlinks);
		
		Vector<String> cleanedtokens = Parser.cleanTokens(reversedlinks);
		
		String html = createHTML(cleanedtokens);
		title = createHTML(processedtokens2);
		//System.out.println("html: " + html);
		
		//html = createHTMLLinks(html);
		//System.out.println("htmllinks: " + html);
		
		pagecontents[2] = html;
		pagecontents[1] = title;
		return pagecontents;	
	}
	
	/**
	 * 
	 * @param content
	 * @return
	 */
	static public Vector<String> createTokens(String content) {
		
		Vector<String> tokens = new Vector<String>(0);
		Matcher m;
		
		while (!content.equals("")) {
			
			if ((m = BACKSYMBOL.matcher(content)).find()) { 
				
				String token = new String(m.group(0));
				tokens.add("backsymbol");
				tokens.add(token);
				content = m.replaceFirst("");
				m.reset();
			}
			else if ((m = BACKWORDB.matcher(content)).find()) { 
				
				String token = new String(m.group(0));
				tokens.add("backwordb");
				tokens.add(token);
				content = m.replaceFirst("");
				m.reset();
			}
			else if ((m = BACKWORD.matcher(content)).find()) { 
				
				String token = new String(m.group(0));
				tokens.add("backword");
				tokens.add(token);
				content = m.replaceFirst("");
				m.reset();
			}
			else if ((m = OPENBRACKET.matcher(content)).find()) { 
				
				String token = new String(m.group(0));
				tokens.add("openbracket");
				tokens.add(token);
				content = m.replaceFirst("");
				m.reset();
			}
			else if ((m = CLOSEBRACKET.matcher(content)).find()) { 
				
				String token = new String(m.group(0));
				tokens.add("closebracket");
				tokens.add(token);
				content = m.replaceFirst("");
				m.reset();
			}
			else if ((m = COMMENT.matcher(content)).find()) { 
				
				String token = new String(m.group(0));
				tokens.add("comment");
				tokens.add(token);
				content = m.replaceFirst("");
				m.reset();
			}
			else if ((m = BREAKLINE.matcher(content)).find()) { 
				
				//String token = new String(m.group(0));
				//tokens.add("breakline");
				//tokens.add(token);
				content = m.replaceFirst("");
				m.reset();
			}
			else if ((m = WORD.matcher(content)).find()) { 
				
				String token = new String(m.group(0));
				tokens.add("word");
				
				// We want the spaces after a word.
				// We are getting only 1 space. Other
				// spaces are lost. Maybe a function
				// needs to be implemented to get them all.

				if (m.end(0) < content.length() && content.charAt(m.end(0)) == ' ')
					tokens.add(token + " ");
				else
					tokens.add(token);
				
				content = m.replaceFirst("");
				m.reset();
			}
			else if ((m = NUMBER.matcher(content)).find()) { 
				
				String token = new String(m.group(0));
				tokens.add("number");
				tokens.add(token);
				content = m.replaceFirst("");
				m.reset();
			}
			else if ((m = PUNCTUATION.matcher(content)).find()) { 
				
				String token = new String(m.group(0));
				tokens.add("punctuation");
				tokens.add(token);
				content = m.replaceFirst("");
				m.reset();
			}
			else
				content = "";
			
			content = content.trim();
		}
		
		return tokens;	
	}

	/**
	 * 
	 * @param tokens
	 * @return
	 */
	static public Vector<String> processTokens(Vector<String> tokens) {
		
		Vector<String> content = new Vector <String>(0);
		
		for (int i = 0; i < tokens.size(); i += 2) {
			
		    String element = tokens.elementAt(i);
			
		    if (element.equals("openbracket")) {
				
				content.add("openbracket");
				content.add(tokens.elementAt(i+1));
			}
		    else if (element.equals("closebracket")) {
				
				content.add("closebracket");
				content.add(tokens.elementAt(i+1));
			}
			else if (element.equals("word")) {
				
				content.add("word");
				content.add(tokens.elementAt(i+1));
			}
			else if (element.equals("number")) {
				
				content.add("number");
				content.add(tokens.elementAt(i+1));
			}
			else if (element.equals("punctuation")) {
				
				content.add("punctuation");
				content.add(tokens.elementAt(i+1));
			}
			else if (element.equals("backsymbol")) {
				
				content.add("backsymbol");
				content.add(Parser.processBackWordB(tokens, i));
				
			}
			else if (element.equals("backwordb")) {
				
				content.add("backwordb");
				content.add(Parser.processBackWordB(tokens, i));
			}
			else if (element.equals("backword")) {
					
				String backword = Parser.processBackWord(tokens.elementAt(i+1));
				if (backword != null) {
					content.add("backword");
					content.add(backword);
				}
			}				
		}
		
		return content;
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	static public String processBackWordBX(String elements) {
		
		String element = new String(elements + "}");
		
		if (element.matches("\\\\Language\\s*\\{\\s*\\}"))
			return LANGUAGE;
		
		else if (element.matches("\\\\HyperName\\s*\\{\\s*\\}"))
			return HYPERNAME;
		
		else if (element.matches("\\\\htbmdir\\s*\\{\\s*\\}"))
			return IMAGEDIR;
		
		else if (element.matches("\\\\space\\s*\\{\\s*\\}"))
			return "";
		
		else if (element.matches("\\\\ixpt\\s*\\{\\s*\\}"))
			return "";
		else
			return null;
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	static public String processBackWord(String element) {
		
		if (element.equals("\\pp"))
			return "";
		else if (element.equals("\\newline"))
			return "<br/>";
		else if (element.equals("\\horizontalline"))
			return "<hr/>";
		else
			return null;
	}
	
	/**
	 * 
	 * @param html
	 * @return
	 */
	public String createHTMLLinks(String html) {
		
		html = html.replaceAll("<start>","<br>" + DOTIMAGE + "<a " + Parser.ASTYLE + " href='");
		html = html.replaceAll("</end>", "</a>");
		html = html.replaceAll("<middle>", "'>");
		return html;
	}
	
	/**
	 * 
	 * @param tokens
	 * @param i
	 * @return
	 */
	static public String processBackWordB(Vector<String> tokens, int i) {
		
		String element = tokens.elementAt(i+1);
		int position = Parser.findCloseBracket(tokens, i);
		String command;
		
		if (element.matches("\\\\menuwindowlink\\{")) {
					
			// Find the first close
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
			position = Parser.findOpenBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("'>", position + 1);
				//tokens.setElementAt("fopenbracket", position);
			}
			position = Parser.findCloseBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("</a>", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
			return "<br/>" + DOTIMAGE + "<a " + Parser.ASTYLE + " href='mwl://";
			
		}
		else if (element.matches("\\\\downlink\\{")) {
			
			if (position < tokens.size())
				tokens.setElementAt("", position + 1); 
			
			position = Parser.findOpenBracket(tokens, position);
			if (position < tokens.size())
				tokens.setElementAt("'>", position + 1);
			
			position = Parser.findCloseBracket(tokens, position);
			if (position < tokens.size())
				tokens.setElementAt("</a>", position + 1); 

			return "<br/><a " + Parser.ASTYLE + " href='dl://";
		}
		else if (element.matches("\\\\menuitemstyle\\{")) {
			
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
			return "";	

		}
		else if (element.matches("\\\\menudownlink\\{")) {
		
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
				
			position = Parser.findOpenBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("'>", position + 1);
				//tokens.setElementAt("fopenbracket", position);
			}
			
			position = Parser.findCloseBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("</a>", position + 1);
				//tokens.setElementAt("fclosebracket", position);
			}
			return "<br/>" + DOTIMAGE + "<a " + Parser.ASTYLE + " href='mdl://";

		}
		else if (element.matches("^\\\\menumemolink\\{")) {
				
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
			
			position = Parser.findOpenBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("'>", position + 1);
				//tokens.setElementAt("fopenbracket", position);
			}
			position = Parser.findCloseBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("</a>", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}	
			return "<br/>" + DOTIMAGE + "<a " + Parser.ASTYLE + " href='mml://";
				
		}
		else if (element.matches("^\\\\menulink\\{")) {
			
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
			
			position = Parser.findOpenBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("'>", position + 1);
				//tokens.setElementAt("fopenbracket", position);
			}
			position = Parser.findCloseBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("</a>", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}	
			return "<br/>" + DOTIMAGE + "<a " + Parser.ASTYLE + " href='ml://";
				
		}/*
		else if (element.matches("^\\\\labelSpace\\{")) {
			
			if (position < tokens.size())
				tokens.setElementAt("</labelspace>", position + 1); 
				
			return "<labelspace>";
				
		}
		else if (element.matches("^\\\\xtc\\{")) {
			
			if (position < tokens.size())
				tokens.setElementAt("</xtc>", position + 1); 
				
			return "<xtc>";
				
		}*/
		else if (element.matches("^\\\\menulispmemolink\\{")) {
			
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
			
			position = Parser.findOpenBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("'>", position + 1);
				//tokens.setElementAt("fopenbracket", position);
			}
			position = Parser.findCloseBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("</a>", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}	
			return "<br/>" + DOTIMAGE + "<a " + Parser.ASTYLE + " href='mlml://";
		}/*
		else if (element.matches("\\\\ignore\\{")) {
			
			if (position < tokens.size())
				tokens.setElementAt("</ignore>", position + 1); 
				
			return "<ignore>";
		}
		else if (element.matches("\\\\spadfunFrom\\{")) {
			
			if (position < tokens.size())
				tokens.setElementAt("</end>", position + 1); 
				
			return "<start>sff://";
		}*/
		
		else if (element.matches("\\\\menulispdownlink\\{")) {
			
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
			
			position = Parser.findOpenBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("'>", position + 1);
				//tokens.setElementAt("fopenbracket", position);
			}
			position = Parser.findCloseBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("</a>", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}	
			return "<br/>" + DOTIMAGE + "<a " + Parser.ASTYLE + " href='mldl://";		
		}
		else if (element.matches("\\\\menuunixlink\\{")) {
			
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
			
			position = Parser.findOpenBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("'>", position + 1);
				//tokens.setElementAt("fopenbracket", position);
			}
			position = Parser.findCloseBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("</a>", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}	
			return "<br/>" + DOTIMAGE + "<a " + Parser.ASTYLE + " href='mul://";	
		}
		else if (element.matches("\\\\centerline\\{")) {
			
			if (position < tokens.size()) {
				tokens.setElementAt("</center>", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
			return "<center>";	
		}
		else if (element.matches("\\\\Isize\\{")) {
			
			if (position < tokens.size()) {
				tokens.setElementAt("</span>", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
			return "<span>";	
		}
		else if (element.matches("\\\\inputbitmap\\{")) {
			
			if (position < tokens.size()) {
				tokens.setElementAt("'>", position + 1);
				//tokens.setElementAt("fclosebracket", position);
			}	
			return "<img src='file://" + currentdir.trim();
		}/*
		else if (element.matches("\\\\inputstring\\{")) {
			
			if (position < tokens.size())
				tokens.setElementAt("'", position + 1); 
				
			return "<input type='text' name='";
		}*/
		
		// I need to fix the css of this tag
		// to leave a proper space as intended 
		// with the tag \tab{#}
		else if (element.matches("\\\\tab\\{")) {
			
			if (position < tokens.size())
				tokens.setElementAt("</span>", position + 1); 
			
			return "<span style='color:#FFFFFF;'>";	
		}
		else if (element.matches("\\\\autobutt\\{")) {
			
			if (position < tokens.size()) {
				tokens.setElementAt("</span>", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
			
			return "<span style='color:#FFFFFF;'>";
		}/*
		else if (element.matches("\\\\stringvalue\\{")) {
			
			if (position < tokens.size())
				tokens.setElementAt("</stringvalue>", position + 1); 
			
			return "<stringvalue>";	
		}*/
		else if (element.matches("\\\\texht\\{")) {
			
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
			position = Parser.findOpenBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1);
				//tokens.setElementAt("fopenbracket", position);
			}
			position = Parser.findCloseBracket(tokens, position);
			if (position < tokens.size()) {
				tokens.setElementAt("</span>", position + 1); 
				//tokens.setElementAt("fclosebracket", position);
			}
				
			return "<span>";
		}/*
		else if (element.matches("\\\\axiomFunFrom\\{")) {
				
			if (position < tokens.size())
				tokens.setElementAt("</end>", position + 1); 

			return "<start>ml://";
		}*/
		else if (element.matches("\\{\\\\bf\\s+")) {
			
			if (position < tokens.size()) {
				tokens.setElementAt("</strong>", position + 1);
				tokens.setElementAt("fclosebracket", position);		
			}
			
			return "<strong>";	
		}/*
		else if (element.matches("^\\{\\\\em\\s+")) {
			
			if (position < tokens.size())
				tokens.setElementAt("</strong>", position + 1); 
			
			return "<strong>";
		}*/
		else if (element.matches("^\\\\table\\{")) {
			
			if (position < tokens.size())
				tokens.setElementAt("</table>", position + 1); 
			
			return "<table>";			
		}
		else if (element.matches("^\\\\spadtype\\{")) {
			
			if (position < tokens.size())
				tokens.setElementAt("</spadtype>", position + 1); 
			
			return "<spadtype>";			
		}
		else if (element.matches("^\\\\spadpaste\\{")) {
			
			if (position < tokens.size())
				tokens.setElementAt("</spadpaste>", position + 1); 
			
			return "<spadpaste>";			
		}/*
		
		
		else if (element.matches("^\\{\\s*")) {
			
			if (position < tokens.size())
				tokens.setElementAt("", position + 1); 
			
			return "";
		}*/
		else if (element.matches("\\{")) {
			
			position = Parser.findCloseBracket(tokens, i);
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1);
				//tokens.setElementAt("fclosebracket", position);
			}
			
			return "";
		}
		else if ((command = processBackWordBX(element)) != null) {
			
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1);
				//tokens.setElementAt("fclosebracket", position);
			}
			
			return command;
	    }
		else {
			
			if (position < tokens.size()) {
				tokens.setElementAt("", position + 1);
				//tokens.setElementAt("", position);
			}
			
			return "";
		}
	}
	
	/**
	 * 
	 * @param content
	 * @return
	 */
	static public Vector<String> reverseLinks(Vector<String> content) {
		
		for (int i = 0; i < content.size(); i++) {
		
			if (content.elementAt(i).matches("<br/>" + DOTIMAGE + "<a.+" + "|" +
					"<br/>" + "<a.+")) {

				int end = Parser.findEndTag(content, i);
				int middle = Parser.findMiddleTag(content, i);
				
				Vector <String> right = new Vector<String>(0);
				for (int j = middle+1 ; j < end-1; j++)
					right.add(content.elementAt(j));
				
				right.add("openbracket");
				right.add("'>");
				
				for (int j = i+1 ; j < middle-1; j++)
					right.add(content.elementAt(j));
				
				for (int k = 0; k < right.size(); k++) 					
					content.setElementAt(right.elementAt(k), i+1+k);

			}
		}
		
		return content;
	}
	
	/**
	 * 
	 * @param content
	 * @param position
	 * @return
	 */
	static public int findEndTag(Vector<String> content, int position) {
	    
		int level = 1;
	    position = position + 1;
	    while ( level > 0 && position < content.size()) {
	        
	    	if (content.elementAt(position).matches("</a>")) 		
	    		level--;
	    		
	    	if (content.elementAt(position).matches("<br/>" + DOTIMAGE + "<a.+" + "|" +
					"<br/>" + "<a.+")) 		
	    		level++;
	    	
	    	position++;
	    }
	    
	    return position - 1;
	}
	
	/**
	 * 
	 * @param content
	 * @param position
	 * @return
	 */
	static public int findMiddleTag(Vector<String> content, int position) {
	    
		int level = 1;
	    position = position + 1;
	    while ( level > 0 && position < content.size()) {
	        
	    	if (content.elementAt(position).matches("'>")) 		
	    		level--;
	    		
	    	if (content.elementAt(position).matches("<br/>" + DOTIMAGE + "<a.+" + "|" +
					"<br/>" + "<a.+")) 		
	    		level++;
	    	
	    	position++;
	    }
	    
	    return position - 1;
	}
	
	
	/**
	 * 
	 * @param content
	 * @return
	 */
	static public String createHTML(Vector<String> content) {
		
		String output = "";
		
		for (int i = 0; i < content.size(); i+=2)
			output = output + content.elementAt(i+1);

		return output;
	}
	
	/**
	 * 
	 * @param content
	 * @param position
	 * @return
	 */
	static public int findCloseBracket(Vector<String> content, int position) {
		
	    int level = 1;

	    position = position + 2;
	    while ( level > 0 && position < content.size()) {
	        if (content.elementAt(position).equals("backwordb")) level++;
	        if (content.elementAt(position).equals("backsymbol")) level++;
	        if (content.elementAt(position).equals("openbracket")) level++;
	        if (content.elementAt(position).equals("closebracket")) level--;
	        position = position + 2;
	    }
	    
	    return position-2;
	}
	
	/**
	 * 
	 * @param content
	 * @param position
	 * @return
	 */
	static public int findOpenBracket(Vector<String> content, int position) {
		
	    int level = 1;

	    position = position + 2;
	    while ( level > 0 && position < content.size()) {
	        if (content.elementAt(position).equals("backwordb")) level++;
	        if (content.elementAt(position).equals("backsymbol")) level++;
	        //if (content.elementAt(position).equals("openbracket")) level++;
	        if (content.elementAt(position).equals("openbracket")) level--;
	        position = position + 2;
	    }
	    
	    return position-2;
	}

	/**
	 * 
	 * @param html
	 * @return
	 */
	static public Vector<String> cleanTokens(Vector<String> html) {
		
		for (int i = 0; i < html.size(); i+=2) {
			
			if (html.elementAt(i).equals("openbracket")) {
				
				if (html.elementAt(i+1).matches("\\{")) {
					
					html.set(i+1, "");
				}
			}
			else if (html.elementAt(i).equals("closebracket")) {
				
				if (html.elementAt(i+1).matches("\\}")) {
					
					html.set(i+1, "");
				}
			}
		}
		
		return html;
		
	}
	
	/**
	 * 
	 * @param pageinfo is the basic information
	 * needed to parse the file.
	 * 
	 * @param filecontent is the actual string
	 * containing the content of the file where
	 * the page to be processed is stored.
	 * 
	 * @return pagecontent[] <br>
	 * pagecontent[0]: Page title <br>
	 * pagecontent[1]: Page name <br>
	 * pagecontent[2]: Page content <br>
	 */
	static public String[] getPageContents(String pageinfo[], String filecontent) {
		
		String pagecontent[] = new String[3];
		
		// Go to the line specified on pageinfo[2]
		// and get the information in this line.
		// This is of the form: \begin{page}{title}{pagename}
		int endline = 0;
		int line = Integer.parseInt(pageinfo[2].trim());
		
		for (int i = 0; i < line - 1; i++) {
		
			// Eliminate all the useless lines until
			// we get to the target page line by line.
			endline = filecontent.indexOf("\n");
			filecontent = filecontent.substring(endline + 1, filecontent.length());
		}
		endline = filecontent.indexOf("\n");
		
		// Extract current line which must be the target.
		String temp = filecontent.substring(0, endline);
	
		// Process the line to extract title.
		String[] titlestring = temp.split("\\}\\{");
		
		// There was an error and we are processing
		// the wrong line.
		if (titlestring.length < 3)
			return null;
		
		// Save the title string.
		pagecontent[0] = titlestring[1];
		
		// Now extract the pagename.
		int end = titlestring[2].lastIndexOf("}");
		pagecontent[1] = titlestring[2].substring(0, end);
	
		// The rest of the page put it in contents and
		// eliminate other pages.
		int ends = filecontent.indexOf("end{page}") - 2;		
		filecontent = filecontent.substring(endline, ends);
	
		pagecontent[2] = filecontent;
		return pagecontent;
	}
	
	/**
	 * 
	 * @return
	 */
	static public String[] processingError() {
		
		String[] processedcontent = new String[3];
		processedcontent[0] = "ErrorPage";
		processedcontent[1] = "Error Page";
		processedcontent[2] = "There was an internal error while processing this page.";
		return processedcontent;
	}
}
