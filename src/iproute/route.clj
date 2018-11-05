(ns iproute.route
  (:require [instaparse.core :as instaparse]
            [clojure.java.io :as io]))

(defn- parse-int [s]
  (BigInteger. s))

(defn- with-parsed-int-key
  "Makes transformation [:metric \"256\"] [:mtu \"1500\"] => [:metric 256] [:mtu 1500]"
  [transform-map & keys]
  (reduce #(assoc %1 %2 (juxt (constantly %2) parse-int)) transform-map keys))

(defn- with-hashable-content
  "Makes transformation [:src [:ip \"10.8.0.31\"]] => [:src {:ip \"10.8.0.31\"}]"
  [transform-map & keys]
  (reduce #(assoc %1 %2 (juxt (constantly %2) (fn [& args] (into {} args)))) transform-map keys))

(def parser (instaparse/parser (io/resource "route.bnf")))

(defn parsing [parse-fn x & opts]
  (instaparse/transform (-> {:ip (fn [& args] [:ip (apply str args)])}
                            (with-hashable-content :net :src :route)
                            (with-parsed-int-key :metric :mtu :advmss :error :hoplimit)
                            (assoc :default (fn [& args] [:default true])))
                        (apply parse-fn parser x opts)))

(def parses (partial parsing instaparse/parses))
(def parse (partial parsing instaparse/parse))
