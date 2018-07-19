(ns hatredify.lib.core
  (:require [hatredify.db.functions :as dbf]))

(defn split-to-tokens
  "Splits `s` to the collection of tokens, not necessarily words."
  [s]
  (clojure.string/split s #"\b"))

(defn replace-with-antonyms
  "Replaces each word in `coll` with antonym from `m`, if present.
  Additionally upper-cases them. `m` maps each word to the set of its antonyms."
  [m coll]
  (map #(if-let [antonyms (m (clojure.string/lower-case %))]
          (clojure.string/upper-case (rand-nth (seq antonyms)))
          %)
       coll))

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
     (split-to-tokens)
     (replace-with-antonyms (dbf/words-and-antonyms))
     (apply str)
     (change-articles)))
