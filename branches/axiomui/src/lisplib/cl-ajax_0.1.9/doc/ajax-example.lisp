(require 'asdf)
(asdf:operate 'asdf:load-op :araneida)

;; Standard Araneida setup.
(eval-when (:compile-toplevel :load-toplevel :execute)
  #+nil (pushnew :araneida-uses-proxy *features*)
  #+araneida-threads (pushnew :araneida-uses-threads *features*))

(defvar *demo-url* (araneida::parse-urlstring "http://localhost:8000"))

#-araneida-uses-proxy
(defvar *listener*
  (make-instance #+araneida-uses-threads 'araneida::threaded-http-listener
                 #-araneida-uses-threads 'araneida::serve-event-http-listener
                 :port (araneida::url-port *demo-url*)))

#+araneida-uses-proxy
(defvar *listener*
  (let ((fwd-url (araneida::copy-url *demo-url*)))
    (setf (araneida::url-port fwd-url) (+ 1024 (araneida::url-port *demo-url*)))
    (make-instance #+araneida-uses-threads 'araneida::threaded-reverse-proxy-listener
                   #-araneida-uses-threads 'araneida::serve-event-reverse-proxy-listener
                   :translations
                   `((,(araneida::urlstring *demo-url*) ,(araneida::urlstring fwd-url)))
                   :address #(0 0 0 0)
                   :port (araneida::url-port fwd-url))))    

;; Load CL-AJAX.
(asdf:operate 'asdf:compile-op :cl-ajax)
(asdf:operate 'asdf:load-op :cl-ajax)

(in-package "CL-AJAX")

;; Set it running.
(install-handler (http-listener-handler cl-user::*listener*)
		 (make-instance 'ajax-function-handler)
		 (urlstring (merge-url cl-user::*demo-url* "/ajax-function/")) nil)

;; TODO: replace this with your code.
(defexported multiply (x y) (* x y))
;; or (export-function #'multiply)

;; Here's another one that returns some HTML.
(let ((ind 3)
      (words '("One" "Two" "Three" "Four")))
  (defexported grab-word () 
     (concatenate 
       'string 
       "<div><h3>Returned word:</h3><p>" 
       (nth (mod (incf ind) 4) words)
       "</p></div>")))

;; Sample handler to return a page.
(defclass sample-handler (handler) ())

;; Two Javascript functions to tie into page handlers.
(defparameter do_mult
  "
function do_mult() {
  var xval = document.getElementById('x').value;
  var yval = document.getElementById('y').value;
  debug_alert ('X: ' + xval + '; Y: ' + yval);
  ajax_multiply(set_element_from_response,
		true,
		'multresult',
		null,
		document.getElementById('x').value,
		document.getElementById('y').value);
}
")

(defparameter do_word
  "
function do_word() {
  ajax_grab_word(set_element_from_response,
		true,
		'wordresult',
		null);
}
")

;; Handler for the demo page.
(defmethod handle-request-response ((handler sample-handler)
				    method request)
  (declare (ignore method))
  ;; Serve XHTML. This pretty much buggers up Safari and IE, I think.
  (request-send-headers request
			:content-type "application/xhtml+xml")
  (format (request-stream request) "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"
        \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">
	<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-gb\">
<head>
  ~A
  <title>Sample Page</title>
</head>
<body><h1>Sample Page</h1>
  <p>This is a sample page for CL-AJAX.</p>
  <h2>Exported functions:</h2>
  <ul>~{<li>~A</li>~}</ul>
  <h2>Multiply:</h2>
  <div id=\"multiplybox\">
    <input type=\"text\" width=\"10\" id=\"x\" />
    <input type=\"text\" width=\"10\" id=\"y\" />
    <input type=\"submit\" value=\"Multiply\" onclick=\"do_mult();\" />
    <p id=\"multresult\">(Result)</p>
  </div>
  <div id=\"wordbox\">
    <input type=\"submit\" value=\"Fetch a word\" onclick=\"do_word();\" />
    <div id=\"wordresult\"></div>
  </div>
  <hr />
  </body>
</html>"
	  (cl-ajax::build-preamble 
	    (urlstring (merge-url cl-user::*demo-url* "/ajax-function/"))
	    do_mult
	    do_word)
	  (mapcar #'car (get-exported-functions)))
  t)

;; Install the handler.
(install-handler (http-listener-handler cl-user::*listener*)
		 (make-instance 'sample-handler)
		 (urlstring (merge-url cl-user::*demo-url* "/sample-page/")) nil)

(start-listening cl-user::*listener*)
