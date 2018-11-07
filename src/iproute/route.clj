(ns iproute.route
  (:require [instaparse.core :as instaparse :refer [defparser]]
            [iproute.parser.transform :as transform]
            [clojure.java.io :as io]))

(defparser parser (io/resource "route.ebnf"))

(defn parsing [parse-fn x & opts]
  (instaparse/transform (-> {:route transform/into-hashmap :routes vector}
                            (transform/parse-using-and-retain-key transform/into-hashmap :net)
                            (transform/parse-using-and-retain-key str :ip :via :src)
                            (transform/parse-using-and-retain-key transform/parse-int :metric :mtu :advmss :error :hoplimit)
                            (assoc :default (fn [& args] [:default true])))
                        (apply parse-fn parser x opts)))

(def parses (partial parsing instaparse/parses))
(def parse (partial parsing instaparse/parse))
