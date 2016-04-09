(require '[clojure.string :as str] )

(defn gen-strings [n]
  (str/join (repeatedly n #(rand-nth ["a" "b" "c" "d" "e" "f" "g"
                                      "h" "i" "j" "k" "l" "m" "n"
                                      "o" "p" "q" "r" "s" "t" "u"
                                      "v" "w" "x" "y" "z"]))))

(defn single-count [n]
  (map frequencies n)
  )

(defn agg-count [n]
  (frequencies(str/join(map str/join n))))

(defn sequential-count [n]
  (let [string-length 50
        generated-strings (repeatedly n #(gen-strings string-length))
        ]
    (println generated-strings)
    (println (str/replace
               (str/replace
                 (str/join "\n"
                           (str/split
                             (apply str (single-count generated-strings)) #"}")
                           )
                 #"\\" "" )
               #"\{" "")
             )
    (println (agg-count generated-strings))
    )
  )

; (println(sequential-count 100))
(sequential-count 500)

; (println
;          x
;            (split "The Quick Brown Fox" #"\s")))
