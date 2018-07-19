-- :name get-thesaurus :? :*
-- :doc Loads all thesaurus into memory.
SELECT w.word, an.antonym
FROM words w
JOIN antonyms an ON w.id = an.word_id

-- :name insert-word! :i! :raw
-- :doc Inserts new word to find for replacement.
INSERT INTO words (word) VALUES (:word)

-- :name insert-antonym! :i! :raw
-- :doc Inserts antonyms to corresponding words.
INSERT INTO antonyms (word_id, antonym) VALUES (:word_id, :antonym)

-- :name delete-word! :! :n
-- :doc Deletes a word by name. WARNING: also deletes all referenced antonyms.
DELETE FROM words WHERE word = :word

-- :name delete-antonym! :! :n
-- :doc Deletes an antonym by name from the set referencing given word.
DELETE FROM antonyms WHERE antonym = :antonym
AND word_id = (SELECT id FROM words WHERE word = :word)
