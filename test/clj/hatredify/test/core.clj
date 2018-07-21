(ns hatredify.test.core
  (:use [hatredify.lib.core]
        [kerodon.core]
        [kerodon.test]
        [clojure.test])
  (:require [hatredify.db.core :refer [*db*] :as db]
            [luminus-migrations.core :as migrations]
            [hatredify.config :refer [env]]
            [mount.core :as mount]
            [hatredify.routes.home :refer [app]]))

;; Reserve for database setup
(comment
  (use-fixtures
    :once
    (fn [f]
      (mount/start
       #'hatredify.config/env
       #'hatredify.db.core/*db*)
      (migrations/migrate ["reset"] (select-keys env [:database-url]))
      (migrations/migrate ["migrate"] (select-keys env [:database-url]))
      (def g-id ((keyword "scope_identity()") (db/insert-word! {:word "good"})))
      (def w-id ((keyword "scope_identity()") (db/insert-word! {:word "warm"})))
      (db/insert-antonym! {:word_id g-id :antonym "awful"})
      (db/insert-antonym! {:word_id w-id :antonym "cold"})
      (f))))

(deftest integration-fill-in-textarea-and-press-the-button
  (-> (session app)
     (visit "/")
     (has (status? 200) "page is found")
     (fill-in :#text-chunk "It is a good day, sir! It's so warm outside!")
     (has (some-text? "It is a good day, sir! It's so warm outside!")
          "text-chunk is filled in")
     (press "HATREDIFY")
     (within [:#hatredified-chunk]
             (has (some-regex?
                   #"It is an? \b[A-Z]+\b day, sir! It's so \b[A-Z]+\b outside!")
                  "hatredified-chunk is present"))))
