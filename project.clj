(defproject rm-hull/clustering "0.2.0"
  :description "Implementation of K-Means, QT and Hierarchical clustering algorithms, in Clojure."
  :url "https://github.com/rm-hull/clustering"
  :license {
    :name "The MIT License (MIT)"
    :url "http://opensource.org/licenses/MIT"}
  :dependencies [
    [org.clojure/math.combinatorics "0.2.0"]
    [rm-hull/helpmate "0.1.4"]]
  :scm {:url "git@github.com:rm-hull/clustering.git"}
  :source-paths ["src"]
  :jar-exclusions [#"(?:^|/).git"]
  :codox {
    :source-paths ["src"]
    :doc-files [
      "doc/background.md"
      "doc/qt.md"
      "doc/k-means.md"
      "doc/hierarchical.md"
      "doc/references.md"
      "LICENSE.md"
    ]
    :output-path "doc/api"
    :source-uri "http://github.com/rm-hull/clustering/blob/main/{filepath}#L{line}"  }
  :min-lein-version "2.8.1"
  :profiles {
    :dev {
      :global-vars {*warn-on-reflection* true}
      :dependencies [
        [org.clojure/clojure "1.11.1"]
        [org.clojure/data.csv "1.0.1"]
        [org.clojure/test.check "1.1.1"]
        [clj-time "0.15.2"]]
      :plugins [
        [lein-codox "0.10.8"]
        [lein-cljfmt "0.9.2"]
        [lein-cloverage "1.2.4"]]}})
