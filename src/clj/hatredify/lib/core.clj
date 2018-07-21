(ns hatredify.lib.core)

(defn split-to-tokens
  "Splits `test` to the collection of tokens, not necessarily words."
  [text]
  (clojure.string/split text #"\b"))

(defn replace-with-antonyms
  "Replaces each word in `tokens` with antonym from `dict`, if present.
  Additionally upper-cases them. `dict` maps each word to the set of its
  antonyms."
  [dict tokens]
  (map #(if-let [antonyms (dict (clojure.string/lower-case %))]
          (clojure.string/upper-case (rand-nth (seq antonyms)))
          %)
       tokens))

(defn change-articles
  "Changes 'a' article to 'an' and in reverse where necessary."
  [text]
  (-> text
      (clojure.string/replace #"a ([AEIOU])" "an $1")
      (clojure.string/replace #"an ([QWRTYPSDFGHJKLZXCVBNM])" "a $1")))

(defn hatredify-text
  "Finds all positive adjectives, replaces with antonyms, makes uppercase."
  [dict text]
  (->> text
       (split-to-tokens)
       (replace-with-antonyms dict)
       (apply str)
       (change-articles)))
