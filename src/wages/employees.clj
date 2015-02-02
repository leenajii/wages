(ns wages.employees
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]
            [wages.calculations :as calc]
            [wages.hours :as hours]
            [wages.monthly-data :as monthly-data]))

(def monthly-data (monthly-data/get-monthly-data))

(defn update-result [result {total :total overtime :overtime evening-hours :evening-hours daily-salary :daily-salary
                      daily-overtime-salary :daily-overtime-salary daily-evening-salary :daily-evening-salary}]
    (merge {} {:name (:name result) :person-id (:person-id result)
               :monthly-total (+ (:monthly-total result) total)
               :monthly-overtime (+ (:monthly-overtime result) overtime)
               :monthly-evening-hours (+ (:monthly-evening-hours result) evening-hours)
               :normal-salary (+ (:normal-salary result) daily-salary)
               :overtime-salary (+ (:overtime-salary result) daily-overtime-salary)
               :evening-salary (+ (:evening-salary result) daily-evening-salary)}))

(defn do-summing [result current-record]
  (let [updated-employee (hours/daily-hours current-record)
        daily-salary (calc/regular-wage (:total updated-employee))
        daily-overtime-salary (calc/overtime-compensation (:overtime updated-employee))
        daily-evening-salary (calc/evening-work-wage (:evening-hours updated-employee))]
    (update-result result (merge updated-employee {:daily-salary daily-salary :daily-overtime-salary daily-overtime-salary :daily-evening-salary daily-evening-salary}))))

(defn process-employee-days [employee employee-number]    ; employee {10.3.2014 [{:name Scott Scala, :person-id 2, :date 10.3.2014, :start 8:15, :end 16:15} {:name Scott Scala, :person-id 2, :date 10.3.2014, :start 22:00, :end 23:00}]
  (let [name (first (:name employee))
        processed (reduce do-summing {:name name :person-id employee-number :monthly-total 0
                                      :monthly-overtime 0 :monthly-evening-hours 0
                                      :normal-salary 0 :overtime-salary 0 :evening-salary 0} employee)]
    processed))

(defn employee-row->map [result row]
  (conj result {:name (first row) :person-id (second row) :date (nth row 2) :start (nth row 3) :end (nth row 4)}))

(defn get-salary [processed-employee]
  (let [monthly-salary (+ (+ (:normal-salary processed-employee) (:overtime-salary processed-employee)) (:evening-salary processed-employee))
        rounded-salary (format "%.2f" monthly-salary)]
    (merge processed-employee {:salary rounded-salary})))

(defn- get-employee-number [employee]
  (first employee))

(defn- get-employee-data [employee]
  (if (= (first employee) "1")
    (println employee))
  (first (rest employee)))
                                                                                    ; Formats
(defn process [employee]                                                            ; [1 [[Janet Java 1 3.3.2014 9:30 17:00] [Janet Java 1 4.3.2014 9:45 16:30]]]
  (let [employee-map (reduce employee-row->map [] (get-employee-data employee))     ; [{:name Scott Scala, :person-id 2, :date 2.3.2014, :start 6:00, :end 14:00} {:name Scott Scala, :person-id 2, :date 3.3.2014, :start 8:15, :end 16:00}]
        employee-grouped-by-date (group-by #(:date %) employee-map)                 ; {10.3.2014 [{:name Scott Scala, :person-id 2, :date 10.3.2014, :start 8:15, :end 16:15} {:name Scott Scala, :person-id 2, :date 10.3.2014, :start 22:00, :end 23:00}]
        processed-employee (process-employee-days employee-grouped-by-date (get-employee-number employee))
        employee-with-salary (get-salary processed-employee)]
    (info "MAP:" employee-map)
    (info "GROUPED:" employee-grouped-by-date)
    (info "Processed data:" employee-with-salary)))

(defn calculate-monthly-salaries []
  (info "Starting to calculate salaries...")
  (let [salaries-without-title (rest monthly-data)
        salaries-grouped-by-person-id (group-by #(nth % 1) salaries-without-title)]
    (doall (map process salaries-grouped-by-person-id))))