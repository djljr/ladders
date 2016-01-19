(ns ladders.db
  (:require [datomic.api :as d]
            [ladders.config :as conf]))

(def uri (:datomic-uri conf/config))

(defonce conn (atom nil))

(defn connect []
  (reset! conn (d/connect uri)))

(defn transact [data]
  (if-not (nil? @conn)
    (d/transact @conn data)))

(def schema [[ ;; player
              {:db/id #db/id[:db.part/db]
               :db/ident :player/firstName
               :db/index true
               :db/valueType :db.type/string
               :db/cardinality :db.cardinality/one
               :db.install/_attribute :db.part/db}
              {:db/id #db/id[:db.part/db]
               :db/ident :player/lastName
               :db/index true
               :db/valueType :db.type/string
               :db/cardinality :db.cardinality/one
               :db.install/_attribute :db.part/db}
              {:db/id #db/id[:db.part/db]
               :db/ident :player/email
               :db/index true
               :db/unique :db.unique/identity
               :db/valueType :db.type/string
               :db/cardinality :db.cardinality/one
               :db.install/_attribute :db.part/db}]
             ;; match
             [{:db/id #db/id[:db.part/db]
               :db/ident :match/game
               :db/valueType :db.type/ref
               :db/cardinality :db.cardinality/one
               :db.install/_attribute :db.part/db}
              {:db/id #db/id[:db.part/db]
               :db/ident :match/time
               :db/valueType :db.type/instant
               :db/cardinality :db.cardinality/one
               :db/index true
               :db.install/_attribute :db.part/db}
              {:db/id #db/id[:db.part/db]
               :db/ident :match/participant
               :db/valueType :db.type/ref
               :db/cardinality :db.cardinality/many
               :db/isComponent true
               :db.install/_attribute :db.part/db}]
             ;; participant
             [{:db/id #db/id[:db.part/db]
               :db/ident :participant/player
               :db/valueType :db.type/ref
               :db/cardinality :db.cardinality/one
               :db.install/_attribute :db.part/db}
              {:db/id #db/id[:db.part/db]
               :db/ident :participant/place
               :db/valueType :db.type/long
               :db/cardinality :db.cardinality/one
               :db/index true
               :db.install/_attribute :db.part/db}]
             ;; game
             [{:db/id #db/id[:db.part/db]
               :db/ident :game/name
               :db/valueType :db.type/string
               :db/cardinality :db.cardinality/one
               :db/index true
               :db.install/_attribute :db.part/db}]])


