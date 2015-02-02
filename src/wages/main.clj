(ns wages.main
  (:gen-class)
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]
            [wages.calculations :as calc]
            [wages.hours :as hours]
            [wages.salaries :as salaries])
  (:import [java.lang Runtime Thread]))

(def salaries (salaries/get-salaries))

(defn update-result [result employee-daily-result]
  (let [daily-total (:total employee-daily-result)
        daily-overtime (:overtime employee-daily-result)
        daily-evening-hours (:evening-hours employee-daily-result)
        daily-salary (:daily-salary employee-daily-result)
        daily-overtime-salary (:daily-overtime-salary employee-daily-result)
        daily-evening-salary (:daily-evening-salary employee-daily-result)]
    (merge {} {:name (:name result) :person-id (:person-id result)
               :monthly-total (+ (:monthly-total result) daily-total)
               :monthly-overtime (+ (:monthly-overtime result) daily-overtime)
               :monthly-evening-hours (+ (:monthly-evening-hours result) daily-evening-hours)
               :normal-salary (+ (:normal-salary result) daily-salary)
               :overtime-salary (+ (:overtime-salary result) daily-overtime-salary)
               :evening-salary (+ (:evening-salary result) daily-evening-salary)})))

(defn process-employee-days [employee employee-number]
  (let [name (first (:name employee))
         do-summing (fn [result current-record]
                      (let [updated-employee (hours/daily-hours current-record)
                            daily-salary (calc/regular-wage (:total updated-employee))
                            daily-overtime-salary (calc/overtime-compensation (:overtime updated-employee))
                            daily-evening-salary (calc/evening-work-wage (:evening-hours updated-employee))]
                        (if (= employee-number "1")(println updated-employee))
                        (update-result result (merge updated-employee {:daily-salary daily-salary :daily-overtime-salary daily-overtime-salary :daily-evening-salary daily-evening-salary}))))
      processed (reduce do-summing {:name name :person-id employee-number :monthly-total 0
                                    :monthly-overtime 0 :monthly-evening-hours 0
                                    :normal-salary 0 :overtime-salary 0 :evening-salary 0} employee)]
      processed))

(defn employee-row->map [result row] 
  (conj result {:name (first row) :person-id (second row) :date (nth row 2) :start (nth row 3) :end (nth row 4)}))

;(reduce map-first {} rules)

(defn process [employee]
  (if (= (first employee) "1") 
    (println employee))
  (let [employee-number (first employee)
        employee-data (first (rest employee))
        employee-map (reduce employee-row->map [] employee-data)                                                                                                                                     
        grouped-map (group-by #(:date %) employee-map)
        processed-employee (process-employee-days grouped-map employee-number)
        monthly-salary (+ (+ (:normal-salary processed-employee) (:overtime-salary processed-employee)) (:evening-salary processed-employee))
        rounded-salary (format "%.2f" monthly-salary)]
    (info "Processed data:" (merge processed-employee {:salary rounded-salary}))))

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


