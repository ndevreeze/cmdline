(defproject ndevreeze/cmdline "0.1.2"
  :description "Command line and script functions"
  :url "https://github.com/ndevreeze/cmdline"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "1.0.194"]
                 [me.raynes/fs "1.4.6"]]

  :target-path "target/%s"

  :resource-paths ["resources"]
  
  :profiles {:dev {:dependencies [[midje "1.9.9"]
                                  [clj-commons/pomegranate "1.2.0"]]}}

  :repl-options {:init-ns ndevreeze.cmdline}

  :repositories [["releases" {:url "https://clojars.org/repo/"
                              :creds :gpg}]])

