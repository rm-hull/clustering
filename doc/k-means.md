# K-means clustering

K-means clustering aims to partition _n_ observations into _k_ clusters in
which each observation belongs to the cluster with the nearest mean, serving as
a prototype of the cluster. This results in a partitioning of the data space
into Voronoi cells.

K-means clustering is an NP-hard problem, but can be simply implemented using
the iterative refinement technique outlined below.

1. Pick k points called means. This is called initialization.
2. Associate each input point with the mean that is closest to it. We obtain k
   clusters of points, and we refer to this process as classifying the points.
3. Update each mean to have the average value of the corresponding cluster.
4. If the k means have significantly changed, go back to step 2. If they did
   not, we say that the algorithm converged.
5. The k means represent different clusters -- every point is in the cluster
   corresponding to the closest mean.

## Worked Example

Using the same one-dimensional dataset as the previous example, but
instead requiring the `clustering/k-means` namespace:

```clojure
(require '[clustering.core.k-means :as k-means])
(require '[clj-time.core :refer [after? date-time interval in-days])
(require '[clj-time.format :refer [unparse formatters])
(require '[clj-time.coerce :refer [to-long from-long])

(def test-dataset
  (hash-set
    (date-time 2013 7 21)
    (date-time 2013 7 25)
    ...)))
```

We still need a distance measure between two dates, but additionally, the
K-Means algorithm also requires a function capable of averaging across a
range of dates:

```clojure
(defn distance [dt-a dt-b]
  (if (after? dt-a dt-b)
    (distance dt-b dt-a)
    (in-days (interval dt-a dt-b))))

(defn average [dates]
  (from-long
    (/ (reduce + (map to-long dates)) (count dates))))
```

The algorithm is initialised with some mean values randomly selected from
the dataset. Note that the desired number of clusters must be specified
up-front (in this case, 3):

```clojure
(def means (k-means/init-means 3 test-dataset)

(def groups (k-means/cluster distance average test-dataset means 0))

(count groups)
; => 3

(map fmt (sort (groups 0)))
; => ("2012-12-26" "2012-12-28" "2013-01-16")

(map fmt (sort (groups 1)))
; => ("2013-07-01" "2013-07-14" "2013-07-21" "2013-07-25" "2013-07-31" "2013-08-03")

(map fmt (sort (groups 2)))
; => ("2012-05-28" "2012-06-02" "2012-06-06" "2012-06-07" "2012-06-09")
```

_(Note: Because of the random initialisation of the means, the cluster orderings
will be different each time evaluation occurs.)_
