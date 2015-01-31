(ns wages.calculations
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.tools.logging :refer [info error]]))

(def hourly-wage 3.75)
(def evening-work-compensation 1.15)

(defn regular-daily-wage [hours]
  (* hourly-wage hours))

(defn evening-work-wage [hours]
  (* evening-work-compensation hours))

(defn- first-overtime-hours [hours]
  (* 0.25 (* hours hourly-wage)))

(defn- next-overtime-hours [hours]
  (* 0.5 (* hours hourly-wage)))

(defn- after-overtime-overs [hours]
  (* 1 (* hours hourly-wage)))

(defn first-and-next [hours]
  (let [next-hours (- hours 2)
        first-hours 2
        next-wage (next-overtime-hours next-hours)
        first-wage (first-overtime-hours first-hours)]
    (+ first-wage next-wage)))

(defn maximum [hours]
  (let [after-hours (- hours 4)
        next-hours 2
        first-hours 2
        after-wage (after-overtime-overs after-hours)
        next-wage (next-overtime-hours next-hours)
        first-wage (first-overtime-hours first-hours)]
    (+ (+ first-wage next-wage) after-wage)))

(defn overtime-compensation [hours]
  (cond
    (> hours 4) (maximum hours)
    (> hours 2) (first-and-next hours)
    (> hours 0) (first-overtime-hours hours)
    :else 0))

(defn total-daily-wage [normal-hours evening-hours overtime-hours]
  (let [normal-wage (regular-daily-wage normal-hours)
        evening-wage (evening-work-wage evening-hours)
        overtime-wage (overtime-compensation overtime-hours)]
    (+ (+ evening-wage overtime-wage) normal-wage)))


