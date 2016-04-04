(defproject test_clojure "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :java-source-paths ["/src/csci3055u"]
  :prep-tasks ["javac" "compile"]

  :import (csci3055u.Person)
  :aot (test_clojure.core))

