(ns hatredify.test.db.core
  (:require [hatredify.db.core :refer [*db*] :as db]
            [hatredify.db.functions :as dbf]
            [luminus-migrations.core :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [hatredify.config :refer [env]]
            [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'hatredify.config/env
     #'hatredify.db.core/*db*)
    (migrations/migrate ["reset"] (select-keys env [:database-url]))
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest words-and-antonyms
  (jdbc/with-db-transaction [t-conn *db*]
    #_(jdbc/db-set-rollback-only! t-conn)
    (testing "insert a word and return its id"
      (is (= 1 ((keyword "scope_identity()") (db/insert-word!
                                              t-conn
                                              {:word "good"})))))
    (testing "insert an antonym and return its id"
      (is (= 1 ((keyword "scope_identity()") (db/insert-antonym!
                                              t-conn
                                              {:word_id 1 :antonym "bad"})))))
    (testing "insert an antonym and return its id"
      (is (= 2 ((keyword "scope_identity()") (db/insert-antonym!
                                              t-conn
                                              {:word_id 1 :antonym "awful"})))))
    (testing "query word and its antonyms"
      (is (= [{:word "good" :antonym "bad"}
               {:word "good" :antonym "awful"}]
             (db/get-thesaurus t-conn {})))))
  (testing "query and group words and antonyms"
    (is (= {"good" #{"bad" "awful"}}
           (dbf/words-and-antonyms))))
  (testing "delete 1 antonym"
    (is (= 1 (db/delete-antonym! {:antonym "bad" :word "good"})))
    (is (= [{:word "good" :antonym "awful"}] (db/get-thesaurus))))
  (testing "delete a word and an associated antonym"
    (is (= 1 (db/delete-word! {:word "good"})))
    (is (= [] (db/get-thesaurus)))))
