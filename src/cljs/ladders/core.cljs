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
  [:div
   [:div "Home | " [:a {:href "/users"} "Users"]]
   [:h2 "Current Rankings"]
   [:h3 "Week 1"]
   [rankings-component rankings]])

(defn add-user-page []
  [:div
   [:div [:a {:href "/"} "Home"] " | Users"]
   [:h2 "Player Information"]
   [:form
    [:fieldset {:class "form-group"}
     [:label "Username"]
     [:input {:type "text" :class "form-control"}]]
    [:fieldset {:class "form-group"}
     [:label "Name"]
     [:input {:type "text" :class "form-control"}]]
    [:button {:type "submit" :class "btn btn-primary pull-right"} "Add User"]]])

(defn current-page []
  [:div [(session/get :current-page)]])


;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/users" []
  (session/put! :current-page #'add-user-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
