(ns wages.main
  (:gen-class)
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]
            [wages.salaries :as salaries])
  (:import [java.lang Runtime Thread]))

(def salaries (salaries/get-salaries))

(defn run []
  (info "Starting to calculate salaries...")
  (let [fields (first salaries)
        salaries-without-title (rest salaries)
        grouped (group-by #(nth % 1) salaries-without-title)]
    (info "Salaries:" salaries-without-title)
    (doall (map println salaries-without-title))
    (info "Grouped:" grouped)))

(defn -main []
  (try
    (run)
    (catch Exception e
      (error "Error happened in main" e))))


