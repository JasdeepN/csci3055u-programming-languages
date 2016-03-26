(defn render [data]
  (cond
    (=(type data) java.lang.Double) (str "FLOAT" [data])
    (=(type data) java.lang.String) (str data)
    (=(type data) java.lang.Long) (str "INTEGER" [data])
    (=(type data) clojure.lang.PersistentVector) (clojure.string/join " " (map render data))
    :else "BLANK"
    )
  )


(println (render 10))
(println (render 3.1415))
(println (render "hello world"))
(println (render 'a))
(println (render [3.1415, "hello"]))
