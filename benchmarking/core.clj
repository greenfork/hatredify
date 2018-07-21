;;;; Benchmarking

;;; This file aims to compare different implementations in an objective manner.
;;; Each benchmark will have a short description in comments and during the
;;; running phase in the stdout.

(ns hatredify.benchmarking.core
  (:require [criterium.core :refer [bench]]
            [hatredify.lib.wordnet :as wd]))

;;; Test `array-map` vs `hash-map` implementation for
;;; hatredify.lib.wordnet/form-dictionary-map.
;;; Test goes as follows:
;;;
;;; 1. Load positive words, which are used for dictionary generation.
;;; 2. Generate dictionary using the specified function.
;;; 3. Look up 150 words in 2k+ dictionary map (same words from the
;;;    1st step are used as a lookup list).

;; Initial implementation is with `hash-map`
(println (str "\nTesting `hash-map` implementation of"
              " hatredify.lib.wordnet/form-dictionary-map ..."))
(bench (-> wd/positive-words
           #'wd/form-dictionary-map
           (map wd/positive-words)))

;; Evaluation count : 4342928640 in 60 samples of 72382144 calls.
;; Execution time mean : 11.958204 ns
;; Execution time std-deviation : 0.274132 ns
;; Execution time lower quantile : 11.259084 ns ( 2.5%)
;; Execution time upper quantile : 12.319270 ns (97.5%)
;; Overhead used : 2.150824 ns

;; Found 2 outliers in 60 samples (3.3333 %)
;; low-severe	 2 (3.3333 %)
;; Variance from outliers : 10.9922 % Variance is moderately inflated by outliers


;; Change it to `array-map`
(println (str "\nTesting `array-map` implementation of"
              " hatredify.lib.wordnet/form-dictionary-map ..."))
(bench (with-redefs [clojure.core/hash-map clojure.core/array-map]
         (-> wd/positive-words
             #'wd/form-dictionary-map
             (map wd/positive-words))))

;; Evaluation count : 47801520 in 60 samples of 796692 calls.
;; Execution time mean : 1.306409 µs
;; Execution time std-deviation : 28.908909 ns
;; Execution time lower quantile : 1.261108 µs ( 2.5%)
;; Execution time upper quantile : 1.370636 µs (97.5%)
;; Overhead used : 2.150824 ns

;; Found 4 outliers in 60 samples (6.6667 %)
;; low-severe	 3 (5.0000 %)
;; low-mild	 1 (1.6667 %)
;; Variance from outliers : 10.9428 % Variance is moderately inflated by outliers

;;; Conclusion
;;; Mean time execution of `hash-map` is 109 faster than `array-map`
;;; (notice the difference in measurements: ns vs µs).
