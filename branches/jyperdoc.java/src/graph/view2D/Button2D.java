package graph.view2D;

import graph.Actions;
import graph.Header2;

import javax.swing.JButton;

public class Button2D extends JButton {
	
	public static final int bColor = 98;
	public static final int graphColor = 138;
	
	private int buttonX;     
	private int buttonY;      
	private int buttonWidth;
	private int buttonHeight;
	private int buttonKey;
	private int pot;
	private int graphNum;
	private int graphSelect;
	private int mask;
	private String text;
	private int textColor;
	private int textHue;
	private int textShade;
	private int xHalf;
	private int yHalf;
	/**
	 * @return the buttonX
	 */
	public int getButtonX() {
		return buttonX;
	}
	/**
	 * @param buttonX the buttonX to set
	 */
	public void setButtonX(int buttonX) {
		this.buttonX = buttonX;
	}
	/**
	 * @return the buttonY
	 */
	public int getButtonY() {
		return buttonY;
	}
	/**
	 * @param buttonY the buttonY to set
	 */
	public void setButtonY(int buttonY) {
		this.buttonY = buttonY;
	}
	/**
	 * @return the buttonWidth
	 */
	public int getButtonWidth() {
		return buttonWidth;
	}
	/**
	 * @param buttonWidth the buttonWidth to set
	 */
	public void setButtonWidth(int buttonWidth) {
		this.buttonWidth = buttonWidth;
	}
	/**
	 * @return the buttonHeight
	 */
	public int getButtonHeight() {
		return buttonHeight;
	}
	/**
	 * @param buttonHeight the buttonHeight to set
	 */
	public void setButtonHeight(int buttonHeight) {
		this.buttonHeight = buttonHeight;
	}
	/**
	 * @return the buttonKey
	 */
	public int getButtonKey() {
		return buttonKey;
	}
	/**
	 * @param buttonKey the buttonKey to set
	 */
	public void setButtonKey(int buttonKey) {
		this.buttonKey = buttonKey;
	}
	/**
	 * @return the pot
	 */
	public int getPot() {
		return pot;
	}
	/**
	 * @param pot the pot to set
	 */
	public void setPot(int pot) {
		this.pot = pot;
	}
	/**
	 * @return the graphNum
	 */
	public int getGraphNum() {
		return graphNum;
	}
	/**
	 * @param graphNum the graphNum to set
	 */
	public void setGraphNum(int graphNum) {
		this.graphNum = graphNum;
	}
	/**
	 * @return the graphSelect
	 */
	public int getGraphSelect() {
		return graphSelect;
	}
	/**
	 * @param graphSelect the graphSelect to set
	 */
	public void setGraphSelect(int graphSelect) {
		this.graphSelect = graphSelect;
	}
	/**
	 * @return the mask
	 */
	public int getMask() {
		return mask;
	}
	/**
	 * @param mask the mask to set
	 */
	public void setMask(int mask) {
		this.mask = mask;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the textColor
	 */
	public int getTextColor() {
		return textColor;
	}
	/**
	 * @param textColor the textColor to set
	 */
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	/**
	 * @return the xHalf
	 */
	public int getXHalf() {
		return xHalf;
	}
	/**
	 * @param half the xHalf to set
	 */
	public void setXHalf(int half) {
		xHalf = half;
	}
	/**
	 * @return the yHalf
	 */
	public int getYHalf() {
		return yHalf;
	}
	/**
	 * @param half the yHalf to set
	 */
	public void setYHalf(int half) {
		yHalf = half;
	}
	
	static public int 
	initButtons (Button2D[] buttons)
	{
	  int ii, num = 0;

	/********** Scale(Zoom) and Translate Potentiometer Buttons **********/

	  /* Title:  "Scale" */
	  ii = Actions.scale2D;
	  buttons[ii].buttonX = 5;
	  buttons[ii].buttonY      = 85;
	  buttons[ii].buttonWidth  = 110;
	  buttons[ii].buttonHeight = 80;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.yes;          /* scale is a potentiometer */
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	 // buttons[ii].mask         = potMASK;
	  buttons[ii].textColor    = 164;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Title:  "Translate" */
	  ii = Actions.translate2D;
	  buttons[ii].buttonX      = 121;
	  buttons[ii].buttonY      = 85;
	  buttons[ii].buttonWidth  = 110;
	  buttons[ii].buttonHeight = 80;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.yes;          /* translate is a potentiometer */
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = potMASK;
	  buttons[ii].textColor    = 21;           /* line color of translate arrow */
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Scale potentiometer buttons */

	  /* Scale along X axis */
	  ii = Actions.zoom2Dx;
	  buttons[ii].buttonX      = 5;
	  buttons[ii].buttonY      = 55;
	  buttons[ii].buttonWidth  = 53;
	  buttons[ii].buttonHeight = 25;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "X On ";
	  buttons[ii].textColor    = Button2D.bColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;


	  /* Scale along Y axis */
	  ii = Actions.zoom2Dy;
	  buttons[ii].buttonX      = 62;
	  buttons[ii].buttonY      = 55;
	  buttons[ii].buttonWidth  = 53;
	  buttons[ii].buttonHeight = 25;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Y On ";
	  buttons[ii].textColor    = bColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Translate along X axis */
	  ii = Actions.translate2Dx;
	  buttons[ii].buttonX      = 121;
	  buttons[ii].buttonY      = 55;
	  buttons[ii].buttonWidth  = 53;
	  buttons[ii].buttonHeight = 25;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "X On ";
	  buttons[ii].textColor    = bColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;


	  /* Translate along Y axis */
	  ii = Actions.translate2Dy;
	  buttons[ii].buttonX      = 179;
	  buttons[ii].buttonY      = 55;
	  buttons[ii].buttonWidth  = 53;
	  buttons[ii].buttonHeight = 25;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	 // buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Y On ";
	  buttons[ii].textColor    = bColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;


	  /* Axes Turned On/Off */
	  ii = Actions.axesOnOff2D;
	  buttons[ii].buttonX      = 5;
	  buttons[ii].buttonY      = 292;
	  buttons[ii].buttonWidth  = 90;
	  buttons[ii].buttonHeight = 30;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Axes On ";
	  buttons[ii].textColor    = 75;
	  buttons[ii].textHue      = 10;
	  buttons[ii].textShade    = 3;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Units Turned On/Off */
	  ii = Actions.unitsOnOff2D;
	  buttons[ii].buttonX      = 100;
	  buttons[ii].buttonY      = 292;
	  buttons[ii].buttonWidth  = 90;
	  buttons[ii].buttonHeight = 30;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Units Off";
	  buttons[ii].textColor    = 75;
	  buttons[ii].textHue      = 10;
	  buttons[ii].textShade    = 3;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Generate a Postscript file */
	  ii = Actions.ps2D;
	  buttons[ii].buttonX      = 195;
	  buttons[ii].buttonY      = 292;
	  buttons[ii].buttonWidth  = 36;
	  buttons[ii].buttonHeight = 30;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	 // buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "PS";
	  buttons[ii].textColor    = 35;
	  buttons[ii].textHue      = 5;
	  buttons[ii].textShade    = 2;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Bounding Rectangle On/Off */
	  ii = Actions.spline2D;
	  buttons[ii].buttonX      = 5;
	  buttons[ii].buttonY      = 329;
	  buttons[ii].buttonWidth  = 66;
	  buttons[ii].buttonHeight = 30;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Box Off";
	  buttons[ii].textColor    = 7;
	  buttons[ii].textHue      = 26;
	  buttons[ii].textShade    = 3;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Graph points On/Off */
	  ii = Actions.pointsOnOff;
	  buttons[ii].buttonX      = 75;
	  buttons[ii].buttonY      = 329;
	  buttons[ii].buttonWidth  = 67;
	  buttons[ii].buttonHeight = 30;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	 // buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Pts On ";
	  buttons[ii].textColor    = 7;
	  buttons[ii].textHue      = 26;
	  buttons[ii].textShade    = 3;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Graph lines On/Off */
	  ii = Actions.connectOnOff;
	  buttons[ii].buttonX      = 147;
	  buttons[ii].buttonY      = 329;
	  buttons[ii].buttonWidth  = 84;
	  buttons[ii].buttonHeight = 30;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Lines On ";
	  buttons[ii].textColor    = 7;
	  buttons[ii].textHue      = 26;
	  buttons[ii].textShade    = 3;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Reset View Position Button */
	  ii = Actions.reset2D;
	  buttons[ii].buttonX      = 5;
	  buttons[ii].buttonY      = 364;
	  buttons[ii].buttonWidth  = 60;
	  buttons[ii].buttonHeight = 30;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Reset";
	  buttons[ii].textColor    = bColor;
	  buttons[ii].textHue      = 5;
	  buttons[ii].textShade    = 2;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Hide Control Panel */
	  ii = Actions.hideControl2D;
	  buttons[ii].buttonX      = 70;
	  buttons[ii].buttonY      = 364;
	  buttons[ii].buttonWidth  = 88;
	  buttons[ii].buttonHeight = 30;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Hide Panel";
	  buttons[ii].textColor    = bColor;
	  buttons[ii].textHue      = 5;
	  buttons[ii].textShade    = 2;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Exits from the viewport running */
	  ii = Actions.closeAll2D;
	  buttons[ii].buttonX      = 169;
	  buttons[ii].buttonY      = 370;
	  buttons[ii].buttonWidth  = 61;
	  buttons[ii].buttonHeight = 24;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	 // buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Quit";
	  buttons[ii].textColor    = 13;
	  buttons[ii].textHue      = 29;
	  buttons[ii].textShade    = 2;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Indicates that the graph from a viewport is to be picked up. */
	  ii = Actions.pick2D;
	  buttons[ii].buttonX      = 190;
	  buttons[ii].buttonY      = 217;
	  buttons[ii].buttonWidth  = 40;
	  buttons[ii].buttonHeight = 24;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Pick";
	  buttons[ii].textColor    = 123;
	  buttons[ii].textHue      = 19;
	  buttons[ii].textShade    = 3;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Indicates that the graph from a viewport is to be dropped into a slot. */
	  ii = Actions.drop2D;
	  buttons[ii].buttonX      = 190;
	  buttons[ii].buttonY      = 245;
	  buttons[ii].buttonWidth  = 40;
	  buttons[ii].buttonHeight = 24;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Drop";
	  buttons[ii].textColor    = 123;
	  buttons[ii].textHue      = 19;
	  buttons[ii].textShade    = 3;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Indicates that the status of the graphs being displayed in the viewport
	     is to be cleared. */
	  ii = Actions.clear2D;
	  buttons[ii].buttonX      = 5;
	  buttons[ii].buttonY      = 217;
	  buttons[ii].buttonWidth  = 49;
	  buttons[ii].buttonHeight = 24;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Clear";
	  buttons[ii].textColor    = 123;
	  buttons[ii].textHue      = 19;
	  buttons[ii].textShade    = 3;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* Asks for the scale and translation information for the specified graph. */
	  ii = Actions.query2D;
	  buttons[ii].buttonX      = 5;
	  buttons[ii].buttonY      = 245;
	  buttons[ii].buttonWidth  = 49;
	  buttons[ii].buttonHeight = 24;
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "Query";
	  buttons[ii].textColor    = 123;
	  buttons[ii].textHue      = 19;
	  buttons[ii].textShade    = 3;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  /* These buttons indicate the 9 available slot numbers into which
	     a 2D graph can be placed, and the status of the graph, i.e. whether
	     it is displayed or not. */

	  ii = Actions.graph1;
	  /*buttons[ii].buttonX      = graphBarLeft;
	  buttons[ii].buttonY      = graphBarTop;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;
	  buttons[ii].graphNum     = Header2.yes;
	  buttons[ii].graphSelect  = Header2.no;
	 // buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "1";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graphSelect1;
	  /*buttons[ii].buttonX      = graphBarLeft;
	  buttons[ii].buttonY      = graphBarTop + graphBarHeight;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight-2;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.yes;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "^";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graph2;
	  /*buttons[ii].buttonX      = graphBarLeft + (graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.yes;
	  buttons[ii].graphSelect  = Header2.no;
	 // buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "2";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graphSelect2;
	  /*buttons[ii].buttonX      = graphBarLeft + (graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop + graphBarHeight;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight-2;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.yes;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "-";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graph3;
	 /* buttons[ii].buttonX      = graphBarLeft + 2*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.yes;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "3";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graphSelect3;
	  /*buttons[ii].buttonX      = graphBarLeft + 2*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop + graphBarHeight;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight-2;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;  /**** blend these three together ***/
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.yes;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "-";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graph4;
	  /*buttons[ii].buttonX      = graphBarLeft + 3*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.yes;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "4";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graphSelect4;
	  /*buttons[ii].buttonX      = graphBarLeft + 3*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop + graphBarHeight;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight-2;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.yes;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "-";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graph5;
	  /*buttons[ii].buttonX      = graphBarLeft + 4*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.yes;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "5";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graphSelect5;
	  /*buttons[ii].buttonX      = graphBarLeft + 4*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop + graphBarHeight;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight-2;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.yes;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "-";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graph6;
	  /*buttons[ii].buttonX      = graphBarLeft + 5*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.yes;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "6";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graphSelect6;
	  /*buttons[ii].buttonX      = graphBarLeft + 5*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop + graphBarHeight;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight-2;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.yes;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "-";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graph7;
	 /* buttons[ii].buttonX      = graphBarLeft + 6*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.yes;
	  buttons[ii].graphSelect  = Header2.no;
	 // buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "7";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graphSelect7;
	  /*buttons[ii].buttonX      = graphBarLeft + 6*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop + graphBarHeight;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight-2;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.yes;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "-";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graph8;
	  /*buttons[ii].buttonX      = graphBarLeft + 7*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.yes;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "8";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graphSelect8;
	  /*buttons[ii].buttonX      = graphBarLeft + 7*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop + graphBarHeight;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight-2;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.yes;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "-";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graph9;
	  /*buttons[ii].buttonX      = graphBarLeft + 8*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.yes;
	  buttons[ii].graphSelect  = Header2.no;
	  //buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "9";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  ii = Actions.graphSelect9;
	  /*buttons[ii].buttonX      = graphBarLeft + 8*(graphBarWidth);
	  buttons[ii].buttonY      = graphBarTop + graphBarHeight;
	  buttons[ii].buttonWidth  = graphBarWidth;
	  buttons[ii].buttonHeight = graphBarHeight-2;*/
	  buttons[ii].buttonKey    = ii;
	  buttons[ii].pot          = Header2.no;                /* this is a regular button */
	  buttons[ii].graphNum     = Header2.no;
	  buttons[ii].graphSelect  = Header2.yes;
	 // buttons[ii].mask         = buttonMASK;
	  buttons[ii].text = "*";
	  buttons[ii].textColor    = graphColor;
	  buttons[ii].xHalf        = buttons[ii].buttonWidth/2;
	  buttons[ii].yHalf        = buttons[ii].buttonHeight/2;
	  ++num;

	  return(num);
	}
	  

}
