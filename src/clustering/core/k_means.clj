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

(ns clustering.core.k-means

  "K-Means clustering generates a specific number of disjoint, flat
  (non-hierarchical) clusters. It is well suited to generating globular
  clusters. The K-Means method is numerical, unsupervised, non-deterministic
  and iterative.

   * There are always K clusters.
   * There is always at least one item in each cluster.
   * The clusters are non-hierarchical and they do not overlap.
   * Every member of a cluster is closer to its cluster than any other cluster
     because closeness does not always involve the 'center' of clusters.

  The dataset is partitioned into K clusters and the data points are randomly
  assigned to the clusters resulting in clusters that have roughly the same
  number of data points.  For each data point: Calculate the distance from the
  data point to each cluster.

  If the data point is closest to its own cluster, leave it where it is. If the
  data point is not closest to its own cluster, move it into the closest
  cluster.

  Repeat the above step until a complete pass through all the data points
  results in no data point moving from one cluster to another. At this point
  the clusters are stable and the clustering process ends.  The choice of
  initial partition can greatly affect the final clusters that result, in
  terms of inter-cluster and intracluster distances and cohesion."

  (:refer-clojure :exclude [update]))

(defn init-means [k dataset]
  (if (> k (count dataset))
    (throw (IllegalArgumentException. (str "Cannot attempt to cluster " (count dataset) " points into " k " clusters")))
    (let [arr (vec dataset)
          n   (count arr)]
      (for [i (range k)]
        (arr (rand-int n))))))

(defn find-closest
  "Determine which mean is closed to the specified point, according to the
  supplied distance function (this should take 2 points and return a scalar
  difference between them)."
  [distance-fn point means]
  (second
    (reduce
      (fn [state curr]
        (let [dist (distance-fn point curr)]
          (if (< dist (or (first state) Integer/MAX_VALUE))
            [dist curr]
            state)))
      []
      means)))

(defn classify [distance-fn dataset means]
  (merge
    (into {} (map #(vector % nil) means))
    (group-by #(find-closest distance-fn % means) dataset)))

(defn update [average-fn classified old-means]
  (map
    (fn [mean]
     (if-let [points (classified mean)]
       (average-fn points)
       mean))
     old-means))

(defn converged? [distance-fn eta old-means new-means]
  (every?
    #(<= % eta)
    (map distance-fn old-means new-means)))

(defn centroids [distance-fn average-fn dataset means eta]
  (let [clusters (classify distance-fn dataset means)
        new-means (update average-fn clusters means)]
    (if (converged? distance-fn eta means new-means)
      new-means
      (recur distance-fn average-fn dataset new-means eta))))

(defn cluster [distance-fn average-fn dataset means eta]
  (->>
    (centroids distance-fn average-fn dataset means eta)
    (classify distance-fn dataset)
    vals
    vec))
