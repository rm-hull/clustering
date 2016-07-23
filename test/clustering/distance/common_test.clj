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

(ns clustering.distance.common-test
  (:require
    [clojure.test :refer :all]
    [clustering.distance.common :refer :all]))

(deftest check-sum
  (is (= 0 (sum nil)))
  (is (= 15 (sum [1 2 3 4 5]))))

(deftest check-diff
  (is (= 0 (diff 2 2)))
  (is (= 0.5 (diff 2.5 2)))
  (is (= 5/4 (diff 3/4 2)))
  (is (= 1/4 (diff 1 3/4))))

(deftest check-sqr
  (is (= 1 (sqr -1)))
  (is (= 0 (sqr 0)))
  (is (= 9.869587728099999 (sqr 3.14159))))

(deftest check-sum-product
   (is (= 0 (sum-product nil nil)))
   (is (= 2 (sum-product [1] [2 3])))
   (is (= 14 (sum-product [1 4] [2 3]))))

(deftest check-sum-squares
   (is (= 0 (sum-squares nil)))
   (is (= 13 (sum-squares [2 3])))
   (is (= 55 (sum-squares [1 2 3 4 5]))))
