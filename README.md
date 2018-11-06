# iproute

[![Build Status][BS img]][Build Status]

A Clojure library designed to parse output of ip route utils

## Usage

```clojure
(def samples (map string/trim (string/split "10.8.0.32 dev tun11  proto kernel  scope link  src 10.8.0.31
  169.254.0.0/16 dev eth0  proto kernel  scope link  src 169.254.70.142
  fe80::/64 dev eth1  proto kernel  metric 256  mtu 1500 advmss 1440 hoplimit 0
  127.0.0.0/8 dev lo  scope link" #"\n")))

(clojure.pprint/print-table (map (fn [x] {:iproute x :parsed (iproute.route/parse x)}) samples))
```

|                                                                      :iproute |                                                                                                                     :parsed |
|-------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------|
|                  10.8.0.32 dev tun11  proto kernel  scope link  src 10.8.0.31 |                     [:route {:net {:ip "10.8.0.32"}, :dev "tun11", :proto "kernel", :scope "link", :src {:ip "10.8.0.31"}}] |
|         169.254.0.0/16 dev eth0  proto kernel  scope link  src 169.254.70.142 |   [:route {:net {:ip "169.254.0.0", :mask "16"}, :dev "eth0", :proto "kernel", :scope "link", :src {:ip "169.254.70.142"}}] |
| fe80::/64 dev eth1  proto kernel  metric 256  mtu 1500 advmss 1440 hoplimit 0 | [:route {:net {:ip "fe80::", :mask "64"}, :dev "eth1", :proto "kernel", :metric 256, :mtu 1500, :advmss 1440, :hoplimit 0}] |
|                                                127.0.0.0/8 dev lo  scope link |                                                      [:route {:net {:ip "127.0.0.0", :mask "8"}, :dev "lo", :scope "link"}] |



## REPL Development
Development runs only java-8 due rewrite-clj oddity

```
lein trampoline run -m rebel-readline.main
(require '[cljsh.repl :refer [save defnc defc]]) (use 'aprint.core) (use 'cljsh.namespaces) (use 'cljsh.deps) (use 'com.rpl.specter) (require 'cljsh.complement) (cljsh.complement/patch) (use 'cljsh.inspect) (require 'cljsh.utils) (require '[cljsh.source :refer [source-expand-ns]]) (require '[clojure.java.io :as io]) (use 'clojure.tools.trace)
```

## Tests

```
(use 'eftest.runner)
(run-tests (find-tests "test"))
```

## License

Copyright © 2018 Vlad Bokov

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[Build Status]: https://travis-ci.org/lunatic-cat/iproute
[BS img]: https://travis-ci.org/lunatic-cat/iproute.png
