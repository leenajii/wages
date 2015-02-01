(ns wages.hours
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]
            [clj-time.format :as ftime]
            [clj-time.core :as t]))

(def fields [:name :person-id :date :start :end])

(defn daily-total [employee]
  (let [start (ftime/parse (ftime/formatters :hour-minute) (:start employee))
        end (ftime/parse (ftime/formatters :hour-minute) (:end employee))
        total-hours (t/in-hours (t/interval start end))]
    (merge employee {:total total-hours})))

(defn daily-hours [employee-record]
  (let [date (first employee-record)
        daily-data (second employee-record)
        daily-hashmap (zipmap fields (first daily-data))]
    (daily-total daily-hashmap)))


