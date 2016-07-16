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
    [clojure.test.check :as tc]
    [clojure.test.check.generators :as gen]
    [clojure.test.check.properties :as prop]
    [clojure.test.check.clojure-test :refer [defspec]]
    [clustering.core.hierarchical :refer :all]
    [clustering.data-viz.dendrogram :refer :all]
    [clustering.data-viz.image :refer :all]
    [clustering.test-helper :refer :all]
    [clj-time.core :refer [date-time]]))

(defn print-clusters
  ([clust]
   (print-clusters clust 0))

  ([clust n]
   (dotimes [i n]
     (print " "))

   (if (:branch? clust)
     (println "-")
     (println (:distance clust) "###" (fmt (:data clust))))

   (when-let [left (:left clust)]
     (print-clusters left (inc n)))

   (when-let [right (:right clust)]
     (print-clusters right (inc n)))))

(comment
  (def groups (cluster distance average test-dataset))
  (print-clusters groups)

  (write-png
    (->img groups fmt)
    "doc/dendrogram.png")
)


