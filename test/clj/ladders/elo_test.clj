(ns ladders.elo-test
  (:require [clojure.test :refer :all])
  (:require [ladders.elo :refer :all]))

(deftest expected-math-is-accurateb
  (let [[expected-a expected-b] (expected 1450 1550)]
    (testing "expected outcomes are calculated correctly"
      (is (= 0.3599350001971149 expected-a))
      (is (= 0.6400649998028851 expected-b)))))

(deftest expected-is-symmetrical
  (is (= 1.0 (apply + (expected 1400 1500)))))

(deftest next-rating-math-is-accurate
  (is (= 1506 (next-rating 1500 24 0.75 1))))

(deftest next-ratings-returns-expected-results-with-expected-outcome
  (let [[next-a next-b] (next-ratings 1550 1450)]
    (testing "each new rating is as expected"
      (is (= 1559 next-a))
      (is (= 1441 next-b)))))

(deftest next-ratings-returns-expected-results-with-upset
  (let [[next-a next-b] (next-ratings 1450 1550)]
    (testing "each new rating is as expected"
      (is (= 1465 next-a))
      (is (= 1535 next-b)))))
