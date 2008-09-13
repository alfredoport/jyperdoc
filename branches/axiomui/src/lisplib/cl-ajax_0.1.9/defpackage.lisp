;; CL-AJAX
;; A library for Asynchronous Javascript + XML in Common Lisp.
;;
;; Copyright 2005 Richard Newman
;; http://www.holygoat.co.uk/
;;
;; Released 2005-03-08
;; Updated 2005-03-24
;;
;; See the enclosed README and LICENSE files for more information.

(defpackage "CL-AJAX"
  (:export :*clajax-debug*
	   :to-js-boolean
	   :make-ajax-function
	   :build-preamble
	   :ajax-function-handler
	   :get-exported-functions
	   :unexport-function
	   :get-function
	   :export-function
	   :defexported
	   :make-js-function
	   :gen-arg-list)
  (:use "COMMON-LISP" "ARANEIDA"
	#+openmcl "CCL"))
