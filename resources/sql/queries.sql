-- :name get-thesaurus :? :*
-- :doc loads all thesaurus into memory
SELECT w.word, an.antonym
FROM words w
JOIN antonyms an ON w.id = an.word_id
