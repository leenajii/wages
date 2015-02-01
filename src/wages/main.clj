(ns wages.main
  (:gen-class)
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]
            [wages.salaries :as salaries])
  (:import [java.lang Runtime Thread]))

(def salaries (salaries/get-salaries))

(defn process [employee]
  (let [employee-number (first employee)
        employee-data (first (rest employee))
        grouped (group-by #(nth % 2) employee-data)]
    (info "Processing person number:" employee-number)
    (info "Person data:")
    (doall (map println grouped))))

(defn run []
  (info "Starting to calculate salaries...")
  (let [fields (first salaries)
        salaries-without-title (rest salaries)
        grouped (group-by #(nth % 1) salaries-without-title)]
    (doall (map process grouped))))

(defn -main []
  (try
    (run)
    (catch Exception e
      (let [error-message (str " Message: "(.getMessage e))]
        (error e error-message)))))


