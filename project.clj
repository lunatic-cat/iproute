(defproject iproute "0.1.0-SNAPSHOT"
  :description "iproute2 parser"
  :url "https://github.com/razum2um/iproute"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0-RC1"]
                 [instaparse "1.4.9"]]
  :profiles {:dev {:dependencies [[rewrite-clj "0.6.1"]
                                  [eftest "0.5.3"]
                                  [com.bhauman/rebel-readline "0.1.4" :exclusions [rewrite-clj]]
                                  [cljsh "0.1.0-SNAPSHOT"]
                                  [com.rpl/specter "1.1.1"]
                                  [aprint "0.1.3"]
                                  [robert/hooke "1.3.0"]
                                  [org.clojure/tools.trace "0.7.10"]]}})
