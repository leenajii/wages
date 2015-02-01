(ns wages.test-helpers
  (:require [clj-containment-matchers.core :refer [anything]]
            [clojure.test :refer [is]]))

(defmacro contains-exactly? [actual expected] `(is (clj-containment-matchers.core/contains-exactly? ~actual ~expected)))