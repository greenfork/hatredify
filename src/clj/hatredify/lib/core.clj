(ns hatredify.lib.core
  (:require [hatredify.db.core :as db]))

(defn split-to-words
  "Splits the text to the collection of words."
  [s]
  (clojure.string/split s #"\b"))

(defn list-words-and-antonyms
  "Returns a map of words with their antonyms from database."
  []
  (loop [raw-map (db/get-thesaurus)
         rs {}]
    (let [f (first raw-map)
          k (:word f)
          v (:antonym f)]
      (if (seq raw-map)
        (recur (rest raw-map)
               (merge-with #(clojure.set/union %1 %2) rs {k #{v}}))
        rs))))

(defn replace-with-antonyms
  "Replaces words with upper-cased antonyms."
  [coll]
  (let [antonym-list (list-words-and-antonyms)]
    (map #(if-let [antonyms (get antonym-list %)]
            (clojure.string/upper-case (rand-nth (seq antonyms)))
            %)
         coll)))

(defn hatredify-text
  "Finds all positive adjectives, replace with antonyms, make uppercase."
  [s]
  (->> s
     (split-to-words)
     (replace-with-antonyms)
     (apply str)))
