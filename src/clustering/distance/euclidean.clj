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

(ns clustering.distance.euclidean
  "The Euclidean distance (or Euclidean metric) is the \"ordinary\" (i.e.
  straight-line) distance between two points in Euclidean space."

  (:require
    [clustering.distance.common :refer :all]))

(defn quadrance
  "Quadrance (also called squared euclidean distance) measures separation of
  points in Euclidean space. It is not a not a metric as it does not satisfy
  the triangle inequality, however, it is frequently used in optimization
  problems in which distances only have to be compared."
  [xs ys]
  (sum (map (comp sqr diff) xs ys)))

(defn distance
  "Calculates the straight line distance between two Cartesian coordinates.
  Unless the exact difference is required, prefer the computationally-cheaper
  quadrance instead."
  [xs ys]
  (Math/sqrt (quadrance xs ys)))

(defn manhattan
  "Calculates the distance between two points to be the sum of the absolute
  differences of their Cartesian coordinates."
  [xs ys]
  (sum (map diff xs ys)))

(defn chebyshev
  "Calculates the absolute magnitude of the differences between Cartesian
   coordinates. Also known as maximum value distance."
  [xs ys]
  (reduce max 0 (map diff xs ys)))
