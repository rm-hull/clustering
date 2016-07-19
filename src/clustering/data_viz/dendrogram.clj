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

(ns clustering.data-viz.dendrogram
  (:require
    [clojure.string :refer [join]]
    [hiccup.core :refer [html]]
    [clustering.data-viz.image :refer :all])
  (:import
    [java.awt.geom AffineTransform GeneralPath]
    [java.awt Graphics2D Color BasicStroke]))

(defn height [cluster]
  (if (:branch? cluster)
    (+ (height (:left cluster)) (height (:right cluster)))
    1))

(defn depth [cluster]
  (if (:branch? cluster)
    (+ (:distance cluster) (max (depth (:left cluster)) (depth (:right cluster))))
    0))

(def item-height 20)

(defn calc-bounds [cluster]
  (let [width 1024
        margin 150]
    { :h (* item-height (height cluster))
      :w width
      :scaling (double (/ (- width margin) (depth cluster)))}))

(defn draw-node [draw-branch-fn draw-text-fn cluster x y scaling]
  (if (:branch? cluster)
    (let [h1 (* item-height (height (:left cluster)))
          h2 (* item-height (height (:right cluster)))
          top  (- y (/ (+ h1 h2) 2))
          bottom (+ y (/ (+ h1 h2) 2))
          ll (+ x (* scaling (:distance cluster)))
          t (+ top (/ h1 2))
          b (- bottom (/ h2 2))]
      (draw-branch-fn t ll b x)
      (draw-node draw-branch-fn draw-text-fn (:left cluster) ll t scaling)
      (draw-node draw-branch-fn draw-text-fn (:right cluster) ll b scaling))
    (draw-text-fn (:data cluster) (int (+ x 5)) (int (+ y 4)))))

(defn ->img
  ([cluster]
    (->img cluster str))

  ([cluster render-fn]
    (let [{:keys [w h scaling]} (calc-bounds cluster)
          img (create-image w h)
          g2d (create-graphics img)
          text-fn  (fn [data ^long x ^long y]
                         (.drawString g2d ^String (render-fn data) x y))
          brnch-fn (fn [top right bottom left]
                         (doto g2d
                           (.drawLine left top left bottom)
                           (.drawLine left top right top)
                           (.drawLine left bottom right bottom)))]
      (doto g2d
        (.setBackground Color/WHITE)
        (.setColor Color/BLACK)
        (.clearRect 0 0 w h)
        (.setStroke (BasicStroke. 1))
        (.drawLine 0 (/ h 2) 10 (/ h 2)))
      (draw-node brnch-fn text-fn cluster 10 (/ h 2) scaling)
      (.dispose g2d)
      img)))

(defn round
  "Round a double to the given precision (number of significant digits)"
  [precision]
  (fn [d]
    (let [factor (Math/pow 10 precision)]
      (/ (Math/round (* d factor)) factor))))

(defn polyline [line-style points]
  [:polyline
    {:points (join "," (map (round 2) points))
     :style line-style}])

(defn ->svg
  ([cluster]
    (->svg cluster str))

  ([cluster render-fn]
    (let [{:keys [w h scaling]} (calc-bounds cluster)
          line-style "fill:white;stroke:black;stroke-width:2"
          collector  (atom [:g])
          text-fn    (fn [data ^long x ^long y]
                       (swap! collector conj
                              [:text {:x x :y y  :font-family "sans-serif"} (render-fn data)]))
          brnch-fn   (fn [top right bottom left]
                       (swap! collector conj
                              (polyline line-style [right top left top left bottom right bottom])))]
    (swap! collector conj
           (polyline line-style [ 0 (/ h 2) 10 (/ h 2)]))
    (draw-node brnch-fn text-fn cluster 10 (/ h 2) scaling)
    (html
      [:svg
       { :xmlns "http://www.w3.org/2000/svg"
         :xmlns:xlink "http://www.w3.org/1999/xlink"
         :width w :height h
         :zoomAndPan "magnify"
         :preserveAspectRatio "xMidYMid meet"
         :overflow "visible"
         :version "1.0" }
       @collector]))))


