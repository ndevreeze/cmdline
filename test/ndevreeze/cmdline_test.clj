(ns ndevreeze.cmdline-test
  (:require [midje.sweet :as midje]
            [ndevreeze.cmdline :refer :all]))

(def tmp-cfg "/tmp/test-config.edn")

(defn create-tmp-config
  "Create temporary config file for testing"
  []
  (spit tmp-cfg "{:file \"file-in-config\"}"))

(def cli-options
  [["-c" "--config CONFIG" "Config file"
    :default "/tmp/test-config.edn"]
   ["-f" "--file FILE" "File to test"
    :default "default-file"]
   ["-h" "--help" "Show this help"]])

(def cli-options-no-config
  [["-f" "--file FILE" "File to test"
    :default "default-file"]
   ["-h" "--help" "Show this help"]])

(midje/facts
 "test parse-opts"

 (midje/fact "Config can be read"
             (do (create-tmp-config)
                 (dissoc (parse-opts [] cli-options) :summary))
             => {:arguments []
                 :errors nil
                 :options {:config "/tmp/test-config.edn"
                           :file "file-in-config"
                           :msg "config found: /tmp/test-config.edn"}})

 (midje/fact "Cmdline takes presedence"
             (do (create-tmp-config)
                 (dissoc (parse-opts ["--file" "file-in-cmdline"] cli-options) :summary))
             => {:arguments []
                 :errors nil
                 :options {:config "/tmp/test-config.edn"
                           :file "file-in-cmdline"
                           :msg "config found: /tmp/test-config.edn"}})

 (midje/fact "Message when config does not exist"
             (dissoc
              (parse-opts ["--config" "/tmp/no-exist" "--file" "file-in-cmdline"] cli-options)
              :summary)
             => {:arguments []
                 :errors nil
                 :msg "config path given but does not exist: /tmp/no-exist"
                 :options {:config "/tmp/no-exist" :file "file-in-cmdline"}})

 (midje/fact "Message when config not given"
             (dissoc
              (parse-opts ["--file" "file-in-cmdline"] cli-options-no-config)
              :summary)
             => {:arguments []
                 :errors nil
                 :msg "config option not given"
                 :options {:file "file-in-cmdline"}})

 (midje/fact "Use default when both config and cmdline not given"
             (dissoc
              (parse-opts [] cli-options-no-config)
              :summary)
             => {:arguments []
                 :errors nil
                 :msg "config option not given"
                 :options {:file "default-file"}}))


 
