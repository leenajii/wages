(ns wages.main
  (:gen-class)
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]])
  (:import [java.lang Runtime Thread]))

(defn get-salaries []
  (slurp "resources/hourlist201403.csv"))

(defn run []
  (info "Starting to calculate salaries...")
  (info "Salaries:"(get-salaries)))

(defn -main []
  (try
    (run)
    (catch Exception e
      (error "Error happened in main" e))))


