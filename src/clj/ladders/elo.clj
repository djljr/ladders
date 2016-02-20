(ns ladders.elo)

;; q-term name taken from formula on wikipedia page for elo-rating
;; https://en.wikipedia.org/wiki/Elo_rating_system#Mathematical_details
(defn q-term [rating] (Math/pow 10 (/ rating 400)))

;; given the ratings of two players, return a vector of the expected outcome
;; for each player, essentially the chance that each player will win
(defn expected [rating-a rating-b]
  (let
      [qa (q-term rating-a)
       qb (q-term rating-b)
       denom (+ qa qb)]
    [(/ qa denom) (/ qb denom)]))

;; player-rating is the player's rating before the match
;; k is the k-factor, which controls impact of new games vs. past performance
;; expected-outcome is between 0 and 1, calculated by calling (expected)
;; actual-outcome is either 0 or 1, 0 indicates loss and 1 indicates a win
(defn next-rating [player-rating k expected-outcome actual-outcome]
  (-> (- actual-outcome expected-outcome)
      (* k)
      (+ player-rating)
      (Math/round)))

;; k-factor determined by picking a middle-of-the-road value from the
;; wikipedia page on elo-rating
;; https://en.wikipedia.org/wiki/Elo_rating_system#Most_accurate_K-factor
(def k-factor 24)

;; given the ratings for the winner and a loser of a game, this function
;; will return a pair of new ratings for the players.  The rating for the
;; winner of the game is the first parameter, followed by the loser's rating
;; result: [new-rating-winner new-rating-loser]
(defn next-ratings [rating-winner rating-loser]
  (let [[exp-winner exp-loser] (expected rating-winner rating-loser)]
    [(next-rating rating-winner k-factor exp-winner 1)
     (next-rating rating-loser k-factor exp-loser 0)]))

(def initial-rating 1500)

;; given a map of player_ids to their ratings, as well as the ids for a winner
;; and a loser, this function will return an updated map with the updated
;; ratings for the players that played.  If a player is not include in the
;; ratings map then the initial-rating will be used as their starting point.
(defn next-round [ratings winner loser]
  (let [winner-before (get ratings winner initial-rating)
        loser-before (get ratings loser initial-rating)]
    (->> (next-ratings winner-before loser-before)
         (zipmap [winner loser])
         (merge ratings))))

;; given a series of game results and initial ratings this function will return
;; a map of player_id to their current rating.  results is a col of the form
;; [[winner loser] [winner loser]...]
(defn many-rounds
  ([results] (many-rounds {} results))
  ([initial-ratings results]
   (reduce
    (fn [ratings result]
      (next-round ratings (first result) (second result)))
    initial-ratings
    results)))