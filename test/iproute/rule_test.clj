(ns iproute.rule-test
  (:require [iproute.rule :refer :all]
            [clojure.test :refer :all]))

;; ip rule list
(def samples
  "0:	from all lookup local
  10097:	from all to 8.8.8.8 lookup ovpnc1
  10098:	from all to 1.1.1.1 lookup ovpnc1
  32765:	from all to 192.168.0.1/24 lookup ovpnc1
  32766:	from all lookup main
  32767:	from all lookup default")

(def expected
  '([{:from "all", :lookup "local", :pref 0}
     {:from "all", :lookup "ovpnc1", :pref 10097, :to {:ip "8.8.8.8"}}
     {:from "all", :lookup "ovpnc1", :pref 10098, :to {:ip "1.1.1.1"}}
     {:from "all", :lookup "ovpnc1",:pref 32765,:to {:ip "192.168.0.1", :mask "24"}}
     {:from "all", :lookup "main", :pref 32766}
     {:from "all", :lookup "default", :pref 32767}]))


(deftest test-route-parser-samples
  (is (= expected (parses samples))))

(comment
  ;; overview for examples to parsed result
  (do (require 'iproute.rule-test :reload-all) (clojure.pprint/print-table [0 1] (map (comp (juxt #(clojure.string/replace % "\t" " ") parse) clojure.string/trim) (clojure.string/split samples #"\n"))))
  )
