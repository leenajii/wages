(ns wages.calculations-test
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :refer [info]]
            [wages.calculations :as calculate]))

(deftest calculates-daily-wage-correctly-with-even-hours
  (let [daily-wage (calculate/regular-daily-wage 8)]
    (is (= 30.0 daily-wage))))

(deftest rounds-daily-wage-correctly-with-half-hours
  (let [daily-wage (calculate/regular-daily-wage 7.5)]
    (is (= 28.125 daily-wage))))



