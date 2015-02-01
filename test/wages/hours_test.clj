(ns wages.hours-test
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :refer [info]]
            [wages.test-helpers :refer [contains-exactly?]]
            [wages.hours :as hours]))

(deftest calculates-daily-total-correctly
  (let [daily-hours-1 (hours/daily-total {:end "3:00" :start "1:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-hours-2 (hours/daily-total {:end "23:00" :start "18:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-hours-3 (hours/daily-total {:end "23:00" :start "5:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-hours-4 (hours/daily-total {:end "23:15" :start "5:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})]
    (is (= 2.0 (:total daily-hours-1)))
    (is (= 5.0 (:total daily-hours-2)))
    (is (= 18.0 (:total daily-hours-3)))
    (is (= 18.25 (:total daily-hours-4)))))

(deftest calculates-daily-overtime-correctly
  (let [daily-overtime-1 (hours/daily-overtime {:total 2.0 :end "3:00" :start "1:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-overtime-2 (hours/daily-overtime {:total 5.0 :end "23:00" :start "18:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-overtime-3 (hours/daily-overtime {:total 18.0 :end "23:00" :start "5:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-overtime-4 (hours/daily-overtime {:total 18.25 :end "23:15" :start "5:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})]
    (is (= 0 daily-overtime-1))
    (is (= 0 daily-overtime-2))
    (is (= 10.0 daily-overtime-3))
    (is (= 10.25 daily-overtime-4))))

(deftest calculates-daily-evening-hours-correctly-when-start-and-end-are-before-morning-start
  (let [daily-evening-hours-1 (hours/daily-evening-hours {:end "3:00" :start "1:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-evening-hours-2 (hours/daily-evening-hours {:end "6:00" :start "3:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})]
    (is (= 2.0 daily-evening-hours-1))
    (is (= 3.0 daily-evening-hours-2) )))

(deftest calculates-daily-evening-hours-correctly-when-start-is-before-morning-start-and-end-before-evening-start
  (let [daily-evening-hours-1 (hours/daily-evening-hours {:end "14:00" :start "1:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-evening-hours-2 (hours/daily-evening-hours {:end "18:00" :start "5:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})]
    (is (= 5.0 daily-evening-hours-1))
    (is (= 1.0 daily-evening-hours-2) )))

(deftest calculates-daily-hours-correctly
  (let [daily-hours (hours/daily-hours ["20.3.2014" [["Larry Lolcode" 3 "20.3.2014" "1:00" "3:00"]]])]
    (contains-exactly? daily-hours {:total 2.0 :end "3:00" :start "1:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})))