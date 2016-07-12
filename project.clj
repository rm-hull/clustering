(defproject rm-hull/clustering "0.1.0"
  :description "Implementation of K-Means, QT and Hierarchical clustering algorithms, in Clojure."
  :url "https://github.com/rm-hull/clustering"
  :license {
    :name "The MIT License (MIT)"
    :url "http://opensource.org/licenses/MIT"}
  :dependencies [
    [org.clojure/clojure "1.8.0"]
    [org.clojure/math.combinatorics "0.1.3"]
    [hiccup "1.0.5"]]
  :scm {:url "git@github.com:rm-hull/clustering.git"}
  :source-paths ["src"]
  :jar-exclusions [#"(?:^|/).git"]
  :codox {
    :source-paths ["src"]
    :output-path "doc/api"
    :source-uri "http://github.com/rm-hull/clustering/blob/master/{filepath}#L{line}"  }
  :min-lein-version "2.6.1"
  :profiles {
    :dev {
      :global-vars {*warn-on-reflection* true}
      :dependencies [
        [org.clojure/test.check "0.9.0"]
        [clj-time "0.12.0"] ]
      :plugins [
        [lein-codox "0.9.5"]
        [lein-cloverage "1.0.6"]]}})
