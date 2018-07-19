(ns hatredify.db.functions
  (:require [hatredify.db.core :as db]))

(defn words-and-antonyms
  "Returns a map of words with their antonyms from database."
  []
  (loop [raw-map (db/get-thesaurus)
         rs {}]
    (let [f (first raw-map)
          k (:word f)
          v (:antonym f)]
      (if (seq raw-map)
        (recur (rest raw-map)
               (merge-with clojure.set/union rs {k #{v}}))
        rs))))
