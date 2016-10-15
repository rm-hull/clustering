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

(ns clustering.core.hierarchical-test
  (:require
   [clojure.test :refer :all]
   [clustering.core.hierarchical :refer :all]
   [clustering.average.simple :refer :all]
   [clustering.distance.euclidean :refer :all]))

(def test-data
  (map vector [-1 7 98 22 14 19 21 3]))

(deftest check-find-closest
  (is (= [] (find-closest distance nil)))
  (is (= [1.0 [[22] [21]]] (find-closest distance test-data))))

(deftest check-cluster-and-walk
  (let [collector (atom [])
        visitor (fn [clust lvl]
                  (swap! collector conj
                         [lvl (or (:branch? clust) (:data clust))]))]
    (->>
     (cluster distance average test-data)
     (prefix-walk visitor))

    (is (= [[0 true]
            [1 true]
            [2 true]
            [3 true]
            [4 [-1]]
            [4 [3]]
            [3 [7]]
            [2 true]
            [3 true]
            [4 true]
            [5 [22]]
            [5 [21]]
            [4 [19]]
            [3 [14]]
            [1 [98]]]
           @collector))))
