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

(ns clustering.data-viz.dendrogram-test
  (:require
    [clojure.test :refer :all]
    [clojure.java.io :as io]
    [digest]
    [clustering.core.hierarchical :refer :all]
    [clustering.data-viz.dendrogram :refer :all]
    [clustering.data-viz.image :refer :all]))

(def test-data
  (bi-cluster
    :root
    [(bi-cluster :l1
       [(bi-cluster :ll2)
        (bi-cluster :lr2)] 10)
     (bi-cluster :r1
       [(bi-cluster :rl2)
        (bi-cluster :rr2)] 9)] 15))

(deftest check-height
  (is (= 4 (height test-data)))
  (is (= 1 (height (bi-cluster :leaf))))
  (is (= 0 (height nil)))
  (is (= 0 (height [])))
  (is (= 0 (height {}))))

(deftest check-depth
  (is (= 25 (depth test-data)))
  (is (= 0 (depth nil)))
  (is (= 0 (depth [])))
  (is (= 0 (depth {}))))

(deftest check-calc-bounds
  (is (= {:h 80 :w 1024 :scaling 34.96} (calc-bounds test-data)))
  (is (= {:h 0 :w 1024 :scaling 0} (calc-bounds nil))))

(deftest check-rounding
  (is (= 3.0   ((round 0) 3.14159)))
  (is (= 3.1   ((round 1) 3.14159)))
  (is (= 3.14  ((round 2) 3.14159)))
  (is (= 3.142 ((round 3) 3.14159))))

(deftest check-polyline
  (is (= [:polyline
          {:points "1.23,4.57,7.89,10.11"
           :style "some-styles"}]
         (polyline "some-styles", [1.23456, 4.56789, 7.890123, 10.111213]))))

(deftest check->svg
  (is (= (slurp "test/data/dendrogram.svg") (->svg test-data))))

(deftest check->img
  (let [tmp-file "test/data/__tmp.png"]
    (try
      (write-png tmp-file (->img test-data))
      (is (= (digest/md5 (io/as-file tmp-file))
             (digest/md5 (io/as-file "test/data/dendrogram.png"))))
    (finally
      (io/delete-file tmp-file)))))


