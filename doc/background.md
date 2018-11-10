# Overview

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

## _N_-dimensional clustering

The following examples show distance and averaging functions on a 1-dimensional
dataset comprising of dates. For most numeric datasets, any of the distance
measures in `clustering.distance` would be sufficient; these implementations
can handle arbitrarily-large _n_-dimensional datasets.

For non-numeric data, it would be necessary to either provide a mapper method
to 'convert' non-numeric values into a meaningful numeric value (and then use
the provided distance functions), or implement your own custom distance function.

Likewise, there exists a generic `clustering.average` namespace that operates
on arbitrarily-large _n_-dimensional numeric datasets.
