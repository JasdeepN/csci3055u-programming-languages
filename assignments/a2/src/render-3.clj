(defprotocol render-protocol
  (render [data]))

(extend-protocol render-protocol
  java.lang.String
  (render [data]
          (str data))

  java.lang.Double
  (render [data]
          (str "FLOAT" [data]))

  java.lang.Long
  (render [data]
          (str "INTEGER" [data]))

  clojure.lang.PersistentVector
  (render [data]
          (clojure.string/join (map render data)))

  java.lang.Object
  (render [data]
          (str "BLANK")))

(println (render 10))
(println (render 3.1415))
(println (render "hello world"))
(println (render 'a))
(println (render [3.1415, "hello"]))
