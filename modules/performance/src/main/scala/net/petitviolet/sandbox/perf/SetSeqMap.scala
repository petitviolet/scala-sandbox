package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

/**
 * NUMはコレクションの要素数(1 ot NUM)
 * 要素の中見はi % divider
 * dividerが小さいほど重複し、大きいほど重複が少ない
 * mapするならSeqの方が良い
 * Setをmapする場合でもtoSeqしてからやれば、スループットが改善する可能性もある
 * parは参考
 *
 * [info] Benchmark                      (NUM)  (divider)   Mode  Cnt        Score         Error  Units
 * [info] SetSeqMap.seqMap                  10          2  thrpt   20  7206775.408 ± 1157990.133  ops/s
 * [info] SetSeqMap.setMap                  10          2  thrpt   20  2012821.143 ±  254674.664  ops/s
 * [info] SetSeqMap.setToSeqToSetMap        10          2  thrpt   20  2672353.944 ±   56257.742  ops/s
 * [info] SetSeqMap.setToSeqToSetParMap     10          2  thrpt   20  1869675.818 ±   26161.739  ops/s
 *
 * [info] SetSeqMap.seqMap                  10          5  thrpt   20  5620394.372 ± 1551618.961  ops/s
 * [info] SetSeqMap.setMap                  10          5  thrpt   20  1133935.315 ±  118963.538  ops/s
 * [info] SetSeqMap.setToSeqToSetMap        10          5  thrpt   20  1738314.911 ±   41478.518  ops/s
 * [info] SetSeqMap.setToSeqToSetParMap     10          5  thrpt   20  1303872.650 ±   22036.167  ops/s
 *
 * [info] SetSeqMap.seqMap                  10         10  thrpt   20  5998303.246 ± 1519345.553  ops/s
 * [info] SetSeqMap.setMap                  10         10  thrpt   20   923394.721 ±   43050.739  ops/s
 * [info] SetSeqMap.setToSeqToSetMap        10         10  thrpt   20  1217495.705 ±   43785.678  ops/s
 * [info] SetSeqMap.setToSeqToSetParMap     10         10  thrpt   20  1098094.018 ±   12986.708  ops/s
 *
 * [info] SetSeqMap.seqMap                 100          2  thrpt   20   955626.526 ±   25673.664  ops/s
 * [info] SetSeqMap.setMap                 100          2  thrpt   20   645272.883 ±   17108.524  ops/s
 * [info] SetSeqMap.setToSeqToSetMap       100          2  thrpt   20   338502.005 ±  147206.439  ops/s
 * [info] SetSeqMap.setToSeqToSetParMap    100          2  thrpt   20   487993.704 ±   11797.560  ops/s
 *
 * [info] SetSeqMap.seqMap                 100          5  thrpt   20   772722.622 ±  136100.591  ops/s
 * [info] SetSeqMap.setMap                 100          5  thrpt   20   345119.168 ±   21184.235  ops/s
 * [info] SetSeqMap.setToSeqToSetMap       100          5  thrpt   20   340911.700 ±   43145.188  ops/s
 * [info] SetSeqMap.setToSeqToSetParMap    100          5  thrpt   20   346660.949 ±    5809.422  ops/s
 *
 * [info] SetSeqMap.seqMap                 100         10  thrpt   20   821573.420 ±   25233.615  ops/s
 * [info] SetSeqMap.setMap                 100         10  thrpt   20   328370.778 ±   16890.075  ops/s
 * [info] SetSeqMap.setToSeqToSetMap       100         10  thrpt   20   281623.049 ±   97630.877  ops/s
 * [info] SetSeqMap.setToSeqToSetParMap    100         10  thrpt   20   341262.760 ±    5617.898  ops/s
 *
 * [info] SetSeqMap.seqMap                1000          2  thrpt   20    57336.413 ±    4776.813  ops/s
 * [info] SetSeqMap.setMap                1000          2  thrpt   20    24902.394 ±    1755.213  ops/s
 * [info] SetSeqMap.setToSeqToSetMap      1000          2  thrpt   20    19844.671 ±    6772.988  ops/s
 * [info] SetSeqMap.setToSeqToSetParMap   1000          2  thrpt   20    28981.939 ±     502.133  ops/s
 *
 * [info] SetSeqMap.seqMap                1000          5  thrpt   20    52004.231 ±    6937.619  ops/s
 * [info] SetSeqMap.setMap                1000          5  thrpt   20    29189.240 ±    1496.241  ops/s
 * [info] SetSeqMap.setToSeqToSetMap      1000          5  thrpt   20    36052.293 ±     642.888  ops/s
 * [info] SetSeqMap.setToSeqToSetParMap   1000          5  thrpt   20    34037.167 ±     470.476  ops/s
 *
 * [info] SetSeqMap.seqMap                1000         10  thrpt   20    50734.934 ±   11155.949  ops/s
 * [info] SetSeqMap.setMap                1000         10  thrpt   20    28105.001 ±    4085.755  ops/s
 * [info] SetSeqMap.setToSeqToSetMap      1000         10  thrpt   20    36352.388 ±     527.149  ops/s
 * [info] SetSeqMap.setToSeqToSetParMap   1000         10  thrpt   20    33936.058 ±     578.170  ops/s
 *
 * [info] SetSeqMap.seqMap               10000          2  thrpt   20     4334.072 ±     908.103  ops/s
 * [info] SetSeqMap.setMap               10000          2  thrpt   20     1476.945 ±      91.136  ops/s
 * [info] SetSeqMap.setToSeqToSetMap     10000          2  thrpt   20     2695.218 ±      49.306  ops/s
 * [info] SetSeqMap.setToSeqToSetParMap  10000          2  thrpt   20     2768.830 ±      53.658  ops/s
 *
 * [info] SetSeqMap.seqMap               10000          5  thrpt   20     5499.338 ±     433.218  ops/s
 * [info] SetSeqMap.setMap               10000          5  thrpt   20     1542.406 ±      72.210  ops/s
 * [info] SetSeqMap.setToSeqToSetMap     10000          5  thrpt   20     3359.222 ±      55.803  ops/s
 * [info] SetSeqMap.setToSeqToSetParMap  10000          5  thrpt   20     3318.605 ±      47.527  ops/s
 *
 * [info] SetSeqMap.seqMap               10000         10  thrpt   20     5369.101 ±     596.157  ops/s
 * [info] SetSeqMap.setMap               10000         10  thrpt   20     3341.545 ±     793.254  ops/s
 * [info] SetSeqMap.setToSeqToSetMap     10000         10  thrpt   20     3316.795 ±      61.864  ops/s
 * [info] SetSeqMap.setToSeqToSetParMap  10000         10  thrpt   20     3209.814 ±      55.183  ops/s
 *
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f1 .*SetSeqMap.*'
 *
 * [info] Benchmark         (NUM)  (divider)   Mode  Cnt        Score       Error  Units
 * [info] SetSeqMap.seqMap    100          2  thrpt   20  1008702.608 ± 13996.782  ops/s
 * [info] SetSeqMap.seqMap    100       1000  thrpt   20  1000115.057 ± 44815.880  ops/s
 * [info] SetSeqMap.seqMap  10000          2  thrpt   20     7817.339 ±   205.774  ops/s
 * [info] SetSeqMap.seqMap  10000       1000  thrpt   20     6698.324 ±   547.732  ops/s
 * [info] SetSeqMap.setMap    100          2  thrpt   20   752281.601 ±  5889.282  ops/s
 * [info] SetSeqMap.setMap    100       1000  thrpt   20   109611.682 ±  8627.055  ops/s
 * [info] SetSeqMap.setMap  10000          2  thrpt   20     1241.677 ±   145.807  ops/s
 * [info] SetSeqMap.setMap  10000       1000  thrpt   20     1319.398 ±   107.874  ops/s
 *
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f1 .*SetSeqMap.*'
 */
@State(Scope.Benchmark)
class SetSeqMap {
//  @Param(Array("10", "100", "1000", "10000"))
  @Param(Array("100", "10000"))
  var NUM: Int = _

  @Param(Array("2", "1000"))
  var divider: Int = _

  var targetSeq: Seq[Int] = _
  var targetSet: Set[Int] = _

  @Setup
  def setup = {
    targetSeq = (1 to NUM).toSeq
    targetSet = (1 to NUM).toSet
  }

  private def f(i: Int): Int = i % divider

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def seqMap(): Seq[Int] = {
    targetSeq map f
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def setMap(): Set[Int] = {
    targetSet map f
  }

//  @Benchmark
//  @BenchmarkMode(Array(Mode.Throughput))
//  def setToSeqToSetMap(): Set[Int] = {
//    targetSet.toSeq.map(f)(collection.breakOut)
//  }
//
//  @Benchmark
//  @BenchmarkMode(Array(Mode.Throughput))
//  def setToSeqToSetViewMap(): Set[Int] = {
//    targetSet.toSeq.view.map(f)(collection.breakOut)
//  }
//
//  @Benchmark
//  @BenchmarkMode(Array(Mode.Throughput))
//  def setToSeqToSetParMap(): Set[Int] = {
//    targetSet.toSeq.par.map(f)(collection.breakOut)
//  }

//  val N: Int = ???
//  val func: Int => Int = ???
//  val seq: Seq[Int] = (1 to N).toSeq
//  val set: Set[Int] = (1 to N).toSet
//
//  seq map func
//  seq map func
}
