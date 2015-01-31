(ns wages.salaries
  (:gen-class)
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io])
  (:import [java.lang Runtime Thread]))

(defn read-salaries [file]
  (with-open [in-file (io/reader file)]
    (doall
      (csv/read-csv in-file))))

(defn get-salaries []
  (read-salaries "resources/hourlist201403.csv"))