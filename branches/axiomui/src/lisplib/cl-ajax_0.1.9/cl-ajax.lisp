;; CL-AJAX
;; A library for Asynchronous Javascript + XML in Common Lisp.
;;
;; Copyright 2005 Richard Newman
;; http://www.holygoat.co.uk/
;;
;; Released 2005-03-08
;; Updated 2005-03-14
;; Updated 2005-03-25
;;
;; See the enclosed README and LICENSE files for more information.

(in-package "CL-AJAX")

;;=== OUTPUT. ===
;; Whether to output debug information.
(defvar *clajax-debug* nil)

;; Vain attempt to standardise retrieval of function names.
;; Note that SBCL returns stupid things from function-lambda-expression
;; (such as "top level local call CAR" for #'car), and returns symbols
;; for some functions and strings for others, so that's messy.
;; LispWorks's function-name only works on internal functions AFAICS.
;; Still, it works on SBCL, OpenMCL, CLISP, and LispWorks. 
;; This hasn't been tested on Allegro or CMUCL.
(defmacro ajax-function-name (fn)
  #+sbcl `(string (caddr (multiple-value-list (function-lambda-expression ,fn))))
  #+openmcl `(function-name ,fn)
  #+allegro `(string (xref::object-to-function-name ,fn))
  #+cmucl `(string (kernel:%function-header-name ,fn))
  #+clisp `(string (caddr (multiple-value-list (function-lambda-expression ,fn))))
  #+lispworks `(let ((res (system::function-name ,fn)))
		 (if (null res)
		   (string (caddr (multiple-value-list (function-lambda-expression ,fn))))
		   (when res
		     (symbol-name res)))))

;; Setup for arglist parsing.
#+sbcl (require 'sb-introspect)

#+(and :CMU :new-compiler)
   (defun arglist (name)
     (let* ((function (symbol-function name))
            (stype (system:%primitive get-vector-subtype function)))
       (when (eql stype system:%function-entry-subtype)
         (cadr (system:%primitive header-ref function
                                  system:%function-entry-type-slot)))))
(defmacro ajax-arglist (fn)
  #+sbcl `(sb-introspect::function-arglist ,fn)
  #+openmcl `(arglist ,fn)
  #+lispworks `(function-lambda-list ,fn)
  #+clisp `(ext:arglist ,fn)
  #+allegro `(excl::arglist ,fn)
  #+cmucl `(arglist ,fn))

(defun to-js-boolean (x)
  "Utility to turn x into a Javascript boolean."
  (if x "true" "false"))

;; FOR GENERATING PREAMBLE.
(defun out-debug-js-function ()
  "Output the debugging setting and function."
  (concatenate 'string "var clajax_debug = " (to-js-boolean *clajax-debug*) ";

function debug_alert(text) {
  if (clajax_debug)
    alert(\"CLAJAX: \" + text);
}
"))

;; Get an XmlHttpRequest object.
(defparameter init-request-js-function "
function init_request() {
  debug_alert(\"Initialising request...\");
  var r;
  if (window.XMLHttpRequest) { r = new XMLHttpRequest(); }
  else {
    try { r = new ActiveXObject(\"Msxml2.XMLHTTP\"); } catch (e) {
      try { r = new ActiveXObject(\"Microsoft.XMLHTTP\"); } catch (ee) {
        r = null;
      }}}
  if (!r) debug_alert(\"Browser couldn't make a connection object.\");
  return r;
}
")

(defun make-ajax-preamble (server-uri) 
  "Output a string containing the do_call function."
  (format nil "
// We assume that we've been passed in a argument string.
function ajax_call_uri(func, callback, expects_xml, elem_id, argstr, opts) {
  var uri = '~A';
  var i;
  var response = null;
  if (callback == null) return;

  if (uri.indexOf('?') == -1)
    uri = uri + '?ajax-fun=' + encodeURIComponent(func);
  else
    uri = uri + '&ajax-fun=' + encodeURIComponent(func);   
  uri = uri + argstr;
  if (opts)
    for (i = 0; i < opts.length; ++i) {
      uri = uri + '&ajax-ext' + i + '=' + encodeURIComponent(opts[i]);
    }
  if (expects_xml == true) 
    uri = uri + '&ajax-xml=true';
  else 
    uri = uri + '&ajax-xml=false';
  if (null != elem_id)
    uri = uri + '&ajax-elem=' + elem_id;

  var re = init_request();
  //document.location = uri;
  re.open('GET', uri, true);
  re.onreadystatechange = function() {
    if (re.readyState != 4) return;
    if (expects_xml)
      response = re.responseXML;
    if (!response) 
      response = re.responseText;
    callback(response);
  }
  re.send(null);
  delete re;
}"
    server-uri))

(defun join-string-list (string-list delim)
    "Concatenates a list of strings, separating them by delim."
    (format nil (concatenate 'string "~{~A~^" delim "~}") string-list))

(defun js-pref (s)
  "Prefix s by 'p_'."
  (if (null s)
    "p_"
    (if (stringp s)
      (concatenate 'string "p_" (string-downcase s))
      nil)))

(defun make-js-arglist (arg-lists)
  "Construct a comma-separated list with useful prefixes."
  (join-string-list 
    (mapcar #'(lambda (x) (js-pref (symbol-name x))) 
	    (append (cdr (assoc nil arg-lists))
		    (cdr (assoc '&optional arg-lists)))) ", "))

(defun make-safe-js-name (fun-name)
  "Trivial function to switch dashes to underscores.
   Javascript is rubbish about this. More needs to be done."
  (substitute #\_ #\- fun-name))

(defmacro safe-js-val (x)
  (let ((v (gensym)))
    `(let ((,v ,x))
       (format nil "(undefined === ~A) ? \"'undefined\" : ~A" ,v ,v))))

;; TODO: it's not entirely clear whether the &amp; escaping is necessary...
;; sort this out.
(defun make-ajax-function(fun-name)
  "Output a string containing the appropriate Javascript for accessing fun-name
   on server-uri."
  (let ((js-fun-name (concatenate 'string "ajax_" (string-downcase (make-safe-js-name fun-name))))
	(arg-lists (gen-arg-list (get-function fun-name)))
	(made-url "'' "))
    (let* ((standard-args (cdr (assoc nil arg-lists)))
	   (optional-args (cdr (assoc '&optional arg-lists)))
	   (rest-args (cdr (assoc '&rest arg-lists)))
	   (expected-standard-count (length standard-args))
	   (js-args (make-safe-js-name (make-js-arglist arg-lists))))
      (let ((argc 0))
	(dolist (standards standard-args)
	  (setf made-url 
            (format nil "~A+ '&ajax-~A=' + encodeURIComponent(~A) "
		    made-url
		    (incf argc)
	            (js-pref (symbol-name standards)))))
      ;; The URL has now been constructed with function and normal arguments.
      ;; Now build the Javascript function.
      (concatenate 'string 
"function " js-fun-name "(callback, expects_xml, elem_id, key_dict" 
  (if 
    (not (or (string= js-args "") (null js-args))) 
    (concatenate 'string  ", " js-args))
 ") {
"
  (if (or optional-args rest-args)
		 (format nil "  var start_ext = ~A;
  var end_ext = arguments.length;
  var opts = new Array();
  var i = start_ext;
  for (; i < end_ext; ++i)
    opts.push(arguments[i]);
" (+ 4 expected-standard-count)))

"  ajax_call_uri('" fun-name "', callback, expects_xml, elem_id, (" made-url "), " 
  (if (or optional-args rest-args) "opts" "null") ", key_dict);
}")))))

;; Replace a div in the document with a remote result node.
;; Updated 2005-03-25 to use innerHTML.
;; Then updated again... not to.
;; Doesn't work in Safari... namespaces?
(defparameter cl-ajax::set-element-js-function "
  function set_element_to_result(elem_id, result, replacing) {
    elem = document.getElementById(elem_id);
    debug_alert(result);
    var i = 0;
    debug_alert(replacing);
    if (replacing) {
      var max = elem.childNodes.length;
      for (; i < max; ++i) {
        debug_alert('Removing: ' + elem.childNodes[0]);
        elem.removeChild(elem.childNodes[0]);
      }
    }
    i = 0;
    for (; i < result.childNodes.length; ++i) {
      debug_alert(result.childNodes[i]);
      elem.appendChild(document.importNode(result.childNodes[i], true));
    }
  }")

;; This doesn't work.
(defparameter cl-ajax::set-element-js-function-1 "
function set_element_to_result(elem_id, result, replacing) {
  elem = document.getElementById(elem_id);
//  resdiv = result.getElementsByTagName('div')[0];
//  resdiv = result.innerHTML;
//  alert(resdiv);
  //impo = document.importNode(resdiv, true);
impo = document.importNode(result, true);
  // And slot it into the DOM, giving it the correct ID if replacing is true
  // (so we can do it again if necessary).
  // elem.parentNode.replaceChild(impo, elem);

  // if (replacing)
  //  impo.setAttribute('id', elem_id);

  // Appending instead of replacing:
  // elem.parentNode.appendChild(impo);
  // What to do if it's just a string value:
  // elem.value = result;
  debug_alert(result);
  alert(impo.innerHTML);
  alert(impo);
  elem.innerHTML = impo.innerHTML; // resdiv; // result.innerHTML;
}")

;; Useful function -- give it a chunk of XML
;; <response>
;;   <elem_id>someid</elem_id>
;;   <result><div>some xhtml result</div></result>
;; </response>
;; and wire it into the page by calling set_element_to_result.
(defparameter cl-ajax::set-element-xml-js-function "
function replace_element_from_response(res) {
  doc_el = res.documentElement;
  e_id = doc_el.getElementsByTagName('elem_id')[0].firstChild.data;
  res_el = doc_el.getElementsByTagName('result')[0];
  set_element_to_result(e_id, res_el, true);
}
function set_element_from_response(res) {
 doc_el = res.documentElement;
  e_id = doc_el.getElementsByTagName('elem_id')[0].firstChild.data;
  res_el = doc_el.getElementsByTagName('result')[0];
  set_element_to_result(e_id, res_el, (e_id ? true : false));
}")

(defun mini-escape (s)
  "Escape s, but not with <br> etc."
  (apply #'concatenate 'string
	 (loop for c across s
	       if (araneida::html-reserved-p c)
	       collect (format nil "&#~A;" (char-code c))
	       else collect (string c))))

(defun build-preamble (server-uri &rest additional-scriptage)
  "Outputs all the necessary Javascript (definitions and functions)."
  (concatenate 'string
	       "
<script type=\"text/javascript\">
//<![CDATA[
//<!--
"
	       (out-debug-js-function)
	       (make-ajax-preamble server-uri)
	       init-request-js-function
	       set-element-js-function
 	       set-element-xml-js-function
	       (join-string-list 
		(mapcar #'(lambda (x) (make-ajax-function (car x)))
			(get-exported-functions)) "

")
	       (join-string-list additional-scriptage "

")
	       "//-->
//]]>
</script>
"))

;;===============

(defclass ajax-function-handler (handler)
  ())

(defun safe-symbol-function (sym)
  "If the symbol has a bound function,
   return the bound function, otherwise nil."
  (if (and sym (fboundp sym))
    (symbol-function sym)
    nil))

;; Management of exported functions.
;; This version is a bit smarter:
;; - re-exporting will replace, not just append
;; - can unexport by symbol or string
(let ((exported-functions '()))

  (defun get-exported-functions ()
    "Access the list of exported (i.e. allowed) functions."
    exported-functions)
  
  (defun unexport-function (fun)
    "Remove fun from the list of allowed functions.
     fun can either be the name of a function or the function itself."
    (if (stringp fun)
      (let ((name (string-upcase fun)))
	(setf exported-functions (remove-if #'(lambda (x) (string= name (car x))) exported-functions)))
      (if (and fun (symbolp fun))
	  (setf exported-functions (remove-if #'(lambda (x) (eq (cdr x) fun)) exported-functions)))))

  (defun get-function (fun-name)
    "Return the function associated with the name, or nil for failure."
;    (safe-symbol-function 
      (cdr (assoc (string-upcase fun-name) 
		  exported-functions :test #'string=)))
;)

  (defun unexport-all () 
    (setf exported-functions '()))

  (defun export-function (fun &optional (fun-name (if (functionp fun)
						    (ajax-function-name fun)
						    (symbol-name fun))))
    "Ensure that fun-name is mapped to fun in the allowed functions list."
    (let ((exists (assoc (string-upcase fun-name) exported-functions :test #'string=)))
      (if exists
	(progn
	  (if (functionp fun)
	    (setf (cdr exists) fun)
	    (setf (cdr exists) (symbol-function fun)))
	  `(,exists))
	(setf exported-functions 
	      (append (list
			(cons
			  (string-upcase fun-name)
			  (if (functionp fun)
			    fun
			    (symbol-function fun)))) exported-functions)))))

  ;; Updated 2005-03-15 to only export if the function has been successfully
  ;; defined.
  (defmacro defexported (name params &rest body)
    "Shorthand macro for defining a function to export."
    (let ((f (gensym)))
      `(let ((,f (defun ,name ,params ,@body)))
	 (if ,f (export-function #',name))))))

;; DON'T USE. gen-arg-list is better. This is kept here for fun.
#|
(defun examine-arguments (fun)
  "Split the input function's argument list into named sublists.
   Probably slower than gen-arg-list."
  (let ((splits (list (cons nil ())
		  (cons '&optional ())
		  (cons '&rest ())
		  (cons '&key ())
		  (cons '&aux ())))
	(current nil))
    (dolist (a (arglist fun))
      (if (member a lambda-list-keywords)
	(setf current a)
	  (setf (cdr (assoc current splits))
	      (append (list a) (cdr (assoc current splits))))))
    splits))
|#

;; USE.
(defun gen-arg-list (fun)
  "Argument list parsing."
  (let ((clist (list))
	(sub (cons nil nil)))
    (dolist (a (ajax-arglist fun))
      (if (member a lambda-list-keywords)
	(progn
	  (setf clist (append (list (copy-tree sub)) clist))
	  (setf sub (cons a nil)))
	(setf (cdr sub) (append  (cdr sub) (list a)))))
    (append (list (copy-tree sub)) clist)))


(defun run-function (fun args keys)
  "Run the named exported function with the provided argument lists. Returns
   the result and whether the call was successful.
   E.g. (run-function #'format (t \"~A~%\" (+ 5 6)) nil)."
   ;(format t "Running: ~A~%~{~A ~}~%~{~A ~}~%" fun args keys) 
   (if fun
     (values (apply fun (append args keys)) t)
     (values nil nil)))
  
(defun parse-argument-list (arg-alist &optional (lookup-fun #'find-symbol))
  "Returns an argument keylist from an alist.
   Associations with strings beginning 'ajax-' are ordered by appearance
   and clustered into arg-list. All other associations are looked up by
   keyword and listed in key-list.
   An optional lookup function can be passed in -- usually #'intern, to
   force the creation of unknown keywords.
   Arguments are read using read-from-string, but are not evaluated unless
   the looked-up function does it deliberately."
  (let ((arg-list '())
	(key-list '()))
    (dolist (x arg-alist)
      (if (eq 5 (string< "ajax-" (car x))) 
	(setf arg-list 
	      (nconc arg-list 
		     `(,(read-from-string (cadr x)))) )
	(setf key-list 
	      (nconc key-list 
		     `(,(funcall 
			  lookup-fun 
			  (string-upcase (car x)) 
			  "KEYWORD") ,(read-from-string (cadr x)))))))
    (values arg-list key-list)))

;;
;; URL PARAMETERS:
;;
;; ajax-fun:  the name of the Lisp function to run. No quotes.
;;            E.g. <<car>>.
;; ajax-xml:  whether to return XML or plain text. <<true>> or
;;            absent for yes, any other present value for no.
;; ajax-elem: the element ID to return in the XML. If no value is
;;            provided, the elem_id element is not returned.
;;            No quotes: e.g. <<abc123>>.
;; ajax-.*:   an arbitrary number of parameters. These are Lisp
;;            values as read by the REPL, so strings must be quoted.
;;            Lists etc. may be provided; don't quote these.
;;            E.g. <<ajax-1="Foo"&ajax-2=(a b c)&ajax-3=t>>.
;;
;; All other arguments are interpreted as being keyword arguments
;; for the selected function. These must also be quoted as necessary.
;;
;; All of these should be generated in the JS function, of course.
;;
(defmethod handle-request-response ((handler ajax-function-handler)
				   method request)
  (declare (ignore method))
  (let* ((ques (url-query-alist (request-url request)))
	 (fun-valid (get-function (cadr (assoc "ajax-fun" ques :test #'string=))))
	 (return-xml (cadr (assoc "ajax-xml" ques :test #'string=)))
	 (elem-id (cadr (assoc "ajax-elem" ques :test #'string=))))
    (if (null return-xml)                 ; return XML by default.
      (setf return-xml t)
      (if (string= "true" return-xml)
	(setf return-xml t)
	(setf return-xml nil)))
    (if fun-valid
      (progn
	(request-send-headers request
			      :content-type (if return-xml
					      "text/xml"
					      "text/plain")
			      :expires "Mon, 26th Jul 1997 05:00:00 GMT"
			      :cache-control "no-cache, must-revalidate"
			      :pragma "no-cache")   ;; last-modified is automatically 'now'
	(multiple-value-bind (args keys)
	  (parse-argument-list
		      (remove-if #'(lambda (x) 
				     (member (car x) 
					     '("ajax-fun" "ajax-xml" "ajax-elem") 
					     :test #'string=)) ques))
	  (if return-xml
	    (format (request-stream request)
		    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>
		    <response>~A<result xmlns=\"http://www.w3.org/1999/xhtml\">~A</result></response>"
		    (if elem-id
		      (concatenate 'string "<elem_id>" elem-id "</elem_id>") "")
		    (run-function fun-valid args keys))
 	    (format (request-stream request) "~A" (run-function fun-valid args keys))))
	  t)
      (progn
	(request-send-error request 'http-not-implemented)
	(format (request-stream request)
		"The specified function was not exported by this server.") t))))


;; Little utility.
(defun install-multiple-handlers (base-urls)
  "Utility function to install the same handler for multiple URLs."
  (declare (special *listener*))
  (let ((ajax-handler (make-instance 'ajax-function-handler)))
    (dolist (base-url base-urls)
      (install-handler (http-listener-handler *listener*)
		       ajax-handler
		       (urlstring (merge-url base-url "/ajax-function/")) nil))))
