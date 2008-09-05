
(/VERSIONCHECK 2) 

(DEFUN |AXSERV;axServer;IV;1| (|port| $)
  (PROG (|s| |i| |w| |line| |response| |reqtype| |getrequest| |j|
             |headers|)
    (RETURN
      (SEQ (LETT |s| (|openTcpServer| "test" |port| 1)
                 |AXSERV;axServer;IV;1|)
           (LETT |i| 1 |AXSERV;axServer;IV;1|)
           (LETT |j| 1 |AXSERV;axServer;IV;1|)
           (LETT |headers| "" |AXSERV;axServer;IV;1|)
           (EXIT (SEQ G190 (COND ((NULL (< 0 |i|)) (GO G191)))
                      (SEQ (LETT |w| (|acceptTcpConnection|)
                                 |AXSERV;axServer;IV;1|)
                           (SEQ G190
                                (COND ((NULL (< 0 |j|)) (GO G191)))
                                (SEQ (LETT |line| (|readTcpSocket| |w|)
                                      |AXSERV;axServer;IV;1|)
                                     (EXIT
                                      (COND
                                        ((EQL (QCSIZE |line|) 2)
                                         (LETT |j| 0
                                          |AXSERV;axServer;IV;1|))
                                        ('T
                                         (LETT |headers|
                                          (SPADCALL
                                           (LIST |headers| |line|)
                                           (|getShellEntry| $ 8))
                                          |AXSERV;axServer;IV;1|)))))
                                NIL (GO G190) G191 (EXIT NIL))
                           (LETT |response|
                                 (|AXSERV;multiServ| |headers| $)
                                 |AXSERV;axServer;IV;1|)
                           (LETT |reqtype|
                                 (|AXSERV;getRequest| |headers| $)
                                 |AXSERV;axServer;IV;1|)
                           (LETT |getrequest|
                                 (|AXSERV;getRequestGET| |headers| $)
                                 |AXSERV;axServer;IV;1|)
                           (|AXSERV;outputToSocket| |w| |response|
                               (|AXSERV;getContentType| |getrequest| $)
                               $)
                           (LETT |j| 1 |AXSERV;axServer;IV;1|)
                           (EXIT (LETT |headers| ""
                                       |AXSERV;axServer;IV;1|)))
                      NIL (GO G190) G191 (EXIT NIL))))))) 

(DEFUN |AXSERV;multiServ| (|headers| $)
  (PROG (|reqtype| |content|)
    (RETURN
      (SEQ (LETT |reqtype| (|AXSERV;getRequest| |headers| $)
                 |AXSERV;multiServ|)
           (LETT |content| "" |AXSERV;multiServ|)
           (COND
             ((EQUAL |reqtype| "GET")
              (LETT |content|
                    (|AXSERV;getFile|
                        (|AXSERV;getRequestGET| |headers| $) $)
                    |AXSERV;multiServ|)))
           (EXIT |content|))))) 

(DEFUN |AXSERV;getRequest| (|headers| $)
  (PROG (|params| |reqtype|)
    (RETURN
      (SEQ (LETT |params|
                 (SPADCALL |headers|
                     (SPADCALL " " (|getShellEntry| $ 13))
                     (|getShellEntry| $ 14))
                 |AXSERV;getRequest|)
           (LETT |reqtype| (|SPADfirst| |params|) |AXSERV;getRequest|)
           (EXIT |reqtype|))))) 

(DEFUN |AXSERV;getRequestGET| (|reqtype| $)
  (PROG (|request|)
    (RETURN
      (SEQ (LETT |request|
                 (|SPADfirst|
                     (CDR (SPADCALL |reqtype|
                              (SPADCALL " " (|getShellEntry| $ 13))
                              (|getShellEntry| $ 14))))
                 |AXSERV;getRequestGET|)
           (EXIT |request|))))) 

(DEFUN |AXSERV;makeErrorPage| (|msg| $)
  (PROG (|page|)
    (RETURN
      (SEQ (LETT |page| "<!DOCTYPE html PUBLIC "
                 |AXSERV;makeErrorPage|)
           (LETT |page|
                 (SPADCALL |page|
                     "\"-//W3C//DTD XHTML 1.0 Strict//EN\" "
                     (|getShellEntry| $ 15))
                 |AXSERV;makeErrorPage|)
           (LETT |page|
                 (SPADCALL |page|
                     "\"http://www.w3.org/TR/xthml1/DTD/xhtml1-strict.dtd\">"
                     (|getShellEntry| $ 15))
                 |AXSERV;makeErrorPage|)
           (LETT |page|
                 (SPADCALL |page|
                     "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                     (|getShellEntry| $ 15))
                 |AXSERV;makeErrorPage|)
           (LETT |page|
                 (SPADCALL |page|
                     (SPADCALL
                         "<head><title>Error</title></head><body>"
                         (SPADCALL |msg| "</body></html>"
                             (|getShellEntry| $ 15))
                         (|getShellEntry| $ 15))
                     (|getShellEntry| $ 15))
                 |AXSERV;makeErrorPage|)
           (EXIT |page|))))) 

(DEFUN |AXSERV;getFile| (|pathvar| $)
  (PROG (|params| |fn| |ext| |contentType| |f| |line| |conc| |nl|
            |content|)
    (RETURN
      (SEQ (LETT |params|
                 (SPADCALL |pathvar|
                     (SPADCALL "?" (|getShellEntry| $ 13))
                     (|getShellEntry| $ 14))
                 |AXSERV;getFile|)
           (COND
             ((SPADCALL (LENGTH |params|) 1 (|getShellEntry| $ 18))
              (LETT |pathvar| (|SPADfirst| |params|) |AXSERV;getFile|)))
           (LETT |fn| (SPADCALL |pathvar| (|getShellEntry| $ 20))
                 |AXSERV;getFile|)
           (LETT |ext| (SPADCALL |fn| (|getShellEntry| $ 21))
                 |AXSERV;getFile|)
           (LETT |contentType| (|AXSERV;getContentType| |ext| $)
                 |AXSERV;getFile|)
           (LETT |content| "" |AXSERV;getFile|)
           (COND
             ((SPADCALL |fn| (|getShellEntry| $ 22))
              (SEQ (LETT |f|
                         (SPADCALL |fn| "input" (|getShellEntry| $ 24))
                         |AXSERV;getFile|)
                   (SEQ G190
                        (COND
                          ((NULL (SPADCALL
                                     (SPADCALL |f|
                                      (|getShellEntry| $ 25))
                                     (|getShellEntry| $ 26)))
                           (GO G191)))
                        (SEQ (LETT |line|
                                   (SPADCALL |f|
                                    (|getShellEntry| $ 27))
                                   |AXSERV;getFile|)
                             (LETT |conc| |line| |AXSERV;getFile|)
                             (LETT |nl| (STRING |NewLine|)
                                   |AXSERV;getFile|)
                             (EXIT (LETT |content|
                                    (SPADCALL
                                     (LIST |content| |conc| |nl|)
                                     (|getShellEntry| $ 8))
                                    |AXSERV;getFile|)))
                        NIL (GO G190) G191 (EXIT NIL))
                   (EXIT (SPADCALL |f| (|getShellEntry| $ 28)))))
             ('T
              (LETT |content|
                    (|AXSERV;makeErrorPage| "Could not open the file."
                        $)
                    |AXSERV;getFile|)))
           (EXIT |content|))))) 

(DEFUN |AXSERV;outputToSocket| (|s| |filestream| |contentType| $)
  (PROG (|filelength| |nl| |file| |r|)
    (RETURN
      (SEQ (LETT |filelength| (STRINGIMAGE (QCSIZE |filestream|))
                 |AXSERV;outputToSocket|)
           (LETT |file| "" |AXSERV;outputToSocket|)
           (LETT |nl| (STRING |NewLine|) |AXSERV;outputToSocket|)
           (LETT |file|
                 (SPADCALL
                     (LIST "Content-Length: " |filelength| |nl| |nl|
                           |file|)
                     (|getShellEntry| $ 8))
                 |AXSERV;outputToSocket|)
           (LETT |file|
                 (SPADCALL (LIST "Connection: close" |nl| |file|)
                     (|getShellEntry| $ 8))
                 |AXSERV;outputToSocket|)
           (LETT |file|
                 (SPADCALL
                     (LIST "Content-Type: " |contentType| |nl| |file|)
                     (|getShellEntry| $ 8))
                 |AXSERV;outputToSocket|)
           (LETT |file|
                 (SPADCALL (LIST "HTTP/1.1 200 OK" |nl| |file|)
                     (|getShellEntry| $ 8))
                 |AXSERV;outputToSocket|)
           (LETT |file|
                 (SPADCALL (LIST |file| |filestream|)
                     (|getShellEntry| $ 8))
                 |AXSERV;outputToSocket|)
           (EXIT (LETT |r|
                       (|writeTcpSocket| |s| |file| (QCSIZE |file|))
                       |AXSERV;outputToSocket|)))))) 

(DEFUN |AXSERV;getContentType| (|pathvar| $)
  (PROG (|fn| |ext| |contentType|)
    (RETURN
      (SEQ (LETT |fn| (SPADCALL |pathvar| (|getShellEntry| $ 20))
                 |AXSERV;getContentType|)
           (LETT |ext| (SPADCALL |fn| (|getShellEntry| $ 21))
                 |AXSERV;getContentType|)
           (LETT |contentType| "text/plain" |AXSERV;getContentType|)
           (COND
             ((EQUAL |ext| "html")
              (LETT |contentType| "text/html" |AXSERV;getContentType|))
             ((EQUAL |ext| "htm")
              (LETT |contentType| "text/htm" |AXSERV;getContentType|))
             ((EQUAL |ext| "xml")
              (LETT |contentType| "application/xml"
                    |AXSERV;getContentType|))
             ((EQUAL |ext| "xhtml")
              (LETT |contentType| "application/xhtml+xml"
                    |AXSERV;getContentType|))
             ((EQUAL |ext| "js")
              (LETT |contentType| "application/javascript"
                    |AXSERV;getContentType|))
             ((EQUAL |ext| "css")
              (LETT |contentType| "text/css" |AXSERV;getContentType|))
             ((EQUAL |ext| "png")
              (LETT |contentType| "image/png" |AXSERV;getContentType|))
             ((OR (EQUAL |ext| "jpg") (EQUAL |ext| "jpeg"))
              (LETT |contentType| "image/jpeg" |AXSERV;getContentType|))
             ((EQUAL |ext| "gif")
              (LETT |contentType| "image/gif" |AXSERV;getContentType|))
             ((EQUAL |ext| "txt")
              (LETT |contentType| "text/plain" |AXSERV;getContentType|)))
           (EXIT |contentType|))))) 

(DEFUN |AxiomServer| ()
  (PROG ()
    (RETURN
      (PROG (#0=#:G1425)
        (RETURN
          (COND
            ((LETT #0# (HGET |$ConstructorCache| '|AxiomServer|)
                   |AxiomServer|)
             (|CDRwithIncrement| (CDAR #0#)))
            ('T
             (UNWIND-PROTECT
               (PROG1 (CDDAR (HPUT |$ConstructorCache| '|AxiomServer|
                                   (LIST
                                    (CONS NIL
                                     (CONS 1 (|AxiomServer;|))))))
                 (LETT #0# T |AxiomServer|))
               (COND
                 ((NOT #0#) (HREM |$ConstructorCache| '|AxiomServer|))))))))))) 

(DEFUN |AxiomServer;| ()
  (PROG (|dv$| $ |pv$|)
    (RETURN
      (PROGN
        (LETT |dv$| '(|AxiomServer|) . #0=(|AxiomServer|))
        (LETT $ (|newShell| 29) . #0#)
        (|setShellEntry| $ 0 |dv$|)
        (|setShellEntry| $ 3
            (LETT |pv$| (|buildPredVector| 0 0 NIL) . #0#))
        (|haddProp| |$ConstructorCache| '|AxiomServer| NIL (CONS 1 $))
        (|stuffDomainSlots| $)
        $)))) 

(MAKEPROP '|AxiomServer| '|infovec|
    (LIST '#(NIL NIL NIL NIL NIL NIL (|List| $) (|String|)
             (0 . |concat|) (|Void|) (|Integer|) |AXSERV;axServer;IV;1|
             (|Character|) (5 . |char|) (10 . |split|) (16 . |elt|)
             (|Boolean|) (|NonNegativeInteger|) (22 . ~=) (|FileName|)
             (28 . |coerce|) (33 . |extension|) (38 . |readable?|)
             (|TextFile|) (43 . |open|) (49 . |endOfFile?|)
             (54 . |not|) (59 . |readLine!|) (64 . |close!|))
          '#(|axServer| 69) 'NIL
          (CONS (|makeByteWordVec2| 1 'NIL)
                (CONS '#()
                      (CONS '#()
                            (|makeByteWordVec2| 28
                                '(1 7 0 6 8 1 12 0 7 13 2 7 6 0 12 14 2
                                  7 0 0 0 15 2 17 16 0 0 18 1 19 0 7 20
                                  1 19 7 0 21 1 19 16 0 22 2 23 0 19 7
                                  24 1 23 16 0 25 1 16 0 0 26 1 23 7 0
                                  27 1 23 0 0 28 1 0 9 10 11)))))
          '|lookupComplete|)) 

(SETQ |$CategoryFrame|
      (|put| '|AxiomServer| '|isFunctor|
             '(((|axServer| ((|Void|) (|Integer|))) T (ELT $ 11)))
             (|addModemap| '|AxiomServer| '(|AxiomServer|)
                 '((CATEGORY |package|
                       (SIGNATURE |axServer| ((|Void|) (|Integer|)))))
                 T '|AxiomServer|
                 (|put| '|AxiomServer| '|mode|
                        '(|Mapping|
                             (CATEGORY |package|
                                 (SIGNATURE |axServer|
                                     ((|Void|) (|Integer|)))))
                        |$CategoryFrame|)))) 

(MAKEPROP '|AxiomServer| 'NILADIC T) 
