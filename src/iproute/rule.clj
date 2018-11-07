(ns iproute.rule
  (:require [instaparse.core :as instaparse :refer [defparser]]
            [iproute.parser.transform :as transform]
            [clojure.java.io :as io]))

(defparser parser (io/resource "rule.ebnf"))

(defn parsing [parse-fn x & opts]
  (instaparse/transform (-> {:rule transform/into-hashmap :rules vector}
                            (transform/parse-using-and-retain-key transform/parse-int :pref)
                            (transform/parse-using-and-retain-key transform/into-hashmap :to)
                            (transform/parse-using-and-retain-key str :ip))
                        (apply parse-fn parser x opts)))

(def parses (partial parsing instaparse/parses))
(def parse (partial parsing instaparse/parse))
