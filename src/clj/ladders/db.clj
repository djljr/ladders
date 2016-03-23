(ns ladders.db
  (:require [datomic.api :as d]
            [ladders.config :as conf]))

(def uri (:datomic-uri conf/config))

(defonce conn (atom nil))

(defn create []
  (d/create-database uri))

(defn connect []
  (reset! conn (d/connect uri)))

(defn transact [data]
  (if-not (nil? @conn)
    (d/transact @conn data)))
