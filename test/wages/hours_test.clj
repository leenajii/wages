(ns wages.hours-test
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :refer [info]]
            [wages.test-helpers :refer [contains-exactly?]]
            [wages.hours :as hours]))

(deftest calculates-daily-total-correctly
  (let [daily-hours-1 (hours/daily-total ["20.3.2014" [{:end "3:00" :start "1:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"}]])
        daily-hours-2 (hours/daily-total ["20.3.2014" [{:end "23:00" :start "18:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"}]])
        daily-hours-3 (hours/daily-total ["20.3.2014" [{:end "23:00" :start "5:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"}]])
        daily-hours-4 (hours/daily-total ["20.3.2014" [{:end "23:15" :start "5:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"}]])]
    (is (= 2.0 (:total daily-hours-1)))
    (is (= 5.0 (:total daily-hours-2)))
    (is (= 18.0 (:total daily-hours-3)))
    (is (= 18.25 (:total daily-hours-4)))))

(deftest calculates-daily-overtime-correctly
  (let [daily-overtime-1 (hours/daily-overtime {:total 2.0 :end "3:00" :start "1:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-overtime-2 (hours/daily-overtime {:total 5.0 :end "23:00" :start "18:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-overtime-3 (hours/daily-overtime {:total 18.0 :end "23:00" :start "5:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-overtime-4 (hours/daily-overtime {:total 18.25 :end "23:15" :start "5:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})]
    (is (= 0 (:overtime daily-overtime-1)))
    (is (= 0 (:overtime daily-overtime-2)))
    (is (= 10.0 (:overtime daily-overtime-3)))
    (is (= 10.25 (:overtime daily-overtime-4)))))

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

(deftest calculates-daily-evening-hours-correctly-when-start-and-end-are-after-evening-start
  (let [daily-evening-hours-1 (hours/daily-evening-hours {:end "22:00" :start "18:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-evening-hours-2 (hours/daily-evening-hours {:end "19:15" :start "18:30" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})]
    (is (= 4.0 daily-evening-hours-1))
    (is (= 0.75 daily-evening-hours-2) )))

(deftest calculates-daily-evening-hours-correctly-when-start-is-after-morning-start-and-end-after-evening-start
  (let [daily-evening-hours-1 (hours/daily-evening-hours {:end "22:00" :start "6:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-evening-hours-2 (hours/daily-evening-hours {:end "18:15" :start "8:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})]
    (is (= 4.0 daily-evening-hours-1))
    (is (= 0.25 daily-evening-hours-2) )))

(deftest calculates-daily-evening-hours-correctly-when-start-is-before-morning-start-and-end-after-evening-start
  (let [daily-evening-hours-1 (hours/daily-evening-hours {:end "22:00" :start "5:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-evening-hours-2 (hours/daily-evening-hours {:end "18:15" :start "5:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})]
    (is (= 5.0 daily-evening-hours-1))
    (is (= 1.25 daily-evening-hours-2) )))

(deftest calculates-daily-hours-correctly
  (let [daily-hours-1 (hours/daily-hours ["20.3.2014" [{:name "Larry Lolcode" :person-id 3 :date "20.3.2014" :start "1:00" :end "3:00"}]])
        daily-hours-2 (hours/daily-hours ["20.3.2014" [{:name "Larry Lolcode" :person-id 3 :date "20.3.2014" :start "6:00" :end "20:00"}]])]
    (contains-exactly? daily-hours-1 {:evening-hours 2.0 :overtime 0 :total 2.0 :end "3:00" :start "1:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
    (contains-exactly? daily-hours-2 {:evening-hours 2.0 :overtime 6.0 :total 14.0 :end "20:00" :start "6:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})))
