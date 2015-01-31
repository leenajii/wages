(defproject wages "0.1.0-SNAPSHOT"
  :description "Salary calculation"
  :url "https://github.com/leenajii/wages.git"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-time/clj-time "0.8.0"]
                 [propertea "1.3.1"]
                 [environ "1.0.0"]
                 [cheshire "5.3.1"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-log4j12 "1.7.7"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jdmk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 [org.clojure/tools.trace "0.7.8"]
                 [org.clojars.quoll/turtle "0.2.2"]
                 [org.clojure/core.memoize "0.5.6"]]
  :plugins [[lein-test-out "0.3.1"]]
  :aot [clojure.tools.logging.impl
        clj-time.core
        wages.main]
  :resource-paths ["src/wages"]
  :source-paths ["src/"]
  :test-paths ["test"]
  :jvm-opts ["-Xmx1g"]
  :profiles {:dev
             {:dependencies [[clj-containment-matchers "0.9.3"]
                             [ns-tracker "0.2.2"]
                             [javax.servlet/servlet-api "2.5"]
                             [ring-mock "0.1.5"]
                             [org.clojure/tools.namespace "0.2.7"]]}}
  :aliases {"start" ["trampoline" "run"]}
  :main wages.main)

