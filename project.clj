(defproject ndevreeze/cmdline "0.2.0"
  :description "Command line and script functions"
  :url "https://github.com/ndevreeze/cmdline"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  
  :dependencies [[org.clojure/clojure "1.12.1"]
                 [org.clojure/tools.cli "1.1.230"]
                 [clj-commons/fs "1.6.311"]

                 ;; 2023-01-29: solve dependencies
                 [org.apache.httpcomponents/httpclient "4.5.14" :exclusions [commons-logging]]
                 [commons-codec "1.18.0"]

                 ]

  :target-path "target/%s"

  :resource-paths ["resources"]
  
  :profiles {:dev {:dependencies [[midje "1.10.10"]
                                  [clj-commons/pomegranate "1.2.24"]]}}

  :repl-options {:init-ns ndevreeze.cmdline}

  :codox
  {:output-path "docs/api"
   :metadata {:doc/format :markdown}
   :source-uri "https://github.com/ndevreeze/cmdline/blob/master/{filepath}#L{line}"}

  :repositories [["releases" {:url "https://clojars.org/repo/"
                              :creds :gpg}]])

