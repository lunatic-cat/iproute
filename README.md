# iproute

[![Build Status][BS img]][Build Status]
[![Clojars Project](https://img.shields.io/clojars/v/iproute.svg)](https://clojars.org/iproute)

A library to parse output of `iproute2` using EBNF grammar

## Usage

```clojure
(def samples "10.8.0.32 dev tun11  proto kernel  scope link  src 10.8.0.31
  169.254.0.0/16 dev eth0  proto kernel  scope link  src 169.254.70.142
  fe80::/64 dev eth1  proto kernel  metric 256  mtu 1500 advmss 1440 hoplimit 0
  127.0.0.0/8 dev lo  scope link")
  
(iproute.route/parse samples)

[{:net {:ip "10.8.0.32"}, :dev "tun11", :proto "kernel", :scope "link", :src "10.8.0.31"}
{:net {:ip "169.254.0.0", :mask "16"}, :dev "eth0", :proto "kernel", :scope "link", :src "169.254.70.142"}
{:net {:ip "fe80::", :mask "64"}, :dev "eth1", :proto "kernel", :metric 256, :mtu 1500, :advmss 1440, :hoplimit 0}
{:net {:ip "127.0.0.0", :mask "8"}, :dev "lo", :scope "link"}]

(def rule-samples "0:	from all lookup local
  10097:	from all to 8.8.8.8 lookup ovpnc1
  10098:	from all to 1.1.1.1 lookup ovpnc1
  32766:	from all lookup main
  32767:	from all lookup default")

(iproute.rule/parse rule-samples)

[{:pref 0, :from "all", :lookup "local"}
{:pref 10097, :from "all", :to "8.8.8.8", :lookup "ovpnc1"}
{:pref 10098, :from "all", :to "1.1.1.1", :lookup "ovpnc1"}
{:pref 32766, :from "all", :lookup "main"}
{:pref 32767, :from "all", :lookup "default"}]
```

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
