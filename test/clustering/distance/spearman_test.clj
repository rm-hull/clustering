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

(ns clustering.distance.spearman-test
  (:require
    [clojure.test :refer :all]
    [clustering.distance.spearman :refer :all]
    [clustering.distance.common :refer [transpose]]
    ))

(def data-set
  ; IQ vs. Hours of TV per week
  ; data taken from: https://en.wikipedia.org/wiki/Spearman%27s_rank_correlation_coefficient#Example
  [[106 7]
   [86  0]
   [100 27]
   [101 50]
   [99  28]
   [103 29]
   [97  20]
   [113 12]
   [112 6]
   [110 17]])

(deftest check-correlation-coefficient
  (is (zero? (correlation-coefficient nil nil)))
  (is (= -29/165
         (apply correlation-coefficient (transpose data-set)))))
