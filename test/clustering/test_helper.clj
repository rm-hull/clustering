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

(ns clustering.test-helper
  (:require
   [clojure.test :refer :all]
   [clj-time.core :refer [after? date-time interval in-days]]
   [clj-time.format :refer [unparse formatters]]
   [clj-time.coerce :refer [to-long from-long]]))

(defn distance [dt-a dt-b]
  (if (after? dt-a dt-b)
    (distance dt-b dt-a)
    (in-days (interval dt-a dt-b))))

(defn average [dates]
  (from-long
   (/ (reduce + (map to-long dates)) (count dates))))

(def fmt (partial unparse (formatters :date)))

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

(deftest check-distance
  (is (= 7 (distance (date-time 2013 7 21) (date-time 2013 7 28))))
  (is (= 7 (distance (date-time 2013 7 28) (date-time 2013 7 21))))
  (is (= 0 (distance (date-time 2016 1 19) (date-time 2016 1 19)))))

(deftest check-average
  (is (= (date-time 2016 11 19)
         (average
          (list
           (date-time 2016 11 18)
           (date-time 2016 11 20))))))

(deftest check-fmt
  (is (= "2019-02-19" (fmt (date-time 2019 2 19)))))
