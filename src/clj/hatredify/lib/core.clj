(ns hatredify.lib.core)

(defn split-to-words
  "Splits the text to the collection of words."
  [s]
  (clojure.string/split s #"\b"))

(defn list-words-and-antonyms
  "Returns a map of words with their antonyms."
  []
  {
   "good" ["bad" "awful"]
   "warm" ["cold"]
   })

(defn replace-with-antonyms
  "Replaces words with antonyms."
  [coll]
  (let [antonym-list (list-words-and-antonyms)]
    (map #(if-let [antonyms (get antonym-list %)]
            (clojure.string/upper-case (rand-nth antonyms))
            %) coll)))

(defn hatredify-text
  "Finds all positive adjectives, replace with antonyms, make uppercase."
  [s]
  (->> s
     (split-to-words)
     (replace-with-antonyms)
     (apply str)))
