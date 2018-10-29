(ns iproute.route-test
  (:require [iproute.route :as sut]
            [clojure.test :as t :refer [deftest is]]
            [clojure.string :as string]
            [instaparse.core :as instaparse]))

(def samples
  (map string/trim
       (string/split "10.8.0.32 dev tun11  proto kernel  scope link  src 10.8.0.31
  10.80.255.126 dev ppp0  proto kernel  scope link
  169.254.39.0/24 dev br0  proto kernel  scope link  src 169.254.39.121
  192.168.1.0/24 dev br0  proto kernel  scope link  src 192.168.1.1
  169.254.0.0/16 dev eth0  proto kernel  scope link  src 169.254.70.142
  127.0.0.0/8 dev lo  scope link
  default via 10.80.255.126 dev ppp0" #"\n")))

(def expected
  '(([:route [:net "10.8.0.32"] [:dev "tun11"] [:proto "kernel"] [:scope "link"] [:src "10.8.0.31"]])
    ([:route [:net "10.80.255.126"] [:dev "ppp0"] [:proto "kernel"] [:scope "link"]])
    ([:route [:net "169.254.39.0" "24"] [:dev "br0"] [:proto "kernel"] [:scope "link"] [:src "169.254.39.121"]])
    ([:route [:net "192.168.1.0" "24"] [:dev "br0"] [:proto "kernel"] [:scope "link"] [:src "192.168.1.1"]])
    ([:route [:net "169.254.0.0" "16"] [:dev "eth0"] [:proto "kernel"] [:scope "link"] [:src "169.254.70.142"]])
    ([:route [:net "127.0.0.0" "8"] [:dev "lo"] [:scope "link"]])
    ([:route [:default "10.80.255.126"] [:dev "ppp0"]])))

(deftest route-parser
  (is (= expected
         (map (partial instaparse/parses sut/route-parser) samples))))
