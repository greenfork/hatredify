(ns hatredify.test.lib.core
  (:require [hatredify.lib.core :refer :all]
            [clojure.test :refer :all]))

(deftest main-logic
  (let [s1 "It is a good day, sir!"
        coll1 ["It" " " "is" " " "a" " " "good" " " "day" ", " "sir" "!"]
        rcoll1 ["It" " " "is" " " "a" " " "AWFUL" " " "day" ", " "sir" "!"]
        rs1 "It is a AWFUL day, sir!"
        rs1-articles "It is an AWFUL day, sir!"
        s2 "It's so warm outside!"
        coll2 ["It" "'" "s" " " "so" " " "warm" " " "outside" "!"]
        rcoll2 ["It" "'" "s" " " "so" " " "COLD" " " "outside" "!"]
        rs2 "It's so COLD outside!"
        antonyms {"good" #{"awful"} "warm" #{"cold"}}]
    (testing "splits s to tokens"
      (is (= (split-to-tokens s1) coll1))
      (is (= (split-to-tokens s2) coll2)))
    (testing "replaces words with antonyms"
      (let [f (partial replace-with-antonyms antonyms)]
        (is (= (f coll1) rcoll1))
        (is (= (f coll2) rcoll2))))
    (testing "changes to proper articles"
      (is (=  (change-articles rs1) rs1-articles)))
    (testing "whole pipeline"
      (with-redefs [hatredify.db.functions/words-and-antonyms (fn [] antonyms)]
        (is (= (hatredify-text s1) rs1-articles))
        (is (= (hatredify-text s2) rs2))))))
