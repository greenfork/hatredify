(ns hatredify.lib.wordnet
  (:require [clj-wordnet.core :as wd]))

(def wordnet (wd/make-dictionary "resources/data/wordnet_dict_en/"))

(defn- find-antonyms
  "Returns a list of wordnet maps of antonyms related to the given `word-id`."
  [word-id]
  ((comp :antonym wd/lexical-relations wordnet) word-id))

(defn- find-similar-words
  "Returns a list of wordnet maps of similar adjectives to the given `word-id`."
  [word-id]
  (when-let [similar-synsets
             ((comp :similar-to wd/semantic-relations wd/synset wordnet) word-id)]
    (filter #(= :adjective (:pos %))
            (flatten (map wd/words similar-synsets)))))

(defn- extract-all-antonyms
  "Returns a set of adjectives, antonymous to the given `word`."
  [word]
  (when-let [lemmas (wordnet word :adjective)]
    (let [lemma-ids
          (map :id lemmas)
          similar-words-ids
          (map :id (flatten (keep find-similar-words lemma-ids)))]
      (->> (concat lemma-ids similar-words-ids)
           (keep find-antonyms)
           (flatten)
           (map :lemma)
           (into #{})))))

(defn form-dictionary-map
  "Returns a map which maps `word` to the set of its antonyms."
  [word]
  {word (extract-all-antonyms word)})
