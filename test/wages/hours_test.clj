(ns wages.hours-test
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :refer [info]]
            [wages.test-helpers :refer [contains-exactly?]]
            [wages.hours :as hours]))

(deftest calculates-daily-total-correctly
  (let [daily-hours (hours/daily-total {:end "3:00" :start "1:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})]
    (is (= 2 (:total daily-hours)))))

(deftest calculates-daily-hours-correctly
  (let [daily-hours (hours/daily-hours ["20.3.2014" [["Larry Lolcode" 3 "20.3.2014" "1:00" "3:00"]]])]
    (contains-exactly? daily-hours {:total 2 :end "3:00" :start "1:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})))