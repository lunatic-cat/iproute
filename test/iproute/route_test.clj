(ns iproute.route-test
  (:require [iproute.route :refer :all]
            [clojure.test :refer :all]
            [clojure.string :as string]
            [instaparse.core :as instaparse]))

;; ip route list
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
  '(([:route {:dev "tun11", :net {:ip "10.8.0.32"}, :proto "kernel", :scope "link", :src {:ip "10.8.0.31"}}])
    ([:route {:dev "ppp0", :net {:ip "10.80.255.126"}, :proto "kernel", :scope "link"}])
    ([:route {:dev "br0", :net {:ip "169.254.39.0", :mask "24"}, :proto "kernel", :scope "link", :src {:ip "169.254.39.121"}}])
    ([:route {:dev "br0", :net {:ip "192.168.1.0", :mask "24"}, :proto "kernel", :scope "link", :src {:ip "192.168.1.1"}}])
    ([:route {:dev "eth0", :net {:ip "169.254.0.0", :mask "16"}, :proto "kernel", :scope "link", :src {:ip "169.254.70.142"}}])
    ([:route {:dev "lo", :net {:ip "127.0.0.0", :mask "8"}, :scope "link"}])
    ([:route {:default true, :dev "ppp0", :via [:ip "10.80.255.126"]}])))

(deftest test-route-parser-samples
  (is (= expected (map parses samples))))

(def all-samples
  (map string/trim (string/split
    "10.8.0.32 dev tun11  proto kernel  scope link  src 10.8.0.31
    10.80.255.126 dev ppp0  proto kernel  scope link
    169.254.39.0/24 dev br0  proto kernel  scope link  src 169.254.39.121
    192.168.1.0/24 dev br0  proto kernel  scope link  src 192.168.1.1
    169.254.0.0/16 dev eth0  proto kernel  scope link  src 169.254.70.142
    127.0.0.0/8 dev lo  scope link
    default via 10.80.255.126 dev ppp0
    10.8.0.32 dev tun11  table ovpnc1  proto kernel  scope link  src 10.8.0.31
    10.80.255.126 dev ppp0  table ovpnc1  proto kernel  scope link
    169.254.39.0/24 dev br0  table ovpnc1  proto kernel  scope link  src 169.254.39.121
    192.168.1.0/24 dev br0  table ovpnc1  proto kernel  scope link  src 192.168.1.1
    169.254.0.0/16 dev eth0  table ovpnc1  proto kernel  scope link  src 169.254.70.142
    127.0.0.0/8 dev lo  table ovpnc1  scope link
    default via 10.8.0.32 dev tun11  table ovpnc1
    local 192.168.1.1 dev br0  table local  proto kernel  scope host  src 192.168.1.1
    broadcast 127.255.255.255 dev lo  table local  proto kernel  scope link  src 127.0.0.1
    broadcast 192.168.1.0 dev br0  table local  proto kernel  scope link  src 192.168.1.1
    broadcast 169.254.39.0 dev br0  table local  proto kernel  scope link  src 169.254.39.121
    broadcast 169.254.0.0 dev eth0  table local  proto kernel  scope link  src 169.254.70.142
    broadcast 192.168.1.255 dev br0  table local  proto kernel  scope link  src 192.168.1.1
    local 10.8.0.31 dev tun11  table local  proto kernel  scope host  src 10.8.0.31
    broadcast 169.254.255.255 dev eth0  table local  proto kernel  scope link  src 169.254.70.142
    local 5.166.107.184 dev ppp0  table local  proto kernel  scope host  src 5.166.107.184
    broadcast 5.166.107.184 dev ppp0  table local  proto kernel  scope link  src 5.166.107.184
    broadcast 169.254.39.255 dev br0  table local  proto kernel  scope link  src 169.254.39.121
    local 169.254.70.142 dev eth0  table local  proto kernel  scope host  src 169.254.70.142
    local 169.254.39.121 dev br0  table local  proto kernel  scope host  src 169.254.39.121
    broadcast 127.0.0.0 dev lo  table local  proto kernel  scope link  src 127.0.0.1
    local 127.0.0.1 dev lo  table local  proto kernel  scope host  src 127.0.0.1
    local 127.0.0.0/8 dev lo  table local  proto kernel  scope host  src 127.0.0.1
    2a02:2698:422:3c70::/64 dev br0  proto kernel  metric 256  mtu 1492 advmss 1432 hoplimit 0
    fe80::bc40:c8ff:fe32:9e55 dev ifb1  proto kernel  metric 256  mtu 1500 advmss 1440 hoplimit 0
    fe80::c414:aff:feab:cb dev ifb0  proto kernel  metric 256  mtu 1500 advmss 1440 hoplimit 0
    fe80::d217:c2ff:feb2:3128 dev vlan2  proto kernel  metric 256  mtu 1500 advmss 1440 hoplimit 0
    fe80::/64 dev eth1  proto kernel  metric 256  mtu 1500 advmss 1440 hoplimit 0
    fe80::/64 dev vlan1  proto kernel  metric 256  mtu 1500 advmss 1440 hoplimit 0
    fe80::/64 dev br0  proto kernel  metric 256  mtu 1500 advmss 1440 hoplimit 0
    fe80::/64 dev ifb0  proto kernel  metric 256  mtu 1500 advmss 1440 hoplimit 0
    fe80::/64 dev ifb1  proto kernel  metric 256  mtu 1500 advmss 1440 hoplimit 0
    fe80::/64 dev vlan2  proto kernel  metric 256  mtu 1500 advmss 1440 hoplimit 0
    fe80::/64 dev ppp0  proto kernel  metric 256  mtu 1492 advmss 1432 hoplimit 0
    fe80::/10 dev ppp0  metric 1  mtu 1492 advmss 1432 hoplimit 0
    fe80::/10 dev ppp0  proto kernel  metric 256  mtu 1492 advmss 1432 hoplimit 0
    default via fe80::8626:2bff:feec:d78b dev ppp0  proto kernel  metric 1024  expires 4471sec mtu 1492 advmss 1432 hoplimit 64
    unreachable default dev lo  table 0  proto kernel  metric 4294967295  error -101 hoplimit 255
    local ::1 via :: dev lo  table local  proto none  metric 0  mtu 16436 advmss 16376 hoplimit 0
    local 2a02:2698:422:3c70:: via :: dev lo  table local  proto none  metric 0  mtu 16436 advmss 16376 hoplimit 0
    local 2a02:2698:422:3c70::1 via :: dev lo  table local  proto none  metric 0  mtu 16436 advmss 16376 hoplimit 0
    local fe80:: via :: dev lo  table local  proto none  metric 0  mtu 16436 advmss 16376 hoplimit 0
    local fe80:: via :: dev lo  table local  proto none  metric 0  mtu 16436 advmss 16376 hoplimit 0
    local fe80:: via :: dev lo  table local  proto none  metric 0  mtu 16436 advmss 16376 hoplimit 0
    local fe80::810:3ac7:995b:e0ac via :: dev lo  table local  proto none  metric 0  mtu 16436 advmss 16376 hoplimit 0
    local fe80::bc40:c8ff:fe32:9e55 via :: dev lo  table local  proto none  metric 0  mtu 16436 advmss 16376 hoplimit 0
    local fe80::c414:aff:feab:cb via :: dev lo  table local  proto none  metric 0  mtu 16436 advmss 16376 hoplimit 0
    local fe80::d217:c2ff:feb2:3128 via :: dev lo  table local  proto none  metric 0  mtu 16436 advmss 16376 hoplimit 0
    local fe80::d217:c2ff:feb2:3128 via :: dev lo  table local  proto none  metric 0  mtu 16436 advmss 16376 hoplimit 0
    local fe80::d217:c2ff:feb2:3128 via :: dev lo  table local  proto none  metric 0  mtu 16436 advmss 16376 hoplimit 0
    ff00::/8 dev eth1  table local  metric 256  mtu 1500 advmss 1440 hoplimit 0
    ff00::/8 dev vlan1  table local  metric 256  mtu 1500 advmss 1440 hoplimit 0
    ff00::/8 dev br0  table local  metric 256  mtu 1500 advmss 1440 hoplimit 0
    ff00::/8 dev ifb0  table local  metric 256  mtu 1500 advmss 1440 hoplimit 0
    ff00::/8 dev ifb1  table local  metric 256  mtu 1500 advmss 1440 hoplimit 0
    ff00::/8 dev vlan2  table local  metric 256  mtu 1500 advmss 1440 hoplimit 0
    ff00::/8 dev ppp0  table local  metric 256  mtu 1492 advmss 1432 hoplimit 0
    unreachable default dev lo  table 0  proto kernel  metric 4294967295  error -101 hoplimit 255" #"\n")))

(def all-expected
  '(([:route {:dev "tun11", :net {:ip "10.8.0.32"}, :proto "kernel", :scope "link", :src {:ip "10.8.0.31"}}])
    ([:route {:dev "ppp0", :net {:ip "10.80.255.126"}, :proto "kernel", :scope "link"}])
    ([:route {:dev "br0", :net {:ip "169.254.39.0", :mask "24"}, :proto "kernel", :scope "link", :src {:ip "169.254.39.121"}}])
    ([:route {:dev "br0", :net {:ip "192.168.1.0", :mask "24"}, :proto "kernel", :scope "link", :src {:ip "192.168.1.1"}}])
    ([:route {:dev "eth0", :net {:ip "169.254.0.0", :mask "16"}, :proto "kernel", :scope "link", :src {:ip "169.254.70.142"}}])
    ([:route {:dev "lo", :net {:ip "127.0.0.0", :mask "8"}, :scope "link"}])
    ([:route {:default true, :dev "ppp0", :via [:ip "10.80.255.126"]}])
    ([:route {:dev "tun11", :net {:ip "10.8.0.32"}, :proto "kernel", :scope "link", :src {:ip "10.8.0.31"}, :table "ovpnc1"}])
    ([:route {:dev "ppp0", :net {:ip "10.80.255.126"}, :proto "kernel", :scope "link", :table "ovpnc1"}])
    ([:route {:dev "br0", :net {:ip "169.254.39.0", :mask "24"}, :proto "kernel", :scope "link", :src {:ip "169.254.39.121"}, :table "ovpnc1"}])
    ([:route {:dev "br0", :net {:ip "192.168.1.0", :mask "24"}, :proto "kernel", :scope "link", :src {:ip "192.168.1.1"}, :table "ovpnc1"}])
    ([:route {:dev "eth0", :net {:ip "169.254.0.0", :mask "16"}, :proto "kernel", :scope "link", :src {:ip "169.254.70.142"}, :table "ovpnc1"}])
    ([:route {:dev "lo", :net {:ip "127.0.0.0", :mask "8"}, :scope "link", :table "ovpnc1"}])
    ([:route {:default true, :dev "tun11", :table "ovpnc1", :via [:ip "10.8.0.32"]}])
    ([:route {:dev "br0", :mode "local", :net {:ip "192.168.1.1"}, :proto "kernel", :scope "host", :src {:ip "192.168.1.1"}, :table "local"}])
    ([:route {:dev "lo", :mode "broadcast", :net {:ip "127.255.255.255"}, :proto "kernel", :scope "link", :src {:ip "127.0.0.1"}, :table "local"}])
    ([:route {:dev "br0", :mode "broadcast", :net {:ip "192.168.1.0"}, :proto "kernel", :scope "link", :src {:ip "192.168.1.1"}, :table "local"}])
    ([:route {:dev "br0", :mode "broadcast", :net {:ip "169.254.39.0"}, :proto "kernel", :scope "link", :src {:ip "169.254.39.121"}, :table "local"}])
    ([:route {:dev "eth0", :mode "broadcast", :net {:ip "169.254.0.0"}, :proto "kernel", :scope "link", :src {:ip "169.254.70.142"}, :table "local"}])
    ([:route {:dev "br0", :mode "broadcast", :net {:ip "192.168.1.255"}, :proto "kernel", :scope "link", :src {:ip "192.168.1.1"}, :table "local"}])
    ([:route {:dev "tun11", :mode "local", :net {:ip "10.8.0.31"}, :proto "kernel", :scope "host", :src {:ip "10.8.0.31"}, :table "local"}])
    ([:route {:dev "eth0", :mode "broadcast", :net {:ip "169.254.255.255"}, :proto "kernel", :scope "link", :src {:ip "169.254.70.142"}, :table "local"}])
    ([:route {:dev "ppp0", :mode "local", :net {:ip "5.166.107.184"}, :proto "kernel", :scope "host", :src {:ip "5.166.107.184"}, :table "local"}])
    ([:route {:dev "ppp0", :mode "broadcast", :net {:ip "5.166.107.184"}, :proto "kernel", :scope "link", :src {:ip "5.166.107.184"}, :table "local"}])
    ([:route {:dev "br0", :mode "broadcast", :net {:ip "169.254.39.255"}, :proto "kernel", :scope "link", :src {:ip "169.254.39.121"}, :table "local"}])
    ([:route {:dev "eth0", :mode "local", :net {:ip "169.254.70.142"}, :proto "kernel", :scope "host", :src {:ip "169.254.70.142"}, :table "local"}])
    ([:route {:dev "br0", :mode "local", :net {:ip "169.254.39.121"}, :proto "kernel", :scope "host", :src {:ip "169.254.39.121"}, :table "local"}])
    ([:route {:dev "lo", :mode "broadcast", :net {:ip "127.0.0.0"}, :proto "kernel", :scope "link", :src {:ip "127.0.0.1"}, :table "local"}])
    ([:route {:dev "lo", :mode "local", :net {:ip "127.0.0.1"}, :proto "kernel", :scope "host", :src {:ip "127.0.0.1"}, :table "local"}])
    ([:route {:dev "lo", :mode "local", :net {:ip "127.0.0.0", :mask "8"}, :proto "kernel", :scope "host", :src {:ip "127.0.0.1"}, :table "local"}])
    ([:route {:advmss 1432, :dev "br0", :hoplimit 0, :metric 256, :mtu 1492, :net {:ip "2a02:2698:422:3c70::", :mask "64"}, :proto "kernel"}])
    ([:route {:advmss 1440, :dev "ifb1", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "fe80::bc40:c8ff:fe32:9e55"}, :proto "kernel"}])
    ([:route {:advmss 1440, :dev "ifb0", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "fe80::c414:aff:feab:cb"}, :proto "kernel"}])
    ([:route {:advmss 1440, :dev "vlan2", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "fe80::d217:c2ff:feb2:3128"}, :proto "kernel"}])
    ([:route {:advmss 1440, :dev "eth1", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "fe80::", :mask "64"}, :proto "kernel"}])
    ([:route {:advmss 1440, :dev "vlan1", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "fe80::", :mask "64"}, :proto "kernel"}])
    ([:route {:advmss 1440, :dev "br0", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "fe80::", :mask "64"}, :proto "kernel"}])
    ([:route {:advmss 1440, :dev "ifb0", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "fe80::", :mask "64"}, :proto "kernel"}])
    ([:route {:advmss 1440, :dev "ifb1", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "fe80::", :mask "64"}, :proto "kernel"}])
    ([:route {:advmss 1440, :dev "vlan2", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "fe80::", :mask "64"}, :proto "kernel"}])
    ([:route {:advmss 1432, :dev "ppp0", :hoplimit 0, :metric 256, :mtu 1492, :net {:ip "fe80::", :mask "64"}, :proto "kernel"}])
    ([:route {:advmss 1432, :dev "ppp0", :hoplimit 0, :metric 1, :mtu 1492, :net {:ip "fe80::", :mask "10"}}])
    ([:route {:advmss 1432, :dev "ppp0", :hoplimit 0, :metric 256, :mtu 1492, :net {:ip "fe80::", :mask "10"}, :proto "kernel"}])
    ([:route {:advmss 1432, :default true, :dev "ppp0", :expires "4471", :hoplimit 64, :metric 1024, :mtu 1492, :proto "kernel", :via [:ip "fe80::8626:2bff:feec:d78b"]}])
    ([:route {:default true, :dev "lo", :error -101, :hoplimit 255, :metric 4294967295, :mode "unreachable", :proto "kernel", :table "0"}])
    ([:route {:advmss 16376, :dev "lo", :hoplimit 0, :metric 0, :mode "local", :mtu 16436, :net {:ip "::1"}, :proto "none", :table "local", :via [:ip "::"]}])
    ([:route {:advmss 16376, :dev "lo", :hoplimit 0, :metric 0, :mode "local", :mtu 16436, :net {:ip "2a02:2698:422:3c70::"}, :proto "none", :table "local", :via [:ip "::"]}])
    ([:route {:advmss 16376, :dev "lo", :hoplimit 0, :metric 0, :mode "local", :mtu 16436, :net {:ip "2a02:2698:422:3c70::1"}, :proto "none", :table "local", :via [:ip "::"]}])
    ([:route {:advmss 16376, :dev "lo", :hoplimit 0, :metric 0, :mode "local", :mtu 16436, :net {:ip "fe80::"}, :proto "none", :table "local", :via [:ip "::"]}])
    ([:route {:advmss 16376, :dev "lo", :hoplimit 0, :metric 0, :mode "local", :mtu 16436, :net {:ip "fe80::"}, :proto "none", :table "local", :via [:ip "::"]}])
    ([:route {:advmss 16376, :dev "lo", :hoplimit 0, :metric 0, :mode "local", :mtu 16436, :net {:ip "fe80::"}, :proto "none", :table "local", :via [:ip "::"]}])
    ([:route {:advmss 16376, :dev "lo", :hoplimit 0, :metric 0, :mode "local", :mtu 16436, :net {:ip "fe80::810:3ac7:995b:e0ac"}, :proto "none", :table "local", :via [:ip "::"]}])
    ([:route {:advmss 16376, :dev "lo", :hoplimit 0, :metric 0, :mode "local", :mtu 16436, :net {:ip "fe80::bc40:c8ff:fe32:9e55"}, :proto "none", :table "local", :via [:ip "::"]}])
    ([:route {:advmss 16376, :dev "lo", :hoplimit 0, :metric 0, :mode "local", :mtu 16436, :net {:ip "fe80::c414:aff:feab:cb"}, :proto "none", :table "local", :via [:ip "::"]}])
    ([:route {:advmss 16376, :dev "lo", :hoplimit 0, :metric 0, :mode "local", :mtu 16436, :net {:ip "fe80::d217:c2ff:feb2:3128"}, :proto "none", :table "local", :via [:ip "::"]}])
    ([:route {:advmss 16376, :dev "lo", :hoplimit 0, :metric 0, :mode "local", :mtu 16436, :net {:ip "fe80::d217:c2ff:feb2:3128"}, :proto "none", :table "local", :via [:ip "::"]}])
    ([:route {:advmss 16376, :dev "lo", :hoplimit 0, :metric 0, :mode "local", :mtu 16436, :net {:ip "fe80::d217:c2ff:feb2:3128"}, :proto "none", :table "local", :via [:ip "::"]}])
    ([:route {:advmss 1440, :dev "eth1", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "ff00::", :mask "8"}, :table "local"}])
    ([:route {:advmss 1440, :dev "vlan1", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "ff00::", :mask "8"}, :table "local"}])
    ([:route {:advmss 1440, :dev "br0", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "ff00::", :mask "8"}, :table "local"}])
    ([:route {:advmss 1440, :dev "ifb0", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "ff00::", :mask "8"}, :table "local"}])
    ([:route {:advmss 1440, :dev "ifb1", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "ff00::", :mask "8"}, :table "local"}])
    ([:route {:advmss 1440, :dev "vlan2", :hoplimit 0, :metric 256, :mtu 1500, :net {:ip "ff00::", :mask "8"}, :table "local"}])
    ([:route {:advmss 1432, :dev "ppp0", :hoplimit 0, :metric 256, :mtu 1492, :net {:ip "ff00::", :mask "8"}, :table "local"}])
    ([:route {:default true, :dev "lo", :error -101, :hoplimit 255, :metric 4294967295, :mode "unreachable", :proto "kernel", :table "0"}])))

(deftest test-route-parser-all-samples
  (is (= all-expected (map parses all-samples))))

(comment
  (do (require 'iproute.route-test :reload-all) (clojure.pprint/print-table [0 1] (map (juxt identity parse) all-samples)))
  )

