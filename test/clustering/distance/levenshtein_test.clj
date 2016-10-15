;; The MIT License (MIT)
;;
;; Copyright (c) 2016 Richard Hull
;;
;; Permission is hereby granted, free of charge, to any person obtaining a copy
;; of this software and associated documentation files (the "Software"), to deal
;; in the Software without restriction, including without limitation the rights
;; to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
;; copies of the Software, and to permit persons to whom the Software is
;; furnished to do so, subject to the following conditions:
;;
;; The above copyright notice and this permission notice shall be included in all
;; copies or substantial portions of the Software.
;;
;; THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
;; IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
;; FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
;; AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
;; LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
;; OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
;; SOFTWARE.

(ns clustering.distance.levenshtein-test
  (:require
   [clojure.test :refer :all]
   [clustering.distance.levenshtein :refer :all]))

(deftest check-distance
  (is (= 0 (distance nil nil)))
  (is (= 5 (distance nil "hello")))
  (is (= 0 (distance "hello" "hello")))
  (is (= 1 (distance "hello" "hullo")))
  (is (= 1 (distance "hello" "hell")))
  (is (= 1 (distance "hello" "ello")))
  (is (= 4 (distance "hello" "world")))
  (is (= 4 (distance "world" "hello")))
  (is (= 5 (distance "helpo" "world"))))
