# iproute

A Clojure library designed to parse output of ip route utils

## Usage

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
