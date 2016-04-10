(require '[clojure.string :as str] )

(defn gen-strings [n]
  (str/join (repeatedly n #(rand-nth ["a" "b" "c" "d" "e" "f" "g"
                                      "h" "i" "j" "k" "l" "m" "n"
                                      "o" "p" "q" "r" "s" "t" "u"
                                      "v" "w" "x" "y" "z"]
                                     )
                        )
            )
  )

(defn counter [n]
  (frequencies n)
  )

(defn test1
  ([n l k]
   (println "> generating" n  l "character strings, processing using" k "(+1) threads..." )
   (println ">" (* n l) "total characters, up to" (Math/ceil (unchecked-divide-int (* n l) k)) "elements per thread....")
   (println "> generating strings...")

   (let [string-length     l
         coll-str          (repeatedly n #(gen-strings string-length))
         coll-clean        (str/join (doall(pmap str/join coll-str)))
         size              (Math/ceil (unchecked-divide-int (* n l) k))
         final-count       (seq nil)
         ]
     (println "> generating complete starting threads...")

     (time(test1 n l k 0 1 coll-clean coll-str size final-count))
     ))

  ([n l k r1 r2 coll coll-str size final-count]
   (if (> (* size r2) (* n l))
     ;end of list
     (let [sample         (subs coll (* size r1))
           timer          (future(counter sample))]
       (println "thread" r1 "start *EXTRA")
       (println "counting...")
       ; (println (concat final-count (counter @timer)))
       )

     (let [sample         (subs coll (* size r1) (* size r2))
           timer          (future(counter sample))]
       (println "thread" r1 "start")
       ; (test1 n l k (inc r1) (inc r2) coll coll-str size (concat final-count (counter @timer))
       ; (println (conj final-count @timer))
       (println @timer final-count)
       (println (next @timer))

       )
     )
   )
  )


; (println "done" @timer))

; (future(time(counter(subs (str/join (map str/join coll)) (* size r1) (* size r2)))))
(test1 5 10 3)
; (test1 2 0)
(shutdown-agents)
; (str/join(map str/join (repeatedly n #(gen-strings string-length)))))
