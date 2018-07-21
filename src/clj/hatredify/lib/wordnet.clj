(ns hatredify.lib.wordnet
  (:require [clj-wordnet.core :as wd]))

(def wordnet (wd/make-dictionary "resources/data/wordnet_dict_en/"))
(def positive-words (line-seq (clojure.java.io/reader
                               "resources/data/really_positive_adj.txt")))

(defn- find-antonyms
  "Returns a list of wordnet maps of antonyms related to the given `word-id`."
  [word-id]
  ((comp :antonym wd/lexical-relations wordnet) word-id))

(defn- find-similar-words
  "Returns a list of wordnet maps of similar adjectives to the given `word-id`."
  [word-id]
  (let [similar-synsets
        ((comp :similar-to wd/semantic-relations wd/synset wordnet) word-id)]
    (filter #(= :adjective (:pos %))
            (mapcat wd/words similar-synsets))))

(defn- extract-ids
  "Returns a list of wordnet word IDs from a given `word`.
  In wordnet one word can contain different amount of meanings, each has its
  own ID."
  [word]
  (map :id (wordnet word :adjective)))

(defn- extract-antonyms
  "Returns a set of adjectives, antonymous to the given `word`."
  [word]
  (let [word-ids (extract-ids word)
        similar-words-ids
        (map :id (filter some? (mapcat find-similar-words word-ids)))]
    (->> (concat word-ids similar-words-ids)
         (keep find-antonyms)
         (flatten)
         (map :lemma)
         (into #{}))))

(defn- extract-similar-words
  "Returns a set of adjectives, which are similar to the `word`."
  [word]
  (->> (extract-ids word)
       (mapcat find-similar-words)
       (map :lemma)
       (into #{})))

(defn- extract-synonyms
  "Returns a set of adjectives with at least 1 synset in common with `word`."
  [word]
  (->> (wordnet word :adjective)
       (map wd/synset)
       (mapcat wd/words)
       (map :lemma)
       (into #{})))

(defn form-dictionary-map
  "Returns a dictionary which maps words to the set of their antonyms."
  [word-list]
  (let [synonyms #{}]
    (->> (set word-list)
         (clojure.set/union synonyms)
         (reduce #(clojure.set/union %1 (extract-similar-words %2)) #{})
         (clojure.set/union synonyms)
         (reduce #(clojure.set/union %1 (extract-synonyms %2)) #{})
         (clojure.set/union synonyms)
         (map #(list % (extract-antonyms %)))
         (filter (comp not empty? second))
         flatten
         (apply hash-map))))
