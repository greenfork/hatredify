(ns hatredify.lib.core
  (:require [hatredify.db.core :as db]))

(defn split-to-words
  "Splits the text to the collection of words."
  [s]
  (clojure.string/split s #"\b"))

(defn- db-words-and-antonyms
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
  "Replaces words `coll` with antonyms from `m`, upper-cases them."
  [m coll]
  (let [antonym-list m]
    (map #(if-let [antonyms (get antonym-list %)]
            (clojure.string/upper-case (rand-nth (seq antonyms)))
            %)
         coll)))

(defn change-articles
  "Changes 'a' article to 'an' and in reverse where necessary."
  [s]
  (-> s
     (clojure.string/replace #"a ([AEIO])" "an $1")
     (clojure.string/replace #"an ([QWRTYUPSDFGHJKLZXCVBNM])" "a $1")))

(defn hatredify-text
  "Finds all positive adjectives, replaces with antonyms, makes uppercase."
  [s]
  (->> s
     (split-to-words)
     (replace-with-antonyms (db-words-and-antonyms))
     (apply str)
     (change-articles)))
