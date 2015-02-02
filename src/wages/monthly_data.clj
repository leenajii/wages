(ns wages.monthly-data
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io])
  (:import [java.lang Runtime Thread]))

(defn- read-monthly-data [file]
  (with-open [in-file (io/reader file)]
    (doall
      (csv/read-csv in-file))))

(defn get-monthly-data []
  (read-monthly-data "resources/hourlist201403.csv"))