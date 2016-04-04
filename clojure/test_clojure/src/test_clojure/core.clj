(ns test_clojure.core
	(:import csci3055u.Person))
	; (:gen classes)

(def jack
	(new Person "firstname"))

; (defn -main []
; 	(println "main"))

; (defn fib [n]
; 	(if (< n 2)
; 		1
; 		(+ (fib (- n 1)) (fib ( - n 2)))))

; (defn fib-tail
; 	([n]
; 		(if (< n 2) 1)
; 			(fib-tail (dec n) 1 1)))
; 	([counter F-now F-prev]
; 	(cond
; 		(zero? counter) F-now
; 		:else (fib-tail (dec counter) (bigint(+ F-now F-prev))
; 			F-now))))


; (println "Recursive")
; (println (fib-tail 99 1 1))
; ; (println (fib 3))

; var a = 1
; var b = 1
; var fib
; for j = 2 to n
; 	fib a + b
; 	a = b
; 	b = fib
; 	return fib




