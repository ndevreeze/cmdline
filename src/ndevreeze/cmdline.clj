(ns ndevreeze.cmdline
  (:require [clojure.tools.cli :as cli]
            [me.raynes.fs :as fs]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.edn :as edn]))

(defn normalized
  "Just replace / characters with system specific path separator.
   For use with (fs/expand-home)
   fs/normalized adds the current working dir (cwd), which gives a chicken/egg problem"
  [s]
  (str/replace s "/" java.io.File/separator))

(defn expand-home
  "My version of fs/expand-home, using normalized in this namespace.
   Make sure we return a File object"
  [path]
  (if path
    (fs/file (fs/expand-home (normalized path)))))

;; 2020-06-07: Maybe climatic or similar makes this one obsolete?
;; 2020-07-01: would like to call parse-fn also on config-opts, but
;; don't know how. Normally for int's etc not needed, but can be
;; useful for expand-home
(defn parse-opts
  "Wrapper around cli/parse-opts that reads config file.
   Handles default parameters like this:
   1. explicitly on cmdline
   2. or else given in config file
   3. or else use default value"
  [args cli-options]
  (let [opts    (cli/parse-opts args cli-options)
        opts-nd (cli/parse-opts args cli-options :no-defaults true)]
    (if-let [config-file (:config (:options opts))]
      (let [config-path (expand-home config-file)]
        (if (fs/exists? config-path)
          (let [cfg-opt (edn/read-string (slurp config-path))]
            (merge opts
                   {:options
                    (merge (:options opts)    ;; start with options including defaults
                           cfg-opt            ;; override with values in config
                           (:options opts-nd) ;; override with explicit cmdline args
                           {:msg (format "config found: %s" config-path)})}))
          (assoc opts :msg (format "config path given but does not exist: %s" config-path))))
      (assoc opts :msg "config option not given"))))

(defn check-and-exec
  "Check command-line arguments and if ok execute the given function.
   System/exit when the function is done, wrt hanging child-processes.
   The function should expect 2 params: the :options and :arguments parts of parsed options.
   Params:
   description - one line description of tool
   cli-options - vector of options according to tools.cli
   script      - the function to execute
   args        - arguments given on command-line, as a sequence
   ctx         - context, a map. Could contain :script and :cwd"
  ([description cli-options function args ctx]
   (let [opts (parse-opts args cli-options)]
     (if (or (:help (:options opts)) (:errors opts))
       (println (str description "\n"
                     "Command line arguments:\n"
                     (:summary opts)
                     "\n"
                     (str/join "; " (:errors opts))
                     "\n"
                     "<script> [options] <param> ..."
                     "\n"
                     "Current options: " (dissoc opts :summary)
                     "\n"
                     "Default Config location expanded: " 
                     (expand-home (:config (:options opts)))))
       (function (:options opts) (:arguments opts) ctx))))
  ([description cli-options function args]
   (check-and-exec description cli-options function args {})))
