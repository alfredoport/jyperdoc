;; file: http.lisp

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
              )
             :daemon nil)
 )

(defun |SiListen| (s)
;;  (format t "SiListen-1")
 (si::listen s)
 )
(defun |SiAccept| (s) (si::accept s))
(defun |SiCopyStream| (q s) (si::copy-stream q s))

;; Camm Maguire's modified demo server

(defun foo (s)
 (setq get "" pathvar "")
 (do ((c (read-char s) (read-char s)))
     ((eq c '#\Space))
     (setq get (concat get (string c)))
     )
 (write-line "get: ")
 (write-line get)
 (do ((c (read-char s) (read-char s nil 'the-end)))
     ((eq c '#\Space))
     (setq pathvar (concat pathvar (string c)))
     )
 (write-line "pathvar: ")
 (write-line pathvar)
 (when pathvar
   (if (pathname-name (pathname pathvar))
       (with-open-file (q pathvar) (si::copy-stream q s))
     (dolist (l (directory pathvar)) (format s "~a~%" (namestring l)))
     )
   )
 (close s)
 )

(defun bar (p fn)
 (let ((s (si::socket p :server fn)))
       (tagbody l
               (when (si::listen s)
                       (let ((w (si::accept s)))
                               (foo w)))
               (sleep 10000)
               (go l))))

;;(bar 8080 #'foo)

