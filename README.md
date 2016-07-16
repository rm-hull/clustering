# Clustering Algorithms
[![Build Status](https://travis-ci.org/rm-hull/clustering.svg?branch=master)](http://travis-ci.org/rm-hull/clustering) [![Coverage Status](https://coveralls.io/repos/rm-hull/clustering/badge.svg?branch=master)](https://coveralls.io/r/rm-hull/clustering?branch=master) [![Dependencies Status](https://jarkeeper.com/rm-hull/clustering/status.svg)](https://jarkeeper.com/rm-hull/clustering) [![Downloads](https://jarkeeper.com/rm-hull/clustering/downloads.svg)](https://jarkeeper.com/rm-hull/clustering) [![Clojars Project](https://img.shields.io/clojars/v/rm-hull/clustering.svg)](https://clojars.org/rm-hull/clustering)

Implementation of K-Means, QT and Hierarchical clustering algorithms, in Clojure.

### Pre-requisites

You will need [Leiningen](https://github.com/technomancy/leiningen) 2.6.1 or above installed.

### Building

To build and install the library locally, run:

    $ cd clustering
    $ lein test
    $ lein install

### Including in your project

There is a version hosted at [Clojars](https://clojars.org/rm-hull/clustering).
For leiningen include a dependency:

```clojure
[rm-hull/clustering "0.1.0"]
```

For maven-based projects, add the following to your `pom.xml`:

```xml
<dependency>
  <groupId>rm-hull</groupId>
  <artifactId>clustering</artifactId>
  <version>0.1.0</version>
</dependency>
```

## API Documentation

See [www.destructuring-bind.org/clustering](http://www.destructuring-bind.org/clustering/) for API details.

## Algorithms

### Quality Threshold (QT) clustering

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

> **NOTE:** QT clustering is computationally intensive and time
> consuming - increasing the minimum cluster size or increasing
> the number of data points can greatly increase the computational
> time.

#### Worked example

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

### K-Means clustering

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

#### Worked Example

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

### Hierarchical clustering

Agglomorative clustering seeks to pair up nearest points (according to a chosen
distace measurement) into a cluster, progressively merging clusters into a
hierarchy, until there only is a single cluster left.

#### Worked Example

As before, using the date example, we need the `distance` and `average` functions
as defined previously:

```clojure
(require '[clustering.core.hierarchical :as hier])
(require '[clustering.data-viz.image :refer :all])
(require '[clustering.data-viz.dendrogram :as dendrogram])
(require '[clj-time.core :refer [after? date-time interval in-days])
(require '[clj-time.format :refer [unparse formatters])
(require '[clj-time.coerce :refer [to-long from-long])

(def test-dataset
  (hash-set
    (date-time 2013 7 21)
    (date-time 2013 7 25)
    ...)))

(defn distance [dt-a dt-b]
   ...)

(defn average [dates]
   ...)
```

Rather than returning a vector of clusters, hierarchical clustering returns a
single cluster object with left and right sub-parts that require recursive
traversal, most easily demonstrated with a suitable data visualization, such as
a [dendrogram](https://en.wikipedia.org/wiki/Dendrogram):

```clojure
(def groups (hier/cluster distance average test-dataset))

(write-png
  (dendrogram/->img group fmt)
  "doc/dendrogram.png")
```

![dendrogram](https://rawgithub.com/rm-hull/clustering/master/doc/dendrogram.png)

## References

* https://sites.google.com/site/dataclusteringalgorithms/quality-threshold-clustering-algorithm-1
* https://www.coursera.org/learn/parprog1/programming/akLxD/k-means

## License

The MIT License (MIT)

Copyright (c) 2016 Richard Hull

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
