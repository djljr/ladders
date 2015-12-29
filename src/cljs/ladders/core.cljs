(ns ladders.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [cljs.pprint :as pprint]))

(defn menu [selected]
  (let [menu-options [["/" "Home"]
                      ["/users" "Users"]
                      ["/result" "Add Challenge"]]]
    [:ul {:class "nav nav-pills"}
     (map (fn [[url option-name]]
            (let [class (if (= selected option-name) "active" "")
                  li-attrs {:role "presentation" :class class}]
              ^{:key url} [:li li-attrs [:a {:href url} option-name]]))
          menu-options)]))

(defn current-page []
  [:div [(session/get :current-page)]])

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
   [menu "Home"]
   [:h2 "Current Rankings"]
   [:h3 "Week 1"]
   [rankings-component rankings]])

(defn add-user-page []
  [:div
   [menu "Users"]
   [:h2 "Player Information"]
   [:form
    [:fieldset {:class "form-group"}
     [:label "Username"]
     [:input {:type "text" :class "form-control"}]]
    [:fieldset {:class "form-group"}
     [:label "Name"]
     [:input {:type "text" :class "form-control"}]]
    [:button {:type "submit" :class "btn btn-primary pull-right"} "Add User"]]])

(defn add-challenge-result-page []
  [:div
   [menu "Add Challenge"]
   [:h2 "Add Challenge"]
   [:form
    [:fieldset {:class "form-group"}
     [:label "Player 1"]
     [:select {:class "form-control"}
      [:option "Jeff"]
      [:option "Dennis"]]]
    [:h3 "VS"]
    [:fieldset {:class "form-group"}
     [:label "Player 2"]
     [:select {:class "form-control"}
      [:option "Jeff"]
      [:option "Dennis"]]]
    [:fieldset {:class "form-group"}
     [:label "Winner"]
     [:div {:class "radio"}
      [:label
       [:input {:type "radio" :name "winner"} "Player 1"]]]
     [:div {:class "radio"}
      [:label
       [:input {:type "radio" :name "winner"} "Player 2"]]]]
    [:button {:type "submit" :class "btn btn-primary pull-right"} "Update"]]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/users" []
  (session/put! :current-page #'add-user-page))

(secretary/defroute "/result" []
  (session/put! :current-page #'add-challenge-result-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
