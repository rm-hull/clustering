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

(ns clustering.distance.levenshtein

  "The Levenshtein distance is a metric for measuring the amount of difference
  between two sequences (i.e. an edit distance). The Levenshtein distance
  between two strings is defined as the minimum number of edits needed to
  transform one string into the other, with the allowable edit operations
  being insertion, deletion, or substitution of a single character.")

;; Attribution: Rosetta code
;; https://rosettacode.org/wiki/Levenshtein_distance#Clojure

(def distance
  (memoize
   (fn [str1 str2]
     (let [len1 (count str1)
           len2 (count str2)]
       (cond (zero? len1) len2
             (zero? len2) len1
             :else
             (let [cost (if (= (first str1) (first str2)) 0 1)]
               (min (inc (distance (rest str1) str2))
                    (inc (distance str1 (rest str2)))
                    (+ cost
                       (distance (rest str1) (rest str2))))))))))
