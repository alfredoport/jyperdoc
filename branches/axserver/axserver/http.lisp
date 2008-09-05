;; file: http.lisp

(in-package "BOOT")

(defun |Open| (path)
 (si::open path :direction :input :if-exists nil :if-does-not-exist nil)
 )

(defvar |StandardOutput| *standard-output*)

(defvar  |NewLine| '#\NewLine)

(defun |WriteLine| (string &optional (outstream *standard-output*))
 (write-line string outstream)
 (finish-output outstream) )

;; some regexp stuff

(defun |StringMatch| (s1 s2)
 (si::string-match s1 s2)
 )

(defun |ListMatches| (&rest args)
 (si::list-matches args)
 )

(defun |MatchBeginning| (i)
 (si::match-beginning i)
 )

(defun |MatchEnd| (i)
 (si::match-end i)
 )

;; the socket stuff

(defun |SiSock| (p spadfn)
;;  (format t "SiSocket-1")
 (si::socket p :server
            (function
             (lambda (w) (SPADCALL w spadfn) )
             ))
;            :daemon nil)
 )

(defun |SiListen| (s)
;;  (format t "SiListen-1")
 (si::listen s)
 )
(defun |SiAccept| (s) (si::accept s))
(defun |SiCopyStream| (q s) (si::copy-stream q s))

;; Camm Maguire's modified demo server

(defun |mathObject2String| (x)
   (if (characterp x)
       (coerce (list x) 'string)
         (|object2String| x)))

(defun replace-entities (str)
   (let (resultlen result (strlen (length str)))
       (setq resultlen
	        (+ strlen
		         (* 4 (count #\< str))           ; <       ==> &#60;
			       (* 4 (count #\newline str))))   ; newline ==> <br/>
         (setq result (make-string resultlen))
	   (do ((i 0 (+ i 1)) (j 0 (+ j 1)))
	           ((= i strlen) result)
		      (cond
			    ((char= (char str i) #\<)
			          (setf (char result j) #\&) (incf j)
				       (setf (char result j) #\#) (incf j)
				            (setf (char result j) #\6) (incf j)
					         (setf (char result j) #\0) (incf j)
						      (setf (char result j) #\;))
			        ((char= (char str i) #\newline)
				      (setf (char result j) #\<) (incf j)
				           (setf (char result j) #\b) (incf j)
					        (setf (char result j) #\r) (incf j)
						     (setf (char result j) #\/) (incf j)
						          (setf (char result j) #\>))
				    (t
				           (setf (char result j) (char str i)))))))

