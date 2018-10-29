(ns iproute.route
  (:require [instaparse.core :as instaparse]))

(def route-grammar
  "route = <_>? (net | default) dev proto? scope? src?
  net = ip mask?
  default = <'default'> <_> <'via'> <_> ip <_>?
  <mask> = <'/'> #'\\d{1,2}' <_>?
  <ip> = #'\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}' <_>?
  <n> = #'[0-9]+'
  dev = <'dev'> <_> word <_>?
  proto = <'proto'> <_> word <_>?
  scope = <'scope'> <_> word <_>?
  src = <'src'> <_> ip <_>?
  <word> = #'[a-za-z0-9]+'
  <_> = #'\\s'*")

(def route-parser (instaparse/parser route-grammar))

