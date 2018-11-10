# Quality Threshold (QT) clustering

From: https://sites.google.com/site/dataclusteringalgorithms/quality-threshold-clustering-algorithm-1

1. Initialize the threshold distance allowed for clusters and the
   minimum cluster size.
2. Build a candidate cluster for each data point by including the
   closest point, the next closest, and so on, until the distance
   of the cluster surpasses the threshold.
3. Save the candidate cluster with the most points as the first true
   cluster, and remove all points in the cluster from further
   consideration.
4. Repeat with the reduced set of points until no more cluster can
   be formed having the minimum cluster size.

**NOTE:** QT clustering is computationally intensive and time consuming -
increasing the minimum cluster size or increasing the number of data points can
greatly increase the computational time.

## Worked example

We'll start by trying to cluster a simple one-dimensional set of dates:

```clojure
(require '[clustering.core.qt :as qt])
(require '[clj-time.core :refer [after? date-time interval in-days])
(require '[clj-time.format :refer [unparse formatters])

(def test-dataset
  (hash-set
    (date-time 2013 7 21)
    (date-time 2013 7 25)
    (date-time 2013 7 14)
    (date-time 2013 7 31)
    (date-time 2013 7 1)
    (date-time 2013 8 3)

    (date-time 2012 12 26)
    (date-time 2012 12 28)
    (date-time 2013 1 16)

    (date-time 2012 6 2)
    (date-time 2012 6 7)
    (date-time 2012 6 6)
    (date-time 2012 6 9)
    (date-time 2012 5 28)))
```

In order to use the QT clustering algorithm, we need to first define some
measure of distance between data-points; this is quite easy for dates:

```clojure
(defn distance [dt-a dt-b]
  (if (after? dt-a dt-b)
    (distance dt-b dt-a)
    (in-days (interval dt-a dt-b))))

(distance
  (date-time 2012 12 26)
  (date-time 2013 1 16))
; => 21
```

For convenience, let's also define a date formatter:

```clojure
(def fmt (partial unparse (formatters :date)))

(fmt (date-time 2019 2 19))
; => "2019-02-19"
```

To split these into clusters (with a minimum cluster size of 3), grouped
roughly into months,

```clojure
(def groups (qt/cluster distance test-dataset 31 3))

(count groups)
; => 3

(map fmt (sort (groups 0)))
; => ("2013-07-01" "2013-07-14" "2013-07-21" "2013-07-25" "2013-07-31" "2013-08-03")

(map fmt (sort (groups 1)))
; => ("2012-05-28" "2012-06-02" "2012-06-06" "2012-06-07" "2012-06-09")

(map fmt (sort (groups 2)))
; => ("2012-12-26" "2012-12-28" "2013-01-16")
```

