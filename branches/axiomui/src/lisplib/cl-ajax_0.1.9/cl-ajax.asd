;;; -*- Lisp -*-
(defpackage :cl-ajax-system (:use #:asdf #:cl))
(in-package :cl-ajax-system)

(defsystem cl-ajax
  :depends-on (araneida)
  :version "0.1.8"
  :components ((:file "defpackage")
               (:file "cl-ajax" :depends-on ("defpackage"))
               (:module "doc"
                        :components ((:static-file "example" :pathname "ajax-example.lisp")))))
