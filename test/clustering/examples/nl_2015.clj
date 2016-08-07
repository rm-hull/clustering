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

(ns clustering.examples.nl-2015
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clustering.core.hierarchical :refer :all]
    [clustering.average.simple :refer :all]
    [clustering.distance.pearson :refer :all]
    [clustering.data-viz.dendrogram :refer :all]
    [clustering.data-viz.image :refer :all]))

(defn convert-row [record]
  {:name (first record)
   :data (map #(Double/parseDouble %) (next record))})

(defn load-csv [filename]
  (with-open [in-file (io/reader filename)]
    (let [[header & data] (csv/read-csv in-file)]
    (doall
      (map convert-row data)))))

(defn dist [rec1 rec2]
  (distance (:data rec1) (:data rec2)))

(defn avg [recs]
  { :name "n/a" :data (average (map :data recs))})

(defn dendrogram [hier-data]
  (->svg hier-data :name))

(defn generate-dendrogram [dataset-name]
  (->>
    (load-csv (str "test/data/" dataset-name ".csv"))
    (cluster dist avg)
    dendrogram
    (spit (str "doc/" dataset-name ".svg")) ))

;(generate-dendrogram "nl_2015")
