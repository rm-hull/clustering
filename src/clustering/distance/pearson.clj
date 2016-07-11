(ns clustering.distance.pearson)

(defn- sum [xs]
  (reduce + xs))

(defn sqr [x]
  (* x x))

(defn- sum-product [xs ys]
  (sum (map #(* %1 %2) xs ys)))

(defn- sum-squares [xs]
  (sum-product xs xs))

(defn correlation-coefficient

  "Pearson product-moment correlation coefficient is a measure of the linear
  correlation between two variables X and Y, giving a value between +1 and −1
  inclusive, where 1 is total positive correlation, 0 is no correlation, and
  −1 is total negative correlation."

  [xs ys]
  (let [len  (count xs)
        sum1 (sum xs)
        sum2 (sum ys)
        num  (- (sum-product xs ys) (/ (* sum1 sum2) len))
        den  (Math/sqrt  (* (- (sum-squares xs) (/ (sqr sum1) len))
                            (- (sum-squares ys) (/ (sqr sum2) len))))]
    (println "len=" len "sum1=" sum1 "sum2=" sum2 "num=" num "den=" den)
    (if (zero? den)
      0
      (/ num den))))

(defn distance

  "Pearson's distance can be defined from their correlation coefficient as:

     d(X,Y) = 1 - ρ(X,Y)

  Considering that the Pearson correlation coefficient falls between [−1, 1],
  the Pearson distance lies in [0, 2]."

  [xs ys]
  (- 1.0 (correlation-coefficient xs ys)))
