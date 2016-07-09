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

(ns clustering.qt
  (:require
    [clojure.set :refer [difference]]))

;; Quality Threshold (QT) clustering algorithm
;; -------------------------------------------
;;
;; From: https://sites.google.com/site/dataclusteringalgorithms/quality-threshold-clustering-algorithm-1
;;
;; 1) Initialize the threshold distance allowed for clusters and the
;;    minimum cluster size.
;;
;; 2) Build a candidate cluster for each data point by including the
;;    closest point, the next closest, and so on, until the distance
;;    of the cluster surpasses the threshold.
;;
;; 3) Save the candidate cluster with the most points as the first true
;;    cluster, and remove all points in the cluster from further
;;    consideration.
;;
;; 4) Repeat with the reduced set of points until no more cluster can
;;    be formed having the minimum cluster size.
;;

(defn candidate-cluster

  "Determine which members of the dataset are closed to the candidate
  point within the given threshold"

  [distance-fn point coll threshold]
  (let [too-big? #(> (distance-fn point %) threshold)]
    (set (remove too-big? coll))))

(defn most-candidates [distance-fn coll threshold]
  (->>
    coll
    (map #(candidate-cluster distance-fn % coll threshold))
    (sort-by count >)
    first))

(defn cluster

  "Groups members of supplied dataset into specific clusters according to the
  provided distance function (this should take 2 collection members and return
  a scalar difference between them). Candidate clusters are assembled such
  that the distance of the cluster surpasses the threshold. Further clusters
  are formed from the remaining points until no more clusters can be formed
  having the minimum cluster size."

  [distance-fn coll threshold min-size]
  (loop [clusters []
         uniq (set coll)]
    (let [best (most-candidates distance-fn uniq threshold)]
      (if (< (count best) min-size)
        clusters
        (recur
          (conj clusters best)
          (difference uniq best))))))

