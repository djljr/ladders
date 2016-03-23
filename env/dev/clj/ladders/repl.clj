(ns ladders.repl
  (:require [datomic.api :as d]
            [ladders.db :as db]
            [clojure.java.io :as io])
  (:use ladders.handler
        ring.server.standalone
        [ring.middleware file-info file]))

(defonce server (atom nil))

(defn transact [conn datoms]
  (for [tx-data datoms]
    @(d/transact conn tx-data)))

(def schema (-> "schema.edn" io/resource slurp read-string))

(def test-data
  [{:db/id (d/tempid :db.part/user)
    :player/email "dennis@example.com"
    :player/firstName "Dennis"
    :player/lastName "Lipovsky"}])

(defn create-database []
  (d/delete-database db/uri)
  (d/create-database db/uri)
  (db/connect)
  (transact @db/conn (concat schema test-data)))

(defn get-handler []
  ;; #'app expands to (var app) so that when we reload our code,
  ;; the server is forced to re-resolve the symbol in the var
  ;; rather than having its own copy. When the root binding
  ;; changes, the server picks it up without having to restart.
  (-> #'app
      ; Makes static assets in $PROJECT_DIR/resources/public/ available.
      (wrap-file "resources")
      ; Content-Type, Content-Length, and Last Modified headers for files in body
      (wrap-file-info)))

(defn start-server
  "used for starting the server in development mode from REPL"
  [& [port]]
  (let [port (if port (Integer/parseInt port) 3000)]
    (reset! server
            (serve (get-handler)
                   {:port port
                    :auto-reload? true
                    :join? false}))
    (println (str "You can view the site at http://localhost:" port))))

(defn stop-server []
  (.stop @server)
  (reset! server nil))

;; holding area for useful forms to send to the repl
(comment
  (def uri "datomic:free://localhost:4334/ladders")

  (def uri "datomic:mem://ladders")
  (d/create-database uri)
  (def conn (d/connect uri))

  (def player {:db/id (d/tempid :db.part/user)
               :player/email "dennis@example.com"
               :player/firstName "Dennis"
               :player/lastName "Lipovsky"})

  (d/transact conn [player])

  (def db (d/db conn))
  (d/pull db '[*] [:player/email "dennis@example.com"])
  (def db-player (d/q '[:find ?player . ;. means there is exactly 1
                        :where
                        [?player :player/firstName "Dennis"]]
                      db))
  (d/pull db '[:player/firstName :player/lastName :player/email] db-player)
  (d/q '[:find (pull ?player [:player/firstName :player/lastName :player/email]) .
         :where
         [?player :player/firstName "Dennis"]]
       db)
  )
