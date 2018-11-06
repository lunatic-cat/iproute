(ns iproute.route
  (:require [instaparse.core :as instaparse :refer [defparser]]
            [clojure.java.io :as io]))

(defn- parse-int [s] (BigInteger. s))
(defn- into-hashmap [& args] (into {} args))

(defn- parse-using-and-retain-key
  "Makes transformation [:metric \"256\"] [:mtu \"1500\"] => [:metric 256] [:mtu 1500]"
  [transform-map using-fn & keys]
  (reduce #(assoc %1 %2 (juxt (constantly %2) using-fn)) transform-map keys))

(defparser parser (io/resource "route.ebnf"))

(defn parsing [parse-fn x & opts]
  (instaparse/transform (-> {:route into-hashmap :routes vector}
                            (parse-using-and-retain-key into-hashmap :net :src)
                            (parse-using-and-retain-key str :ip)
                            (parse-using-and-retain-key parse-int :metric :mtu :advmss :error :hoplimit)
                            (assoc :default (fn [& args] [:default true])))
                        (apply parse-fn parser x opts)))

(def parses (partial parsing instaparse/parses))
(def parse (partial parsing instaparse/parse))
