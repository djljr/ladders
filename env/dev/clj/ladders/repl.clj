(ns ladders.repl
  (:require [datomic.api :as d]
            [ladders.db :as db])
  (:use ladders.handler
        ring.server.standalone
        [ring.middleware file-info file]))

(defonce server (atom nil))

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

(defn transact-all
  "Run all transactions from coll"
  [conn coll]
  (loop [n 0
         [tx & more] coll]
    (if tx
      (recur (+ n (count (:tx-data  @(d/transact conn tx))))
             more)
      {:datoms n})))

;; holding area for useful forms to send to the repl
(comment
  (def uri "datomic:free://localhost:4334/ladders")
  (def conn (d/connect uri))
  (def player {:db/id (d/tempid :db.part/user)
               :player/email "dennis@example.com"
               :player/firstName "Dennis"
               :player/lastName "Lipovsky"})
  (d/transact conn player)
  (def db (d/db conn))
  (d/pull db '[*] [:player/email "dennis@example.com"])
  )
