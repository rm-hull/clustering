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

There _will be_ a version hosted at [Clojars](https://clojars.org/rm-hull/clustering).
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
(require '[clustering/qt :as qt])
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

## K-Means clustering

> TODO

## Hierarchical clustering

> TODO

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
