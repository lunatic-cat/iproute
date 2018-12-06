(defproject iproute "0.1.5-SNAPSHOT"
  :description "iproute2 parser"
  :url "https://github.com/razum2um/iproute"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0-RC1"]
                 [instaparse "1.4.9"]]
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.10.0-alpha3"]
                                  [eftest "0.5.3"]]}})
