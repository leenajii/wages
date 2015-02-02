(ns wages.calculations-test
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :refer [info]]
            [wages.calculations :as calculate]))

(deftest calculates-daily-wage-correctly-with-even-hours
  (let [daily-wage (calculate/regular-wage 8)]
    (is (= 30.0 daily-wage))))

(deftest calculates-daily-wage-correctly-with-half-hours
  (let [daily-wage (calculate/regular-wage 7.5)]
    (is (= 28.125 daily-wage))))

(deftest calculates-evening-compensation-correctly-with-even-hours
  (let [evening-wage (calculate/evening-work-wage 8)]
    (is (= 9.2 evening-wage))))

(deftest calculates-evening-compensation-correctly-with-half-hours
  (let [evening-wage (calculate/evening-work-wage 7.5)]
    (is (= 8.625 evening-wage))))

(deftest calculates-overtime-compensation-corretly-for-max-2-hours
  (let [overtime-wage-2-hours (calculate/overtime-compensation 2)
        overtime-wage-1_5-hours (calculate/overtime-compensation 1.5)]
    (is (= 1.875 overtime-wage-2-hours))
    (is (= 1.40625 overtime-wage-1_5-hours))))

(deftest calculates-overtime-compensation-corretly-for-max-4-hours
  (let [overtime-wage-4-hours (calculate/overtime-compensation 4)
        overtime-wage-2_5-hours (calculate/overtime-compensation 2.5)]
    (is (= 5.625 overtime-wage-4-hours))
    (is (= 2.8125 overtime-wage-2_5-hours))))


(deftest calculates-overtime-compensation-correctly-for-over-4-hours
  (let [overtime-wage-8-hours (calculate/overtime-compensation 8)
        overtime-wage-10_5-hours (calculate/overtime-compensation 10.5)]
    (is (= 20.625 overtime-wage-8-hours))
    (is (= 30.0 overtime-wage-10_5-hours))))

(deftest calculates-salary-correctly-for-1-day
  (let [daily-wage-all-compensations (calculate/total-daily-wage 8 8 2)
        daily-wage-no-overtime (calculate/total-daily-wage 8 8 0)
        daily-wage-no-evening-compensation (calculate/total-daily-wage 8 0 2)]
    (is (= 41.075 daily-wage-all-compensations))
    (is (= 39.20 daily-wage-no-overtime))
    (is (= 31.875 daily-wage-no-evening-compensation))))


