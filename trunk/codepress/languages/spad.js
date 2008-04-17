/*
 * CodePress regular expressions for SPAD syntax highlighting.
 * This is based on the code for Java. Needs to be modified to
 * follow SPAD language.
 */
 
// SPAD
Language.syntax = [
	{ input : /\"(.*?)(\"|<br>|<\/P>)/g, output : '<s>"$1$2</s>'}, // strings double quote
	{ input : /\'(.*?)(\'|<br>|<\/P>)/g, output : '<s>\'$1$2</s>'}, // strings single quote
	{ input : /\b(Any|Boolean|corce|else|Exports|false|for|if|Implementation|import|Integer|integrate|keyword|OutputForm|package|public|String|Symbol|then|true|where|while|with)\b/g, output : '<b>$1</b>'}, // reserved words
	{ input : /([^:]|^)--(.*?)(<br|<\/P)/g, output : '$1<i>--$2</i>$3'}, // comments //
	{ input : /([^:]|^)\+\+(.*?)(<br|<\/P)/g, output : '$1<i>++$2</i>$3'} // comments //	
	//,{ input : /\/\*(.*?)\*\//g, output : '<i>/*$1*/</i>' }// comments /* */
]

Language.snippets = []

Language.complete = [
	{ input : '\'',output : '\'$0\'' },
	{ input : '"', output : '"$0"' },
	{ input : '(', output : '\($0\)' },
	{ input : '[', output : '\[$0\]' },
	{ input : '{', output : '{\n\t$0\n}' }		
]

Language.shortcuts = []
