# cmdline

Small wrapper around command line parsing with clojure.tools.cli

## Why?

Combine named command line arguments from three sources:
1. Explicitly given on the command line.
2. Given in a user defined config file.
3. Defaults as given in the source code.

## Installation

Leiningen/Boot

    [ndevreeze/cmdline "0.1.2"]

Clojure CLI/deps.edn

    ndevreeze/cmdline {:mvn/version "0.1.2"}

[![Clojars Project](https://img.shields.io/clojars/v/ndevreeze/cmdline.svg)](https://clojars.org/ndevreeze/cmdline)

## Usage

Require:

    (ns my.namespace
      (:require [nl.nicodevreeze.cmdline :as cl]))
            
Define the command line options:

    (def cli-options
      [["-c" "--config CONFIG" "Config file"
        :default "~/.config/my-program/my-program.edn"]
       ["-h" "--help" "Show this help"]
       ["-p" "--port PORT" "TCP port to use"
        :default 1234 :parse-fn #(Integer/parseInt %)]
       ["-v" "--verbose" "Verbose output"]])

Define the worker function:

    (defn do-script
      "Main script"
      [opt arguments ctx]
      (when (:verbose opt)
        (output-some-more)))

Small -main function:

    (defn -main [& args]
      (cl/check-and-exec "Description of script"
                         cli-options do-script args ctx))

Ctx/context can be anything, mostly a map.

## Developing

### Testing

    $ lein midje

or:

    $ lein repl (or start in Cider)
    (use 'midje.repl)
    (autotest)
    
### Bugs

* No known errors, see Github/issues.

## Related and similar projects (libraries)

* https://github.com/clojure/tools.cli - main library for parsing options.
    
## License

Copyright © 2021 Nico de Vreeze

Distributed under the Eclipse Public License, the same as Clojure.
