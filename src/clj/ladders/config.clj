(ns ladders.config
  (:require [clojure.java.io :refer [resource]]))

(def config (-> "config.edn" resource slurp read-string))
