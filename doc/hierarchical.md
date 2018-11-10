# Hierarchical clustering

Agglomorative clustering seeks to pair up nearest points (according to a chosen
distance measurement) into a cluster, progressively merging clusters into a
hierarchy, until there only is a single cluster left.

## Worked Example

As before, using the date example, we need the `distance` and `average` functions
as defined previously:

```clojure
(require '[clustering.core.hierarchical :as hier])
(require '[clustering.data-viz.image :refer :all])
(require '[clustering.data-viz.dendrogram :as dendrogram])
(require '[clj-time.core :refer [after? date-time interval in-days])
(require '[clj-time.format :refer [unparse formatters])
(require '[clj-time.coerce :refer [to-long from-long])

(def test-dataset
  (hash-set
    (date-time 2013 7 21)
    (date-time 2013 7 25)
    ...)))

(defn distance [dt-a dt-b]
   ...)

(defn average [dates]
   ...)
```

Rather than returning a vector of clusters, hierarchical clustering returns a
single cluster object with left and right sub-parts that require recursive
traversal, most easily demonstrated with a suitable data visualization, such as
a [dendrogram](https://en.wikipedia.org/wiki/Dendrogram):

```clojure
(def groups (hier/cluster distance average test-dataset))

(write-png
  "doc/dendrogram.png"
  (dendrogram/->img group fmt))

(spit
  "doc/dendrogram.svg"
  (dendrogram/->svg group fmt))
```

![dendrogram](https://rawgithub.com/rm-hull/clustering/master/doc/dendrogram.svg)

## More Examples

Further examples can be found in the
https://github.com/rm-hull/clustering/tree/master/test/clustering/examples
directory.

### Word Similaries

Taking a list of sampled dictionary words and using the Levenshtein distance to
cluster, the hierarchical clustering algorithm produce the following
dendrogram:

![word-similarities](https://rawgithub.com/rm-hull/clustering/master/doc/word-similarities.svg)

Substituting different distance metrics (see [clj-fuzzy](http://yomguithereal.github.io/clj-fuzzy/clojure.html))
would give different (and maybe more interesting) cluster clumps.

### Baseball: Team & League Standard Batting

[baseball-reference.com](http://www.baseball-reference.com/) has lots of
interesting historical statistics for all major league games, one of which is
the [2015 National League](http://www.baseball-reference.com/leagues/NL/2015.shtml#teams_standard_batting::none):


|Tm|#Bat|BatAge|R/G|G|PA|AB|R|H|2B|3B|HR|RBI|SB|CS|BB|SO|BA|OBP|SLG|OPS|OPS+|TB|GDP|HBP|SH|SF|IBB|LOB|
|---|---:|-----:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
|ARI|50|26.6|4.44|162|6276|5649|720|1494|289|48|154|680|132|44|490|1312|.264|.324|.414|.738|96|2341|134|33|46|57|40|1153|
|ATL|60|28.8|3.54|162|6034|5420|573|1361|251|18|100|548|69|33|471|1107|.251|.314|.359|.674|88|1948|148|44|67|31|39|1145|
|CHC|50|26.9|4.25|162|6200|5491|689|1341|272|30|171|657|95|37|567|1518|.244|.321|.398|.719|97|2186|101|74|32|35|47|1165|
|CIN|50|29.5|3.95|162|6196|5571|640|1382|257|27|167|613|134|38|496|1255|.248|.312|.394|.706|92|2194|112|42|47|40|38|1148|
|COL|51|28.0|4.55|162|6071|5572|737|1479|274|49|186|702|97|43|388|1283|.265|.315|.432|.748|89|2409|114|33|44|34|47|1016|
|LAD|55|29.6|4.12|162|6090|5385|667|1346|263|26|187|638|59|34|563|1258|.250|.326|.413|.739|107|2222|135|60|49|30|31|1121|
|MIA|51|27.9|3.78|162|5988|5463|613|1420|236|40|120|575|112|45|375|1150|.260|.310|.384|.694|91|2096|133|39|71|40|30|1059|
|MIL|49|28.1|4.04|162|6024|5480|655|1378|274|34|145|624|84|29|412|1299|.251|.307|.393|.700|90|2155|130|41|55|34|35|1026|
|NYM|49|28.5|4.22|162|6145|5527|683|1351|295|17|177|654|51|25|488|1290|.244|.312|.400|.712|98|2211|130|68|29|32|42|1098|
|PHI|50|28.0|3.86|162|6053|5529|626|1374|272|37|130|586|88|32|387|1274|.249|.303|.382|.684|86|2110|119|54|53|29|20|1066|
|PIT|46|28.2|4.30|162|6285|5631|697|1462|292|27|140|661|98|45|461|1322|.260|.323|.396|.719|98|2228|115|89|63|41|46|1166|
|SDP|46|27.7|4.01|162|6019|5457|650|1324|260|36|148|623|82|29|426|1327|.243|.300|.385|.685|92|2100|108|40|52|42|22|1028|
|SFG|48|28.9|4.30|162|6153|5565|696|1486|288|39|136|663|93|36|457|1159|.267|.326|.406|.732|102|2260|142|49|45|37|30|1130|
|STL|46|28.4|3.99|162|6139|5484|647|1386|288|39|137|619|69|38|506|1267|.253|.321|.394|.716|95|2163|128|66|39|42|47|1152|
|WSN|44|28.4|4.34|162|6117|5428|703|1363|265|13|177|665|57|23|539|1344|.251|.321|.403|.724|95|2185|129|44|55|51|38|1114|
|LgAvg|48|28.2|4.11|162|6119|5510|666|1396|272|32|152|634|88|35|468|1278|.253|.316|.397|.713|94|2187|125|52|50|38|37|1106|714|28.2|4.11|2430|91790|82652|9996|20947|4076|480|2275|9508|1320|531|7026|19165|.253|.316|.397|.713|94|32808|1878|776|747|575|552|16587|

Using the Euclidean distance function, this yields the following dendrogram:

![baseball-nl2015](https://rawgithub.com/rm-hull/clustering/master/doc/nl_2015.svg)
