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

(ns clustering.examples.word-similarities
  (:require
    [clojure.string :as s]
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clustering.core.hierarchical :refer :all]
    [clustering.average.simple :refer :all]
    [clustering.distance.levenshtein :refer :all]
    [clustering.data-viz.dendrogram :refer :all]
    [clustering.data-viz.image :refer :all]))

(defn avg-ch [letters]
  (char  (/ (reduce + (map int letters)) (count letters))))

(defn avg-word [words]
  (apply str (apply map (fn [& letters] (avg-ch letters)) words)))

(defn load-words [filename]
  (->>
    (slurp filename)
    s/split-lines
    (map #(s/replace (s/lower-case %) #"\W" ""))))

(defn length [n]
  (fn [word]
    (= n (count word))))

(defn generate-word-similarities []
  (let [data-set (->>
                   (load-words "test/data/en-GB/words")
                   (drop 2000)
                   (take-nth 50)
                   (filter (length 6))
                   (take 200))
        hier-data (cluster distance avg-word data-set)]
    (spit
      "doc/word-similarities.svg"
      (->svg hier-data))))

(generate-word-similarities)
