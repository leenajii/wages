(ns wages.hours-test
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :refer [info]]
            [wages.test-helpers :refer [contains-exactly?]]
            [wages.hours :as hours]))

(deftest calculates-daily-total-correctly
  (let [daily-hours-1 (hours/daily-total {:end "3:00" :start "1:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-hours-2 (hours/daily-total {:end "23:00" :start "18:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})
        daily-hours-3 (hours/daily-total {:end "23:00" :start "5:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})]
    (is (= 2 (:total daily-hours-1)))
    (is (= 5 (:total daily-hours-2)))
    (is (= 18 (:total daily-hours-3)))))

(deftest calculates-daily-hours-correctly
  (let [daily-hours (hours/daily-hours ["20.3.2014" [["Larry Lolcode" 3 "20.3.2014" "1:00" "3:00"]]])]
    (contains-exactly? daily-hours {:total 2 :end "3:00" :start "1:00" :date "20.3.2014" :person-id 3 :name "Larry Lolcode"})))