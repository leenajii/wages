(ns wages.employees-test
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :refer [info]]
            [wages.test-helpers :refer [contains-exactly?]]
            [wages.employees :as employees]))

(deftest processed-day-correctly-with-one-entry
  (let [processed (employees/process-day {:name "Larry Lolcode" :person-id 3 :monthly-total 0
                                          :monthly-overtime 0 :monthly-evening-hours 0
                                          :normal-salary 0 :overtime-salary 0 :evening-salary 0} ["20.3.2014" [{:name "Larry Lolcode" :person-id 3 :date "20.3.2014" :start "1:00" :end "3:00"}]])]
    (contains-exactly? processed {:evening-salary 2.3, :overtime-salary 0, :normal-salary 7.5,
                                  :monthly-evening-hours 2.0, :monthly-overtime 0, :monthly-total 2.0, :person-id 3, :name "Larry Lolcode"})))
