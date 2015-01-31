(ns wages.calculations
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]))

;Total Daily Pay =
; Regular Daily Wage + Evening Work Compensation + Overtime Compensations

(def hourly-wage 3.75)
(def evening-work-compensation 1.15)
(def hourly-evening-wage (+ hourly-wage evening-work-compensation))

(defn regular-daily-wage [hours]
  (* hourly-wage hours))

(defn evening-work-wage [hours]
  (* hourly-evening-wage hours))