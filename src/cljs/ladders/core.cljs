(ns ladders.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]))

(def rankings
  [{:player "Jeff" :stats {:wins 10 :losses 15}}
   {:player "Dennis" :stats {:wins 9 :losses 25}}])

(defn ranking-row [rank player]
  ^{:key (:player player)}
  [:tr
   [:td (inc rank)]
   [:td (:player player)]
   [:td (-> player :stats :wins)]
   [:td (-> player :stats :losses)]])

(defn rankings-component [rankings]
  [:table {:class "table table-hover"}
   [:tr
    [:th "Ranking"]
    [:th "Player"]
    [:th "Wins"]
    [:th "Losses"]]
   (map-indexed ranking-row rankings)])

(defn home-page []
  [:div [:h2 "Current Rankings"]
        [:h3 "Week 1"]
   [rankings-component rankings]])

(defn about-page []
  [:div [:h2 "About ladders"]
   [:div [:a {:href "/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])


;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
