(in-package "CL-AJAX")
(use-package :org.ancar.CLUnit)

(defmacro ajax-test (name &rest body)
  `(deftest ,name :category "CL-AJAX"
            :test-fn (lambda () ,@body)))

(ajax-test "to-js-boolean"
           (and (string= "true" (to-js-boolean t))
                (string= "false" (to-js-boolean nil))))

;; out-debug-js-function

(ajax-test "js-pref"
           (and (string= "p_horse" (js-pref "HoRsE"))
                (string= "p_" (js-pref nil))
                (string= "p_" (js-pref ""))
                (null (js-pref 'su))))

(ajax-test "make-js-arglist"
           (and (string= "" (make-js-arglist '()))
                (string= "p_wha, p_who, p_su" (make-js-arglist '((nil Wha who) (&optional su))))))

(ajax-test "make-safe-js-name"
           (and (string= "" (make-safe-js-name ""))
                (string= "whatever_we_like_" (make-safe-js-name "whatever-we-like-"))))

;; safe-js-val
;; make-ajax-function
;; set-*
;; build-preamble

(ajax-test "safe-symbol-function"
           (and (null (safe-symbol-function (gensym)))
                (eq #'car (safe-symbol-function 'car))))

;; exported functions stuff

;; gen-arg-list
;; run-function
;; parse-argument-list


(run-all-tests)
