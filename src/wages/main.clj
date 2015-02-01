(ns wages.main
  (:gen-class)
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]
            [wages.hours :as hours]
            [wages.salaries :as salaries])
  (:import [java.lang Runtime Thread]))

(def salaries (salaries/get-salaries))

(defn update-result [result employee-daily-result]
  (let [daily-total (:total employee-daily-result)
        daily-overtime (:overtime employee-daily-result)
        daily-evening-hours (:evening-hours employee-daily-result)]
    (merge {} {:name (:name result) :person-id (:person-id result)
               :monthly-total (+ (:monthly-total result) daily-total)
               :monthly-overtime (+ (:monthly-overtime result) daily-overtime)
               :monthly-evening-hours (+ (:monthly-evening-hours result) daily-evening-hours)})))

(defn process-employee-days [employee employee-number]
  (let [name (first (first (nth (first employee) 1)))
         do-summing (fn [result current-record]
                      (let [updated-employee (hours/daily-hours current-record)]
                        (update-result result updated-employee)))
      processed (reduce do-summing {:name name :person-id employee-number :monthly-total 0 :monthly-overtime 0 :monthly-evening-hours 0} employee)]
      processed))
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


