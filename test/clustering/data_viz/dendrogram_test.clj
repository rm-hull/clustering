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
   [clustering.core.hierarchical :refer :all]
   [clustering.data-viz.dendrogram :refer :all]
   [clustering.data-viz.image :refer :all])
  (:import
   [java.awt.image BufferedImage]
   [javax.imageio ImageIO]))

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

(deftest check->svg
  (is (= (slurp "test/data/dendrogram.svg") (->svg test-data))))

(deftest check->img
  (let [tmp-file "test/data/__tmp.png"]
    (try
      ;; Due to platform differences in how fonts are rendered, there is not
      ;; much more than we can do to check the image loads ok, and that the
      ;; size is as expected
      (write-png tmp-file (->img test-data))
      (let [^BufferedImage img (ImageIO/read (io/file tmp-file))]
        (is (= 1024 (.getWidth img)))
        (is (= 80 (.getHeight img))))
      (finally
        (io/delete-file tmp-file)))))

