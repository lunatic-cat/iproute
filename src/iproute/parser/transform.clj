(ns iproute.parser.transform)

(defn parse-int [s] (BigInteger. s))
(defn into-hashmap [& args] (into {} args))

(defn parse-using-and-retain-key
  "Makes transformation [:metric \"256\"] [:mtu \"1500\"] => [:metric 256] [:mtu 1500]"
  [transform-map using-fn & keys]
  (reduce #(assoc %1 %2 (juxt (constantly %2) using-fn)) transform-map keys))
