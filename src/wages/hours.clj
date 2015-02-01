(ns wages.hours
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]
            [clj-time.format :as ftime]
            [clj-time.core :as t]))

(def fields [:name :person-id :date :start :end])

(defn- time->time-in-minutes [time-string]
  (ftime/parse (ftime/formatters :hour-minute) time-string))

(defn daily-overtime [employee]
  (let [daily-total (:total employee)]
  (if (> daily-total 8)
    (- daily-total 8)
    0)))

(defn daily-total [employee]
  (let [start (time->time-in-minutes (:start employee))
        end (time->time-in-minutes (:end employee))
        total-minutes (t/in-minutes (t/interval start end))
        total-hours (double (/ total-minutes 60))]
    (merge employee {:total total-hours})))

(defn daily-hours [employee-record]
  (let [date (first employee-record)
        daily-data (second employee-record)
        daily-data-mapped (zipmap fields (first daily-data))]
    (daily-total daily-data-mapped)))


