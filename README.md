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

## Basic Usage

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
