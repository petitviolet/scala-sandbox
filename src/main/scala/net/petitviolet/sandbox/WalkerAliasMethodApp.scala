package net.petitviolet.sandbox
import scala.util.Random

object WalkerAliasMethodApp extends App {
  val N = 3
  val pp: Seq[Double] = Seq(0.1, 0.1, 0.2)

  WalkerAliasMethod.setUp(N, pp)
//  val results = (0 until 1000000).map(_ => WalkerAliasMethod.sample())
  val results = (0 until 1000000).map(_ => sampling(pp))
  val counts = results.distinct.map(i => (i, results.count(_ == i))).sortWith(_._2 < _._2)
  println(counts)

  def sampling(pp: Seq[Double]) = {
    val sum = pp.sum
    val zipped = pp.map(_ / sum).zipWithIndex
    val mapped = acc(zipped).sortWith(_._2 < _._2)
    val p = new Random().nextDouble()
    val idx = mapped find { case (_, score) =>
      p <= score
    } getOrElse mapped.last
    idx._1
  }

  def acc(zipped: Seq[(Double, Int)]) = {
    zipped map { case (v, k) =>
        if (k > 0) (k, v + zipped(k - 1)._1)
        else (k, v)
    }
  }

  object WalkerAliasMethod {  // N=number of sample values, ff=bar fractions, aa=aliases
    val random = new Random
    var N: Int = _
    var ff: Array[Double] = _
    var aa: Array[Int] = _

    def setUp(N:Int, pp: Seq[Double]) { //pp=probability vector
      this.N = N
      var jmin: Int = 0
      var jmax: Int = 0
      var bmin: Double = 0
      var bmax: Double = 0
      val s: Double = pp.sum
      var oon: Double = 0
      val tol: Double = 0
      val bb: Array[Double] = new Array(N)
      ff = new Array(N)
      aa = new Array(N)

      oon = 1.0d / N

      (0 until N) foreach { i: Int =>
        bb(i) = pp(i) - oon
        aa(i) = i
        ff(i) = 1.0
      }

      (0 until N) foreach { i: Int =>
        bmin += 1E10
        jmin -= 1
        bmax -= 1E10
        jmax -= 1
        (0 until N) foreach { j: Int =>
          if (bmin > bb(j)) {
            bmin = bb(j)
            jmin = j
          }
          if (bmax < bb(j)) {
            bmax = bb(j)
            jmax = j
          }
        }
        if (bmax-bmin >= tol) {
          aa(jmin) = jmax
          ff(jmin) = 1 + bmin * N
          bb(jmin) = 0
          bb(jmax) = bmax + bmin
        } else {
          return
        }
      }
    }

    def sample() = {
      val n = random.nextInt(N)
      if (random.nextDouble() > ff(n)) aa(n)
      else n
    }
  }
}
