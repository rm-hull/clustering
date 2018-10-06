# Clustering Algorithms
[![Build Status](https://travis-ci.org/rm-hull/clustering.svg?branch=master)](http://travis-ci.org/rm-hull/clustering)
[![Coverage Status](https://coveralls.io/repos/rm-hull/clustering/badge.svg?branch=master)](https://coveralls.io/r/rm-hull/clustering?branch=master)
[![Dependencies Status](https://versions.deps.co/rm-hull/clustering/status.svg)](https://versions.deps.co/rm-hull/clustering)
[![Downloads](https://versions.deps.co/rm-hull/clustering/downloads.svg)](https://versions.deps.co/rm-hull/clustering)
[![Clojars Project](https://img.shields.io/clojars/v/rm-hull/clustering.svg)](https://clojars.org/rm-hull/clustering)
[![Maintenance](https://img.shields.io/maintenance/yes/2018.svg?maxAge=2592000)]()

Implementation of K-Means, QT and Hierarchical clustering algorithms, in Clojure/Clojurescript.

### Pre-requisites

You will need [Leiningen](https://github.com/technomancy/leiningen) 2.7.1 or above installed.

### Building

To build and install the library locally, run:

    $ cd clustering
    $ lein test
    $ lein install

### Including in your project

There is a version hosted at [Clojars](https://clojars.org/rm-hull/clustering).
For leiningen include a dependency:

```clojure
[rm-hull/clustering "0.2.0"]
```

For maven-based projects, add the following to your `pom.xml`:

```xml
<dependency>
  <groupId>rm-hull</groupId>
  <artifactId>clustering</artifactId>
  <version>0.2.0</version>
</dependency>
```

## API Documentation

See [www.destructuring-bind.org/clustering](http://www.destructuring-bind.org/clustering/) for API details.

## High-level Overview

The main entry point to all the algorithms is the `cluster` function in each of
the `clustering.core.qt`, `clustering.core.k-means` and
`clustering.core.hierarchical` namespaces. Generally all cluster variants
require a distance function and a dataset (sequence/collection/...), where:

* the **distance** function should take two dataset items and perform
  some scalar measure of distance between the two points. Typical applications
  include euclidean distance, manhattan distance or pearson distance.

* the **dataset** should be a SEQable collection of data points that would be
  clustered.

The K-means and hierarchical clustering algorithms also require an
**averaging** function that takes a number of dataset items and creates an
"average" based on those items.

### _N_-dimensional clustering

The below examples show distance and averaging functions on a 1-dimensional
dataset comprising of dates. For most numeric datasets, any of the distance
measures in `clustering.distance` would be sufficient; these implementations
can handle arbitrarily-large _n_-dimensional datasets.

For non-numeric data, it would be necessary to either provide a mapper method
to 'convert' non-numeric values into a meaningful numeric value (and then use
the provided distance functions), or implement your own custom distance function.

Likewise, there exists a generic `clustering.average` namespace that operates
on arbitrarily-large _n_-dimensional numeric datasets.

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
distance measurement) into a cluster, progressively merging clusters into a
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
  "doc/dendrogram.png"
  (dendrogram/->img group fmt))

(spit
  "doc/dendrogram.svg"
  (dendrogram/->svg group fmt))
```

![dendrogram](https://rawgithub.com/rm-hull/clustering/master/doc/dendrogram.svg)

## More Examples

Further examples can be found in the
https://github.com/rm-hull/clustering/tree/master/test/clustering/examples
directory.

### Word Similaries

Taking a list of sampled dictionary words and using the Levenshtein distance to
cluster, the hierarchical clustering algorithm produce the following
dendrogram:

![word-similarities](https://rawgithub.com/rm-hull/clustering/master/doc/word-similarities.svg)

Substituting different distance metrics (see [clj-fuzzy](http://yomguithereal.github.io/clj-fuzzy/clojure.html))
would give different (and maybe more interesting) cluster clumps.

### Baseball: Team & League Standard Batting

[baseball-reference.com](http://www.baseball-reference.com/) has lots of
interesting historical statistics for all major league games, one of which is
the [2015 National League](http://www.baseball-reference.com/leagues/NL/2015.shtml#teams_standard_batting::none):


|Tm|#Bat|BatAge|R/G|G|PA|AB|R|H|2B|3B|HR|RBI|SB|CS|BB|SO|BA|OBP|SLG|OPS|OPS+|TB|GDP|HBP|SH|SF|IBB|LOB|
|---|---:|-----:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
|ARI|50|26.6|4.44|162|6276|5649|720|1494|289|48|154|680|132|44|490|1312|.264|.324|.414|.738|96|2341|134|33|46|57|40|1153|
|ATL|60|28.8|3.54|162|6034|5420|573|1361|251|18|100|548|69|33|471|1107|.251|.314|.359|.674|88|1948|148|44|67|31|39|1145|
|CHC|50|26.9|4.25|162|6200|5491|689|1341|272|30|171|657|95|37|567|1518|.244|.321|.398|.719|97|2186|101|74|32|35|47|1165|
|CIN|50|29.5|3.95|162|6196|5571|640|1382|257|27|167|613|134|38|496|1255|.248|.312|.394|.706|92|2194|112|42|47|40|38|1148|
|COL|51|28.0|4.55|162|6071|5572|737|1479|274|49|186|702|97|43|388|1283|.265|.315|.432|.748|89|2409|114|33|44|34|47|1016|
|LAD|55|29.6|4.12|162|6090|5385|667|1346|263|26|187|638|59|34|563|1258|.250|.326|.413|.739|107|2222|135|60|49|30|31|1121|
|MIA|51|27.9|3.78|162|5988|5463|613|1420|236|40|120|575|112|45|375|1150|.260|.310|.384|.694|91|2096|133|39|71|40|30|1059|
|MIL|49|28.1|4.04|162|6024|5480|655|1378|274|34|145|624|84|29|412|1299|.251|.307|.393|.700|90|2155|130|41|55|34|35|1026|
|NYM|49|28.5|4.22|162|6145|5527|683|1351|295|17|177|654|51|25|488|1290|.244|.312|.400|.712|98|2211|130|68|29|32|42|1098|
|PHI|50|28.0|3.86|162|6053|5529|626|1374|272|37|130|586|88|32|387|1274|.249|.303|.382|.684|86|2110|119|54|53|29|20|1066|
|PIT|46|28.2|4.30|162|6285|5631|697|1462|292|27|140|661|98|45|461|1322|.260|.323|.396|.719|98|2228|115|89|63|41|46|1166|
|SDP|46|27.7|4.01|162|6019|5457|650|1324|260|36|148|623|82|29|426|1327|.243|.300|.385|.685|92|2100|108|40|52|42|22|1028|
|SFG|48|28.9|4.30|162|6153|5565|696|1486|288|39|136|663|93|36|457|1159|.267|.326|.406|.732|102|2260|142|49|45|37|30|1130|
|STL|46|28.4|3.99|162|6139|5484|647|1386|288|39|137|619|69|38|506|1267|.253|.321|.394|.716|95|2163|128|66|39|42|47|1152|
|WSN|44|28.4|4.34|162|6117|5428|703|1363|265|13|177|665|57|23|539|1344|.251|.321|.403|.724|95|2185|129|44|55|51|38|1114|
|LgAvg|48|28.2|4.11|162|6119|5510|666|1396|272|32|152|634|88|35|468|1278|.253|.316|.397|.713|94|2187|125|52|50|38|37|1106|714|28.2|4.11|2430|91790|82652|9996|20947|4076|480|2275|9508|1320|531|7026|19165|.253|.316|.397|.713|94|32808|1878|776|747|575|552|16587|

Using the Euclidean distance function, this yields the following dendrogram:

![baseball-nl2015](https://rawgithub.com/rm-hull/clustering/master/doc/nl_2015.svg)

## References

* https://sites.google.com/site/dataclusteringalgorithms/quality-threshold-clustering-algorithm-1
* https://www.coursera.org/learn/parprog1/programming/akLxD/k-means
* US, UK & German city data derived from http://people.sc.fsu.edu/~jburkardt/datasets/cities/cities.html
* US voting data sourced from https://forge.scilab.org/index.php/p/rdataset/source/tree/master/csv/cluster/votes.repub.csv

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
