(ns wages.hours
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]
            [clj-time.format :as ftime]
            [clj-time.core :as t]))

(defn- time->time-in-minutes [time-string]
  (ftime/parse (ftime/formatters :hour-minute) time-string))

(def fields [:name :person-id :date :start :end])
(def morning-start (time->time-in-minutes "6:00"))
(def evening-start (time->time-in-minutes "18:00"))

(defn is-before-or-equals-morning-start [timestamp]
  (if (t/before? timestamp (t/plus morning-start (t/minutes 1))) true ;before 6:01, otherwise 6:00 returns false
    false))

(defn is-after-or-equals-morning-start [timestamp]
  (if (t/after? timestamp (t/minus morning-start (t/minutes 1))) true ;after 17:59, otherwise 18:00 returns false
    false))

(defn is-before-or-equals-evening-start [timestamp]
  (if (t/before? timestamp (t/plus evening-start (t/minutes 1))) true ;before 18:01, otherwise 18:00 returns false
    false))

(defn is-after-or-equals-evening-start [timestamp]
  (if (t/after? timestamp (t/minus evening-start (t/minutes 1))) true ;after 17:59, otherwise 18:00 returns false
    false))

(defn- time-interval-in-hours [start end]
  (let [total-minutes (t/in-minutes (t/interval start end))
        total-hours (double (/ total-minutes 60))]
    total-hours))

(defn daily-evening-hours [employee]
  (let [start (time->time-in-minutes (:start employee))
        end (time->time-in-minutes (:end employee))]
    (cond
      (and (is-before-or-equals-morning-start start) (is-before-or-equals-morning-start end)) (time-interval-in-hours start end) ;start and end before morning start time
      (and (is-before-or-equals-morning-start start) (is-before-or-equals-evening-start end)) (time-interval-in-hours start morning-start) ;start before morning start, end before evening start --> only morning "evening-hours"
      (and (is-after-or-equals-evening-start start) (is-after-or-equals-evening-start end)) (time-interval-in-hours start end) ;start and end after evening start
      (and (is-after-or-equals-morning-start start) (is-after-or-equals-evening-start end)) (time-interval-in-hours evening-start end) ;start before morning start, end after evening start
      :else 0)))

(defn daily-overtime [employee]
  (let [daily-total (:total employee)]
  (if (> daily-total 8)
    (- daily-total 8)
    0)))

(defn daily-total [employee]
  (let [start (time->time-in-minutes (:start employee))
        end (time->time-in-minutes (:end employee))
        total-hours (time-interval-in-hours start end)]
    (merge employee {:total total-hours})))

(defn daily-hours [employee-record]
  (let [date (first employee-record)
        daily-data (second employee-record)
        daily-data-mapped (zipmap fields (first daily-data))]
    (daily-total daily-data-mapped)))


