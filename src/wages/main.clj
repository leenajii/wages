(ns wages.main
  (:gen-class)
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]])
  (:import [java.lang Runtime Thread]))


(defn run []
  (info "Starting to calculate salaries..."))

(defn -main []
  (try
    (run)
    (catch Exception e
      (error "Error happened in main" e))))


