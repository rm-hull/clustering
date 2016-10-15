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

(ns clustering.distance.pearson-test
  (:require
   [clojure.test :refer :all]
   [clustering.distance.pearson :refer :all]
   [clustering.distance.common :refer [transpose]]))

(def data-set
  ; Age vs. Glucose level
  ; data taken from: http://www.statisticshowto.com/how-to-compute-pearsons-correlation-coefficients/
  [[43 99]
   [21 65]
   [25 79]
   [42 75]
   [57 87]
   [59 81]])

(deftest check-correlation-coefficient
  (is (zero? (correlation-coefficient nil nil)))
  (is (zero? (correlation-coefficient [2 2] [2 2])))
  (is (= 1.0 (correlation-coefficient [1 2] [1 2])))
  (is (= 1.0 (correlation-coefficient [1 2] [2 4])))
  (is (= -0.5 (correlation-coefficient [2 2 3] [2 3 2])))
  (is (= 0.5298089018901744
         (apply correlation-coefficient (transpose data-set)))))

(deftest check-distance
  (is (= 1.0 (distance nil nil)))
  (is (= 1.0 (distance [2 2] [2 2])))
  (is (zero? (distance [1 2] [1 2])))
  (is (zero? (distance [1 2] [2 4])))
  (is (= 1.5 (distance [2 2 3] [2 3 2])))
  (is (= 0.4701910981098256
         (apply distance (transpose data-set)))))

