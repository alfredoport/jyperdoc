/*
 Copyright (c) 2007 - 2008 Arthur C. Ralfs
 All rights reserved.

 Copyright (c) 2007 - 2008 Alfredo Portes
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are
 met:

     - Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.

     - Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in
       the documentation and/or other materials provided with the
       distribution.

     - The name of Arthur C. Ralfs may not be used to endorse or promote 
       products derived from this software without specific prior written 
       permission.

     - The name of Alfredo Portes may not be used to endorse or promote 
       products derived from this software without specific prior written 
       permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/* We need to process commands
 * that start with ")".
 */
function checkCommand(command) {

  /*if (/\)show/.test(command)) {

    command = 'showcall='+command;
  }
	else if (/\)help/.test(command)) {

		command = 'interpcall='+command
	}
  else {*/
    
    command = 'command='+command;

  //}
  return command;
}

/********************************
 * Some functions are not currently
 * allowed to be ran in this interface.
 ********************************/

function invalidCommands(command) {

  if (/\)draw/.test(command)) {
    return true;
  }
  return true;

}

/********************************
 * The format of passing arguments
 * is 'argument,value'. We use
 * ',' instead of the standard '='
 * so it is not confused by the
 * server at the time of processing
 * the commands sent to it.
 * ******************************/
function redirectToBrowse(value) {

    document.location = 'browse.xhtml?' + value + "," + document.getElementById('searchbox').value;
}

/*
 *
 */
function asq(domain, key) {

    document.location = 'browse.xhtml?' +  "asqxml=" + domain + "," + key;
}

/*
 *
 */
function remove_extra(targ) {

		targ.value = targ.value.replace('/\n+$/g',"");
}

/* ***************************
 * Every time a key is pressed
 * we check to see if the user
 * has requested for a cell to
 * be evaluate it. 
 * **************************/
function keyPressed(e) {
  var keynum, keychar, shift;
  //  var command = document.getElementById ('commreq').value
  // this works in Firefox, do we need something else in IE?
  if (e.target)
  {
    targ = e.target;
    command = targ.value;
  }
  if(window.event) // IE
  {
    keynum = e.keyCode
  }
  else if(e.which) // Netscape/Firefox/Opera
  {
    keynum = e.which
  }
  shift = e.shiftKey;

  if(shift && keynum == 13 && command != "") {
    //alert(targ.id);
    makeRequest(targ);
  }
}

/**
 */
function linkEvaluate(stepNum) {

  makeRequest(document.getElementById(stepNum));
}

// I think the only place this is used is in making the initial
// top level compCell so maybe this code should be put into
// makeCompCell

function putFocus() {
  command = document.getElementById('comm');
  command.focus();
}

/*
 *
 */
function activeCell(targ) {

  document.getElementById(targ.id).style.border="solid 1px #0000FF";
}

/*
 *
 */
function passiveCell(targ) {

  document.getElementById(targ.id).style.border="solid 1px #CCCCCC";
}

/*
 *
 */
function onBlur(e) {

  var targ = e.target;
  targ.parentNode.lastChild.style.display="none";
  passiveCell(targ);
}

/*
 *
 */
function onFocus(e) {

  var targ = e.target;
  var children = document.getElementsByTagName("*");
  for (var i = 0; i < children.length; i++) {
    if (children[i].className == "evaluate")
      children[i].style.display="none";
    if (children[i].className == "cell_input")
      children[i].style.border="solid 1px #CCCCCC";
  }
  targ.parentNode.lastChild.style.display="inline";
  document.getElementById(targ.id).style.border="solid 1px #0000FF";

  //activeCell(targ);
  //var root = ctgCompCell(targ);
  //document.getElementById(root.lastChild.id).style.display="inline";
}

/*
 *
 */
function changeFocus(e) {

  if (e.target)
  {
    targ = e.target;
    targ.focus();
    e.stopPropagation();
  }
}

/* This function is used to resize
 * the textareas */ 
function resize_textarea(t) {
  a = t.value.split('\n');
  t.rows = a.length;
}

/* This is a dummy writeToFile function.
 * Need to be re-written. */
function writeToFile(path,content) {

  //content = content.replace('/\n+$/g',"");
  makeRequestServer('command','ifile:TextFile := open("'+path+'","output");'
  + 'content:String:="'+content+'";'
  + 'writeLine!(ifile,content);'
  + 'close!ifile;');
}

/*
 *
 */
function readFromEditor() {

  // getCode is magic from Codepress.
  // One gives the id of the textarea
  // and getCode gets the content of it.
  //writeToFile('casn.input',area.getCode());
  makeRequestServer('command',area.getCode());
  
}

/*
 *
 */
function compileFromEditor() {

  writeToFile('casn.input',area.getCode());
  makeRequestServer('interpcall',')compile casn.input');
}

// check at some point whether I can have targ as an argument
function makeRequest(targ) {
    // Other methods for creating the XMLHttpRequest object 
    // in browsers different from Firefox and IE need to
    // to be placed here.
    // First test for IE XMLHttp objects.
    try {
        http_request = new ActiveXObject("Msxml2.XMLHTTP");
    } 
    catch (e) {
        try {
            http_request = new ActiveXObject("Microsoft.XMLHTTP");
        } 
        catch (e) {
            http_request = false;
        }
    }
    // If the previous failed then try for Firefox xmlhttp object.
    if (!http_request && typeof XMLHttpRequest != 'undefined') {
      try {
        http_request = new XMLHttpRequest();
      }
      catch (e) {

        http_request = false;
      }
    }
    if (!http_request && window.createRequest) {
      try {
        http_request = window.createRequest();
      }
      catch (e) {
        http_request = false;
      }
    }

    var command = targ.value;
    http_request.open('POST', '127.0.0.1:8085', true);
    http_request.onreadystatechange = addCompCell;
    http_request.setRequestHeader('Content-Type', 'text/plain');
    //http_request.send(checkCommand(removeOutput(command)));
    http_request.send(checkCommand(command));

}

/*
 *
 */
function removeOutput(command) {

  var ncommand = command.replace(/^\n+|\n+$/g,"");
  ncommand = ncommand.replace(/^\s+|\s+$/g,"");
  ncommand = ncommand.replace(/\n+/g,";");
  return ncommand;
}

/* this function sends compCell as serialized string */

function sendCompCell(compCellString) {
	var http_request = HTTP.newRequest();
	http_request.open('POST', '127.0.0.1:8085', true);
	http_request.onreadystatechange = function() {
		if (http_request.readyState == 4) {
			if (http_request.status == 200) {
				var answer = http_request.responseText;
				alert(answer);
			}
		}	
	}
	http_request.setRequestHeader('Content-Type', 'text/plain');	
	http_request.send("compCellString="+compCellString);
}

/* Here's the structure:

<div class="compCell">
  <div class="result">
    <!-- div.result contains the results from fricas command 
         returned via XMLHttpRequest.responseText -->
    <div class="stepnum">
      <!-- this contains fricas's step number -->
    </div>
    <div class="command">
      <form>
        <span>(stepnum)-></span>
	<input class="inComm" 
               type="text" 
               size="80" 
               value="command" 
               alt="command"/><!-- this contains the command sent to fricas -->
      </form>
    </div>
    <div class="algebra">
      <!-- this contains the contents of the algebraOutputStream
           generated in response to the command -->
    </div>
    <div class="mathml">
      <!-- this contains the mathml generated in response to the command -->
    </div>
    <div class="type">
      Type: <!-- this contains fricas's type of the result -->
    </div>
  </div>
  <!-- may contain any number of child compCells, however when
       compCell is initially created it won't contain any child
       compCells -->
</div>

makeCompCell should take mathString=XMLHttpRequest.responseText and
the element of which it will be added to as a child.  It's position
as a child will depend on whether the prospective parent is "the"
rootCompCell or an ordinary compCell.
*/

function makeCompCell(mathString,parent) {
    var compCell = document.createElementNS('http://www.w3.org/1999/xhtml','div');
    compCell.setAttribute('class','compCell');
    var resultBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
    resultBox.setAttribute('class','result');
    var mathRange = document.createRange();
    mathRange.selectNodeContents(resultBox);
    var mathFragment = mathRange.createContextualFragment(mathString);
    resultBox.appendChild(mathFragment);
    compCell.appendChild(resultBox);

    // command and type come bare and need to be decorated, i.e.
    // the command has to have the form and input boxes constructed and
    // the type needs just the text "Type: " added.

//    alert('makeCompCell1');
    var stepNum = getStepBox(resultBox).firstChild.data;
//    alert('makeCompCell2');

    var typeBox = getTypeBox(resultBox);
    var commandBox = getCommandBox(resultBox); 
    var command = commandBox.firstChild.data;
    
		// This is probably not needed anymore
    // Convert the ; into breaklines
    //command = command.replace(/;+/g,"\n");

    // Count the number of lines so we
    // can adjust the inputBox attribute 
    // (textarea) to have this number of rows.
    var nrows = command.split('\n').length;

    compCell.setAttribute('id', 'step'+stepNum);
    //formBox = document.createElementNS('http://www.w3.org/1999/xhtml','form');
    spanBox = document.createElement('span');
    spanBox.appendChild(document.createTextNode('('+stepNum+') -> '));
    //formBox.appendChild(spanBox);
    inputBox = document.createElementNS('http://www.w3.org/1999/xhtml','textarea');

//    alert('keyPressedReval(event,' + "'step" + stepNum + "'" + ')');
    //inputBox.setAttribute('onkeypress','keyPressedReval(event,' + "'step" + stepNum + "'" + ')');
    inputBox.setAttribute('onkeypress','keyPressed(event)');
    inputBox.setAttribute('onclick','changeFocus(event)'); 
    inputBox.setAttribute('onkeyup','resize_textarea(event.target)');
    inputBox.setAttribute('onkeydown','remove_extra(event.target)');
    inputBox.setAttribute('onfocus','onFocus(event)');
    //inputBox.setAttribute('onblur','onBlur(event)');
    inputBox.setAttribute('class','cell_input');
    //inputBox.setAttribute('type','text'); 
    inputBox.setAttribute('rows',nrows);
    inputBox.setAttribute('cols','80');
    inputBox.setAttribute('value',command);
    inputBox.setAttribute('alt',command); 
    inputBox.setAttribute('id', stepNum);
    inputBox.appendChild(document.createTextNode(command));

    //inputBox.appendChild(spanBox);
    //formBox.appendChild(inputBox);
    
    // Type: Only display the type of the result
    // if it something to show (error messages
    // do not have types :) )
    if (typeBox.firstChild != null)
      typeBox.insertBefore(document.createTextNode('Type: '),typeBox.firstChild);
    commandBox.removeChild(commandBox.firstChild);
  
    //commandBox.appendChild(formBox);
    commandBox.appendChild(inputBox);

    // Evaluate Link
    var evaluate = document.createElementNS('http://www.w3.org/1999/xhtml','a');
    evaluate.setAttribute('class','evaluate');
    evaluate.setAttribute('href','javascript:void%200');
    evaluate.setAttribute('onclick','javascript:linkEvaluate('+stepNum+')');
    evaluate.appendChild(document.createTextNode('evaluate'));
    commandBox.appendChild(evaluate);

    if (parent.className == "compCell") {
        //insert before parents resultBox

	updateResultBox(compCell, parent);
	//parent.insertBefore(compCell,getResultBox(parent));
	inputBox.focus();
    } 
    else {//parent.className = "rootCompCell"

	//Make sure to restart the numbers of rows to 1
	//in the rootCompCell textarea.
        parent.firstChild.firstChild.setAttribute('rows','1');
        //insert at top of stack, childNodes[0] is the command box
	parent.insertBefore(compCell,getFirstCompCell(parent));
	//parent.appendChild(compCell);
    }
    // do I need 'onclick','javascript:showContext(event)'?
	
}

/* Each compCell has a unique resultBox determined by its className
 */

function getResultBox(compCell) {
    var children = compCell.childNodes;
    for (var i = 0; i < children.length; i++) {
	if (CSSClass.is(children[i], 'result')) return children[i];
    }
}

/* Takes a parent node and replaces its contenst
 * with the ones from compCell. This is used when
 * the user updates/changes the command in one
 * of the cells.
 */

function updateResultBox(compCell, parent) {

    var children = parent.childNodes;
    for (var i = 0; i < children.length; i++)
      parent.removeChild(children[i]);
    
    children = compCell.childNodes;
    for (var i = 0; i < children.length; i++)
      parent.appendChild(children[i]);
}

/* I don't want to rely on the specific structure of the resultBox to access
 * it's children.  Rather I want to think of it as a bag containing, at present,
 * boxes for stepnum, command, algebra, mathml, and type which could be in any
 * order.  Also I may want to add something new, for instance a TeX box.  So I
 * define functions to retrieve these nodes for a given resultBox node.
 */

function getStepBox(resultBox) {
    // make sure argument is of right class
    if (!CSSClass.is(resultBox,'result')) {
	alert('getStepBox: argument not resultBox');
	return;
    }
    var children = resultBox.childNodes;
    for (var i = 0; i < children.length; i++) {
	if (CSSClass.is(children[i],'stepnum')) return children[i]
    }
}

// this function should get the stepnum of the lowest level compCell
// containing the present element 'g'.  If g is a compCell then that's
// the stepnum of itself.  If g is not a compCell then it is the stepnum
// of the containing compCell 

function getStepNum(g) {
    return getStepBox(getResultBox(g)).firstChild.data;
}

/*
 *
 */
function getCommandBox(resultBox) {
    // make sure argument is of right class
    if (!CSSClass.is(resultBox,'result')) {
	alert('getStepBox: argument not resultBox');
	return;
    }
    var children = resultBox.childNodes;
    for (var i = 0; i < children.length; i++) {
	if (CSSClass.is(children[i],'command')) return children[i]
    }
}

/*
 *
 */
function getAlgebraBox(resultBox) {
    // make sure argument is of right class
    if (!CSSClass.is(resultBox,'result')) {
	alert('getStepBox: argument not resultBox');
	return;
    }
    var children = resultBox.childNodes;
    for (var i = 0; i < children.length; i++) {
	if (CSSClass.is(children[i],'algebra')) return children[i]
    }
}

/*
 *
 */
function getMathmlBox(resultBox) {
    // make sure argument is of right class
    if (!CSSClass.is(resultBox,'result')) {
	alert('getStepBox: argument not resultBox');
	return;
    }
    var children = resultBox.childNodes;
    for (var i = 0; i < children.length; i++) {
	if (CSSClass.is(children[i],'mathml')) return children[i]
    }
}

/*
 *
 */
function getTypeBox(resultBox) {
    // make sure argument is of right class
    if (!CSSClass.is(resultBox,'result')) {
	alert('getStepBox: argument not resultBox');
	return;
    }
    var children = resultBox.childNodes;
    for (var i = 0; i < children.length; i++) {
	if (CSSClass.is(children[i],'type')) return children[i]
    }
}

/* I want a function that will return the first child compCell of a given
 * compCell or rootCompCell.
 */

function getFirstCompCell(compCell) {
    var children = compCell.childNodes;
    for (var i = 0; i < children.length; i++) {
	if (CSSClass.is(children[i],'compCell')) return children[i];
    }
    return null;
}

/*
 *
 */
function getSibCompCells(compCell) {
//    alert('getSibCompCells 1');
    var parent = ctgCompCell(compCell);
    var children = parent.childNodes;
    var sibs = [];
    for (var i = 0; i < children.length; i++) {
	if (CSSClass.is(children[i],'compCell')) sibs.push(children[i])
    }
    return sibs;
}

/* addCompCell is the 'onreadystatechange' function in the
   XMLHttpRequest object.  

   Here's the structure of a compCell produced by makeCompCell
   when a successful command has been returned, in this example
   the command was 'x'

   <div id="step6" class="compCell" style="display: block">
     <div class="stepnum">6</div>
     <div class="command">
       <form>
	 <span>(6)-></span>
	 <input type="text" class="inComm" onkeypress="keyPressed(event)" onclick="changeFocus(event)" size="80" value="x" alt="x">x</input>
       </form>
     </div>
     <div class="algebra"></div>
     <div class="mathml">
       <math xmlns="http://www.w3.org/1998/Math/MathML mathsize="big" display="block">
	 ... result returned from FriCAS
       </math>
     </div>
     <div class="type">Type: ... type returned from FriCAS</div>
   </div>
*/
/*
The structure returned from FriCAS now is
<div class="stepnum"></div>
<div class="command"></div>
<div class="algebra"></div>
<div class="mathml"></div>
<div class="type"></div>
*/

function addCompCell() {
   if (http_request.readyState == 4) {
       if (http_request.status == 200) {
	   // need parent compCell and mathString to pass to makeCompCell
	   var parent = ctgCompCell(targ);
	   // var parent = targ.parentNode.parentNode.parentNode;
	   // restore the original command that was in the input box
           targ.value = targ.getAttribute("alt");
	   var mathString = http_request.responseText;
	   //alert(mathString);
	   makeCompCell(mathString,parent);
       }
       else {
           alert('There was a problem with the request.'+ http_request.statusText);
       }
    }
}

/*
 *
 */
function ctgCompCell(targ) {
    // given any node this should return the containing compCell
    if (targ.parentNode.className == 'compCell' || targ.parentNode.className == 'rootCompCell') {
	return targ.parentNode;
    }
    else {
	return ctgCompCell(targ.parentNode);
    }
}

/*
 *
 */
function showContext(e) {
    var x = e.clientX;
    var y = e.clientY;
    x = x + window.pageXOffset;
    y = y + window.pageYOffset;
    // set the containing compCell and make sure event doesn't arise
    // from a command box
    var f = e.target;
    while ( !f.className ) {
	f = f.parentNode;
    }
    if ( !(CSSClass.is(f,'mathml') || CSSClass.is(f,'algebra') || CSSClass.is(f,'type') || CSSClass.is(f,'compCell') || CSSClass.is(f,'rootCompCell'))) return;
//    if ( CSSClass.is(f,'command') || CSSClass.is(f,'inComm') ) return;
    if (f.className == 'compCell') var g = f;
    else var g = ctgCompCell(f);
    // use an anonymous function to hold everything else
    (function() {
	var contents = document.getElementById('contents');
	// create box to hold the menu
	var menuBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	// create a drag bar at the top
	var dragBar = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	menuBox.appendChild(dragBar);
	dragBar.setAttribute('style','background-color: gray; height: 10px;');
	dragBar.setAttribute('onmousedown','drag(this.parentNode,event)');


/*
 * next item specifies the compCell Id and allows navigating
 * up and down the compCell tree
 */
	function makeIdBox() {
	    var compIdBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	    var idTable = document.createElementNS('http://www.w3.org/1999/xhtml','table');
	    idTable.className = 'idTable';
	    var row1 = document.createElementNS('http://www.w3.org/1999/xhtml','tr');
	    var row1td1 = document.createElementNS('http://www.w3.org/1999/xhtml','td');
	    var row1td2 = document.createElementNS('http://www.w3.org/1999/xhtml','td');
	    var row1td3 = document.createElementNS('http://www.w3.org/1999/xhtml','td');
	    row1.appendChild(row1td1);
	    row1.appendChild(row1td2);
	    row1.appendChild(row1td3);
	    idTable.appendChild(row1);
	    row1td1.appendChild(document.createTextNode('Comp Cell '));
	    //
	    var sibs = getSibCompCells(g);
	    for ( var i = 0; i < sibs.length; i++) {
		var div = document.createElementNS('http://www.w3.org/1999/xhtml','div');
		row1td2.appendChild(div);
		div.appendChild(document.createTextNode(getStepNum(sibs[i])));
		div.setAttribute('style','cursor: pointer;');
		if ( sibs[i] == g ) CSSClass.add(div,'active');
		div.onclick = function() {
		    var divs = row1td2.childNodes;
		    for ( var i = 0; i < divs.length; i++) {
			CSSClass.remove(divs[i],'active');
		    }
		    CSSClass.add(this,'active');
		    id = this.firstChild.data;
		    var sibs = getSibCompCells(g);
		    for (var i = 0; i < sibs.length; i++) {
			if (getStepNum(sibs[i]) == id) {
			    g = sibs[i];
			    g.scrollIntoView();
			    // need to move context menu into view after scrolling
//			    x = g.offsetLeft;
			    y = g.offsetTop;
			    menuBox.setAttribute('style','position: absolute; left: ' + x + 'px; top: ' + y + 'px;');
			    menuBox.scrollIntoView();
			}
		    }
		}
	    }
	    //
	    var idNum = getStepNum(g);
	    if (CSSClass.is(ctgCompCell(g),'compCell')) {
		var div = document.createElementNS('http://www.w3.org/1999/xhtml','div');
		div.appendChild(document.createTextNode('\u25b2'));
		row1td3.appendChild(div);
		div.onclick = function() {
		    g = ctgCompCell(g);
		    menuBox.replaceChild(makeIdBox(),compIdBox);
		};
		div.setAttribute('style', 'cursor: pointer;');
	    }
	    if (getFirstCompCell(g)) {
		var div = document.createElementNS('http://www.w3.org/1999/xhtml','div');
		div.appendChild(document.createTextNode('\u25bc'));
		row1td3.appendChild(div);
		div.onclick = function() {
		    g = getFirstCompCell(g);
		    menuBox.replaceChild(makeIdBox(),compIdBox);
		}
		div.setAttribute('style', 'cursor: pointer;');
	    }
	    compIdBox.appendChild(idTable);
	    return compIdBox;
	}//end makeIdBox

	var compIdBox = makeIdBox();
/*
 * next item is clickable and sets display: none; for this compCell.
 * if display is already none then it should do the reverse. 
 */
	var cellBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	var cellText = document.createTextNode('show/hide cell');
	cellBox.appendChild(cellText);
	cellBox.className = "context-item";
//	cellBox.setAttribute('onclick','showCell(\'' + g.id + '\')');
	cellBox.onclick = function() {
	    var children = g.childNodes;
	    for ( var i = 0; i < children.length; i++) {
		if ( !CSSClass.is(children[i],'compCell') ) {
		    if (CSSClass.is(children[i],'hide')) {
			CSSClass.remove(children[i],'hide');
		    } else {
			CSSClass.add(children[i],'hide');
		    }
		}
    }
}

/*
 * next item sets whole branch to display: none; or the reverse
 */
	var branchBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	var branchText = document.createTextNode('show/hide branch');
	branchBox.appendChild(branchText);
	branchBox.className = "context-item";
	branchBox.onclick = function() {
	    showBranch(g);
	}
/*
 * next item hides whole branch from present compCell to
 * the end except head
 */
	var headBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	var headText = document.createTextNode('hide except head');
	headBox.appendChild(headText);
	headBox.className = "context-item";
	headBox.setAttribute('onclick','showHead(\'' + g.id + '\')');
	headBox.onclick = function() {
	    showHead(g);
	}
/*
 * next item is rotate this compCell to top of its containing compCell.  If it's
 * already at the top then it rotates it to the top of its containing
 * branch, and so on
 */
	var rotateBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	rotateBox.appendChild(document.createTextNode('rotate to top'));
	rotateBox.className = "context-item";
	rotateBox.onclick = function() {
	    rotateHead(g);
	    menuBox.replaceChild(makeIdBox(),compIdBox);

	};
/*
 * next item is for inserting a text box
 */
	var textOpt = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	var textMenu = null;
	textOpt.appendChild(document.createTextNode('insert text'));
	textOpt.className = "context-item";
	textOpt.onclick = function() {
	    if (!textMenu) {
	    textMenu = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	    textOpt.appendChild(textMenu);
	    textMenu.setAttribute('style','margin-left: 10px;');
	    var startResult = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	    startResult.appendChild(document.createTextNode('start result'));
	    textMenu.appendChild(startResult);
	    startResult.onclick = function() {
		var textBox = makeTextBox();
		getResultBox(g).insertBefore(textBox,getResultBox(g).firstChild);
	    }
	    var endResult = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	    endResult.appendChild(document.createTextNode('end result'));
	    textMenu.appendChild(endResult);
	    endResult.onclick = function() {
		var textBox = makeTextBox();
		getResultBox(g).appendChild(textBox);
	    }
	    var beforeResult = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	    beforeResult.appendChild(document.createTextNode('before result'));
	    textMenu.appendChild(beforeResult);
	    beforeResult.onclick = function() {
		var textBox = makeTextBox();
		g.insertBefore(textBox,getResultBox(g));
	    }
	    var afterResult = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	    afterResult.appendChild(document.createTextNode('after result'));
	    textMenu.appendChild(afterResult);
	    afterResult.onclick = function() {
		var textBox = makeTextBox();
		if (getResultBox(g).nextSibling) g.insertBefore(textBox,getResultBox(g).nextSibling);
		else g.appendChild(textBox);
	    }
	    var beforeCell = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	    beforeCell.appendChild(document.createTextNode('before cell'));
	    textMenu.appendChild(beforeCell);
	    beforeCell.onclick = function() {
		var textBox = makeTextBox();
		g.parentNode.insertBefore(textBox,g);
	    }
	    var afterCell = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	    afterCell.appendChild(document.createTextNode('after cell'));
	    textMenu.appendChild(afterCell);
	    afterCell.onclick = function() {
		var textBox = makeTextBox();
		if (g.nextSibling) g.parentNode.insertBefore(textBox,g.nextSibling);
		else g.parentNode.appendChild(textBox);
	    }
	    }
	    else {
		textOpt.removeChild(textMenu);
		textMenu = null;
	    }
	}

/*
 * next item is to serialize a compCell
 */
  var serialBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	serialBox.appendChild(document.createTextNode('serialize'));
	serialBox.className = "context-item";
	serialBox.onclick = function() {
		var compCellString = (new XMLSerializer()).serializeToString(g);
//      alert(compCellString);
    sendCompCell(compCellString);
		var downBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
		downBox.appendChild(document.createTextNode('download'));
		downBox.setAttribute('style','cursor: pointer; margin-left: 10px; border: outset 2px silver; width: 80px; text-align: center;');
		serialBox.appendChild(downBox);
		downBox.onclick = function(event) {
			window.open('127.0.0.1:8085?compCellString=yes');
			event.stopPropagation();
			serialBox.removeChild(downBox);
		}
	}

/*
 * next item is to insert a saved compCell
 */
  var insertFile = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	insertFile.appendChild(document.createTextNode('insert file'));
	insertFile.className = "context-item";
	insertFile.onclick = function(event) {
		if (event.target != insertFile) return;
		event.stopPropagation();
		var filePick = document.createElementNS('http://www.w3.org/1999/xhtml','input');
		filePick.setAttribute('type','file');
		filePick.setAttribute('name','filePick');
		var fileForm = document.createElementNS('http://www.w3.org/1999/xhtml','form');
		fileForm.setAttribute('action','127.0.0.1:8085');
		fileForm.setAttribute('method','post');
		fileForm.setAttribute('enctype','multipart/form-data');
		fileForm.appendChild(filePick);
		var submitButt = document.createElementNS('http://www.w3.org/1999/xhtml','input');
		submitButt.setAttribute('type','submit');
		submitButt.value = 'select';
		submitButt.onclick = function() {
			var insertButt = document.createElementNS('http://www.w3.org/1999/xhtml','input');
			insertButt.setAttribute('type','button');
			insertButt.value = 'insert';
			insertButt.onclick = function() {
				var http_request = HTTP.newRequest();
				http_request.open('POST', '127.0.0.1:8085', true);
				http_request.onreadystatechange = function() {
					if (http_request.readyState == 4) {
						if (http_request.status == 200) {
							var compCellString = http_request.responseText;
							//alert(compCellString);
							var compCellRange = document.createRange();
							compCellRange.selectNodeContents(g);
							var compCellFragment = compCellRange.createContextualFragment(compCellString);
							g.appendChild(handleCompCellFrag(compCellFragment));
							fileForm.removeChild(filePick);
							fileForm.removeChild(submitButt);
							fileForm.removeChild(insertButt);
						}
					}
				}
				http_request.setRequestHeader('Content-Type', 'text/plain');
				http_request.send("fileInsert=yes");
			}
			fileForm.appendChild(insertButt);
		}
		fileForm.appendChild(submitButt);
		insertFile.appendChild(fileForm);
	}

/*
 * last item in menu is for closing the menu without taking any action
 */
	var closeBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	closeBox.setAttribute('onclick','closeContext(event)');
	closeBox.className = "context-item";
	var closeText = document.createTextNode('close');
	closeBox.appendChild(closeText);
/*
 * append above menu items to ctgBox
 */
	// create the div to hold the items
	menuBox.appendChild(compIdBox);
	menuBox.appendChild(cellBox);
	menuBox.appendChild(branchBox);
	menuBox.appendChild(headBox);
	menuBox.appendChild(rotateBox);
	menuBox.appendChild(textOpt);
	menuBox.appendChild(serialBox);
	menuBox.appendChild(insertFile);
	menuBox.appendChild(closeBox);

/*
 * set the class on the menuBox so it can be styled in mainstyle.css
 * and position it
 */
	menuBox.setAttribute('class','context-menu');
	menuBox.setAttribute('style','position: absolute; left: ' + x + 'px; top: ' + y + 'px;');
/*
 * attach everything to document
 */
	document.getElementById('contents').appendChild(menuBox);
    })()
	}


/*
 * handleFileInsert gets the saved compCell from the server
 * and cleans it up, in particular removing step numbers, so
 * I remove number from the ( )-> before the command input
 * box however I leave the step num in the the <div class="stepnum">
 * box because the order of the old inserted commands might be
 * useful.
 */
var uniqueInsId = (function() {
		var num = 1;
		return function() { return num++; }

})()

function handleCompCellFrag(compCellFrag) {
	// this depends on the fact that there is only one child node
  // because the serialization option works on a compCell so tmp
	// is the top level compCell
	var tmpComp = compCellFrag.childNodes[0];
	var insNum = uniqueInsId();
	(function(tmpComp) {
	 var tmpResult = getResultBox(tmpComp);
	 var tmpNum = getStepNum(tmpComp);
	 var tmpStep = getStepBox(tmpResult);
	 for (var j = 0; j < tmpStep.childNodes.length; j++) {
	 tmpStep.removeChild(tmpStep.childNodes[j]);
	 }
	 tmpStep.appendChild(document.createTextNode(insNum+'.'+tmpNum));
	 tmpComp.id = 'ins'+insNum+'.'+tmpNum;
	 var tmpSpan = getCommandBox(tmpResult).getElementsByTagName('span')[0];
	 for (var j = 0; j < tmpSpan.childNodes.length; j++) {
	 tmpSpan.removeChild(tmpSpan.childNodes[j]);
	 }	
	 tmpSpan.appendChild(document.createTextNode('(ins'+insNum+'.'+tmpNum+')->'));	 var tmpChildComps = getChildCompCells(tmpComp);
	 for (j = 0; j < tmpChildComps.length; j++) {
	   arguments.callee(tmpChildComps[j]);
	 }
	 })(tmpComp);
	return compCellFrag;
}


/*
 * showCell both shows and hides a compCell
 */


function showCell(compId) {
    var compCell = document.getElementById(compId);
    var children = compCell.childNodes;
    for ( var i = 0; i < children.length; i++) {
	if ( !CSSClass.is(children[i],'compCell') ) {
	    if (CSSClass.is(children[i],'hide')) {
		CSSClass.remove(children[i],'hide');
	    } else {
		CSSClass.add(children[i],'hide');
	    }
	}
    }
}

/*
 *
 */
function closeContext(e) {
    var tempBox = e.target.parentNode;
    tempBox.parentNode.removeChild(tempBox);
}

/*
 *
 */
function getX(e) {
    var x = 0;
    while(e) {
	x += e.offsetLeft;
	e = e.offsetParent;
    }
    return x;
}

/*
 *
 */
function getY(e) {
    var y = 0;
    while(e) {
	y += e.offsetTop;
	e = e.offsetParent;
    }
    return y;
}

/*
 *  I only want to set the children elements of the compCell
 *  to hidden that represent the results of that particular cell
 *  but leave the children that are compCells visible.
 *  So this will recursively switch the hidden class for
 *  all children.
 */

function showBranch(compCell) {
    var children = compCell.childNodes;
    for ( var i = 0; i < children.length; i++) {
	if ( children[i].className != 'compCell' ) {
	    if (CSSClass.is(children[i],'hide')) {
		CSSClass.remove(children[i],'hide');
	    } else {
		CSSClass.add(children[i],'hide');
	    }
	}
    }
    for ( i = 0; i < children.length; i++) {
	if (CSSClass.is(children[i],'compCell')) {
	    showBranch(children[i]);
	}
    }
}


/*
 *  I want to test to see if the present compCell has any children
 *  and, if not, then I'm at the head of the branch and I do nothing,
 *  but if it does then apply showHead recursively and hide the present
 *  cell.  First thing is I need a list of the compCell children.
 */

function showHead(compCell) {
    var compCells = new Array();
    var children = compCell.childNodes;
    for ( var i = 0; i < children.length; i++) {
	if ( CSSClass.is(children[i],'compCell') ) {
	    compCells.push(children[i]);
	}
    }
    if ( compCells.length > 0 ) { //if there are children compCells then hide contents
	for ( i = 0; i < children.length; i++) {
	    if ( !CSSClass.is(children[i],'compCell') ) {
		if (CSSClass.is(children[i],'hide')) {
		    CSSClass.remove(children[i],'hide');
		} else {
		    CSSClass.add(children[i],'hide');
		}
	    }
	}
	// apply showHead recursively to child compCells
	for ( i = 0; i < compCells.length; i++) {
	    showHead(compCells[i]);
	}
    }
}

/*  Get list of sibling compCells, if more than one
 *  then cyclically rotate the present one to first
 */

function rotateHead(compCell) {
    var parent = compCell.parentNode;
    var sibCompCells = getSibCompCells(compCell);
    var j;
    if ( sibCompCells.length > 1 ) { //do the rotation
	// find index of compCell among siblings
	for ( i = 0; i < sibCompCells.length; i++) {
	    if ( sibCompCells[i] == compCell ) j = i; 
	}
	// j is index of compCell, if j != 0 rotate to 0
	var swap;
	for ( i = 0; i < j; i++) {
	    parent.appendChild(parent.removeChild(sibCompCells[i]));
	}
    }
}

/*
 *
 */
function makeTextBox() {
    // edit box
    var hidden = false;
    var editBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
    editBox.className = 'editBox';
    var formBox = document.createElementNS('http://www.w3.org/1999/xhtml','form');
    var textareaBox = document.createElementNS('http://www.w3.org/1999/xhtml','textarea');
    textareaBox.setAttribute('style','width: 90%; height: 200px;');
    textareaBox.setAttribute('onclick','changeFocus(event)');
    editBox.appendChild(formBox);
    formBox.appendChild(textareaBox);
    // update button
    var updateButt = document.createElementNS('http://www.w3.org/1999/xhtml','span');
    updateButt.appendChild(document.createTextNode('update display'));
    updateButt.setAttribute('style','cursor: pointer; border: outset 2px silver; padding: 0px 5px;');
    updateButt.onclick = function() {
	  var textString = textareaBox.value;
	  // apply tex2mml on tex (inline) elements here
	var startTex;
  var endTex;
  while ( (startTex = textString.search('<tex>')) != -1 ) {
		var endTex = textString.search('</tex>');
		var texString = textString.slice(startTex+5,endTex);
		var mmlString = tex2mml(texString,'inline');
		textString = textString.slice(0,startTex) + mmlString + textString.slice(endTex+6);
	}
  // apply tex2mml on Tex (block) elements here
	while ( (startTex = textString.search('<Tex>')) != -1 ) {
		var endTex = textString.search('</Tex>');
		var texString = textString.slice(startTex+5,endTex);
		var mmlString = tex2mml(texString,'display');
		textString = textString.slice(0,startTex) + mmlString + textString.slice(endTex+6);
	}
	textRange = document.createRange();
	textRange.selectNodeContents(displayBox);
	var textFragment = textRange.createContextualFragment(textString);
	var oneBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	oneBox.appendChild(textFragment);
	if (displayBox.firstChild) displayBox.removeChild(displayBox.firstChild);
	displayBox.appendChild(oneBox);
    }
    // display
    var displayBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
    displayBox.className = 'displayBox';
    displayBox.onclick = function(e) {
	var x = e.clientX;
	var y = e.clientY;
	x = x + window.pageXOffset;
	y = y + window.pageYOffset;
	var menuBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
/*
 * create switch to toggle visibility of edit box and update button
 */
	var toggleBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	toggleBox.setAttribute('style','cursor: pointer;');
	toggleBox.appendChild(document.createTextNode('show/hide edit box'));
	toggleBox.onclick = function() {
	    if (hidden == false) {
		CSSClass.add(editBox,'hide');
		CSSClass.add(updateButt,'hide');
		hidden = true;
		menuBox.parentNode.removeChild(menuBox);
	    }
	    else {
		CSSClass.remove(editBox,'hide');
		CSSClass.remove(updateButt,'hide');
		hidden = false;
		menuBox.parentNode.removeChild(menuBox);
	    }
	}
/*
 * set the class on the menuBox, so it can be styled in mainstyle.css,
 * and position it
 */
	menuBox.setAttribute('class','context-menu');
	menuBox.setAttribute('style','position: absolute; left: ' + x + 'px; top: ' + y + 'px;');
/*
 * attach everything to document
 */
	menuBox.appendChild(toggleBox);
	document.getElementById('contents').appendChild(menuBox);
	
    }
    // textBox to hold everything
    var textBox = document.createElementNS('http://www.w3.org/1999/xhtml','div');
    textBox.appendChild(editBox);
    textBox.appendChild(updateButt);
    textBox.appendChild(displayBox);
    return textBox;
}

/*
 * module for manipulating CSS classes.  Thanks to David Flanagan, 'Javascript The
 * Definitive Guide' O'Reilly for these functions
 */

var CSSClass = {};

// Return true if element e is a member of the class c; false otherwise

CSSClass.is = function(e,c) {
    if (typeof e == "string") e = document.getElementById(e);
    //optimize common cases
    var classes = e.className;
    if (!classes) return false;  // Not a member of any classes
    if (classes == c) return true; // Member of just this one class
    return e.className.search("\\b" + c + "\\b") != -1;
}

// Add class c to the className of element e if it is not already there.

CSSClass.add = function(e,c) {
    if (typeof e == "string") e = document.getElementById(e);
    if (CSSClass.is(e,c)) return; // If already a member do nothing
    if (e.className) c = " " + c; // Whitespace separator if needed
    e.className += c;  // Append the new class to the end
}

// Remove all occurrences of class c from the className of element e

CSSClass.remove = function(e,c) {
    if (typeof e == "string") e = document.getElementById(e);
    e.className = e.className.replace(new RegExp("\\b" + c + "\\b\\s*","g"), "");
}


/* Module to drag an element
 * 
 * Arguments:
 *
 *  elementToDrag: the element that received the mousedown event or
 *    some containing element.  It must be absolutely positioned.  Its
 *    style.left and style.top values will be changed based on the user's
 *    drag.
 *
 *  event:  the Event object for the mousedown event.
 *
 */

function drag(elementToDrag, event){
    // The mouse position, in window coordinates, at which the 
    // drag event begins.
    var startX = event.clientX, startY = event.clientY;
    
    // The original position, in document coordinates, of the  element
    // that is going to be dragged.  Since elementToDrag is absolutely positioned
    // we assume that its offsetParent is the document body.
    var origX = elementToDrag.offsetLeft, origY = elementToDrag.offsetTop;

    // Even though the coordinates are computed in different coordinate systems
    // we can still compute the difference between them and use it in the 
    // moveHandler() function.  This works because the scrollbar position never
    // changes during the drag.
    var deltaX = startX - origX, deltaY = startY - origY;

    // Register the event handlers that will respond to the mousemove events
    // and the mouseup event that follow this mousedown event.
    document.addEventListener("mousemove",moveHandler,true);
    document.addEventListener("mouseup",upHandler,true);

    // We've handled this event.  Don't let anybody else see it.
    event.preventDefault();

    /*
     * This is the handler that captures mousemove events when an element is
     * being dragged.  It is responsible for moving the element.
     */

    function moveHandler(e) {
	// Move the element to the current mouse position adusted as necessary by
	// the offset of the initial mouse click.
	elementToDrag.style.left = (e.clientX - deltaX) + "px";
	elementToDrag.style.top = (e.clientY - deltaY) + "px";
	// Don't let anyone else see this event.
	e.stopPropagation();
    }

    /*
     * This is the handler that captures the final mouseup event that occurs at
     * the end of a drag.
     */

    function upHandler(e) {
	// Unregister the capturing event handlers.
	document.removeEventListener("mouseup",upHandler,true);
	document.removeEventListener("mousemove",moveHandler,true);
	// Don't let the event propagate any further.
	e.stopPropagation();
    }
}

/**************************
 * Pages Content Section
 **************************/

var appname = "JyperDoc";

/*
 *
 */
function init() {
	displayTitle("<img src='notebook.gif'/>" + appname);
	menu();
}

/*
 *
 */
function displayTitle(title) {
	
	var banner = document.getElementById("banner");
	banner.innerHTML = title;
			
}

/*
 *
 */
function menu() {
	
  var menu = document.getElementById("menu");

	  var links = document.createElementNS('http://www.w3.org/1999/xhtml', 'div');
		links.setAttribute('id','links');
		links.setAttribute('class','links');

/*		var interp	= document.createElementNS('http://www.w3.org/1999/xhtml', 'a');
		interp.setAttribute('id','interpreter');
	  //interp.setAttribute('class','nouse'); 
		//interp.setAttribute('href','jyperdoc.xhtml');
		interp.appendChild(document.createTextNode('Use '));
*/
		var editor  = document.createElementNS('http://www.w3.org/1999/xhtml', 'a');
		editor.setAttribute('id','editor');
		editor.setAttribute('href','javascript:void%200');
    editor.setAttribute('onclick',"window.open('editor.xhtml')");
		editor.appendChild(document.createTextNode('Editor '));

		var browser  = document.createElementNS('http://www.w3.org/1999/xhtml', 'a');
		browser.setAttribute('id','browser');
		browser.setAttribute('onclick',"window.open('man0page.xhtml')");
		browser.appendChild(document.createTextNode('Browse'));


  var abook  = document.createElementNS('http://www.w3.org/1999/xhtml', 'a');
  abook.setAttribute('id','axbook');
  abook.setAttribute('onclick',"window.open('../axbook/book-contents.xhtml')");
  abook.appendChild(document.createTextNode('Axiom Book'));
								
	  //links.appendChild(interp);
	  //links.appendChild(editor);
	  //links.appendChild(browser);
	  links.appendChild(abook);
	  menu.appendChild(links);	
}

/*
 *
 */
function markInUse(e) {

	var targ = e.target;
  document.getElementById(targ.id).setAttribute('class','inuse');
  //link.style.background-color="#1e90ff";
}

/*
 *
 */
function interpreter() {

  	var contents = document.getElementById("contents");
		var rootcompcell = document.createElementNS('http://www.w3.org/1999/xhtml', 'div');
		rootcompcell.setAttribute('class','rootCompCell'); 
		rootcompcell.setAttribute('onclick','javascript:showContext(event);');
		rootcompcell.setAttribute('onkeyup','javascript:resize_textarea(event.target);');
		
		var command = document.createElementNS('http://www.w3.org/1999/xhtml','div');
	  command.setAttribute('class','command');

		var comm = document.createElementNS('http://www.w3.org/1999/xhtml','textarea');
	  comm.setAttribute('id','comm');
		comm.setAttribute('class','cell_input');
		comm.setAttribute('name','command');
		comm.setAttribute('rows','1');
		comm.setAttribute('cols','80');
		comm.setAttribute('onkeypress','keyPressed(event);');
		comm.setAttribute('onfocus','onFocus(event);');
		
		command.appendChild(comm);
		rootcompcell.appendChild(command);
		contents.appendChild(rootcompcell);
}

/*
 *
 */
function editor() {

	  var contents = document.getElementById("contents");
		  contents.innerHTML = "<div id='buttons'><button id='compile'>)compile</button>" +
				"<button id='read'>)read</button>" +
				"<button id='open'>open</button>" +
				"<button id='save'>save</button>" +
				"</div>" +
				"<div class='editor'>" +
				"<textarea id='myCpWindow' " +
				"rows='30' " +
				"cols='80' " +
				"class='codepress javascript linenumbers'" +
				">--This is a test.</textarea></div>";

			  var selecttags = document.getElementsByTagName("select");
			  for (var i = 0; i < selecttags.length; i++)
					selecttags[i].style.display="none";
}

/*************************
 * Database Browse Section
 ************************/

function getValues() {

	var query = window.location.search;
	// Skip the leading ?, which should always be there,
	// but be careful anyway
	if (query.substring(0, 1) == '?') {
	  query = query.substring(1);
	}
	var data = query.split(',');
	for (i = 0; (i < data.length); i++) {
	  data[i] = unescape(data[i]);
	}
	if (data[0] == 'getconstructorhtml')
		makeRequestServer(data[0],data[1]);
}

/*
 *
 */
function makeRequestServer(command, value) {

	try {
		http_request = new ActiveXObject("Msxml2.XMLHTTP");
	}
	catch (e) {	
		try {
			http_request = new ActiveXObject("Microsoft.XMLHTTP");
		}
		catch (e) {	
			http_request = false;
		}
	}
  if (!http_request && typeof XMLHttpRequest != 'undefined') {
		try {
			http_request = new XMLHttpRequest();
		}
		catch (e) {
			http_request = false;
		}
	}
	if (!http_request && window.createRequest) {
		try {
			http_request = window.createRequest();
		}
		catch (e) {
			http_request = false;
		}
	}
	http_request.open('POST', '127.0.0.1:8085', true);
	http_request.onreadystatechange = handleResponse;
	http_request.setRequestHeader('Content-Type', 'text/plain');
	http_request.send(command+"="+value);
}

/*
 *
 */
function handleResponse() {
	if (http_request.readyState == 4) {
		if (http_request.status == 200) {
			document.getElementById("contents-browse").innerHTML = http_request.responseText;
		}
		else {
			alert('There was a problem with the request.'+ http_request.statusText);
		}
	}
}

/* HTTP.newRequest() utility from David Flanagan */
// list of XMLHttpRequest creation factories to try
  var HTTP = {};
HTTP._factories = [
  function() { return new XMLHttpRequest(); },
	function() { return new ActiveXObject("Msxml2.XMLHTTP"); },
	function() { return new ActieXObject("Microsoft.XMLHTTP"); }
	];
	HTTP._factory = null;
	HTTP.newRequest = function() {
		if (HTTP._factory != null) return HTTP._factory();
		  for (var i = 0; i < HTTP._factories.length; i++) {
				try {
					var factory = HTTP._factories[i];
					var request = factory();
					if (request != null) {
						HTTP._factory = factory;
						return request;
					}
				}
				catch(e) {
					continue;
				}
			}
			// If we get here we failed
			HTTP._factory = function() {
				throw new Error("XMLHttpRequest not supported");
			}
			HTTP._factory();
	}
	