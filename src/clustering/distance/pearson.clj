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

(ns clustering.distance.pearson

  "Pearson's correlation coefficient is the covariance of the two variables
  divided by the product of their standard deviations, and is commonly
  represented by the Greek letter ρ (rho).

  Developed by Karl Pearson from a related idea introduced by Francis Galton
  in the 1880s, this product-moment correlation coefficient is widely used in
  the sciences as a measure of the degree of linear dependence between two
  variables. Early work on the distribution of the sample correlation
  coefficient was carried out by Anil Kumar Gain and R. A. Fisher from the
  University of Cambridge.

  See: https://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient"

  (:require
    [clustering.distance.common :refer :all]))

(defn- div0 [x y]
  (if (zero? y) 0 (/ x y)))

(defn correlation-coefficient

  "Pearson product-moment correlation coefficient is a measure of the linear
  correlation between two variables X and Y, giving a value between +1 and −1
  inclusive, where 1 is total positive correlation, 0 is no correlation, and
  −1 is total negative correlation."

  [xs ys]
  (let [len   (count xs)
        sumx  (sum xs)
        sumy  (sum ys)
        numer (- (sum-product xs ys) (div0 (* sumx sumy) len))
        denom (Math/sqrt (* (- (sum-squares xs) (div0 (sqr sumx) len))
                            (- (sum-squares ys) (div0 (sqr sumy) len))))]
    (div0 numer denom)))

(defn distance

  "Pearson's distance can be defined from their correlation coefficient as:

     d(X,Y) = 1 - ρ(X,Y)

  Considering that the Pearson correlation coefficient falls between [−1, 1],
  the Pearson distance lies in [0, 2]."

  [xs ys]
  (- 1.0 (correlation-coefficient xs ys)))

(defn squared-distance

  "The Pearson Squared distance measures the similarity in shape between two
  profiles, but can also capture inverse relationships.

  While most combinations of clustering algorithm and distance metrics provide
  meaningful results, there are a few combinations that are difficult to
  interpret. In particular, combining K-Means clustering with the Pearson
  Squared distance metric can lead to non-intuitive centroid plots since the
  centroid represents the mean of the cluster and Pearson Squared can group
  anti-correlated objects. In these cases, visually drilling into clusters to
  see the individual members through the use of Cluster Plots produce better
  results."

  [xs ys]
  (- 1.0 (* 2 (correlation-coefficient xs ys))))
