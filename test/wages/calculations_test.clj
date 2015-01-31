(ns wages.calculations-test
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :refer [info]]
            [wages.calculations :as calculate]))

(deftest calculates-daily-wage-correctly-with-even-hours
  (let [daily-wage (calculate/regular-daily-wage 8)]
    (is (= 30.0 daily-wage))))

(deftest calculates-daily-wage-correctly-with-half-hours
  (let [daily-wage (calculate/regular-daily-wage 7.5)]
    (is (= 28.125 daily-wage))))

(deftest calculates-evening-wage-correctly-with-even-hours
  (let [evening-wage (calculate/evening-work-wage 8)]
    (is (= 39.2 evening-wage))))

(deftest calculates-evening-wage-correctly-with-half-hours
  (let [evening-wage (calculate/evening-work-wage 7.5)]
    (is (= 36.75 evening-wage))))



