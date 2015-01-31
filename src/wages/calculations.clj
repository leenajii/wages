(ns wages.calculations
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]))

;Total Daily Pay =
; Regular Daily Wage + Evening Work Compensation + Overtime Compensations

(def hourly-wage 3.75)

(defn regular-daily-wage [hours]
  (* hourly-wage hours))