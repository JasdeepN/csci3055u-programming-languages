(defmulti render class)

(defmethod render java.lang.String [data]
  (str data))
(defmethod render java.lang.Double [data]
  (str "FLOAT" [data]))
(defmethod render java.lang.Long [data]
  (str "INTEGER" [data]))
(defmethod render java.lang.Object [data]
  (str "BLANK" [data]))
(defmethod render clojure.lang.PersistentVector [data]
  (clojure.string/join (map render data)))


(println (render 10))
(println (render 3.1415))
(println (render "hello world"))
(println (render 'a))
(println (render [3.1415, "hello"]))
