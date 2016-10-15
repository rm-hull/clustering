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

(ns clustering.core.k-means-test
  (:require
   [clojure.test :refer :all]
   [clojure.test.check :as tc]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.properties :as prop]
   [clojure.test.check.clojure-test :refer [defspec]]
   [clustering.core.k-means :refer :all]
   [clustering.test-helper :refer :all]
   [clj-time.core :refer [date-time]]))

(deftest check-init-mean
  (is (thrown-with-msg?
       IllegalArgumentException
       #"Cannot attempt to cluster 0 points into 4 clusters"
       (init-means 4 [])))
  (is (thrown-with-msg?
       IllegalArgumentException
       #"Cannot attempt to cluster 0 points into 4 clusters"
       (init-means 4 nil))))

;(defspec init-mean-throws-err-when-not-enough-points
;  100
;  (prop/for-all [k (gen/choose 100 200)
;                 pts (gen/vector gen/int 0 99)]
;    (is (thrown-with-msg?
;          IllegalArgumentException
;          #"Cannot attempt to cluster \d+ points into \d+ clusters"
;         (init-means k pts)))))

(defspec init-mean-always-generates-k-elements
  100
  (prop/for-all [k (gen/choose 0 30)
                 pts (gen/not-empty (gen/vector gen/int 40 300))]
                (is (= k (count (init-means k pts))))))

(defspec init-mean-only-contains-pts
  100
  (prop/for-all [k (gen/choose 0 20)
                 pts (gen/not-empty (gen/vector gen/int 40 300))]
                (is (every? (set pts) (init-means k pts)))))

(defn diff [a b]
  (Math/abs (- a b)))

(defn avg [points]
  (/ (apply + points) (count points)))

(deftest check-find-closest
  (is (nil? (find-closest diff 4 [])))
  (is (nil? (find-closest diff 4 nil))))

(defspec find-closest-always-returns-one-of-the-means
  100
  (prop/for-all [pt gen/int
                 means (gen/not-empty (gen/list gen/int))]
                (is (contains? (set means) (find-closest diff pt means)))))

(defspec find-closest-always-returns-closest-mean
  100
  (prop/for-all [pt gen/int
                 means (gen/not-empty (gen/list gen/int))]
                (let [diffs (sort-by second (map #(vector % (diff pt %)) means))]
                  (is (= (ffirst diffs) (find-closest diff pt means))))))

(defspec classify-should-classify-all-points
  100
  (prop/for-all [points (gen/list gen/int)
                 means (gen/list gen/int)]
                (is (= (set points) (set (apply concat (vals (classify diff points means))))))))

(defspec classify-should-map-all-means
  100
  (prop/for-all [points (gen/list gen/int)
                 means (gen/not-empty (gen/list gen/int))]
                (is (= (set means) (set (keys (classify diff points means)))))))

(comment
  (def means (init-means 3 test-dataset))
  (def groups (cluster distance average test-dataset means 0))
  (count groups)
  (map fmt (sort (groups 0)))
  (map fmt (sort (groups 1)))
  (map fmt (sort (groups 2))))
