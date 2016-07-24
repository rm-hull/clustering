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

(ns clustering.core.qt-test
  (:require
    [clojure.test :refer :all]
    [clustering.core.qt :refer :all]
    [clustering.test-helper :refer :all]
    [clj-time.core :refer [date-time]]))

(deftest check-candidate-cluster
  (is (empty? (candidate-cluster distance (date-time 2013 7 15) test-dataset 0)))
  (is (empty? (candidate-cluster distance (date-time 2013 7 15) nil 150)))
  (is (= (candidate-cluster distance (date-time 2013 7 15) test-dataset 1)
         (hash-set (date-time 2013 7 14))))
  (is (= (candidate-cluster distance (date-time 2013 7 15) test-dataset 15)
         (hash-set
           (date-time 2013 7 1)
           (date-time 2013 7 14)
           (date-time 2013 7 21)
           (date-time 2013 7 25))))
  (is (= (candidate-cluster distance (date-time 2013 7 15) test-dataset 31)
         (hash-set
           (date-time 2013 7 1)
           (date-time 2013 7 14)
           (date-time 2013 7 21)
           (date-time 2013 7 25)
           (date-time 2013 7 31)
           (date-time 2013 8 3)))))

(deftest check-most-candidates
  (is (empty? (most-candidates distance nil 31)))
  (is (= (most-candidates distance test-dataset 31)
         (hash-set
           (date-time 2013 7 1)
           (date-time 2013 7 14)
           (date-time 2013 7 21)
           (date-time 2013 7 25)
           (date-time 2013 7 31)
           (date-time 2013 8 3)))))

(deftest check-cluster
  (is (empty? (cluster distance nil 31 3)))
  (is (empty? (cluster distance [(date-time 2016 11 3)] 31 3)))
  (is (= [(hash-set (date-time 2016 11 3))]
         (cluster distance [(date-time 2016 11 3)] 31 1)))
  (is (= [(hash-set
            (date-time 2013 7 1)
            (date-time 2013 7 14)
            (date-time 2013 7 21)
            (date-time 2013 7 25)
            (date-time 2013 7 31)
            (date-time 2013 8 3))
          (hash-set
            (date-time 2012 5 28)
            (date-time 2012 6 2)
            (date-time 2012 6 6)
            (date-time 2012 6 7)
            (date-time 2012 6 9))
          (hash-set
            (date-time 2012 12 26)
            (date-time 2012 12 28)
            (date-time 2013 1  16))]
         (cluster distance test-dataset 31 3))))
